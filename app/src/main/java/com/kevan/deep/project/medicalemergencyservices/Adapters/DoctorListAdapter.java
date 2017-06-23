package com.kevan.deep.project.medicalemergencyservices.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kevan.deep.project.medicalemergencyservices.R;

/**
 * Created by DEEP on 02-Mar-17.
 */
public class DoctorListAdapter extends ArrayAdapter {

    Context context;
    String[] dName;
    String[] dSpec;
    String[] dAvail;

    public DoctorListAdapter(Context context,String[] dName, String[] dSpec , String[] dAvail) {
        super(context, R.layout.doctor_list_item,dName);

        this.context=context;
        this.dName=dName;
        this.dSpec=dSpec;
        this.dAvail=dAvail;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview=inflater.inflate(R.layout.doctor_list_item,parent,false);

        TextView docName=(TextView) rowview.findViewById(R.id.doc_name);
        TextView docSpec=(TextView) rowview.findViewById(R.id.doc_spec);
        TextView docAvail=(TextView) rowview.findViewById(R.id.doc_avail);

        docName.setText(dName[position]);
        docSpec.setText(dSpec[position]);
        docAvail.setText(dAvail[position]);

        Animation animation = AnimationUtils.loadAnimation(context,R.anim.fade_up_in);
        rowview.startAnimation(animation);

        return rowview;
    }
}