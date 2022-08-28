package com.stavan.sims;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LectureDetails extends AppCompatActivity {

    EditText deptInput, classInput, divInput, lectureInput;
    String deptName, className, divName, lectureName;
    String callingActivity;
    Button proceedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_details);

        deptInput = findViewById(R.id.upload_dept_name_input);
        classInput = findViewById(R.id.upload_class_input);
        divInput = findViewById(R.id.upload_div_input);
        lectureInput = findViewById(R.id.upload_lecture_input);
        proceedBtn = findViewById(R.id.upload_proceed_btn);

        callingActivity = getIntent().getStringExtra("CallingActivity");

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deptName = deptInput.getText().toString().toUpperCase().trim();
                className = classInput.getText().toString().toUpperCase().trim();
                divName = divInput.getText().toString().toUpperCase().trim();
                lectureName = lectureInput.getText().toString().toUpperCase().trim();

                deptName = "CS";
                className = "TYCS";
                divName = "B";
                lectureName = "LINUX";

                if(deptName.isEmpty()) {
                    deptInput.setError("Required");
                    return;
                }

                if(className.isEmpty()) {
                    classInput.setError("Required");
                    return;
                }

                if(divName.isEmpty()) {
                    divInput.setError("Required");
                    return;
                }

                if(lectureName.isEmpty()) {
                    lectureInput.setError("Required");
                    return;
                }

                deptName = deptName.toUpperCase();
                className = className.toUpperCase();
                divName = divName.toUpperCase();
                lectureName = lectureName.toUpperCase();

                Intent intent = new Intent(getApplicationContext(), UploadResourcesPage.class);
                intent.putExtra("DeptName", deptName);
                intent.putExtra("ClassName", className);
                intent.putExtra("DivName", divName);
                intent.putExtra("LectName", lectureName);
                intent.putExtra("CallingActivity", callingActivity);
                startActivity(intent);

            }
        });

    }
}