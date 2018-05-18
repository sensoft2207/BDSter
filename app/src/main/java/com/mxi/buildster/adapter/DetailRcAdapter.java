/*
package com.mxi.buildster.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mxi.buildster.R;
import com.mxi.buildster.model.DetailData;

import java.util.ArrayList;

*/
/**
 * Created by vishal on 16/5/18.
 *//*


public class DetailRcAdapter extends RecyclerView.Adapter<DetailRcAdapter.MyViewHolder> {

    private ArrayList<DetailData> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image_detail;

        public MyViewHolder(View itemView) {
            super(itemView);


            this.image_detail = (ImageView) itemView.findViewById(R.id.image_detail);

        }
    }

    public DetailRcAdapter(ArrayList<DetailData> data) {

        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rc_detail_item, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {


        ImageView imageView = holder.image_detail;

        imageView.setImageResource(dataSet.get(listPosition).getImage_detail());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }}
*/
