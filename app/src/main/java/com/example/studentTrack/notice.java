package com.example.studentTrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("ConstantConditions")
public class notice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notice");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void one(View v){
        Intent student = new Intent(getApplication(), notice1.class);
        startActivity(student);
    }

    public void two(View v){
        Intent teacher = new Intent(getApplication(), notice1.class);
        startActivity(teacher);
    }

    public void three(View v){
        Intent other = new Intent(getApplication(), notice1.class);
        startActivity(other);
    }
    public void four(View v){
        Intent other = new Intent(getApplication(), attendance.class);
        startActivity(other);
    }
}



