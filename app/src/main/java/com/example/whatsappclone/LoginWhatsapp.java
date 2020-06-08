package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginWhatsapp extends AppCompatActivity implements View.OnClickListener{
    private EditText signupemail,signuppassword;
    private Button btnlogin;
    private TextView signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_whatsapp);
        setTitle("Login");
        signupemail=findViewById(R.id.LoginEmail);
        signuppassword=findViewById(R.id.LoginPassword);
        btnlogin=findViewById(R.id.btnLogin);
        signup=findViewById(R.id.loginSignup);
        btnlogin.setOnClickListener(this);
        signup.setOnClickListener(this);
        if(ParseUser.getCurrentUser()!=null) {
            ParseUser.getCurrentUser().logOut();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                try{
                    if (signupemail.getText().toString().equals("") || signuppassword.getText().toString().equals("")) {
                        FancyToast.makeText(LoginWhatsapp.this, "Email , Password both are required", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                    } else {
                        final ProgressDialog progressDialog = new ProgressDialog(this);
                        progressDialog.setMessage("Logning up");
                        progressDialog.show();
                        ParseUser.logInInBackground(signupemail.getText().toString(), signuppassword.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (user != null && e == null) {
                                    FancyToast.makeText(LoginWhatsapp.this, "Logged in", Toast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                    transitionToSocialMediaActivity();
                                } else {
                                    FancyToast.makeText(LoginWhatsapp.this, e.getMessage(), Toast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
                    }
                }catch (Exception e){
                    FancyToast.makeText(LoginWhatsapp.this, e.getMessage(), Toast.LENGTH_LONG, FancyToast.ERROR, true).show();
                }
                break;
            case R.id.loginSignup:
                finish();
                break;
        }
    }
    private void transitionToSocialMediaActivity(){
        Intent intent = new Intent(LoginWhatsapp.this,InsideActivity.class);
        startActivity(intent);
        finish();
    }
}