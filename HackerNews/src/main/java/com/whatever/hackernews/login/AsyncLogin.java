package com.whatever.hackernews.login;

import android.os.AsyncTask;
import android.util.Log;
import com.whatever.hackernews.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by PetoU on 22/05/14.
 */
public class AsyncLogin extends AsyncTask<String, Void, String> {

    private String userName;
    private String password;
    private int responseCode;
    private String fnid;
    private AsyncLoginListener listener;

    public AsyncLogin(String userName, String password, AsyncLoginListener delegate) {

        this.userName = userName;
        this.password = password;
        this.listener = delegate;

    }

    @Override
    protected String doInBackground(String... params) {

        String GETurl = Consts.LOGIN_FORM_GET;
        String POSTurl = Consts.LOGIN_POST;
        String testUrl = Consts.MAINPAGE;
        Map<String, String> loginCookies = null;


        // parse HTML, token GET request
        try {
            Document doc = Jsoup.connect(GETurl).get();
            Elements form = doc.select("input");
            fnid = form.attr("value");

            Log.i("fnid", fnid);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // POST login request

        try {
            Connection.Response response = Jsoup.connect(POSTurl).data("u", userName, "p", password, "fnid", fnid)
                    .method(Connection.Method.POST)
                    .execute();

            loginCookies = response.cookies();

            for(Map.Entry<String, String> entry : loginCookies.entrySet()){
                  Log.i("cookie", entry.getKey() + " : " + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // test GET request

        try {
            Document doc = Jsoup.connect(testUrl)
                    .cookie("__cfduid", loginCookies.get("__cfduid"))
                    .cookie("user", loginCookies.get("user"))
                    .get();

            Elements test = doc.getElementsByClass("pagetop");

            for(Element e : test){
                Log.i("test", e.text());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //
    // send responseCode to delegate
    //
    @Override
    protected void onPostExecute(String s) {
        listener.onTaskComplete(responseCode, fnid);
    }

    public interface AsyncLoginListener {
        public void onTaskComplete(int responseCode, String responseString);
    }
}
