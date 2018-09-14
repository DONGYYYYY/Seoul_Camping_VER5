package apiParseing;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import listview.CommentItem;

public class Parser_Spring_response extends AsyncTask<String , Void , ArrayList<CommentItem>> {

        String str = "http://192.168.0.16:8080/comment/listAll";
        OutputStream os = null;
        DataInputStream dis = null;
        String  recieveMsg;

         ArrayList<CommentItem> datamap = new ArrayList<>();

        @Override
        protected  ArrayList<CommentItem> doInBackground(String ... types) {
        try {
            str = str+"?type="+types[0];
            URL url = new URL(str);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(1000);
            urlConnection.setReadTimeout(1000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoInput(true);

            InputStreamReader temp = new InputStreamReader(urlConnection.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(temp);
            StringBuffer buffer = new StringBuffer();

            while ((str = reader.readLine()) != null) {
                buffer.append(str);
            }
            recieveMsg = buffer.toString();
            datamap = jsonparse(recieveMsg);
            reader.close();
            if( urlConnection.getResponseCode()== HttpURLConnection.HTTP_OK) {
                dis= new DataInputStream(urlConnection.getInputStream());
                Log.i("응답 : " , Integer.toString(urlConnection.getResponseCode()));
            }
            else
            {
                Log.i("응답 : " , Integer.toString(urlConnection.getResponseCode()));
            }
            urlConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return datamap;
    }

    public ArrayList<CommentItem> jsonparse(String jsonString) {

        CommentItem data;
        ArrayList<CommentItem> tmp = new ArrayList<>();
        String id,text,regdate,type;
        float star;
        int number;

        try {
            JSONArray jarray = new JSONArray(jsonString);
            for (int i = 0; i < jarray.length(); i++) {
                data = new CommentItem();
                JSONObject jObject = jarray.getJSONObject(i);

                number = Integer.parseInt(jObject.optString("number"));
                id = jObject.optString("id");//아이디
                text = jObject.optString("text");//온도
                star = Float.parseFloat(jObject.optString("star"));//하늘상태
                regdate = jObject.optString("regdate");//눈,비 0:맑음
                type = jObject.optString("type");
                Log.i("number : ",Integer.toString(number));
                Log.i("id : ",id);
                data.setId(id);
                data.setNumber(number);
                data.setText(text);
                data.setStar(star);
                data.setType(type);
                data.setRegdate(regdate);
                tmp.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tmp;
    }
}
