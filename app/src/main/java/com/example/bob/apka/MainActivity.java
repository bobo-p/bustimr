package com.example.bob.apka;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Bus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    ArrayAdapter<String> ad;
    ListView lista;
    ArrayList<String> li;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        li = new ArrayList<String>();
       // Toast.makeText(getApplicationContext(),getApplicationContext().getAssets().toString(),Toast.LENGTH_LONG).show();
        lista = (ListView) findViewById(R.id.listView);


        readFile();

        ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, li);
        Integer wiela = li.size();
        Toast.makeText(getApplicationContext(),wiela.toString(),Toast.LENGTH_LONG).show();
        lista.setAdapter(ad);
        final ListView myView = (ListView)findViewById(R.id.listView);

        myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedFromList =(myView.getItemAtPosition(position).toString());
                String[] parts = selectedFromList.trim().split(" ");
                String myId="";
                if(parts.length>=1)
                    myId=parts[0].trim();
                // Creating a uri to fetch country details corresponding to selected listview item
                Uri data = Uri.withAppendedPath(StopsProvider.CONTENT_URI, String.valueOf(myId));
                //MainActivity.res = ((TextView)view).getText().toString();
                Intent stopInent = new Intent(getApplicationContext(),ResultActivity.class);
                stopInent.setData(data);
                startActivity(stopInent);
                finish();
            }
        });
        //ad.notifyDataSetChanged();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_search) {
            onSearchRequested();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        ad.notifyDataSetChanged();

    }

    @Override
    public  void onResume() {
        super.onResume();
        readFile();
        ad.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void readFile() {

        String filename = "myfile";

        InputStream inputStream;
        BufferedReader reader;
        li.clear();
        try {
            inputStream = new FileInputStream(this.getFilesDir().getPath() + "/" + filename);
            InputStreamReader r = new InputStreamReader(inputStream);
            reader = new BufferedReader(r);
            String t;
            while((t = reader.readLine()) != null){
                li.add(t);
            }
            reader.close();
            r.close();
            inputStream.close();
            // s = this.getFilesDir().list();
        } catch (Exception e) {
            Log.e("error",e.toString());
        }
    }
}

