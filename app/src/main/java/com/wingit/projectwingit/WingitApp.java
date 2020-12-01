package com.wingit.projectwingit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.wingit.projectwingit.debug.WingitLogging;
import com.wingit.projectwingit.io.LambdaRequests;
import com.wingit.projectwingit.io.LambdaResponse;
import com.wingit.projectwingit.utils.LoginInfo;
import com.wingit.projectwingit.utils.WingitLambdaConstants;

import org.json.JSONException;
import org.json.JSONObject;

import static com.wingit.projectwingit.io.LambdaRequests.createAccount;
import static com.wingit.projectwingit.utils.WingitLambdaConstants.*;
import static com.wingit.projectwingit.utils.WingitUtils.hashPassword;

/**
 * The main application class
 */
public class WingitApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginInfo.APP_CONTEXT = getApplicationContext();

        final TextView textView = (TextView) findViewById(R.id.testText);
        String passwordHash = hashPassword("wingit!1");
        String username = "JustWingit";
        LambdaResponse r = LambdaRequests.login(username, passwordHash);
        LambdaResponse response = LambdaRequests.changePassword("cse110wingit@gmail.com", "264856", passwordHash);
        textView.post(new Runnable() {
            public void run() {
                try{e();}catch(Exception a){textView.setText("JSON EXCPEIONT: " + a.getMessage());}
            }

            public void e() throws Exception{
                String s = "" + response.getResponseJSON();
                //String s = ""+response.getResponseJSON().toString();
                textView.setText(s);
            }
        });
    }

}