package com.bbparty.eroc.bbp_qr.arbreRefranyer;

/**
 * Created by Pol.
 */

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseModel extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DBrefranys";

    // Contacts table name
    private static final String TABLE_REFRANYS = "refranys";


    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NIVELL = "lvl";
    private static final String KEY_PREGUNTA = "pregunta";
    private static final String KEY_RESPOSTA = "resposta";
    private static final String ALTERNATIVA1 =  "alternativa1";
    private static final String ALTERNATIVA2 = "alternativa2";
    private static final String ALTERNATIVA3 = "alternativa3";
    private static final String ALTERNATIVA4 = "alternativa4";

    public DatabaseModel(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REFRANYS_TABLE = "CREATE TABLE " + TABLE_REFRANYS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NIVELL + " INTEGER,"
                + KEY_PREGUNTA + " TEXT,"
                + KEY_RESPOSTA + " TEXT,"
                + ALTERNATIVA1 + " TEXT,"
                + ALTERNATIVA2 + " TEXT,"
                + ALTERNATIVA3 + " TEXT,"
                + ALTERNATIVA4 + " TEXT" + ")";
        db.execSQL(CREATE_REFRANYS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REFRANYS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new Refrany
    void addRefrany(RefranyDBObject _refrany) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NIVELL, _refrany.getNivell());
        values.put(KEY_PREGUNTA, _refrany.getPregunta());
        values.put(KEY_RESPOSTA, _refrany.getResposta());
        values.put(ALTERNATIVA1, _refrany.getAlternativa1());
        values.put(ALTERNATIVA2, _refrany.getAlternativa2());
        values.put(ALTERNATIVA3, _refrany.getAlternativa3());
        values.put(ALTERNATIVA4, _refrany.getAlternativa4());

        // Inserting Row
        db.insert(TABLE_REFRANYS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single refrany
    RefranyDBObject getRefrany(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REFRANYS, new String[] { KEY_ID,
                        KEY_NIVELL,KEY_PREGUNTA, KEY_RESPOSTA, ALTERNATIVA1, ALTERNATIVA2, ALTERNATIVA3, ALTERNATIVA4 }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        RefranyDBObject refrany= new RefranyDBObject(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        // return refrany
        return refrany;
    }

    // Getting All Contacts
    public List<RefranyDBObject> getAllRefranys() {
        List<RefranyDBObject> refranysList = new ArrayList<RefranyDBObject>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REFRANYS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RefranyDBObject refrany = new RefranyDBObject();
                refrany.setID(Integer.parseInt(cursor.getString(0)));
                refrany.setNivell(Integer.parseInt(cursor.getString(1)));
                refrany.setPregunta(cursor.getString(2));
                refrany.setResposta(cursor.getString(3));
                refrany.setAlternativa1(cursor.getString(4));
                refrany.setAlternativa2(cursor.getString(5));
                refrany.setAlternativa3(cursor.getString(6));
                refrany.setAlternativa4(cursor.getString(7));

                // Adding refrany to list
                refranysList.add(refrany);
            } while (cursor.moveToNext());
        }

        // return refranyslist
        return refranysList;
    }

    // Updating single refrany
    public int updateRefrany(RefranyDBObject _refrany) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NIVELL, _refrany.getNivell());
        values.put(KEY_PREGUNTA, _refrany.getPregunta());
        values.put(KEY_RESPOSTA, _refrany.getResposta());
        values.put(ALTERNATIVA1, _refrany.getAlternativa1());
        values.put(ALTERNATIVA2, _refrany.getAlternativa2());
        values.put(ALTERNATIVA3, _refrany.getAlternativa3());
        values.put(ALTERNATIVA4, _refrany.getAlternativa4());

        // updating row
        return db.update(TABLE_REFRANYS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(_refrany.getID()) });
    }

    // Deleting single refrany
    public void deleteContact(RefranyDBObject _refrany) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REFRANYS, KEY_ID + " = ?",
                new String[] { String.valueOf(_refrany.getID()) });
        db.close();
    }


    // Getting refranys Count
    public int getRefranysCount() {
        String countQuery = "SELECT  * FROM " + TABLE_REFRANYS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
