package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SelectDateForDoubtsPage extends AppCompatActivity {

    DatePicker datePicker;
    String deptName, className, divName, lectName;
    List<String> lecsList = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date_for_doubts_page);

        Intent intent = getIntent();
        deptName = intent.getStringExtra("DeptName").toUpperCase();
        className = intent.getStringExtra("ClassName").toUpperCase();
        divName = intent.getStringExtra("DivName").toUpperCase();

        checkDetailsExists();

        arrayAdapter = new ArrayAdapter(SelectDateForDoubtsPage.this, android.R.layout.simple_list_item_1 ,lecsList);

        datePicker = findViewById(R.id.view_attendance_date_picker);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String clickedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                String formattedDate = Misc.getFormattedDate(clickedDate);
                lecsList(formattedDate);
            }
        });

    }

    private void lecsList(String formattedDate) {

        clearList();

        ListView listView = new ListView(this);
        getLectures(formattedDate, lecsList);

        listView.setAdapter(arrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(SelectDateForDoubtsPage.this);
        builder.setCancelable(true);
        builder.setView(listView);

        final AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        dialog.show();

        // click on the particular lecture to go to its doubts page
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(lecsList.get(i).equals("No lecture for this day!")) {
                   return;
                }

                Intent intent;

                // figure out the activity by which 'this' was started, and navigate to the further activity
                if(getIntent().getStringExtra("NavigateTo").equals("DoubtsPage"))
                    intent = new Intent(getApplicationContext(), DoubtsPage.class);
                else {
                    intent = new Intent(getApplicationContext(), DoubtPageStudents.class);
                    String name = getIntent().getStringExtra("Name");
                    intent.putExtra("Name", name);
                }

                intent.putExtra("DeptName", deptName);
                intent.putExtra("ClassName", className);
                intent.putExtra("DivName", divName);
                intent.putExtra("LectName", lecsList.get(i));
                intent.putExtra("Date", formattedDate);

                dialog.cancel();

                startActivity(intent);
            }
        });

    }


    // function used to display the lecture names of the provided date
    private void getLectures(String formattedDate, List<String> lecsList) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("attendance_info")
                .child(deptName)
                .child(className)
                .child(divName)
                .child(formattedDate)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if(task.isSuccessful()) {
                            clearList();

                            if(task.getResult().getChildrenCount() == 0) {
                                lecsList.add("No lecture for this day!");
                                return;
                            }

                            for(DataSnapshot snapshot : task.getResult().getChildren()) {
                                lecsList.add(snapshot.getKey().toString());
                            }

                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    private void clearList() {
        lecsList.clear();
        arrayAdapter.notifyDataSetChanged();
    }

    private void checkDetailsExists() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("student_info")
                .child(deptName)
                .child(className)
                .child(divName)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(!task.getResult().exists()) {
                            Toast.makeText(SelectDateForDoubtsPage.this, "Invalid details were provided", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                });
    }
}