package com.example.finance.FourModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.Collection;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.Vector;
import android.app.Fragment;
import android.view.LayoutInflater;
import com.example.finance.R;

public class StockMarket  extends Fragment {
    private static HashSet<String> StockIds_ = new HashSet();
    private static Vector<String> SelectedStockItems_ = new Vector();
    private final static int BackgroundColor_ = Color.WHITE;
    private final static int HighlightColor_ = Color.rgb(210, 233, 255);
    private final static String ShIndex = "sh000001";
    private final static String SzIndex = "sz399001";
    private final static String ChuangIndex = "sz399006";
    private final static String StockIdsKey_ = "StockIds";
    private ImageButton stock_btn;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.stock_market,container,false);
        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String idsStr = sharedPref.getString(StockIdsKey_, ShIndex + "," + SzIndex + "," + ChuangIndex);
        stock_btn=(ImageButton)getActivity().findViewById(R.id.stock_button);
        String[] ids = idsStr.split(",");
        StockIds_.clear();
        for (String id : ids) {
            StockIds_.add(id);
        }
        Timer timer = new Timer("RefreshStocks");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                refreshStocks();
            }
        }, 0, 10000); // 10 seconds
        stock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) getActivity().findViewById(R.id.editText_stockId);
                String stockId = editText.getText().toString();
                if(stockId.length() != 6)
                    return;
                if (stockId.startsWith("6")) {
                    stockId = "sh" + stockId;
                } else if (stockId.startsWith("0") || stockId.startsWith("3")) {
                    stockId = "sz" + stockId;
                } else
                    return;
                StockIds_.add(stockId);
                refreshStocks();
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        saveStocksToPreferences();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        saveStocksToPreferences();
        super.onSaveInstanceState(savedInstanceState);
    }
    private void saveStocksToPreferences(){
        String ids = "";
        for (String id : StockIds_){
            ids += id;
            ids += ",";
        }
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(StockIdsKey_, ids);
        editor.commit();
    }
    public class Stock {
        public String id_, name_;
        public String open_, yesterday_, now_, high_, low_;
        public String b1_, b2_, b3_, b4_, b5_;
        public String bp1_, bp2_, bp3_, bp4_, bp5_;
        public String s1_, s2_, s3_, s4_, s5_;
        public String sp1_, sp2_, sp3_, sp4_, sp5_;
        public String time_;
    }

    public TreeMap<String, Stock> sinaResponseToStocks(String response){
        response = response.replaceAll("\n", "");
        String[] stocks = response.split(";");
        TreeMap<String, Stock> stockMap = new TreeMap();
        for(String stock : stocks) {
            String[] leftRight = stock.split("=");
            if (leftRight.length < 2)
                continue;
            String right = leftRight[1].replaceAll("\"", "");
            if (right.isEmpty())
                continue;
            String left = leftRight[0];
            if (left.isEmpty())
                continue;

            Stock stockNow = new Stock();
            stockNow.id_ = left.split("_")[2];

            String[] values = right.split(",");
            stockNow.name_ = values[0];
            stockNow.open_ = values[1];
            stockNow.yesterday_ = values[2];
            stockNow.now_ = values[3];
            stockNow.high_ = values[4];
            stockNow.low_ = values[5];
            stockNow.b1_ = values[10];
            stockNow.b2_ = values[12];
            stockNow.b3_ = values[14];
            stockNow.b4_ = values[16];
            stockNow.b5_ = values[18];
            stockNow.bp1_ = values[11];
            stockNow.bp2_ = values[13];
            stockNow.bp3_ = values[15];
            stockNow.bp4_ = values[17];
            stockNow.bp5_ = values[19];
            stockNow.s1_ = values[20];
            stockNow.s2_ = values[22];
            stockNow.s3_ = values[24];
            stockNow.s4_ = values[26];
            stockNow.s5_ = values[28];
            stockNow.sp1_ = values[21];
            stockNow.sp2_ = values[23];
            stockNow.sp3_ = values[25];
            stockNow.sp4_ = values[27];
            stockNow.sp5_ = values[29];
            stockNow.time_ = values[values.length - 3] + "_" + values[values.length - 2];
            stockMap.put(stockNow.id_, stockNow);
        }

        return stockMap;
    }

    public void querySinaStocks(String list){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url ="http://hq.sinajs.cn/list=" + list;
        //http://hq.sinajs.cn/list=sh600000,sh600536

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        updateStockListView(sinaResponseToStocks(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        queue.add(stringRequest);
    }

    private void refreshStocks(){
        String ids = "";
        for (String id : StockIds_){
            ids += id;
            ids += ",";
        }
        querySinaStocks(ids);
    }



    public void updateStockListView(TreeMap<String, Stock> stockMap){

        TableLayout table = (TableLayout)getActivity().findViewById(R.id.stock_table);
        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(true);
        table.removeAllViews();

        // Title
        TableRow rowTitle = new TableRow(getActivity());
        TextView nameTitle = new TextView(getActivity());
        nameTitle.setText(getResources().getString(R.string.stock_name_title));
        rowTitle.addView(nameTitle);
        TextView nowTitle = new TextView(getActivity());
        nowTitle.setGravity(Gravity.RIGHT);
        nowTitle.setText(getResources().getString(R.string.stock_now_title));
        rowTitle.addView(nowTitle);
        TextView percentTitle = new TextView(getActivity());
        percentTitle.setGravity(Gravity.RIGHT);
        percentTitle.setText(getResources().getString(R.string.stock_increase_percent_title));
        rowTitle.addView(percentTitle);
        TextView increaseTitle = new TextView(getActivity());
        increaseTitle.setGravity(Gravity.RIGHT);
        increaseTitle.setText(getResources().getString(R.string.stock_increase_title));
        rowTitle.addView(increaseTitle);
        table.addView(rowTitle);

        Collection<Stock> stocks = stockMap.values();
        for(Stock stock : stocks)
        {
            if(stock.id_.equals(ShIndex) || stock.id_.equals(SzIndex) || stock.id_.equals(ChuangIndex)){
                Double dNow = Double.parseDouble(stock.now_);
                Double dYesterday = Double.parseDouble(stock.yesterday_);
                Double dIncrease = dNow - dYesterday;
                Double dPercent = dIncrease / dYesterday * 100;
                String change = String.format("%.2f", dPercent) + "% " + String.format("%.2f", dIncrease);

                int indexId;
                int changeId;
                if(stock.id_.equals(ShIndex)) {
                    indexId = R.id.stock_sh_index;
                    changeId = R.id.stock_sh_change;
                }
                else if(stock.id_.equals(SzIndex)) {
                    indexId = R.id.stock_sz_index;
                    changeId = R.id.stock_sz_change;
                }
                else{
                    indexId = R.id.stock_chuang_index;
                    changeId = R.id.stock_chuang_change;
                }

                TextView indexText = (TextView)getActivity().findViewById(indexId);
                indexText.setText(stock.now_);
                int color = Color.BLACK;
                if(dIncrease > 0) {
                    color = Color.RED;
                }
                else if(dIncrease < 0){
                    color = Color.GREEN;
                }
                indexText.setTextColor(color);

                TextView changeText = (TextView)getActivity().findViewById(changeId);
                changeText.setText(change);

                continue;
            }

            TableRow row = new TableRow(getActivity());
            row.setGravity(Gravity.CENTER_VERTICAL);
            if (SelectedStockItems_.contains(stock.id_)){
                row.setBackgroundColor(HighlightColor_);
            }

            LinearLayout nameId = new LinearLayout(getActivity());
            nameId.setOrientation(LinearLayout.VERTICAL);
            TextView name = new TextView(getActivity());
            name.setText(stock.name_);
            nameId.addView(name);
            TextView id = new TextView(getActivity());
            id.setTextSize(10);
            id.setText(stock.id_);
            nameId.addView(id);
            row.addView(nameId);

            TextView now = new TextView(getActivity());
            now.setGravity(Gravity.RIGHT);
            now.setText(stock.now_);
            row.addView(now);

            TextView percent = new TextView(getActivity());
            percent.setGravity(Gravity.RIGHT);
            TextView increaseValue = new TextView(getActivity());
            increaseValue.setGravity(Gravity.RIGHT);
            Double dOpen = Double.parseDouble(stock.open_);
            Double dB1 = Double.parseDouble(stock.bp1_);
            Double dS1 = Double.parseDouble(stock.sp1_);
            if(dOpen == 0 && dB1 == 0 && dS1 == 0) {
                percent.setText("--");
                increaseValue.setText("--");
            }
            else{
                Double dNow = Double.parseDouble(stock.now_);
                if(dNow == 0) {// before open
                    if(dS1 == 0) {
                        dNow = dB1;
                        now.setText(stock.bp1_);
                    }
                    else {
                        dNow = dS1;
                        now.setText(stock.sp1_);
                    }
                }
                Double dYesterday = Double.parseDouble(stock.yesterday_);
                Double dIncrease = dNow - dYesterday;
                Double dPercent = dIncrease / dYesterday * 100;
                percent.setText(String.format("%.2f", dPercent) + "%");
                increaseValue.setText(String.format("%.2f", dIncrease));
                int color = Color.BLACK;
                if(dIncrease > 0) {
                    color = Color.RED;
                }
                else if(dIncrease < 0){
                    color = Color.GREEN;
                }

                now.setTextColor(color);
                percent.setTextColor(color);
                increaseValue.setTextColor(color);
            }
            row.addView(percent);
            row.addView(increaseValue);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewGroup group = (ViewGroup) v;
                    ViewGroup nameId = (ViewGroup) group.getChildAt(0);
                    TextView idText = (TextView) nameId.getChildAt(1);
                    if (SelectedStockItems_.contains(idText.getText().toString())) {
                        v.setBackgroundColor(BackgroundColor_);
                        SelectedStockItems_.remove(idText.getText().toString());
                    } else {
                        v.setBackgroundColor(HighlightColor_);
                        SelectedStockItems_.add(idText.getText().toString());
                    }
                }
            });

            table.addView(row);

            String sid = stock.id_;
            sid = sid.replaceAll("sh", "");
            sid = sid.replaceAll("sz", "");
        }
    }
}
