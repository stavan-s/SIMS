package com.stavan.sims;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.StringTokenizer;

public class AddNewFaculty extends AppCompatActivity {

    public EditText nameInput, emailInput, numberInput;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_faculty);

        nameInput = findViewById(R.id.faculty_name_input);
        emailInput = findViewById(R.id.faculty_email_input);
        numberInput = findViewById(R.id.faculty_number_input);
        registerBtn = findViewById(R.id.add_faculty_register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = nameInput.getText().toString().trim();
                StringTokenizer stringTokenizer = new StringTokenizer(name);
                String fName = stringTokenizer.nextToken();
                String mName = stringTokenizer.nextToken();
                String lName = stringTokenizer.nextToken();

                String email = emailInput.getText().toString().trim();
                String number = numberInput.getText().toString().trim();

                if(name.isEmpty()) {
                    nameInput.setError("Required");
                    return;
                }

                if(fName.isEmpty() || mName.isEmpty() || lName.isEmpty()) {
                    nameInput.setError("Enter full name");
                    return;
                }

                if(email.isEmpty()) {
                    emailInput.setError("Required");
                    return;
                }

                if(number.isEmpty()) {
                    numberInput.setError("Required");
                    return;
                }

//                Toast.makeText(AddNewFaculty.this, fName + " " + mName + " " + lName, Toast.LENGTH_SHORT).show();

                Faculty faculty = new Faculty(fName, mName, lName, email, number);
                Misc.registerFacultyAccount(getApplicationContext(), faculty);
            }
        });

    }
}