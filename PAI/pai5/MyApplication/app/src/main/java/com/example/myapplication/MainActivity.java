package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.myapplication.Objects.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

    private EditText inputCamas;
    private EditText inputMesas;
    private EditText inputSillas;
    private EditText inputSillones;
    private EditText inputKey;

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

        try {
            populate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

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

        inputCamas = (EditText) findViewById(R.id.inputCamas);
        inputMesas = (EditText) findViewById(R.id.inputMesas);
        inputSillas = (EditText) findViewById(R.id.inputSillas);
        inputSillones= (EditText) findViewById(R.id.inputSillones);
        inputKey= (EditText) findViewById(R.id.inputKey);

        Integer numCamas = Integer.valueOf(inputCamas.getText().toString());
        Integer numMesas = Integer.valueOf(inputMesas.getText().toString());
        Integer numSillas = Integer.valueOf(inputSillas.getText().toString());
        Integer numSillones = Integer.valueOf(inputSillones.getText().toString());
        String key = inputKey.getText().toString();

        if (numCamas > 300) {
            Toast.makeText(getApplicationContext(), "El número máximo de camas es 300.", Toast.LENGTH_LONG).show();
        } else if (numMesas > 300) {
            Toast.makeText(getApplicationContext(), "El número máximo de mesas es 300.", Toast.LENGTH_LONG).show();
        }else if (numSillas > 300) {
            Toast.makeText(getApplicationContext(), "El número máximo de sillas es 300.", Toast.LENGTH_LONG).show();
        } else if (numSillones > 300) {
            Toast.makeText(getApplicationContext(), "El número máximo de sillones es 300.", Toast.LENGTH_LONG).show();
        }else if (key.isEmpty()) {
            Toast.makeText(getApplicationContext(), "La clave es obligatorio.", Toast.LENGTH_LONG).show();
        } else {
            new AlertDialog.Builder(this)
                .setTitle("Enviar")
                .setMessage("Se va a proceder al envio")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        // COMPROBAR FIRMA
                        checkSign();
//                        Toast.makeText(MainActivity.this, "Petición enviada correctamente", Toast.LENGTH_SHORT).show();
                    }
                }
                )
                .
                setNegativeButton(android.R.string.no, null)
                .
                show();
        }
    }

    private void checkSign() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child(("users"));
        Query query = databaseReference.orderByChild("privateKey").equalTo(inputKey.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot scannedTagFirebase : dataSnapshot.getChildren()) {
                        User user = scannedTagFirebase.getValue(User.class);
                        Toast.makeText(MainActivity.this, "Petición enviada correctamente", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showInfo("No existen ningún usuario con esa clave");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void populate() throws NoSuchAlgorithmException {
        DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        User user1 = generateUser("user1");
        User user2 = generateUser("user2");
        user2.setPrivateKey("3");
        user2.setPublicKey("4");
        List<User> users = Arrays.asList(user1, user2);
        mFirebaseDatabase.child("users").removeValue();
        for (User elem : users) {
            mFirebaseDatabase.child("users").push().setValue(elem);
        }
    }

    private User generateUser(String userId) throws NoSuchAlgorithmException {
        List<String> claves = generateKey();
        String privateKey = claves.get(0);
        String publicKey = claves.get(1);
        User res = new User(userId, publicKey, privateKey);
        return res;
    }

    private List<String> generateKey() throws NoSuchAlgorithmException {
        List<String> res = new ArrayList<>();
        KeyPairGenerator generadorRSA = KeyPairGenerator.getInstance("RSA");
        generadorRSA.initialize(1024);
        KeyPair claves = generadorRSA.genKeyPair();
//        res.add(claves.getPrivate().toString());
//        res.add(claves.getPublic().toString());
        res.add("1");
        res.add("2");
        return res;
    }

    private void showInfo(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
