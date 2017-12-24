package com.example.zoir.myoffice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ConnectActivity extends AppCompatActivity {

    private Button mBtn;
    private ListView mlistItems;

    private Element[] nots;
    private WifiManager wifiManager;
    private List<ScanResult> wifiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        mBtn = (Button) findViewById(R.id.wifiBtn);
        mlistItems = (ListView) findViewById(R.id.listItems);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectWifi();
            }
        });

        mlistItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              startActivity(new Intent(ConnectActivity.this,ConnectWifi.class).putExtra("e1",nots[position].getTitle().toString()));
            }
        });
    }

    public void detectWifi() {
        this.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        this.wifiManager.startScan();
        this.wifiList = this.wifiManager.getScanResults();

        Log.d("TAG", wifiList.toString());

        this.nots = new Element[wifiList.size()];
        for( int i=0; i<wifiList.size(); i++ ) {
            String item = wifiList.get(i).toString();
            String[] vector_item = item.split(",");
            String item_ssid = vector_item[0];
            String item_capabilities = vector_item[2];
            String item_level = vector_item[3];
            String ssid = item_ssid.split(": ")[1];
            String security = item_capabilities.split(": ")[1];
            String level = item_level.split(": ")[1];

            nots[i] = new Element(ssid, security, level);
        }

        AdapterElements adapterElements = new AdapterElements(this);
        ListView netList = (ListView) findViewById(R.id.listItems);
        netList.setAdapter(adapterElements);
    }

    class AdapterElements extends ArrayAdapter<Object> {
        Activity context;
        public AdapterElements ( Activity context ) {
            super(context,R.layout.items,nots);
            this.context = context;
        }

        public View getView(int Position, View convetrView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.items, null);

            TextView tvSsid = (TextView) item.findViewById(R.id.tvSSID);
            tvSsid.setText(nots[Position].getTitle());

            TextView tvSecurity = (TextView) item.findViewById(R.id.tvSecurity);
            tvSecurity.setText(nots[Position].getSecurity());

            TextView tvLevel = (TextView) item.findViewById(R.id.tvLevel);
            //String level = nots[Position].getLevel();

            tvLevel.setText("High");
            return item;
        }
    }
}
