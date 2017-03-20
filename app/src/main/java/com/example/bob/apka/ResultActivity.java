package com.example.bob.apka;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class ResultActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mUri;
    private TextView res;
    public List<Data> list;
    MyAdapter adapter;
    String filename;
    ListView lista;
    ArrayList<String> stops = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result);
        filename = "myfile";
        makeList();
        Intent intent = getIntent();
        mUri = intent.getData();
        MyBus.getInstance().register(this);

        lista = (ListView) findViewById(R.id.resultView);
        list = new ArrayList<Data>();
        adapter = new MyAdapter(this, list);
        lista.setAdapter(adapter);
        getSupportLoaderManager().initLoader(0,null,this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);

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
    /** Invoked by initLoader() */
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(getBaseContext(), mUri, null, null , null, null);
    }

    /** Invoked by onCreateLoader(), will be executed in ui-thread */
    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        if(cursor.moveToFirst()){
            saveItem(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
            saveAndClose();
           new DataGiver().execute(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0))));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
    }
    @com.squareup.otto.Subscribe
    public void onAsyncTaskResult(ReceiveEvent event) {

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
    @Override
    protected void onStop() {
        super.onStop();

    }

    private void makeList() {
        try {
            FileInputStream inputStream = new FileInputStream(this.getFilesDir().getPath() + "/" + filename);
            InputStreamReader r = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(r);
            String t;
            while((t = reader.readLine()) != null){
                stops.add(t);
            }
            // s = this.getFilesDir().list();
        } catch (Exception e) {
            Log.e("error",e.toString());
        }
    }

    private void saveItem(String s) {

        if(stops.size() < 10) stops.add(s);
        else {
            if(stops.contains(s))
            stops.remove(0);
            stops.add(s);
        }
    }

    private void saveAndClose() {

        try {
            FileOutputStream out = new FileOutputStream(this.getFilesDir().getPath() + "/" + filename);
            OutputStreamWriter w = new OutputStreamWriter(out);
            BufferedWriter writer = new BufferedWriter(w);


            for(String s : stops) {
                writer.write(s);
                writer.newLine();
            }

            writer.close();
            out.close();
        } catch(Exception e) {
            Log.e("error",e.toString());
        }

    }
}
