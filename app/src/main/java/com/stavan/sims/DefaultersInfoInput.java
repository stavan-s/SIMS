package com.stavan.sims;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        deptInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(deptInput.getText()) && !deptInput.getText().equals("Department"))
                    return;
                Misc.setDeptInput(DefaultersInfoInput.this, deptInput);
            }
        });

        classInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(classInput.getText()) && !classInput.getText().equals("Class"))
                    return;
                Misc.setClassInput(DefaultersInfoInput.this, classInput, deptInput.getText().toString());
            }
        });

        divInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(divInput.getText()) && !divInput.getText().equals("Div"))
                    return;
                Misc.setDivInput(DefaultersInfoInput.this, divInput, deptInput.getText().toString() ,classInput.getText().toString());
            }
        });

        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String deptName = deptInput.getText().toString().toUpperCase().trim();
                String className = classInput.getText().toString().toUpperCase().trim();
                String divName = divInput.getText().toString().toUpperCase().trim();

                if(deptInput.getText().equals("Department") || deptName.isEmpty()) {
                    deptInput.setError("Required");
                    return;
                }

                if(classInput.getText().equals("Class") || className.isEmpty()) {
                    classInput.setError("Required");
                    return;
                }

                if(divInput.getText().equals("Division") || divName.isEmpty()) {
                    divInput.setError("Required");
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