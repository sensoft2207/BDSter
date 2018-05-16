package com.mxi.buildster.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxi.buildster.R;
import com.mxi.buildster.adapter.GridViewAdapter;
import com.mxi.buildster.adapter.ProDetailGridAdapter;
import com.mxi.buildster.model.ImagePdf;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by vishal on 14/5/18.
 */

public class MyProjectDetailActivity extends AppCompatActivity {


    ProDetailGridAdapter _pdAdapter;

    GridView grid_project;

    ImageView iv_back;

    TextView tv_pro_detail_head;

    String project_name,image_path;

    private File root;

    private ArrayList<File> fileList = new ArrayList<File>();

    ArrayList<ImagePdf> bitmaps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_project_detail);

        init();
    }

    private void init() {

        bitmaps = new ArrayList<>();

        project_name = getIntent().getStringExtra("project_name");
        image_path = getIntent().getStringExtra("image_path");


        root = new File(image_path);

        getfile(root);

        Log.e("bitmap_list_image", String.valueOf(bitmaps.size()));

        iv_back = (ImageView)findViewById(R.id.iv_back);
        tv_pro_detail_head = (TextView) findViewById(R.id.tv_pro_detail_head);
        grid_project = (GridView) findViewById(R.id.grid_project);

        displaySelectedImg();

        tv_pro_detail_head.setText(project_name);

        clickListner();
    }

    private void displaySelectedImg() {

        final ProgressDialog dialog = new ProgressDialog(MyProjectDetailActivity.this);
        dialog.setTitle("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        dialog.dismiss();

                        _pdAdapter = new ProDetailGridAdapter(getApplicationContext(),bitmaps);
                        grid_project.setAdapter(_pdAdapter);

                    }
                });
            }
        }).start();

    }

    private void clickListner() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();

            }
        });
    }

    public ArrayList<File> getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {

                    ImagePdf im = new ImagePdf();

                    fileList.add(listFile[i]);

                    Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(listFile[i]));

                    im.setBim(bitmap);

                    bitmaps.add(im);

                    getfile(listFile[i]);


                } else {
                    if (listFile[i].getName().endsWith(".png")
                            || listFile[i].getName().endsWith(".jpg")
                            || listFile[i].getName().endsWith(".jpeg")
                            || listFile[i].getName().endsWith(".gif"))

                    {
                        ImagePdf im = new ImagePdf();

                        fileList.add(listFile[i]);

                        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(listFile[i]));

                        im.setBim(bitmap);

                        bitmaps.add(im);
                    }
                }

            }
        }
        return fileList;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
