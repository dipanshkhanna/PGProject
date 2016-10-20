package com.dipansh.pg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText email,password;
    Button login,reset,forgotPass,register;
    String line,Email,pass,response;
    HttpURLConnection urlConnection;
    OutputStream outputStream;
    OutputStreamWriter outputStreamWriter;
    BufferedWriter bufferedWriter;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        email = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.pass);

        login = (Button)findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);
        reset = (Button)findViewById(R.id.reset);
        forgotPass = (Button)findViewById(R.id.forgot);
    }

    public void Login(View v){
        try{
            Email = email.getText().toString();
            pass = password.getText().toString();

            if(Email.equals(null) || pass.equals(null)){
                Toast.makeText(getApplicationContext(),"Fill Both Fields",Toast.LENGTH_SHORT).show();
            }else{
                Login();

                SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("loggedIn", Email);
                editor.commit();
            }
        }catch (Exception e){

        }

    }

    public void Reset(View v){
        email.setText("");
        password.setText("");
        email.requestFocus();
    }

    public void ForgotPass (View v){
        String Email = email.getText().toString();
        Intent i = new Intent(MainActivity.this, Forgot_Pass.class);
        i.putExtra("email",Email);
        startActivity(i);
    }

    public void CreatCon(){
        try{
            URL url = new URL("http://10.0.2.2/Android/pgproject_php/login.php");
            urlConnection =(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
        }catch (Exception e){

        }
    }

    public void Register(View v){
        Intent i = new Intent(MainActivity.this, Forgot_Pass.class);
        startActivity(i);
    }

    public void Login(){
        CreatCon();
        try{
            outputStream = urlConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            String data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(Email,"UTF-8")+
                    "&"+URLEncoder.encode("pass","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line+ "\n");
            }
            JSONObject jsonObject = new JSONObject(sb.toString());

            Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_LONG).show();
            bufferedWriter.flush();
            bufferedWriter.close();
            Reset(v);

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }

}
