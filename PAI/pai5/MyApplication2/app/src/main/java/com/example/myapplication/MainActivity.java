package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Object.UserAccess;
import com.example.myapplication.Object.Votation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private Button recountVotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Active for populate database
        populate();

        EditText userId = (EditText) findViewById(R.id.user_id);
        EditText votationId = (EditText) findViewById(R.id.votation_id);
        Button buttonSend = (Button) findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(userId, votationId);
            }
        });

        recountVotes = findViewById(R.id.recount_votes);
        recountVotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }

    private void validate(EditText userId, EditText votationId) {
        String userIdAux = userId != null && userId.getText() != null ? userId.getText().toString() : null;
        String votationIdAux = votationId != null && votationId.getText() != null ? votationId.getText().toString() : null;
        if (userIdAux != null && votationIdAux != null) {
            Toast.makeText(this, "Comprobando parámetros", Toast.LENGTH_SHORT).show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference().child(("users"));
            Query query = databaseReference.orderByChild("userId").equalTo(userIdAux);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot scannedTagFirebase : dataSnapshot.getChildren()) {
                            UserAccess userAccess = scannedTagFirebase.getValue(UserAccess.class);
                            if (userAccess.getVoteId().equals(votationIdAux)) {
                                Log.i("INSEGUS", "Usuario con acceso");
                                String token = userAccess.getToken();
                                startUserVotation(token, votationIdAux);
                            } else {
                                showInfo("Usuario sin acceso");
                                Log.i("INSEGUS", "Usuario sin acceso");
                            }
                        }
                    } else {
                        showInfo("No existe ese usuario");
                        Log.i("INSEGUS", "Usuario inexistente");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(this, "Algún parámetro es incorrecto", Toast.LENGTH_LONG).show();
        }
    }

    private void startUserVotation(String token, String votationIdAux) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child(("votations"));
        Query query = databaseReference.orderByChild("votationId").equalTo(votationIdAux);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot scannedTagFirebase : dataSnapshot.getChildren()) {
                        Votation votation = scannedTagFirebase.getValue(Votation.class);
                        Intent intent = new Intent(getApplicationContext(), VoteActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("votation", votation);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                    }
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


    private void populate() {
        DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        UserAccess user1 = new UserAccess("alvdebon", "votation1", "oasfoajdfoaijdejiewofmoweijiwoefoiewo");
        UserAccess user2 = new UserAccess("carfenben", "votation1", "afdasdfaeiwufjiewojfoiejofiwjeoifjwoe");
        UserAccess user3 = new UserAccess("antsalper4", "votation1", "fowierfjweiofoiwejifwoejiwojfoiwjefoi");
        UserAccess user4 = new UserAccess("random", "votation2", "oaiwdjfoweijwoiejfoiwejadofjaoewfjoia");
        List<UserAccess> users = Arrays.asList(user1, user2, user3, user4);
        mFirebaseDatabase.child("users").removeValue();
        for (UserAccess elem : users) {
            mFirebaseDatabase.child("users").push().setValue(elem);
        }
        mFirebaseDatabase.child("votations").removeValue();
        Votation votation1 = new Votation("Retraso de la feria", "En esta votación tiene que decidir si retrasar la feria a septiembre", Arrays.asList("Sí", "No"), "votation1");
        List<Votation> votations = Arrays.asList(votation1);
        for (Votation elem: votations) {
            mFirebaseDatabase.child("votations").push().setValue(elem);
        }
        mFirebaseDatabase.child("votes").removeValue();
    }

}
