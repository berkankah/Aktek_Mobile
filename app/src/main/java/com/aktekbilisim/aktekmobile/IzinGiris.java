package com.aktekbilisim.aktekmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class IzinGiris extends Activity {

    EditText txtKulIzin;
    EditText txtOnayBek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin_giris);
        txtKulIzin=(EditText)findViewById(R.id.editTxtKulIzin);
        txtOnayBek=(EditText)findViewById(R.id.editTxtOnayBek);
        txtKulIzin.setKeyListener(null);
        txtOnayBek.setKeyListener(null);
    }


    public void yeniEkle_onClick(View V)
    {
        Intent intent = new Intent(IzinGiris.this,IzinEkle.class);
        startActivity(intent);
    }
}
