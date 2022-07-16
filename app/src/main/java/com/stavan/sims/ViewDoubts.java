package com.stavan.sims;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ViewDoubts extends AppCompatActivity {

    EditText deptInput, classInput, divInput;
    Button viewBtn;

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

                String deptName = deptInput.getText().toString().trim();
                String className = classInput.getText().toString().trim();
                String divName = divInput.getText().toString().trim();

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

                Intent intent = new Intent(getApplicationContext(), SelectDateForDoubtsPage.class);
                intent.putExtra("DeptName", deptName);
                intent.putExtra("ClassName", className);
                intent.putExtra("DivName", divName);
                intent.putExtra("NavigateTo", "DoubtsPage");
                startActivity(intent);

            }
        });
    }
}