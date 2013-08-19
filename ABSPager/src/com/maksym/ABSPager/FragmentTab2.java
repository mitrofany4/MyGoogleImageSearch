package com.maksym.ABSPager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;


public class FragmentTab2 extends SherlockListFragment {

    DBAdapter db;
    ArrayList<GoogleImageBean> favimages;

    getFavImageTask task;

    public FragmentTab2() {
    }

    @Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab2.xml
		View view = inflater.inflate(R.layout.fragmenttab2, container, false);

		return view;
	}

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//setUserVisibleHint(true);
	}

    public class getFavImageTask extends AsyncTask<Void,Void,Void> {

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
           db = new DBAdapter(getSherlockActivity());
           db.OpentoRead();
           favimages=db.getAllFavorites();
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
            FavListViewAdapter adapter = new FavListViewAdapter(getSherlockActivity(),R.layout.favlistview_row,favimages);
            setListAdapter(adapter);
            db.Close();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            task = new getFavImageTask();
            task.execute();
        }
    }
}
