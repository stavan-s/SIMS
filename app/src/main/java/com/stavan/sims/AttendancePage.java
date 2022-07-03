package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class AttendancePage extends AppCompatActivity {

    private ListView listView;
    List<Item> info_list;
    DatabaseReference db;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_page);

        Intent intent = getIntent();
        String deptName = intent.getStringExtra("DeptName").toString();
        String className = intent.getStringExtra("ClassName").toString();
        String divName = intent.getStringExtra("DivName").toString();
        String lectureName = intent.getStringExtra("LecName").toString();

        info_list = new ArrayList<Item>();

        listView = findViewById(R.id.list_view);
        submitBtn = findViewById(R.id.attendance_submit_btn);

        CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), info_list);
        listView.setAdapter(customBaseAdapter);
        db = FirebaseDatabase.getInstance().getReference();

        populateList(deptName, className, divName, customBaseAdapter);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> absentRollNos = new ArrayList<>();

                for (int i=0; i<info_list.size(); i++){
                    if (info_list.get(i).isChecked()){
                        String temp = info_list.get(i).ItemString;
                        absentRollNos.add(temp);
                    }
                }

                Intent intent1 = new Intent(getApplicationContext(), ConfirmAbsentRollsPage.class);
                intent1.putExtra("DeptName", deptName);
                intent1.putExtra("ClassName", className);
                intent1.putExtra("DivName", divName);
                intent1.putExtra("LecName", lectureName);
                intent1.putStringArrayListExtra("absentRollNos", absentRollNos);
                startActivity(intent1);

            }
        });

    }

    private void populateList(String deptName, String className, String divName, CustomBaseAdapter customBaseAdapter) {
                db.child("student_info")
                .child(deptName)
                .child(className)
                .child(divName)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String rollNo = snapshot1.getKey().toString();
                    String name = snapshot1.child("fName").getValue().toString() + " "
                                    + snapshot1.child("lName").getValue().toString();
                    info_list.add(new Item(rollNo + " - " + name, false));
                    customBaseAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}