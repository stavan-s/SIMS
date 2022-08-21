package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

    ConstraintLayout attendanceCard, doubtsCard;
    Button logoutBtn;
    FirebaseAuth fAuth;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        attendanceCard = findViewById(R.id.student_page_attendance_card);
        doubtsCard = findViewById(R.id.student_page_doubts_card);
        logoutBtn = findViewById(R.id.logoutBtn);
        fAuth = FirebaseAuth.getInstance();

        // get logged in student's details in the student object for further reference
        setStudentDetails();

        // logout current user(student) on button click
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                finishAffinity();
            }
        });


        // click to view attendance of current student
        attendanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewAttendanceRecord.class);
                intent.putExtra("Student", student);
                startActivity(intent);
            }
        });


        // click to post doubts
        doubtsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectDateForDoubtsPage.class);
                intent.putExtra("Name", student.getfName() + " " + student.getlName());
                intent.putExtra("DeptName", student.getDepartment());
                intent.putExtra("ClassName", student.getClassName());
                intent.putExtra("DivName", student.getDiv());
                intent.putExtra("NavigateTo", "DoubtPageStudents");
                startActivity(intent);
            }
        });

    }


    // function that is used to fetch student details from db, and encapsulate them in an object of Student class
    private void setStudentDetails() {

        String uid = fAuth.getUid();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.child("account_type").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    String deptName = task.getResult().child("dept_name").getValue().toString();
                    String className = task.getResult().child("class_name").getValue().toString();
                    String divName = task.getResult().child("div_name").getValue().toString();
                    String rollNo = task.getResult().child("roll_no").getValue().toString();

                    db.child("student_info").child(deptName).child(className).child(divName).child(rollNo).get()
                            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    student = task.getResult().getValue(Student.class);
                                }
                            });
                }
            }
        });
    }

}