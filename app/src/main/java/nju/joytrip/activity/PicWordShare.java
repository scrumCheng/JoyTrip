package nju.joytrip.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import android.widget.Toast;


import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;

import org.json.JSONArray;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import nju.joytrip.R;
import nju.joytrip.adapter.GridAdapter;
import nju.joytrip.entity.PWShare;
import nju.joytrip.entity.User;



public class PicWordShare extends AppCompatActivity {
    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;
    private ArrayList<String> imagePaths = new ArrayList<>();

    private GridView mgridView;
    private GridAdapter mgridAdapter;
    private EditText mtextView;
    private Button msubmit_btn;
    ImageCaptureManager captureManager = new ImageCaptureManager(PicWordShare.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_word_share);
        setTitle("图文分享");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mgridView = findViewById(R.id.pic_share_gridView);
        mgridView.setNumColumns(3);

        mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgs = (String)parent.getItemAtPosition(position);
                if("paizhao".equals(imgs)){
                    PhotoPickerIntent intent = new PhotoPickerIntent(PicWordShare.this);
                    intent.setSelectModel(SelectModel.MULTI);
                    intent.setShowCarema(true);// 是否显示拍照
                    intent.setMaxTotal(9);// 最多选择照片数量，默认为9
                    if(imagePaths.contains("paizhao")){
                        imagePaths.remove("paizhao");
                    }
                    intent .setSelectedPaths(imagePaths);// 已选中的照片地址， 用于回显选中状态
                    startActivityForResult(intent, REQUEST_CAMERA_CODE);
                }else{
                    Toast.makeText(PicWordShare.this,"1"+position,Toast.LENGTH_LONG).show();
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(PicWordShare.this);
                    intent.setCurrentItem(position);

                    intent.setPhotoPaths(imagePaths);
                    startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                }
            }
        });
        imagePaths.add("paizhao");
        mgridAdapter = new GridAdapter(PicWordShare.this,imagePaths);
        mgridView.setAdapter(mgridAdapter);


        msubmit_btn = findViewById(R.id.share_submit_btn);
        msubmit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtextView = (EditText) findViewById(R.id.share_content_text);
                String content = mtextView.getText().toString();
                PWShare share = new PWShare();
                User user = BmobUser.getCurrentUser(User.class);
                share.setContent(content);
                share.setUser(user);
                share.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(getApplication(), "发布成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PicWordShare.this,MainActivity.class);
                            intent.putExtra("id",1);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch(requestCode){
                //选择照片
            case REQUEST_CAMERA_CODE:
                ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                Log.d("PicWordShare","数量"+list.size());
                loadAdpater(list);
                break;
            case ImageCaptureManager.REQUEST_TAKE_PHOTO:

                if(captureManager.getCurrentPhotoPath() != null) {
                    captureManager.galleryAddPic();
                    // 照片地址
                    String image = captureManager.getCurrentPhotoPath();
                    ArrayList<String> ListExtra = new ArrayList<String>();
                    ListExtra.add(image);
                    loadAdpater(ListExtra);
                    // ...
                }
                break;
            case REQUEST_PREVIEW_CODE:
                ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                loadAdpater(ListExtra);
                break;
        }
    }
}

    private void loadAdpater(ArrayList<String> paths) {
        if(paths.contains("paizhao")){
            paths.remove("paizhao");
        }
        paths.add("paizhao");
        imagePaths.addAll(paths);
        if(mgridAdapter==null) {
            mgridAdapter = new GridAdapter(PicWordShare.this, imagePaths);
        }
        mgridView.setAdapter(mgridAdapter);
        try{
            JSONArray obj = new JSONArray(imagePaths);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
