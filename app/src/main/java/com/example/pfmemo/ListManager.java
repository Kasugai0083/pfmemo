package com.example.pfmemo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * メインシーンのメモリストを管理するシーン
 * シングルトンパターンを適応
 *
 * 主なクラス:
 * - {@link MainActivity}: メインのシーン。
 * - {@link ContentsActivity}: メモ内部の処理を行うシーン。
 * - {@link FirstTimeDatabase}: データベースの作成と入出力を行う。
 *
 * @author kasugai
 * @version 1.0.0
 * @since 1.0.0
 */
public class ListManager {
    //DONE: シングルトンパターンを適応

    /** シングルトンの固有インスタンス */
    private static ListManager instance;

    /** メインメモリストのインスタンス */
    private static ListView lv;

    /** データベース管理のインスタンス */
    FirstTimeDatabase firstDb;

    /** データベースのインスタンス */
    SQLiteDatabase db;

    /** メインシーンの context */
    Context main_context;

    /** データベースの入出力を行うリスト */
    List<Map<String, Object>> list_data = new ArrayList<>();

    /** メモリスト作成用の Adapter */
    SimpleAdapter adapter;

    //DONE: コンストラクタ(初期化)
    private ListManager(){
    }

    /**
     * インスタンスの初期化とメモリストの作成を行う
     * @param _context MainActivity の context を指定
     */
    public void initData(Context _context){
        main_context = _context;
        firstDb = FirstTimeDatabase.getInstance(main_context);
        db = firstDb.getWritableDatabase();

        writeList();

        adapter = new SimpleAdapter(
                main_context,
                list_data,
                R.layout.list_item,
                new String[] {"date", "title", "image"},
                new int[] {R.id.date, R.id.title, R.id.img}
        );
        // データを代入
        lv.setAdapter(adapter);

    }

    /**
     * メモリストの更新処理
     * 一度 list_data をクリアしてから list_data にデータベースの書き込みを行う
     */
    public void updateData(){
        list_data.clear();
        writeList();
        adapter.notifyDataSetChanged();
    }

    /**
     * データベースの内容を list_data に出力する
     * 日付順のソートと date テーブル値のフォーマットも行う
     */
    private void writeList(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        Cursor cursor = db.rawQuery("SELECT * FROM user", null);
        if(cursor.moveToFirst()){
            do{
                Map<String,Object> item = new HashMap<>();
                item.put("id", cursor.getLong(0));
                item.put("date",sdf.format(cursor.getLong(1)));
                item.put("title", cursor.getString(2));
                item.put("image",cursor.getInt(3));
                list_data.add(item);
            }while(cursor.moveToNext());
        }

        // DONE: 日付で並び替え
        Collections.sort(list_data, new DateComparator());
        cursor.close();
    }

    /**
     * date テーブルを基準に降順ソートを行う
     */
    private class DateComparator implements Comparator<Map<String, Object>> {
        @Override
        public int compare(Map<String, Object> _map1, Map<String, Object> _map2)
        {
            String date1 = _map2.get("date").toString();
            String date2 = _map1.get("date").toString();
            return date1.compareTo(date2);
        }
    }

    /**
     * メインシーンのメモリストを設定する
     * @param _lv メインシーンの ListView インスタンスを指定
     */
    public void setListView(ListView _lv){
        this.lv = _lv;
    }

    /**
     * シングルトンのインスタンスを取得する
     * @return シングルトンのインスタンスを返す
     */
    public static ListManager getInstance(){
        if(instance == null){
            instance = new ListManager();
        }
        return instance;
    }
}
