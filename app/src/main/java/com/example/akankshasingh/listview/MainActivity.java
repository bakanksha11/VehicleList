package com.example.akankshasingh.listview;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Context context;
    ArrayList<HashMap<String, String>> vehicleList;
    JSONObject obj;
    JSONArray vehicleArray;
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        vehicleList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.vehicle_list);
        lv.setOnItemClickListener(this);

        new GetVehicleDetails().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (vehicleArray != null) {
            Intent myIntent = new Intent(MainActivity.this, ShowMap.class);
            try {
                myIntent.putExtra("Latitude", vehicleArray.getJSONObject(position).getString("Lati"));
                myIntent.putExtra("Longitude", vehicleArray.getJSONObject(position).getString("Long"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            startActivity(myIntent);
        }
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("jsondata.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    private class GetVehicleDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            if (loadJSONFromAsset(context) != null) {
                try {
                    vehicleArray = new JSONArray(loadJSONFromAsset(context));

                    for (int i = 0; i < vehicleArray.length(); i++) {
                        obj = vehicleArray.getJSONObject(i);
                        String vehicle = obj.getString("Vehicle");
                        String address = obj.getString("Location");
                        String date = obj.getString("Date");
                        String latitude = obj.getString("Lati");
                        String longitude = obj.getString("Long");

                        // tmp hash map for single vehicleListData
                        HashMap<String, String> vehicleListData = new HashMap<>();

                        // adding each child node to HashMap key => value
                        vehicleListData.put("Vehicle", vehicle);
                        vehicleListData.put("Location", address);
                        vehicleListData.put("Date", date);
                        vehicleListData.put("Lati", latitude);
                        vehicleListData.put("Long", longitude);

                        // adding vehicleListData to vehicleListData list
                        vehicleList.add(vehicleListData);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "didn't get json from file");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "didn't get json from file",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            VehicleListAdapter adapter = new VehicleListAdapter(MainActivity.this, vehicleList,
                    R.layout.list_item, new String[]{"Vehicle", "Date", "Location"},
                    new int[]{R.id.text_vehicle, R.id.text_date, R.id.text_address});
            lv.setAdapter(adapter);
        }
    }
}