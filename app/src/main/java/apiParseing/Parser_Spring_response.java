package apiParseing;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import listview.CommentItem;

/* local spring server와 연동 */
public class Parser_Spring_response extends AsyncTask<String , Void , ArrayList<CommentItem>> {

        String str = "http://192.168.0.16:8080/comment/listAll";
        OutputStream os = null;
        DataInputStream dis = null;
        String  recieveMsg;

         ArrayList<CommentItem> datamap = new ArrayList<>();

        @Override
        protected  ArrayList<CommentItem> doInBackground(String ... types) {
        try {
            //GET형식이기 때문에 RequestParams에 해당하는 부분 url에 추가
            str = str+"?type="+types[0];
            URL url = new URL(str);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // 연결

            urlConnection.setConnectTimeout(1000);
            urlConnection.setReadTimeout(1000);
            /* GET방식 사용 */
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");//받아오는 형식을 json으로 설정
            urlConnection.setDoInput(true); // input true

            InputStreamReader temp = new InputStreamReader(urlConnection.getInputStream(), "UTF-8"); // 받아오는 역할만 하기 때문에 inputStreamReader만 사용
            BufferedReader reader = new BufferedReader(temp);
            StringBuffer buffer = new StringBuffer();

            while ((str = reader.readLine()) != null) { // 끝까지 읽음
                buffer.append(str);
            }

            recieveMsg = buffer.toString();
            datamap = jsonparse(recieveMsg);

            reader.close();
            if( urlConnection.getResponseCode()== HttpURLConnection.HTTP_OK) { // 연결이 정상적으로 작동했는지 확인
                dis= new DataInputStream(urlConnection.getInputStream());
                Log.i("응답 : " , Integer.toString(urlConnection.getResponseCode()));
            }
            else
            {
                Log.i("응답 : " , Integer.toString(urlConnection.getResponseCode()));
            }
            urlConnection.disconnect(); // 연결해제

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return datamap;
    }

    public ArrayList<CommentItem> jsonparse(String jsonString) { //받아온 json객체에 대해서 원하는 값을 matching하기 위한 메소드

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

                number = Integer.parseInt(jObject.optString("number")); // 숫자(auto_increment)
                id = jObject.optString("id");// 아이디(mac)
                text = jObject.optString("text");// comment
                star = Float.parseFloat(jObject.optString("star"));// 별점
                regdate = jObject.optString("regdate");// 시간
                type = jObject.optString("type"); // 캠핑장 정보 (캠핑장 id)

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
