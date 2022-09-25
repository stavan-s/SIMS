package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

    EditText lectureInput;
    TextView deptInput, classInput, divInput;
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

        deptInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(deptInput.getText()) && !deptInput.getText().equals("Department"))
                    return;
                Misc.setDeptInput(AddLecture.this, deptInput);
            }
        });

        classInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(classInput.getText()) && !classInput.getText().equals("Class"))
                    return;
                Misc.setClassInput(AddLecture.this, classInput, deptInput.getText().toString());
            }
        });

        divInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(divInput.getText()) && !divInput.getText().equals("Div"))
                    return;
                Misc.setDivInput(AddLecture.this, divInput, deptInput.getText().toString() ,classInput.getText().toString());
            }
        });

        // click to create a new lecture of the specified class
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(deptInput.getText().equals("Department")) {
                    deptInput.setError("Required");
                    return;
                }

                if(classInput.getText().equals("Class")) {
                    classInput.setError("Required");
                    return;
                }

                if(divInput.getText().equals("Division")) {
                    divInput.setError("Required");
                    return;
                }

                lectureName = lectureInput.getText().toString().toUpperCase().trim();
                if(lectureName.isEmpty()) {
                    lectureInput.setError("Required");
                    return;
                }

                deptName = deptInput.getText().toString().toUpperCase().trim();
                className = classInput.getText().toString().toUpperCase().trim();
                divName = divInput.getText().toString().toUpperCase().trim();

                validateInput();

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
                        if(task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                checkLectureExists();
                            }
                            else {
                                Toast.makeText(AddLecture.this, "Invalid Details!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    private void checkLectureExists() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("lecture_info")
                .child(deptName)
                .child(className)
                .child(divName)
                .child(lectureName)
                .child(Misc.getDate())
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if(task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                Toast.makeText(AddLecture.this, "Lecture already exists", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else {
                                addLecture(deptName, className, divName, lectureName, Misc.getDate());
                            }
                        }

                    }
                });

    }


    // function to add the lecture's entry in the db, and navigate to AttendancePage
    private void addLecture(String deptName, String className, String divName, String lectureName, String date) {

        increaseLecCount(deptName, className, divName, lectureName);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.child("lecture_info")
                .child(deptName)
                .child(className)
                .child(divName)
                .child(lectureName)
                .child(date)
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


    private void increaseLecCount(String deptName, String className, String divName, String lecName) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("lecture_info").child(deptName).child(className).child(divName);
        db.child(lecName)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful() && task.getResult().exists()) {
                            String count = task.getResult().child("lec_count").getValue().toString();
                            if(count.equals("null"))
                                count = "0";
                            int c = Integer.parseInt(count);
                            
                            db.child(lecName)
                                    .child("lec_count")
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
                            db.child(lecName)
                                    .child("lec_count")
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}