package com.exam.administrator.nccc_trip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class NCCCtripSplash extends AppCompatActivity {
    public final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    public final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 2;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nccctrip_splash);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        getPermissionGPS();

        try{
            getPermissionId();
        }
        catch (Exception e){}
        finally{
            String myDeviceId = getDeviceId();
            Log.e(TAG, "iiiiiiiiiiiii        " + myDeviceId);

            try {
                UserCheck userCheck = new UserCheck();
                String noob = userCheck.execute(myDeviceId).get();
                Log.e("df", noob);
                if(noob.equals("N")) {
                    Dbload dbload = new Dbload();
                    String result = dbload.execute(myDeviceId, "20", "2", "1").get();
                    Log.i(TAG, "iiiiiiiiiiiii  " + result);
                }
                else{}
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }


        this.finish();
    }



    public void getPermissionGPS(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        }
        else{

        }
    }
    public void getPermissionId()
    {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);


        }
        else{

        }

    }
    public String getDeviceId(){
        String idByANDROID_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        return idByANDROID_ID;
    }

    public void onRequestPermissionResult(int requestCode, String permission[], int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                break;
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                break;
            default:
                break;

        }
    }
    class Dbload extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        protected String doInBackground(String... strings){
            try{
                String str;
                URL url = new URL("http://222.116.135.79:8080/nccc_t/connectDb.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id=" + strings[0]+ "&age=" + strings[1] + "&group=" +  strings[2] + "&sex=" + strings[3];

                osw.write(sendMsg);
                osw.flush();

                if(conn.getResponseCode() >= 200 || conn.getResponseCode() < 300){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    StringBuffer buffer = new StringBuffer();
                    Log.e("dd", "dddddddddd");
                    while((str = reader.readLine()) != null ){
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                }
                else{
                    Log.i("통신 결과", conn.getResponseCode() + "에러");

                }
            }
            catch (MalformedURLException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }

            return receiveMsg;
        }
    }

    class UserCheck extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        protected String doInBackground(String... strings){
            try{
                String str;
                URL url = new URL("http://222.116.135.79:8080/nccc_t/checkUser.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id=" + strings[0];

                osw.write(sendMsg);
                osw.flush();

                if(conn.getResponseCode() >= 200 || conn.getResponseCode() < 300){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    StringBuffer buffer = new StringBuffer();
                    Log.e("dd", "yesyesyes");
                    while((str = reader.readLine()) != null ){
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                }
                else{
                    Log.i("통신 결과", conn.getResponseCode() + "에러");

                }
            }
            catch (MalformedURLException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }



}