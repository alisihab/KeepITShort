package com.example.bahadir.myapplicationn;

import android.util.Log;

public class Insann {

        public String name;
        public String url;
        public String id;
        public String uzaklik;
        public String anasisikilmisurl;
        public Insann() {

        }

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            Log.i("tago" , "Insann url " + url );
            this.url=url;

        }
        public String getUzaklik() {
            return uzaklik;
        }
        public void setUzaklik(String uzaklik) {
            this.uzaklik = uzaklik;
        }
    }


