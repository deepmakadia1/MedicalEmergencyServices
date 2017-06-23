package com.kevan.deep.project.medicalemergencyservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kevan.deep.project.medicalemergencyservices.Adapters.HospitalListAdapter;
import com.kevan.deep.project.medicalemergencyservices.api.APIsConnectivity;
import com.kevan.deep.project.medicalemergencyservices.api.DataConnectivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ListingActivity extends AppCompatActivity {

    String[] H_city, H_name, H_General_bed, H_Special_bed;


    int status;
    public static String[] H_id;
    String[] hgeneralbed;


    public static int flag, arraySize;


    String city = SearchActivity.H_city;

    private ProgressDialog progDialog;
    APIsConnectivity getAPI = new APIsConnectivity();
    DataConnectivity getData = new DataConnectivity();

    ListView listView;
    Activity activity = this;
    Button btnYes_exit, btnNo_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        listView = (ListView) findViewById(R.id.hospital_list);
        if(SearchActivity.a==0)
            new ListingActivity.LazyDataConnection("searchhospitalcity").execute("searchhospitalcity", getAPI.searchhospitalcity(city));
        else
            new ListingActivity.LazyDataConnection("searchhospitalname").execute("searchhospitalname", getAPI.searchhospitalname(SearchActivity.H_name));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                flag = Integer.parseInt(H_id[i]);
                Intent in = new Intent(ListingActivity.this, DetailActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(ListingActivity.this, SearchActivity.class);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(in);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(ListingActivity.this);
                LayoutInflater inflater = activity.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.exit_alert, null);
                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();

                dialog.setView(dialogView);
                dialog.show();

                btnNo_exit = (Button) dialog.findViewById(R.id.exit_btn_no);
                btnYes_exit = (Button) dialog.findViewById(R.id.exit_btn_yes);

                btnNo_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnYes_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });

                return true;
            case R.id.action_setting:
                Toast.makeText(getApplicationContext(), "Setting Selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_disclaimer:
                Toast.makeText(getApplicationContext(), "Disclaimer Selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                return;
            } else if (method.equals("searchhospitalcity")) {
                try {
                    if (progDialog != null && progDialog.isShowing()) {
                        progDialog.dismiss();
                    }

                } catch (Exception ex) {
                }
                searchhospitalcity(xmlResponse, "item");
                if (status == 1) {

                    HospitalListAdapter adptr = new HospitalListAdapter(ListingActivity.this, H_name, H_city, H_General_bed);
                    if (adptr.getCount() > 0) {
                        listView.setAdapter(adptr);
                    }


                }

            }
        }


        //=====================================================================================
        // searchhospitalcity
        //=====================================================================================
        public void searchhospitalcity(String str_RespXML, String str_TagName) {
            Document doc = getData.XMLfromString(str_RespXML);
            NodeList nodes = doc.getElementsByTagName(str_TagName);
            H_city = new String[nodes.getLength()];
            H_name = new String[nodes.getLength()];
            H_id = new String[nodes.getLength()];
            H_General_bed = new String[nodes.getLength()];
            H_Special_bed = new String[nodes.getLength()];
            if (nodes.getLength() > 0)
                for (int i = 0; i <= nodes.getLength(); i++) {
                    Node e1 = nodes.item(i);
                    Element el = (Element) e1;
                    if (el != null) {
                        try {
                            H_city[i] = el.getElementsByTagName("H_city").item(0).getTextContent();
                        } catch (Exception e) {
                        }
                        try {
                            H_name[i] = el.getElementsByTagName("H_name").item(0).getTextContent();
                        } catch (Exception e) {

                        }
                        try {
                            H_General_bed[i] = el.getElementsByTagName("H_General_bed").item(0).getTextContent();
                        } catch (Exception e) {

                        }
                        try {
                            H_Special_bed[i] = el.getElementsByTagName("H_Special_bed").item(0).getTextContent();
                        } catch (Exception e) {

                        }
                        try {
                            status = Integer.parseInt(el.getElementsByTagName("status").item(0).getTextContent());
                        } catch (Exception e) {

                        }
                        try {
                            H_id[i] = el.getElementsByTagName("H_id").item(0).getTextContent();
                        } catch (Exception e) {

                        }


                    }

                }
        }
    }

    //=====================================================================================
    // searchhospitalname
    //=====================================================================================
    public void searchhospitalname(String str_RespXML, String str_TagName) {
        Document doc = getData.XMLfromString(str_RespXML);
        NodeList nodes = doc.getElementsByTagName(str_TagName);
        H_city = new String[nodes.getLength()];
        H_name = new String[nodes.getLength()];
        hgeneralbed = new String[nodes.getLength()];
        H_id = new String[nodes.getLength()];
        arraySize = nodes.getLength();
        if (nodes.getLength() > 0)
            for (int i = 0; i <= nodes.getLength(); i++) {
                Node e1 = nodes.item(i);
                Element el = (Element) e1;
                if (el != null) {
                    try {
                        H_city[i] = el.getElementsByTagName("H_city").item(0).getTextContent();
                    } catch (Exception e) {
                    }
                    try {
                        H_name[i] = el.getElementsByTagName("H_name").item(0).getTextContent();
                    } catch (Exception e) {

                    }
                    try {
                        status = Integer.parseInt(el.getElementsByTagName("status").item(0).getTextContent());
                    } catch (Exception e) {

                    }
                    try {
                        hgeneralbed[i] = el.getElementsByTagName("H_General_bed").item(0).getTextContent();
                    } catch (Exception e) {

                    }
                    try {
                        H_id[i] = el.getElementsByTagName("H_id").item(0).getTextContent();
                    } catch (Exception e) {

                    }

                }

            }
    }

}

