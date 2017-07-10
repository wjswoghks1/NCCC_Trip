package com.exam.administrator.nccc_trip;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    EditText keyword;
    ImageView back;
    ImageView search;
    SearchOnClickListener listener;
    TextView searchText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        searchText =(TextView) findViewById(R.id.Search_Text);

        keyword = (EditText) findViewById(R.id.searchKeyword);
        back = (ImageView) findViewById(R.id.backHome);
        search = (ImageView) findViewById(R.id.realSearch);

        listener = new SearchOnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.backHome:
                        SearchActivity.this.finish();
                    case R.id.realSearch:
                        Log.e("ㅇㅇ", "dddd");
                        final String searchQuery = keyword.getText().toString();
                        Log.e("ㅇㅇ", searchQuery);
                        final Handler handler = new Handler(){
                            public void handleMessage(Message msg) {
                                switch (msg.what){
                                    case 1:
                                        String name = (String)msg.obj;
                                        searchText.append(name + "\n");
                                        break;
                                    case 2:
                                        String adress = (String)msg.obj;
                                        searchText.append(adress + "\n");
                                        break;
                                }
                            }
                        };

                        new Thread(new Runnable() { // 반드시 스레드 이용 그래야 반복해서 쓸수 있다고 함
                            @Override
                            public void run() {

                                try {
                                    URL url = new URL("http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=P6bhFFBWwGkij2sSFyuE1fYOhmljx2J0qqEjWC65a0BMXkdVEYQo44MRq0yZK7Txgqbp9GbSWfexAXQhBEwtLg%3D%3D&contentTypeId=12&areaCode=33&sigunguCode=11&cat1=A01&cat2=A0101&cat3=&listYN=Y&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=A&numOfRows=12&pageNo=1&_type=json");
                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                    conn.setDefaultUseCaches(false);
                                    conn.setDoInput(true);
                                    conn.setDoOutput(false);
                                    conn.setRequestMethod("GET");

                                    if (conn != null) {
                                        conn.setConnectTimeout(10000);
                                        conn.setUseCaches(false);

                                        if (conn.getResponseCode() >= 200 || conn.getResponseCode() < 300 ) { //접속 잘 되었는지 안되었는지 파악
                                            Log.i(TAG, "ii1");
                                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); // InputStreamReader로 가져온다음 Buffer에 넣으면 이상하게 에러가 남, 그냥 바로 넣을것.
                                            Log.i(TAG, "ii2");
                                            for(;;){
                                                String buf = "";
                                                buf = br.readLine();
                                                StringBuffer sb = new StringBuffer();
                                                sb.append(buf);
                                                Log.i(TAG, buf);
                                                if (buf == null) {
                                                    Log.i(TAG, "break");
                                                    break;
                                                }
                                                JSONObject result = new JSONObject(buf);
                                                JSONArray results = result.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item"); //가장 큰 테두리부터 갈라갈라 가져오기
                                                Log.i(TAG, "ii4");

                                                for (int i=0;i<results.length(); i++ ){
                                                    JSONObject json = results.getJSONObject(i);
                                                    Log.i(TAG, "ii6");
                                                    String name = json.getString("title");
                                                    String adress = json.getString("addr1");

                                                    if(name.contains(searchQuery))
                                                    {
                                                        handler.sendMessage(Message.obtain(handler, 1, name+"\n"+adress));
                                                    }
                                                    if(adress.contains(searchQuery))
                                                    {
                                                        handler.sendMessage(Message.obtain(handler, 1, name+"\n"+adress));
                                                    }


                                                }

                                            }
                                            br.close();
                                        }
                                        conn.disconnect();
                                    }
                                }
                                catch(Exception e){
                                    Log.e(TAG, "ii7");
                                    Log.w(TAG, e.getMessage());
                                }
                            }
                        }).start();

                }


            }

        };
        back.setOnClickListener(listener);
        search.setOnClickListener(listener);




    }
}
