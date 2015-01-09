package com.mc.madcamp_1_b;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {
	static InputStream js = null;
	static JSONArray jObj = null;
	static String json = "";
	
	public JSONArray getJSONFromUrl(String Url){
		try{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Url);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			js = httpEntity.getContent();
			// 이 과정들을 거치면 결국 InputStream 형태로 데이터를 얻을 수 있는 것이다.
		}catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (org.apache.http.client.ClientProtocolException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
		
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(js,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			//리더를 이용하여 라인을 읽고, 성공적으로 읽어왔으면 StringBuilder에 한 줄씩 넣는다.
			while ((line = reader.readLine())!=null) {
				sb.append(line + "\n");
			}
			js.close();
			json = sb.toString();
		}catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
		
		// 어레이로 만듬.
		 try {
	            jObj = new JSONArray(json);
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }

	        // Return the JSON Object.
	        return jObj;
		
		
	}
	
	public List<HashMap<String,String>> parse(JSONArray jObject){
		
		return getInfos(jObject);
	}
	
	private List<HashMap<String, String>> getInfos(JSONArray column){
		int numofdata = column.length();
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> data = null;
		
		for (int i = 0; i < numofdata; i++) {
			try{
				data = getInfo((JSONObject)column.get(i));
				dataList.add(data);
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
		return dataList;
	}
	
	private HashMap<String, String> getInfo(JSONObject jData){
		
		HashMap<String, String> data = new HashMap<String, String>();
		String state = "";
		String capital = "";
		String latitude = "";
		String longitude = "";
		
		try{
			state = jData.getString("state");
			capital = jData.getString("capital");
			latitude = jData.getString("latitude");
			longitude = jData.getString("longitude");
			data.put("state", state);
			data.put("capital", capital);
			data.put("latitude",latitude);
			data.put("longitude",longitude);
		} catch(JSONException e){
			e.printStackTrace();
		}
		return data;
	}
}
