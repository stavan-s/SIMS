package com.stavan.sims;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class AdminPage extends AppCompatActivity {

    Button logoutBtn;
    FirebaseAuth fAuth;
    ConstraintLayout addStudentBtn, removeStudentBtn, addFacultyBtn, removeFacultyBtn, studentsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        // Check permissions
        Dexter.withContext(this)
                .withPermission(Manifest.permission.SEND_SMS)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {

                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();

        logoutBtn = findViewById(R.id.logoutBtn);
        fAuth = FirebaseAuth.getInstance();
        addStudentBtn = findViewById(R.id.add_student_card);
        removeStudentBtn = findViewById(R.id.remove_student_card);
        addFacultyBtn = findViewById(R.id.add_faculty_card);
        removeFacultyBtn = findViewById(R.id.remove_faculty_card);
        studentsInfo = findViewById(R.id.stud_info_card);

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
                startActivity(new Intent(getApplicationContext(), RemoveStudentPage.class));
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
                startActivity(new Intent(getApplicationContext(), RemoveFacultyPage.class));
            }
        });

        studentsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GetStudentDetails.class);
                intent.putExtra("CallingActivity", "AdminDashboard");
                startActivity(intent);
            }
        });

    }
}