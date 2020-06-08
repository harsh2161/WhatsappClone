package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText signupusername,signupemail,signuppassword;
    private Button btnsignup;
    private TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        setTitle("Sign Up");
        signupemail= findViewById(R.id.SignUpEmail);
        signupusername=findViewById(R.id.SignUpUsername);
        signuppassword=findViewById(R.id.SignUpPassword);
        btnsignup=findViewById(R.id.btnSignUp);
        login=findViewById(R.id.signupLogin);
        btnsignup.setOnClickListener(this);
        login.setOnClickListener(this);
        signuppassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(btnsignup);
                }
                return false;
            }
        });
        if(ParseUser.getCurrentUser()!=null){
            transitionToSocialMediaActivity();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignUp:
                try{
                    if(signupemail.getText().toString().equals("")||signupusername.getText().toString().equals("")||signuppassword.getText().toString().equals("")){
                        FancyToast.makeText(MainActivity.this,"Email , Username , Password all are required", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                    }else {
                        ParseUser User = new ParseUser();
                        User.setEmail(signupemail.getText().toString());
                        User.setUsername(signupusername.getText().toString());
                        User.setPassword(signuppassword.getText().toString());
                        final ProgressDialog progressDialog = new ProgressDialog(this);
                        progressDialog.setMessage("Signing up " + signupusername.getText().toString());
                        progressDialog.show();
                        User.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    FancyToast.makeText(MainActivity.this, "Signup successfully.", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                    transitionToSocialMediaActivity();
                                } else {
                                    FancyToast.makeText(MainActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
                    }
                }catch(Exception e){
                    FancyToast.makeText(MainActivity.this,e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                }
                break;
            case R.id.signupLogin:
                Intent intent = new Intent(MainActivity.this,LoginWhatsapp.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    private void transitionToSocialMediaActivity(){
        try {
            Intent intent = new Intent(MainActivity.this, InsideActivity.class);
            startActivity(intent);
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}