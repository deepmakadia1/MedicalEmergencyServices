package com.kevan.deep.project.medicalemergencyservices.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.kevan.deep.project.medicalemergencyservices.R;

/**
 * Created by DEEP on 08-Feb-17.
 */

public class HospitalListAdapter extends ArrayAdapter {

    Context context;
    String[] hName;
    String[] hAdd;
    String[] bAvail;

    public HospitalListAdapter(Context context, String[] hName, String[] hAdd , String[] bAvail) {
        super(context, R.layout.list_row,hName);
        this.context=context;
        this.hName=hName;
        this.hAdd=hAdd;
        this.bAvail=bAvail;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview=inflater.inflate(R.layout.list_row,parent,false);

        TextView hspName=(TextView) rowview.findViewById(R.id.hName);
        TextView hspAdd=(TextView) rowview.findViewById(R.id.hAdd);
        TextView bdAvail=(TextView) rowview.findViewById(R.id.bAvail);

        hspName.setText(hName[position]);
        hspAdd.setText(hAdd[position]);
        bdAvail.setText(bAvail[position]);

        Animation animation = AnimationUtils.loadAnimation(context,R.anim.fade_up_in);
        rowview.startAnimation(animation);

        return rowview;
    }
}
