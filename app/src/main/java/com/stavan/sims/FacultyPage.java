package com.stavan.sims;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class FacultyPage extends AppCompatActivity {

    Button logoutBtn;
    FirebaseAuth fAuth;
    ConstraintLayout addLectureCard, doubtsCard, uploadCard, defaultersCard, studentsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_page);

        addLectureCard = findViewById(R.id.add_lecture_card);
        doubtsCard = findViewById(R.id.doubts_card);
        uploadCard = findViewById(R.id.upload_resources_card);
        defaultersCard = findViewById(R.id.defaulters_list_card);
        studentsInfo = findViewById(R.id.stud_info_card);
        logoutBtn = findViewById(R.id.logoutBtn);
        fAuth = FirebaseAuth.getInstance();

        // logout button
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                finishAffinity();
            }
        });


        // click to add a new lecture, to further mark the attendance
        addLectureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddLecture.class));
            }
        });


        // click to view student's doubts of any particular lecture
        doubtsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewDoubts.class));
            }
        });

        uploadCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LectureDetails.class);
                intent.putExtra("CallingActivity", "FacultyDashboard");
                startActivity(intent);
            }
        });

        defaultersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DefaultersInfoInput.class));
            }
        });

        studentsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GetStudentDetails.class);
                intent.putExtra("CallingActivity", "FacultyDashboard");
                startActivity(intent);
            }
        });

    }
}