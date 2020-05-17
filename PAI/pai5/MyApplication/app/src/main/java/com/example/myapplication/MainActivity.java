package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Objects.Purchase;
import com.example.myapplication.Objects.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class MainActivity extends AppCompatActivity {

    // Setup Server information
    protected static String server = "192.168.1.133";
    protected static int port = 7070;
    private static final int CHOOSE_FILE_REQUESTCODE = 8777;
    private static final int PICKFILE_RESULT_CODE = 8778;
    private EditText inputCamas;
    private EditText inputMesas;
    private EditText inputSillas;
    private EditText inputSillones;
    private String key = "";
    private EditText inputUsername;
    private Button buttonKey;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public byte[] CreaFirmaDigital(String key, String data) {
        byte[] res = null;
        try {
            Signature firma = Signature.getInstance("SHA256withRSA");

            byte[] encodedPrivateKey = Base64.getDecoder().decode(key);

            KeyFactory factory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(encodedPrivateKey);
            PrivateKey private_key = factory.generatePrivate(spec);

            firma.initSign(private_key);

            byte[] bytes = data.getBytes();

            firma.update(bytes);

            res = firma.sign();

        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String encrypt(String publicKey, String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA256AndMGF1Padding");
        byte[] bytes = data.getBytes();
        byte[] encodedPublicKey = Base64.getDecoder().decode(publicKey);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey public_key = factory.generatePublic(new X509EncodedKeySpec(encodedPublicKey));
        cipher.init(Cipher.ENCRYPT_MODE, public_key);
        String res = Base64.getEncoder().encodeToString(cipher.doFinal(bytes));
        return res;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<String> generatePair() throws NoSuchAlgorithmException, InvalidKeySpecException {
        List<String> res = new ArrayList<>();

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");

        keyPairGen.initialize(2048, new SecureRandom());

        KeyPair pair = keyPairGen.generateKeyPair();

        PrivateKey privatekey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        byte[] encodedPublicKey = publicKey.getEncoded();
        String b64PublicKey = Base64.getEncoder().encodeToString(encodedPublicKey);

        byte[] encodedPrivateKey = privatekey.getEncoded();
        String b64PrivateKey = Base64.getEncoder().encodeToString(encodedPrivateKey);

        res.add(b64PublicKey);
        res.add(b64PrivateKey);
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

    // And then somewhere, in your activity:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK){
            Uri content_describer = data.getData();
            BufferedReader reader = null;
            try {
                // open the user-picked file for reading:
                InputStream in = getContentResolver().openInputStream(content_describer);
                // now read the content:
                reader = new BufferedReader(new InputStreamReader(in));
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null){
                    builder.append(line);
                }
                // Do something with the content in
                key = builder.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonKey = (Button) findViewById(R.id.search_key);
        buttonKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                // Ask specifically for something that can be opened:
                chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
                chooseFile.setType("*/*");
                startActivityForResult(
                        Intent.createChooser(chooseFile, "Choose a file"),
                        PICKFILE_RESULT_CODE
                );
            }
        });

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
        inputSillones = (EditText) findViewById(R.id.inputSillones);
        inputUsername = (EditText) findViewById(R.id.input_username);

        Integer numCamas = Integer.valueOf(inputCamas.getText().toString());
        Integer numMesas = Integer.valueOf(inputMesas.getText().toString());
        Integer numSillas = Integer.valueOf(inputSillas.getText().toString());
        Integer numSillones = Integer.valueOf(inputSillones.getText().toString());
        String username = inputUsername.getText().toString();

        if (numCamas == null || numCamas < 0 || numCamas > 300) {
            Toast.makeText(getApplicationContext(), "El número de camas debe estar entre 0 y 300.", Toast.LENGTH_LONG).show();
        } else if (numMesas == null || numMesas < 0 || numMesas > 300) {
            Toast.makeText(getApplicationContext(), "El número de mesas debe estar entre 0 y 300.", Toast.LENGTH_LONG).show();
        } else if (numSillas == null || numSillas < 0 || numSillas > 300) {
            Toast.makeText(getApplicationContext(), "El número de sillas debe estar entre 0 y 300.", Toast.LENGTH_LONG).show();
        } else if (numSillones == null || numSillones < 0 || numSillones > 300) {
            Toast.makeText(getApplicationContext(), "El número de sillones debe estar entre 0 y 300.", Toast.LENGTH_LONG).show();
        } else if (key.isEmpty()) {
            Toast.makeText(getApplicationContext(), "La clave es obligatorio.", Toast.LENGTH_LONG).show();
        } else if (username == null || username.length() == 0 || username.length() > 26) {
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
        Integer numCamas = Integer.valueOf(inputCamas.getText().toString());
        Integer numMesas = Integer.valueOf(inputMesas.getText().toString());
        Integer numSillas = Integer.valueOf(inputSillas.getText().toString());
        Integer numSillones = Integer.valueOf(inputSillones.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot scannedTagFirebase : dataSnapshot.getChildren()) {
                        User user = scannedTagFirebase.getValue(User.class);
                        // Comprobamos la firma y si es correcta enviamos la compra
                        String data = "numCamas:" + numCamas + "," + "numMesas:" + numMesas + "," + "numSillas:" + numSillas + "," + "numSillones:" + numSillones;
                        byte[] sign = CreaFirmaDigital(key, data);
                        Boolean aux = null;
                        try {
                            aux = checkValidSign(user.getPublicKey(), data, sign);
                        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeySpecException | InvalidKeyException e) {
                            e.printStackTrace();
                        }
                        if (aux) {
                            try {
                                String dataEncrypt = encrypt("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkcOmzOWI1rSCYvcGFrhW0xrWgGs/8JV/gyG+32aTvTLfF/AbLqrJi+osTSg6scjvyCILOwngd09QLQVyGk/rSY8SuzLmfyHR9uO4lHIYGPZGrnyaUPLuosoCqaGkxFTk5FI7cQPyhKQYgVJxUbjeJmujU8gbmQGw64JY6xKk9/kVCu4LqdNxMDrRMoMFDdqjH9XO+go8K6O8XQEi8mQvISdCGdFRDPPfUBw7TlBYEVJSvLAZPDCO8UkvM8VYvHcvS/ziBrMjzphGt6+y9zo/+cX3w9RebwiFKqXxVZfwBTeb+8wpDKkxyrUCyX51qOeMlJz0s3NiOdlbfCRAA3FBRwIDAQAB", data);
                                createPurchaseAndPush(user, dataEncrypt, sign);
                            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
                                e.printStackTrace();
                            }
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

    public Boolean checkValidSign(String publicKey, String s, byte[] signature) throws NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException {
        Boolean res = false;
        Signature firma = Signature.getInstance("SHA256withRSA");
        byte[] encodedPublicKey = Base64.getDecoder().decode(publicKey);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey public_key = factory.generatePublic(new X509EncodedKeySpec(encodedPublicKey));
        firma.initVerify(public_key);
        firma.update(s.getBytes());
        res = firma.verify(signature);

        return res;
    }

    private void createPurchaseAndPush(User user, String data, byte[] signature) {
        String sign = Base64.getEncoder().encodeToString(signature);
        Purchase purchase = new Purchase(data, sign);
        DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabase.child("purchase").push().setValue(purchase);
        Toast.makeText(MainActivity.this, "Petición enviada correctamente", Toast.LENGTH_SHORT).show();
    }


    private void populate() throws NoSuchAlgorithmException {
        DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        User user1 = generateUser("user1");
        User user2 = generateUser("user2");
        user1.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmQj2+NijdXDD7wCTCHOrI1eUfdeuf1cF5yQ6n7iLlVf0ahRkfN6wqYpmeEjt8FNne28h8wnKGIw1EZifQK0DG5Q4LY7XjFECgGniXBbHFJZqnzGP03JpFE1E+KczESb4lUPFVNNNU9fkZNvVZVHO0XJ9XXJAPT2U/KNlyBAk+fsMlIrxlQENPSHnfAHspEHRXBN2JsX5XYx3RbqYrm6PsnY4duOG1eQypJSRQXMpzfkJJZ8DhB3c+r9vGdb6QEdQc202d34wBOjiWREc62ldQyepq2/2uz3+qMPVCFaWyxDkOyrXVwjKRCOyqNiHQQnnnIliQNbfJYajWLtL9j2p7wIDAQAB");
        user2.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxhQApw50gyzY+r60kJYvwEya3EbLsxd7+A3bVA9OySluX5lDHaarCCwkPa1JCVqSsInfO5bJEF8BzzhzinAb8DzIHJwPfoX9FlWw8XMGYYyt/lzFgBttL47aWqX6fMyXMoHo6IurzGXxAmofj5R5g21WrwlG1Up2Eb9OtqPH/FwtWQU0zudXruX+wshf3+kMkRXhONivPLaI7/0JzV+wcjg8Eoti34PYRD377DKgAIF+OEi4b5l1rfG5Yy1tMlum4vJjQW2EOS2zMraXEhQ6IGggqXPx7jBp1PRCbTJjokJ546/Y4RkRAKYR05ez1xAu98ObEk2pQn8qx2UO83EN1QIDAQAB");
        List<User> users = Arrays.asList(user1, user2);
        mFirebaseDatabase.child("users").removeValue();
        for (User elem : users) {
            mFirebaseDatabase.child("users").push().setValue(elem);
        }
        mFirebaseDatabase.child("purchase").removeValue();
    }

    private User generateUser(String userId) throws NoSuchAlgorithmException {
//        List<String> claves = generateKey();
//        String privateKey = claves.get(0);
//        String publicKey = claves.get(1);
        User res = new User(userId, "");
        return res;
    }
/*
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

 */

    private void showInfo(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
