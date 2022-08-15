package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

public class ViewDefaulters extends AppCompatActivity {

    String deptName, className, divName;
    Button generatePdfBtn;
    ProgressDialog dialog;
    ArrayList<String> defaultersRollNoList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_defaulters);

        generatePdfBtn = findViewById(R.id.view_defaulters_generate_pdf_btn);
        listView = findViewById(R.id.view_defaulters_listview);

        Intent intent = getIntent();
        deptName = intent.getStringExtra("DeptName");
        className = intent.getStringExtra("ClassName");
        divName = intent.getStringExtra("DivName");

        dialog = ProgressDialog.show(this, "",
        "Fetching attendance info....", true);

        populateList();

        generatePdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

    }

    private void populateList() {

        defaultersRollNoList = new ArrayList<>();

        DatabaseReference dbAttendanceInfo = FirebaseDatabase.getInstance().getReference().child("attendance_info").child(deptName).child(className).child(divName);
        DatabaseReference dbStudentInfo = FirebaseDatabase.getInstance().getReference().child("student_info").child(deptName).child(className).child(divName);

        dbAttendanceInfo.child("lec_count").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if(task.isSuccessful() && task.getResult().exists()) {
                    String lecCount = task.getResult().getValue().toString();

                    dbStudentInfo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            defaultersRollNoList.clear();

                            for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                                String rollNo = snapshot1.getKey().toString();
                                String absentCount = snapshot1.child("absentCount").getValue().toString();
                                int presentCount = Integer.parseInt(lecCount)-Integer.parseInt(absentCount);
                                int attendancePercent = (int) Math.ceil( ( (double) presentCount / Integer.parseInt(lecCount) ) *100 );

                                if(attendancePercent < 75) {
                                    String name = snapshot1.child("fName").getValue().toString() + " " + snapshot1.child("lName").getValue().toString();
                                    defaultersRollNoList.add(rollNo + " - " + name + " (" + attendancePercent + "%)");
                                }

                            }

                            dialog.dismiss();
                            displayList();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if(!task.getResult().exists()) {
                    Toast.makeText(ViewDefaulters.this, "Invalid details were provided", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }

            }
        });

    }

    private void displayList() {

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, defaultersRollNoList);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

    }

}