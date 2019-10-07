package com.example.dell.alarmalert_csdn;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static List<Clock> list = new ArrayList<>();
    public static TimeAdapter timeAdapter;
    RecyclerView recyclerView;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    private ImageView more;
    TextView title;
    Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = findViewById(R.id.title);
        recyclerView = findViewById(R.id.clock_list);
        navigationView = findViewById(R.id.nav);
        more = findViewById(R.id.open_nav);
        drawerLayout = findViewById(R.id.layout1);
        initTitle();
        LitePal.getDatabase();
        initRecyclerView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initRecyclerView();
    }

    private void initTitle() {
        more.setImageResource(R.drawable.ic_more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
            }
        });
        title.setText("你的闹钟");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.about:
                        Intent intent = new Intent(context, AboutAuther.class);
                        startActivity(intent);
                        break;
                    case R.id.explain:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("使用说明");
                        builder.setMessage("更新后的闹钟APP,供学习交流使用,暂时一次只能运行一个闹钟,下一个版本继续改");
                        builder.setCancelable(true);
                        builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                        break;
                    case R.id.add:
                        Intent intent1 = new Intent(MainActivity.this, AddClock.class);
                        startActivity(intent1);
                        break;
                }
                return true;
            }
        });

    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        timeAdapter = new TimeAdapter(list, context);
        recyclerView.setAdapter(timeAdapter);
        list.clear();
        List<Clock> list1 = DataSupport.findAll(Clock.class);
        for (Clock clock : list1) {
            list.add(clock);
        }
        timeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)){
            drawerLayout.closeDrawer(navigationView);
        }
        else {
            MainActivity.this.finish();
        }
    }
}
