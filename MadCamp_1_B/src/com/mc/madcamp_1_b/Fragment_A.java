package com.mc.madcamp_1_b;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Fragment_A extends Fragment {

	String LINK = "http://www.gosolkit.com/SampleJSON.php";
	List<HashMap<String, String>> temp = null;
	private Toast toast = null;

	public Fragment_A() {
		// TODO Auto-generated constructor stub
	}

	void showToast(String s) {
		toast.setText(s);
		toast.show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_a, container, false);
		ListViewLoaderTask listViewLoaderTask = new ListViewLoaderTask();
		listViewLoaderTask.execute(LINK);
		return v;
	}

	private class ListViewLoaderTask extends
			AsyncTask<String, Void, SimpleAdapter> {
		JSONArray jArray;

		@Override
		protected SimpleAdapter doInBackground(String... params) {
			try {
				// 여기서 LINK를 바탕으로 jsonarray를 만듬.
				JSONParser jsonParser = new JSONParser();
				jArray = jsonParser.getJSONFromUrl(LINK);
			} catch (Exception e) {
				Log.d("JSON Exception1", e.toString());
			}

			JSONParser jsonParser = new JSONParser();
			List<HashMap<String, String>> states = null;

			// pares하면 결국 state에 data들이 들어가는게 된다.
			try {
				states = jsonParser.parse(jArray);
			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}

			temp = states; // Click에서 사용하기 위해 data를 저장해둠. (개선 가능)
			String[] keys = { "state", "capital" };
			int[] ids = { R.id.state_name, R.id.capital_name };

			SimpleAdapter adapter = new SimpleAdapter(getActivity(), states, R.layout.lv_layout, keys, ids);
			// SimpleAdapter(context, data, layout, from, to)
			// : context 위에 data에서 from에 있는 key들을 뽑아와 to의 적재적소에 넣어준다. 단, to에서
			// 정의된 것들이 layout에 있어야함.
			return adapter;
		}

		@Override
		protected void onPostExecute(SimpleAdapter adapter) {
			// TODO Auto-generated method stub
			ListView listView = (ListView) getView().findViewById(R.id.states);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(listOnClickListener);
		}

		private ListView.OnItemClickListener listOnClickListener = new ListView.OnItemClickListener() {
			public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
				String text = "Latitude : "
						+ temp.get(position).get("latitude") + "\nLongitude : "
						+ temp.get(position).get("longitude");
				if (toast == null) {
					toast = Toast.makeText(getActivity().getBaseContext(),
							text, Toast.LENGTH_SHORT);
				}
				showToast(text);
			}
		};

	}
}
