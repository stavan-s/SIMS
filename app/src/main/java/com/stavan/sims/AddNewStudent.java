package com.stavan.sims;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;
import java.util.StringTokenizer;

public class AddNewStudent extends AppCompatActivity {

    public EditText emailInput, nameInput, departmentInput, classInput, divisionInput, rollInput, ownNumberInput, parentNumberInput;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student);

        emailInput = findViewById(R.id.student_email_input);
        nameInput = findViewById(R.id.student_name_input);
        departmentInput = findViewById(R.id.student_dept_input);
        classInput = findViewById(R.id.student_class_input);
        divisionInput = findViewById(R.id.student_div_input);
        rollInput = findViewById(R.id.student_roll_input);
        ownNumberInput = findViewById(R.id.student_own_number_input);
        parentNumberInput = findViewById(R.id.student_parent_number_input);
        submitBtn = findViewById(R.id.add_student_submit_button);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailInput.getText().toString().trim();

                String name = nameInput.getText().toString().trim();
                StringTokenizer fullName = new StringTokenizer(name);
                String fName, mName, lName;
                fName = fullName.nextToken();
                mName = fullName.nextToken();
                lName = fullName.nextToken();

                String departmentName = departmentInput.getText().toString().trim().toUpperCase();
                String className = classInput.getText().toString().trim().toUpperCase();
                String divisionName = divisionInput.getText().toString().trim().toUpperCase();
                String rollNo = rollInput.getText().toString().trim();
                String ownNumber = ownNumberInput.getText().toString().trim();
                String parentNumber = parentNumberInput.getText().toString().trim();

                if(email.isEmpty()) {
                    emailInput.setError("Required");
                    return;
                }

                if(name.isEmpty()) {
                    nameInput.setError("Required");
                    return;
                }

                if(fName.isEmpty() || mName.isEmpty() || lName.isEmpty()) {
                    nameInput.setError("Please enter full name");
                    return;
                }

                if(departmentName.isEmpty()) {
                    departmentInput.setError("Required");
                    return;
                }

                if(className.isEmpty()) {
                    classInput.setError("Required");
                    return;
                }

                if(divisionName.isEmpty()) {
                    divisionInput.setError("Required");
                    return;
                }

                if(rollNo.isEmpty()) {
                    rollInput.setError("Required");
                    return;
                }

                if(ownNumber.isEmpty()) {
                    ownNumberInput.setError("Required");
                    return;
                }

                if(parentNumber.isEmpty()) {
                    parentNumberInput.setError("Required");
                    return;
                }

                Student student = new Student(email, fName, mName, lName, departmentName, className, divisionName, rollNo, ownNumber, parentNumber, Misc.getDate());
                Misc.registerStudentAccount(getApplicationContext(), student);

                startActivity(new Intent(getApplicationContext(), AdminPage.class));
                finishAffinity();
            }
        });

    }

}