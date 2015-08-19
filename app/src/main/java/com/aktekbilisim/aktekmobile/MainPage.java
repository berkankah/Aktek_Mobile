package com.aktekbilisim.aktekmobile;

import android.content.Intent;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class MainPage extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ImageView imageViewMasrafGiris=(ImageView)findViewById(R.id.btnMasrafGirisAnaEkran);
        ImageView imageView›zinGiris=(ImageView) findViewById(R.id.btnIzinGirisAnaEkran);
        imageViewMasrafGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, MasrafDonemGiris.class);
                startActivity(intent);
            }
        });
        imageView›zinGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainPage.this,IzinGiris.class);
                startActivity(intent);
            }
        });
    }
}
