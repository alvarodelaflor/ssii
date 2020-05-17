package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Objects.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import java.util.Map;



public class MainActivity extends AppCompatActivity {

    // Setup Server information
    protected static String server = "192.168.1.133";
    protected static int port = 7070;

    public byte[] CreaFirmaDigital(String s) {
        byte[] res = null;
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
            keyPairGen.initialize(2048);
            KeyPair pair = keyPairGen.generateKeyPair();

            Signature firma = Signature.getInstance("SHA256withECDSA");
            PrivateKey privatekey= pair.getPrivate();

            firma.initSign(privatekey);

            byte[] bytes = s.getBytes();

            firma.update(bytes);

            res =firma.sign();

        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Boolean VerificaFirmaDigital(String s, PublicKey key, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Boolean res = false;
        Signature firma = Signature.getInstance("SHA256withECDSA");
        firma.initVerify(key);
        firma.update(s.getBytes());
        res = firma.verify(signature);
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populate();

        // Capturamos el boton de Enviar
        View button = findViewById(R.id.button_send);

        // Llama al listener del boton Enviar
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


    }

    // Creación de un cuadro de dialogo para confirmar pedido
    private void showDialog() throws Resources.NotFoundException {
        final CheckBox camas = (CheckBox) findViewById(R.id.checkBox_camas);
        final CheckBox mesas = (CheckBox) findViewById(R.id.checkBox_mesas);
        final CheckBox sillas = (CheckBox) findViewById(R.id.checkBox_sillas);
        final CheckBox sillones = (CheckBox) findViewById(R.id.checkBox_sillones);

        List<CheckBox> buttons = Arrays.asList(camas, mesas, sillas, sillones);

        if (!checkAnyVote(buttons)) {
            // Mostramos un mensaje emergente;
            Toast.makeText(getApplicationContext(), "Selecciona al menos un elemento", Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Enviar")
                    .setMessage("Se va a proceder al envio")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                // Catch ok button and send information
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    // 1. Extraer los datos de la vista
                                    Map<String, Boolean> result = new HashMap<>();
                                    for (CheckBox elem : buttons) {
                                        result.put(elem.getText().toString(), elem.isChecked());
                                    }
                                    // 2. Firmar los datos

                                    // 3. Enviar los datos

                                    Toast.makeText(MainActivity.this, "Petición enviada correctamente", Toast.LENGTH_SHORT).show();
                                }
                            }

                    )
                    .

                            setNegativeButton(android.R.string.no, null)

                    .

                            show();
        }
    }

    private Boolean checkAnyVote(List<CheckBox> checkBoxes) {
        Boolean res = false;
        for (CheckBox elem : checkBoxes) {
            if (elem.isChecked()) {
                res = true;
                break;
            }
        }
        return  res;
    }

    private void populate() {
        DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        User user1 = new User("user1", "publicKey1", "privateKey1");
        User user2 = new User("user2", "publicKey2", "privateKey2");
        List<User> users = Arrays.asList(user1, user2);
        mFirebaseDatabase.child("users").removeValue();
        for (User elem : users) {
            mFirebaseDatabase.child("users").push().setValue(elem);
        }
    }

}
