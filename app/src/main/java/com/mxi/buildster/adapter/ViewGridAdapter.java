package com.mxi.buildster.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.mxi.buildster.R;
import com.mxi.buildster.model.ImagePdf;
import com.mxi.buildster.utils.SquareImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by vishal on 8/5/18.
 */

public class ViewGridAdapter extends BaseAdapter {
    private Context mContext;

    private final ArrayList<ImagePdf> Imageid;

    private static LayoutInflater inflater=null;


    public ViewGridAdapter(Context c, ArrayList<ImagePdf> Imageid ) {
        mContext = c;
        this.Imageid = Imageid;

        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Imageid.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class ViewHolder
    {

        SquareImageView imageView;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            //inflate the layout on basis of boolean

            convertView = inflater.inflate(R.layout.grid_item_two, parent, false);

            final ImagePdf im = Imageid.get(position);

            viewHolder.imageView = (SquareImageView) convertView.findViewById(R.id.grid_image);

            viewHolder.imageView.setImageBitmap(im.getBim());



            viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewGridAdapter.ViewHolder) convertView.getTag();


        return convertView;
    }

}

