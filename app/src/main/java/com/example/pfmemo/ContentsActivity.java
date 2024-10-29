package com.example.pfmemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * メモの内容を確認、編集するシーン。
 *
 * 主なクラス:
 * - {@link MainActivity}: メインのシーン。
 * - {@link Const}: 定数を管理し、プログラムの設定値を保持する。
 * - {@link FirstTimeDatabase}: データベースの作成と入出力を行う。
 * - {@link ListManager}: メインのメモリストを管理する。
 * - {@link ImageAdapter}: 複数の画像をSpinner化する。
 *
 * @author kasugai
 * @version 1.0.0
 * @since 1.0.0
 */
public class ContentsActivity extends AppCompatActivity implements Const{

    /** タイトルと本文のインスタンス */
    EditText edit_ttl,edit_memo;

    /** ListManagerのインスタンス */
    ListManager lm = ListManager.getInstance();

    /**
     * SPINNER_NUMとデータベースのimg番号を指定してspinnerImageに指定するIDを返却する
     * SPINNER_NUMとデータベースが合致しない場合 -1
     * @param _spinner SPINNER_NUMを指定
     * @param _value データベースのimgを指定
     * @return ID番号が返される
     */
    public int convertNumToID(int[] _spinner, int _value){
        for(int i = 0; i < _spinner.length; i++){
            if(_spinner[i] == _value){
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        // プルダウン初期化
        Spinner spinner = findViewById(R.id.spinner);
        ImageAdapter adapter = new ImageAdapter(
                this,                    // Contextを指定
                R.layout.spinner_list,          // レイアウトを指定
                SPINNER_ITEMS,                  // アイテム名を指定
                SPINNER_IMAGES                  // 画像名を指定
        );
        spinner.setAdapter(adapter);
        // プルダウン初期化ここまで


        // データベースの初期化
        FirstTimeDatabase firstDb = FirstTimeDatabase.getInstance(this);
        SQLiteDatabase db = firstDb.getWritableDatabase();

        edit_ttl = findViewById(R.id.editTitle);
        edit_memo = findViewById(R.id.editMemo);

        // 遷移後の値受け渡し
        if(getIntent().getSerializableExtra("ID") != null){
            // pos -> id
            Object obj = getIntent().getSerializableExtra("ID");

            if(obj instanceof Integer){
                int pos = (Integer) obj;
                // Toastで指定番号を確認
                String msg = pos + "番目のアイテムがクリックされました";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                // データベースのidを指定
                Cursor cursor = db.rawQuery("SELECT * FROM user WHERE id = ?", new String[]{String.valueOf(pos)});

                // タイトルをデータベースから設定
                if (cursor.moveToFirst()) {
                    int titleIndex = cursor.getColumnIndex("title");
                    int mainIndex = cursor.getColumnIndex("main");
                    int imgIndex = cursor.getColumnIndex("img");
                    String tmp_str;

                    // DONE: データベースからドロイド君のアイコンを設定する
                    spinner.setSelection(convertNumToID(SPINNER_NUM, cursor.getInt(imgIndex)));

                    // タイトルを設定
                    tmp_str = cursor.getString(titleIndex);
                    edit_ttl.setText(tmp_str);

                    // DONE: データベースから本文を設定する
                    tmp_str = cursor.getString(mainIndex);
                    edit_memo.setText(tmp_str);
                }
                cursor.close();
            }
        }

        Button save_btn = findViewById(R.id.save_btn);

        save_btn.setOnClickListener(v -> {

            ContentValues values = new ContentValues();
            // DONE: タイトルの更新
            values.put("title",edit_ttl.getText().toString());
            // DONE: 本文の更新
            values.put("main",edit_memo.getText().toString());

            // DONE: アイコンの更新
            // DONE: icon_idから画像IDをデータベースに書き込めるように
            int icon_id = spinner.getSelectedItemPosition();
            values.put("img",SPINNER_NUM[icon_id]);

            // DONE: 日付の更新
            values.put("date",Const.getCurrTime());

            // データベースの書き込み
            if(getIntent().getSerializableExtra("ID") != null){ //編集処理
                // pos -> id
                Object obj = getIntent().getSerializableExtra("ID");

                if(obj instanceof Integer){
                    int pos = (Integer) obj;
                    db.update("user",values,"id = ?", new String[]{String.valueOf(pos)});
                }
            }else{ // 新規処理
                // DONE: データベースに新しいデータを挿入する
                firstDb.insertData(edit_ttl, edit_memo, spinner);
            }

            // DONE: メインのリストをリフレッシュ(アップデート)する
            lm.updateData();

            // メインに戻る
            finish();
        });

        // 削除ボタン処理
        Button delete_btn = findViewById(R.id.delete_btn);
        if(getIntent().getSerializableExtra("ID") != null){
            delete_btn.setVisibility(View.VISIBLE);
        }else{
            delete_btn.setVisibility(View.INVISIBLE);
        }
        delete_btn.setOnClickListener(v -> {
            // pos -> id
            Object obj = getIntent().getSerializableExtra("ID");

            if(obj instanceof Integer){
                int pos = (Integer) obj;
                db.delete("user","id = ?", new String[]{String.valueOf(pos)});
                String msg = pos + "番目のアイテムを削除しました";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
            lm.updateData();
            finish();
        });
    }
}
