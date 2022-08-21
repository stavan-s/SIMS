package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.StringTokenizer;

public class AddLecture extends AppCompatActivity {

    EditText deptInput, classInput, divInput, lectureInput;
    Button createBtn;
    String deptName, className, divName, lectureName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture);


        deptInput = findViewById(R.id.add_lec_dept_name_input);
        classInput = findViewById(R.id.add_lec_class_input);
        divInput = findViewById(R.id.add_lec_div_input);
        lectureInput = findViewById(R.id.add_lec_lecture_input);
        createBtn = findViewById(R.id.add_lec_create_btn);


        // click to create a new lecture of the specified class
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deptName = deptInput.getText().toString().toUpperCase().trim();
                className = classInput.getText().toString().toUpperCase().trim();
                divName = divInput.getText().toString().toUpperCase().trim();
                lectureName = lectureInput.getText().toString().toUpperCase().trim();

                if(deptName.isEmpty()) {
                    deptInput.setError("Required");
                    return;
                }

                if(className.isEmpty()) {
                    classInput.setError("Required");
                    return;
                }

                if(divName.isEmpty()) {
                    divInput.setError("Required");
                    return;
                }

                if(lectureName.isEmpty()) {
                    lectureInput.setError("Required");
                    return;
                }

                deptName = deptName.toUpperCase();
                className = className.toUpperCase();
                divName = divName.toUpperCase();
                lectureName = lectureName.toUpperCase();

                validateInput();

            }
        });

    }


    // function to add the lecture's entry in the db, and navigate to AttendancePage
    private void addLecture(String deptName, String className, String divName, String lectureName, String date) {

        increaseLecCount(deptName, className, divName);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.child("attendance_info")
                .child(deptName)
                .child(className)
                .child(divName)
                .child(date)
                .child(lectureName)
                .child("absentees")
                .setValue("null").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), AttendancePage.class);
                            intent.putExtra("DeptName", deptName);
                            intent.putExtra("ClassName", className);
                            intent.putExtra("DivName", divName);
                            intent.putExtra("LecName", lectureName);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(AddLecture.this, "Error while adding a lecture", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        
    }


    private void increaseLecCount(String deptName, String className, String divName) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("attendance_info").child(deptName).child(className).child(divName);
        db.child("lec_count")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful() && task.getResult().exists()) {
                            String count = task.getResult().getValue().toString();
                            if(count.equals("null"))
                                count = "0";
                            int c = Integer.parseInt(count);
                            
                            db.child("lec_count")
                                    .setValue(String.valueOf(c+1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(!task.isSuccessful()) {
                                                Toast.makeText(AddLecture.this, "Error occurred", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            
                        }
                        else if(!task.getResult().exists()) {
                            db.child("lec_count")
                                    .setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(!task.isSuccessful()) {
                                                Toast.makeText(AddLecture.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });

    }


    private void validateInput() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.child("student_info")
                .child(deptName)
                .child(className)
                .child(divName)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(!task.getResult().exists()) {
                            Toast.makeText(AddLecture.this, "Invalid details were provided", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                        else {
                            addLecture(deptName, className, divName, lectureName, Misc.getDate());
                        }
                    }
                });
    }

}