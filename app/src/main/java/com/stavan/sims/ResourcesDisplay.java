package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ResourcesDisplay extends AppCompatActivity {

    ListView listView;
    public ResourcesDisplayCustomAdapter adapter;
    ArrayList<PdfUpload> files;

    String deptName, className, divName, lectureName;
    String callingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources_display);

        listView = findViewById(R.id.resources_display_listview);

        deptName = getIntent().getStringExtra("DeptName");
        className = getIntent().getStringExtra("ClassName");
        divName = getIntent().getStringExtra("DivName");
        lectureName = getIntent().getStringExtra("LectName");
        callingActivity = getIntent().getStringExtra("CallingActivity");

        files = new ArrayList<>();

        adapter = new ResourcesDisplayCustomAdapter(getApplicationContext(), files, callingActivity, deptName, className, divName, lectureName);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        populateList();

    }

    private void populateList() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("resource_uploads")
                .child(deptName)
                .child(className)
                .child(divName)
                .child(lectureName)
                .orderByKey()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        files.clear();

                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {

                            String refName = snapshot1.getKey().toString();
                            String fileName = snapshot1.child("name").getValue().toString();
                            String uri = snapshot1.child("uri").getValue().toString();
                            String date = snapshot1.child("date").getValue().toString();

                            PdfUpload file = new PdfUpload();
                            file.setRefName(refName);
                            file.setName(fileName);
                            file.setDate(date);
                            file.setUri(uri);

                            files.add(file);

                        }

                        Collections.reverse(files);
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}