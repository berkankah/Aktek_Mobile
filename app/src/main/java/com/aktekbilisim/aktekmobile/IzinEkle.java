package com.aktekbilisim.aktekmobile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.aktekbilisim.services.DBAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class IzinEkle extends Activity {


    EditText editTxtBaslangic;
    EditText editTxtisBasi;
    EditText  editTxtVekalet;
    EditText  editTxtNeden;
    EditText  editTxtHbr;
    EditText  editTxtAcil;
    EditText editTxtVekaletAcklmasi;
    Spinner  spinKategori;
    Spinner spinTip;
    DatePickerDialog.OnDateSetListener baslangic,bitis;
    final int DATE_BASLANGIC=0;
    final int DATE_BITIS=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin_ekle);
        editTxtBaslangic = (EditText) findViewById(R.id.editTxtIzinBas);
        editTxtisBasi=(EditText)findViewById(R.id.editTxtIsBasi);
        editTxtVekalet=(EditText)findViewById(R.id.editTxtVekalet);
        editTxtNeden=(EditText)findViewById(R.id.editTxtNeden);
        editTxtHbr=(EditText)findViewById(R.id.editTxtIzinSrcHbr);
       editTxtAcil=(EditText)findViewById(R.id.editTxtAcilDrmHbr);
        editTxtVekaletAcklmasi=(EditText)findViewById(R.id.editVkltAcklama);
        spinKategori=(Spinner)findViewById(R.id.spinIzinKtgr);
        spinTip=(Spinner)findViewById(R.id.spinIzinTipi);
        baslangic= new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker arg0,int arg1,int arg2,int arg3)
            {
                boolean error=false;
                if(!editTxtisBasi.getText().toString().equals(""))
                {
                    String[] dateisBasi=editTxtisBasi.getText().toString().split("/");
                    if(arg1>Integer.valueOf(dateisBasi[2]))
                    {
                        error=true;
                    }
                    else
                    {
                        if(arg2>Integer.valueOf(dateisBasi[1]))
                        {
                            error=true;
                        }
                        else
                        {
                            if(arg3>=Integer.valueOf(dateisBasi[0])){
                                error=true;
                            }
                        }
                    }

                }

                if(error)
                {
                    Toast.makeText(getApplicationContext(),"Başlangıç Tarihi Bitiş Tarihinden Önce Olmalıdır",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    editTxtBaslangic.setText(arg3 + "/" + arg2 + "/" + arg1);
                }
            }

        };
        bitis= new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker arg0,int arg1,int arg2,int arg3)
            {
                boolean error=false;
                if(!editTxtBaslangic.getText().toString().equals(""))
                {
                  String[] dateBas= editTxtBaslangic.getText().toString().split("/");
                    if(arg1<Integer.valueOf(dateBas[2]))
                    {
                       error=true;

                    }
                    else
                    {
                        if(arg2<Integer.valueOf(dateBas[1]))
                        {
                            error=true;
                        }
                        else
                        {
                            if(arg3<=Integer.valueOf(dateBas[0]))
                            {
                                error=true;
                            }
                        }

                    }

                               }
                if(error)
                {
                    Toast.makeText(getApplicationContext(),"Bitiş Tarihi Başlangıç Tarihinden Sonra Olmalıdır",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    editTxtisBasi.setText(arg3 + "/" + arg2 + "/" + arg1);
                }

            }

        };
        editTxtBaslangic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });
        editTxtisBasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });
    }

@Override
    protected Dialog onCreateDialog(int id)
    {
            switch(id) {
                case DATE_BASLANGIC:
                    return new DatePickerDialog(this, baslangic, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                case DATE_BITIS:
                    return new DatePickerDialog(this,bitis, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            }
        return null;
    }
    public void btnEkleOnClick(View V)
    {
        if(editTxtBaslangic.getText().toString().equals("") || editTxtisBasi.getText().toString().equals("")||editTxtVekalet.getText().toString().equals("")||spinKategori.getSelectedItem().toString().equals("") || spinTip.getSelectedItem().toString().equals(""))
        {

            Toast.makeText(this,"L�tfen Zorunlu Alanlar? Doldurunuz.",Toast.LENGTH_SHORT).show();
            return;
        }

        int id=0;
        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAllIzinRecords();
        if (c.moveToFirst()) {
            id = 1;
            do {
                id++;
            } while (c.moveToNext());
        }
        db.insertIzinRecord(id,0,spinKategori.getSelectedItem().toString(),spinTip.getSelectedItem().toString(),editTxtBaslangic.getText().toString(),editTxtisBasi.getText().toString(),editTxtVekalet.getText().toString(),editTxtNeden.getText().toString(),editTxtHbr.getText().toString(),editTxtAcil.getText().toString(),editTxtVekaletAcklmasi.getText().toString());
        db.close();
        Toast.makeText(this,"?zin ?ste?iniz Onaya G�nderilmi?tir",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,MainPage.class);
        startActivity(intent);

    }

}
