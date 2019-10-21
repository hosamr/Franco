package com.hosam2277.hosamr.franco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ProgrammingKnowledge on 4/3/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Franco.db";
    public static final String TABLE_NAME = "franco_table";
    public static final String ID = "ID";
    public static final String francoCol = "FRANCO";
    public static final String arabicCol = "ARABIC";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,FRANCO TEXT,ARABIC TEXT)");
        insert(db,"hosam", "حسام");
        insert(db,"asln", "اصلا");
        insert(db,"salamo ", "سلام");
        insert(db,"3alikom", "عليكم");
        insert(db,"isa", "إن شاء الله");
        insert(db,"ina", "ان شاء الله");
        insert(db,"isa", "ان شاءالله");
        insert(db,"msa", "ما شاء الله");
        insert(db,"s3", "سلام عليكم");
        insert(db,"el7", "الحمد لله");
        insert(db,"l7l", "الحمد لله");
        insert(db,"tamam", "تمام");
        insert(db,"eh", "ايه");
        insert(db,"enta", "انت");
        insert(db,"kowayyes", "كويس");
        insert(db,"bos", "بص");
        insert(db,"bas", "بس");
        insert(db,"delw2ti", "دلوقتي");
        insert(db,"ashofak", "أشوفك");
        insert(db,"mashi", "ماشي");
        insert(db,"mashy", "ماشي");
        insert(db,"bye", "باي");
        insert(db,"3arabia", "عربية");
        insert(db,"gaw", "جو");
        insert(db,"f", "في");
        insert(db,"we", "و");
        insert(db,"3amel", "عامل");
        insert(db,"chou", "شو");
        insert(db,"shu", "شو");
        insert(db,"3am", "عم");
        insert(db,"ti3mel", "تعمل");
        insert(db,"shlounik", "شلونك");
        insert(db,"shlounak", "شلونك");
        insert(db,"ba2a", "بقى");
        insert(db,"b2a", "بقى");
        insert(db,"7asal", "حصل");
        insert(db,"we", "و");
        insert(db,"wi", "و");
        insert(db,"Kont", "كنت");
        insert(db,"sob7", "صبح");
        insert(db,"lssa", "لسة");
        insert(db,"2a5er", "آخر");
        insert(db,"zabet", "ظابط");
        insert(db,"allah", "الله");
        insert(db,"hwwa", "هو");
        insert(db,"da5el", "داخل");
        insert(db,"5areg", "خارج");
        insert(db,"7aga", "حاجة");
        insert(db,"saba5", "صبخ");

    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


    public boolean insertData(String franco,String arabic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(francoCol,franco);
        contentValues.put(arabicCol,arabic);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getFranco(String arabic) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select "+francoCol+" FROM  "+TABLE_NAME+" WHERE TRIM(" +arabicCol+ ") = '"+arabic.trim()+"'",null);
        return res;
    }
    public Cursor getArabic(String franco) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select "+arabicCol+" FROM  "+TABLE_NAME+" WHERE TRIM(" +francoCol+ ") = '"+franco.trim()+"'",null);
        return res;
    }

    public boolean updateData(String id,String name,String surname,String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID,id);
        contentValues.put(francoCol,name);
        contentValues.put(arabicCol,surname);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
    public void insert(SQLiteDatabase db,String franco,String arabic){
        ContentValues contentValues = new ContentValues();
        contentValues.put(francoCol,franco);
        contentValues.put(arabicCol,arabic);
        long result = db.insert(TABLE_NAME,null ,contentValues);

    }
}
