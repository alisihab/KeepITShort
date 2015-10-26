package com.example.bahadir.myapplicationn;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    ListView list1;
    View view;
    String charset;
    String query;
    CevredekileriGoster cG;
    ArrayList<Insann> InsanListesi;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }
    private String idSharedPreferenceAl() {
        SharedPreferences sP = getActivity().getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String veritabani_id = sP.getString("veritabani_id", "default id");
        Log.i("tago", "Page Fragment haf�zadan ula�t�m veritaban� id = " + veritabani_id);
        return veritabani_id;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cevrendekiler, container, false);
        list1 = (ListView) view.findViewById(R.id.listView);
        Log.i("tago", "Page Fragment onCreateView");
        String veritabani_id = idSharedPreferenceAl();
        if (!veritabani_id.equals("default")) {
            cevredekilericek(veritabani_id);
        } else if (veritabani_id.equals("default")) {
            do{
                idSharedPreferenceAl();
            }while(!veritabani_id.equals("default"));
            cevredekilericek(veritabani_id);
        } else {
            Log.i("tago", "Anan� avrad�n� sikerim senin");
        }
        return view;
    }
    private void cevredekilericek(String veritabani_id) {
        cG = new CevredekileriGoster();
        cG.execute(veritabani_id);
    }

    private class CevredekileriGoster extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            charset = "utf-8";
            String param1 = "id";
            try {
                query = String.format("param1=%s", URLEncoder.encode(param1, charset));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("tago", "Page Fragment cevredekileri goster ba�lat�ld�");
            try {
                return cevredekilerigor(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "olmad�";
            }
        }

        public String cevredekilerigor(String id) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy//near_users?id=" +id).openConnection();
                Log.i("tago", "Page Fragment cevredekileri gor bagı kuruldu");
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "* /*");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            try {
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
                if(connection.getResponseCode() == 200) {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Log.i("tago", "InputStream");
                    String inputline=null;

                    for(int i=0 ; i<3 ; i++){
                        inputline = in.readLine();
                        Log.i("tago" , ""+i+" for inputline= " + inputline);
                    }
                    /*while ((inputline = in.readLine()) != null) {
                        Log.i("tago", "Caxo camcamcam bambam" + inputline);
                        Log.i("tago", "while inputline = " + inputline);
                    }*/
                            InsanListesi = new ArrayList();
                            JSONArray jsono = new JSONArray(inputline);
                            for (int i = 0; i < jsono.length(); i++) {
                                JSONObject object = jsono.getJSONObject(i);
                                Insann insann = new Insann();
                                insann.setId(object.optString("id"));
                                insann.setName(object.optString("name"));
                                insann.setUrl(object.optString("profile_picture"));
                                insann.setUzaklik(object.optString("distance"));
                                InsanListesi.add(insann);
                            }
                    }
                    /*
                   InsanListesi = new ArrayList();
                    for(int i = 0 ; i < 12 ; i++){
                        Insann insan = new Insann();
                        insan.setId("" + i);
                        insan.setName("Bahadır");
                        insan.setUrl("https://graph.facebook.com/116429068707650/picture?type=large");
                        InsanListesi.add(insan);
                    }
                }*/
                else
                {
                    in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    Log.i("tago" , "Error Stream");
                    String inputline;
                    while ((inputline = in.readLine()) != null) {
                        Log.i("tago", "camcamcam" + inputline);
                }
                    InsanListesi = new ArrayList();
                    JSONArray jsono = new JSONArray(inputline);
                    for (int i = 0; i < jsono.length(); i++) {
                        JSONObject object = jsono.getJSONObject(i);
                        Insann insann = new Insann();
                        insann.setId(object.optString("id"));
                        insann.setName(object.optString("name"));
                        insann.setUrl(object.optString("profile_picture"));
                        insann.setUzaklik(object.optString("distance"));
                        InsanListesi.add(insann);
                    }
                }
                in.close();
                Log.i("tago", "Page Fragment cevredekileri gor inputline yazd�m");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("tago", "Page Fragment cevredekileri gor yazamad�m");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("tago", "json Exception");
            }
            return "inputline";
        }

        @Override
        protected void onPostExecute(String s) {
            InsannAdapter adapter = new InsannAdapter (getActivity() , R.layout.listview_item , InsanListesi);
            list1.setAdapter(adapter);
        }
    }
    }