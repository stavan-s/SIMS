package com.stavan.sims;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ResourcesDisplayCustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<PdfUpload> pdfs;
    String callingActivity;
    String deptName, className, divName, lectureName;

    public ResourcesDisplayCustomAdapter(Context context, ArrayList<PdfUpload> pdfs, String callingActivity, String deptName, String className, String divName, String lectureName) {
        this.context = context;
        this.pdfs = pdfs;
        this.callingActivity = callingActivity;
        this.deptName = deptName;
        this.className = className;
        this.divName = divName;
        this.lectureName = lectureName;
    }

    @Override
    public int getCount() {
        return pdfs.size();
    }

    @Override
    public Object getItem(int i) {
        return pdfs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.resources_display_custom_row, null);

        TextView fileNameView = rowView.findViewById(R.id.file_name);
        TextView dateView = rowView.findViewById(R.id.date_display);
        ImageView downloadBtn = rowView.findViewById(R.id.download_btn);

        fileNameView.setText(pdfs.get(i).getName());
        dateView.setText(pdfs.get(i).getDate());

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(pdfs.get(i).getUri()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        return rowView;

    }

}
