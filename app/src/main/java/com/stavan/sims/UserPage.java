package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class UserPage extends AppCompatActivity {

    TextView display;
    Button logoutBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        logoutBtn = findViewById(R.id.logoutBtn);
        display = findViewById(R.id.display);
        fAuth = FirebaseAuth.getInstance();

        setName();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                finishAffinity();
            }
        });

    }

    private void setName() {

        String uid = fAuth.getUid();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("account_type").child(uid);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String result = String.valueOf(snapshot.getValue(String.class));
                StringTokenizer st = new StringTokenizer(result);
                String type = st.nextToken();
                String dept = st.nextToken();
                String className = st.nextToken();
                String div = st.nextToken();
                String rollNo = st.nextToken();

                DatabaseReference db1 = FirebaseDatabase.getInstance().getReference();
                db1.child("student_info").child(dept).child(className).child(div).child(rollNo).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()) {
//                            Toast.makeText(UserPage.this, String.valueOf(task.getResult().getValue()), Toast.LENGTH_SHORT).show();
                            Log.d("ResultDisplay", String.valueOf(task.getResult().getValue()));
                            DataSnapshot snapshot1 = task.getResult();
                            String name = String.valueOf(snapshot1.child("fName").getValue());
                            Log.d("NameOfStudent", name);
                            display.setText("Welcome " + name);
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}