package com.exam.administrator.nccc_trip;

        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.ArrayList;

        import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017-07-04.
 */

public class TripFragment extends Fragment {
    String apiKey = "P6bhFFBWwGkij2sSFyuE1fYOhmljx2J0qqEjWC65a0BMXkdVEYQo44MRq0yZK7Txgqbp9GbSWfexAXQhBEwtLg%3D%3D";
    String name1= null;
    String name2 = null;
    public TripFragment(){
    }


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList items;
    String name;
    String adress;
    String imgUrl;
    Bitmap bmimg;
    Handler handler;
    RecyclerView recyclerView2;
    RecyclerView.Adapter adapter2;
    RecyclerView.LayoutManager layoutManager2;
    ArrayList items2;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_trip, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_best);
        items = new ArrayList();
        layoutManager = new LinearLayoutManager(view.getContext());

        adapter = new TourAdapter(items, view.getContext());
        recyclerView.setAdapter(adapter);


        recyclerView.setLayoutManager(layoutManager);


        Thread t = new Thread(new Runnable() { // 반드시 스레드 이용 그래야 반복해서 쓸수 있다고 함
            @Override
            public void run() {

            try {
                URL url = new URL("http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey="+apiKey+"&contentTypeId=12&areaCode=33&sigunguCode=11&cat1=A01&cat2=A0101&cat3=&listYN=Y&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=A&numOfRows=12&pageNo=1&_type=json");
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

                            for (int i=0;i<results.length(); i++){

                                try{
                                    JSONObject json = results.getJSONObject(i);
                                    Log.i(TAG, "ii6");
                                    name = json.getString("title");
                                    adress = json.getString("addr1");
                                    Log.e("d33333fd",name);
                                    Log.e("33333dfd", adress);
                                    imgUrl = json.getString("firstimage");
                                    URL imgurl = new URL(imgUrl);
                                    HttpURLConnection imgConn = (HttpURLConnection) imgurl.openConnection();
                                    imgConn.setDoInput(true);
                                    imgConn.connect();

                                    InputStream is = imgConn.getInputStream();
                                    bmimg = BitmapFactory.decodeStream(is);
                                    items.add(new TourItem(name, adress, "123.4" + "km", bmimg, R.drawable.first_medal));




                                }
                                catch (Exception ee){
                                    JSONObject json = results.getJSONObject(i);
                                    Log.i(TAG, "NO image");
                                    name = json.getString("title");
                                    adress = json.getString("addr1");
                                    items.add(new TourItem(name, adress, "123.4" + "km", null, R.drawable.first_medal));


                                }


                            }

                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            }
            catch(Exception e){
                try{

                }
                catch (Exception ee){

                }
                Log.e(TAG, "ii7");
                Log.w(TAG, e.getMessage());
            }

            }
        });
        t.start();
        try{
            t.join();
            Log.i("eeeeeeeeeeeeee",items.get(0).toString());
        }catch (Exception ee){
            ee.printStackTrace();
        }


        return view;
    }


}
