package com.stavan.sims;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddLecture extends AppCompatActivity {

    EditText deptInput, classInput, divInput;
    Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture);

        deptInput = findViewById(R.id.add_lec_dept_name_input);
        classInput = findViewById(R.id.add_lec_class_input);
        divInput = findViewById(R.id.add_lec_div_input);
        createBtn = findViewById(R.id.add_lec_create_btn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String deptName = deptInput.getText().toString();
                String className = classInput.getText().toString();
                String divName = divInput.getText().toString();



            }
        });

    }
}