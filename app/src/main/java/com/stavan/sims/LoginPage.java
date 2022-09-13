package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import org.w3c.dom.Text;

import java.util.StringTokenizer;

public class LoginPage extends AppCompatActivity {

    // Creating references for views
    Button loginBtn;
    EditText loginEmailInput, loginPasswordInput;
    ImageView passView;
    int passToggle = 0;
    FirebaseAuth fAuth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Mapping the actual ids to the reference
        loginBtn = findViewById(R.id.loginBtn);
        loginEmailInput = findViewById(R.id.loginEmailInput);
        loginPasswordInput = findViewById(R.id.loginPasswordInput);
        passView = findViewById(R.id.pass_visible);
        passView.setBackgroundResource(R.drawable.ic_pass_view);
        fAuth = FirebaseAuth.getInstance();


        // hide/view password toggle option
        passView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passToggle == 0) {
                    loginPasswordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passView.setBackgroundResource(R.drawable.ic_pass_hide);
                }
                else {
                    loginPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passView.setBackgroundResource(R.drawable.ic_pass_view);
                }
                passToggle ^= 1;
            }
        });


        // login button functionality
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = loginEmailInput.getText().toString().trim();
                String pass = loginPasswordInput.getText().toString().trim();

//                email = "admin@gmail.com";
//                pass = "123456";

                if(email.isEmpty()) {
                    loginEmailInput.setError("Email is Required");
                    return;
                }
                if(pass.isEmpty()) {
                    loginPasswordInput.setError("Password is Required");
                    return;
                }

                dialog = ProgressDialog.show(LoginPage.this, "",
                        "Signing in...", true);

                fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Misc.goToPage(getApplicationContext(), fAuth.getUid(), dialog);
                        }
                        else {
                            Toast.makeText(LoginPage.this, task.getException().getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finishAffinity();
    }
}