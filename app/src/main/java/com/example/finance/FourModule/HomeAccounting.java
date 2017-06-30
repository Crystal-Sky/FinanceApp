package com.example.finance.FourModule;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.finance.AboutListView.ListAdapter;
import com.example.finance.AboutListView.ListItem;
import com.example.finance.MonthRecord;
import com.example.finance.NewRecord;
import com.example.finance.R;
import com.example.finance.TodayRecord;
import com.example.finance.WeekRecord;
import com.example.finance.YearRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 云雪 on 2016/11/23.
 */
public class HomeAccounting  extends Fragment {
    ListView listView;
    List<ListItem> list;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.home_accounting,container,false);
        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView)getActivity().findViewById(R.id.homeList);
        ListAdapter listAdapter = new ListAdapter(getActivity());
        listView.setAdapter(listAdapter);
        list = new ArrayList<ListItem>();
        ListItem item;
        item=new ListItem(3,"记一笔","");
        list.add(item);
        item=new ListItem(3,"今天","");
        list.add(item);
        item=new ListItem(3,"本周","");
        list.add(item);
        item=new ListItem(3,"本月","");
        list.add(item);
        item=new ListItem(3,"本年","");
        list.add(item);
        listAdapter.setList(list);
        listAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).getName().equals("记一笔"))
                {
                    Intent intent=new Intent(getActivity(),NewRecord.class);
                    startActivity(intent);//开启一个新的Activity
                }
                if (list.get(position).getName().equals("今天"))
                {
                    Intent intent=new Intent(getActivity(),TodayRecord.class);
                    startActivity(intent);//开启一个新的Activity
                }
                if (list.get(position).getName().equals("本周"))
                {
                    Intent intent=new Intent(getActivity(),WeekRecord.class);
                    startActivity(intent);//开启一个新的Activity
                }
                if (list.get(position).getName().equals("本月"))
                {
                    Intent intent=new Intent(getActivity(),MonthRecord.class);
                    startActivity(intent);//开启一个新的Activity
                }
                if (list.get(position).getName().equals("本年"))
                {
                    Intent intent=new Intent(getActivity(),YearRecord.class);
                    startActivity(intent);//开启一个新的Activity
                }
            }
        });
    }
}