package com.example.studentTrack;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


@SuppressWarnings({"ConstantConditions", "CanBeFinal", "WeakerAccess", "RedundantCast"})
public class eee extends AppCompatActivity {

    ListView listView;

    int[] images = {R.drawable.teacher,
            R.drawable.teacher,
            R.drawable.teacher,
            R.drawable.teacher,
            R.drawable.mam,
            R.drawable.mam,

    };
    String[] Name = {"Prof. Balaji Prasad Padhi, HOD",
            "Prof.Dipankar Panigrahi",
            "Prof.Sovit Kumar Pradhan",
            "Prof.Debswarup Rath",
            "Prof.Sabita Panigrahy",
            "Prof.Krishna Kumari Vegad",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eee);

//        listView = (ListView) findViewById(R.id.listcse);
//        CustomAdapter customAdapter = new CustomAdapter();
//        listView.setAdapter(customAdapter);



        final TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        //tab1
        TabHost.TabSpec spec = tabHost.newTabSpec("ABOUT");
        spec.setContent(R.id.tab1);
        spec.setIndicator("ABOUT");
        tabHost.addTab(spec);
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#1669cf"));
        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).findViewById(android.R.id.title);
        tv.setTextColor(Color.WHITE);

        //tab2
        spec = tabHost.newTabSpec("HOD'S DESK");
        spec.setContent(R.id.tab2);
        spec.setIndicator("HOD'S DESK");
        tabHost.addTab(spec);


        //tab3
//        spec = tabHost.newTabSpec("FACULTY");
//        spec.setContent(R.id.tab3);
//        spec.setIndicator("FACULTY");
//        tabHost.addTab(spec);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                int tab = tabHost.getCurrentTab();
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    // When tab is not selected
                    tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#444444"));
                    TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                    tv.setTextColor(Color.WHITE);
                }
                // When tab is selected
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#FDD030"));
                TextView tv = (TextView) tabHost.getTabWidget().getChildAt(tab).findViewById(android.R.id.title);
                tv.setTextColor(Color.BLACK);
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("EEE");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("RedundantCast")
    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            @SuppressLint({"InflateParams", "ViewHolder"}) View view = getLayoutInflater().inflate(R.layout.customlayout,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imgview);
            TextView textView = (TextView) view.findViewById(R.id.textview);

            imageView.setImageResource(images[position]);
            textView.setText(Name[position]);
            return view;
        }
    }
}



