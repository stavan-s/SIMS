package com.stavan.sims;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

        deptInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(deptInput.getText()) && !deptInput.getText().equals("Department"))
                    return;
                Misc.setDeptInput(RemoveStudentPage.this, deptInput);
            }
        });

        classInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(classInput.getText()) && !classInput.getText().equals("Class"))
                    return;
                Misc.setClassInput(RemoveStudentPage.this, classInput, deptInput.getText().toString());
            }
        });

        divInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(divInput.getText()) && !divInput.getText().equals("Div"))
                    return;
                Misc.setDivInput(RemoveStudentPage.this, divInput, deptInput.getText().toString() ,classInput.getText().toString());
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String deptName = deptInput.getText().toString().trim().toUpperCase();
                String className = classInput.getText().toString().trim().toUpperCase();
                String divName = divInput.getText().toString().trim().toUpperCase();
                String rollNo = rollNoInput.getText().toString().trim();

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
                    rollNoInput.setError("Required");
                    return;
                }

                Misc.removeStudentAccount(getApplicationContext(), deptName, className, divName, rollNo);

            }
        });

    }
}