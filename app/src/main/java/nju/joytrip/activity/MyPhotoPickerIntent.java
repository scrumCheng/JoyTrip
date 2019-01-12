package nju.joytrip.activity;

import android.content.Context;
import android.content.Intent;

import com.foamtrace.photopicker.ImageConfig;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import nju.joytrip.activity.MyPhotoPickerActivity;

import java.util.ArrayList;

public class MyPhotoPickerIntent extends Intent {

    public MyPhotoPickerIntent(Context packageContext) {
        super(packageContext,nju.joytrip.activity.MyPhotoPickerActivity.this);
    }

    public void setShowCarema(boolean bool){
        this.putExtra(MyPhotoPickerActivity.EXTRA_SHOW_CAMERA, bool);
    }

    public void setMaxTotal(int total){
        this.putExtra(MyPhotoPickerActivity.EXTRA_SELECT_COUNT, total);
    }

    /**
     * 选择
     * @param model
     */
    public void setSelectModel(SelectModel model){
        this.putExtra(MyPhotoPickerActivity.EXTRA_SELECT_MODE, Integer.parseInt(model.toString()));
    }

    /**
     * 已选择的照片地址
     * @param imagePathis
     */
    public void setSelectedPaths(ArrayList<String> imagePathis){
        this.putStringArrayListExtra(MyPhotoPickerActivity.EXTRA_DEFAULT_SELECTED_LIST, imagePathis);
    }

    /**
     * 显示相册图片的属性
     * @param config
     */
    public void setImageConfig(ImageConfig config){
        this.putExtra(MyPhotoPickerActivity.EXTRA_IMAGE_CONFIG, config);
    }

}
