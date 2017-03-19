package com.example.bob.apka;

/**
 * Created by Bob on 2015-07-22.
 */
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Created by Bob on 2015-07-07.
 * @author Bob
 */
public class DBHelper extends SQLiteOpenHelper {

    /** Sciezka do bazy*/
    private static String DB_PATH;
    private final static String DB_NAME = "stops.db";
    private final static String FIELD_ID = "_id";
    private static String FIELD_NAME = "name";
    private static String TABLE_NAME = "stops";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private HashMap<String , String> Map;
    /** Konstruktor
     * Sets databse dir,setting up projection map ,opens databae */
    public DBHelper(Context context) {

        super(context,DB_NAME,null,1);
        this.myContext = context;
        DB_PATH = myContext.getApplicationInfo().dataDir + "/databases/";
        myDataBase = null;
        Map = new HashMap<String , String>();
        Map.put("_ID",FIELD_ID + " as " + "_id");
        Map.put(SearchManager.SUGGEST_COLUMN_TEXT_1,FIELD_NAME + " as " + SearchManager.SUGGEST_COLUMN_TEXT_1);
        Map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, FIELD_ID + " as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        try {
            createDataBase();
        } catch (Exception e) {
            Toast.makeText(myContext,e.toString(),Toast.LENGTH_LONG).show();
        }
        if(checkDB())openDataBase();
        else  Toast.makeText(myContext,"baza nie istnieje",Toast.LENGTH_LONG).show();
    }

    private void createDataBase() throws IOException {

        boolean dbExist = checkDB();
        if(!dbExist) {

            this.getReadableDatabase();
            try {
                copyDB();

            } catch (IOException e) {
                throw new Error("Error copying DB");
            }
        }

    }

    private boolean checkDB() {

        SQLiteDatabase check = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            check = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READONLY);

        } catch(SQLException e) {
            //TODO
            //Toast.makeText(myContext,e.toString(),Toast.LENGTH_LONG).show();
        }

        if(check != null){
            check.close();

        }

        return check != null ? true : false;
    }

    private void copyDB() throws IOException{

        InputStream input=myContext.getAssets().open((DB_NAME));
        String out = DB_PATH + DB_NAME;
        OutputStream output = new FileOutputStream(out);


        byte[] buffer = new byte[1024];
        int length;
        while((length = input.read(buffer)) > 0){
            output.write(buffer,0,length);
        }
        output.flush();
        output.close();
        input.close();
    }

    private void openDataBase() throws SQLException {

        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READONLY);

    }

    public Cursor getNames(String[] selArgs) {

        String selection = FIELD_NAME + " like ? ";
        if(selArgs!=null) selArgs[0] = "%"+selArgs[0]+"%";

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setProjectionMap(Map);

        queryBuilder.setTables(TABLE_NAME);

        Cursor c = queryBuilder.query(myDataBase,
                new String[] { "_ID",
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID },
                selection,
                selArgs,
                null,
                null,
                FIELD_NAME + " asc ","10"
        );
        return c;
    }

    public Cursor getName(String id) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(TABLE_NAME);

        Cursor c = queryBuilder.query(myDataBase,
                new String[] { "_id", "name" } ,
                "_id = ?", new String[] { id } , null, null, null ,"1"
        );

        return c;
    }

    public Cursor getTextName(String id) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(TABLE_NAME);

        Cursor c = queryBuilder.query(myDataBase,
                new String[] { "_id", "name" } ,
                "_id = ?", new String[] { id } , null, null, null ,"1"
        );

        return c;
    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getCursor(String query){
        return myDataBase.rawQuery(query,null);
    }
}