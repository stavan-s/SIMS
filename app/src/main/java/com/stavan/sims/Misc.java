package com.stavan.sims;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

// Class to define miscellaneous functions
public class Misc {


    // Generates a random secure password of 8 characters
    public static String generatePassword() {

        final String ALPHABETS = "abcdefghijklmnoprstuvwxyz";
        final String DIGITS = "0123456789";
        final String SPECIAL_CHARACTERS = "!@#$%^&*?;";
        String finalPass = "";

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
        return finalPass.substring(0, 7);
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

                    student.setUID(fAuth.getUid());

                    addStudentDetails(context, student);

                    addStudentAccountType(fAuth.getUid(), "Student", student);

                    fAuth.signOut();

//                    sendCredentialsToStudent(student.getfName(), email, password, student.getOwnNumber(), student.getParentNumber());

//                    clearAddStudentPageTextFields();
                }
                else {
                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    Log.d("ErrorInRegistration", task.getException().toString());
                }
            }
        });
    }


    public static void removeStudentAccount(Context context, String deptName, String className, String divName, String rollNo) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.child("student_info")
                .child(deptName)
                .child(className)
                .child(divName)
                .child(rollNo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String uid = snapshot.child("uid").getValue().toString();
                        db.child("account_type")
                                .child(uid)
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {

                                            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                            db.child("student_info")
                                                    .child(deptName)
                                                    .child(className)
                                                    .child(divName)
                                                    .child(rollNo)
                                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(context, "Student details removed successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    public static void registerFacultyAccount(Context context, Faculty faculty, String dept) {

        String email = faculty.getEmail();
        String password = generatePassword();
        faculty.setInitialPass(password);
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    faculty.setuid(fAuth.getUid());

                    addFacultyDetails(context, faculty, dept);

                    addFacultyAccountType(fAuth.getUid(), "Faculty", dept, faculty.getNumber());

                    fAuth.signOut();

//                    sendCredentialsToFaculty(faculty.getfName(), email, password, faculty.getNumber());

//                    clearAddFacultyPageTextFields();
                }
                else {
                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    Log.d("ErrorInRegistration", task.getException().toString());
                }
            }
        });
    }

    private static void clearAddFacultyPageTextFields() {
        AddNewFaculty obj = new AddNewFaculty();
        obj.nameInput.setText("");
        obj.emailInput.setText("");
        obj.numberInput.setText("");
        obj.nameInput.requestFocus();
    }

    private static void clearAddStudentPageTextFields() {
        AddNewStudent obj = new AddNewStudent();
        obj.nameInput.setText("");
        obj.emailInput.setText("");
        obj.departmentInput.setText("");
        obj.classInput.setText("");
        obj.divisionInput.setText("");
        obj.rollInput.setText("");
        obj.ownNumberInput.setText("");
        obj.parentNumberInput.setText("");
        obj.nameInput.requestFocus();
    }


    // function used to send the credentials of an account after account registration
    private static void sendCredentialsToStudent(String name, String id, String password, String ownNumber, String parentNumber) {

        String message = "Credentials for your SIMS app login :\n"
                        + "Name : " + name + "\n"
                        + "Email id : " + id + "\n"
                        + "Password : " + password;

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(ownNumber, null, message, null, null);
        smsManager.sendTextMessage(parentNumber, null, message, null, null);

        Log.d("SMS", "SMS Sent");
    }

    private static void sendCredentialsToFaculty(String name, String id, String password, String number) {

        String message = "Credentials for your SIMS app login :\n"
                + "Name : " + name + "\n"
                + "Email id : " + id + "\n"
                + "Password : " + password;

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, message, null, null);

        Log.d("SMS", "SMS Sent");
    }


    // function used to add the account type (for login verification) of an account
    private static void addStudentAccountType(String uid, String accountType, Student student) {

        Map<String, String> values = new HashMap<>();
        values.put("dept_name", student.getDepartment());
        values.put("class_name", student.getClassName());
        values.put("div_name", student.getDiv());
        values.put("roll_no", student.getRollNo());
        values.put("type", "Student");


        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("account_type").child(uid).setValue(values).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Log.d("AccountTypeAdded", uid);
                }
            }
        });
    }


    private static void addFacultyAccountType(String uid, String accountType, String dept, String number) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("account_type")
                .child(uid)
                .setValue(accountType + " " + dept + " " + number).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {

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


    private static void addFacultyDetails(Context context, Faculty faculty, String dept) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("faculty_info")
                .child(dept)
                .child(faculty.getNumber())
                .setValue(faculty).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {

                        }
                    }
                });

        Toast.makeText(context, "Faculty " + faculty.getfName() + " " + faculty.getmName() + " Registered Successfully", Toast.LENGTH_SHORT).show();
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

    public static String getFormattedDate(String clickedDate) {

        StringTokenizer st = new StringTokenizer(clickedDate, "-");
        String year = st.nextToken();
        String month = st.nextToken();
        String day = st.nextToken();

        String formattedDate = year;

        if(month.length()==1) {
            formattedDate += "-0" + month;
        }
        else {
            formattedDate += "-" + month;
        }

        if(day.length()==1) {
            formattedDate += "-0" + day;
        }
        else {
            formattedDate += "-" + day;
        }

        return formattedDate;
    }
}
