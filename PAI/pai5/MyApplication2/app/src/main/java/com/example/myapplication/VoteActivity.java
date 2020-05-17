package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class VoteActivity extends AppCompatActivity {

    private String token;
    private Votation votation;
    private TextView name;
    private TextView description;
    private CheckBox option1;
    private CheckBox option2;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Intent i = getIntent();
        votation = (Votation) i.getSerializableExtra("votation");
        token = i.getStringExtra("token");
        name = (TextView) findViewById(R.id.votation_name);
        name.setText(votation.getName());
        description = (TextView) findViewById(R.id.votation_description);
        description.setText(votation.getDescription());
        option1 = (CheckBox) findViewById(R.id.option_1);
        option1.setText(votation.getOptions().get(0));
        option2 = (CheckBox) findViewById(R.id.option_2);
        option2.setText(votation.getOptions().get(1));

        send = (Button) findViewById(R.id.send_vote);
        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    sendVote();
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
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendVote() throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        List<CheckBox> options = Arrays.asList(option1, option2);
        Boolean check = options.stream().filter(x -> x.isChecked()).count() == 1 ? true : false;
        if (check) {
            CheckBox checkBox = options.stream().filter(x -> x.isChecked()).findFirst().orElse(null);
            String userVote = checkBox.getText().toString();
            AES aes = new AES();
            String userVoteCipher = aes.encriptar(userVote + "-TRUE_PASSWORD-" + token, "password!");
            Vote vote = new Vote(token, userVoteCipher);
            checkVoteAndSend(vote);
        } else {
            Toast.makeText(getApplicationContext(), "Tu voto tiene que tener 1 opción", Toast.LENGTH_LONG).show();
        }
    }

    private void checkVoteAndSend(Vote vote) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child(("votes"));
        Query query = databaseReference.orderByChild("token").equalTo(vote.getToken());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    showInfo("Ya había votado");
                    Log.i("INSEGUS", "No puedes volver a votar");
                } else {
                    DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
                    mFirebaseDatabase.child("votes").push().setValue(vote);
                    showInfo("Voto emitido correctamente");
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showInfo(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
