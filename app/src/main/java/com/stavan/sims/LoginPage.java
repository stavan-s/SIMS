package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
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
    TextView forgot;
    Button loginBtn;
    EditText loginEmailInput, loginPasswordInput;
    ProgressBar progressBar;
    ImageView passView;
    int passToggle = 0;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Mapping the actual ids to the reference
//        forgot = findViewById(R.id.forgot);
        loginBtn = findViewById(R.id.loginBtn);
        loginEmailInput = findViewById(R.id.loginEmailInput);
        loginPasswordInput = findViewById(R.id.loginPasswordInput);
        progressBar = findViewById(R.id.progressBar2);
        passView = findViewById(R.id.pass_visible);
        passView.setBackgroundResource(R.drawable.ic_pass_view);
        fAuth = FirebaseAuth.getInstance();

        // Creating spinner for selecting account type
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        // Check if user is already logged in with an account
        if(fAuth.getCurrentUser() != null) {

            ProgressDialog dialog = ProgressDialog.show(LoginPage.this, "",
                    "Loading. Please wait...", true);


            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            db.child("account_type").child(fAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()) {
                        String accType = task.getResult().child("type").getValue().toString();
                        goToPage(accType);
                        finishAffinity();
                    }
                }
            });
        }
    
//        // navigation to reset password page
//        forgot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginPage.this, ResetPass.class));
//            }
//        });


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
//                pass = "12345";

                if(email.isEmpty()) {
                    loginEmailInput.setError("Email is Required");
                    return;
                }
                if(pass.isEmpty()) {
                    loginPasswordInput.setError("Password is Required");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            TextView textView = (TextView) spinner.getSelectedView();
                            String accountType = textView.getText().toString();
//                            accountType = "Admin";
                            
                            validateAccountType(fAuth.getUid(), accountType);
                        }
                        else {
                            Toast.makeText(LoginPage.this, task.getException().getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }


    // function to validate whether the selected account type matches with the actual account type
    private void validateAccountType(String uid, String accountType) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.child("account_type").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String accType = task.getResult().child("type").getValue().toString();

                    if (accType.equals(accountType)) {
                        goToPage(accountType);
                        finishAffinity();
                    }
                    else {
                        Toast.makeText(LoginPage.this, "'Account type' is not valid for this account", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        fAuth.signOut();
                    }
                }
            }
        });
    }


    // function to go to a particular account related page
    public void goToPage(String accountType) {

        if (accountType.equals("Admin")) {
            startActivity(new Intent(getApplicationContext(), AdminPage.class));
            finishAffinity();
        }

        if (accountType.equals("Faculty")) {
            startActivity(new Intent(getApplicationContext(), FacultyPage.class));
            finishAffinity();
        }

        if (accountType.equals("Student")) {
            startActivity(new Intent(getApplicationContext(), UserPage.class));
            finishAffinity();
        }

    }

}