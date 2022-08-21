package com.stavan.sims;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class RemoveFacultyPage extends AppCompatActivity {

    EditText deptInput, numberInput;
    Button removeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_faculty_page);

        deptInput = findViewById(R.id.remove_faculty_dept_name_input);
        numberInput = findViewById(R.id.remove_faculty_number_input);
        removeBtn = findViewById(R.id.remove_student_remove_btn);

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String deptName = deptInput.getText().toString().trim().toUpperCase();
                String number = numberInput.getText().toString().trim();

                if(deptName.isEmpty()) {
                    deptInput.setError("Required");
                    return;
                }

                if(number.isEmpty()) {
                    numberInput.setError("Required");
                    return;
                }

                Misc.removeFacultyAccount(getApplicationContext(), deptName, number);
                onBackPressed();

            }
        });

    }
}