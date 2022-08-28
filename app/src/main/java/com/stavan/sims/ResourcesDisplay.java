package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), ViewPdf.class);
                intent.putExtra("pdf_url", files.get(i).getUri());
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(callingActivity.equals("StudentDashboard"))
                    return true;

                AlertDialog.Builder alert = new AlertDialog.Builder(ResourcesDisplay.this);
                alert.setMessage("Are you sure to delete record");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteFile(i);

                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).create().show();

                return true;

            }
        });


    }

    private void deleteFile(int i) {

        try {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("uploads/" + files.get(i).getRefName() + ".pdf");
            storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    // delete details from db
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child("resource_uploads")
                            .child(deptName)
                            .child(className)
                            .child(divName)
                            .child(lectureName)
                            .child(files.get(i).getRefName())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(ResourcesDisplay.this, "File Deleted Successfully", Toast.LENGTH_SHORT).show();

                                }
                            });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
        }

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