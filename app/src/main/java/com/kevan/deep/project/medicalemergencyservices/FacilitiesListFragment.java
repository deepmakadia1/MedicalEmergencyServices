package com.kevan.deep.project.medicalemergencyservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.kevan.deep.project.medicalemergencyservices.Adapters.DoctorListAdapter;
import com.kevan.deep.project.medicalemergencyservices.Adapters.FacilityListAdapter;
import com.kevan.deep.project.medicalemergencyservices.api.APIsConnectivity;
import com.kevan.deep.project.medicalemergencyservices.api.DataConnectivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class FacilitiesListFragment extends Fragment{

    FacilityListAdapter facilityListAdapter;
//    String[] fName = new String[]{"MRI","X-Ray","Laboratories"};
//    String[] fAvail = new String[]{"N","Y","Y"};

    String[] faci_avail, faci_name;
    String bAvail;
    Fragment facilitiesListFragment = FacilitiesListFragment.this;
    int status;
    public static String[] H_id;
    ListView listView;
    private ProgressDialog progDialog;
    APIsConnectivity getAPI = new APIsConnectivity();
    DataConnectivity getData = new DataConnectivity();
    public static int a, arraySize;


    public FacilitiesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_doctor_list,container,false);

        listView =(ListView) view.findViewById(R.id.doc_list);

//        facilityListAdapter= new FacilityListAdapter(getActivity(),fName,fAvail);
//        if (facilityListAdapter.getCount() > 0) {
//            listView.setAdapter(facilityListAdapter);
//        }

        new FacilitiesListFragment.LazyDataConnection("hospitalfacility").execute("hospitalfacility", getAPI.hospitalfacility
                (ListingActivity.flag + ""));

        return view;
    }




    //=====================================================================================
// Async Task Class
//=====================================================================================
    public class LazyDataConnection extends AsyncTask<String, Void, String> {
        String method;

        public LazyDataConnection(String method) {
            this.method = method;
        }

        @Override
        protected String doInBackground(String... arg0) {
            method = arg0[0];
            return getData.callWebService(arg0[0], arg0[1]);
        }

        protected void onPostExecute(String xmlResponse) {
            if (xmlResponse.equals("")) {
                try {
                    if (progDialog != null && progDialog.isShowing()) {
                        progDialog.dismiss();
                    }
                } catch (Exception ex) {
                }
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                return;
            } else if (method.equals("hospitalfacility")) {
                try {
                    if (progDialog != null && progDialog.isShowing()) {
                        progDialog.dismiss();
                    }

                } catch (Exception ex) {
                }
                hospitalfacility(xmlResponse, "item");

                FacilityListAdapter adptr = new FacilityListAdapter(getContext(), faci_avail, faci_name);
                if (adptr.getCount() > 0) {
                    listView.setAdapter(adptr);
                }
            }
        }
    }

    //=====================================================================================
    // searchhospitalcity
    //=====================================================================================
    public void hospitalfacility(String str_RespXML, String str_TagName) {
        Document doc = getData.XMLfromString(str_RespXML);
        NodeList nodes = doc.getElementsByTagName(str_TagName);
        faci_avail = new String[nodes.getLength()];
        faci_name = new String[nodes.getLength()];
        H_id = new String[nodes.getLength()];
        if (nodes.getLength() > 0)
            for (int i = 0; i <= nodes.getLength(); i++) {
                Node e1 = nodes.item(i);
                Element el = (Element) e1;
                if (el != null) {
                    try {
                        faci_avail[i] = el.getElementsByTagName("faci_name").item(0).getTextContent();
                    } catch (Exception e) {
                    }
                    try {
                        faci_name[i] = el.getElementsByTagName("faci_avail").item(0).getTextContent();
                    } catch (Exception e) {

                    }

//                    try {
//                        H_id[i] = el.getElementsByTagName("h_id").item(0).getTextContent();
//                    } catch (Exception e) {
//                    }
                }
            }
    }
}
