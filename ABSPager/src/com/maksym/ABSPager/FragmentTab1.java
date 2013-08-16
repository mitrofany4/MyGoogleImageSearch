package com.maksym.ABSPager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class FragmentTab1 extends SherlockFragment {

    private ArrayList<GoogleImageBean> listImages = new ArrayList<GoogleImageBean>();
    private EditText txtSearchText;
    private Button searchbtn, pushbtn;
    private ListView imageslistview;
    Activity activity;
    String strSearch = null;
    private int number = 1;
    private getImageTask task;
    public DBAdapter db;

    public FragmentTab1() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        activity = getSherlockActivity();

    }

    @Override
	public SherlockFragmentActivity getSherlockActivity() {
		return super.getSherlockActivity();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

    private View.OnClickListener searchbtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            strSearch = txtSearchText.getText().toString();
            strSearch = Uri.encode(strSearch);

            System.out.println("Search string => "+strSearch);
            listImages.clear();
            task = new getImageTask();
            task.execute();
        }
    };

    private View.OnClickListener pushbtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            pushToDB(listImages);
        }
    };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab1.xml
		View view = inflater.inflate(R.layout.fragmenttab1, container, false);

        txtSearchText = (EditText) view.findViewById(R.id.txtViewSearch);
        searchbtn = (Button) view.findViewById(R.id.btnSearch);
        searchbtn.setOnClickListener(searchbtnOnClickListener);
        pushbtn = (Button) view.findViewById(R.id.push_btn);
        pushbtn.setOnClickListener(pushbtnOnClickListener);
        imageslistview = (ListView) view.findViewById(R.id.imagesLV);
        return view;

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setUserVisibleHint(true);
	}

    public class getImageTask extends AsyncTask<Void,Void,Void> {
        JSONObject json;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            dialog = ProgressDialog.show(getActivity(), "", "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String google = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
            String perpage = "&rsz=8";
            String count = "&start="+String.valueOf(number);
            String charset = "UTF-8";
            URL url;
            try {
                url = new URL(google+ URLEncoder.encode(strSearch,charset)+perpage+count);
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if(dialog.isShowing())
            {
                dialog.dismiss();
            }

            try {
                JSONObject responseObject = json.getJSONObject("responseData");
                JSONArray resultArray = responseObject.getJSONArray("results");

                listImages.addAll(getImageList(resultArray));
                displayList(listImages);
                System.out.println("Result array length => "+resultArray.length());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public ArrayList<GoogleImageBean> getImageList(JSONArray resultArray)
    {
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

    private void displayList(ArrayList<GoogleImageBean> images) {
        imageslistview.setAdapter(new LazyAdapter(getActivity(),images,strSearch));
    }

    public void pushToDB(ArrayList<GoogleImageBean> images){
        db = new DBAdapter(getSherlockActivity());
        db.OpentoWrite();

        for (GoogleImageBean im : images){
            db.createImageBean(im);
        }

        db.Close();


    }


    void showDialog(String image_url) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = MyDialogFragment.newInstance(image_url);
        newFragment.show(ft, "dialog");
    }


}
