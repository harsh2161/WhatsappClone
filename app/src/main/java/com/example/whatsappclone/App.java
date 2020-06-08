package com.example.whatsappclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.Parse.Configuration;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this).
                applicationId("xyEWFu0cQqxktVNImZVmr7kbdU04cu2yG2fuhTZg")
                .clientKey("oZjSTmeaHEw8izJgXKsh2pZOnQyqNCDFyro4i8iC")
                .server("https://parseapi.back4app.com/").build());
    }
}
