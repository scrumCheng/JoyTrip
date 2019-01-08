package nju.joytrip.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import nju.joytrip.R;
import nju.joytrip.entity.E_WordShare;

public class CheckWordShare extends AppCompatActivity {
    private SimpleAdapter mShareAdapter;
    private Button mShareButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_share);
        Bmob.initialize(this, "f6fbdb11a6a945a3382bf9225de95646");
        ListView lv = (ListView)findViewById(R.id.share_item_list);
        loadData(lv);
        mShareButton = (Button)findViewById(R.id.share_btn);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckWordShare.this, WordSharePublish.class);
                startActivity(intent);
            }
        });
    }
    private void loadData(final ListView lv){
        BmobQuery<E_WordShare> bmobQuery = new BmobQuery<E_WordShare>();
        bmobQuery.findObjects(new FindListener<E_WordShare>() {
            @Override
            public void done(List<E_WordShare> list, BmobException e) {
                if (e == null){
                    List<Map<String, String>> mapList = new ArrayList<>();
                    for (E_WordShare event : list){
                        HashMap mHashMap = new HashMap<>();
                        mHashMap.put("title", event.getTitle());
                        mHashMap.put("content", event.getContent());
                        mHashMap.put("time", event.getCreatedAt());
                        mHashMap.put("id", event.getObjectId());
                        mapList.add(mHashMap);
                    }
                    mShareAdapter = new SimpleAdapter(CheckWordShare.this, mapList, R.layout.word_share_item,
                            new String[]{"title", "content", "time"},
                            new int[]{R.id.share_item_title, R.id.share_item_content, R.id.share_item_createTime});

                    lv.setAdapter(mShareAdapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            HashMap<String, String> h = (HashMap<String, String>)parent.getItemAtPosition(position);
                            String eventId = h.get("id");
                            Intent intent = new Intent(CheckWordShare.this, WShareDetail.class);
                            intent.putExtra("id", eventId);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }
}
