package com.example.bob.apka;

/**
 * Created by Bob on 2015-07-22.
 */
import android.app.Fragment;
import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Bob on 2015-07-08.
 */
public class StopsProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.bob.apka";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/stops");

    DBHelper myDB;

    private static final int SUGGESTIONS = 1;
    private static final int SEARCH = 2;
    private static final int GET = 3;

    UriMatcher matcher = buildUriMatcher();

    private UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY,SUGGESTIONS);
        uriMatcher.addURI(AUTHORITY, "stops",SEARCH);
        uriMatcher.addURI(AUTHORITY,"stops/#",GET);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        myDB = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c = null;
        switch(matcher.match(uri)) {
            case SUGGESTIONS :
                c = myDB.getNames(selectionArgs);
                break;
            case SEARCH :
                c = myDB.getNames(selectionArgs);
                break;
            case GET :
                String id = uri.getLastPathSegment();
                c = myDB.getName(id);

        }
        return c;
    }

    @Override
    public String getType(Uri uri) {

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

