package com.example.finance;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finance.AboutListView.AlertView;
import com.example.finance.AboutListView.ListAdapter;
import com.example.finance.AboutListView.ListItem;
import com.example.finance.AboutListView.OnDismissListener;
import com.example.finance.AboutListView.OnItemClickListener;
import com.example.finance.DBA.InDB;
import com.example.finance.DBA.InObject;
import com.example.finance.DBA.OutDB;
import com.example.finance.DBA.OutObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class InputRecord extends Fragment implements OnItemClickListener, OnDismissListener {
    Button newAnother;
    Button save;
    private int mYear;// 年
    private int mMonth;// 月
    private int mDay;// 日
    private double money=0;//金额
    private String name="名称";//名称
    private TextView textType;//类型文本
    private String type="工资";//类型
    private String note="备注";//备注
    ListView listView;//列表
    ListAdapter listAdapter;//适配器
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.input_record,container,false);
        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView)getActivity().findViewById(R.id.in_list);
        newAnother=(Button)getActivity().findViewById(R.id.new_another_input);
        save=(Button)getActivity().findViewById(R.id.record_save_input);
        listAdapter = new ListAdapter(getActivity());
        listView.setAdapter(listAdapter);

        final Calendar c = Calendar.getInstance();// 获取当前系统日期
        mYear = c.get(Calendar.YEAR);// 获取年份
        mMonth = c.get(Calendar.MONTH);// 获取月份
        mDay = c.get(Calendar.DAY_OF_MONTH);// 获取天数

        List<ListItem> list = new ArrayList<ListItem>();
        ListItem item;
        item=new ListItem(0,"","");
        list.add(item);
        item=new ListItem(1,"名称","");
        list.add(item);
        item=new ListItem(2,"类型","工资");
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
                    case 0:
                        EditText E=(EditText)view.findViewById(R.id.money_editText);
                        E.requestFocus();
                        E.setSelection(E.getText().length());
                        break;
                    case 2:
                        textType=(TextView)view.findViewById(R.id.iden);
                        new AlertView(new String[]{"工资", "股票", "红包", "兼职", "奖金","分红","其他"},
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
        newAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!listAdapter.getAMoney().trim().equals("0")&&!listAdapter.getAMoney().trim().equals("")){
                    money=Double.parseDouble(listAdapter.getAMoney());
                    name=listAdapter.getAName();
                    note=listAdapter.getANote();
                    InDB inAccount = new InDB(getActivity());
                    InObject tb_inAccount = new InObject(
                            inAccount.getMaxId() + 1,money,name,type,
                            mYear,mMonth+1,mDay,note);
                    inAccount.add(tb_inAccount);
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
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!listAdapter.getAMoney().trim().equals("0")&&!listAdapter.getAMoney().trim().equals("")){
                    money=Double.parseDouble(listAdapter.getAMoney());
                    name=listAdapter.getAName();
                    note=listAdapter.getANote();
                    InDB inAccount = new InDB(getActivity());
                    InObject tb_inAccount = new InObject(
                            inAccount.getMaxId() + 1,money,name,type,
                            mYear,mMonth+1,mDay,note);
                    inAccount.add(tb_inAccount);
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
    @Override
    public void onItemClick(Object o,int position) {
        switch (position){
            case 0:
                type="工资";
                break;
            case 1:
                type="股票";
                break;
            case 2:
                type="红包";
                break;
            case 3:
                type="兼职";
                break;
            case 4:
                type="奖金";
                break;
            case 5:
                type="分红";
                break;
            case 6:
                type="其他";
                break;
            default:
                break;
        }
        textType.setText(type);
    }

    @Override
    public void onDismiss(Object o) {
    }
    public OnItemClickListener GET(){
        return this;
    }
    public OnDismissListener GET2(){
        return this;
    }
}
