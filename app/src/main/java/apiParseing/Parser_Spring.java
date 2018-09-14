package apiParseing;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import listview.CommentItem;

public class Parser_Spring extends AsyncTask<CommentItem , Void , Void > {

    String str = "http://192.168.0.16:8080/comment/addList";
    DataInputStream dis = null;
    DataOutputStream dout = null;

    @Override
    protected Void doInBackground(CommentItem... items) {
        try {
            URL url = new URL(str);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(1000);
            urlConnection.setReadTimeout(1000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setDoOutput(true);

            JSONObject obj = new JSONObject();
            obj.put("id", items[0].getId());
            obj.put("text", items[0].getText());
            obj.put("star", items[0].getStar());
            obj.put("type",items[0].getType());
            dout = new DataOutputStream(urlConnection.getOutputStream());
            dout.write(obj.toString().getBytes());
            dout.flush();
            if( urlConnection.getResponseCode()== HttpURLConnection.HTTP_OK) {
                dis= new DataInputStream(urlConnection.getInputStream());
                Log.i("응답 : " , Integer.toString(urlConnection.getResponseCode()));
            }
            else
            {
                Log.i("응답 : " , Integer.toString(urlConnection.getResponseCode()));
            }
            dout.close();
            urlConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}