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

import android.widget.Toast;

import com.liji.imagezoom.util.ImageZoom;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


import cn.bmob.v3.listener.UploadBatchListener;
import me.nereo.multi_image_selector.MultiImageSelector;
import nju.joytrip.R;
import nju.joytrip.adapter.GridAdapter;
import nju.joytrip.customview.MyGridView;
import nju.joytrip.entity.PWShare;
import nju.joytrip.entity.User;



public class PicWordSharePublish extends AppCompatActivity {
    private static final int REQUEST_IMAGE = 10;
    private static final int REQUEST_PREVIEW = 20;
    private ArrayList<String> imagePaths = new ArrayList<>();
    private MyGridView mgridView;
    private GridAdapter mgridAdapter;
    private EditText mtextView;
    private Button msubmit_btn;
    private final PWShare mshare = new PWShare();


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
                if(imagePaths.contains("add")){
                    imagePaths.remove("add");
                }
                if("add".equals(imgs)){
                    MultiImageSelector.create(PicWordSharePublish.this)
                            .showCamera(true) // 是否显示相机. 默认为显示
                            .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                            .multi() // 多选模式, 默认模式;
                            .origin(imagePaths) // 默认已选择图片. 只有在选择模式为多选时有效
                            .start(PicWordSharePublish.this, REQUEST_IMAGE);

                }
                else{
                    //预览图片
                    String img = (String)parent.getItemAtPosition(position);
                    List<String> l = new ArrayList<>();
                    ImageZoom.show(PicWordSharePublish.this,img,imagePaths);

                }
            }
        });
        imagePaths.add("add");
        mgridAdapter = new GridAdapter(PicWordSharePublish.this,imagePaths);
        mgridView.setAdapter(mgridAdapter);


        msubmit_btn = findViewById(R.id.share_submit_btn);
        msubmit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtextView = (EditText) findViewById(R.id.share_content_text);
                String content = mtextView.getText().toString();
                User user = BmobUser.getCurrentUser(User.class);
                mshare.setContent(content);
                mshare.setUser(user);
                if (imagePaths.contains("add")) {
                    imagePaths.remove("add");
                }
                final String[] filepaths = new String[imagePaths.size()];
                for (int i = 0; i < imagePaths.size(); i++) {
                    filepaths[i] = imagePaths.get(i);
                }
                BmobFile.uploadBatch(filepaths, new UploadBatchListener() {

                    @Override
                    public void onSuccess(List<BmobFile> files, List<String> urls) {
                        //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                        //2、urls-上传文件的完整url地址
                        if (urls.size() == filepaths.length) {//如果数量相等，则代表文件全部上传完成
                            ArrayList<String> s = new ArrayList<>();
                            for (int i = 0; i < urls.size(); i++) {
                                s.add(urls.get(i));
                            }
                            mshare.setPhotoList(s);
                            mshare.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(getApplication(), "发布成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PicWordSharePublish.this, MainActivity.class);
                                        intent.putExtra("id", 1);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(int statuscode, String errormsg) {
                        Log.i("cuowu", "错误码" + statuscode + ",错误描述：" + errormsg);
                    }

                    @Override
                    public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                        //1、curIndex--表示当前第几个文件正在上传
                        //2、curPercent--表示当前上传文件的进度值（百分比）
                        //3、total--表示总的上传文件数
                        //4、totalPercent--表示总的上传进度（百分比）
                    }
                });
                if (filepaths.length == 0) {
                    mshare.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(getApplication(), "发布成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PicWordSharePublish.this, MainActivity.class);
                                intent.putExtra("id", 1);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch(requestCode){
                //选择照片
            case REQUEST_IMAGE:
                ArrayList<String> list = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                Log.d("PicWordSharePublish","数量"+list.size());
                loadAdpater(list);
                break;
//             预览照片
                case REQUEST_PREVIEW:
        }
    }
}

    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths!=null&& imagePaths.size()>0){
            imagePaths.clear();
        }
        if(paths.contains("add")){
            paths.remove("add");
        }
        paths.add("add");
        imagePaths.addAll(paths);
        mgridAdapter = new GridAdapter(PicWordSharePublish.this, imagePaths);
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
