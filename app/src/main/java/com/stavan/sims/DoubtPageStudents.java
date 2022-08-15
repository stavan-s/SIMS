package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.List;

public class DoubtPageStudents extends AppCompatActivity {

    ListView listView;
    ImageButton postDoubtBtn;
    EditText doubtInput;
    DoubtPageStudentsAdapter arrayAdapter;
    List<Doubt> doubtsList;

    String name, deptName, className, divName, lecName, date;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubt_page_students);

        Intent intent = getIntent();
        name = intent.getStringExtra("Name");
        deptName = intent.getStringExtra("DeptName");
        className = intent.getStringExtra("ClassName");
        divName = intent.getStringExtra("DivName");
        lecName = intent.getStringExtra("LectName");
        date = intent.getStringExtra("Date");
        
        listView = findViewById(R.id.doubts_page_students_listview);
        postDoubtBtn = findViewById(R.id.post_doubt_btn);
        doubtInput = findViewById(R.id.doubt_input_btn);


        doubtsList = new ArrayList<>();

        fetchData();

        arrayAdapter = new DoubtPageStudentsAdapter(this, doubtsList);
        listView.setAdapter(arrayAdapter);

        postDoubtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String doubt = doubtInput.getText().toString().trim();

                if(doubt.isEmpty()) {
                    Toast.makeText(DoubtPageStudents.this, "Please enter your doubt!", Toast.LENGTH_SHORT).show();
                    return;
                }

                postDoubt(doubt);
            }
        });

    }

    private void postDoubt(String doubt) {

        doubtInput.setText("");

        DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                .child("lecture_doubts")
                .child(deptName)
                .child(className)
                .child(divName)
                .child(lecName)
                .child(date)
                .child(doubt);

        db.setValue(new DoubtPost(name, "0")).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(DoubtPageStudents.this, "Doubt posted successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void clearList() {
        doubtsList.clear();
        arrayAdapter.notifyDataSetChanged();
    }

    public void fetchData() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("lecture_doubts")
                .child(deptName)
                .child(className)
                .child(divName)
                .child(lecName)
                .child(date)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        clearList();

                        if(snapshot.getChildrenCount() == 0) {
                            return;
                        }

                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String doubt = snapshot1.getKey().toString();
                            String name = snapshot1.child("name").getValue().toString();
                            String cleared = snapshot1.child("cleared").getValue().toString();
                            boolean clearedValue;
                            if(cleared.equals("0"))
                                clearedValue = false;
                            else
                                clearedValue = true;
                            Doubt doubtData = new Doubt(name, doubt, clearedValue);
                            doubtsList.add(doubtData);
                            arrayAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}