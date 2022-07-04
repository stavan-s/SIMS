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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
        submitBtn = findViewById(R.id.confirm_submit_btn);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, absentRollNos);
        listView.setAdapter(arrayAdapter);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                absentRollNosString = "";
                for(int i = 0; i<absentRollNos.size(); i++) {
                    StringTokenizer stringTokenizer = new StringTokenizer(absentRollNos.get(i));
                    absentRollNosString += stringTokenizer.nextToken() + " ";
                }

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.child("attendance_info")
                        .child(deptName)
                        .child(className)
                        .child(divName)
                        .child(Misc.getDate())
                        .child(lectureName)
                        .child("absentees")
                        .setValue(absentRollNosString).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                informAbsentees(absentRollNosString);
                                startActivity(new Intent(getApplicationContext(), FacultyPage.class));
                                finishAffinity();
                            }
                        });

            }
        });

    }

    private void informAbsentees(String absentRollNos) {

        ArrayList<String> rollNos = new ArrayList<>();

        StringTokenizer stringTokenizer = new StringTokenizer(absentRollNos);
        while(stringTokenizer.hasMoreTokens()) {
            rollNos.add(stringTokenizer.nextToken());
        }

        for(int i = 0; i<rollNos.size(); i++) {

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

                            Toast.makeText(ConfirmAbsentRollsPage.this, ownNumberMsg, Toast.LENGTH_LONG).show();
                            Toast.makeText(ConfirmAbsentRollsPage.this, parentNumberMsg, Toast.LENGTH_LONG).show();

//                            Misc.sendSMS(ownNumber, ownNumberMsg);
//                            Misc.sendSMS(parentNumber, parentNumberMsg);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }

    }

}