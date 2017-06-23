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

public class FacilityListAdapter extends ArrayAdapter {

    Context context;
    String[] fName;
    String[] fAvail;

    public FacilityListAdapter(Context context,String[] fName, String[] fAvail) {
        super(context, R.layout.facility_list_item,fName);

        this.context=context;
        this.fName=fName;
        this.fAvail=fAvail;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview=inflater.inflate(R.layout.facility_list_item,parent,false);

        TextView facName=(TextView) rowview.findViewById(R.id.fac_name);
        TextView facAvail=(TextView) rowview.findViewById(R.id.fac_avail);

        facName.setText(fName[position]);
        facAvail.setText(fAvail[position]);

        Animation animation = AnimationUtils.loadAnimation(context,R.anim.fade_up_in);
        rowview.startAnimation(animation);

        return rowview;
    }
}
