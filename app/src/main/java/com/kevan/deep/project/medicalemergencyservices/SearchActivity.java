package com.kevan.deep.project.medicalemergencyservices;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.kevan.deep.project.medicalemergencyservices.Adapters.HospitalListAdapter;
import com.kevan.deep.project.medicalemergencyservices.MapClass.GetNearbyPlacesData;
import com.kevan.deep.project.medicalemergencyservices.api.APIsConnectivity;
import com.kevan.deep.project.medicalemergencyservices.api.DataConnectivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private ProgressDialog progDialog;
    APIsConnectivity getAPI = new APIsConnectivity();
    DataConnectivity getData = new DataConnectivity();
    Button btnSearch;
    Button btnYes_exit, btnNo_exit;
    Activity activity = SearchActivity.this;
    private GoogleMap map;
    public static int a;
    SupportMapFragment mapFragment;
    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 5000;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    String id="NULL";
    int status;
    ArrayAdapter adapter,adapter2;
    AutoCompleteTextView tvCity, tvName;

    public static String H_city;
    public static String H_name;

    String[] name, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        tvCity = (AutoCompleteTextView) findViewById(R.id.search_tv_city);
        tvName = (AutoCompleteTextView) findViewById(R.id.search_tv_hsptl);

        btnSearch = (Button) findViewById(R.id.search_btn_search);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        } else {
            Log.d("onCreate", "Google Play Services available.");
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                H_city = tvCity.getText().toString();
                H_name = tvName.getText().toString();

                if(!Arrays.asList(city).contains(H_city)){
                    Toast.makeText(getApplicationContext(),"Invalid city name",Toast.LENGTH_SHORT).show();
                }

                if(!Arrays.asList(name).contains(H_name)){
                    Toast.makeText(getApplicationContext(), "Invalid hospital name", Toast.LENGTH_SHORT).show();
                }


                if (H_city.equals("") && H_name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter any one", Toast.LENGTH_SHORT).show();
                } else if (!H_city.equals("") && !H_name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter only one", Toast.LENGTH_SHORT).show();
                } else if (!H_city.equals("")) {
                    a=0;
                    Intent i1 = new Intent(SearchActivity.this, ListingActivity.class);
                    startActivity(i1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                } else {
                    a=1;
                    Intent i2 = new Intent(SearchActivity.this, ListingActivity.class);
                    startActivity(i2);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }
        });

        new SearchActivity.LazyDataConnection("gethospitalcity").execute("gethospitalcity", getAPI.gethospitalcity(id));

    }



    @Override
    public void onBackPressed() {
        Intent in = new Intent(SearchActivity.this, MainActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    //=====================================================================================
    // Option menu Classes
//=====================================================================================

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
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
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
                Toast.makeText(getApplicationContext(), "Disbtclaimer Selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //=====================================================================================
    // Map Classes
//=====================================================================================

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, 0).show();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }
        Button btnHospital = (Button) findViewById(R.id.btnHospital);
        btnHospital.setOnClickListener(new View.OnClickListener() {
            String Hospital = "hospital";

            @Override
            public void onClick(View v) {
                Log.d("onClick", "Button is Clicked");
                map.clear();
                String url = getUrl(latitude, longitude, Hospital);
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = map;
                DataTransfer[1] = url;
                Log.d("onClick", url);
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.execute(DataTransfer);
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {


        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = map.addMarker(markerOptions);

        //move map camera
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(14));

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
        Log.d("onLocationChanged", "Exit");
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        map.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
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
            } else if (method.equals("gethospitalcity")) {
                try {
                    if (progDialog != null && progDialog.isShowing()) {
                        progDialog.dismiss();
                    }

                } catch (Exception ex) {}
                gethospitalcity(xmlResponse, "item");

                Set<String> set = new HashSet<String>(Arrays.asList(city));

                String[] city_final= set.toArray(new String[set.size()]);

                adapter = new ArrayAdapter(SearchActivity.this, android.R.layout.simple_list_item_1, city_final);
                adapter2 = new ArrayAdapter(SearchActivity.this, android.R.layout.simple_list_item_1, name);

                tvCity.setAdapter(adapter);
                tvCity.setThreshold(1);

                tvName.setAdapter(adapter2);
                tvName.setThreshold(1);
            }

        }
    }


    //=====================================================================================
    // gethospitalcity
    //=====================================================================================
    public void gethospitalcity(String str_RespXML, String str_TagName) {
        Document doc = getData.XMLfromString(str_RespXML);
        NodeList nodes = doc.getElementsByTagName(str_TagName);
        city = new String[nodes.getLength()];
        name = new String[nodes.getLength()];
        if (nodes.getLength() > 0)
            for (int i = 0; i <= nodes.getLength(); i++) {
                Node e1 = nodes.item(i);
                Element el = (Element) e1;
                if (el != null) {
                    try {
                        city[i] = el.getElementsByTagName("H_city").item(0).getTextContent();
                    } catch (Exception e) {
                    }
                    try {
                        name[i] = el.getElementsByTagName("H_name").item(0).getTextContent();
                    } catch (Exception e) {

                    }
                    try {
                        status = Integer.parseInt(el.getElementsByTagName("status").item(0).getTextContent());
                    } catch (Exception e) {
                    }
                }
            }
    }
}
