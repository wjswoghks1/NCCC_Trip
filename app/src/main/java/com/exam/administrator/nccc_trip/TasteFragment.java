package com.exam.administrator.nccc_trip;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.R.attr.radius;


public class TasteFragment extends Fragment {

    private List<Item> ItemList;
    int page = 1;
    int radius = 10000;
    String apikey = "a0822a15d67056c8c62651bc3ebb13c2";
    public TasteFragment(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_taste, container, false);


        GpsInfo gps = new GpsInfo(getActivity());
        double lat = gps.getLatitude();
        double lon = gps.getLongitude();

        ItemList = new ArrayList<>();

        String query = "짜장면";
        OldSearcher searcher = new OldSearcher();
        searcher.searchKeyword(getActivity().getApplicationContext(), query, lat, lon, radius, page, 2, apikey, new OnFinishSearchListener() {
            @Override
            public void onSuccess(List<Item> itemList) {
                showResult(itemList);
            }

            @Override
            public void onFail() {

            }
        });
        return view;
    }

    private void showResult(List<Item> itemList){

        for(int i = 0; i<itemList.size(); i++){
            Item item = itemList.get(i);
            String iuu = String.valueOf(item.category);
            Log.e("ddd", iuu);
            String[] arr = iuu.split(" > ");
            Log.e("ddd", arr[1]);

            Log.e("ddfsd", "dddd" + item.latitude);
            Log.e("ddfsd", "dddd" + item.longitude);
            ItemList.add(itemList.get(i));

        }


    }
}
