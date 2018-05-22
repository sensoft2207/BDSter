package com.mxi.buildster.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxi.buildster.R;
import com.mxi.buildster.adapter.AssignedGridAdapter;
import com.mxi.buildster.comman.CommanClass;
import com.mxi.buildster.database.DatabaseHelper;
import com.mxi.buildster.model.ProjectAssignData;

import java.util.List;

/**
 * Created by vishal on 21/5/18.
 */

public class AssignedProjectActivity extends AppCompatActivity {

    CommanClass cc;

    GridView grid_project;

    public static AssignedGridAdapter ajAdapter;

    DatabaseHelper db;

    ImageView iv_back;

    List<ProjectAssignData> allProjectData;

    TextView tv_no_project;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assigned_project);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        db = new DatabaseHelper(this);

        iv_back = (ImageView)findViewById(R.id.iv_back);
        grid_project = (GridView) findViewById(R.id.grid_project);
        tv_no_project = (TextView) findViewById(R.id.tv_no_project);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db.getAllPointsFromTableOne() == null){

        }else {

            allProjectData = db.getAllAssignedIssue();

        }

        if (allProjectData.size() == 0){

            tv_no_project.setVisibility(View.VISIBLE);
            grid_project.setVisibility(View.INVISIBLE);

        }else {

            tv_no_project.setVisibility(View.INVISIBLE);
            grid_project.setVisibility(View.VISIBLE);

            for (int i = 0; i < allProjectData.size(); i++) {

                String projectlocation = allProjectData.get(i).getProject_name_assign();
                String  projectcomment = String.valueOf(allProjectData.get(i).getId());

            }

            ajAdapter = new AssignedGridAdapter(getApplicationContext(),allProjectData);
            grid_project.setAdapter(ajAdapter);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
