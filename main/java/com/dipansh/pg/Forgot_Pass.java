package com.dipansh.pg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Forgot_Pass extends AppCompatActivity {

    EditText email;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__pass);

        email = (EditText)findViewById(R.id.email);
        try {
            Bundle bundle = getIntent().getExtras();
            id = bundle.getString("email");
        }catch (Exception e){

        }

        if(id!="" && id!=null){
            email.setText(id);
        }
    }
}
