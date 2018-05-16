package com.mxi.buildster.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mxi.buildster.R;
import com.mxi.buildster.adapter.GridViewAdapter;
import com.mxi.buildster.model.ImagePdf;
import com.mxi.buildster.utils.FilePath;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by vishal on 7/5/18.
 */

public class SelectPdfActivity extends AppCompatActivity {

    GridView gridMain;

    GridViewAdapter gAdapter;

    ImageView iv_back,iv_upload_pdf;

    TextView tv_no_images,tv_pdf_name;

    LinearLayout ln_save_image;

    private int PICK_PDF_REQUEST = 1;

    private static final int STORAGE_PERMISSION_CODE = 1212;

    private Uri filePath;


    String project_name,project_address,project_manager,company_name;

    byte[] selectedImage;

    ArrayList<ImagePdf> bitmaps;
    public static ArrayList<ImagePdf> bitmapsFinal;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_pdf_activity);

        init();
    }

    private void init() {

        bitmapsFinal = new ArrayList<>();

        requestStoragePermission();

        project_name = getIntent().getStringExtra("project_name");
        project_address = getIntent().getStringExtra("project_address");
        project_manager = getIntent().getStringExtra("project_manager");
        company_name = getIntent().getStringExtra("company_name");
        selectedImage = getIntent().getByteArrayExtra("selectedImage");


        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_upload_pdf = (ImageView)findViewById(R.id.iv_upload_pdf);
        gridMain = (GridView) findViewById(R.id.gridMain);
        tv_no_images = (TextView) findViewById(R.id.tv_no_images);
        tv_pdf_name = (TextView) findViewById(R.id.tv_pdf_name);
        ln_save_image = (LinearLayout) findViewById(R.id.ln_save_image);



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

        iv_upload_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showFileChooser();

            }
        });

        ln_save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bitmapsFinal = gAdapter.getCheckedItems();

                Intent intent = new Intent(SelectPdfActivity.this,ViewSelectedImages.class);
                intent.putExtra("project_name",project_name);
                intent.putExtra("project_address",project_address);
                intent.putExtra("project_manager",project_manager);
                intent.putExtra("company_name",company_name);
                intent.putExtra("selectedImage",selectedImage);
                startActivity(intent);

                Log.e("FINALBITMAP", String.valueOf(bitmapsFinal.size()));
            }
        });
    }

    private ArrayList<ImagePdf> pdfToBitmap(final String pdfFile) {
        bitmaps = new ArrayList<>();

        final File fl = new File(pdfFile);

        final ProgressDialog dialog = new ProgressDialog(SelectPdfActivity.this);
        dialog.setTitle("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        tv_no_images.setVisibility(View.VISIBLE);
        gridMain.setVisibility(View.INVISIBLE);
        ln_save_image.setVisibility(View.INVISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(fl, ParcelFileDescriptor.MODE_READ_ONLY));

                    Bitmap bitmap;
                    final int pageCount = renderer.getPageCount();
                    for (int i = 0; i < pageCount; i++) {
                        PdfRenderer.Page page = renderer.openPage(i);

                        ImagePdf im = new ImagePdf();

                        int width = getResources().getDisplayMetrics().densityDpi / 72 * page.getWidth();
                        int height = getResources().getDisplayMetrics().densityDpi / 72 * page.getHeight();
                        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                        bitmap = getResizedBitmap(bitmap,500);


                        im.setBim(bitmap);

                        bitmaps.add(im);



                        // close the page
                        page.close();

                    }

                    Log.e("SIZEE", String.valueOf(bitmaps.size()));

                    // close the renderer
                    renderer.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        gAdapter = new GridViewAdapter(getApplicationContext(),bitmaps);
                        gridMain.setAdapter(gAdapter);

                        tv_no_images.setVisibility(View.INVISIBLE);
                        gridMain.setVisibility(View.VISIBLE);
                        ln_save_image.setVisibility(View.VISIBLE);

                    }
                });
            }
        }).start();

        return bitmaps;

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);

    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            String path = getPDFPath(filePath);

            Log.e("PATHHHHHHH",path);

            pdfToBitmap(path);

                String uriString = filePath.toString();
                File myFile = new File(uriString);
                String pathh = myFile.getAbsolutePath();
                String displayName = null;

                Log.e("MAINPATH",path);

                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getContentResolver().query(filePath, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            tv_pdf_name.setText(displayName);
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    displayName = myFile.getName();

                }
        }
    }

    public String getPDFPath(Uri uri){

        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
