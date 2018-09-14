package apiParseing;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import model.CampData;

public class JsonParser extends AsyncTask<Void, Void, Map<String, CampData>> {
    String str, recieveMsg;

    Map<String, CampData> datamap = new HashMap<String, CampData>();
    String serviceKey = "79495a5372736b79333670797a6867"; // 서비스키

    protected Map<String, CampData> doInBackground(Void... voids) {
        String xmlurl = "http://openapi.seoul.go.kr:8088/"+serviceKey+"/json/Mgiscampinginfoseoul/1/20";
        URL url = null;
        try {
            url = new URL(xmlurl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader temp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(temp);
                StringBuffer buffer = new StringBuffer();

                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                recieveMsg = buffer.toString();
                datamap = jsonparse(recieveMsg);
                reader.close();
            }
        } catch (Exception e) {

        }
        return datamap;
    }

    public Map<String, CampData> jsonparse(String jsonString) {

        CampData data;
        Map<String, CampData> tmp = new HashMap<String, CampData>();
        String COT_ADDR_FULL_NEW, COT_ADDR_FULL_OLD, COT_COORD_X, COT_COORD_Y, COT_CONTS_NAME, COT_TEL_NO, COT_VALUE_01, COT_VALUE_02, Homepageurl, COT_VALUE_04, reservurl, COT_CONTS_ID;

        try {
            JSONArray jarray = new JSONObject(jsonString).getJSONObject("Mgiscampinginfoseoul").getJSONArray("row");
            for (int i = 0; i < jarray.length(); i++) {
                data = new CampData();
                JSONObject jObject = jarray.getJSONObject(i);

                COT_CONTS_ID = jObject.optString("COT_CONTS_ID");//캠핑장id
                COT_ADDR_FULL_NEW = jObject.optString("COT_ADDR_FULL_NEW");//신주소
                COT_ADDR_FULL_OLD = jObject.optString("COT_ADDR_FULL_OLD");//구주소
                COT_COORD_X = jObject.optString("COT_COORD_X");//x좌표
                COT_COORD_Y = jObject.optString("COT_COORD_Y");//y좌표
                COT_CONTS_NAME = jObject.optString("COT_CONTS_NAME");//캠핑장이름
                COT_TEL_NO = jObject.optString("COT_TEL_NO");//전화번호
                COT_VALUE_01 = jObject.optString("COT_VALUE_01");//상세정보1
                COT_VALUE_02 = jObject.optString("COT_VALUE_02");//상세정보2
                Homepageurl = jObject.optString("COT_VALUE_03");//홈페이지 url
                COT_VALUE_04 = jObject.optString("COT_VALUE_04");//상세정보3
                reservurl = jObject.optString("COT_VALUE_05");//예약url

                if (COT_ADDR_FULL_NEW.isEmpty()) { // 신주소가 없을 경우
                    data.setAdd(COT_ADDR_FULL_OLD); // 구주소로 설정
                } else {
                    data.setAdd(COT_ADDR_FULL_NEW);//주소
                }

                data.setId(COT_CONTS_ID);
                data.setX(Double.parseDouble(COT_COORD_X));
                data.setY(Double.parseDouble(COT_COORD_Y));
                data.setName(COT_CONTS_NAME);
                data.setTel(COT_TEL_NO);
                data.setValue1(COT_VALUE_01);
                data.setValue2(COT_VALUE_02);
                data.setHomepageurl(Homepageurl);
                data.setValue3(COT_VALUE_04);
                data.setResurl(reservurl);

                tmp.put(COT_CONTS_ID, data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tmp;
    }
}
