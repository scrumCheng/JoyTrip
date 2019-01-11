package nju.joytrip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import nju.joytrip.R;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mlistUrls;
    private LayoutInflater minflater;

    public GridAdapter(Context context, ArrayList<String> listUrls){
        mContext = context;
        this.mlistUrls = listUrls;
        if(listUrls.size()==10){
            listUrls.remove(listUrls.size()-1);
        }
        minflater =  LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mlistUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return mlistUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = minflater.inflate(R.layout.pic_word_item,parent,false);
            holder.image = convertView.findViewById(R.id.image_view);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        final String path = mlistUrls.get(position);
        if(path.equals("paizhao")){
            holder.image.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(mContext)
                    .asBitmap()
                    .load(path)
                    .apply(bitmapTransform(new CropCircleTransformation()))
                    .into(holder.image);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView image;
    }
}
