package com.example.finance.FourModule;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.finance.AboutListView.ListAdapter;
import com.example.finance.AboutListView.ListItem;
import com.example.finance.BaseMessage;
import com.example.finance.CakeView;
import com.example.finance.DBA.InDB;
import com.example.finance.DBA.OutDB;
import com.example.finance.DoubleDatePickerDialog;
import com.example.finance.R;

import java.util.Calendar;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChartAnalysis extends Fragment {
    private int year1,month1,day1,year2,month2,day2;
    private CakeView cv;
    private List<BaseMessage> mList;
    private Button btn,btn_income,btn_outcome;
    private boolean Tag = false;//记录是否选择时间
    private TextView tvSum,tvName,tvTime;
    String textString;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.chart_analysis,container,false);
        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cv = (CakeView) getActivity().findViewById(R.id.CakeView);
        btn = (Button)getActivity().findViewById(R.id.dateBtn);
        btn_income = (Button) getActivity().findViewById(R.id.btn_income);
        btn_outcome = (Button) getActivity().findViewById(R.id.btn_outcome);
        tvSum = (TextView) getActivity().findViewById(R.id.tvSum);
        tvName = (TextView) getActivity().findViewById(R.id.tvName);
        tvTime = (TextView) getActivity().findViewById(R.id.tvTime);
        mList = new ArrayList<>();
        Calendar c = Calendar.getInstance();



        cv.setTextColor(Color.parseColor("#000000"));
        //设置默认当前月的图表
        mList = new InDB(getActivity()).currentYearData();
        cv.setCakeData(mList);
        tvName.setText("收入总额：");
        tvSum.setText(""+InDB.currentSum);
        textString = String.format("%d-%d\n",c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1);
        tvTime.setText("             "+textString);
        //tvName.setText("");
        btn.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                Tag = true;
                // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                new DoubleDatePickerDialog(getActivity(), 0, new DoubleDatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear,
                                          int endDayOfMonth) {
                        textString = String.format("%d-%d-%d至%d-%d-%d\n", startYear,
                                startMonthOfYear + 1, startDayOfMonth, endYear, endMonthOfYear + 1, endDayOfMonth);

                        year1=startYear;
                        month1=startMonthOfYear + 1;
                        day1=startDayOfMonth;
                        year2=endYear;
                        month2=endMonthOfYear + 1;
                        day2=endDayOfMonth;
                        if (year1>year2 || (year1<year2 && month1>month2) ||(year1<year2 && month1<month1 && day1>day2)){
                            Toast.makeText(getActivity(),"时间选择错误！",Toast.LENGTH_LONG).show();
                            Tag = false;
                        }
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
            }
        });
        // cv.setStartAngle(60);

        //cv.setSpacingLineColor(Color.parseColor("#000000"));

        btn_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tag){
                    mList = new InDB(getActivity()).incomeData_analysis(year1,month1,day1,year2,month2,day2);
                    cv.setCakeData(mList);
                    tvName.setText("收入总额：");
                    tvSum.setText(InDB.sum+"");
                    tvTime.setText(textString);
                    Tag = false;
                }else {
                    Toast.makeText(getActivity(),"请选择时间",Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_outcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tag){
                    mList = new OutDB(getActivity()).outcomeData_analysis(year1,month1,day1,year2,month2,day2);
                    cv.setCakeData(mList);
                    tvName.setText("支出总额：");
                    tvSum.setText(OutDB.sum+"");
                    tvTime.setText(textString);
                    Tag = false;
                }else {
                    Toast.makeText(getActivity(),"请选择时间",Toast.LENGTH_LONG).show();
                }
            }
        });
       /* cv.setTextColor(Color.parseColor("#000000"));
        //cv.setCakeStrokeWidth(500);
        cv.setCakeData(mList);*/
    }
}