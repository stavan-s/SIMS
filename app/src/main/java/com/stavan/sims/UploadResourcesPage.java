package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Locale;

public class UploadResourcesPage extends AppCompatActivity {

    String deptName, className, divName, lectureName;
    EditText filenameInput;
    Button uploadBtn;
    ConstraintLayout viewResourcesBtn;
    StorageReference store = FirebaseStorage.getInstance().getReference();
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_resources_page);

        deptName = getIntent().getStringExtra("DeptName");
        className = getIntent().getStringExtra("ClassName");
        divName = getIntent().getStringExtra("DivName");
        lectureName = getIntent().getStringExtra("LectName");
        filenameInput = findViewById(R.id.upload_page_file_name_input);
        uploadBtn = findViewById(R.id.upload_page_upload_btn);
        viewResourcesBtn = findViewById(R.id.upload_page_access_card);


        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fileName = filenameInput.getText().toString().trim();
                if(fileName.isEmpty()) {
                    filenameInput.setError("Required!");
                    return;
                }

                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select PDF File"), 1);

            }
        });

        viewResourcesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResourcesDisplay.class);
                intent.putExtra("DeptName", deptName);
                intent.putExtra("ClassName", className);
                intent.putExtra("DivName", divName);
                intent.putExtra("LectName", lectureName);
                intent.putExtra("CallingActivity", getIntent().getStringExtra("CallingActivity"));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK
            && data != null && data.getData() != null) {

            Uri file = data.getData();

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            String storageRefFileName = String.valueOf(System.currentTimeMillis());

                StorageReference storeRef = store.child("uploads/" + storageRefFileName + ".pdf");
                storeRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                while(!uri.isComplete());
                                Uri url = uri.getResult();

                                PdfUpload pdf = new PdfUpload(filenameInput.getText().toString().trim(), url.toString(), Misc.getDate());
                                db.child("resource_uploads")
                                        .child(deptName)
                                        .child(className)
                                        .child(divName)
                                        .child(lectureName)
                                        .child(storageRefFileName)
                                        .setValue(pdf);

                                Toast.makeText(UploadResourcesPage.this, "File Uploaded!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                filenameInput.setText("");

                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                progressDialog.setMessage("Uploaded: " + (int)progress + "%");


                            }
                        });

        }
        else {
            Toast.makeText(this, "Some Error Occurred!!", Toast.LENGTH_SHORT).show();
        }
    }
}