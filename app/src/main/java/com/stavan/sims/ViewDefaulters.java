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
    ProgressDialog dialog;
    ArrayList<String> defaultersRollNoList;
    ListView listView;
    int lecCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_defaulters);

        listView = findViewById(R.id.view_defaulters_listview);

        Intent intent = getIntent();
        deptName = intent.getStringExtra("DeptName");
        className = intent.getStringExtra("ClassName");
        divName = intent.getStringExtra("DivName");

        dialog = ProgressDialog.show(this, "",
        "Fetching attendance info....", true);

        populateList();

    }

    private void populateList() {

        defaultersRollNoList = new ArrayList<>();

        DatabaseReference dbAttendanceInfo = FirebaseDatabase.getInstance().getReference().child("lecture_info").child(deptName).child(className).child(divName);
        DatabaseReference dbStudentInfo = FirebaseDatabase.getInstance().getReference().child("student_info").child(deptName).child(className).child(divName);

        dbAttendanceInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot lecture : snapshot.getChildren()) {

                    String dbLecCount = lecture.child("lec_count").getValue().toString();
                    lecCount += Integer.parseInt(dbLecCount);

                }


                    dbStudentInfo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            defaultersRollNoList.clear();

                            for(DataSnapshot rollNo : snapshot.getChildren()) {

                                String roll = rollNo.getKey().toString();

                                int absentCount = 0;

                                for(DataSnapshot lecAbsentees : rollNo.child("lec_absentees").getChildren()) {

                                    absentCount += Integer.parseInt(lecAbsentees.getValue().toString());

                                }

                                int presentCount = Integer.parseInt(String.valueOf(lecCount))-Integer.parseInt(String.valueOf(absentCount));
                                int attendancePercent = (int) Math.ceil( ( (double) presentCount / Integer.parseInt(String.valueOf(lecCount)) ) *100 );

                                if(attendancePercent < 75) {
                                    String name = rollNo.child("fName").getValue().toString() + " " + rollNo.child("lName").getValue().toString();
                                    defaultersRollNoList.add(roll + " - " + name + " (" + attendancePercent + "%)");
                                }


                            }

                            displayList();
                            dialog.dismiss();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void displayList() {

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, defaultersRollNoList);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

    }

}