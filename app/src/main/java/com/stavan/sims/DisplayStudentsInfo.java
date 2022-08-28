package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DisplayStudentsInfo extends AppCompatActivity {

    TextView studName, studClass, studDept, studRoll, studOwnNumber, studParentNumber, studPass;
    ListView listView;
    Student student;
    ArrayAdapter adapter;
    ArrayList<String> attendanceSummary = new ArrayList<>();

    ArrayList<String> totalLecNames, studentLecNames;
    ArrayList<Integer> totalLecCount, studentLecCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_students_info);

        student = (Student) getIntent().getSerializableExtra("StudentObject");
        String callingActivity = getIntent().getStringExtra("CallingActivity");

        studName = findViewById(R.id.stud_name);
        studClass = findViewById(R.id.stud_class);
        studDept = findViewById(R.id.stud_dept);
        studRoll = findViewById(R.id.stud_roll);
        studOwnNumber = findViewById(R.id.stud_own_number);
        studParentNumber = findViewById(R.id.stud_parent_number);
        studPass = findViewById(R.id.stud_pass);
        listView = findViewById(R.id.display_students_info_listview);

        String fullName = student.getfName() + " " + student.getmName() + " " + student.getlName();
        String deptName = student.getDepartment();
        String classDivName = student.getClassName() + " / " + student.getDiv();
        String rollNo = student.getRollNo();
        String ownNumber = student.getOwnNumber();
        String parentNumber = student.getParentNumber();

        studName.setText(fullName);
        studClass.setText(classDivName);
        studDept.setText(deptName);
        studRoll.setText(rollNo);
        studOwnNumber.setText(ownNumber);
        studParentNumber.setText(parentNumber);

        if(callingActivity.equals("AdminDashboard")) {
            String pass = student.getInitialPass();
            studPass.setText(pass);
        }
        else {
            findViewById(R.id.studPassDisplay).setVisibility(View.GONE);
            studPass.setVisibility(View.GONE);
        }

        populateList();

        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, attendanceSummary);
        listView.setAdapter(adapter);
    }

    private void populateList() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        totalLecNames = new ArrayList<>();
        totalLecCount = new ArrayList<>();
        studentLecNames = new ArrayList<>();
        studentLecCount = new ArrayList<>();

        db.child("lecture_info")
                .child(student.getDepartment())
                .child(student.getClassName())
                .child(student.getDiv())
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        for(DataSnapshot lecture : task.getResult().getChildren()) {
                            if(lecture.child("lec_count").exists()) {
                                totalLecNames.add(lecture.getKey().toString());
                                totalLecCount.add(Integer.parseInt(lecture.child("lec_count").getValue().toString()));
                            }
                        }

                        db.child("student_info")
                                .child(student.getDepartment())
                                .child(student.getClassName())
                                .child(student.getDiv())
                                .child(student.getRollNo())
                                .child("lec_absentees")
                                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                                        attendanceSummary.clear();

                                        for(DataSnapshot lecture : task.getResult().getChildren()) {
                                            studentLecNames.add(lecture.getKey().toString());
                                            studentLecCount.add(Integer.parseInt(lecture.getValue().toString()));
                                        }

                                        // =============================================================

                                        for(int i = 0; i<totalLecNames.size(); i++) {
                                            if(!studentLecNames.toString().contains(totalLecNames.get(i))) {
                                                studentLecNames.add(i, totalLecNames.get(i));
                                                studentLecCount.add(i, 0);
                                            }
                                        }

                                        // =============================================================


                                        for(int i = 0; i<studentLecNames.size(); i++) {

                                            String lecName = studentLecNames.get(i);
                                            int studLecCount = studentLecCount.get(i);
                                            int allLecCount = totalLecCount.get(i);

                                            String entry = lecName + " : " + (allLecCount - studLecCount) + "/" + allLecCount;

                                            attendanceSummary.add(entry);

                                        }

                                        adapter.notifyDataSetChanged();

                                    }
                                });

                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}