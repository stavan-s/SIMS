package com.stavan.sims;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DisplayFacultyInfo extends AppCompatActivity {

    TextView nameDisplay, numDisplay, passDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_faculty_info);

            nameDisplay = findViewById(R.id.fact_name);
            numDisplay = findViewById(R.id.fact_num);
            passDisplay = findViewById(R.id.fact_pass);

            Faculty faculty = (Faculty) getIntent().getSerializableExtra("FacultyObject");

            String fullName = faculty.getfName() + " " + faculty.getmName() + " " + faculty.getlName();
            nameDisplay.setText(fullName);
            numDisplay.setText(faculty.getNumber());
            passDisplay.setText(faculty.getInitialPass());

    }
}