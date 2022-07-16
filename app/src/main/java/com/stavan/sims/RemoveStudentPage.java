package com.stavan.sims;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RemoveStudentPage extends AppCompatActivity {

    EditText deptInput, classInput, divInput, rollNoInput;
    Button removeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_student_page);

        deptInput = findViewById(R.id.remove_student_dept_name_input);
        classInput = findViewById(R.id.remove_student_class_input);
        divInput = findViewById(R.id.remove_student_div_input);
        rollNoInput = findViewById(R.id.remove_student_rollno_input);
        removeBtn = findViewById(R.id.remove_student_remove_btn);

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String deptName = deptInput.getText().toString().trim();
                String className = classInput.getText().toString().trim();
                String divName = divInput.getText().toString().trim();
                String rollNo = rollNoInput.getText().toString().trim();

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

                if(rollNo.isEmpty()) {
                    rollNoInput.setError("Required");
                    return;
                }

                deptName = deptName.toUpperCase();
                className = className.toUpperCase();
                divName = divName.toUpperCase();

                Misc.removeStudentAccount(getApplicationContext(), deptName, className, divName, rollNo);

            }
        });

    }
}