package com.dipansh.pg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Pgdetail extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener{

    String pgname,pgadd,line;
    float numStars;
    TextView txtPGName,txtPGAddress,txtPGContact;
    RatingBar ratingBar;
    HttpURLConnection urlConnection;
    OutputStream outputStream;
    OutputStreamWriter outputStreamWriter;
    BufferedWriter bufferedWriter;
    Button location,route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgdetail);
        txtPGName=(TextView)findViewById(R.id.DPG_name);
        txtPGAddress=(TextView)findViewById(R.id.DPG_Address);
        txtPGContact=(TextView)findViewById(R.id.DPG_Contact);
        ratingBar = (RatingBar)findViewById(R.id.DPG_RatingBar);
        location = (Button)findViewById(R.id.loc);
        route = (Button)findViewById(R.id.route);

        Bundle bundle=getIntent().getExtras();
        pgname=bundle.getString("pgname");
        pgadd=bundle.getString("pgadd");
        String pgcontact=bundle.getString("pgcon");
        int pgrating=bundle.getInt("pgrat");

        txtPGName.setText(pgname);
        txtPGAddress.setText(pgadd);
        txtPGContact.setText(pgcontact);
        ratingBar.setRating(pgrating);
        ratingBar.setOnRatingBarChangeListener(this);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
        numStars = ratingBar.getRating();
        Log.i("Changed??", String.valueOf(numStars));
        DatabasePush();
    }

    public void DatabasePush(){
        CreatCon();
        try{
            outputStream = urlConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            String data = URLEncoder.encode("pgname","UTF-8")+"="+URLEncoder.encode(pgname,"UTF-8")+
                    "&"+URLEncoder.encode("rating","UTF-8")+"="+URLEncoder.encode(String.valueOf(numStars),"UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line+ "\n");
            }
            String resp = sb.toString();

            Toast.makeText(getApplicationContext(),resp,Toast.LENGTH_LONG).show();
            bufferedWriter.flush();
            bufferedWriter.close();

        }catch (Exception e){
            Log.e("Excp--", e.toString());
        }
    }

    public void ShowLocation(View v){
        Intent intent = new Intent(Pgdetail.this,MapsActivity.class);
        intent.putExtra("address",pgadd);
        intent.putExtra("type","showLocation");
        startActivity(intent);
    }

    public void ShowRoute(View v){
        Intent intent = new Intent(Pgdetail.this,MapsActivity.class);
        intent.putExtra("address",pgadd);
        intent.putExtra("type","showRoute");
        startActivity(intent);
    }

    public void CreatCon(){
        try{
            URL url = new URL("http://10.0.2.2/Android/pgproject_php/rating.php");
            urlConnection =(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
        }catch (Exception e){

        }
    }
}
