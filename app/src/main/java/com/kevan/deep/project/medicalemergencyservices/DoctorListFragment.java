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
import com.kevan.deep.project.medicalemergencyservices.api.APIsConnectivity;
import com.kevan.deep.project.medicalemergencyservices.api.DataConnectivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class DoctorListFragment extends Fragment {


    ListView listView;
    DoctorListAdapter doctorListAdapter;

//    String[] dName = new String[]{"Deep","Parth","Kevan"};
//    String[] dSpec = new String[]{"Orthopedic","Dental","Surgon"};
//    String[] dAvail = new String[]{"Y","N","Y"};

    String[] doctor_name, doctor_speciality, doctor_avail;
    String bAvail;
    int status;
    public static String[] H_id;
    String[] hgeneralbed;
    private ProgressDialog progDialog;
    APIsConnectivity getAPI = new APIsConnectivity();
    DataConnectivity getData = new DataConnectivity();
    public static int a, arraySize;



    public DoctorListFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_doctor_list,container,false);

        listView =(ListView) view.findViewById(R.id.doc_list);

//        doctorListAdapter= new DoctorListAdapter(getActivity(),dName,dSpec,dAvail);
//        if (doctorListAdapter.getCount() > 0) {
//            listView.setAdapter(doctorListAdapter);
//        }

        new DoctorListFragment.LazyDataConnection("hospitaldoctor").execute("hospitaldoctor", getAPI.hospitaldoctor
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
            } else if (method.equals("hospitaldoctor")) {
                try {
                    if (progDialog != null && progDialog.isShowing()) {
                        progDialog.dismiss();
                    }

                } catch (Exception ex) {
                }
                hospitaldoctor(xmlResponse, "item");

                DoctorListAdapter adptr = new DoctorListAdapter(getContext(), doctor_name, doctor_speciality, doctor_avail);
                if (adptr.getCount() > 0) {
                    listView.setAdapter(adptr);

                }

            }
        }

    }

    //=====================================================================================
    // hospitaldoctor
    //=====================================================================================
    public void hospitaldoctor(String str_RespXML, String str_TagName) {
        Document doc = getData.XMLfromString(str_RespXML);
        NodeList nodes = doc.getElementsByTagName(str_TagName);
        doctor_name = new String[nodes.getLength()];
        doctor_speciality = new String[nodes.getLength()];
        doctor_avail = new String[nodes.getLength()];
        //hgeneralbed = new String[nodes.getLength()];
        // H_id = new String[nodes.getLength()];
        if (nodes.getLength() > 0)
            for (int i = 0; i <= nodes.getLength(); i++) {
                Node e1 = nodes.item(i);
                Element el = (Element) e1;
                if (el != null) {
                    try {
                        doctor_name[i] = el.getElementsByTagName("doct_name").item(0).getTextContent();
                    } catch (Exception e) {
                    }
                    try {
                        doctor_speciality[i] = el.getElementsByTagName("doct_spec").item(0).getTextContent();
                    } catch (Exception e) {

                    }

                    try {
                        doctor_avail[i] = el.getElementsByTagName("doct_avail").item(0).getTextContent();
                    } catch (Exception e) {

                    }
//                    try {
//                        H_id[i] = el.getElementsByTagName("H_id").item(0).getTextContent();
//                    } catch (Exception e) {
//
//                    }

                }

            }
    }

}
