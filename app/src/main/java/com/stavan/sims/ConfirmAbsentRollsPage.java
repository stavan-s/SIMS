package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.StringTokenizer;

public class ConfirmAbsentRollsPage extends AppCompatActivity {

    ListView listView;
    Button submitBtn;
    String absentRollNosString;
    String deptName, className, divName, lectureName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_absent_rolls_page);

        Intent intent = getIntent();
        deptName = intent.getStringExtra("DeptName").toString();
        className = intent.getStringExtra("ClassName").toString();
        divName = intent.getStringExtra("DivName").toString();
        lectureName = intent.getStringExtra("LecName").toString();
        ArrayList<String> absentRollNos = intent.getStringArrayListExtra("absentRollNos");

        listView = findViewById(R.id.confirm_listview);
        submitBtn = findViewById(R.id.confirm_absentees_submit_btn);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, absentRollNos);
        listView.setAdapter(arrayAdapter);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                absentRollNosString = "";
                for (int i = 0; i < absentRollNos.size(); i++) {
                    StringTokenizer stringTokenizer = new StringTokenizer(absentRollNos.get(i));
                    absentRollNosString += stringTokenizer.nextToken() + " ";
                }

                if (absentRollNosString.isEmpty()) {
                    absentRollNosString = "null";
                }

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.child("lecture_info")
                        .child(deptName)
                        .child(className)
                        .child(divName)
                        .child(lectureName)
                        .child(Misc.getDate())
                        .child("absentees")
                        .setValue(absentRollNosString).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!absentRollNosString.equals("null")) {
                                    // informAbsentees(absentRollNosString);
                                    addAbsentCount(absentRollNosString);
                                }
                                startActivity(new Intent(getApplicationContext(), FacultyPage.class));
                                finishAffinity();
                            }
                        });

            }
        });
    }

    private void addAbsentCount(String absentRollNosString) {

        ArrayList<String> absentRollNosList = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(absentRollNosString);
        while(st.hasMoreTokens()) {
            String rollNo = st.nextToken();
            absentRollNosList.add(rollNo);
        }

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("student_info").child(deptName).child(className).child(divName);
        for(int i = 0; i<absentRollNosList.size(); i++) {
            String rollNo = absentRollNosList.get(i);
            db.child(rollNo).child("lec_absentees").child(lectureName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful() && task.getResult().exists()) {
                        String count = task.getResult().getValue().toString();
                        int c = Integer.parseInt(count);
                        db.child(rollNo).child("lec_absentees").child(lectureName).setValue(String.valueOf(c+1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful()) {
                                    Toast.makeText(ConfirmAbsentRollsPage.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        db.child(rollNo).child("lec_absentees").child(lectureName).setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful()) {
                                    Toast.makeText(ConfirmAbsentRollsPage.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void informAbsentees(String absentRollNos) {

        ArrayList<String> rollNos = new ArrayList<>();

        StringTokenizer stringTokenizer = new StringTokenizer(absentRollNos);
        while (stringTokenizer.hasMoreTokens()) {
            rollNos.add(stringTokenizer.nextToken());
        }

        for (int i = 0; i < rollNos.size(); i++) {

            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            db.child("student_info")
                    .child(deptName)
                    .child(className)
                    .child(divName)
                    .child(rollNos.get(i))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("fName").getValue().toString()
                                    + " " + snapshot.child("mName").getValue().toString()
                                    + " " + snapshot.child("lName").getValue().toString();
                            String ownNumber = snapshot.child("ownNumber").getValue().toString();
                            String parentNumber = snapshot.child("ownNumber").getValue().toString();

                            String ownNumberMsg = Misc.getOwnNumberMsg(name, lectureName);
                            String parentNumberMsg = Misc.getParentNumberMsg(name, lectureName);

                            Misc.sendSMS(ownNumber, ownNumberMsg);
                            Misc.sendSMS(parentNumber, parentNumberMsg);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }
    }
}


