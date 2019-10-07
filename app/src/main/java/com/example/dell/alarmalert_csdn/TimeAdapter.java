package com.example.dell.alarmalert_csdn;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.ValueIterator;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static com.example.dell.alarmalert_csdn.MainActivity.timeAdapter;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {
    List<Clock> list;
    LayoutInflater layoutInflater;
    Context context;
    Calendar calendar = Calendar.getInstance();
    public static int pos;



    public TimeAdapter(List<Clock> list, Context context) {
        this.list = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public TimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.clockitem, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final TimeAdapter.ViewHolder viewHolder, final int i) {
        final Clock clock = list.get(i);
        pos = i;

        Log.e("i=======",i+"  "+clock.getClockType());
        if (clock.getClockType() == Clock.clock_open){
            viewHolder.aSwitch.setChecked(true);
            viewHolder.hour.setTextColor(context.getResources().getColor(R.color.colorBlack));
            viewHolder.minute.setTextColor(context.getResources().getColor(R.color.colorBlack));
            viewHolder.net.setTextColor(context.getResources().getColor(R.color.colorBlack));
            viewHolder.content.setTextColor(context.getResources().getColor(R.color.colorBlack));
        }else if (clock.getClockType() == Clock.clock_close){
            viewHolder.aSwitch.setChecked(false);
            viewHolder.hour.setTextColor(context.getResources().getColor(R.color.colorGray));
            viewHolder.minute.setTextColor(context.getResources().getColor(R.color.colorGray));
            viewHolder.net.setTextColor(context.getResources().getColor(R.color.colorGray));
            viewHolder.content.setTextColor(context.getResources().getColor(R.color.colorGray));
        }
        viewHolder.hour.setText(clock.getHour()+"");
        viewHolder.minute.setText(clock.getMinute()+"");

        viewHolder.content.setText(clock.getContent());
        viewHolder.todetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TODETAIL",clock.getClockType()+"");
                Intent intent = new Intent(context, ClockDetail.class);
                intent.putExtra("position", i);
                context.startActivity(intent);
            }
        });

        viewHolder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    clock.setClockType(Clock.clock_open);
                    //clock.updateAll();
                    clock.save();
                    Toast.makeText(context, "开启闹钟", Toast.LENGTH_SHORT).show();
                    viewHolder.hour.setTextColor(context.getResources().getColor(R.color.colorBlack));
                    viewHolder.minute.setTextColor(context.getResources().getColor(R.color.colorBlack));
                    viewHolder.net.setTextColor(context.getResources().getColor(R.color.colorBlack));
                    viewHolder.content.setTextColor(context.getResources().getColor(R.color.colorBlack));
                    Intent intent = new Intent(context, CallAlarm.class);
                    PendingIntent sender = PendingIntent.getBroadcast(
                            context, 0, intent, 0);
                    AlarmManager am;
                    am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clock.getHour()));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(clock.getMinute()));
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    Log.e("TAG",calendar.getTimeInMillis()+"");
                    Log.e("TAG",System.currentTimeMillis()+"");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (System.currentTimeMillis()>calendar.getTimeInMillis()+40000){
                            //加24小时
                            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+86400000, sender);
                        }else {
                            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                        }
                    }
                } else if (!isChecked){
                    clock.setClockType(Clock.clock_close);
                    //clock.updateAll();
                    clock.save();
                    Log.e("status------",clock.getHour()+clock.getMinute()+clock.getMinute()+clock.getClockType()+"");

                    Log.e("关闭闹钟",clock.getClockType()+"");
                    Intent intent = new Intent(context, CallAlarm.class);
                    PendingIntent sender=PendingIntent.getBroadcast(
                            context,0, intent, 0);
                    AlarmManager am;
                    am =(AlarmManager)context.getSystemService(ALARM_SERVICE);
                    am.cancel(sender);
                    Toast.makeText(context, "关闭闹钟", Toast.LENGTH_SHORT).show();
                    viewHolder.hour.setTextColor(context.getResources().getColor(R.color.colorGray));
                    viewHolder.minute.setTextColor(context.getResources().getColor(R.color.colorGray));
                    viewHolder.net.setTextColor(context.getResources().getColor(R.color.colorGray));
                    viewHolder.content.setTextColor(context.getResources().getColor(R.color.colorGray));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView hour;
        TextView minute;
        TextView content;
        TextView net;
        Switch aSwitch;
        LinearLayout todetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hour = itemView.findViewById(R.id.hour);
            minute = itemView.findViewById(R.id.minute);
            net = itemView.findViewById(R.id.net);
            content = itemView.findViewById(R.id.content_item);
            aSwitch = itemView.findViewById(R.id.switch_control);
            todetail = itemView.findViewById(R.id.todetail);
        }
    }
}
