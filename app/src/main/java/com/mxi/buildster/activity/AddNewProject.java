package com.mxi.buildster.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxi.buildster.R;
import com.mxi.buildster.comman.CommanClass;
import com.mxi.buildster.utils.Utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vishal on 7/5/18.
 */

public class AddNewProject extends AppCompatActivity {

    CommanClass cc;

    private static final int SELECT_PICTURE = 100;

    EditText ed_project_name,ed_project_address,ed_project_manager,ed_company_name;

    String project_name,project_address,project_manager,company_name;

    byte[] selectedImage;

    ImageView iv_back,iv_choose_pic;

    TextView tv_pro_pic;

    Button btn_next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_project);

        cc = new CommanClass(this);

        init();
    }

    private void init() {

        ed_project_name = (EditText) findViewById(R.id.ed_project_name);
        ed_project_address = (EditText) findViewById(R.id.ed_project_address);
        ed_project_manager = (EditText) findViewById(R.id.ed_project_manager);
        ed_company_name = (EditText) findViewById(R.id.ed_company_name);

        iv_back = (ImageView)findViewById(R.id.iv_back);
        tv_pro_pic = (TextView) findViewById(R.id.tv_pro_pic);
        iv_choose_pic = (ImageView)findViewById(R.id.iv_choose_pic);
        btn_next = (Button) findViewById(R.id.btn_next);

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

        iv_choose_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImageChooser();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateInfo();

            }
        });

    }

    private void validateInfo() {

        project_name = ed_project_name.getText().toString();
        project_address = ed_project_address.getText().toString();
        project_manager = ed_project_manager.getText().toString();
        company_name = ed_company_name.getText().toString();

        if (project_name.equals("")){
            cc.showToast("Please enter project name");
        }else if(project_address.equals("")){
            cc.showToast("Please enter project address");
        }else if(project_manager.equals("")){
            cc.showToast("Please enter project manager name");
        }else if(company_name.equals("")){
            cc.showToast("Please enter company name");
        }else if(selectedImage == null){
            cc.showToast("Choose project photo");
        }
        else {
            Intent intentSelectPdf = new Intent(AddNewProject.this,SelectPdfActivity.class);
            intentSelectPdf.putExtra("project_name",project_name);
            intentSelectPdf.putExtra("project_address",project_address);
            intentSelectPdf.putExtra("project_manager",project_manager);
            intentSelectPdf.putExtra("company_name",company_name);
            intentSelectPdf.putExtra("selectedImage",selectedImage);
            startActivity(intentSelectPdf);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
    }

    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageUri = data.getData();

                if (null != selectedImageUri) {

                    // Saving to Database...
                    if (saveImageInDB(selectedImageUri)) {

                        tv_pro_pic.setText("Image selected");

                    }
                }

            }
        }
    }

    Boolean saveImageInDB(Uri selectedImageUri) {

        try {

            InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
            selectedImage = Utils.getBytes(iStream);

            Log.e("IMAGEBYTE", String.valueOf(selectedImage));

            return true;
        } catch (IOException ioe) {
            Log.e("ImgError", "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
            return false;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
