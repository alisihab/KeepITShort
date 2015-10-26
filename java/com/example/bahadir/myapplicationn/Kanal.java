package com.example.bahadir.myapplicationn;

public class Kanal {

    String kanaladi;
    String id;
    String kanalurl;
    String kurankisi;
    String date;
    int likesayisi;
    boolean official;


    public Kanal(Boolean official){
        this.official = official;
    }
    public String getKanaladi() {
        return kanaladi;
    }
    public void setKanaladi(String kanaladi) {
        this.kanaladi = kanaladi;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getLikesayisi() {
        return likesayisi;
    }
    public void setLikesayisi(int likesayisi) {
        this.likesayisi = likesayisi;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getKurankisi() {
        return kurankisi;
    }
    public void setKurankisi(String kurankisi) {
        this.kurankisi = kurankisi;
    }
    public String getKanalurl() {
        return kanalurl;
    }
    public void setKanalurl(String kanalurl) {
        this.kanalurl = kanalurl;
    }
}
