package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class more_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String locationsName = extras.getString("locationsName");
            String locationName = extras.getString("locationName");
            String dataTime = extras.getString("dataTime");
            String value = extras.getString("value");

            // 假设你已经在布局文件中定义了对应的 TextView 对象
            TextView tvLocationsName = findViewById(R.id.tvMoreLoca);
            TextView tvLocationName = findViewById(R.id.tvMoreArea);
            TextView tvDataTime = findViewById(R.id.tvMoreTime);
            TextView tvValue = findViewById(R.id.tvMoreTempature);

            // 将数据设置到 TextView 对象中
            tvLocationsName.setText(locationsName);
            tvLocationName.setText(locationName);
            tvDataTime.setText("營業時間: " + dataTime);
            tvValue.setText("民眾滿意度: " + value);
        }
    }

    public void goBack(View view) {
        finish();
    }
}