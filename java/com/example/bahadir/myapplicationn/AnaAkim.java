package com.example.bahadir.myapplicationn;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.astuetz.PagerSlidingTabStrip;

public class AnaAkim extends FragmentActivity {
    Bitmap bitmap;
    String isim;
    String resimurl;
    boolean notificationbas;
    protected void onCreate(Bundle bambam){
        super.onCreate(bambam);
        setContentView(R.layout.genelaltplan);
        notificationbas = true;
        notificationSharedPrefKaydet();
        /*Intent intent = getIntent();
        bitmap = intent.getParcelableExtra("resim");
        isim =  intent.getStringExtra("isim");
        resimurl = intent.getStringExtra("resimurl");;
        String firstname = intent.getStringExtra("firstname");
        String lastname = intent.getStringExtra("lastname");
        Intent inte = new Intent(AnaAkim.this , TakipServisi.class);
        inte.putExtra("isim" , isim);
        inte.putExtra("resimurl" , resimurl);
        inte.putExtra("firstname" , firstname);
        inte.putExtra("lastname", lastname);
        startService(inte);*/
        tanimlar();
        nickAl();
       // yerolustur();
        //mesafeyiBul(x, y);
    }

    private void notificationSharedPrefKaydet() {
        SharedPreferences sP = getSharedPreferences("notification" , Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sP.edit();
        prefEditor.putBoolean("notificationver", notificationbas);
        prefEditor.commit();
    }

    protected void onResume() {
        super.onResume();
        Log.i("tago", "AnaAkim " + isim + " giriş yaptı");

    }
    public void tanimlar(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setViewPager(viewPager);
    }
    public void nickAl(){
        final Dialog dialog = new Dialog(AnaAkim.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdialog);
        dialog.getWindow().setDimAmount(0.7f);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final EditText etv1 = (EditText) dialog.findViewById(R.id.editText);
        Button onaylaButonu = (Button) dialog.findViewById(R.id.button5);
        onaylaButonu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nick = etv1.getText().toString();
                Log.i("tago", "nickin :" + nick);
                dialog.cancel();
                nickiSharedKeydet(nick);
            }
        });

    }

    private void nickiSharedKeydet(String nick) {
        SharedPreferences sP = getSharedPreferences("kullaniciverileri",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putString("kullanicinick" , nick);
        editor.commit();
    }

    protected void onStop() {
        super.onStop();
    }

   /* protected void onDestroy() {
        unregisterReceiver(breceiver);
        stopService(new Intent(AnaAkim.this , TakipServisi.class));
        super.onDestroy();
    }
*/

}
