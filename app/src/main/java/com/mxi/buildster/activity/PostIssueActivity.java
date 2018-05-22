package com.mxi.buildster.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.mxi.buildster.database.DatabaseHelper;
import com.mxi.buildster.model.ProjectAssignData;
import com.mxi.buildster.utils.PickUtility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by vishal on 18/5/18.
 */

public class PostIssueActivity extends AppCompatActivity {

    CommanClass cc;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    byte[] byteArrayEditedIssue,byteArrayChoosen;

    private String userChoosenTask,location,comment;

    ImageView iv_back,iv_choose_pic;

    TextView tv_pro_pic;

    EditText ed_location,ed_comment;

    Button btn_create_dialog,btn_cancle_dialog;

    long tag1_id;

    DatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_post_activity);

        init();

    }

    private void init() {

        cc = new CommanClass(this);

        db = new DatabaseHelper(this);

        byteArrayEditedIssue = getIntent().getByteArrayExtra("byteArrayEditedIssue");

        Log.e("EDITEDBYTE", String.valueOf(byteArrayEditedIssue));

        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_choose_pic = (ImageView)findViewById(R.id.iv_choose_pic);

        ed_location = (EditText) findViewById(R.id.ed_location);
        ed_comment = (EditText) findViewById(R.id.ed_comment);

        tv_pro_pic = (TextView) findViewById(R.id.tv_pro_pic);

        btn_create_dialog = (Button) findViewById(R.id.btn_create_dialog);
        btn_cancle_dialog = (Button) findViewById(R.id.btn_cancle_dialog);

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

        btn_create_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validationIssue();
            }
        });


        btn_cancle_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
            }
        });

        iv_choose_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });

    }

    private void validationIssue() {

        location = ed_location.getText().toString();
        comment = ed_comment.getText().toString();

        if (location.equals("")){
            cc.showToast("Please enter location");
        }else if (comment.equals("")){
            cc.showToast("Please enter comment");
        }else if (byteArrayEditedIssue == null){
            cc.showToast("Issue image not done");
        }else if (byteArrayChoosen == null){
            cc.showToast("Please choose image");
        }else {

            saveIssueToDatabase(location,comment,byteArrayEditedIssue,byteArrayChoosen);
        }

    }

    private void saveIssueToDatabase(String location, String comment,
                                     byte[] byteArrayEditedIssue, byte[] byteArrayChoosen) {

        ProjectAssignData issueData = new ProjectAssignData(location,comment,byteArrayEditedIssue,byteArrayChoosen);

        tag1_id = db.createTableTwo(issueData);

        db.closeDB();

        onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PickUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(PostIssueActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = PickUtility.checkPermission(PostIssueActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArrayChoosen = stream.toByteArray();

        tv_pro_pic.setText("Image selected");

        Log.e("byteArrayChoosen", String.valueOf(byteArrayChoosen));
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArrayChoosen = stream.toByteArray();

        tv_pro_pic.setText("Image selected");

        Log.e("byteArrayChoosen", String.valueOf(byteArrayChoosen));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
