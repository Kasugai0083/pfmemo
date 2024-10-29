package com.example.pfmemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

/**
 * プロジェクトのエントリーポイントとなるクラス。
 * メイン画面のシーン制御を行う。
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
public class MainActivity extends AppCompatActivity {

    /** シーン遷移のためのintentを管理 */
    Intent it;

    /** メイン画面に配置されたメモリストのインスタンス */
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // データベースチェック
        FirstTimeDatabase firstDb = FirstTimeDatabase.getInstance(this);
        firstDb.checkData();

        // DONE: リストの処理をリスト管理クラスに移行する
        // DONE: リストの更新処理を追加する
        lv = findViewById(R.id.main_list);

        ListManager lm = ListManager.getInstance();
        lm.setListView(lv);
        lm.initData(this);
        lm.updateData();

        it = new Intent(this, ContentsActivity.class);
        Button btn = findViewById(R.id.new_btn);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
                // クリックしたアイテムのデータベースIDを取得
                Map<String, Object> selectedItem = (Map<String, Object>) parent.getItemAtPosition(pos);
                Object databaseId = selectedItem.get("id");

                // クリックしたリストのIDを送信
                it.putExtra("ID",Integer.valueOf(databaseId.toString()));
                startActivity(it);
                // IDの破棄
                it.removeExtra("ID");
            }
        });

        btn.setOnClickListener(v -> {
            startActivity(it);
        });
    }
}