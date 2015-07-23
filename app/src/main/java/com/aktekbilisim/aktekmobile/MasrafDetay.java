package com.aktekbilisim.aktekmobile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.aktekbilisim.interfaces.onAsyncTaskListener;
import com.aktekbilisim.modals.MasrafDetayPOJO;
import com.aktekbilisim.services.DBAdapter;
import com.aktekbilisim.services.GetWorker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class MasrafDetay extends FragmentActivity {
    EditText dateTextDetay;
    EditText txtBelgeNoDetay;
    EditText txtFirmaAdiDetay;
    EditText txtTutarDetay;
    Spinner spinnerSirketAdiDetay;
    EditText txtAciklamaDetay;
    EditText mEdit;
    Spinner spinnerMasrafKoduDetay;
    int rowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masraf_detay);
        mEdit = (EditText) findViewById(R.id.dateTextDetay);
        spinnerMasrafKoduDetay=(Spinner) findViewById(R.id.txtMasrafKoduDetay);
        dateTextDetay = (EditText) findViewById(R.id.dateTextDetay);
        txtBelgeNoDetay = (EditText) findViewById(R.id.txtBelgeNoDetay);
        txtFirmaAdiDetay = (EditText) findViewById(R.id.txtFirmaAdiDetay);
        txtTutarDetay = (EditText) findViewById(R.id.txtTutarDetay);
        spinnerSirketAdiDetay = (Spinner) findViewById(R.id.txtSirketAdiDetay);
        txtAciklamaDetay = (EditText) findViewById(R.id.txtAciklamaDetay);
        mEdit.setKeyListener(null);
        Button guncelleBtn = (Button) findViewById(R.id.guncelleBtn);
        Button silBtn = (Button) findViewById(R.id.silBtn);
        final DBAdapter db = new DBAdapter(this);
        db.open();
        rowId = Integer.parseInt(getIntent().getExtras().getString("masrafId"));
        Cursor c = db.getRecord(rowId);
        if (c.moveToFirst()) {
            setListener("http://kesim.aksa.com/api/my?tip=sirket","sirket",c.getString(6));
            setListener("http://kesim.aksa.com/api/my?tip=masrafKodu", "masrafKodu",c.getString(1));
//            s pinnerMasrafKoduDetay.setText(c.getString(1).toString());
//            spinnerSirketAdiDetay.setText(c.getString(6).toString());
            dateTextDetay.setText(c.getString(2).toString());
            txtBelgeNoDetay.setText(c.getString(3).toString());
            txtFirmaAdiDetay.setText(c.getString(4).toString());
            txtTutarDetay.setText(c.getString(5).toString());
            txtAciklamaDetay.setText(c.getString(7).toString());
        }
        db.close();
        silBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MasrafDetayPOJO> detayList = new ArrayList<MasrafDetayPOJO>();
                DBAdapter db = new DBAdapter(MasrafDetay.this);
                db.open();
                db.deleteContact(rowId);
                Cursor cAll = db.getAllRecords();
                if (cAll.moveToFirst()) {
                    do {

                        MasrafDetayPOJO newMasraf = new MasrafDetayPOJO();
                        newMasraf.txtMasrafKoduDetayPojo=cAll.getString(1);
                        newMasraf.dateTextDetayPojo = cAll.getString(2);
                        newMasraf.txtBelgeNoDetayPojo = cAll.getString(3);
                        newMasraf.txtFirmaAdiDetayPojo = cAll.getString(4);
                        newMasraf.txtTutarDetayPojo = cAll.getString(5);
                        newMasraf.txtSirketAdiDetayPojo = cAll.getString(6);
                        newMasraf.txtAciklamaDetayPojo = cAll.getString(7);
                        detayList.add(newMasraf);
                        db.deleteContact(Integer.parseInt(cAll.getString(0)));
                    } while (cAll.moveToNext());
                }
                for (int i=0;i<detayList.size();i++){
                    int id=i+1;
                    db.insertRecord(id,detayList.get(i).txtMasrafKoduDetayPojo,detayList.get(i).dateTextDetayPojo, detayList.get(i).txtBelgeNoDetayPojo, detayList.get(i).txtFirmaAdiDetayPojo, detayList.get(i).txtTutarDetayPojo, detayList.get(i).txtSirketAdiDetayPojo, detayList.get(i).txtAciklamaDetayPojo);
                }
                db.close();
                Toast.makeText(MasrafDetay.this, "Masrafýnýz Silindi.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MasrafDetay.this, MasrafListPage.class);
                startActivity(intent);
            }
        });
        guncelleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateTextDetay.getText().toString().equals("") || txtBelgeNoDetay.getText().toString().equals("") || txtFirmaAdiDetay.getText().toString().equals("") || txtTutarDetay.getText().toString().equals("") || txtAciklamaDetay.getText().toString().equals("")) {
                    Toast.makeText(MasrafDetay.this, "Lütfen Tüm Alanlarý Doldurunuz.", Toast.LENGTH_SHORT).show();
                } else {
                    db.open();
                    db.updateRecord(rowId,spinnerMasrafKoduDetay.getSelectedItem().toString(), dateTextDetay.getText().toString(), txtBelgeNoDetay.getText().toString(), txtFirmaAdiDetay.getText().toString(), txtTutarDetay.getText().toString(), spinnerSirketAdiDetay.getSelectedItem().toString(), txtAciklamaDetay.getText().toString());
                    db.close();
                    Intent intent = new Intent(MasrafDetay.this, MasrafListPage.class);
                    startActivity(intent);
                }
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
    public void setListener(String url, final String spinnerType, final String spinnerSelectValues){
        final String[] params = new String[1];
        params[0] =url;
        final onAsyncTaskListener listener = new onAsyncTaskListener() {
            ProgressDialog dialog;
            @Override
            public void onTaskStarted() {
                dialog = ProgressDialog.show(MasrafDetay.this, "Yükleniyor", "Lütfen Bekleyiniz Yükleniyor...", true, false);
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
                        (MasrafDetay.this, android.R.layout.simple_spinner_item,listViewArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if(spinnerType.equals("sirket")) {
                    spinnerSirketAdiDetay.setAdapter(adapter);
                    spinnerSirketAdiDetay.setSelection(adapter.getPosition(spinnerSelectValues));
                }
                else{
                    spinnerMasrafKoduDetay.setAdapter(adapter);
                    spinnerMasrafKoduDetay.setSelection(adapter.getPosition(spinnerSelectValues));
                }

            }

            @Override
            public void onTaskError() {

            }
        };

        GetWorker mWorker = new GetWorker(MasrafDetay.this, listener);
        mWorker.executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR, params);
    }
}
