package com.exam.administrator.nccc_trip;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.WidgetContainer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CalendarActivity extends AppCompatActivity {

    String insertUrl = "http://222.116.135.79:8080/nccc_t/scheduleInsert.jsp";
    String checkUrl = "http://222.116.135.79:8080/nccc_t/checkSchedule.jsp";
    String deleteUrl = "http://222.116.135.79:8080/nccc_t/deleteSchedule.jsp";
    String calendarUrl = "http://222.116.135.79:8080/nccc_t/getSchedule.jsp";


    MaterialCalendarView tourCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        tourCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);


        OneDayDecorator oneDayDecorator = new OneDayDecorator();

        tourCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2000, 1, 1))
                .setMaximumDate(CalendarDay.from(2100, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        tourCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            private final Calendar calendar = Calendar.getInstance();
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Intent i = new Intent(CalendarActivity.this, ScheduleActivity.class);
                date.copyTo(calendar);
                int weekYear = calendar.get(Calendar.YEAR);
                int weekMonth = calendar.get(Calendar.MONTH) + 1;
                int weekDay = calendar.get(Calendar.DATE);
                Log.e("&&&&", ""+weekYear+weekMonth+weekDay);
                i.putExtra("year", weekYear);
                i.putExtra("month", weekMonth);
                i.putExtra("day", weekDay);
                startActivity(i);
            }
        });

        tourCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);





        //final Dbload insertScehdule = new Dbload();
        final String id_client = getDeviceId();

        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        try {
                            String highlightDay = (String) msg.obj;
                            //String fdfd1 = insertScehdule.execute(id_client, title, "2017", "07", "13", "06").get();
                            Log.e("&&&&", highlightDay);
                            String[] arr = highlightDay.split("/");
                            int year  = Integer.parseInt(arr[0]);
                            int month  = Integer.parseInt(arr[1]);
                            int day  = Integer.parseInt(arr[2]);
                            int time  = Integer.parseInt(arr[3]);

                            tourCalendarView.addDecorator(new tourDecorator(year, month, day));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        String adress = (String) msg.obj;

                        break;
                }
            }
        };




        Thread calendarThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sendMsg;
                    URL url = new URL(calendarUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestMethod("POST");
                    OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                    sendMsg = "id_client=" + id_client;  // 일정에서 삭제할때는 이것만 필요

                    osw.write(sendMsg);
                    osw.flush();
                    Log.e("##i1", "dddddddddd");
                    try {
                        Log.e("##i2", "dddddddddd");
                        if (conn.getResponseCode() >= 200 || conn.getResponseCode() < 300) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                            StringBuffer buf = new StringBuffer();
                            String str= "";
                            while ((str = reader.readLine()) != null) {
                                buf.append(str);
                            }


                            try {
                                Log.e("##i3", "dddddddddd");
                                JSONObject json = new JSONObject(buf.toString());
                                JSONArray jj = json.getJSONArray("List");


                                for (int i = 0; i < jj.length(); i++) {
                                    JSONObject scheduleJson = jj.getJSONObject(i);
                                    String yo = scheduleJson.getString("year");
                                    String mo = scheduleJson.getString("month");
                                    String doo = scheduleJson.getString("day");
                                    String to = scheduleJson.getString("time");


                                    String dayDay = yo + "/" + mo + "/" + doo +"/" + to;
                                    handler.sendMessage(Message.obtain(handler, 1, dayDay));

                                }


                            } catch (Exception e) {
                            }
                            reader.close();

                        }
                        conn.disconnect();

                        Log.e("##i10", "dddddddddd");
                    }
                    catch (Exception exxx) {
                        Log.e("##i11", "dddddddddd");
                        exxx.printStackTrace();
                    }
                } catch (Exception e) {
                    Log.e("##i12", "dddddddddd");
                }

            }
        });

        calendarThread.start();



    }
    public String getDeviceId(){
        String idByANDROID_ID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        return idByANDROID_ID;
    }



    public class tourDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        int ccyear;
        int ccmonth;
        int ccday;
        public tourDecorator(int cyear, int cmonth, int cday) {
            ccyear = cyear;
            ccmonth = cmonth - 1;
            ccday = cday;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekYear = calendar.get(Calendar.YEAR);
            int weekMonth = calendar.get(Calendar.MONTH);
            int weekDay = calendar.get(Calendar.DATE);
            return (weekYear == ccyear && weekMonth == ccmonth && weekDay == ccday);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.GREEN));

        }
    }

    public class SundayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SundayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    public class SaturdayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SaturdayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));

        }
    }

    public class OneDayDecorator implements DayViewDecorator {

        private CalendarDay date;

        public OneDayDecorator() {
            date = CalendarDay.today();
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return date != null && day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(1.4f));
            view.addSpan(new ForegroundColorSpan(Color.GREEN));
        }

        /**
         * We're changing the internals, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
         */
        public void setDate(Date date) {
            this.date = CalendarDay.from(date);
        }
    }




}

 /*    Thread idThread = new Thread(new Runnable() { // 반드시 스레드 이용 그래야 반복해서 쓸수 있다고 함
            @Override
            public void run() {

                try {
                    URL url = new URL("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?ServiceKey=P6bhFFBWwGkij2sSFyuE1fYOhmljx2J0qqEjWC65a0BMXkdVEYQo44MRq0yZK7Txgqbp9GbSWfexAXQhBEwtLg%3D%3D&contentTypeId=12&contentId=126006&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&transGuideYN=Y&_type=json");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDefaultUseCaches(false);
                    conn.setDoInput(true);
                    conn.setDoOutput(false);
                    conn.setRequestMethod("GET");

                    if (conn != null) {
                        conn.setConnectTimeout(10000);
                        conn.setUseCaches(false);

                        if (conn.getResponseCode() >= 200 || conn.getResponseCode() < 300) { //접속 잘 되었는지 안되었는지 파악
                            Log.i(TAG, "ii1");
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); // InputStreamReader로 가져온다음 Buffer에 넣으면 이상하게 에러가 남, 그냥 바로 넣을것.
                            Log.i(TAG, "ii2");
                            for (; ; ) {
                                String buf = "";
                                buf = br.readLine();
                                Log.i(TAG, buf);
                                if (buf == null) {
                                    Log.i(TAG, "break");
                                    break;
                                }
                                JSONObject result = new JSONObject(buf);
                                JSONObject results = result.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONObject("item"); //가장 큰 테두리부터 갈라갈라 가져오기
                                Log.i(TAG, "ii4");

                                String title = results.getString("title");
                                Log.e("aaaaaaaaaaaaaaaaaaa", title);

                                handler.sendMessage(Message.obtain(handler, 1, title));


                            }
                            br.close();
                        }
                        conn.disconnect();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "ii7");
                    Log.w(TAG, e.getMessage());
                }
            }
        });
        idThread.start();
        */

    /* class Dbload extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL(checkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id_client=" + strings[0] + "&title=" + strings[1]; // 제목만 있어도 찾을수 있음

                osw.write(sendMsg);
                osw.flush();

                if (conn.getResponseCode() >= 200 || conn.getResponseCode() < 300) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    StringBuffer buffer = new StringBuffer();
                    Log.e("dd", "dddddddddd");
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    String checkStr = buffer.toString();
                    Log.e("checkckeck", checkStr);
                    reader.close();
                    conn.disconnect();
                    if (checkStr.equals("N")) {
                        Log.e("checkckeck", "dddddddddd");
                        url = new URL(insertUrl);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        conn.setRequestMethod("POST");
                        osw = new OutputStreamWriter(conn.getOutputStream());
                        sendMsg = "id_client=" + strings[0] + "&title=" + strings[1] + "&year=" + strings[2] + "&month=" + strings[3] + "&day=" + strings[4] + "&time=" + strings[5]; // 추가해야해서 모든 내용 있어야함

                        osw.write(sendMsg);
                        osw.flush();


                    } else {
                        Log.e("checkckeck", "YYYYYYYYYYYY");
                        url = new URL(deleteUrl);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        conn.setRequestMethod("POST");
                        osw = new OutputStreamWriter(conn.getOutputStream());
                        sendMsg = "id_client=" + strings[0] + "&title=" + strings[1]; // 일정에서 삭제할때는 이것만 필요

                        osw.write(sendMsg);
                        osw.flush();
                    }
                    try {
                        if (conn.getResponseCode() >= 200 || conn.getResponseCode() < 300) {
                            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                            buffer = new StringBuffer();
                            Log.e("dd", "dddddddddd");
                            while ((str = reader.readLine()) != null) {
                                buffer.append(str);
                            }

                            receiveMsg = buffer.toString();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    reader.close();
                    conn.disconnect();


                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }


    }
    */






