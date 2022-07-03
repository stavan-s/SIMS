package com.stavan.sims;

import android.content.Context;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

// Class to define miscellaneous functions
public class Misc {

    public boolean accTypeResult = false;

    // Generates a random secure password of 8 characters
    public static String generatePassword() {

        String alphabets = "abcdefghijklmnoprstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*?;";
        String finalPass = "";
        char temp;

        for(int i = 0; i<8; i++) {

            if(i == 0) {
                temp = alphabets.charAt(randNum(25));
                finalPass += String.valueOf(temp).toUpperCase();
            }

            if(i == 1 || i == 2 || i == 6) {
                temp = alphabets.charAt(randNum(25));
                finalPass += String.valueOf(temp);
            }

            if(i == 3 || i == 7) {
                temp = specialChars.charAt(randNum(9));
                finalPass += String.valueOf(temp);
            }

            if(i == 4 || i == 5) {
                temp = digits.charAt(randNum(9));
                finalPass += String.valueOf(temp);
            }
        }
        return finalPass;
    }

    // Generates a random number within the provided range
    // helper function for generatePassword()
    public static int randNum(int high) {
        Random random = new Random();
        return random.nextInt(high);
    }


    public static void registerStudentAccount(Context context, Student student) {

        String email = student.getId();
        String password = generatePassword();
        student.setInitialPass(password);
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    addStudentDetails(context, student);

                    addAccountType(fAuth.getUid(), "Student", student);

                    fAuth.signOut();

                    sendCredentials(student.getfName(), email, password, student.getOwnNumber(), student.getParentNumber());
                }
                else {
                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    Log.d("ErrorInRegistration", task.getException().toString());
                }
            }
        });
    }

    private static void sendCredentials(String name, String id, String password, String ownNumber, String parentNumber) {

        String message = "Credentials for your SIMS app login :\n"
                        + "Name : " + name + "\n"
                        + "Email id : " + id + "\n"
                        + "Password : " + password;

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(ownNumber, null, message, null, null);

        Log.d("SMS", "SMS Sent");
    }


    private static void addAccountType(String uid, String accountType, Student student) {

        String value = "Student "
                + student.getDepartment() + " "
                + student.getClassName() + " "
                + student.getDiv() + " "
                + student.getRollNo();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("account_type").child(uid).setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Log.d("AccountTypeAdded", uid);
                }
            }
        });
    }

    private static void addStudentDetails(Context context, Student student) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("student_info")
                .child(student.getDepartment())
                .child(student.getClassName())
                .child(student.getDiv())
                .child(student.getRollNo())
                .setValue(student);

        Toast.makeText(context, "Student " + student.getfName() + " Registered Successfully", Toast.LENGTH_SHORT).show();
    }


    public static String getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(c.getTime());
        return date;
    }

}
