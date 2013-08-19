package com.maksym.ABSPager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.commonsware.cwac.endless.EndlessAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by mitrofany4 on 8/12/13.
 */
public class LazyAdapter extends EndlessAdapter {

    private RotateAnimation rotate=null;
    private View pendingView=null;
    private ArrayList<GoogleImageBean> tempList = new ArrayList<GoogleImageBean>();
    private ArrayList<GoogleImageBean> dlist;
    private int mLastOffset = 0;
    static final int BATCH_SIZE = 8;
    String strSearch;
    JSONObject json;




    LazyAdapter(Activity activity, ArrayList<GoogleImageBean> list, String srtSearch) {
        super(new ListViewImageAdapter(activity, R.layout.searchlistview_row,list));
        strSearch = srtSearch;
        rotate=new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(600);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);
        dlist=list;
    }

    @Override
    protected boolean cacheInBackground() throws Exception {
        tempList.clear();
        int lastOffset = getLastOffset();

        getJSON(lastOffset);
        try {
            JSONObject responseObject = json.getJSONObject("responseData");
            JSONArray resultArray = responseObject.getJSONArray("results");

            tempList = getImageList(resultArray);

            System.out.println("Result array length => " + resultArray.length());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        setLastOffset(lastOffset+BATCH_SIZE);

        if (tempList.size()==0) return false;
        else return true;
    }

    private ArrayList<GoogleImageBean> getImageList(JSONArray resultArray) {

        ArrayList<GoogleImageBean> listImages = new ArrayList<GoogleImageBean>();
        GoogleImageBean bean;

        try
        {
            for(int i=0; i<resultArray.length(); i++)
            {
                JSONObject obj;
                obj = resultArray.getJSONObject(i);
                bean = new GoogleImageBean();

                bean.setTitle(obj.getString("title"));
                bean.setThumbUrl(obj.getString("tbUrl"));
                bean.setUrl(obj.getString("url"));
                System.out.println("Thumb URL => "+obj.getString("tbUrl"));

                listImages.add(bean);

            }
            return listImages;
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void appendCachedData() {
        @SuppressWarnings("unchecked")
        ListViewImageAdapter arrAdapterNew = (ListViewImageAdapter)getWrappedAdapter();

        int listLen = tempList.size();
        for(int i=0; i<listLen; i++){
            arrAdapterNew.add(tempList.get(i));
        }
    }


   @Override
    protected View getPendingView(ViewGroup parent) {
       View row=LayoutInflater.from(parent.getContext()).inflate(R.layout.row, null);

       pendingView=row.findViewById(android.R.id.text1);
       pendingView.setVisibility(View.VISIBLE);
       pendingView=row.findViewById(R.id.throbber);
       pendingView.setVisibility(View.VISIBLE);
       startProgressAnimation();
        return(row);
    }


    void startProgressAnimation() {
        if (pendingView!=null) {
            pendingView.startAnimation(rotate);
        }
    }

    private void getJSON(int start){
        String google = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
        String perpage = "&rsz=8";
        String count = "&start="+String.valueOf(start);
        String charset = "UTF-8";
        URL url;
        try {
            url = new URL(google+ URLEncoder.encode(strSearch, charset)+perpage+count);
            URLConnection connection = url.openConnection();
            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }

            System.out.println("Builder string => "+builder.toString());

            json = new JSONObject(builder.toString());
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void setLastOffset(int i) {
        mLastOffset = i;
    }

    private int getLastOffset(){
        return mLastOffset;
    }

}


