package com.aktekbilisim.aktekmobile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.Calendar;

public class MasrafDonemGiris extends FragmentActivity {
    Button btnDonemKaydet;
    EditText mEdit,txtDonemAcýklama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masraf_donem_giris);
        mEdit = (EditText) findViewById(R.id.dateTextDonem);
        txtDonemAcýklama=(EditText) findViewById(R.id.txtDonemAciklama);
        mEdit.setKeyListener(null);
        btnDonemKaydet=(Button)findViewById(R.id.btnDonem);
        btnDonemKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtDonemAcýklama.getText().toString().equals("") || mEdit.getText().toString().equals("")) {
                    Toast.makeText(MasrafDonemGiris.this, "Lütfen Tüm Alanlarý Doldurunuz.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MasrafDonemGiris.this, MasrafGiris.class);
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
}
