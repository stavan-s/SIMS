package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LectureDetails extends AppCompatActivity {

    EditText deptInput, classInput, divInput, lectureInput;
    String deptName, className, divName, lectureName;
    String callingActivity;
    Button proceedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_details);

        deptInput = findViewById(R.id.upload_dept_name_input);
        classInput = findViewById(R.id.upload_class_input);
        divInput = findViewById(R.id.upload_div_input);
        lectureInput = findViewById(R.id.upload_lecture_input);
        proceedBtn = findViewById(R.id.upload_proceed_btn);

        deptInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(deptInput.getText()) && !deptInput.getText().equals("Department"))
                    return;
                Misc.setDeptInput(LectureDetails.this, deptInput);
            }
        });

        classInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(classInput.getText()) && !classInput.getText().equals("Class"))
                    return;
                Misc.setClassInput(LectureDetails.this, classInput, deptInput.getText().toString());
            }
        });

        divInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(divInput.getText()) && !divInput.getText().equals("Div"))
                    return;
                Misc.setDivInput(LectureDetails.this, divInput, deptInput.getText().toString() ,classInput.getText().toString());
            }
        });

        callingActivity = getIntent().getStringExtra("CallingActivity");

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deptName = deptInput.getText().toString().toUpperCase().trim();
                className = classInput.getText().toString().toUpperCase().trim();
                divName = divInput.getText().toString().toUpperCase().trim();
                lectureName = lectureInput.getText().toString().toUpperCase().trim();

//                deptName = "CS";
//                className = "TYCS";
//                divName = "B";
//                lectureName = "LINUX";

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
                                Intent intent = new Intent(getApplicationContext(), UploadResourcesPage.class);
                                intent.putExtra("DeptName", deptName);
                                intent.putExtra("ClassName", className);
                                intent.putExtra("DivName", divName);
                                intent.putExtra("LectName", lectureName);
                                intent.putExtra("CallingActivity", callingActivity);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(LectureDetails.this, "Invalid Details!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }
}