package com.mxi.buildster.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.mxi.buildster.R;
import com.mxi.buildster.adapter.DetailRcAdapter;
import com.mxi.buildster.model.DetailData;
import com.mxi.buildster.utils.MyData;
import com.mxi.buildster.utils.TouchImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by vishal on 15/5/18.
 */

public class EditProDetailActivity extends AppCompatActivity {

    TouchImageView iv_issue_image;

    RecyclerView rc_detail;

    private ArrayList<DetailData> horizontalDetailList;

    DetailRcAdapter drAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_pro_detail);

        init();
    }

    private void init() {

        rc_detail = (RecyclerView)findViewById(R.id.rc_detail);

        iv_issue_image = (TouchImageView)findViewById(R.id.iv_issue_image);

        horizontalDetailList = new ArrayList<DetailData>();
        for (int i = 0; i < MyData.drawableArray.length; i++) {

            horizontalDetailList.add(new DetailData(
                    MyData.drawableArray[i]
            ));
        }
        drAdapter = new DetailRcAdapter(horizontalDetailList);

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(EditProDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);

        rc_detail.setLayoutManager(horizontalLayoutManagaer);
        rc_detail.setAdapter(drAdapter);

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images/ImageBuildster.png");
        Bitmap bmp = BitmapFactory.decodeFile(myDir.getAbsolutePath());
        iv_issue_image.setImageBitmap(bmp);
    }


}
