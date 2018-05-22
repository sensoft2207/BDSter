package com.mxi.buildster.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.mxi.buildster.R;
import com.mxi.buildster.utils.TouchImageView;
import com.mxi.buildster.utils.Utils;

/**
 * Created by vishal on 21/5/18.
 */

public class FullImgDialogActivity extends AppCompatActivity {

    TouchImageView iv_full_issue_img;

    byte[] img_full_byte;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_img_issue);

        init();
    }

    private void init() {

        img_full_byte = getIntent().getByteArrayExtra("img_full_byte");

        iv_full_issue_img = (TouchImageView)findViewById(R.id.iv_full_issue_img);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                iv_full_issue_img.setImageBitmap(Utils.getImage(img_full_byte));
                iv_full_issue_img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        }, 1000);
    }
}
