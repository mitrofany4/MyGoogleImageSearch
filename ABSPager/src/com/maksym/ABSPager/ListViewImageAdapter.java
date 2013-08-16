package com.maksym.ABSPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ListViewImageAdapter extends ArrayAdapter<GoogleImageBean> {

	public ArrayList<GoogleImageBean> listImages;
	private static LayoutInflater inflater=null;
	public ImageLoader imageLoader;
    DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    Context context;


    public ListViewImageAdapter(Context context, int resource, ArrayList<GoogleImageBean> objects) {
        super(context, resource, objects);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();

        this.listImages = objects;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = ImageLoader.getInstance(); // Получили экземпляр
        imageLoader.init(ImageLoaderConfiguration.createDefault(context)); // Проинициализировали конфигом по умолчанию
        this.context=context;
    }

   public int getCount() {
        return listImages.size();
    }

    public GoogleImageBean getItem(int position) {

    	return listImages.get(position);
    }

    public long getItemId(int position) 
    {
    	return position;
    }
    
    public static class ViewHolder{
    	protected ImageView imgViewImage;
        protected TextView txtViewTitle;
        protected CheckBox favCheckBox;
    	
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	View vi=convertView;
    	final ViewHolder holder;
        final int _position=position;
		if(convertView==null){
			vi = inflater.inflate(R.layout.searchlistview_row, null);
			holder=new ViewHolder();
			
			holder.imgViewImage=(ImageView)vi.findViewById(R.id.elImageView);

            holder.imgViewImage
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDialog(listImages.get(_position).getUrl());
                        }
                    });
			holder.txtViewTitle=(TextView)vi.findViewById(R.id.titleTextView);
			holder.favCheckBox=(CheckBox) vi.findViewById(R.id.likeChBox);
            holder.favCheckBox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            GoogleImageBean element = listImages.get(_position);
                            element.setFavorite(buttonView.isChecked());
                            notifyDataSetChanged();

                        }
                    });
			vi.setTag(holder);
		}
		else
			holder=(ViewHolder)vi.getTag();
		
		GoogleImageBean imageBean = (GoogleImageBean) listImages.get(position);
		imageLoader.displayImage(imageBean.getThumbUrl(), holder.imgViewImage, options, animateFirstListener);
		holder.txtViewTitle.setText(Html.fromHtml(imageBean.getTitle()));
        holder.favCheckBox.setChecked(imageBean.getFavorite());
		return vi;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    void showDialog(String image_url) {

        if (getContext() instanceof FragmentActivity) {
            // We can get the fragment manager
            SherlockFragmentActivity activity = (SherlockFragmentActivity) context;
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            DialogFragment newFragment = MyDialogFragment.newInstance(image_url);
            newFragment.show(ft, "dialog");
        }


    }
}