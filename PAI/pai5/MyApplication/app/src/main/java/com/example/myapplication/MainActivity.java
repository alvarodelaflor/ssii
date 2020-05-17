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

import com.example.myapplication.Objects.Purchase;
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
import java.util.Date;
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
    private EditText inputUsername;

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
        inputUsername = (EditText) findViewById(R.id.input_username);

        Integer numCamas = Integer.valueOf(inputCamas.getText().toString());
        Integer numMesas = Integer.valueOf(inputMesas.getText().toString());
        Integer numSillas = Integer.valueOf(inputSillas.getText().toString());
        Integer numSillones = Integer.valueOf(inputSillones.getText().toString());
        String key = inputKey.getText().toString();
        String username = inputUsername.getText().toString();

        if (numCamas == null || numCamas < 0 || numCamas > 300) {
            Toast.makeText(getApplicationContext(), "El número de camas debe estar entre 0 y 300.", Toast.LENGTH_LONG).show();
        } else if (numMesas == null || numMesas < 0 || numMesas > 300) {
            Toast.makeText(getApplicationContext(), "El número de mesas debe estar entre 0 y 300.", Toast.LENGTH_LONG).show();
        }else if (numSillas == null || numSillas < 0 || numSillas > 300) {
            Toast.makeText(getApplicationContext(), "El número de sillas debe estar entre 0 y 300.", Toast.LENGTH_LONG).show();
        } else if (numSillones == null || numSillones < 0 || numSillones > 300) {
            Toast.makeText(getApplicationContext(), "El número de sillones debe estar entre 0 y 300.", Toast.LENGTH_LONG).show();
        }else if (key.isEmpty()) {
            Toast.makeText(getApplicationContext(), "La clave es obligatorio.", Toast.LENGTH_LONG).show();
        } else if (username==null || username.length() == 0 || username.length() > 26) {
            Toast.makeText(getApplicationContext(), "La longitud de nombre de usuario tiene que estar entre 0 y 26 caracteres", Toast.LENGTH_LONG).show();
        } else {
            new AlertDialog.Builder(this)
                .setTitle("Enviar")
                .setMessage("Se va a proceder al envio")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // En este metodo busco el usuario veo su clave pública y miro si es correcta
                        checkSign();
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
        Query query = databaseReference.orderByChild("id").equalTo(inputUsername.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot scannedTagFirebase : dataSnapshot.getChildren()) {
                        User user = scannedTagFirebase.getValue(User.class);
                        // Comprobamos la firma y si es correcta enviamos la compra
                        Boolean aux = checkValidSign(user.getPublicKey(), inputKey.getText().toString());
                        if (aux) {
                            createPurchaseAndPush(user);
                        } else {
                            showInfo("La clave es incorrecta");
                        }
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

    public Boolean checkValidSign(String publicKey, String privateKey) {
        Boolean res = true;

        return res;
    }

    private void createPurchaseAndPush(User user) {
            Integer numCamas = Integer.valueOf(inputCamas.getText().toString());
            Integer numMesas = Integer.valueOf(inputMesas.getText().toString());
            Integer numSillas = Integer.valueOf(inputSillas.getText().toString());
            Integer numSillones = Integer.valueOf(inputSillones.getText().toString());
            Purchase purchase = new Purchase(numCamas, numMesas, numSillas, numSillones, user.getId(), new Date());
            DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
            mFirebaseDatabase.child("purchase").push().setValue(purchase);
            Toast.makeText(MainActivity.this, "Petición enviada correctamente", Toast.LENGTH_SHORT).show();
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
        mFirebaseDatabase.child("purchase").removeValue();
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
