package com.stavan.sims;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Permissions
        Dexter.withContext(this)
                .withPermission(Manifest.permission.SEND_SMS)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        startActivity(new Intent(getApplicationContext(), LoginPage.class));
                        finishAffinity();
//                        Misc.registerStudentAccount(new Student("stavanshinde2905@gmail.com", "Stavan", "Peter", "Shinde", "CS", "TYCS", "B", "5146", "8291010457", "9764150566"));
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();
    }
}