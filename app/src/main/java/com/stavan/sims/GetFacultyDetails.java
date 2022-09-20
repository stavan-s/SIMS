package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class GetFacultyDetails extends AppCompatActivity {

    EditText deptInput, numberInput;
    Button getBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_faculty_details);

        deptInput = findViewById(R.id.get_info_fact_dept_name_input);
        numberInput = findViewById(R.id.get_info_fact_number_input);
        getBtn = findViewById(R.id.get_info_fact_get_btn);

        deptInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(deptInput.getText()) && !deptInput.getText().equals("Department"))
                    return;
                Misc.setDeptInput(GetFacultyDetails.this, deptInput);
            }
        });


        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String deptName = deptInput.getText().toString().trim().toUpperCase();
                String number = numberInput.getText().toString().trim();

                if(deptInput.getText().equals("Department")) {
                    deptInput.setError("Required");
                    return;
                }

                if(number.isEmpty()) {
                    numberInput.setError("Required!");
                    return;
                }

                getFacultyDetails(deptName, number);

            }
        });

    }

    private void getFacultyDetails(String deptName, String number) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("faculty_info")
                .child(deptName)
                .child(number)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if(task.isSuccessful() && task.getResult().exists()) {
                            Faculty faculty = task.getResult().getValue(Faculty.class);

                            Intent intent = new Intent(getApplicationContext(), DisplayFacultyInfo.class);
                            intent.putExtra("FacultyObject", faculty);
                            startActivity(intent);

                        }
                        else if(!task.getResult().exists()) {
                            Toast.makeText(GetFacultyDetails.this, "Invalid Details!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}