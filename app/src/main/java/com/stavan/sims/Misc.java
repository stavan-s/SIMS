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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.StringTokenizer;

// Class to define miscellaneous functions
public class Misc {

    final private static String ALPHABETS = "abcdefghijklmnoprstuvwxyz";
    final private static String DIGITS = "0123456789";
    final private static String SPECIAL_CHARACTERS = "!@#$%^&*?;";
    private static String finalPass = "";

    // Generates a random secure password of 8 characters
    public static String generatePassword() {

        char temp;

        for(int i = 0; i<8; i++) {

            if(i == 0) {
                temp = ALPHABETS.charAt(randNum(25));
                finalPass += String.valueOf(temp).toUpperCase();
            }

            if(i == 1 || i == 2 || i == 6) {
                temp = ALPHABETS.charAt(randNum(25));
                finalPass += String.valueOf(temp);
            }

            if(i == 3 || i == 7) {
                temp = SPECIAL_CHARACTERS.charAt(randNum(9));
                finalPass += String.valueOf(temp);
            }

            if(i == 4 || i == 5) {
                temp = DIGITS.charAt(randNum(9));
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


    // function used to register a new student's account into the authentication database
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


    // function used to send the credentials of an account after account registration
    private static void sendCredentials(String name, String id, String password, String ownNumber, String parentNumber) {

        String message = "Credentials for your SIMS app login :\n"
                        + "Name : " + name + "\n"
                        + "Email id : " + id + "\n"
                        + "Password : " + password;

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(ownNumber, null, message, null, null);

        Log.d("SMS", "SMS Sent");
    }


    // function used to add the account type (for login verification) of an account
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


    // function used to add student's details (in object form) into the database
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


    // function that returns the current date
    public static String getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(c.getTime());
        return date;
    }

    // function that sends an SMS to the provided number with the provided message
    public static void sendSMS(String number, String message) {

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, message, null, null);

    }

    // generate and return message string for absent student's own number
    public static String getOwnNumberMsg(String name, String lectureName) {
        ArrayList<String> nameInParts = getNameInParts(name);
        String msg = "Hello " + nameInParts.get(0) + " " + nameInParts.get(2) +
                        ",\nyou were absent for the " + lectureName
                        + " lecture today.\n Please follow up with the missed concepts";
        return msg;
    }

    // generate and return message string for absent student's parent's number
    public static String getParentNumberMsg(String name, String lectureName) {
        ArrayList<String> nameInParts = getNameInParts(name);
        String msg = "Hello " + nameInParts.get(1) + " " + nameInParts.get(2) +
                ",\nyour ward " + nameInParts.get(0) + " was absent for the " + lectureName
                + " lecture today.\n";
        return msg;
    }

    // function to get fName, mName and lName separately in a list
    private static ArrayList<String> getNameInParts(String name) {
        ArrayList<String> nameInParts = new ArrayList<>(3);
        StringTokenizer stringTokenizer = new StringTokenizer(name);
        for(int i = 0; i<3; i++)
            nameInParts.add(stringTokenizer.nextToken());
        return nameInParts;
    }

}
