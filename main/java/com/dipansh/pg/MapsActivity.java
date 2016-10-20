package com.dipansh.pg;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> markerPoints;
    String strUrl = "";
    String distance = "";
    String duration = "";
    String destination,type;
    LatLng latLng;
    TextView txtDistance, txtDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle bundle = getIntent().getExtras();
//        String source=bundle.getString("source");
        destination = bundle.getString("address");
        type = bundle.getString("type");
//        Toast.makeText(getApplicationContext(),destination,Toast.LENGTH_LONG).show();

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        txtDuration = (TextView) findViewById(R.id.textView6);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        onSearch();
        if (type.equals("showRoute")) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            Location myLocation=mMap.getMyLocation();
            double latitude=myLocation.getLatitude();
            double longitue=myLocation.getLongitude();
            String origin = latitude+","+longitue;
//            strUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=Chandigarh&destination=New Delhi";
            strUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=Dharmshala&destination=Chandigrah&avoid=tolls";

            MapsActivity.DownloadTask downloadTask = new MapsActivity.DownloadTask();
            downloadTask.execute(strUrl);
        } else if(type.equals("showLocation")){
            Toast.makeText(getApplicationContext(),"Here! "+ latLng ,Toast.LENGTH_LONG).show();
//            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

//        Toast.makeText(getApplicationContext(),"mylocation latitude and longitue"+latitude+" "+latitude,Toast.LENGTH_LONG).show();
        //Add a marker in Sydney and move the camera

        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in" +destination));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //modified code
        //mMap.setMyLocationEnabled(true);
    }

    public void onSearch() {
        List<Address> addList = null;
        Geocoder geocoder = new Geocoder(this);
        try {
                addList = geocoder.getFromLocationName(destination, 1);
        } catch (Exception e) {
            Log.e("", e.toString());
        }
        android.location.Address address = addList.get(0);
        latLng = new LatLng(address.getLatitude(), address.getLongitude());
        Log.e("latlongg",latLng.toString());
    }

    public String downloadUrl(String url1) {


        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(url1);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();
            iStream.close();
            urlConnection.disconnect();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), " " + e, Toast.LENGTH_LONG).show();
        } finally {

        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            MapsActivity.ParserTask parserTask = new MapsActivity.ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++)
                {
                    HashMap<String,String> point = path.get(j);

                    if (j == 0)
                    {    // Get distance from the list
                        distance = (String) point.get("distance");
                        //  Toast.makeText(getApplicationContext(),"distance"+distance,Toast.LENGTH_LONG).show();

                        continue;
                    } else if (j == 1)
                    { // Get duration from the list
                        duration = (String) point.get("duration");
                        // Toast.makeText(getApplicationContext(),"duration"+duration,Toast.LENGTH_LONG).show();

                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);

                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);

                txtDuration.setText("Distance :"+distance+ "Duraion"+duration);


            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);

        }
    }
}
