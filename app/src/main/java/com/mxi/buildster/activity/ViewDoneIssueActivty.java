package com.mxi.buildster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxi.buildster.R;
import com.mxi.buildster.comman.CommanClass;
import com.mxi.buildster.database.DatabaseHelper;
import com.mxi.buildster.utils.Utils;

import static com.mxi.buildster.adapter.AssignedGridAdapter.Imageid;

/**
 * Created by vishal on 21/5/18.
 */

public class ViewDoneIssueActivty extends AppCompatActivity {

    CommanClass cc;

    ImageView iv_one,iv_two,iv_back;

    TextView tv_location,tv_comment;

    Button btn_done,btn_cancle;

    byte[] project_issue_img,project_issue_choose_img;

    String project_name_location,project_name_comment,id;

    DatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.done_issue_activity);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        db = new DatabaseHelper(this);

        project_issue_img = getIntent().getByteArrayExtra("project_issue_img");
        project_issue_choose_img = getIntent().getByteArrayExtra("project_issue_choose_img");

        project_name_location = getIntent().getStringExtra("project_name_assign");
        project_name_comment = getIntent().getStringExtra("project_name_comment");
        id = getIntent().getStringExtra("project_id");

        iv_back = (ImageView)findViewById(R.id.iv_back);

        iv_one = (ImageView)findViewById(R.id.iv_one);
        iv_two = (ImageView)findViewById(R.id.iv_two);

        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_comment = (TextView) findViewById(R.id.tv_comment);

        btn_done = (Button) findViewById(R.id.btn_done);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);

        tv_location.setText(":"+"   "+project_name_location);
        tv_comment.setText(":"+"   "+project_name_comment);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                iv_one.setImageBitmap(Utils.getImage(project_issue_img));
                iv_two.setImageBitmap(Utils.getImage(project_issue_choose_img));
                iv_one.setScaleType(ImageView.ScaleType.FIT_CENTER);
                iv_two.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        }, 1000);

        clickListner();
    }

    private void clickListner() {

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.deleteIssue(id);

                Imageid.remove(id);

                cc.showToast("Project done successfully");

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        iv_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentFull = new Intent(ViewDoneIssueActivty.this,FullImgDialogActivity.class);
                intentFull.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentFull.putExtra("img_full_byte",project_issue_img);
                startActivity(intentFull);

            }
        });

        iv_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentFull = new Intent(ViewDoneIssueActivty.this,FullImgDialogActivity.class);
                intentFull.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentFull.putExtra("img_full_byte",project_issue_choose_img);
                startActivity(intentFull);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
