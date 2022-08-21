package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class ViewAttendanceRecord extends AppCompatActivity {

    Student student;
    DatePicker datePicker;
    List<Attendance> attendanceRecords = new ArrayList<>();
    AttendanceAdapter arrayAdapter = new AttendanceAdapter(this, attendanceRecords);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance_record);

        datePicker = findViewById(R.id.view_attendance_date_picker);

        // get student details from intent
        student = (Student) getIntent().getSerializableExtra("Student");

        // get the date selected by the user
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String clickedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                String formattedDate = Misc.getFormattedDate(clickedDate);

                // show a dialog that contains student's attendance
                showAttendanceDialog(formattedDate);
            }
        });

    }


    // function to show the dialog containing student's attendance
    private void showAttendanceDialog(String clickedDate){

        clearList();

        ListView listView = new ListView(this);
        getAttendance(clickedDate, attendanceRecords);

        listView.setAdapter(arrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewAttendanceRecord.this);
        builder.setCancelable(true);
        builder.setView(listView);

        final AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        dialog.show();

    }


    // function to clear the current list of attendance records to avoid redundant data
    private void clearList() {
        attendanceRecords.clear();
        arrayAdapter.notifyDataSetChanged();
    }


    // function used to fetch the attendance from db
    private void getAttendance(String clickedDate, List<Attendance> attendanceRecords) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("attendance_info")
                .child(student.getDepartment())
                .child(student.getClassName())
                .child(student.getDiv())
                .child(clickedDate)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        clearList();

                        if(task.getResult().getChildrenCount() == 0) {
                            attendanceRecords.add(new Attendance("No lecture for this day!", ""));
                            return;
                        }

                        for (DataSnapshot snapshot1 : task.getResult().getChildren()) {
                            String lecName = snapshot1.getKey().toString();
                            String status;
                            String absenteesStr = snapshot1.child("absentees").getValue().toString();
                            if(absenteesStr.contains(student.getRollNo()))
                                status = "A";
                            else
                                status = "P";

//                            Toast.makeText(ViewAttendanceRecord.this, lecName + " " + status, Toast.LENGTH_SHORT).show();

                            Attendance attendance = new Attendance(lecName, status);
                            attendanceRecords.add(attendance);
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                });

    }

}