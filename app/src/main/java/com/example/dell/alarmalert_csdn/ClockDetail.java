package com.example.dell.alarmalert_csdn;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
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

import static com.example.dell.alarmalert_csdn.MainActivity.list;
import static com.example.dell.alarmalert_csdn.MainActivity.timeAdapter;

public class ClockDetail extends AppCompatActivity implements View.OnClickListener {
    private Calendar calendar;
    private TextView show_hour;
    private TextView show_minute;
    private EditText content;
    private ImageView back;
    private TextView title;
    private Button set;
    private Button save;
    private Button delete;
    Clock clock;
    int position;
    String hourformat;
    String minuteformat;
    Context context = ClockDetail.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_detail);
        show_hour = findViewById(R.id.hour);
        show_minute = findViewById(R.id.minute);
        content = findViewById(R.id.content);
        set = findViewById(R.id.set_time);
        set.setOnClickListener(this);
        save = findViewById(R.id.save);
        save.setOnClickListener(this);
        delete = findViewById(R.id.delete);
        delete.setOnClickListener(this);
        back = findViewById(R.id.open_nav);
        back.setOnClickListener(this);
        back.setImageResource(R.drawable.ic_back);
        title = findViewById(R.id.title);
        title.setText("闹钟详情");
        calendar = Calendar.getInstance();
        initView();

    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        position = getIntent().getIntExtra("position", -1);
        clock = list.get(position);
        Log.e("position", position + "");
        if (clock.getHour() != null && clock.getMinute() != null) {
            hourformat = formatString(clock.getHour());
            minuteformat = formatString(clock.getMinute());
        }
        content.setText(clock.getContent());
        show_hour.setText(clock.getHour() + "");
        show_minute.setText(clock.getMinute() + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_time:
                calendar.setTimeInMillis(System.currentTimeMillis());
                int mhour = calendar.get(Calendar.HOUR_OF_DAY);
                int mminute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(ClockDetail.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        hourformat = format(hourOfDay);
                        minuteformat = format(minute);
                        Toast.makeText(ClockDetail.this, "" + hourformat + ":" + minuteformat, Toast.LENGTH_SHORT).show();
                        show_hour.setText(hourformat);
                        show_minute.setText(minuteformat);


                    }
                }, mhour, mminute, true).show();
                break;
            case R.id.save:
                clock.setHour(hourformat);
                clock.setMinute(minuteformat);
                clock.setContent("" + content.getText().toString());
                clock.setClockType(Clock.clock_open);
                clock.save();
                Intent intent = new Intent(ClockDetail.this, CallAlarm.class);
               // intent.putExtra("content",clock.getContent());
                //sendBroadcast(intent);
                PendingIntent sender = PendingIntent.getBroadcast(
                        ClockDetail.this, 0, intent, 0);
                AlarmManager am;
                am = (AlarmManager) getSystemService(ALARM_SERVICE);
                Log.e("gethour",clock.getHour());
                Log.e("gethour",clock.getMinute());
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clock.getHour()));
                calendar.set(Calendar.MINUTE, Integer.parseInt(clock.getMinute()));
                Log.e("TAG",calendar.getTimeInMillis()+"");
                Log.e("TAG",System.currentTimeMillis()+"");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (System.currentTimeMillis()>calendar.getTimeInMillis()+60000){
                        //加24小时
                        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+86400000, sender);
                    }else {
                        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                    }
                }

                timeAdapter.notifyDataSetChanged();
                finish();
                break;
            case R.id.delete:
                clock.delete();
                timeAdapter.notifyDataSetChanged();
                Intent intent1 = new Intent(context, CallAlarm.class);
                PendingIntent sender1=PendingIntent.getBroadcast(
                        context,0, intent1, 0);
                am =(AlarmManager)context.getSystemService(ALARM_SERVICE);
                am.cancel(sender1);
                finish();
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

    private String formatString(String x) {
        String s = x;
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }
}
