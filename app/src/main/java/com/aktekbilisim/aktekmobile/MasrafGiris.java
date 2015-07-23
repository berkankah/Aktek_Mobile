package com.aktekbilisim.aktekmobile;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.support.v4.app.FragmentActivity;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.widget.Spinner;
import android.widget.Toast;

import com.aktekbilisim.interfaces.onAsyncTaskListener;
import com.aktekbilisim.services.DBAdapter;
import com.aktekbilisim.services.GetWorker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MasrafGiris extends FragmentActivity {
    DBAdapter db = new DBAdapter(this);
    EditText mEdit,dateTxt, txtBelgeNo, txtFirmaAdi, txtTutar, txtAciklama,txtMasrafKodu;
    Button btnKaydet;
    Button btnMasrafListesiGor;
    Spinner spinnerSirketAdi,spinnerMasrafKodu;
    public int id = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masraf_giris);
        final String[] params = new String[1];
        btnKaydet = (Button) findViewById(R.id.yeniEkle);
        btnMasrafListesiGor = (Button) findViewById(R.id.btnMasrafList);
        mEdit = (EditText) findViewById(R.id.dateText);
        dateTxt= (EditText) findViewById(R.id.dateText);
//        txtMasrafKodu=(EditText) findViewById(R.id.txtMasraf);
        txtBelgeNo = (EditText) findViewById(R.id.txtBelgeNo);
        txtFirmaAdi = (EditText) findViewById(R.id.txtFirmaAdi);
        txtTutar = (EditText) findViewById(R.id.txtTutar);
//        txtSirketAdi = (EditText) findViewById(R.id.txtSirketAdi);
        spinnerSirketAdi=(Spinner) findViewById(R.id.txtSirketAdi);
        spinnerMasrafKodu=(Spinner) findViewById(R.id.txtMasraf);
        txtAciklama = (EditText) findViewById(R.id.txtAciklama);
        mEdit.setKeyListener(null);
        setListener("http://kesim.aksa.com/api/my?tip=sirket","sirket");
        setListener("http://kesim.aksa.com/api/my?tip=masrafKodu","masrafKodu");
        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateTxt.getText().toString().equals("") || txtFirmaAdi.getText().toString().equals("") || txtTutar.getText().toString().equals("") || txtAciklama.getText().toString().equals("")) {
                    Toast.makeText(MasrafGiris.this, "Lütfen Tüm Alanlarý Doldurunuz.", Toast.LENGTH_SHORT).show();
                } else {
                    masrafEkle();
                    Intent intent = new Intent(MasrafGiris.this, MasrafListPage.class);
                    startActivity(intent);
                    finish();
                    dateTxt.setText("");
                    txtBelgeNo.setText("");
                    txtFirmaAdi.setText("");
                    txtTutar.setText("");
                    txtAciklama.setText("");
                }
            }
        });
        btnMasrafListesiGor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MasrafGiris.this, MasrafListPage.class);
                startActivity(intent);
            }
        });
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getSupportFragmentManager(), "DatePicker");
            }
        });
    }
    public void masrafEkle() {
        db.open();
        Cursor c = db.getAllRecords();
        if (c.moveToFirst()) {
            id = 1;
            do {
                id++;
            } while (c.moveToNext());
        }
        db.insertRecord(id,spinnerMasrafKodu.getSelectedItem().toString(),dateTxt.getText().toString(), txtBelgeNo.getText().toString(), txtFirmaAdi.getText().toString(),txtTutar.getText().toString(),spinnerSirketAdi.getSelectedItem().toString(),txtAciklama.getText().toString());
        db.close();
    }

    public void populateSetDate(int year, int month, int day) {
        mEdit.setText(day + "/" + month + "/" + year);
    }
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }
    }
    public void setListener(String url, final String spinnerType){
        final String[] params = new String[1];
        params[0] =url;
        final onAsyncTaskListener listener = new onAsyncTaskListener() {
            ProgressDialog dialog;
            @Override
            public void onTaskStarted() {
                dialog = ProgressDialog.show(MasrafGiris.this, "Yükleniyor", "Lütfen Bekleyiniz Yükleniyor...", true, false);
            }

            @Override
            public void onCancelled() {
                dialog.dismiss();
            }

            @Override
            public void onTaskFinished(String result) {
                dialog.dismiss();
                String OutputData = "";
                JSONObject jsonResponse;
                ArrayList listViewArray = new ArrayList();
                try {
                    jsonResponse = new JSONObject(result);
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("table");
                    int lengthJsonArr = jsonMainNode.length();
                    for (int i = 0; i < lengthJsonArr; i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        if(spinnerType.equals("sirket")){
                        String sirkeT_KISA_AD = jsonChildNode.optString("sirkeT_KISA_AD").toString();
                        String sirkeT_ID = jsonChildNode.optString("sirkeT_ID").toString();
                        listViewArray.add(sirkeT_KISA_AD);
                        }
                        else{
                            String sirkeT_KISA_AD = jsonChildNode.optString("masraF_ACIKLAMA").toString();
                            String sirkeT_ID = jsonChildNode.optString("vergI_KODU").toString();
                            listViewArray.add(sirkeT_KISA_AD);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (MasrafGiris.this, android.R.layout.simple_spinner_item,listViewArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if(spinnerType.equals("sirket")) {
                    spinnerSirketAdi.setAdapter(adapter);
                }
                else{
                    spinnerMasrafKodu.setAdapter(adapter);
                }

            }

            @Override
            public void onTaskError() {

            }
        };

        GetWorker mWorker = new GetWorker(MasrafGiris.this, listener);
        mWorker.executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR, params);
    }
}
