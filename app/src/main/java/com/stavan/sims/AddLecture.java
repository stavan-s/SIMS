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

public class AddLecture extends AppCompatActivity {

    EditText deptInput, classInput, divInput, lectureInput;
    Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture);


        deptInput = findViewById(R.id.add_lec_dept_name_input);
        classInput = findViewById(R.id.add_lec_class_input);
        divInput = findViewById(R.id.add_lec_div_input);
        lectureInput = findViewById(R.id.add_lec_lecture_input);
        createBtn = findViewById(R.id.add_lec_create_btn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String deptName = deptInput.getText().toString();
                String className = classInput.getText().toString();
                String divName = divInput.getText().toString();
                String lectureName = lectureInput.getText().toString();

//                deptName = "CS";
//                className = "TYCS";
//                divName = "B";
//                lectureName = "Linux";

                addLecture(deptName, className, divName, lectureName, Misc.getDate());

            }
        });

    }

    private void addLecture(String deptName, String className, String divName, String lectureName, String date) {

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

}