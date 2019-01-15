package nju.joytrip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import nju.joytrip.R;
import nju.joytrip.customview.MyGridView;
import nju.joytrip.entity.PWShare;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ShareAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<PWShare> mDatas;
    private int mLayoutRes;
    private Context mContext;

    public ShareAdapter(Context context, int layoutRes, List<PWShare> datas) {
        this.mContext=context;
        this.mDatas = datas;
        this.mLayoutRes = layoutRes;
        mInflater = LayoutInflater.from(context);
    }

    public LayoutInflater getmInflater() {
        return mInflater;
    }

    public void setmInflater(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }

    public List<PWShare> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<PWShare> mDatas) {
        this.mDatas = mDatas;
    }

    public int getmLayoutRes() {
        return mLayoutRes;
    }

    public void setmLayoutRes(int mLayoutRes) {
        this.mLayoutRes = mLayoutRes;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = mInflater.inflate(mLayoutRes,null);
            holder = new ViewHolder();
            holder.write_photo = (ImageView)convertView.findViewById(R.id.write_photo);
            holder.write_name = (TextView) convertView.findViewById(R.id.write_name);
            holder.write_date = (TextView) convertView.findViewById(R.id.write_date);
            holder.dynamic_text = (TextView) convertView.findViewById(R.id.dynamic_text);
            holder.dynamic_photo = (MyGridView) convertView.findViewById(R.id.dynamic_photo);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        PWShare shareItem = mDatas.get(position);
        final ViewHolder viewHolder = holder;
        Glide.with(mContext)
                .asBitmap()
                .load(shareItem.getUser().getUserPic())
                .apply(bitmapTransform(new CropCircleTransformation()))
                .into(viewHolder.write_photo);
        String username = shareItem.getUser().getUsername();//用户名
        String nickname = shareItem.getUser().getNickname();
        if (nickname == null){
            nickname = username;
        }
        viewHolder.write_name.setText(nickname);
        viewHolder.write_date.setText(shareItem.getCreatedAt());//时间
        viewHolder.dynamic_text.setText(shareItem.getContent());//内容
        viewHolder.dynamic_photo.setNumColumns(3);
        viewHolder.dynamic_photo.setAdapter(new GridAdapter(mContext,shareItem.getPhotoList()));

        return convertView;
    }


    private final class ViewHolder {
        ImageView write_photo;
        TextView write_name;
        TextView write_date;
        TextView dynamic_text;
        MyGridView dynamic_photo;

    }
}
