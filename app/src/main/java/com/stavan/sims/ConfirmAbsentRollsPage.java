package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ConfirmAbsentRollsPage extends AppCompatActivity {

    ListView listView;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_absent_rolls_page);

        Intent intent = getIntent();

        String deptName = intent.getStringExtra("DeptName").toString();
        String className = intent.getStringExtra("ClassName").toString();
        String divName = intent.getStringExtra("DivName").toString();
        String lectureName = intent.getStringExtra("LecName").toString();

        ArrayList<String> absentRollNos = intent.getStringArrayListExtra("absentRollNos");

        listView = findViewById(R.id.confirm_listview);
        submitBtn = findViewById(R.id.confirm_submit_btn);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, absentRollNos);
        listView.setAdapter(arrayAdapter);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String absentRollNosString = "";
                for(int i = 0; i<absentRollNos.size(); i++) {
                    StringTokenizer stringTokenizer = new StringTokenizer(absentRollNos.get(i));
                    absentRollNosString += stringTokenizer.nextToken() + " ";
                }

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.child("attendance_info")
                        .child(deptName)
                        .child(className)
                        .child(divName)
                        .child(Misc.getDate())
                        .child(lectureName)
                        .child("absentees")
                        .setValue(absentRollNosString).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ConfirmAbsentRollsPage.this, "Task Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), FacultyPage.class));
                                finishAffinity();
                            }
                        });

            }
        });

    }
}