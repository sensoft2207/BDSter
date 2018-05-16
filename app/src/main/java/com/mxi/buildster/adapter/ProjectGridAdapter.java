package com.mxi.buildster.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxi.buildster.R;
import com.mxi.buildster.activity.MyProjectDetailActivity;
import com.mxi.buildster.model.ImagePdf;
import com.mxi.buildster.model.ProjectData;
import com.mxi.buildster.utils.SquareImageView;
import com.mxi.buildster.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishal on 14/5/18.
 */

public class ProjectGridAdapter extends BaseAdapter {

    private Context mContext;

    private final List<ProjectData> Imageid;

    private static LayoutInflater inflater=null;


    public ProjectGridAdapter(Context c, List<ProjectData> Imageid ) {
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
        TextView tv_pro_name;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final ProjectGridAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ProjectGridAdapter.ViewHolder();

            convertView = inflater.inflate(R.layout.grid_project_item, parent, false);

            final ProjectData im = Imageid.get(position);

            viewHolder.imageView = (SquareImageView) convertView.findViewById(R.id.grid_pro_image);
            viewHolder.tv_pro_name = (TextView) convertView.findViewById(R.id.tv_pro_name);

            viewHolder.tv_pro_name.setText(im.getProject_name());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    viewHolder.imageView .setImageBitmap(Utils.getImage(im.getSelectedImage()));

                    viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            }, 1000);

            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getImageDataPost(position);
                }
            });


            convertView.setTag(viewHolder);
        } else


            viewHolder = (ProjectGridAdapter.ViewHolder) convertView.getTag();


        return convertView;
    }

    private void getImageDataPost(int position) {

        ProjectData pd = Imageid.get(position);

        Intent intentProDetails = new Intent(mContext, MyProjectDetailActivity.class);
        intentProDetails.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentProDetails.putExtra("project_name",pd.getProject_name());
        intentProDetails.putExtra("image_path",pd.getImage_path());
        mContext.startActivity(intentProDetails);
    }

}


