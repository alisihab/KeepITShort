package com.example.bahadir.myapplicationn;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GrupSohbeti extends Activity {

    ListView liste1;
    Button buton1 , buton2 ;
    EditText etv1;
    boolean taraf;
    String yazaninmesaj;
    ArkadanToplu aT;
    String kanaladi;
    List<Mesaj> mesajlistesi = new ArrayList();
    MesajArrayAdapter adapter ;
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.grupsohbeti);
        tanimlar();
    }

    private void tanimlar() {
        kanaladi = getIntent().getExtras().getString("kanaladi");
        TextView tv1 = (TextView) findViewById(R.id.textViewisim);
        tv1.setText(kanaladi);
        liste1 = (ListView) findViewById(R.id.listViewmesaj);
        buton1 = (Button) findViewById(R.id.buttongonder);
        buton2 = (Button) findViewById(R.id.buttongeri);
        etv1 = (EditText) findViewById(R.id.editTextyazi);
        etv1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendChatMessage();
            }
        });
        adapter = new MesajArrayAdapter(getApplicationContext(), R.layout.mesaj, mesajlistesi);
        liste1.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        liste1.setAdapter(adapter);
        adapter.registerDataSetObserver(new DataSetObserver() {
            public void onChanged() {
                super.onChanged();
                liste1.setSelection(adapter.getCount() - 1);
            }
        });

    }

    private boolean sendChatMessage() {
        yazaninmesaj = etv1.getText().toString();
        String yazaninid = SharedPrefIdAl();
        String yazaninnicki = SharedPrefNickAl();
        aT = new ArkadanToplu(yazaninid,yazaninnicki,kanaladi,yazaninmesaj);
        aT.execute("sikim");
        adapter.add(new Mesaj(taraf, yazaninmesaj));
        etv1.setText("");
        mesajiexternalkaydet(yazaninmesaj);
        return true;
    }

    private String SharedPrefNickAl() {
        SharedPreferences sP = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String yazaninnick = sP.getString("kulanicinick" , "default");
        return yazaninnick;
    }

    private String SharedPrefIdAl() {
        SharedPreferences sP = getSharedPreferences("kullaniciverileri" , Context.MODE_PRIVATE);
        String veritabani_id= sP.getString("veritabani_id", "default");
        return veritabani_id;
    }

    private void mesajiexternalkaydet(String yazaninmesaj) {
    }

    private class ArkadanToplu extends AsyncTask<String,Void,String> {
        String yazaninid;
        String yazaninmesaj;
        String yazaninnicki;
        String kanaladi;
        String charset;
        String query;

        public ArkadanToplu(String yazaninid, String yazaninnicki, String kanaladi , String yazaninmesaj) {
            this.yazaninid = yazaninid;
            this.yazaninmesaj = yazaninmesaj;
            this.yazaninnicki = yazaninnicki;
            this.kanaladi = kanaladi;
            charset = "UTF-8";
            String param1 = "id";
            String param2 = "nick";
            String param3 = "kanal";
            String param4 = "mesaj";
            try {
                query = String.format("param1=%s&param2=%s&param3=%s&param4=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset),
                        URLEncoder.encode(param3, charset),URLEncoder.encode(param4, charset));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected String doInBackground(String... params) {

            Log.i("tago", "Grup mesajı veritabanına gonderme işlemi başlatıldı");
            try {
                return mesajıgonder();
            } catch (Exception e) {
                e.printStackTrace();
                return "olmadı";
            }
        }

        private String mesajıgonder() {
            URLConnection connection = null;

            try {
                Log.i("tago", yazaninid);
                Log.i("tago", yazaninmesaj);
                Log.i("tago", yazaninnicki);
                Log.i("tago" , kanaladi);
                connection = new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy/group_chat.php?" +
                        "id=" + yazaninid + "&name=" + yazaninnicki + "&placename=" + kanaladi + "&msg="+ yazaninmesaj).openConnection();
                Log.i("tago", "GrupSohbeti gruba mesajı gonderme bagı kurdum");
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            try {
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                InputStream response = connection.getInputStream();
                Log.i("tago", "GrupSohbeti mesajı gruba gonderme yazdım");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("tago", "GrupSohbeti Arkadan vurdurma yazamadım");
            }
            return "alabama";
        }
    }
    }

