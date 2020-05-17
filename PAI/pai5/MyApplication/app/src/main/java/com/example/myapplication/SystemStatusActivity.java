package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.Objects.AccessLog;
import com.example.myapplication.Objects.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SystemStatusActivity extends AppCompatActivity {

    private TextView ratio0Before;
    private TextView ratio1Before;
    private TextView ratio2Before;
    private TextView tendencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_status);

        ratio0Before = (TextView) findViewById(R.id.ratio_cero_before);
        ratio1Before = (TextView) findViewById(R.id.ratio_one_before);
        ratio2Before = (TextView) findViewById(R.id.ratio_two_before);
        tendencia = (TextView) findViewById(R.id.tendencia);

        getRatio();
    }

    private void getRatio() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child(("logs"));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<AccessLog> accessLogList = new ArrayList<>();
                for (DataSnapshot scannedTagFirebase : dataSnapshot.getChildren()) {
                    AccessLog accessLog = scannedTagFirebase.getValue(AccessLog.class);
                    accessLogList.add(accessLog);
                }
                checkRatio(accessLogList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkRatio(List<AccessLog> accessLogList) {
        Date actualDate = new Date();

        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.MONTH, -1);
        Date oneMonthBeforeDate = cal1.getTime();

        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.MONTH, -2);
        Date twoMonthBeforeDate = cal2.getTime();

        List<AccessLog> thisMonthAccess = accessLogList.stream()
                .filter(x -> x.getDate().after(oneMonthBeforeDate))
                .collect(Collectors.toList());

        if (thisMonthAccess.size() > 0) {
            Long trueAccess = thisMonthAccess.stream().filter(x -> x.getStatus().equals(true)).count();
            Double ratio0 = (trueAccess * 1.) / thisMonthAccess.size();
            ratio0Before.setText(ratio0.toString());
        } else {
            ratio0Before.setText("1.0");
        }

        List<AccessLog> oneMonthBeforeAccess = accessLogList.stream()
                .filter(x -> x.getDate().after(twoMonthBeforeDate) && x.getDate().before(oneMonthBeforeDate))
                .collect(Collectors.toList());

        if (oneMonthBeforeAccess.size() > 0) {
            Long trueAccess = oneMonthBeforeAccess.stream().filter(x -> x.getStatus().equals(true)).count();
            Double ratio1 = (trueAccess * 1.) / oneMonthBeforeAccess.size();
            ratio1Before.setText(ratio1.toString());
        } else {
            ratio0Before.setText("1.0");
        }

        List<AccessLog> twoMonthBeforeAccess = accessLogList.stream()
                .filter(x -> x.getDate().before(twoMonthBeforeDate))
                .collect(Collectors.toList());

        if (twoMonthBeforeAccess.size() > 0) {
            Long trueAccess = twoMonthBeforeAccess.stream().filter(x -> x.getStatus().equals(true)).count();
            Double ratio2 = (trueAccess * 1.) / twoMonthBeforeAccess.size();
            ratio2Before.setText(ratio2.toString());
        } else {
            ratio0Before.setText("1.0");
        }

        Double actual = Double.valueOf(ratio0Before.getText().toString());
        Double one = Double.valueOf(ratio1Before.getText().toString());
        Double two = Double.valueOf(ratio2Before.getText().toString());

        if ((actual >= one && actual > two) || (actual > one && actual >= two)) {
            tendencia.setText("TENDENCIA POSITIVA");
            tendencia.setTextColor(Color.GREEN);
        } else if (actual < one || actual < two) {
            tendencia.setText("TENDENCIA NEGATIVA");
            tendencia.setTextColor(Color.RED);
        } else if (actual == one && actual == two) {
            tendencia.setText("TENDENCIA NULA");
            tendencia.setTextColor(Color.BLUE);
        }
    }
}
