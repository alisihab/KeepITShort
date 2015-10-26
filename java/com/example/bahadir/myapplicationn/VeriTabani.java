package com.example.bahadir.myapplicationn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class VeriTabani {

    String url;
    String query;
    String charset;
    ArkadanVurdur aV;
    String is;
    String urrl;
    String lat;
    String longi;
    String id;
    String veritabani_id = "default";
    Context context;

    public VeriTabani(Context context){}
    public VeriTabani(Context context , String is , String urrl , String lat , String longi){
        this.is=is;
        this.urrl = urrl;
        this.lat=lat;
        this.longi=longi;
        this.context = context;
        tanımlar();
        Log.i("tago" , "beşli veritabanı objesi oluşturuldu");
    }
    public VeriTabani(Context context , String id , String lato , String longio){
        this.id=id;
        this.lat=lato;
        this.longi = longio;
        this.context = context;
        yenilemetanimlar();
    }

    public void tanımlar() {
        charset = "UTF-8";
        url = "http://185.22.184.103/project/connection.php?name=bahadirturk&url=http://www.google.com&long=33.2132123&lat=33.2322322";
        String param1 = "is";
        String param2 = "urrl";
        String param3 ="longi";
        String param4 ="lat";
            try {
                query = String.format("param1=%s&param2=%s&param3=%s&param4=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset),
                        URLEncoder.encode(param3, charset),URLEncoder.encode(param4, charset ));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    public void yenilemetanimlar(){
        url = "http://185.22.184.103/project/connection.php?name=bahadirturk&url=http://www.google.com&long=33.2132123&lat=33.2322322";
        charset = "UTF-8";
    String param1 = "id";
    String param2 = "long";
    String param3 ="lat";
    try {
        query = String.format("param1=%s&param2=%s&param3=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset),
                URLEncoder.encode(param3, charset) );
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    public void firehttprequest() {
        URLConnection connection = null;
        try {
            connection = new URL("http://185.22.184.103/project/connection.php?name="+URLEncoder.encode("Faarık Fazıl", "ISO-8859-9")+"&url="+urrl+"&long="+longi+"&lat="+lat).openConnection();
            InputStream response = connection.getInputStream();
            Log.i("tago", "tart");

        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setRequestProperty("Accept-Charset", charset);

    }
    public void firehttprequestwithqueryparameters() {
    }
    public void lokasyonuyenile(){
        aV = new ArkadanVurdur();
        aV.execute(url);
    }
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class ArkadanKaynat extends AsyncTask<String , Void , String> {

        protected String doInBackground(String... params) {
            Log.i("tago", "VeriTabani İlk Location verme işlemi başlatıldı");
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
                Log.i("tago" ,"Veri Tabani bilgiyi gonder" + is.trim());
                Log.i("tago" , "Veri Tabani bilgiyi gonder" + urrl);
                Log.i("tago" , "VeriTabani bilgiyi gonder" + longi);
                Log.i("tago" ,"VeriTabani bilgiyi gonder" + lat);
                connection = (HttpURLConnection)new URL("http://185.22.184.103/project/connection.php?name="+is+"&url="+urrl+"&long="+longi+"&lat="+lat).openConnection();
                Log.i("tago" ,"VeriTabani bagı kurdum");
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


            try{
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                try {
                    int a = connection.getResponseCode();
                    String b = connection.getResponseMessage();
                    Log.i("tago", "rerere" + a + " " + b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader in;
                if(connection.getResponseCode() == 200)
                {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Log.i("tago" , "InputStream");
                }
                else
                {
                    in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    Log.i("tago" , "Error Stream");
                }
                String inputline;
                while((inputline=in.readLine()) != null){
                    Log.i("tago" ,"zalazort" + inputline);
                }in.close();
                Log.i("tago", "VeriTabani yazdım");
            }catch(IOException e){
                e.printStackTrace();
                Log.i("tago" , "VeriTabani yazamadım");
            }
            try {
                int status = connection.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String inputline;
                while((inputline=in.readLine()) != null){
                    Log.i("tago" ,"zalazort " +  inputline);
                    JsondanCevir(inputline);
                }in.close();
                Log.i("tago" , "VeriTabani status= " +status);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "alabama";
        }

        public void JsondanCevir(String x) {
            JSONArray jsonarray = null;
            String data = "";
            try {
                jsonarray = new JSONArray(x);
                Log.i("tago" ,"zalazorta" + x);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("tago", "" + jsonarray.length());
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = jsonarray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                veritabani_id = jsonObject.optString("id").toString();
                Log.i("tago", "tur attım");
                data = data +  " id= " + veritabani_id;
            }
            Log.i("tago", data);
            Log.i("tago" ,veritabani_id );
            idSharedPreferenceKaydet(context);
        }

        private void idSharedPreferenceKaydet(Context a) {
            SharedPreferences sP = a.getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = sP.edit();
            prefEditor.putString("veritabani_id", veritabani_id);
            Log.i("tago", "Telefon hafızasına aldım veritabanı id = " + veritabani_id);
            prefEditor.commit();
        }


    }
    public class ArkadanVurdur extends AsyncTask<String , Void , String>{

        protected String doInBackground(String... params) {

            Log.i("tago", "VeriTabani arkadan vurdurma başlatıldı");
            try{
                return lokasyonuyenilee();
            }catch(Exception e){
                e.printStackTrace();
                return "olmadı";
            }
        }

        private String lokasyonuyenilee() {
            URLConnection connection = null;

            try{
                Log.i("tago" , id);
                Log.i("tago" , longi);
                Log.i("tago" , lat);
                connection = new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy/update_location.php?id="+id+
                        "&long="+longi+"&lat="+lat).openConnection();
                Log.i("tago" ,"VeriTabani Arkadan vurdurma bagı kurdum");
            }catch(IOException e){
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            try{
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                InputStream response = connection.getInputStream();
                Log.i("tago" , "VeriTabani Arkadan vurdurma yazdım");
            }catch(IOException e){
                e.printStackTrace();
                Log.i("tago" , "VeriTabani Arkadan vurdurma yazamadım");
            }
            return "alabama";
        }
    }

}
