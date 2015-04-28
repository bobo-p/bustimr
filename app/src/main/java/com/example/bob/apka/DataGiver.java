package com.example.bob.apka;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bob on 2015-01-29.
 */
public class DataGiver extends AsyncTask<String,Void,List<Data> > {


    public String resString;
    public List<Data> list;
    public DataGiver() {

    }

    public List<Data> doInBackground(String...args){
            list=new ArrayList<Data>();
       try {
            String con="http://opole.kiedyprzyjedzie.pl/m/";
           con+=args[0];
            Document doc = Jsoup.connect(con).get();
            Elements el=doc.getElementsByTag("table");
            Elements line=el.get(0).select("td.line");
            Elements direction=el.get(0).select("td.time");
            Elements time=el.get(0).select("td.direction");
            int count=0;
            for(Element element:line) {
                list.add(new Data(line.get(count).text() ,direction.get(count).text(),time.get(count).text() ));
                count++;
            }




        }
        catch (Exception e) {
            //return e.toString();
        }
        return list;
    }
    public void onPostExecute(List<Data> res) {
        MyBus.getInstance().post(new ReceiveEvent(res));
    }
}
