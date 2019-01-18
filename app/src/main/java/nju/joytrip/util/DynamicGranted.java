package nju.joytrip.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class DynamicGranted {
    private Activity mActivity;

    public DynamicGranted(Activity mActivity) {
        this.mActivity = mActivity;
    }

    //添加动态权限
    public void dynamicShare() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_LOGS,
//                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_APN_SETTINGS,
                    Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(mActivity, mPermissionList, 123);
        }
    }

    /**
     * 检测是否授予相关权限
     * @return
     */
    public boolean permissionGranted(){
        //如果没有读写文件的权限
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA )){
            return false;
        }
        return true;
    }
}
