package com.example.bahadir.myapplicationn;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PageFragment4 extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    ImageView image1 , image2;
    TextView tv1 , tv2 ,tv3 ;
    Bitmap bitmap;
    String isim;
    private int mPage;

    public static PageFragment4 newInstance(int page) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE, page);
        PageFragment4 fragment = new PageFragment4();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle b = getActivity().getIntent().getExtras();
        isim = b.getString("isim");
        bitmap = b.getParcelable("resim");
        Log.i("tago" , isim);
        View view = inflater.inflate(R.layout.profil, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        image1 = (ImageView) view.findViewById(R.id.imageView);
        image2 = (ImageView) view.findViewById(R.id.imageView3);
        tv1 = (TextView) view.findViewById(R.id.textView);
        tv2 = (TextView) view.findViewById(R.id.textView5);
        tv3 = (TextView) view.findViewById(R.id.textView6);
        image1.setImageBitmap(bitmap);
        tv1.setText(isim);
    }
}