package com.mxi.buildster.activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mxi.buildster.R;
import com.mxi.buildster.comman.CommanClass;
import com.mxi.buildster.dragevent.DragController;
import com.mxi.buildster.dragevent.DragLayer;
import com.mxi.buildster.dragevent.DragLayerTwo;
import com.mxi.buildster.model.DetailData;
import com.mxi.buildster.utils.MyData;
import com.mxi.buildster.utils.TouchImageView;
import com.mxi.buildster.utils.ZoomLinearLayout;
import com.mxi.buildster.utils.ZoomView;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;



import static com.mxi.buildster.R.id.zoom_linear_layout;

/**
 * Created by vishal on 15/5/18.
 */

public class EditProDetailActivity extends AppCompatActivity implements  View.OnDragListener,View.OnClickListener {

    CommanClass cc;

    ImageView iv_issue_image;

    RecyclerView rc_detail;

    private ArrayList<DetailData> horizontalDetailList;

    DetailRcAdapter drAdapter;


    private DragController mDragController;
    private DragLayer mDragLayer;

    ImageView shape,iv_back;

    int x_touch = 0;
    int y_touch = 0;

    byte[] byteArrayEditedIssue;

    ZoomLinearLayout zoomLinearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_pro_detail);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        mDragController = new DragController(this);

        iv_back = (ImageView)findViewById(R.id.iv_back);
        rc_detail = (RecyclerView)findViewById(R.id.rc_detail);

        iv_issue_image = (ImageView)findViewById(R.id.iv_issue_image);




        zoomLinearLayout = (ZoomLinearLayout) findViewById(R.id.zoom_linear_layout);

        zoomLinearLayout.init(EditProDetailActivity.this);



        horizontalDetailList = new ArrayList<DetailData>();
        for (int i = 0; i < MyData.drawableArray.length; i++) {

            horizontalDetailList.add(new DetailData(
                    MyData.drawableArray[i]
            ));
        }
        drAdapter = new DetailRcAdapter(getApplicationContext(),horizontalDetailList);

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(EditProDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);

        rc_detail.setLayoutManager(horizontalLayoutManagaer);
        rc_detail.setAdapter(drAdapter);

        DragController dragController = mDragController;
        mDragLayer = (DragLayer) findViewById(R.id.fl_drag_view);


        mDragLayer.setOnDragListener(this);
        mDragLayer.setDragController(dragController);
        dragController.addDropTarget(mDragLayer);


        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images/ImageBuildster.png");
        Bitmap bmp = BitmapFactory.decodeFile(myDir.getAbsolutePath());
        iv_issue_image.setImageBitmap(bmp);

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
    }


    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_ENTERED:
//                fl_drag_view.setBackgroundColor(GREEN);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
//                fl_drag_view.setBackgroundColor(RED);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
//                fl_drag_view.setBackgroundColor(WHITE);
                break;
            case DragEvent.ACTION_DROP:
                Log.e("Drop", "Yes its Activity Drop");
                final float dropX = event.getX();
                final float dropY = event.getY();
                final DragData state = (DragData) event.getLocalState();

                final LinearLayout ln_main = (LinearLayout) LayoutInflater.from(this).inflate(
                        R.layout.rc_detail_item, mDragLayer, false);

                shape = (ImageView)ln_main.findViewById(R.id.image_detail);
                shape.setImageResource(state.item);


                if(shape.getParent()!=null){

                    ((ViewGroup)shape.getParent()).removeView(shape); // <- fix

                }else {

                    ln_main.addView(shape);

                }



                shape.setOnClickListener(this);

                int x = (int) (dropX - (float) state.width / 2);
                int y = (int) (dropY - (float) state.height / 2);
                DragLayer.LayoutParams params;

                params = new DragLayer.LayoutParams(DragLayer.LayoutParams.WRAP_CONTENT, DragLayer.LayoutParams.WRAP_CONTENT, x, y);

                if(shape.getParent()!=null){

                    ((ViewGroup)shape.getParent()).removeView(shape); // <- fix

                }else {

                    mDragLayer.addView(shape, params);

                }



                /*Pending vishal.................*/


                shape.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent ev) {
                        boolean handledHere = false;

                        final int action = ev.getAction();

                        switch (action) {
                            case MotionEvent.ACTION_DOWN:

                                x_touch = (int) ev.getX();
                                y_touch = (int) ev.getY();

                                handledHere = startDrag(v);
                                if (handledHere) {
                                    v.performClick();
                                }

                                break;

                            case MotionEvent.ACTION_UP:
                                x_touch = (int) ev.getX();
                                y_touch = (int) ev.getY();

                                break;
                        }

                        return handledHere;
                    }
                });


                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(final View view_main) {

        final Dialog d = new BottomSheetDialog(this);
        d.setContentView(R.layout.bottom_dialog_issue);
        d.setCancelable(true);
        d.show();

        Button btn_next_dialog = (Button)d.findViewById(R.id.btn_next_dialog);
        Button btn_delete_dialog = (Button)d.findViewById(R.id.btn_delete_dialog);

        btn_next_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getEditedIssueImage();

                Intent intentPostIssue = new Intent(EditProDetailActivity.this,PostIssueActivity.class);
                intentPostIssue.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentPostIssue.putExtra("byteArrayEditedIssue",byteArrayEditedIssue);
                startActivity(intentPostIssue);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                d.dismiss();


            }
        });

        btn_delete_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDragLayer.removeView(view_main);

                d.dismiss();
            }
        });

    }

    private void getEditedIssueImage() {

        zoomLinearLayout.destroyDrawingCache();
        zoomLinearLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = zoomLinearLayout.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArrayEditedIssue = stream.toByteArray();

    }

    public class DetailRcAdapter extends RecyclerView.Adapter<DetailRcAdapter.MyViewHolder>  {

        private ArrayList<DetailData> dataSet;

        Context context;

        int item;


        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView image_detail;

            public MyViewHolder(View itemView) {
                super(itemView);


                this.image_detail = (ImageView) itemView.findViewById(R.id.image_detail);



            }
        }

        public DetailRcAdapter(Context context,ArrayList<DetailData> data) {

            this.dataSet = data;
            this.context = context;
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

            final View shape = imageView;


            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    item = MyData.drawableArray[listPosition];

                    final DragData state = new DragData(item, shape.getWidth(), shape.getHeight());
                    final View.DragShadowBuilder shadow = new View.DragShadowBuilder(shape);
                    ViewCompat.startDragAndDrop(shape, null, shadow, state, 0);


                       /* ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDrag(data, shadowBuilder, v, 0);*/

                    return true;
                }
            });
        }




        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }

    public class DragData {

        public final int item;
        public final int width;
        public final int height;

        public DragData(int item, int width, int height) {
            this.item = item;
            this.width = width;
            this.height = height;
        }

    }

    public boolean startDrag(View v) {

        Object obj = v;
        mDragController.startDrag(v, mDragLayer, obj, DragController.DRAG_ACTION_MOVE);

        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
