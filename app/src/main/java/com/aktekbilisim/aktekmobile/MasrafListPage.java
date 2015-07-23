package com.aktekbilisim.aktekmobile;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aktekbilisim.services.DBAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static android.widget.Toast.*;


public class MasrafListPage extends FragmentActivity {
    ListView masrafList;
    Button btnOnay, btnYeniEkle;
    int listNumber = 1;
    double masrafToplami = 0.0;
    TextView txtMasrafToplam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masraf_list_page);
        btnOnay = (Button) findViewById(R.id.btnOnay);
        btnYeniEkle = (Button) findViewById(R.id.btnYeniEkle);
        txtMasrafToplam = (TextView) findViewById(R.id.txtMasrafToplami);
        masrafList = (ListView) findViewById(R.id.listView);
        ArrayList<String> list = new ArrayList<String>();
        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAllRecords();
        if (c.moveToFirst()) {
            do {
                list.add(listNumber + " - " + c.getString(4) + " / " + c.getString(5) + " TL");
                listNumber++;
                masrafToplami = masrafToplami + Double.parseDouble(c.getString(5));
            } while (c.moveToNext());
        }
        txtMasrafToplam.setText("TOPLAM MASRAF: " + masrafToplami + " TL");
        db.close();
        StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        masrafList.setAdapter(adapter);
        masrafList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) masrafList.getItemAtPosition(position);
                Intent intent = new Intent(MasrafListPage.this, MasrafDetay.class);
                intent.putExtra("masrafId", subId(itemValue));
                startActivity(intent);
            }
        });
        btnYeniEkle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MasrafListPage.this, MasrafGiris.class);
                startActivity(intent);
            }
        });
        btnOnay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(MasrafListPage.this);
                dialog.setContentView(R.layout.custom_popup_menu);
                dialog.setTitle("Masraf  Bilgisi");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Girmiþ olduðunuz masrafýnýz -   - koduyla sisteme kayýt edilmiþtir.");//--yerine onay kodu gelecek oracle dan
//                ImageView image = (ImageView) dialog.findViewById(R.id.image);
//                image.setImageResource(R.drawable.yuvarlak35_35);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DBAdapter db = new DBAdapter(MasrafListPage.this);
                        db.open();
                        Cursor c = db.getAllRecords();
                        if (c.moveToFirst()) {
                            do {
                                db.deleteContact(Integer.parseInt(c.getString(0)));
                            } while (c.moveToNext());
                            makeText(MasrafListPage.this, "Masraflarýnýz Ýlgili Kiþiye Onaylanmasý Ýçin Gönderilmiþtir.", LENGTH_SHORT).show();
                            Intent intent = new Intent(MasrafListPage.this, MainPage.class);
                            startActivity(intent);
                        } else {
                            makeText(MasrafListPage.this, "Onaya Gönderilecek Masrafýnýz Bulunmamaktadýr.", LENGTH_SHORT).show();
                        }
                        db.close();
                        dialog.dismiss();
                        Intent intent = new Intent(MasrafListPage.this, MainPage.class);
                        startActivity(intent);
                    }
                });

                dialog.show();
            }

        });
    }

    public String subId(String itemValue) {

        String[] splited = itemValue.split("\\s+");
        return splited[0];
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}

