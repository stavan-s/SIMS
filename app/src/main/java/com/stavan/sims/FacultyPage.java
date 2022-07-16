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
    ConstraintLayout addLectureCard, doubtsCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_page);

        addLectureCard = findViewById(R.id.add_lecture_card);
        doubtsCard = findViewById(R.id.doubts_card);
        logoutBtn = findViewById(R.id.logoutBtn);
        fAuth = FirebaseAuth.getInstance();


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                finishAffinity();
            }
        });


        addLectureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddLecture.class));
            }
        });


        doubtsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewDoubts.class));
            }
        });

    }
}