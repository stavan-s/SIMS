package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewDoubts extends AppCompatActivity {

    EditText deptInput, classInput, divInput;
    Button viewBtn;
    String deptName, className, divName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doubts);

        deptInput = findViewById(R.id.view_doubts_dept_name_input);
        classInput = findViewById(R.id.view_doubts_class_input);
        divInput = findViewById(R.id.view_doubts_div_input);
        viewBtn = findViewById(R.id.view_doubts_view_btn);

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deptName = deptInput.getText().toString().trim();
                className = classInput.getText().toString().trim();
                divName = divInput.getText().toString().trim();

//                deptName = "CS";
//                className = "TYCS";
//                divName = "B";

                if (deptName.isEmpty()) {
                    deptInput.setError("Required");
                    return;
                }

                if (className.isEmpty()) {
                    classInput.setError("Required");
                    return;
                }

                if (divName.isEmpty()) {
                    divInput.setError("Required");
                    return;
                }

                deptName = deptName.toUpperCase();
                className = className.toUpperCase();
                divName = divName.toUpperCase();

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
                                Intent intent = new Intent(getApplicationContext(), SelectDateForDoubtsPage.class);
                                intent.putExtra("DeptName", deptName);
                                intent.putExtra("ClassName", className);
                                intent.putExtra("DivName", divName);
                                intent.putExtra("NavigateTo", "DoubtsPage");
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(ViewDoubts.this, "Invalid Details!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }
}