package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPass extends AppCompatActivity {

    EditText emailInput;
    Button resetBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        emailInput = findViewById(R.id.resetEmailInput);
        resetBtn = findViewById(R.id.resetBtn);
        progressBar = findViewById(R.id.resetProgressBar);
        progressBar.setVisibility(View.INVISIBLE);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString().trim();

                if(email.isEmpty()) {
                    emailInput.setError("Email id is required!");
                    emailInput.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(ResetPass.this, "Reset password link send to your email id", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginPage.class));
                            finish();
                        }
                        else {
                            Toast.makeText(ResetPass.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

    }
}