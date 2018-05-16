package com.mxi.buildster.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.mxi.buildster.R;
import com.mxi.buildster.activity.EditProDetailActivity;
import com.mxi.buildster.model.ImagePdf;
import com.mxi.buildster.utils.SquareImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by vishal on 15/5/18.
 */

public class ProDetailGridAdapter extends BaseAdapter {
    private Context mContext;

    private final ArrayList<ImagePdf> Imageid;


    public ProDetailGridAdapter(Context c, ArrayList<ImagePdf> Imageid ) {
        mContext = c;
        this.Imageid = Imageid;


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
        CheckBox checkbox;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.grid_item, parent, false);//Inflate layout


        final SquareImageView imageView = (SquareImageView) convertView.findViewById(R.id.grid_image);
        final CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);

        checkbox.setVisibility(View.INVISIBLE);

        final ImagePdf im = Imageid.get(position);

        imageView.setImageBitmap(im.getBim());

        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentEditProDetail = new Intent(mContext, EditProDetailActivity.class);
                intentEditProDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intentEditProDetail);

                SaveImage(im.getBim());
            }
        });

        return convertView;
    }

    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        String fname = "ImageBuildster.png";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

