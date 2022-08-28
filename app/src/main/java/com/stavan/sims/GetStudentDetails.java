package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    EditText studDept, studClass, studDiv, studRoll;
    Button getBtn;
    String callingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_student_details);

        studDept = findViewById(R.id.get_info_stud_dept_name_input);
        studClass = findViewById(R.id.get_info_stud_class_input);
        studDiv = findViewById(R.id.get_info_stud_div_input);
        studRoll = findViewById(R.id.get_info_stud_roll_input);
        getBtn = findViewById(R.id.get_info_stud_get_btn);
        callingActivity = getIntent().getStringExtra("CallingActivity");


        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String deptName = studDept.getText().toString().trim().toUpperCase();
                String className = studClass.getText().toString().trim().toUpperCase();
                String divName = studDiv.getText().toString().trim().toUpperCase();
                String rollNo = studRoll.getText().toString().trim().toUpperCase();

//                deptName = "CS";
//                className = "TYCS";
//                divName = "B";
//                rollNo = "5146";

                if(deptName.isEmpty()) {
                    studDept.setError("Required!");
                    return;
                }

                if(className.isEmpty()) {
                    studClass.setError("Required!");
                    return;
                }

                if(divName.isEmpty()) {
                    studDiv.setError("Required!");
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