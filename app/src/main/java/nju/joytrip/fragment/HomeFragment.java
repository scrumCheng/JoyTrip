package nju.joytrip.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import nju.joytrip.R;
import nju.joytrip.activity.DetailActivity;
import nju.joytrip.activity.EventPublish;
import nju.joytrip.entity.Event;

public class HomeFragment extends Fragment {
    private SimpleAdapter adapter;
    private Button pubBtn;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ListView lv = (ListView)view.findViewById(R.id.item_list);
        loadData(lv);
        pubBtn = (Button)view.findViewById(R.id.publish_btn);
        pubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventPublish.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void loadData(final ListView lv){
        BmobQuery<Event> bmobQuery = new BmobQuery<Event>();
        bmobQuery.findObjects(new FindListener<Event>() {
            @Override
            public void done(List<Event> list, BmobException e) {
                if (e == null){
                    List<Map<String, String>> mapList = new ArrayList<>();
                    for (Event event : list){
                        HashMap mHashMap = new HashMap<>();
                        mHashMap.put("title", event.getTitle());
                        mHashMap.put("content", event.getContent());
                        mHashMap.put("time", event.getCreatedAt());
                        mHashMap.put("id", event.getObjectId());
                        mapList.add(mHashMap);
                    }
                    adapter = new SimpleAdapter(getActivity(), mapList, R.layout.event_item,
                            new String[]{"title", "content", "time"},
                            new int[]{R.id.item_title, R.id.item_content, R.id.item_createTime});

                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            HashMap<String, String> h = (HashMap<String, String>)parent.getItemAtPosition(position);
                            String eventId = h.get("id");
                            Intent intent = new Intent(getActivity(), DetailActivity.class);
                            intent.putExtra("id", eventId);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }
}
