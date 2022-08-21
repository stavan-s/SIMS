package com.stavan.sims;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayStudentsInfo extends AppCompatActivity {

    TextView studName, studClass, studDept, studRoll, studOwnNumber, studParentNumber, studPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_students_info);

        Student student = (Student) getIntent().getSerializableExtra("StudentObject");
        String callingActivity = getIntent().getStringExtra("CallingActivity");

        studName = findViewById(R.id.stud_name);
        studClass = findViewById(R.id.stud_class);
        studDept = findViewById(R.id.stud_dept);
        studRoll = findViewById(R.id.stud_roll);
        studOwnNumber = findViewById(R.id.stud_own_number);
        studParentNumber = findViewById(R.id.stud_parent_number);
        studPass = findViewById(R.id.stud_pass);

        String fullName = student.getfName() + " " + student.getmName() + " " + student.getlName();
        String deptName = student.getDepartment();
        String classDivName = student.getClassName() + " / " + student.getDiv();
        String rollNo = student.getRollNo();
        String ownNumber = student.getOwnNumber();
        String parentNumber = student.getParentNumber();
        String absentCount = student.getAbsentCount();

        studName.setText(fullName);
        studClass.setText(classDivName);
        studDept.setText(deptName);
        studRoll.setText(rollNo);
        studOwnNumber.setText(ownNumber);
        studParentNumber.setText(parentNumber);

        if(callingActivity.equals("AdminDashboard")) {
            String pass = student.getInitialPass();
            studPass.setText(pass);
        }
        else {
            findViewById(R.id.studPassDisplay).setVisibility(View.GONE);
            studPass.setVisibility(View.GONE);
        }

    }
}