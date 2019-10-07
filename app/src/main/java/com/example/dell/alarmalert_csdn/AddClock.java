package com.example.dell.alarmalert_csdn;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import javax.security.auth.login.LoginException;

import static com.example.dell.alarmalert_csdn.MainActivity.list;
import static com.example.dell.alarmalert_csdn.MainActivity.timeAdapter;

public class AddClock extends AppCompatActivity implements View.OnClickListener {
    private Calendar calendar;
    private TextView show_hour;
    private TextView show_minute;
    private EditText content;
    private Button set;
    private Button save;
    private ImageView back;
    private TextView title;
    String hourformat;
    String minuteformat;
    Clock clock = new Clock();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clock);
        show_hour = findViewById(R.id.hour);
        show_minute = findViewById(R.id.minute);
        content = findViewById(R.id.content);
        set = findViewById(R.id.set_time);
        set.setOnClickListener(this);
        save = findViewById(R.id.save);
        back = findViewById(R.id.open_nav);
        back.setImageResource(R.drawable.ic_back);
        back.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("添加闹钟");
        save.setOnClickListener(this);
        calendar = Calendar.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_time:
                calendar.setTimeInMillis(System.currentTimeMillis());
                int mhour = calendar.get(Calendar.HOUR_OF_DAY);
                int mminute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(AddClock.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);


                        hourformat = format(hourOfDay);
                        minuteformat = format(minute);
                        Toast.makeText(AddClock.this, "" + hourformat + ":" + minuteformat, Toast.LENGTH_SHORT).show();
                        show_hour.setText(hourformat);
                        show_minute.setText(minuteformat);


                    }
                }, mhour, mminute, true).show();
                break;
            case R.id.save:
                Intent intent = new Intent(AddClock.this, CallAlarm.class);
                PendingIntent sender = PendingIntent.getBroadcast(
                        AddClock.this, 0, intent, 0);
                AlarmManager am;
                am = (AlarmManager) getSystemService(ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (System.currentTimeMillis()>calendar.getTimeInMillis()+40000){
                        //加24小时
                        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+86400000, sender);
                    }else {
                        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                    }
                }
                clock.setHour(hourformat);
                clock.setMinute(minuteformat);
                clock.setContent("" + content.getText().toString());
                clock.setClockType(Clock.clock_open);
                if (clock.getHour()!=null&&clock.getMinute()!=null) {
                    clock.save();
                    list.add(clock);
                    timeAdapter.notifyDataSetChanged();
                    Log.e("Listnumber======",list.size()+"");
                    finish();
                }else {
                    Toast.makeText(this, "请选择闹钟时间", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.open_nav:
                finish();
                break;


        }
    }

    private String format(int x) {
        String s = "" + x;
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }
}
