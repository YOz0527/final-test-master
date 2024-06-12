package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getSimpleName() + "My";
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        catchData();
    }

    private void catchData() {
        String catchData = "https://api.jsonserve.com/Ke2GQO";
        ProgressDialog dialog = ProgressDialog.show(this, "讀取中", "請稍候", true);
        new Thread(() -> {
            try {
                URL url = new URL(catchData);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    json.append(line);
                }

                JSONObject jsonObject = new JSONObject(json.toString());
                JSONArray locationsArray = jsonObject.getJSONObject("records").getJSONArray("locations");

                for (int i = 0; i < locationsArray.length(); i++) {
                    JSONObject location = locationsArray.getJSONObject(i);
                    String locationsName = location.getString("locationsName");
                    JSONArray locationArray = location.getJSONArray("location");

                    for (int j = 0; j < locationArray.length(); j++) {
                        JSONObject loc = locationArray.getJSONObject(j);
                        String locationName = loc.getString("locationName");
                        JSONArray weatherElementArray = loc.getJSONArray("weatherElement");

                        for (int k = 0; k < weatherElementArray.length(); k++) {
                            JSONObject weatherElement = weatherElementArray.getJSONObject(k);
                            String elementName = weatherElement.getString("elementName");

                            if ("T".equals(elementName)) {
                                JSONArray timeArray = weatherElement.getJSONArray("time");

                                for (int l = 0; l < timeArray.length(); l++) {
                                    JSONObject time = timeArray.getJSONObject(l);
                                    String dataTime = time.getString("dataTime");
                                    String value = time.getJSONArray("elementValue").getJSONObject(0).getString("value");

                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("locationsName", locationsName);
                                    hashMap.put("locationName", locationName);
                                    hashMap.put("dataTime", dataTime);
                                    hashMap.put("value", value);

                                    arrayList.add(hashMap);
                                }
                            }
                        }
                    }
                }

                Log.d(TAG, "catchData: " + arrayList);

                runOnUiThread(() -> {
                    dialog.dismiss();
                    RecyclerView recyclerView;
                    MyAdapter myAdapter;
                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                    myAdapter = new MyAdapter();
                    recyclerView.setAdapter(myAdapter);
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvLoc, tvAr, tvDataTime, tvTemp;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvLoc = itemView.findViewById(R.id.tvMoreLoca);
                tvAr = itemView.findViewById(R.id.tvMoreArea);
                tvDataTime = itemView.findViewById(R.id.tvMoreTime);
                tvTemp = itemView.findViewById(R.id.tvMoreTempature);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_data_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            HashMap<String, String> item = arrayList.get(position);
            holder.tvLoc.setText(item.get("locationsName"));
            holder.tvAr.setText(item.get("locationName"));
            holder.tvDataTime.setText("營業時間：" + item.get("dataTime"));
            holder.tvTemp.setText("民眾滿意度：" + item.get("value"));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), more_info.class);
                    intent.putExtra("locationsName", item.get("locationsName"));
                    intent.putExtra("locationName", item.get("locationName"));
                    intent.putExtra("dataTime", item.get("dataTime"));
                    intent.putExtra("value", item.get("value"));
                    v.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}


