package com.dipansh.pg;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PGListView extends AppCompatActivity

{
    String[] pgs;
    String[] address;
    String[] contact;
    String[] rating;
    int[] flags = new int[]{
            R.drawable.india,
            R.drawable.srilanka,
            R.drawable.china,
            R.drawable.bangladesh,
            R.drawable.nepal,
            R.drawable.afghanistan,
            R.drawable.nkorea,
            R.drawable.skorea,
            R.drawable.japan
    };
    int loop=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgview);
        StrictMode.ThreadPolicy threadPolicy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        SelectAll();

        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<loop;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt", " " + pgs[i]);
            hm.put("add", address[i]);
            hm.put("con","Contact : " + contact[i]);
            hm.put("rate", rating[i]);
            hm.put("flag", Integer.toString(flags[i]) );
            aList.add(hm);

        }


        String[] from = { "flag","txt","add","con","rate"};


        int[] to = { R.id.flag,R.id.txt,R.id.cur,R.id.con,R.id.tvstar};


        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_layout, from, to);


        ListView listView = ( ListView ) findViewById(R.id.listview);


        listView.setAdapter(adapter);


        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {

                LinearLayout linearLayoutParent = (LinearLayout) container;

                LinearLayout linearLayoutChild = (LinearLayout ) linearLayoutParent.getChildAt(1);


                TextView tvPg = (TextView) linearLayoutChild.getChildAt(0);
                TextView tvPgA = (TextView) linearLayoutChild.getChildAt(1);
                TextView tvPgC = (TextView) linearLayoutChild.getChildAt(2);
                TextView tvPgR = (TextView) linearLayoutChild.getChildAt(3);

                Toast.makeText(getBaseContext(), tvPg.getText().toString(), Toast.LENGTH_SHORT).show();
                String pgname=tvPg.getText().toString();
                String pgaddr=tvPgA.getText().toString();
                String pgcon=tvPgC.getText().toString();
                int pgrating=Integer.valueOf(tvPgR.getText().toString());
                Intent  intent=new Intent(getApplicationContext(),Pgdetail.class);
                intent.putExtra("pgname",pgname);
                intent.putExtra("pgadd",pgaddr);
                intent.putExtra("pgcon",pgcon);
                intent.putExtra("pgrat",pgrating);
                startActivity(intent);
            }
        };


        listView.setOnItemClickListener(itemClickListener);
    }
    public void SelectAll() {

        String line = "";
        String responseJsonData = null;

        try {
            StringBuilder sb = new StringBuilder();
            String x = "";
            URL httpurl = new URL("http://10.0.2.2/Android/pgproject_php/pglist.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) httpurl.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            Toast.makeText(getApplicationContext(), "connection  established", Toast.LENGTH_SHORT).show();
            OutputStream OS = httpURLConnection.getOutputStream();
            httpURLConnection.connect();
            Toast.makeText(getApplicationContext(), "connect", Toast.LENGTH_LONG).show();


            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));

            if (in != null) {
                while ((line = in.readLine()) != null) {
                    sb.append(line + "\n");
                    x = sb.toString();
                }
                responseJsonData = new String(x);
//                Toast.makeText(getApplicationContext()," "+responseJsonData,Toast.LENGTH_LONG).show();
                Log.e("Exp- ", responseJsonData);

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "webservice2 " + e, Toast.LENGTH_LONG).show();
            Log.e("Exp- ", e.toString());

        }
        try {

            JSONArray ja = new JSONArray(responseJsonData);
            JSONObject jo = null;

          pgs = new String[ja.length()];
           address = new String[ja.length()];
           contact = new String[ja.length()];
           rating = new String[ja.length()];
            loop = ja.length();
            for (int i = 0; i < ja.length(); i++) {

                jo = ja.getJSONObject(i);

                pgs[i] = jo.getString("pgname");
                address[i] = jo.getString("pgaddress");
                contact[i] = jo.getString("pgcontact");
                rating[i] = jo.getString("pgrating");


                Log.e("PGGGG", rating[i]);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), " Web Servic" + e, Toast.LENGTH_LONG).show();
        }

    }
}


