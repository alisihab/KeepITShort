package com.example.bahadir.myapplicationn;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class Mesajlasma extends Activity {

    public boolean taraf = false;
    public ListView list1;
    public MesajArrayAdapter adapter;
    public List<Mesaj> MesajListesi = new ArrayList();
    EditText etv1;
    Button buton1;
    TextView tv1;
    ProgressBar progressbar;
    int ilerleme = 0;
    Handler handler;
    String karsidakiid;
    String karsidakiisim;
    String karsidakiresimurl;
    Bitmap icon;
    ImageButton imagebuton1;
    String yazaninid;
    String yazaninmesaj;
    ArkadanVurdur aV;
    boolean notificationbas;
    boolean okunabilir;
    boolean yazilabilir;
    BroadcastReceiver receiveralfa = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            String message = b.getString("mesaj");
            Log.i("tago", "mesaj = " + message);
            String mesajbolumu = message.trim().substring(35);
            takeChatMessage(mesajbolumu);
        }
    };

    protected void onCreate(Bundle bambam) {
        super.onCreate(bambam);
        setContentView(R.layout.mesajlasma);
        progressbar =(ProgressBar) findViewById(R.id.progressBar2);
        Intent i = getIntent();
        karsidakiisim = i.getStringExtra("isim");
        karsidakiresimurl = i.getStringExtra("resimurl");
        karsidakiid = i.getStringExtra("id");
        icon = i.getBundleExtra("icon").getParcelable("iccon");
        Log.i("tago", karsidakiid);
        tanımlar();
        registerReceiver(receiveralfa, new IntentFilter("broadCastName"));
        DatabaseClass dB = new DatabaseClass(Mesajlasma.this);
        dB.open();
        List<String> kayitlimesajlar = dB.databasedencek(karsidakiid);
        dB.close();
        Log.i("tago" , String.valueOf(kayitlimesajlar));

        for(int k = 0 ; k < kayitlimesajlar.size() ; k++){
            Log.i("tago" , kayitlimesajlar.get(k).substring(0,10));
            if(kayitlimesajlar.get(k).substring(0,10).equals("badbadbado")){
                adapter.add(new Mesaj(!taraf,kayitlimesajlar.get(k).substring(37)));
            }else{
                adapter.add(new Mesaj(taraf,kayitlimesajlar.get(k).substring(0,kayitlimesajlar.get(k).indexOf("rumbararumbarumbarumruru"))));
            }
        }
    }
    protected void onResume() {
        super.onResume();
        notificationbas = false;
        notificationSharedKaydet();
    }
    private void notificationSharedKaydet() {
        SharedPreferences sP = getSharedPreferences("notification", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sP.edit();
        prefEditor.putBoolean("notificationver", notificationbas);
        prefEditor.commit();
    }
    protected void onPause() {
        super.onPause();
        notificationbas = true;
        notificationSharedKaydet();
        unregisterReceiver(receiveralfa);
    }
    private void tanımlar() {
        yazaninid = sharedPreferenceIdAl();
        tv1 = (TextView) findViewById(R.id.textView2);
        tv1.setText(karsidakiisim);
        imagebuton1 = (ImageButton) findViewById(R.id.imageButton);
        imagebuton1.setImageBitmap(icon);
        buton1 = (Button) findViewById(R.id.button4);
        list1 = (ListView) findViewById(R.id.listView2);
        adapter = new MesajArrayAdapter(getApplicationContext(), R.layout.mesaj, MesajListesi);
        etv1 = (EditText) findViewById(R.id.editText2);
        etv1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendChatMessage();
            }
        });
        list1.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list1.setAdapter(adapter);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                list1.setSelection(adapter.getCount() - 1);
            }
        });
    }
    private String sharedPreferenceIdAl() {
        SharedPreferences sp = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String veritabani_id = sp.getString("veritabani_id", "default id");
        Log.i("tago", "Mesajlasma veritabani id = " + veritabani_id);
        return veritabani_id;
    }
    private boolean sendChatMessage() {
        yazaninmesaj = etv1.getText().toString();
        aV = new ArkadanVurdur(yazaninid, yazaninmesaj, karsidakiid);
        aV.execute("sikim");
        adapter.add(new Mesaj(taraf, yazaninmesaj));
        etv1.setText("");
        mesajiexternalkaydet(yazaninmesaj);
        handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            public void run() {
                ilerleme = ilerleme + 10;
                handler.post(new Runnable() {
                    public void run() {
                        progressbar.setProgress(ilerleme);
                    }
                });
            }
        });
        thread.start();

        return true;
    }
    private void mesajiexternalkaydet(String yazaninmesaj) {
        String durum = Environment.getExternalStorageState();
        if (durum.equals(Environment.MEDIA_MOUNTED)) {
            okunabilir = true;
            yazilabilir = true;
            Log.i("tago", "okunabilir ve yazılabilir");
        } else if (durum.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            okunabilir = true;
            yazilabilir = false;
            Log.i("tago", "okunabilir fakat yazılamaz");
        } else {
            okunabilir = false;
            yazilabilir = false;
            Log.i("tago", "okunamaz ve yazılamaz");
        }
        if (okunabilir && yazilabilir) {
            String mesaj = yazaninmesaj;
            File path = Environment.getExternalStorageDirectory();
            Log.i("tago", "path= " + path);
            String whappy = "Whappy";
            File f = new File(path, whappy);
            if (!f.exists()) {
                f.mkdirs();
            }
            File mesajlar = new File(f, "Mesajlar");
            if (!mesajlar.exists()) {
                mesajlar.mkdirs();
            }
            DatabaseClass dB = new DatabaseClass(Mesajlasma.this);
            dB.open();
            dB.olustur(yazaninmesaj,karsidakiid);
            dB.close();
            Log.i("tago", "Mesajlasma sqlite kayıt işlemi yapıldı");
        }
    }
    private boolean takeChatMessage(String mesaajj) {
        adapter.add(new Mesaj(!taraf, mesaajj));
        DatabaseClass databaseClass = new DatabaseClass(Mesajlasma.this);
        databaseClass.open();
        databaseClass.olusturx(mesaajj, karsidakiid);
        databaseClass.close();
        handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            public void run() {
                ilerleme = ilerleme + 10;
                handler.post(new Runnable() {
                    public void run() {
                        progressbar.setProgress(ilerleme);
                    }
                });
            }
        });
        thread.start();

        return true;
    }

    public class ArkadanVurdur extends AsyncTask<String, Void, String> {

        String yazanid;
        String yazanmesaj;
        String karsiid;
        String charset;
        String query;

        public ArkadanVurdur(String yazanid, String yazanmesaj, String karsiid) {
            this.yazanid = yazanid;
            this.yazanmesaj = yazanmesaj;
            this.karsiid = karsiid;
            charset = "UTF-8";
            String param1 = "id";
            String param2 = "long";
            String param3 = "lat";
            try {
                query = String.format("param1=%s&param2=%s&param3=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset),
                        URLEncoder.encode(param3, charset));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected String doInBackground(String... params) {

            Log.i("tago", "Mesajı veritabanına gonderme işlemi başlatıldı");
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
                Log.i("tago", yazanid);
                Log.i("tago", yazanmesaj);
                Log.i("tago", karsiid);
                connection = new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy/sendmsg.php?" +
                        "id=" + yazanid + "&id2=" + karsiid + "&msg=" + yazanmesaj).openConnection();
                Log.i("tago", "VeriTabani mesajı gonderme bagı kurdum");
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
                Log.i("tago", "VeriTabani Arkadan vurdurma yazdım");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("tago", "VeriTabani Arkadan vurdurma yazamadım");
            }
            return "alabama";
        }
    }

    public class urldenResim extends AsyncTask<String, Void, Bitmap> {

        ImageButton bmImage;

        public urldenResim(ImageButton bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... params) {
            URL url;
            Bitmap icon = null;
            try {
                url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                Log.i("tago", "Insan Adapter connect sağladım");
                InputStream input = connection.getInputStream();
                icon = BitmapFactory.decodeStream(input);
                Log.i("tago", "Insan Adapter bitmap yaptım");
                return icon;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return icon;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            bmImage.setImageBitmap(bitmap);

        }
    }
}
