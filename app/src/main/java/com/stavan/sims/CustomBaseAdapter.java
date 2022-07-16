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

class Item {
    boolean checked;
    String ItemString;
    Item(String t, boolean b){
        ItemString = t;
        checked = b;
    }

    public boolean isChecked(){
        return checked;
    }
}

class ViewHolder {
    CheckBox checkBox;
    TextView text;
}

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    List<Item> info;

    public CustomBaseAdapter(Context ctx, List<Item> info) {
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

        ViewHolder viewHolder = new ViewHolder();

        if(rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.custom_listview, null);

            viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.listview_checkbox);
            viewHolder.text = (TextView) rowView.findViewById(R.id.info);
            rowView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        viewHolder.checkBox.setChecked(info.get(i).checked);

        final String itemStr = info.get(i).ItemString;
        viewHolder.text.setText(itemStr);
        viewHolder.checkBox.setTag(i);

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean newState = !info.get(i).isChecked();
                info.get(i).checked = newState;
            }
        });

        viewHolder.checkBox.setChecked(isChecked(i));

        return rowView;

    }

}
