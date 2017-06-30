package com.example.finance.AboutListView;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.example.finance.R;
import java.util.List;

public class ListAdapter  extends BaseAdapter {
    private int index = -1;
    private String money="0";
    private String name="";
    private String note="";
    public String getAName() {
        return name;
    }
    public String getANote() {
        return note;
    }
    public String getAMoney() {
        return money;
    }
    private Activity activity;
    private List<ListItem> list;
    public ListAdapter(Activity activity) {
        this.activity = activity;
    }
    public void setList(List<ListItem> list) {
        this.list = list;
    }
    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }
    @Override//获取对应位置position所对应Item的对象
    public Object getItem(int position) {
        if (list != null && position < list.size()) {
            return list.get(position);
        }
        return null;
    }
    @Override//获取对应位置position所对应的ItemId
    public long getItemId(int position) {
        return position;
    }
    @Override//获取对应位置position所对应的Item的Type
    public int getItemViewType(int position) {
        if (list != null && position < list.size()) {
            return list.get(position).getType();
        }
        return super.getItemViewType(position);
    }
    @Override//获取设置的Type总个数
    public int getViewTypeCount() {
        return ListItem.TYPE_COUNT;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        switch (type) {
            //金额收入框
            case ListItem.MONEY_EDIT:{
                EditViewHolder holder = null;
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.money_edit, null);
                    holder = new EditViewHolder();
                    holder.editText = (EditText) convertView.findViewById(R.id.money_editText);
                    holder.editText.setTag(position);
                    convertView.setTag(holder);
                } else {
                    holder = (EditViewHolder) convertView.getTag();
                    holder.editText.setTag(position);
                }
                int tag = (int) holder.editText.getTag();
                holder.editText.addTextChangedListener(new MyTextWatcher(holder));
                holder.editText.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            index = (int) v.getTag();
                        }
                        return false;
                    }
                });
                holder.editText.clearFocus();
                if (index != -1 && index == position) {
                    holder.editText.requestFocus();
                }
                break;
            }
            //备注、名称输入框
            case ListItem.TYPE_EDIT: {
                EditViewHolder holder = null;
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.edit_main, null);
                    holder = new EditViewHolder();
                    holder.editText = (EditText) convertView.findViewById(R.id.edit);
                    holder.editText.setTag(position);
                    convertView.setTag(holder);
                } else {
                    holder = (EditViewHolder) convertView.getTag();
                    holder.editText.setTag(position);
                }
                int tag = (int) holder.editText.getTag();
                holder.editText.addTextChangedListener(new MyTextWatcher(holder));
                holder.editText.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            index = (int) v.getTag();
                        }
                        return false;
                    }
                });
                holder.editText.clearFocus();
                if (index != -1 && index == position) {
                    holder.editText.requestFocus();
                }
                if(list.get(position).getName().toString().equals("备注")){
                    holder.editText.setSingleLine(false);
                    holder.editText.setHorizontallyScrolling(false);
                }
                else{
                    holder.editText.setSingleLine(true);
                }
                holder.editText.setHint(list.get(position).getName());
                break;
            }
            //分类、日期选项
            case ListItem.TYPE_TEXT: {
                TextViewHolder holder = null;
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.classification_main, null);
                    holder = new TextViewHolder();
                    holder.textView = (TextView) convertView.findViewById(R.id.identity);
                    holder.textRight = (TextView) convertView.findViewById(R.id.iden);
                    convertView.setTag(holder);
                } else {
                    holder = (TextViewHolder) convertView.getTag();
                }
                holder.textView.setText(list.get(position).getName());
                holder.textRight.setText(list.get(position).getSecondName());
                break;
            }
            //首页选项
            case ListItem.MAIN_VIEW:{
                MainViewHolder holder=null;
                if(convertView==null){
                    convertView=activity.getLayoutInflater().inflate(R.layout.homelistview,null);
                    holder=new MainViewHolder();
                    holder.textView=(TextView)convertView.findViewById(R.id.homeListTextView);
                    convertView.setTag(holder);
                }else{
                    holder=(MainViewHolder)convertView.getTag();
                }
                holder.textView.setText(list.get(position).getName());
                break;
            }
            //理财信息暂时使用的选项
            case ListItem.FINA_VIEW:{
                MainViewHolder holder=null;
                if(convertView==null){
                    convertView=activity.getLayoutInflater().inflate(R.layout.financial_view,null);
                    holder=new MainViewHolder();
                    holder.textView=(TextView)convertView.findViewById(R.id.financeListTextView);
                    convertView.setTag(holder);
                }else{
                    holder=(MainViewHolder)convertView.getTag();
                }
                holder.textView.setText(list.get(position).getName());
                break;
            }
            default:
                break;
        }
        return convertView;
    }
    //输入框
    static class EditViewHolder {
        EditText editText;
    }
    //日期、类型的文本
    static class TextViewHolder {
        TextView textView;
        TextView textRight;
    }
    //首页的文本
    public class MainViewHolder {
        TextView textView;
    }
    class MyTextWatcher implements TextWatcher {
        private EditViewHolder holder;
        public MyTextWatcher(EditViewHolder holder) {
            this.holder = holder;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        @Override
        public void afterTextChanged(Editable s) {
            int pos = (int) holder.editText.getTag();
            switch (pos){
                case 0:
                    money =s.toString();
                    break;
                case 1:
                    name=s.toString();
                    break;
                case 4:
                    note=s.toString();
                    break;
                default:
                    break;
            }
        }
    }
}
