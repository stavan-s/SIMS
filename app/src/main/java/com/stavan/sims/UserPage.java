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

        setStudentDetails();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                finishAffinity();
//                Toast.makeText(UserPage.this, student.getfName(), Toast.LENGTH_SHORT).show();
            }
        });

        attendanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewAttendanceRecord.class);
                intent.putExtra("Student", student);
                startActivity(intent);
            }
        });

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

    private void setStudentDetails() {

        String uid = fAuth.getUid();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();


        db.child("account_type").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String result = String.valueOf(snapshot.getValue(String.class));
                StringTokenizer st = new StringTokenizer(result);
                String type = st.nextToken();
                String dept = st.nextToken();
                String className = st.nextToken();
                String div = st.nextToken();
                String rollNo = st.nextToken();

                db.child("student_info").child(dept).child(className).child(div).child(rollNo).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        student = snapshot.getValue(Student.class);
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

}