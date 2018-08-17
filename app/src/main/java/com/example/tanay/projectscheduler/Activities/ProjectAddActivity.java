package com.example.tanay.projectscheduler.Activities;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tanay.projectscheduler.Fragments.DatePickerFragment;
import com.example.tanay.projectscheduler.POJO.Project;
import com.example.tanay.projectscheduler.R;
import com.example.tanay.projectscheduler.Utils.GlobalUtils;

import java.util.Calendar;

public class ProjectAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    Button startDate, targetDate, submit;
    EditText projectName;
    Project newProject;
    int fl = 0;
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(fl == 1){
            newProject.setStart(String.valueOf(dayOfMonth) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year));
            startDate.setText(newProject.getStart());
        }
        if(fl == 2) {
            newProject.setTarget(String.valueOf(dayOfMonth) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year));
            targetDate.setText(newProject.getTarget());
        }
        //        Toast.makeText(ProjectAddActivity.this, String.valueOf(dayOfMonth) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_add);

        startDate = findViewById(R.id.startDate);
        targetDate = findViewById(R.id.targetDate);
        projectName = findViewById(R.id.projectname);
        submit = findViewById(R.id.submit);

        newProject = new Project();

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl = 1;
                android.support.v4.app.DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");

            }
        });

        targetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl = 2;
                android.support.v4.app.DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newProject.getStart() == null || newProject.getTarget() == null || projectName.getText() == null){
                    Toast.makeText(ProjectAddActivity.this, "all Fields are necessary", Toast.LENGTH_SHORT).show();
                }
                else{
                    newProject.setProjectName(projectName.getText().toString());
                    newProject.setManagerUserName(GlobalUtils.SessionManager.getUserName());
                    GlobalUtils.Queries.addProject(newProject);
                    GlobalUtils.Queries.addEmployeeProjectRelation(GlobalUtils.SessionManager.getUserName(),newProject.getProjectName());
                    finish();
                }
            }
        });
    }
}
