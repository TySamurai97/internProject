package com.example.tanay.projectscheduler.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tanay.projectscheduler.POJO.Employee;
import com.example.tanay.projectscheduler.R;
import com.example.tanay.projectscheduler.Utils.GlobalUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    EditText name, companyname, userName, password, email;
    CheckBox isManager;
    Button signup;
    TextView messageBox;
    DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
    Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.projectName);
        companyname = findViewById(R.id.companyname);
        userName = findViewById(R.id.uname);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        signup = findViewById(R.id.signup);
        isManager = findViewById(R.id.ismanager);
        messageBox = findViewById(R.id.messagebox);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString()!=null && companyname.getText().toString()!=null && userName.getText().toString()!=null && password.getText().toString()!=null && email.getText().toString()!=null){
                    employee = new Employee(name.getText().toString(),
                            companyname.getText().toString(),
                            userName.getText().toString(),
                            password.getText().toString(),
                            email.getText().toString(),
                            isManager.isChecked());
                    ValidityTask task = new ValidityTask();
                    task.execute();
                }
                else{
                    messageBox.setText("All fields are required/n");
                }
            }
        });
    }

    Integer flag = 0;
    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    class ValidityTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            flag = 0;
            mref.child("Employees" + "/" + employee.getUserName()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() == null){
                        flag = 1;
                    }
                    else{
                        flag = 2;
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while(flag == 0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            String message = "";
            if(flag == 1) {
                if(!isValidEmail(employee.geteMail())){
                    flag = 2;
                    message += "invalid email.\n";
                }
                else {
                    DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
                    mref.child("Employees").child(employee.getUserName()).setValue(employee);
                    GlobalUtils.SessionManager.setSession(SignUpActivity.this, employee);
                    Intent intent = new Intent(SignUpActivity.this, StartActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
            else{
                message += "Username already taken.\n";
            }
            messageBox.setText(message);
            super.onPostExecute(aVoid);
        }

    }
}