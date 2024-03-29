package com.stavan.sims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoubtsPage extends AppCompatActivity {

    ListView listView;
    ArrayList<Doubt> doubts;
    String deptName, className, divName, lectName, date;
    ImageView image;
    TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubts_page);

        Intent intent = getIntent();
        deptName = intent.getStringExtra("DeptName");
        className = intent.getStringExtra("ClassName");
        divName = intent.getStringExtra("DivName");
        lectName = intent.getStringExtra("LectName");
        date = intent.getStringExtra("Date");

        listView = findViewById(R.id.doubts_page_listview);
        image = findViewById(R.id.doubts_image);
        errorText = findViewById(R.id.doubts_image_text);
        image.setVisibility(View.INVISIBLE);
        errorText.setVisibility(View.INVISIBLE);

        doubts = new ArrayList<>();
        CustomBaseAdapterDoubtPage arrayAdapter = new CustomBaseAdapterDoubtPage(this, doubts, deptName, className, divName, lectName, date);
        listView.setAdapter(arrayAdapter);

//        ProgressDialog dialog = ProgressDialog.show(this, "",
//                "Loading. Please wait...", true);

        populateDoubtsList(arrayAdapter);

    }

    private void populateDoubtsList(CustomBaseAdapterDoubtPage arrayAdapter) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("lecture_info")
                .child(deptName)
                .child(className)
                .child(divName)
                .child(lectName)
                .child(date)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        doubts.clear();

                        if(snapshot.getChildrenCount() == 1) {
                            image.setVisibility(View.VISIBLE);
                            errorText.setVisibility(View.VISIBLE);
                            return;
                        }
                        else {
                            image.setVisibility(View.GONE);
                            errorText.setVisibility(View.GONE);
                        }

                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String doubt = snapshot1.getKey().toString();

                            if(doubt.equals("absentees")) {
                                continue;
                            }

                            String name = snapshot1.child("name").getValue().toString();
                            String cleared = snapshot1.child("cleared").getValue().toString();
                            boolean clearedValue;
                            if(cleared.equals("0"))
                                clearedValue = false;
                            else
                                clearedValue = true;
                            doubts.add(new Doubt(name, doubt, clearedValue));
                            arrayAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}