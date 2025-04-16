package com.example.studentTrack.attendancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.studentTrack.R;

import java.util.ArrayList;

public class MainActivityAttendance extends AppCompatActivity {

    private Database database;
    private ArrayList<Class> classes;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main_activity_attendance);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ListView listview = findViewById(R.id.listview);
        Button new_class = findViewById(R.id.new_class);

        new_class.setOnClickListener(v-> {
            Intent intent = new Intent(MainActivityAttendance.this, ClassEditor.class);
            startActivity(intent);
        });
        Log.d("checking", "1");
        database = new Database();
        Log.d("checking", "2");

        classes = database.getAllClasses();
        Log.d("checking", "3");

        listAdapter = new ListAdapter();
        Log.d("checking", "4");

        listview.setAdapter(listAdapter);
        Log.d("checking", "5");


    }

    @Override
    protected void onResume() {
        super.onResume();
        classes = database.getAllClasses();
        listAdapter.notifyDataSetChanged();
    }

    public class ListAdapter extends BaseAdapter {

        public ListAdapter() {}

        @Override
        public int getCount() {
            return classes.size();
        }

        @Override
        public Class getItem(int i) {
            return classes.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint({"InflateParams", "ViewHolder"})
            View v = inflater.inflate(R.layout.list_item, null);
            TextView text = v.findViewById(R.id.text);
            text.setText(classes.get(i).getClassName());

            text.setOnClickListener(v1-> {
                Intent intent = new Intent(MainActivityAttendance.this, ShowSessions.class);
                intent.putExtra("Class ID", classes.get(i).getID());
                intent.putExtra("Class Name", classes.get(i).getClassName());
                startActivity(intent);
            });

            text.setOnLongClickListener(v1-> {
                Intent intent = new Intent(MainActivityAttendance.this, ClassEditor.class);
                intent.putExtra("Class ID", classes.get(i).getID());
                intent.putExtra("Class Name", classes.get(i).getClassName());
                startActivity(intent);
                return true;
            });
            return v;
        }
    }

}