package com.kevan.deep.project.medicalemergencyservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.kevan.deep.project.medicalemergencyservices.api.APIsConnectivity;
import com.kevan.deep.project.medicalemergencyservices.api.DataConnectivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DetailActivity extends AppCompatActivity {

    Button btnOtherdetail;
    Button btnYes_exit,btnNo_exit;
    Activity activity=this;
    Toolbar toolbar;
    TextView toolbar_tv;
    String H_address, H_name, H_phone, H_fax, H_email;
    String hgeneralbed, hspecialbed;
    private ProgressDialog progDialog;
    APIsConnectivity getAPI = new APIsConnectivity();
    DataConnectivity getData = new DataConnectivity();

    TextView addresstxtview, phonenotxtview, faxtxtview, emailtxtview, generalbedyn, specialbedyn, hospitalnametxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar=(Toolbar)findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        btnOtherdetail=(Button)findViewById(R.id.btn_otherdetail);

        btnOtherdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailActivity.this,OtherDetailActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        // hospitalnametxt = (TextView) findViewById(R.id.hospitalnametxt);
        addresstxtview = (TextView) findViewById(R.id.detail_tv_address);
        phonenotxtview = (TextView) findViewById(R.id.detail_tv_phone);
        emailtxtview = (TextView) findViewById(R.id.detail_tv_email);
        generalbedyn = (TextView) findViewById(R.id.detail_tv_gbed);
        specialbedyn = (TextView) findViewById(R.id.detail_tv_sbed);
        faxtxtview = (TextView) findViewById(R.id.detail_tv_fax);
        toolbar_tv=(TextView) findViewById(R.id.detail_tlbr_tv_hsptl);

        new DetailActivity.LazyDataConnection("searchhospitalalldata").execute("searchhospitalalldata", getAPI.searchhospitalalldata(ListingActivity.flag+""));

    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(DetailActivity.this, ListingActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                AlertDialog.Builder builder=new AlertDialog.Builder(DetailActivity.this);
                LayoutInflater inflater=activity.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.exit_alert,null);
                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();

                dialog.setView(dialogView);
                dialog.show();

                btnNo_exit=(Button)dialog.findViewById(R.id.exit_btn_no);
                btnYes_exit=(Button)dialog.findViewById(R.id.exit_btn_yes);

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
                Toast.makeText(DetailActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                return;
            } else if (method.equals("searchhospitalalldata")) {
                try {
                    if (progDialog != null && progDialog.isShowing()) {
                        progDialog.dismiss();
                    }
                } catch (Exception ex) {
                }
                searchhospitalalldata(xmlResponse, "item");
                addresstxtview.setText(H_address);
                phonenotxtview.setText(H_phone);
                emailtxtview.setText(H_email);
                faxtxtview.setText(H_fax);
                generalbedyn.setText(hgeneralbed);
                specialbedyn.setText(hspecialbed);
                toolbar_tv.setText(H_name);
//                toolbar_tv.setTextColor(getResources().getColor(R.color.black));
//                toolbar.setTitle(H_name);
//                toolbar.setTitleTextColor(Color.WHITE);
                // hospitalnametxt.setText(H_name);
            }
        }
    }

    //=====================================================================================
    // searchhospitalalldata
    //=====================================================================================
    public void searchhospitalalldata(String str_RespXML, String str_TagName) {
        Document doc = getData.XMLfromString(str_RespXML);
        NodeList nodes = doc.getElementsByTagName(str_TagName);
        if (nodes.getLength() > 0)
            for (int i = 0; i <= nodes.getLength(); i++) {
                Node e1 = nodes.item(i);
                Element el = (Element) e1;
                if (el != null) {
                    try {
                        H_address = el.getElementsByTagName("H_address").item(0).getTextContent();
                    } catch (Exception e) {
                    }
                    try {
                        H_name = el.getElementsByTagName("H_name").item(0).getTextContent();
                    } catch (Exception e) {
                    }
                    try {
                        H_phone = el.getElementsByTagName("H_phone").item(0).getTextContent();
                    } catch (Exception e) {
                    }
                    try {
                        hgeneralbed = el.getElementsByTagName("H_General_bed").item(0).getTextContent();
                    } catch (Exception e) {
                    }
                    try {
                        H_fax = el.getElementsByTagName("H_fax").item(0).getTextContent();
                    } catch (Exception e) {
                    }
                    try {
                        hspecialbed = el.getElementsByTagName("H_Special_bed").item(0).getTextContent();
                    } catch (Exception e) {
                    }
                    try {
                        H_email = el.getElementsByTagName("H_email").item(0).getTextContent();
                    } catch (Exception e) {

                    }
                }
            }
    }

}
