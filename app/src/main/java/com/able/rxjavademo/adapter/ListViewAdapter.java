package com.able.rxjavademo.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.able.rxjavademo.myutils.StaticUtils;
import com.able.rxjavademo.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ======================================================
 * Created by Administrator able_fingerth on 2017/5/26.
 * <p/>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private List<String> listStr = new ArrayList<>();

    public ListViewAdapter(Context context, List<String> listStr) {
        this.context = context;
        this.listStr = listStr;
    }

    public void setListStr(List<String> listStr) {
        this.listStr = listStr;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listStr.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new ImageView(context);
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(listStr.get(position), options);
        int height = options.outHeight;
        int width = options.outWidth;
        //LogUtils.setLog("LocalSaveFileUtils", "height == " + height + " ;width == " + width + " ;StaticUtils.sysWidth == " + StaticUtils.sysWidth);
        options.inJustDecodeBounds = false;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, StaticUtils.sysWidth * height / width);
        convertView.setLayoutParams(layoutParams);

        ImageView iv = (ImageView) convertView;
        iv.setImageBitmap(BitmapFactory.decodeFile(listStr.get(position)));
        return convertView;
    }


}
