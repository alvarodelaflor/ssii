package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Object.UserAccess;
import com.example.myapplication.Object.Votation;
import com.example.myapplication.Object.Vote;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RecountActivity extends AppCompatActivity {

    private EditText votationId;
    private EditText votationPassword;
    private Button recount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recount);

        votationId = (EditText) findViewById(R.id.votation_id_recount);
        votationPassword = (EditText) findViewById(R.id.votation_password);
        recount = (Button) findViewById(R.id.button_send_recount);
        recount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
        if (votationId != null && votationPassword != null && votationId.getText() != null && votationPassword.getText() != null) {
            String votationIdAux = votationId.getText().toString();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference().child(("users"));
            Query query = databaseReference.orderByChild("voteId").equalTo(votationIdAux);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        List<String> tokens = new ArrayList<>();
                        for (DataSnapshot scannedTagFirebase : dataSnapshot.getChildren()) {
                            UserAccess userAccess = scannedTagFirebase.getValue(UserAccess.class);
                            tokens.add(userAccess.getToken());
                        }
                        if (tokens.size() > 0) {
                            searchVotes(tokens);
                        } else {
                            showInfo("No existe ningún votante en esa votación.");
                        }
                    } else {
                        showInfo("No existe ningún votante en esa votación");
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            showInfo("Los datos introducidos son incorrectos");
        }

    }

    private void searchVotes(List<String> tokens) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child(("votes"));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> cipherVotes = new ArrayList<>();
                for (DataSnapshot elem : dataSnapshot.getChildren()) {
                    Vote vote = elem.getValue(Vote.class);
                    if (tokens.contains(vote.getToken()) && !cipherVotes.contains(vote.getVotation())) {
                        cipherVotes.add(vote.getVotation());
                    }
                }
                if (cipherVotes.size() > 0) {
                    try {
                        decipherVotes(cipherVotes);
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void decipherVotes(List<String> cipherVotes) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        AES aes = new AES();
        List<String> decipherVotes = new ArrayList<>();
        for (String elem : cipherVotes) {
            String aux = aes.desencriptar(elem, "password!");
            if (aux.contains("-TRUE_PASSWORD-")) {
                decipherVotes.add(aux.split("-TRUE_PASSWORD-")[0]);
            } else {
                showInfo("Contraseña incorrecta");
            }
            if (decipherVotes.size() > 0) {
                showInfo("Iniciando recuento");
                initRecount(decipherVotes);
            }
        }
    }

    private void initRecount(List<String> decipherVotes) {
        List<String> repeatVotes =
                decipherVotes
                        .stream()
                        // agrupar por cancion.
                        .collect(Collectors.groupingBy(s -> s))
                        .entrySet()
                        .stream()
                        .map(e -> e.getKey())
                        .collect(Collectors.toList());
        Map<String, Long> result = new HashMap<>();
        switch (repeatVotes.size()) {
            case 1:
                result.put(repeatVotes.get(0), decipherVotes.stream().filter(x -> x.equals(repeatVotes.get(0))).count());
                break;
            case 2:
                result.put(repeatVotes.get(0), decipherVotes.stream().filter(x -> x.equals(repeatVotes.get(0))).count());
                result.put(repeatVotes.get(1), decipherVotes.stream().filter(x -> x.equals(repeatVotes.get(1))).count());
                break;
            default:
                showInfo("No se ha encontrado ningún voto");
                break;
        }
        if (result.values().size() > 0) {
            Intent intent = new Intent(getApplicationContext(), ShowResult.class);
            intent.putExtra("option1", repeatVotes.get(0));
            intent.putExtra("option1Value", result.get(repeatVotes.get(0)).toString());
            if (result.size() > 1) {
                intent.putExtra("option2", repeatVotes.get(1));
                intent.putExtra("option2Value", result.get(repeatVotes.get(1)).toString());
            } else {
                intent.putExtra("option2", "/");
                intent.putExtra("option2Value", "0");
            }
            intent.putExtra("votationId", votationId.getText().toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        } else {
            showInfo("No se ha encontrado ningún voto");
        }
    }

    private void showInfo(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

}
