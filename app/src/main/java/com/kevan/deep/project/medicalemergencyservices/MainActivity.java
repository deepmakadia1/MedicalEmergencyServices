package com.kevan.deep.project.medicalemergencyservices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;

public class MainActivity extends AppCompatActivity {

    Button btnHospital;
    Button btnEmergency;
    Button btnYes_emergency, btnNo_emergency;
    Button btnYes_exit, btnNo_exit;
    LocationManager mlocManager;

    Activity activity = this;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHospital = (Button) findViewById(R.id.btnhospital);
        btnEmergency = (Button) findViewById(R.id.btnemergency);

        mlocManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        btnHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(getApplicationContext(), "Please turn on GPS", Toast.LENGTH_SHORT).show();
                }
                else {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    LayoutInflater inflater1 = activity.getLayoutInflater();
                    View dialogView1 = inflater1.inflate(R.layout.confirm_dialog, null);
                    builder1.setView(dialogView1);
                    final AlertDialog alertDialog1 = builder1.create();

                    alertDialog1.setView(dialogView1);
                    alertDialog1.show();

                    btnYes_emergency = (Button) alertDialog1.findViewById(R.id.location_btn_yes);
                    btnNo_emergency = (Button) alertDialog1.findViewById(R.id.location_btn_no);

                    btnNo_emergency.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog1.dismiss();
                        }
                    });

                    btnYes_emergency.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog1.dismiss();
                            Toast.makeText(getApplicationContext(), "Location sent", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = activity.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.exit_alert, null);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                alertDialog.setView(dialogView);
                alertDialog.show();

                btnNo_exit = (Button) alertDialog.findViewById(R.id.exit_btn_no);
                btnYes_exit = (Button) alertDialog.findViewById(R.id.exit_btn_yes);

                btnNo_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnYes_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
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
}
