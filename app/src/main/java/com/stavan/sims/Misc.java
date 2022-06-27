package com.stavan.sims;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

// Class to define miscellaneous functions
public class Misc {

    public boolean accTypeResult = false;

    // Generates a random secure password of 8 characters
    public String generatePassword() {

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
    public int randNum(int high) {
        Random random = new Random();
        return random.nextInt(high);
    }

}
