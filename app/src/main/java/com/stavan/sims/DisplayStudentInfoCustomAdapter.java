package com.stavan.sims;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayStudentInfoCustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> entryList;

    public DisplayStudentInfoCustomAdapter(Context context, ArrayList<String> entryList) {
        this.context = context;
        this.entryList = entryList;
    }

    @Override
    public int getCount() {
        return entryList.size();
    }

    @Override
    public Object getItem(int i) {
        return entryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.display_student_info_custom_row, null);

        TextView entryView = (TextView) rowView.findViewById(R.id.student_info_entry);

        String entry = entryList.get(i);
        entryView.setText(Html.fromHtml(entry));

        return rowView;

    }
}
