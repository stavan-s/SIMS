package com.stavan.sims;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

class AttendanceEntity {
    boolean checked;
    String name;
    AttendanceEntity(String name, boolean checked){
        this.name = name;
        this.checked = checked;
    }

    public boolean isChecked(){
        return checked;
    }
}

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    List<AttendanceEntity> info;

    public CustomBaseAdapter(Context ctx, List<AttendanceEntity> info) {
        this.context = ctx;
        this.info = info;
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

    public boolean isChecked(int position) {
        return info.get(position).checked;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rowView = view;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.custom_listview, null);

        CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.listview_checkbox);
        TextView text = (TextView) rowView.findViewById(R.id.info);

        checkBox.setChecked(info.get(i).checked);

        final String itemStr = info.get(i).name;
        text.setText(itemStr);
        checkBox.setTag(i);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean newState = !info.get(i).isChecked();
                info.get(i).checked = newState;
            }
        });

        checkBox.setChecked(isChecked(i));

        return rowView;

    }

}
