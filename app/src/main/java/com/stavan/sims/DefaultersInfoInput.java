package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DefaultersInfoInput extends AppCompatActivity {

    EditText deptInput, classInput, divInput;
    Button generateBtn;
    String deptName, className, divName;

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

                deptName = deptInput.getText().toString().toUpperCase().trim();
                className = classInput.getText().toString().toUpperCase().trim();
                divName = divInput.getText().toString().toUpperCase().trim();

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

                validateInput();

            }
        });

    }

    private void validateInput() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("student_info")
                .child(deptName)
                .child(className)
                .child(divName)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                Intent intent = new Intent(getApplicationContext(), ViewDefaulters.class);
                                intent.putExtra("DeptName", deptName);
                                intent.putExtra("ClassName", className);
                                intent.putExtra("DivName", divName);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(DefaultersInfoInput.this, "Invalid Details!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }
}