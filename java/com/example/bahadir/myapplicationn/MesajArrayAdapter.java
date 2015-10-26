package com.example.bahadir.myapplicationn;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MesajArrayAdapter extends ArrayAdapter<Mesaj> {

    public TextView tv1;
    public List<Mesaj> MesajListesi = new ArrayList();
    public LinearLayout layout1;

    public MesajArrayAdapter(Context applicationContext, int mesaj, List<Mesaj> mesajListesi) {
        super(applicationContext , mesaj , mesajListesi);
    }

    public void add(Mesaj mesaj){
        MesajListesi.add(mesaj);
        super.add(mesaj);
    }

    public int getCount(){
        return this.MesajListesi.size();
    }

    public Mesaj getItem(int index){
        return this.MesajListesi.get(index);
    }

    public View getView(int position , View convertView , ViewGroup parent){
        View v = convertView;
        if(v==null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.mesaj , parent, false);
        }

        layout1 = (LinearLayout) v.findViewById(R.id.mesajlayout);
        Mesaj mesajobj = getItem(position);
        tv1=(TextView) v.findViewById(R.id.textView7);
        tv1.setText(mesajobj.mesac);
       // tv1.setBackgroundResource(mesajobj.left ? R.drawable.pbutton : R.drawable.pbutton3);
        layout1.setGravity(mesajobj.side? Gravity.LEFT : Gravity.RIGHT);
        return v;
    }

}
