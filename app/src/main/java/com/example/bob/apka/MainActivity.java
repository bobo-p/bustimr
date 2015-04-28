package com.example.bob.apka;

import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.*;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    public int count;
    public List<Data> list;
    MyAdapter adapter;
    ListView lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = 0;
        MyBus.getInstance().register(this);
        list = new ArrayList<Data>();

        lista = (ListView) findViewById(R.id.listView);
        adapter = new MyAdapter(this, list);
        lista.setAdapter(adapter);
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

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {

        EditText nr=(EditText)findViewById(R.id.editText);
        new DataGiver().execute(nr.getText().toString());
    }


    @com.squareup.otto.Subscribe
    public void onAsyncTaskResult(ReceiveEvent event) {
        //for (int i=0;i<4;i++) list.add(data[i]);
        //Toast.makeText(this,event.getResult(),Toast.LENGTH_LONG).show();
        //Toast.makeText(this,event.getResult().get(0).a,Toast.LENGTH_LONG).show();
       // ListView lista = (ListView) findViewById(R.id.listView);
        //adapter = new MyAdapter(this, event.getResult());
        //lista.setAdapter(adapter);
        list.clear();
        for(Data x :event.getResult()) {
            list.add(x);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        adapter.notifyDataSetChanged();

    }
}

