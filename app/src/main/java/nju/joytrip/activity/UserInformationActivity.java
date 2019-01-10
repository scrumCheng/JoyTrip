package nju.joytrip.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import nju.joytrip.BuildConfig;
import nju.joytrip.R;
import nju.joytrip.entity.User;

public class UserInformationActivity extends AppCompatActivity {

    private  User user;
    private ImageView imageView_portrait;
    private RelativeLayout layout_portrait, layout_nickname, layout_userid,
            layout_phonenumber, layout_gender, layout_email, layout_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        //初始化Bmob
        Bmob.initialize(this, "f6fbdb11a6a945a3382bf9225de95646");
        user = BmobUser.getCurrentUser(User.class);

        layout_portrait = findViewById(R.id.layout_portrait);
        imageView_portrait = findViewById(R.id.information_activity_portrait);
        layout_nickname = findViewById(R.id.layout_nickname);
        layout_userid = findViewById(R.id.layout_userid);
        layout_phonenumber = findViewById(R.id.layout_phonenumber);
        layout_gender = findViewById(R.id.layout_gender);
        layout_email = findViewById(R.id.layout_email);
        layout_password = findViewById(R.id.layout_password);

        refreshUserInformation();

        layout_portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserInformationActivity.this)
                        .setPositiveButton("相机", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //动态权限：点击相机时获取相机权限
                                DynamicShare();
                                //从相机获取图片
                                getPicFromCamera();
                            }
                        }).setNegativeButton("相册", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //从相册获取图片
                                getPicFromAlbm();
                            }
                        }).create().show();
            }
        });
    }

    private void refreshUserInformation() {
        refreshUserPortrait();
        ((TextView) layout_nickname.getChildAt(1)).setText(user.getNickname());
        ((TextView) layout_userid.getChildAt(1)).setText(user.getUsername());
        ((TextView) layout_phonenumber.getChildAt(1)).setText(user.getMobilePhoneNumber());
        ((TextView) layout_gender.getChildAt(1)).setText(user.getGender());
        ((TextView) layout_email.getChildAt(1)).setText(user.getEmail());
    }

    private void refreshUserPortrait() {
        Glide.with(this)
                .load(user.getUserPic())
                .into(imageView_portrait);
    }

    //添加动态权限
    private void DynamicShare() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.WRITE_APN_SETTINGS,
                    Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
    }

    //调用系统相机
    private void getPicFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    //调用相册
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            // 调用相机后返回
            case 1:
                if (resultCode == RESULT_OK) {
                    Bitmap photo = intent.getParcelableExtra("data");
                    //保存后上传
                    String uri = saveImage("userHeader", photo);
                    //cropPhoto(uri);
                    uploadUserPortrait(uri);
                }
                break;
            //调用相册后返回
            case 2:
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    String []imgs1={MediaStore.Images.Media.DATA};//将图片URI转换成存储路径
                    Cursor cursor=this.managedQuery(uri, imgs1, null, null, null);
                    int index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String img_url=cursor.getString(index);
                    //cropPhoto(img_url);//裁剪图片
                    uploadUserPortrait(img_url);
                }
                break;
            //调用剪裁后返回
            case 3:
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    //在这里获得了剪裁后的Bitmap对象，可以用于上传
                    Bitmap image = bundle.getParcelable("data");
                    //保存后上传
                    String path = saveImage("userHeader", image);
                    uploadUserPortrait(path);
                }
                break;
        }
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(String uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        File file = new File(uri);

//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3 );
    }

    /**
     * 向Bomob上传用户头像
     * @param path
     */
    private int uploadUserPortrait(String path) {
        File file = new File(path);
        final BmobFile bmobFile = new BmobFile(file);
        //上传
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    Toast.makeText(UserInformationActivity.this, "上传文件成功!" , Toast.LENGTH_SHORT).show();
                    user.setUserPic(bmobFile.getFileUrl());
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.d("Success Portrait Upload! New UserPic Uri", user.getUserPic());
                                refreshUserPortrait();
                            }
                        }
                    });
                }else{
                    Toast.makeText(UserInformationActivity.this, "上传文件失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return 0;
    }

    /**
     * 保存图片到本地
     *
     * @param name
     * @param bmp
     * @return
     */
    public String saveImage(String name, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory().getPath());
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
