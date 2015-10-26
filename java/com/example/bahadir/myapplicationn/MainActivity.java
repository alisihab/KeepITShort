package com.example.bahadir.myapplicationn;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import me.pushy.sdk.Pushy;

public class MainActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        Log.i("tago" , "Main Activity onCreate");
        super.onCreate(savedInstanceState);
        Pushy.listen(this);
        setContentView(R.layout.activity_main);
        printHashKey();
        new registerForPushNotificationsAsync().execute();
    }
    protected void onStart() {
        Log.i("tago" , "Main Activity onStart");
        super.onStart();
    }
    protected void onResume() {
        Log.i("tago" , "Main Activity onResume");
        super.onResume();
    }
    protected void onPause() {
        Log.i("tago" , "Main Activity onPause");
        super.onPause();
    }
    protected void onStop() {
        Log.i("tago" , "Main Activity onStop");
        super.onStop();
    }
    protected void onDestroy() {
        Log.i("tago" , "Main Activity onDestroy");
        super.onDestroy();
    }
    private void printHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.bahadir.myapplicationn",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("tago","keyhash= "+  Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private class registerForPushNotificationsAsync extends AsyncTask<Void, Void, Exception> {
        protected Exception doInBackground(Void... params) {
            try {
                // Acquire a unique registration ID for this device
                String registrationId = Pushy.register(getApplicationContext());
                registrationIdSharedPrefKaydet(registrationId);
                Log.i("tago", registrationId);

                // Save the registration ID in your backend database:
                // You must implement this function
                //sendRegistrationIdToBackendServer(registrationId);
            } catch (Exception exc) {
                // Return exc to onPostExecute
                Log.i("tago", "register olmadı");
                Log.i("tago" , exc.getMessage());
                return exc;
            }
            // We're good
            return null;
        }

        private void registrationIdSharedPrefKaydet(String registrationId) {
            SharedPreferences sp = getSharedPreferences("kullaniciverileri" , Context.MODE_PRIVATE);
            SharedPreferences.Editor sedit = sp.edit();
            sedit.putString("registrationid" , registrationId);
            sedit.commit();
            Log.i("tago" , "sharedpreference registration id kaydedildi");
        }

        @Override
        protected void onPostExecute(Exception exc) {
            // Failed?
            if (exc != null) {
                // Show error in toast
                Log.i("tago" , exc.toString());
                return;
            }

            // Succeeded, do something
        }

        // Example implementation
        public void sendRegistrationIdToBackendServer(String registrationId) throws Exception {
            String charset = "UTF-8";
            String param1 = "is";
            String query = "";
            try {
                query = String.format("param1=%s", URLEncoder.encode(param1, charset));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Change this accordingly
            HttpURLConnection connection;
            //URL sendRegIdRequest = new URL("http://185.22.184.103/project/update_regid.php?id=4&regid="+registrationId);
            Log.i("tago" ,"gonder " + registrationId);
            connection = (HttpURLConnection) new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy/update_regid.php?id=4&regid="+registrationId).openConnection();

            // Execute the request
            //sendRegIdRequest.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            try {
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (Exception e){
                Log.i("tago" , "hillfe hillfe");
            }
        }
    }
    }

