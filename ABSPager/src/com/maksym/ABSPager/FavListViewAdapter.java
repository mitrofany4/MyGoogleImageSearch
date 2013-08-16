package com.maksym.ABSPager;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mitrofany4 on 8/15/13.
 */
public class FavListViewAdapter extends ArrayAdapter<GoogleImageBean> {
    private static LayoutInflater inflater=null;
    ArrayList<GoogleImageBean> favimages;
    public ImageLoader imageLoader;
    DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();


    public static class ViewHolder{
        protected ImageView imgViewImage;
        protected TextView txtViewTitle;
    }


    public FavListViewAdapter(Context context, int resource, ArrayList<GoogleImageBean> objects) {
        super(context, resource, objects);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();

        this.favimages = objects;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = ImageLoader.getInstance(); // Получили экземпляр
        imageLoader.init(ImageLoaderConfiguration.createDefault(context)); // Проинициализировали конфигом по умолчанию

    }
    public int getCount() {
        return favimages.size();
    }

    public GoogleImageBean getItem(int position) {

        return favimages.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }


        @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        final ViewHolder holder;
        final int _position=position;
        if(convertView==null){
            vi = inflater.inflate(R.layout.favlistview_row, null);
            holder=new ViewHolder();

            holder.imgViewImage=(ImageView)vi.findViewById(R.id.favImageView);
            holder.txtViewTitle=(TextView)vi.findViewById(R.id.titleTextView);

            vi.setTag(holder);
        }
        else
            holder=(ViewHolder)vi.getTag();

        GoogleImageBean imageBean = (GoogleImageBean) favimages.get(position);
        imageLoader.displayImage(imageBean.getThumbUrl(), holder.imgViewImage, options, animateFirstListener);
        holder.txtViewTitle.setText(Html.fromHtml(imageBean.getTitle()));

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
}