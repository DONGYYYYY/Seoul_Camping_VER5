package apiParseing;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import listview.CommentItem;

/* local spring server와 연동 */
public class Parser_Spring extends AsyncTask<CommentItem , Void , Void > { // AsyncTask < "doInBackground 매개변수 타입 " , "onProgressUpdate에 사용할 매개변수  " , "onPostExecute()에 사용할 매개변수 (doInBackground의 리턴 타입) " >

    String str = "http://192.168.0.16:8080/comment/addList";
    DataInputStream dis = null;
    DataOutputStream dout = null;

    @Override
    protected Void doInBackground(CommentItem... items) { // items는 execute()의 매개변수 인자값
        try { // HttpURLConnection에 필요한 try catch
            URL url = new URL(str);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // 해당 url로 연결

            urlConnection.setConnectTimeout(1000);
            urlConnection.setReadTimeout(1000);

            urlConnection.setRequestMethod("POST"); // post방식으로 설정
            urlConnection.setRequestProperty("Content-type", "application/json"); // 보내는 형식을 json형식으로 설정
            urlConnection.setDoOutput(true); // output을 true로 설정

            /* post로 RequestBody를 json형태로 보내기 때문에 json형식의 object로 생성하여 write */
            JSONObject obj = new JSONObject();
            obj.put("id", items[0].getId());
            obj.put("text", items[0].getText());
            obj.put("star", items[0].getStar());
            obj.put("type",items[0].getType());

            dout = new DataOutputStream(urlConnection.getOutputStream());
            dout.write(obj.toString().getBytes()); // 해당 서버로 전송
            dout.flush();

            /* 응답유무 확인 */
            if( urlConnection.getResponseCode()== HttpURLConnection.HTTP_OK) {
                dis= new DataInputStream(urlConnection.getInputStream());
                Log.i("응답 : " , Integer.toString(urlConnection.getResponseCode()));
            }
            else
            {
                Log.i("응답 : " , Integer.toString(urlConnection.getResponseCode()));
            }
            dout.close(); // outPutStream close
            urlConnection.disconnect(); // url 연결 해제

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