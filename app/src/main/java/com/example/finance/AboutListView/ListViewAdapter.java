package com.example.finance.AboutListView;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finance.R;

public class ListViewAdapter extends BaseAdapter {

    private Activity activity;
    private List<TListItem> list;

    public ListViewAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setList(List<TListItem> list) {
        this.list = list;
    }
    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null && position < list.size()) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (list != null && position < list.size()) {
            return list.get(position).getType();
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return TListItem.TYPE_COUNT;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);

        switch (type) {
            case TListItem.TYPE1: {
                ViewHolder holder=null;
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.todaylistview, null);
                    holder=new ViewHolder();
                    holder.imageView=(ImageView)convertView.findViewById(R.id.item_image);
                    holder.tpye=(TextView) convertView.findViewById(R.id.btpye);
                    holder.name=(TextView)convertView.findViewById(R.id.pname);
                    holder.note=(TextView) convertView.findViewById(R.id.beizhu);
                    holder.year=(TextView) convertView.findViewById(R.id.pay_year);
                    holder.month=(TextView)convertView.findViewById(R.id.pay_month);
                    holder.day=(TextView)convertView.findViewById(R.id.pay_day);
                    holder.money=(TextView)convertView.findViewById(R.id.pay_sum);
                    holder.IOU=(TextView)convertView.findViewById(R.id.shouzhi);
                    convertView.setTag(holder);
                }
                else {
                    holder=(ViewHolder)convertView.getTag();
                }
                holder.imageView.setImageResource(list.get(position).getPicture());
                holder.tpye.setText(list.get(position).getPurtype());
                holder.name.setText(list.get(position).getName());
                holder.note.setText(list.get(position).beizhu());
                holder.money.setText(String.valueOf(list.get(position).getMoney()));
                holder.year.setText(String.valueOf(list.get(position).year()));
                holder.month.setText(String.valueOf(list.get(position).month()));
                holder.day.setText(String.valueOf(list.get(position).day()));
                holder.IOU.setText(list.get(position).getIOU());
                break;
            }
            default:
                break;
        }
        return convertView;
    }
    static class ViewHolder {
        TextView tpye;
        TextView name;
        TextView note;
        TextView money;
        TextView year;
        TextView month;
        TextView day;
        TextView IOU;
        ImageView imageView;
    }
}

