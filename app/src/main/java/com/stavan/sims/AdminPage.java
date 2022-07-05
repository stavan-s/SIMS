package com.stavan.sims;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AdminPage extends AppCompatActivity {

    Button logoutBtn;
    FirebaseAuth fAuth;
    ConstraintLayout addStudentBtn, removeStudentBtn, addFacultyBtn, removeFacultyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        logoutBtn = findViewById(R.id.logoutBtn);
        fAuth = FirebaseAuth.getInstance();
        addStudentBtn = findViewById(R.id.add_student_card);
        removeStudentBtn = findViewById(R.id.remove_student_card);
        addFacultyBtn = findViewById(R.id.add_faculty_card);
        removeFacultyBtn = findViewById(R.id.remove_faculty_card);

        fAuth.signOut();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                finishAffinity();
            }
        });

        addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddNewStudent.class));
            }
        });
        
        removeStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminPage.this, "Remove existing student", Toast.LENGTH_SHORT).show();
            }
        });
        
        addFacultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddNewFaculty.class));
            }
        });
        
        removeFacultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminPage.this, "Remove existing faculty", Toast.LENGTH_SHORT).show();
            }
        });
        
    }
}