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

import java.util.Locale;

public class GetStudentDetails extends AppCompatActivity {

    EditText deptInput, classInput, divInput, studRoll;
    Button getBtn;
    String callingActivity;
    String deptName, className, divName, rollNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_student_details);

        deptInput = findViewById(R.id.get_info_stud_dept_name_input);
        classInput = findViewById(R.id.get_info_stud_class_input);
        divInput = findViewById(R.id.get_info_stud_div_input);
        studRoll = findViewById(R.id.get_info_stud_roll_input);
        getBtn = findViewById(R.id.get_info_stud_get_btn);
        callingActivity = getIntent().getStringExtra("CallingActivity");

        deptInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(deptInput.getText()) && !deptInput.getText().equals("Department"))
                    return;
                Misc.setDeptInput(GetStudentDetails.this, deptInput);
            }
        });

        classInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(classInput.getText()) && !classInput.getText().equals("Class"))
                    return;
                Misc.setClassInput(GetStudentDetails.this, classInput, deptInput.getText().toString());
            }
        });

        divInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(divInput.getText()) && !divInput.getText().equals("Div"))
                    return;
                Misc.setDivInput(GetStudentDetails.this, divInput, deptInput.getText().toString() ,classInput.getText().toString());
            }
        });


        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deptName = deptInput.getText().toString().trim().toUpperCase();
                className = classInput.getText().toString().trim().toUpperCase();
                divName = divInput.getText().toString().trim().toUpperCase();
                rollNo = studRoll.getText().toString().trim().toUpperCase();

//                deptName = "CS";
//                className = "TYCS";
//                divName = "B";
//                rollNo = "5146";

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

                if(rollNo.isEmpty()) {
                    studRoll.setError("Required!");
                    return;
                }

                getStudentDetails(deptName, className, divName, rollNo);
            }
        });

    }

    private void getStudentDetails(String deptName, String className, String divName, String rollNo) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("student_info")
                .child(deptName)
                .child(className)
                .child(divName)
                .child(rollNo)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        
                        if(task.isSuccessful() && task.getResult().exists()) {
                            Student student = task.getResult().getValue(Student.class);

                            Intent intent = new Intent(getApplicationContext(), DisplayStudentsInfo.class);
                            intent.putExtra("StudentObject", student);
                            intent.putExtra("CallingActivity", callingActivity);
                            startActivity(intent);

                        }
                        else if(!task.getResult().exists()) {
                            Toast.makeText(GetStudentDetails.this, "Invalid Details!!", Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                });

    }

}