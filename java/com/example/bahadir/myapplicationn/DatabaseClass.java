package com.example.bahadir.myapplicationn;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatabaseClass {


    private static final String DATABASENAME= "MesajlarData.db";
    private static final String TABLENAME = "MesajTablosu";
    private static final int DATABASEVERSION = 3;

    private static final String ROWID = "_id";
    private static final String KENDIMESAJ = "kendimesaj";
    private static final String KARSIMESAJ = "karsimesaj";
    private static final String KARSIID = "karsiid";
    Context context;
    private DbHelper dbhelper;
    private static File kayityeri;
    private static SQLiteDatabase sqlitedatabaseobjesi;
    public DatabaseClass(Context context) {
        this.context = context;
        File path = Environment.getExternalStorageDirectory();
        String whappy = "Whappy";
        File f = new File(path , whappy);
        if(!f.exists()){
            f.mkdirs();
        }
        File mesajlar =new File(f,"Mesajlar");
        if(!mesajlar.exists()){
            mesajlar.mkdirs();
        }
        kayityeri = mesajlar ;
    }

    public DatabaseClass open(){
        dbhelper = new DbHelper(context);
        checkexternal();
        sqlitedatabaseobjesi = dbhelper.getWritableDatabase();
        return this;
    }
    private void checkexternal() {
        String durum = Environment.getExternalStorageState();
        boolean okunabilir , yazilabilir;
        if(durum.equals(Environment.MEDIA_MOUNTED)){
            okunabilir = true;
            yazilabilir=true;
            Log.i("tago", "okunabilir ve yazılabilir");
        }else if(durum.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
            okunabilir = true;
            yazilabilir = false;
            Log.i("tago" , "okunabilir fakat yazılamaz");
        }else{
            okunabilir = false;
            yazilabilir = false;
            Log.i("tago" , "okunamaz ve yazılamaz");
        }
    }
    public long olustur(String mesaj , String karsiid){
        ContentValues cV = new ContentValues();
        cV.put(KENDIMESAJ , mesaj);
        cV.put(KARSIMESAJ , "badbadbado");
        cV.put(KARSIID, karsiid);
        return sqlitedatabaseobjesi.insert(TABLENAME , null , cV);
    }
    public void close(){
        sqlitedatabaseobjesi.close();
    }
    public List<String> databasedencek(String karsidakiid) {
        String[] kolonlar = new String[]{ROWID , KENDIMESAJ , KARSIMESAJ, KARSIID};
        Cursor c = sqlitedatabaseobjesi.query(TABLENAME, kolonlar, KARSIID+"="+karsidakiid, null, null, null, null);
        List<String> kayitlimesajlar = new ArrayList<>();
        int kendimesajindexi = c.getColumnIndex(KENDIMESAJ);
        int karsimesajindexi = c.getColumnIndex(KARSIMESAJ);
        for(c.moveToFirst() ; !c.isAfterLast() ; c.moveToNext()){
            kayitlimesajlar.add(c.getString(kendimesajindexi) + "rumbararumbarumbarumruru" + c.getString(karsimesajindexi));
        }
        return kayitlimesajlar ;
    }
    public long olusturx(String mesaj,String karsidakiid) {
        ContentValues cV = new ContentValues();
        cV.put(KENDIMESAJ , "badbadbado");
        cV.put(KARSIMESAJ , mesaj);
        cV.put(KARSIID , karsidakiid);
        return sqlitedatabaseobjesi.insert(TABLENAME , null , cV);
    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, kayityeri + File.separator + DATABASENAME, null, DATABASEVERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLENAME + "(" + ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KENDIMESAJ +" TEXT NOT NULL, "+ KARSIMESAJ + " TEXT NOT NULL, " + KARSIID + " TEXT NOT NULL);");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
            onCreate(db);
        }
    }
}
