package com.example.bahadir.myapplicationn;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;

public class TakipServisi extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
,LocationListener{
    Location mCurrentLocation;
    Location mFirstLocation;
    ArkadanKaynat aK;
    String veritabani_id;
    String firstname;
    String lastname;
    String issim;
    String isim;
    String resimurl;
    GoogleApiClient googleclient;
    String regid;
    public void onCreate() {
        super.onCreate();
        Log.i("tago" , "Takip Servisi onCreate");
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("tago", "Takip Servisi onStartCommand");
        Runnable r = new Runnable() {
            public void run() {
                googleclient = new GoogleApiClient.Builder(TakipServisi.this).addApi(LocationServices.API).addConnectionCallbacks(TakipServisi.this)
                        .addOnConnectionFailedListener(TakipServisi.this).build();
                Log.i("tago", "Takip servisi google clienti yaptım");
                if(googleclient!=null) {
                    googleclient.connect();
                    Log.i("tago" , "Takip Servisi google client bos degil");
                }
                Log.i("tago" , "Takip Servisi client baglandım");
            }
        };
        Thread islem = new Thread(r);
        islem.start();
        isim = intent.getStringExtra("isim");
        resimurl = intent.getStringExtra("resimurl");
        firstname = intent.getStringExtra("firstname");
        lastname = intent.getStringExtra("lastname");
        Log.i("tago" , "Takip Servisi firstname= " + firstname);
        Log.i("tago" , "Takip Servisi lastname= " + lastname);
        Log.i("tago" , "Takip Servisi isim " + isim);
        firstname = firstname.replace(" " ,".");
        Log.i("tago", "Takip servisi yenilenmis firstname = " + firstname);
        issim = firstname+"-"+lastname;
        return Service.START_STICKY;
    }
    public void onDestroy() {
        googleclient.disconnect();
        Log.i("tago", "Takip Servisi onDestroy çağırıldı");
        super.onDestroy();
        Log.i("tago", "Takip Servisi baglantı bitirildi");
    }
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onConnected(Bundle bundle) {
        Log.i("tago" , "Takip Servisi onConnected");
        mFirstLocation = LocationServices.FusedLocationApi.getLastLocation(googleclient);
        if (mFirstLocation != null) {
            Log.i("tago" ,"Takip Servisi OnConnected " + "First Latitude = " + String.valueOf(mFirstLocation.getLatitude())+
                    " First Longitude" +String.valueOf(mFirstLocation.getLongitude()) );
        }

        LocationRequest locrequest = new LocationRequest();
        locrequest.setInterval(100000);
        locrequest.setFastestInterval(50000);
        locrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        String veritabani_idd = idSharedPreferenceAl();
        regid = regidSharedPrefAl();
        if(veritabani_idd.equals("default id")||veritabani_idd==null){
            aK = new ArkadanKaynat(regid, issim ,resimurl ,String.valueOf(mFirstLocation.getLongitude())
                   ,String.valueOf(mFirstLocation.getLatitude()) );
            aK.execute();
        }else{
            aK = new ArkadanKaynat(regid ,issim ,resimurl ,String.valueOf(mFirstLocation.getLongitude())
                    ,String.valueOf(mFirstLocation.getLatitude()) );
            aK.execute();
        }
            LocationServices.FusedLocationApi.requestLocationUpdates(googleclient, locrequest, this);
    }
    private String regidSharedPrefAl() {
        SharedPreferences sp = getSharedPreferences("kullaniciverileri" , Context.MODE_PRIVATE);
        String regid = sp.getString("registrationid" , "regid ulasilamadi");
        Log.i("tago" , "Takip Servisi regid sharedpreference aldım= " + regid);
        return regid;
    }
    public void onConnectionSuspended(int i) {

    }
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    public void onLocationChanged(Location location) {
        mCurrentLocation = location ;
        String mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        Log.i("tago", "Takip Servisi Current Location " + String.valueOf(mCurrentLocation.getLatitude()) + " " + String.valueOf(mCurrentLocation.getLongitude()));
        Log.i("tago", "Takip Servisi " + mLastUpdateTime);
        Thread a = new Thread(){
            public void run(){
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                veritabani_id= idSharedPreferenceAl();
                Log.i("tago", "Takip Servisi Location Change id = " + veritabani_id);
                if(veritabani_id!=null){
                    VeriTabani vRefresh = new VeriTabani(getApplicationContext() , veritabani_id,String.valueOf(mCurrentLocation.getLatitude()),
                            String.valueOf(mCurrentLocation.getLongitude()));
                    vRefresh.lokasyonuyenile();
                }
            }
        };
        a.start();
    }
    public String idSharedPreferenceAl() {
            SharedPreferences sP = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
            String veritabani_id = sP.getString("veritabani_id","default id");
            Log.i("tago", "Takip Servisi hafızadan ulaştım veritabanı id = " + veritabani_id);
            return veritabani_id;
        }

    public class ArkadanKaynat extends AsyncTask<String , Void , String> {

        String charset = "UTF-8";
        String param1 = "isim";
        String param2 = "resimurrl";
        String param3 ="longi";
        String param4 ="lat";
        String param5 = "regid";
        String query;
        String isim;
        String resimurrl;
        String longi;
        String lat;
        String regid;
        public ArkadanKaynat(String regid ,String isim , String resimurrl , String longi , String lat){
            this.isim = isim;
            this.resimurrl = resimurrl;
            this.longi = longi;
            this.lat = lat;
            this.regid = regid;
        }


        protected String doInBackground(String... params) {

            try {
                query = String.format("param1=%s&param2=%s&param3=%s&param4=%s&param5=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset),
                        URLEncoder.encode(param3, charset),URLEncoder.encode(param4, charset ),URLEncoder.encode(param5, charset ));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("tago", "Takip Servisi VeriTabani İlk Location verme işlemi başlatıldı");
            try{
                return bilgiyigonder();
            }catch(Exception e){
                e.printStackTrace();
                return "olmadı";
            }
        }

        public String bilgiyigonder() {
            HttpURLConnection connection = null;
            try{
                Log.i("tago" ,"Takip Servisi VeriTabani ilk bilgiyi gonder" + issim.trim());
                Log.i("tago" , "Takip Servisi VeriTabani ilk bilgiyi gonder" + resimurl);
                Log.i("tago" , "Takip Servisi VeriTabani ilk bilgiyi gonder" + longi);
                Log.i("tago" ,"Takip Servisi VeriTabani ilk bilgiyi gonder" + lat);
                Log.i("tago" , "Takip Servisi Veritabani ilk bilgiyi gonder" + regid);
                //connection = (HttpURLConnection)new URL("http://185.22.184.103/project/connection.php?name="+isim+"&url="+resimurl+
                  //      "&long="+longi+"&lat=" +lat).openConnection();
                connection = (HttpURLConnection)new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy/connection.php?name="+isim+"&url="+resimurl+
                        "&long="+longi+"&lat=" +lat+"&regid="+regid).openConnection();
                Log.i("tago" ,"Takip Servisi VeriTabani bagı kurdum");
            }catch(IOException e){
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "*/*");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            String inputline = "";

            try{
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                try {
                    int a = connection.getResponseCode();
                    String b = connection.getResponseMessage();
                    Log.i("tago", "Takip Servisi rerere" + a + " " + b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader in;
                if(connection.getResponseCode() == 200) {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Log.i("tago", "Takip Servisi InputStream");
                    while ((inputline = in.readLine()) != null) {
                        Log.i("tago", "Takip Servisi VeriTabani idyi dondurdu= "+ inputline);
                        idSharedPreferenceKaydet(inputline);
                    }
                }
                else
                {
                    in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    Log.i("tago" , "Takip Servisi Error Stream");
                }
                in.close();
                Log.i("tago", "Takip Servisi VeriTabani yazdım");
                return inputline;
            }catch(IOException e){
                e.printStackTrace();
                Log.i("tago", "Takip Servisi VeriTabani yazamadım");
            }
            return inputline;
        }


        protected void onPostExecute(String s) {
            Intent i = new Intent(TakipServisi.this, AnaAkim.class);
            i.putExtra("isim" , isim);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

        private void idSharedPreferenceKaydet(String id) {
            Log.i("tago" , "Takip Servisi SharedPreference id kaydedilecek= " + id);
            SharedPreferences sP =getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = sP.edit();
            prefEditor.putString("veritabani_id", id);
            Log.i("tago", "Takip Servisi Telefon hafızasına aldım veritabanı id = " + id);
            prefEditor.commit();
        }
    }
}

