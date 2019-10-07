package com.example.dell.alarmalert_csdn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutAuther extends AppCompatActivity {
    private ImageView back;
    private TextView title;
    private Button tosupport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_auther);
        back = findViewById(R.id.open_nav);
        title = findViewById(R.id.title);
        back.setImageResource(R.drawable.ic_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setText("关于作者");
        tosupport = findViewById(R.id.tosupport);
        tosupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutAuther.this,Support.class);
                startActivity(intent);
            }
        });

    }
}
