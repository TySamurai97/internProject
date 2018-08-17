package com.example.tanay.projectscheduler.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tanay.projectscheduler.POJO.Employee;
import com.example.tanay.projectscheduler.POJO.EmployeeProjectRelation;
import com.example.tanay.projectscheduler.POJO.Project;
import com.google.firebase.database.FirebaseDatabase;

import static java.lang.Boolean.FALSE;

public class GlobalUtils {
    public static class SessionManager {
        private static SharedPreferences session;

        public static String getName() {
            return session.getString("name","");
        }

        public static String getCompanyname() {
            return session.getString("compName","");
        }

        public static String getUserName() {
            return session.getString("Uname","");
        }

        public static String getPassword() {
            return session.getString("pass","");
        }

        public static String getEmail() {
            return session.getString("email","");
        }

        public static boolean isIsManager() {
            return session.getBoolean("isManager",FALSE);
        }

        public static void setSession(Context context){
            session = context.getSharedPreferences("sessionref", context.MODE_PRIVATE);
        }

        public static void clear() {
            SharedPreferences.Editor editor = session.edit();
            editor.clear();
            editor.apply();
        }

        public static void setSession(Context context,Employee employee) {
            session = context.getSharedPreferences("sessionref", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = session.edit();
            editor.putString("Uname",employee.getUserName());
            editor.putString("name", employee.getName());
            editor.putString("compName", employee.getCompanyName());
            editor.putString("pass", employee.getPassword());
            editor.putString("email", employee.geteMail());
            editor.putBoolean("isManager", employee.getManager());
            editor.apply();
        }
    }

    public static class Queries{
        public static void addEmployeeProjectRelation(String uName, String projectName){
            String childKey = FirebaseDatabase.getInstance().getReference("EmployeeProjectRelation").push().getKey();
            FirebaseDatabase.getInstance().getReference("EmployeeProjectRelation").child(childKey).setValue(new EmployeeProjectRelation(uName,projectName));
        }

        public static void addProject(Project project){
            String childKey = FirebaseDatabase.getInstance().getReference("Projects").push().getKey();
            FirebaseDatabase.getInstance().getReference("Projects").child(childKey).setValue(project);
        }
    }

}
