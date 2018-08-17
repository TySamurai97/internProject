package com.example.tanay.projectscheduler.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.solver.widgets.ConstraintHorizontalLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tanay.projectscheduler.Activities.ProjectAddActivity;
import com.example.tanay.projectscheduler.Activities.StartActivity;
import com.example.tanay.projectscheduler.POJO.EmployeeProjectRelation;
import com.example.tanay.projectscheduler.POJO.Project;
import com.example.tanay.projectscheduler.R;
import com.example.tanay.projectscheduler.Utils.GlobalUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ProjectListFragment extends Fragment {

    ArrayList<Project> projectList = new ArrayList<>();
    RecyclerView projectsRV;
    ProjectAdapter adapter;
    FloatingActionButton fab;

    public ProjectListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.project_list_fragment,container,false);
        fab = rootView.findViewById(R.id.fab);
        projectsRV = rootView.findViewById(R.id.projectListRecyclerView);
        adapter = new ProjectAdapter();
        projectsRV.setLayoutManager(new LinearLayoutManager( getActivity()));
        projectsRV.setAdapter(adapter);

        fetchProjects();

        if(!GlobalUtils.SessionManager.isIsManager()) fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ProjectAddActivity.class);
                startActivity(i);
            }
        });

        return rootView;
    }

    HashSet<String> projectNameSet = new HashSet<>();

    private void fetchProjects(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference projectsRef = ref.child("Projects");
        DatabaseReference employeeProjectRelationRef = ref.child("EmployeeProjectRelation");
        String userName = GlobalUtils.SessionManager.getUserName();

        employeeProjectRelationRef.orderByChild("userName").equalTo(userName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    projectNameSet.add(ds.child("projectName").getValue().toString());
                }
                for(String proj : projectNameSet){
                    projectsRef.orderByChild("projectName")
                            .equalTo(proj).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ds) {
                            for(DataSnapshot d : ds.getChildren())
                                projectList.add(d.getValue(Project.class));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                PopulationTask task = new PopulationTask();
                task.execute();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private class PopulationTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            while(projectList.size() != projectNameSet.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }
    }

    public class ProjectHolder extends RecyclerView.ViewHolder{
        TextView startDate, targetDate, projectName;
        ConstraintLayout element;
        public ProjectHolder(View itemView) {
            super(itemView);
            this.startDate = itemView.findViewById(R.id.startDate);
            this.targetDate = itemView.findViewById(R.id.targetDate);
            this.projectName = itemView.findViewById(R.id.projectName);
            this.element = itemView.findViewById(R.id.projecteEement);
        }
    }

    public class ProjectAdapter extends RecyclerView.Adapter<ProjectHolder>{
        @NonNull
        @Override
        public ProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater li = getLayoutInflater();
            View itemview = li.inflate(R.layout.project_element,parent,false);
            return new ProjectHolder(itemview);
        }

        @Override
        public void onBindViewHolder(@NonNull final ProjectHolder holder, int position) {
            holder.projectName.setText(projectList.get(position).getProjectName());
            holder.startDate.setText(projectList.get(position).getStart());
            holder.targetDate.setText(projectList.get(position).getTarget());
            if(GlobalUtils.SessionManager.isIsManager()) {
                holder.element.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        createAddEmployeeDialog(holder.projectName.getText().toString());
                        return true;
                    }
                });
                holder.startDate.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        createAddEmployeeDialog(holder.projectName.getText().toString());
                        return true;
                    }
                });
                holder.targetDate.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        createAddEmployeeDialog(holder.projectName.getText().toString());
                        return true;
                    }
                });
                holder.projectName.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        createAddEmployeeDialog(holder.projectName.getText().toString());
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return projectList.size();
        }
    }

    public void createAddEmployeeDialog(final String pName){
        LayoutInflater li = getLayoutInflater();
        View alertView = li.inflate(R.layout.dialog_box_layout,null);
        final EditText input = alertView.findViewById(R.id.employeeName);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(alertView);
        builder.setTitle("Add an Employee to project " + pName);
        builder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                DatabaseReference employees = ref.child("Employees");
                if(input.getText() == null){
                    Toast.makeText(getActivity(), "this field is necessary", Toast.LENGTH_SHORT).show();
                }
                else{
                    employees.orderByKey().equalTo(input.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot==null || dataSnapshot.getChildren()==null) {
                                //Key does not exist
                                Toast.makeText(getActivity(), "User name does not exist", Toast.LENGTH_SHORT).show();
                            } else {
                                //Key exists
                                GlobalUtils.Queries.addEmployeeProjectRelation(input.getText().toString(), pName);
                                Toast.makeText(getActivity(), "User successfully added", Toast.LENGTH_SHORT).show();

                            }
                            dialog.cancel();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        builder.setCancelable(false).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // create alert dialog
        AlertDialog alertDialog = builder.create();

        // show it
        alertDialog.show();
    }

}