package com.example.finance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.example.finance.AboutListView.AlertView;
import com.example.finance.AboutListView.ListAdapter;
import com.example.finance.AboutListView.ListItem;
import com.example.finance.AboutListView.OnDismissListener;
import com.example.finance.AboutListView.OnItemClickListener;
import com.example.finance.DBA.OutDB;
import com.example.finance.DBA.OutObject;


public class OutputRecord extends Fragment
        implements OnItemClickListener, OnDismissListener{
    Button newAnother;//再记一笔按钮
    Button save;//保存按钮
    private int mYear;// 年
    private int mMonth;// 月
    private int mDay;// 日
    private double money=0;//金额
    private String name="名称";//名称
    private TextView textType;//类型文本
    private String type="衣服";//类型
    private String note="备注";//备注
    ListView listView;//列表
    ListAdapter listAdapter;//适配器
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.output_record,container,false);//关联界面
        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //获取再记一笔按钮
        newAnother=(Button)getActivity().findViewById(R.id.new_another_output);
        //获取保存按钮
        save=(Button)getActivity().findViewById(R.id.record_save_output);
        //获取列表
        listView = (ListView)getActivity().findViewById(R.id.out_list);
        listAdapter = new ListAdapter(getActivity());
        listView.setAdapter(listAdapter);//关联适配器
        final Calendar c = Calendar.getInstance();// 获取当前系统日期
        mYear = c.get(Calendar.YEAR);// 获取年份
        mMonth = c.get(Calendar.MONTH);// 获取月份
        mDay = c.get(Calendar.DAY_OF_MONTH);// 获取天数
        final List<ListItem> list = new ArrayList<ListItem>();
        ListItem item;
        item=new ListItem(0,"","");
        list.add(item);
        item=new ListItem(1,"名称","");
        list.add(item);
        item=new ListItem(2,"类型","衣服");
        list.add(item);
        item=new ListItem(2,"日期",mYear+"年"+(mMonth+1)+"月"+mDay+"日");
        list.add(item);
        item=new ListItem(1,"备注","");
        list.add(item);
        listAdapter.setList(list);
        listAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 2:
                        textType=(TextView)view.findViewById(R.id.iden);
                        new AlertView(new String[]{"衣服", "饮食", "住宿", "出行", "其他"},
                                getActivity(), AlertView.Style.Alert,GET()).setOnDismissListener(GET2()).show();
                        break;
                    case 3:
                        final TextView text=(TextView)view.findViewById(R.id.iden);
                        DatePickerDialog.OnDateSetListener mDateSetListener =
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                          int dayOfMonth) {
                                        mYear = year;// 为年份赋值
                                        mMonth = monthOfYear;// 为月份赋值
                                        mDay = dayOfMonth;// 为天赋值
                                        text.setText(mYear+"年"+(mMonth+1)+"月"+mDay+"日");
                                    }
                                };
                        Dialog calendar=new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth,mDay);
                        calendar.show();
                        break;
                    default:
                        break;
                }
            }
        });
        //点击再记一笔按钮做的处理
        newAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!listAdapter.getAMoney().trim().equals("0")&&!listAdapter.getAMoney().trim().equals("")){
                    money=Double.parseDouble(listAdapter.getAMoney());
                    name=listAdapter.getAName();
                    note=listAdapter.getANote();
                    OutDB outAccount = new OutDB(getActivity());
                    OutObject tb_outAccount = new OutObject(
                            outAccount.getMaxId() + 1,money,name,type,
                            mYear,mMonth+1,mDay,note);
                    outAccount.add(tb_outAccount);// 添加支出信息
                    Intent intent = new Intent();
                    intent.setClass(getActivity(),NewRecord.class);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
                    alert.setTitle("提醒");
                    if(listAdapter.getAMoney().trim().equals(""))
                        alert.setMessage("金额不能为空");
                    else
                        alert.setMessage("金额不能为0");
                    alert.setPositiveButton("确定",null);
                    alert.show();
                }
            }
        });
        //点击保存按钮做的处理
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!listAdapter.getAMoney().trim().equals("0")&&!listAdapter.getAMoney().trim().equals("")){
                    String m=listAdapter.getAMoney();
                    money=Double.parseDouble(m);
                    name=listAdapter.getAName();
                    note=listAdapter.getANote();
                    OutDB outAccount = new OutDB(getActivity());
                    OutObject tb_outAccount = new OutObject(
                            outAccount.getMaxId() + 1,money,name,type,
                            mYear,mMonth+1,mDay,note);
                    outAccount.add(tb_outAccount);// 添加支出信息
                    getActivity().finish();
                }else{
                    AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
                    alert.setTitle("提醒");
                    if(listAdapter.getAMoney().trim().equals(""))
                        alert.setMessage("金额不能为空");
                    else
                        alert.setMessage("金额不能为0");
                    alert.setPositiveButton("确定",null);
                    alert.show();
                }
            }
        });
    }
    @Override//类型选中后更改
    public void onItemClick(Object o,int position) {
        switch (position){
            case 0:
                type="衣服";
                break;
            case 1:
                type="饮食";
                break;
            case 2:
                type="住宿";
                break;
            case 3:
                type="出行";
                break;
            case 4:
                type="其他";
                break;
            default:
                break;
        }
        textType.setText(type);
    }
    @Override//没有点中类型选项对话框时对话框消失
    public void onDismiss(Object o) {
    }
    public OnItemClickListener GET(){
        return this;
    }
    public OnDismissListener GET2(){
        return this;
    }
}