package com.example.pfmemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;

// DONE: シングルトンパターンを適応して固有化する

/**
 * データベースの作成と入出力を行う。
 * シングルトンパターンを適応。
 *
 * 主なクラス:
 * - {@link ContentsActivity}: メモ内部の処理を行うシーン。
 * - {@link Const}: 定数を管理し、プログラムの設定値を保持する。
 * - {@link ListManager}: メインのメモリストを管理する。
 *
 * @author kasugai
 * @version 1.0.0
 * @since 1.0.0
 */
public class FirstTimeDatabase extends SQLiteOpenHelper implements Const{

    /** シングルトンの固有インスタンス */
    private static FirstTimeDatabase instance;

    /** データベース名 */
    static final private String DBName = "MyDatabase";

    /** SQLite の version */
    static final private int VERSION = 1;

    /** データベースの入出力を行うインスタンス */
    private SQLiteDatabase db;

    /**
     * データベース名、バージョンの初期化
     * db インスタンスに書き込みを許可
     * @param context
     */
    private FirstTimeDatabase(Context context){
        super(context, DBName,null,VERSION);
        if(this.db == null){
            this.db = getWritableDatabase();
        }
    }

    /**
     * シングルトンのインスタンスを取得する
     * @param context
     * @return インスタンスを返す
     */
    public static FirstTimeDatabase getInstance(Context context){
        if(instance == null){
            instance = new FirstTimeDatabase(context);
        }
        return instance;
    }

    /**
     * データベースを作成する
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db){
        // DONE: dateのデータ型をDATE型にできないか調査する
        // DONE: 本文用のデータ領域を追加する
        db.execSQL("CREATE TABLE user(id INTEGER PRIMARY KEY AUTOINCREMENT, date LONG, title TEXT, img LONG, main TEXT)");
    }

    /**
     * データベースのバージョンを更新する
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    /**
     * 詳細シーンの内容を参照して、データベースを更新する。
     * @param _ttl_v タイトルのインスタンスを指定
     * @param _main_v メモ本文のインスタンスを指定
     * @param _spnr アイコンスピナーのインスタンスを指定
     */
    public void insertData(TextView _ttl_v, TextView _main_v, Spinner _spnr){
        // DONE: 代入する日付の値がおかしいので修正する
        ContentValues values = new ContentValues();
        // DONE: 日付の入力
        values.put("date",Const.getCurrTime());
        // DONE: タイトルの入力
        values.put("title",_ttl_v.getText().toString());
        // DONE: 画像の入力
        values.put("img",SPINNER_NUM[_spnr.getSelectedItemPosition()]);
        // DONE: 本文の入力
        values.put("main",_main_v.getText().toString());

        // 成否処理
        if(db.insert("user",null,values) == -1){
            Log.d("DB","挿入失敗");
        }else{
            Log.d("DB","挿入成功");
        }
    }

    /**
     * データベースに格納された値をLogcatに出力する
     */
    public void checkData(){
        Cursor cursor = db.rawQuery("SELECT * FROM user", null);
        // データベースの出力(Logcat)
        if (cursor.moveToFirst()) {
            do {
                Log.d("DB", "ID: " + cursor.getInt(0) +
                        ", Date: " + cursor.getLong(1) +
                        ", Title: " + cursor.getString(2) +
                        ", Img: " + cursor.getInt(3) +
                        ", Main: " + cursor.getString(4)); // DONE: 本文用のデータを表示する
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}