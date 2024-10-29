package com.example.pfmemo;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * 複数の画像をSpinner化する
 *
 * 主なクラス:
 * - {@link ContentsActivity}: メモ内部の処理を行うシーン。
 *
 * @author kasugai
 * @version 1.0.0
 * @since 1.0.0
 */
public class ImageAdapter extends BaseAdapter {

    /** context で指定されたレイアウトを参照する */
    private final LayoutInflater inflater;

    /** レイアウトファイルを指定する */
    private final int layoutID;

    /** 画像のアイテム名を指定 */
    private final String[] names;

    /** 画像リソースを管理 */
    private final int[] imageIDs;

    static class ViewHolder{
        ImageView imageView;
    }

    /**
     * インスタンスの初期化と画像リソースを取得
     * @param context スピナーを使用するシーンを指定
     * @param itemLayoutId layout ファイルを指定
     * @param spinnerItems 画像のアイテム名を指定
     * @param spinnerImages 画像リソースのファイル名を指定
     */
    ImageAdapter(Context context, int itemLayoutId,
                 String[] spinnerItems, String[] spinnerImages){
        inflater = LayoutInflater.from(context);
        layoutID = itemLayoutId;
        names = spinnerItems;
        imageIDs = new int[spinnerImages.length];
        Resources res = context.getResources();


        for(int i = 0; i < spinnerImages.length; i++){
            imageIDs[i] = res.getIdentifier(
                spinnerImages[i],
                "drawable",
                context.getPackageName()
            );
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(layoutID, null);
            holder = new ViewHolder();

            holder.imageView = convertView.findViewById(R.id.image_view);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageResource(imageIDs[position]);
        return convertView;
    }
    @Override
    public int getCount(){
        return names.length;
    }
    @Override
    public Object getItem(int position){
        return position;
    }
    @Override
    public long getItemId(int position){
        return position;
    }
}
