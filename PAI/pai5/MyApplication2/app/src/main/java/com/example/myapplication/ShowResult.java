package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.myapplication.Object.Votation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ShowResult extends AppCompatActivity {

    private TextView name;
    private TextView description;
    private TextView option1;
    private TextView option2;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        i = getIntent();
        String votationId = i.getStringExtra("votationId");

        name = (TextView) findViewById(R.id.votation_name_recount);
        description = (TextView) findViewById(R.id.votation_description_recount);
        option1 = (TextView) findViewById(R.id.option_1_recount);
        option1.setText(i.getStringExtra("option1") + " (" + i.getStringExtra("option1Value") + " votos)");
        option2 = (TextView) findViewById(R.id.option_2);
        if (i.getStringExtra("option2").equals("/")) {
            option2.setVisibility(View.GONE);
            option2.setText("/");
        } else {
            option2.setText(i.getStringExtra("option2") + " (" + i.getStringExtra("option2Value") + " votos)");
        }

        if (votationId != null) {
            completeData(votationId);
        }
    }

    public void completeData(String votationId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child(("votations"));
        Query query = databaseReference.orderByChild("votationId").equalTo(votationId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot scannedTagFirebase : dataSnapshot.getChildren()) {
                        Votation votation = scannedTagFirebase.getValue(Votation.class);
                        name.setText(votation.getName() + "(ID: " + votationId + ")");
                        description.setText(votation.getDescription());
                        if (option2.getText().equals("/")) {
                            if (i.getStringExtra("option1").equals(votation.getOptions().get(0))) {
                                option2.setText(votation.getOptions().get(1) + " (0 votos)");
                            } else {
                                option2.setText(votation.getOptions().get(0) + " (0 votos)");
                            }
                            option2.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
