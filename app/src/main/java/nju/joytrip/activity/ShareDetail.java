package nju.joytrip.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.liji.imagezoom.util.ImageZoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import nju.joytrip.R;
import nju.joytrip.adapter.GridAdapter;
import nju.joytrip.customview.MyGridView;
import nju.joytrip.entity.Comment;
import nju.joytrip.entity.Event;
import nju.joytrip.entity.PWShare;
import nju.joytrip.entity.ShareComment;
import nju.joytrip.entity.User;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ShareDetail extends AppCompatActivity implements View.OnClickListener{
    private TextView mcontentTv;
    private TextView mcreatedTimeTv;
    private TextView muserTv;
    private ImageView miv;
    private MyGridView mgridView;
    private Button commentBtn;
    private SimpleAdapter adapter;
    private ListView listView;


    private PopupWindow popupWindow;
    private View popupView = null;
    private EditText inputComment;
    private String nInputContentText;
    private TextView btn_submit;
    private RelativeLayout rl_input_container;
    private InputMethodManager mInputManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_detail);

        setTitle("分享详情");
        ActionBar actionBar = getSupportActionBar();
        listView = (ListView)findViewById(R.id.comment_list2);
        commentBtn = (Button)findViewById(R.id.comment_btn2);
        commentBtn.setOnClickListener(this);
        actionBar.setDisplayHomeAsUpEnabled(true);
        String id = getIntent().getStringExtra("id");
        BmobQuery<PWShare> query = new BmobQuery<PWShare>();
        query.include("user");
        query.getObject(id, new QueryListener<PWShare>() {
            @Override
            public void done(PWShare event, BmobException e) {
                if (e == null){
                    String content = event.getContent();
                    String createdTime = event.getCreatedAt();
                    User user = event.getUser();
                    String username = user.getUsername();
                    String nickname = user.getNickname();
                    String userPic = user.getUserPic();
                    mcontentTv = (TextView)findViewById(R.id.share_detail_content);
                    mcontentTv.setText(content);
                    mcreatedTimeTv = (TextView)findViewById(R.id.share_detail_createTime);
                    mcreatedTimeTv.setText(createdTime);
                    muserTv = (TextView)findViewById(R.id.share_detail_name);

                    if (nickname == null){
                        muserTv.setText(username);
                    }else {
                        muserTv.setText(nickname);
                    }
                    miv = (ImageView)findViewById(R.id.share_detail_user_pic);
                    Glide.with(ShareDetail.this)
                            .asBitmap()
                            .load(userPic)
                            .apply(bitmapTransform(new CropCircleTransformation()))
                            .into(miv);
                    mgridView = (MyGridView)findViewById(R.id.share_dynamic_photo);
                    final List<String> l = new ArrayList<>();
                    if(event.getPhotoList().size()!=0){
                        l.addAll(event.getPhotoList());
                    }
                    mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String img = (String)parent.getItemAtPosition(position);
                            ImageZoom.show(ShareDetail.this,img,l);
                        }
                    });
                    GridAdapter mgridAdapter = new GridAdapter(ShareDetail.this,event.getPhotoList());
                    mgridView.setAdapter(mgridAdapter);
                    loadComment(event);
                }
            }
        });
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

    @SuppressLint("WrongConstant")
    private void showPopupcomment(){
        if (popupView == null){
            //加载评论框的资源文件
            popupView = LayoutInflater.from(this).inflate(R.layout.comment_popupwindow, null);
        }
        inputComment = (EditText) popupView.findViewById(R.id.et_discuss);
        btn_submit = (Button) popupView.findViewById(R.id.btn_confirm);
        rl_input_container = (RelativeLayout)popupView.findViewById(R.id.rl_input_container);
        //利用Timer这个Api设置延迟显示软键盘，这里时间为200毫秒
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                mInputManager = (InputMethodManager)inputComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputManager.showSoftInput(inputComment, 0);
            }
        }, 200);
        if (popupWindow == null){
            popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT, false);
        }
        //popupWindow的常规设置，设置点击外部事件，背景色
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
                    popupWindow.dismiss();
                return false;
            }
        });

        // 设置弹出窗体需要软键盘，放在setSoftInputMode之前
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 再设置模式，和Activity的一样，覆盖，调整大小。
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置popupwindow的显示位置，这里应该是显示在底部，即Bottom
        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
        popupWindow.update();

        //设置监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            public void onDismiss() {
                mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(), 0); //强制隐藏键盘
            }
        });

        //外部点击事件
        rl_input_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(), 0); //强制隐藏键盘
                popupWindow.dismiss();
            }
        });

        //评论框内的发送按钮设置点击事件
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nInputContentText = inputComment.getText().toString().trim();

                if (nInputContentText == null || "".equals(nInputContentText)) {
                    Toast.makeText(getApplication(), "请输入评论内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                String id = getIntent().getStringExtra("id");
                BmobQuery<PWShare> query = new BmobQuery<PWShare>();
                query.getObject(id, new QueryListener<PWShare>() {
                    @Override
                    public void done(PWShare event, BmobException e) {
                        if (e == null){
                            User user = BmobUser.getCurrentUser(User.class);
                            ShareComment comment = new ShareComment();
                            comment.setEvent(event);
                            comment.setUser(user);
                            comment.setContent(nInputContentText);
                            comment.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null){
                                        Toast.makeText(getApplication(), "评论成功", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });


                mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(),0);
                popupWindow.dismiss();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment_btn2:
                showPopupcomment();
        }
    }

    private void loadComment(PWShare event){
        BmobQuery<ShareComment> bmobQuery = new BmobQuery<ShareComment>();
        bmobQuery.include("user, event");
        bmobQuery.addWhereEqualTo("event", event);
        bmobQuery.order("createdAt");
        bmobQuery.findObjects(new FindListener<ShareComment>() {
            @Override
            public void done(List<ShareComment> list, BmobException e) {
                if (e == null){
                    List<Map<String, String>> mapList = new ArrayList<>();
                    for (ShareComment comment : list){
                        HashMap mHashMap = new HashMap<>();
                        User user = comment.getUser();
                        String username = user.getNickname();
                        if (username == null){
                            username = user.getUsername();
                        }
                        String content = "：" + comment.getContent();
                        mHashMap.put("username",username);
                        mHashMap.put("content", content);
                        mapList.add(mHashMap);
                    }
                    setListView(mapList);
                }
            }
        });
    }

    private void setListView(List<Map<String, String>> mapList){
        adapter = new SimpleAdapter(ShareDetail.this, mapList, R.layout.comment_item,
                new String[]{"username", "content"},
                new int[]{R.id.comment_name, R.id.comment_content});
        listView.setAdapter(adapter);

    }
}
