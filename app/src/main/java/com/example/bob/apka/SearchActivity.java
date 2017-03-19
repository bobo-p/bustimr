package com.example.bob.apka;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ArrayAdapter adapter;
    List result;
    DBHelper Helper;
    ListView myView;
    SimpleCursorAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        Helper = new DBHelper(getBaseContext());
        myView = (ListView)findViewById(R.id.listNames);

        myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // Creating a uri to fetch country details corresponding to selected listview item
                Uri data = Uri.withAppendedPath(StopsProvider.CONTENT_URI, String.valueOf(id));
                //MainActivity.res = ((TextView)view).getText().toString();
                Intent stopInent = new Intent(getApplicationContext(),ResultActivity.class);
                stopInent.setData(data);
                startActivity(stopInent);
                finish();
            }
        });

        myAdapter = new SimpleCursorAdapter(getBaseContext(),
                android.R.layout.simple_list_item_1,null,
                new String[] {SearchManager.SUGGEST_COLUMN_TEXT_1},
                new int[] {android.R.id.text1},0);

        myView.setAdapter(myAdapter);

        Intent intent=getIntent();
        if(intent.getAction().equals(Intent.ACTION_VIEW)){

            /*Cursor c = Helper.getTextName(intent.getData().getLastPathSegment());

            String n = "";
            if(c.moveToFirst()) {
                n=c.getString(c.getColumnIndex(c.getColumnName(0)));
            }
            MainActivity.res = n;*/

            //MainActivity.res = ((TextView)view).getText().toString();
            Intent stopInent = new Intent(this,ResultActivity.class);
            stopInent.setData(intent.getData());
            startActivity(stopInent);
            finish();

        }else if(intent.getAction().equals(Intent.ACTION_SEARCH)){ // If this activity is invoked, when user presses "Go" in the Keyboard of Search Dialog
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query,"");
        }
    }




    private void doSearch(String query,String id){
        Bundle data = new Bundle();
        data.putString("query", query);
        data.putString("id",id);
        // Invoking onCreateLoader() in non-ui thread
        getSupportLoaderManager().initLoader(1, data, this);
    }


    /** This method is invoked by initLoader() */

    public Loader<Cursor> onCreateLoader(int arg0, Bundle data) {
        Uri uri = Uri.parse("a");
        String u = StopsProvider.CONTENT_URI.toString() + "/" + data.getString("id");
        if(data.getString("id")=="")  uri =StopsProvider.CONTENT_URI;
        else uri =  Uri.parse(u);
        return new CursorLoader(getBaseContext(), uri, null, null , new String[]{data.getString("query")}, null);
    }

    /** This method is executed in ui thread, after onCreateLoader() */
    public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
        myAdapter.swapCursor(c);
    }


    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
    }
}