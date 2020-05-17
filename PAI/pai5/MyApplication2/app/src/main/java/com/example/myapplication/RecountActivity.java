package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
            
        } else {
            showInfo("Los datos introducidos son incorrectos");
        }
    }

    private void showInfo(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
