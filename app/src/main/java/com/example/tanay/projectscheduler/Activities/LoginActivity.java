package com.example.tanay.projectscheduler.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tanay.projectscheduler.POJO.Employee;
import com.example.tanay.projectscheduler.R;
import com.example.tanay.projectscheduler.Utils.GlobalUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText uName,password;
    Button loginButton, signUpButton;
    String UNAME,PASSWORD;
    Employee loginEmployeeSnapshot = null;
    private DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uName = findViewById(R.id.uname);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginbutton);
        signUpButton = findViewById(R.id.signup);


        GlobalUtils.SessionManager.setSession(this);
        if( GlobalUtils.SessionManager.getUserName() != "" && GlobalUtils.SessionManager.getPassword() != "" ){
            Intent i = new Intent(LoginActivity.this, StartActivity.class);
            startActivity(i);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( uName.getText().toString().equals("") || password.getText().toString().equals("") ){
                    Toast.makeText(LoginActivity.this, "please enter both fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    UNAME = uName.getText().toString().trim();
                    PASSWORD = password.getText().toString().trim();

                    ValidityTask newtask = new ValidityTask();
                    newtask.execute();

                }
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    int valid = 0;

    public void validate(final String UNAME, final String PASSWORD){
        mref = FirebaseDatabase.getInstance().getReference();

        DatabaseReference employee = mref.child("Employees" + "/" + UNAME);

        employee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    if(dataSnapshot.child("password").getValue().equals(PASSWORD)){
                        valid = 1;
                        loginEmployeeSnapshot = dataSnapshot.getValue(Employee.class);
                        return;
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "incorrect password", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    valid = 2;
                    Toast.makeText(LoginActivity.this, "incorrect username", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){}
        });
    }

    class ValidityTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            while(valid == 0);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            validate(UNAME, PASSWORD);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if( valid == 1 ){
                GlobalUtils.SessionManager.setSession(LoginActivity.this, loginEmployeeSnapshot);
                Intent i = new Intent(LoginActivity.this, StartActivity.class);
                finish();
                startActivity(i);
            }
            super.onPostExecute(aVoid);
        }
    }
}
