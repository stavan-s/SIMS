package com.stavan.sims;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DefaultersInfoInput extends AppCompatActivity {

    EditText deptInput, classInput, divInput;
    Button generateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defaulters_info_input);

        deptInput = findViewById(R.id.defaulters_info_dept_name_input);
        classInput = findViewById(R.id.defaulters_info_class_input);
        divInput = findViewById(R.id.defaulters_info_div_input);
        generateBtn = findViewById(R.id.defaulters_info_generate_btn);

        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String deptName = deptInput.getText().toString().toUpperCase().trim();
                String className = classInput.getText().toString().toUpperCase().trim();
                String divName = divInput.getText().toString().toUpperCase().trim();

                if(deptName.isEmpty()) {
                    deptInput.setError("Required!");
                    return;
                }

                if(className.isEmpty()) {
                    classInput.setError("Required!");
                    return;
                }

                if(divName.isEmpty()) {
                    divInput.setError("Required!");
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), ViewDefaulters.class);
                intent.putExtra("DeptName", deptName);
                intent.putExtra("ClassName", className);
                intent.putExtra("DivName", divName);
                startActivity(intent);

            }
        });

    }
}