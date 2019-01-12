package nju.joytrip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.foamtrace.photopicker.Image;
import com.foamtrace.photopicker.ImageGridAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImgGridAdapter extends ImageGridAdapter {
    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;

    private Context mContext;

    private LayoutInflater mInflater;
    private boolean showCamera = true;
    private boolean showSelectIndicator = true;

    private List<Image> mImages = new ArrayList<>();
    private List<Image> mSelectedImages = new ArrayList<>();

    private int mItemSize;
    private GridView.LayoutParams mItemLayoutParams;
    private RoundedCorners mroundedCorners = new RoundedCorners(6);
    //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
    private RequestOptions moptions = RequestOptions.bitmapTransform(mroundedCorners).override(300, 300);

    public ImgGridAdapter(Context context, boolean showCamera, int itemSize) {
        super(context, showCamera, itemSize);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        int type = getItemViewType(i);
        if (type == TYPE_CAMERA) {
            view = mInflater.inflate(com.foamtrace.photopicker.R.layout.item_camera, viewGroup, false);
            view.setTag(null);
        } else if (type == TYPE_NORMAL) {
            ViewHold holde;
            if (view == null) {
                view = mInflater.inflate(com.foamtrace.photopicker.R.layout.item_select_image, viewGroup, false);
                holde = new ViewHold(view);
            } else {
                holde = (ViewHold) view.getTag();
                if (holde == null) {
                    view = mInflater.inflate(com.foamtrace.photopicker.R.layout.item_select_image, viewGroup, false);
                    holde = new ViewHold(view);
                }
            }
            if (holde != null) {
                holde.bindData(getItem(i));
            }
            GridView.LayoutParams lp = (GridView.LayoutParams) view.getLayoutParams();
            if (lp.height != mItemSize) {
                view.setLayoutParams(mItemLayoutParams);
            }
        }
        return view;
    }
    class ViewHold {
        ImageView image;
        ImageView indicator;
        View mask;

        ViewHold(View view) {
            image = (ImageView) view.findViewById(com.foamtrace.photopicker.R.id.image);
            indicator = (ImageView) view.findViewById(com.foamtrace.photopicker.R.id.checkmark);
            mask = view.findViewById(com.foamtrace.photopicker.R.id.mask);
            view.setTag(this);
        }

        void bindData(final Image data) {
            if (data == null) return;
            // 处理单选和多选状态
            if (showSelectIndicator) {
                indicator.setVisibility(View.VISIBLE);
                if (mSelectedImages.contains(data)) {
                    // 设置选中状态
                    indicator.setImageResource(com.foamtrace.photopicker.R.mipmap.btn_selected);
                    mask.setVisibility(View.VISIBLE);
                } else {
                    // 未选择
                    indicator.setImageResource(com.foamtrace.photopicker.R.mipmap.btn_unselected);
                    mask.setVisibility(View.GONE);
                }
            } else {
                indicator.setVisibility(View.GONE);
            }
            File imageFile = new File(data.path);

            if (mItemSize > 0) {
                // 显示图片
                Glide.with(mContext).load(imageFile).apply(moptions).into(image);
            }
        }
    }

}
