package com.stavan.sims;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

class Doubt {
    boolean cleared;
    String name, doubt;

    Doubt(String name, String doubt, boolean cleared) {
        this.name = name;
        this.doubt = doubt;
        this.cleared = cleared;
    }

    public boolean isCleared(){
        return cleared;
    }
}

public class CustomBaseAdapterDoubtPage extends BaseAdapter {


    Context context;
    List<Doubt> info;
    String deptName, className, divName, lectName, date;

    public CustomBaseAdapterDoubtPage(Context ctx, List<Doubt> info, String deptName, String className, String divName, String lectName, String date) {
        this.context = ctx;
        this.info = info;
        this.deptName = deptName;
        this.className = className;
        this.divName = divName;
        this.lectName = lectName;
        this.date = date;
    }

    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public Object getItem(int i) {
        return info.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public boolean isCleared(int position) {
        return info.get(position).isCleared();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.custom_list_view_doubts_page, null);

        CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.doubts_page_checkbox);
        TextView name = (TextView) rowView.findViewById(R.id.doubts_page_name);
        TextView doubt = (TextView) rowView.findViewById(R.id.doubts_page_doubt);

        checkBox.setChecked(info.get(i).cleared);
//        if(info.get(i).isCleared()) {
//            viewHolder.checkBox.setEnabled(false);
//        }

        final String nameVal = info.get(i).name;
        name.setText(nameVal);

        final String doubtVal = info.get(i).doubt;
        doubt.setText(doubtVal);
        checkBox.setTag(i);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.get(i).cleared = !info.get(i).isCleared();
                String val = info.get(i).cleared ? "1" : "0";

                // db code to make doubt cleared goes below
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.child("lecture_doubts")
                        .child(deptName)
                        .child(className)
                        .child(divName)
                        .child(lectName)
                        .child(date)
                        .child(info.get(i).doubt)
                        .child("cleared")
                        .setValue(val).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful()) {
                                    Toast.makeText(context, "Problem with database", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
        });
        checkBox.setChecked(isCleared(i));

//        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(!isCleared(i)) {
//                    compoundButton.setEnabled(false);
//                }
//            }
//        });

        return rowView;
    }
}
