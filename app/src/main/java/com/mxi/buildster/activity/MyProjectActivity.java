package com.mxi.buildster.activity;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxi.buildster.R;
import com.mxi.buildster.adapter.ProjectGridAdapter;
import com.mxi.buildster.adapter.ViewGridAdapter;
import com.mxi.buildster.comman.CommanClass;
import com.mxi.buildster.database.DatabaseHelper;
import com.mxi.buildster.model.ProjectData;
import com.mxi.buildster.utils.Utils;

import java.util.List;


/**
 * Created by vishal on 10/5/18.
 */

public class MyProjectActivity extends AppCompatActivity {

    CommanClass cc;

    GridView grid_project;

    ProjectGridAdapter pjAdapter;

    DatabaseHelper db;

    ImageView iv_back;

    List<ProjectData> allProjectData;

    TextView tv_no_project;

    String project_name,project_address,project_manager,company_name,image_path;

    byte[] selectedImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_project);

        cc = new CommanClass(this);

        db = new DatabaseHelper(this);

        init();
    }

    private void init() {

        iv_back = (ImageView)findViewById(R.id.iv_back);
        grid_project = (GridView) findViewById(R.id.grid_project);
        tv_no_project = (TextView) findViewById(R.id.tv_no_project);

        if (db.getAllPointsFromTableOne() == null){

        }else {

            allProjectData = db.getAllPointsFromTableOne();

        }

        if (allProjectData.size() == 0){

            tv_no_project.setVisibility(View.VISIBLE);
            grid_project.setVisibility(View.INVISIBLE);

        }else {

            tv_no_project.setVisibility(View.INVISIBLE);
            grid_project.setVisibility(View.VISIBLE);

            for (int i = 0; i < allProjectData.size(); i++) {

                project_name = allProjectData.get(i).getProject_name();
                project_address = allProjectData.get(i).getProject_address();
                project_manager = allProjectData.get(i).getProject_manager();
                company_name = allProjectData.get(i).getCompany_name();
                image_path = allProjectData.get(i).getImage_path();
                selectedImage = allProjectData.get(i).getSelectedImage();

                Log.e("projectname",project_name);
                Log.e("projectaddress",project_address);
                Log.e("projectmanager",project_manager);
                Log.e("companyname",company_name);
                Log.e("imagepath",image_path);
                Log.e("selectedImage", String.valueOf(selectedImage));
            }

            pjAdapter = new ProjectGridAdapter(getApplicationContext(),allProjectData);
            grid_project.setAdapter(pjAdapter);

        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
