package com.aktekbilisim.services;

/**
 * Created by berkan.kahyaoglu on 15.06.2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    public static final String KEY_ROWID = "id";
    public static final String KEY_MASRAFKODU = "masrafKodu";
    public static final String KEY_DATE = "date";
    public static final String KEY_BELGENO = "belgeNo";
    public static final String KEY_FIRMAADI = "firmaAdi";
    public static final String KEY_TUTAR="tutar";
    public static final String KEY_SIRKETADI="sirketAdi";
    public static final String KEY_DESC="desc";

    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "AktekMobileDBV2";
    private static final String DATABASE_TABLE = "aktekMasrafV2";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE =
            "create table if not exists aktekMasrafV2 (id integer,"
                    + "masrafKodu VARCHAR,date VARCHAR,belgeNo VARCHAR,firmaAdi VARCHAR,tutar VARCHAR,sirketAdi VARCHAR,desc VARCHAR);";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private static final String IZIN_TABLE_CREATE="create table if not exists aktekIzin (id integer,kategori VARCHAR,durum integer,"
            +"tip VARCHAR,baslangic VARCHAR,isbasi VARCHAR,vekalet VARCHAR,neden VARCHAR,haberlesme VARCHAR,acil VARCHAR,Vekalet_Aciklama VARCHAR);";
    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
                db.execSQL(IZIN_TABLE_CREATE);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        db.execSQL(IZIN_TABLE_CREATE);
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long insertRecord(int id,String masrafKodu,String date, String belgeNo, String firmaAdi,String tutar,String sirketAdi,String desc) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID,id);
        initialValues.put(KEY_MASRAFKODU,masrafKodu);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_BELGENO,belgeNo);
        initialValues.put(KEY_FIRMAADI, firmaAdi);
        initialValues.put(KEY_TUTAR, tutar);
        initialValues.put(KEY_SIRKETADI, sirketAdi);
        initialValues.put(KEY_DESC, desc);

        return db.insert(DATABASE_TABLE, null,initialValues);
    }
    public long insertIzinRecord(int id,int durum,String kategori,String tip,String baslangic,String isbasi,String vekalet,String neden,String haber,String acil,String vekalet_aciklamasi)
        {
            ContentValues initialValues = new ContentValues();
        initialValues.put("id", id);
         initialValues.put("durum",durum);
        initialValues.put("kategori", kategori);
        initialValues.put("tip", tip);
        initialValues.put("baslangic", baslangic);
        initialValues.put("isbasi", isbasi);
        initialValues.put("vekalet", vekalet);
        initialValues.put("neden", neden);
        initialValues.put("haberlesme", haber);
        initialValues.put("acil", acil);
        initialValues.put("Vekalet_Aciklama",vekalet_aciklamasi);
return db.insert("aktekIzin",null,initialValues );
    }
    public boolean deleteContact(long rowId) {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    public boolean deleteIzin(long id)
    {
        return db.delete("aktekIzin","id="+id,null)>0;
    }

    public Cursor getAllRecords() {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID,KEY_MASRAFKODU, KEY_DATE, KEY_BELGENO, KEY_FIRMAADI,KEY_TUTAR,KEY_SIRKETADI,KEY_DESC}, null, null, null, null, null);
    }
    public Cursor getAllIzinRecords()
    {
        return db.query("aktekIzin",new String[]{"id","kategori","durum","tip","baslangic","isbasi","vekalet","neden","haberlesme","acil","Vekalet_Aciklama"},null,null,null,null,null);
    }

    public Cursor getRecord(int rowId) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID,KEY_MASRAFKODU, KEY_DATE, KEY_BELGENO, KEY_FIRMAADI,KEY_TUTAR,KEY_SIRKETADI,KEY_DESC},
                        KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor getIzinRecord(int id) throws SQLException
    {
        Cursor mCursor=db.query(true,"aktekIzin",new String[]{"id","durum","kategori","tip","baslangic","isbasi","vekalet","neden","haberlesme","acil","Vekalet_Aciklama"},"id="+id,null,null,null,null,null);
        if(mCursor!=null)
        {
            mCursor.moveToFirst();
        }
        return  mCursor;
    }

    public boolean updateRecord(int id,String masrafKodu,String date, String belgeNo, String firmaAdi,String tutar,String sirketAdi,String desc) {
        ContentValues args = new ContentValues();
        args.put(KEY_MASRAFKODU,masrafKodu);
        args.put(KEY_DATE, date);
        args.put(KEY_BELGENO, belgeNo);
        args.put(KEY_FIRMAADI, firmaAdi);
        args.put(KEY_TUTAR, tutar);
        args.put(KEY_SIRKETADI, sirketAdi);
        args.put(KEY_DESC, desc);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + id, null) > 0;
    }
    public boolean updateIzinRecord(int id,int durum,String kategori,String tip,String baslangic,String isbasi,String vekalet,String neden,String haber,String acil,String vekalet_aciklamasi)
    {
        ContentValues args = new ContentValues();
        args.put("kategori",kategori);
        args.put("tip",kategori);
        args.put("baslangic",baslangic);
        args.put("isbasi",isbasi);
        args.put("vekalet",vekalet);
        args.put("neden",neden);
        args.put("haberlesme",haber);
        args.put("acil",acil);
        args.put("Vekalet_Aciklama",vekalet_aciklamasi);
        return db.update("aktekIzin",args,"id="+id,null)>0;
    }
}