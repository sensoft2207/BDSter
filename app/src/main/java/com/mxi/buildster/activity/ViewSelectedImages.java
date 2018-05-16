package com.mxi.buildster.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mxi.buildster.R;
import com.mxi.buildster.adapter.GridViewAdapter;
import com.mxi.buildster.adapter.ViewGridAdapter;
import com.mxi.buildster.comman.CommanClass;

import com.mxi.buildster.database.DatabaseHelper;
import com.mxi.buildster.model.ImagePdf;
import com.mxi.buildster.model.ProjectData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.mxi.buildster.activity.SelectPdfActivity.bitmapsFinal;

/**
 * Created by vishal on 8/5/18.
 */

public class ViewSelectedImages extends AppCompatActivity {

    CommanClass cc;

    GridView gridMain;

    ViewGridAdapter gAdapter;

    ImageView iv_back;

    TextView tv_no_images,view_image_submit;

    LinearLayout ln_submit_image;

    ArrayList<ImagePdf> bitmapsViewImage;

    String project_name,project_address,project_manager,company_name;

    byte[] selectedImage;

    int folderCount;
    int imageCount = 0;

    ArrayList<String> folderPathMain;

    File[] listFolderFile;

    long tag1_id;

    DatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_selected_image);

        cc = new CommanClass(this);

        db = new DatabaseHelper(this);

        init();
    }

    private void init() {

        folderCount = cc.loadPrefInt("folder_count");

        bitmapsViewImage = new ArrayList<>();

        bitmapsViewImage = bitmapsFinal;

        project_name = getIntent().getStringExtra("project_name");
        project_address = getIntent().getStringExtra("project_address");
        project_manager = getIntent().getStringExtra("project_manager");
        company_name = getIntent().getStringExtra("company_name");
        selectedImage = getIntent().getByteArrayExtra("selectedImage");


        iv_back = (ImageView)findViewById(R.id.iv_back);
        view_image_submit = (TextView) findViewById(R.id.view_image_submit);
        gridMain = (GridView) findViewById(R.id.grid_final);
        tv_no_images = (TextView) findViewById(R.id.tv_no_images);
        ln_submit_image = (LinearLayout) findViewById(R.id.ln_submit);

        Log.e("VIEWIMAGESIZE", String.valueOf(bitmapsViewImage.size()));


        final ProgressDialog dialog = new ProgressDialog(ViewSelectedImages.this);
        dialog.setTitle("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        tv_no_images.setVisibility(View.VISIBLE);
        gridMain.setVisibility(View.INVISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        gAdapter = new ViewGridAdapter(getApplicationContext(),bitmapsViewImage);
                        gridMain.setAdapter(gAdapter);

                        tv_no_images.setVisibility(View.INVISIBLE);
                        gridMain.setVisibility(View.VISIBLE);

                    }
                });
            }
        }).start();



        clickListner();

    }

    private void clickListner() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();

            }
        });

        view_image_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i <bitmapsViewImage.size() ; i++) {

                    ImagePdf imm = bitmapsViewImage.get(i);

                    SaveImage(imm.getBim());

                }

                folderCount++;

                cc.savePrefInt("folder_count",folderCount);

                Log.e("selectedImage", String.valueOf(selectedImage));


                folderPathMain = new ArrayList<String>();

                getFromSdcard();
            }
        });
    }

    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/BuildMain/buildster_"+folderCount);
        myDir.mkdirs();
        String fname = "Image_"+imageCount+".jpg";
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

        imageCount++;

    }


    public void getFromSdcard()
    {
        File file = new File(android.os.Environment.getExternalStorageDirectory(),"BuildMain");

        if (file.isDirectory())
        {
            listFolderFile = file.listFiles();


            for (int i = 0; i < listFolderFile.length; i++)
            {
                if (i + 1 == folderCount){

                    folderPathMain.add(listFolderFile[i].getAbsolutePath());
                    Log.e("path_name",listFolderFile[i].getAbsolutePath());

                    String path = listFolderFile[i].getAbsolutePath();

                    saveToSQLite(project_name,project_address,project_manager,company_name,path,selectedImage);

                    Intent intentMain = new Intent(ViewSelectedImages.this,MainActivity.class);
                    intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentMain);

                }

            }

            Log.e("Pathofsavedimage", String.valueOf(folderPathMain.size()));
        }
    }

    private void saveToSQLite(String project_name, String project_address, String project_manager,
                              String company_name, String path, byte[] selectedImage) {

        ProjectData point = new ProjectData(project_name,project_address,project_manager,company_name,path,selectedImage);

        tag1_id = db.createTableOne(point);

       /* List<ProjectData> allProjectData = db.getAllPointsFromTableOne();

        for (int i = 0; i < allProjectData.size(); i++) {

            String projectname = allProjectData.get(i).getProject_name();
            String projectaddress = allProjectData.get(i).getProject_address();
            String projectmanager = allProjectData.get(i).getProject_manager();
            String companyname = allProjectData.get(i).getCompany_name();
            String imagepath = allProjectData.get(i).getImage_path();

            Log.e("projectname",projectname);
            Log.e("projectaddress",projectaddress);
            Log.e("projectmanager",projectmanager);
            Log.e("companyname",companyname);
            Log.e("imagepath",imagepath);
        }*/

        db.closeDB();
    }

    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    private void showToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
