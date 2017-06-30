package com.example.finance.FourModule;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.AdapterView;
import com.example.finance.AboutListView.ListAdapter;
import com.example.finance.Article;
import com.example.finance.MListAdapter;
import com.example.finance.R;

import java.io.IOException;
import java.util.ArrayList;

public class FinancialInformation  extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeLayout;
    private ListView listview;
    private String path = "http://www.chinawealth.com.cn/zzlc/xwdt/xwzx/list.shtml";
    private MListAdapter adapter;
    private String linkHref;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1 :
                    listview.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }
    };
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.financial_information,container,false);
        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeLayout = (SwipeRefreshLayout)getActivity().findViewById(R.id.swipe_container);
        listview = (ListView) getActivity().findViewById(R.id.listview);

        mSwipeLayout.setOnRefreshListener(this);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setDistanceToTriggerSync(400);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setProgressBackgroundColor(android.R.color.white); // 设定下拉圆圈的背景
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小

        mSwipeLayout.setOnRefreshListener(this);

        new GetListData().execute(path);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(linkHref);
                Intent it = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
            }
        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //停止刷新
                mSwipeLayout.setRefreshing(false);
            }
        },5000);
        Toast.makeText(getActivity(),"刷新成功!", Toast.LENGTH_LONG).show();
    }
    class GetListData extends AsyncTask<String, Void, ArrayList<Article>> {
        @Override
        protected ArrayList<Article> doInBackground(String... arg0) {

            ArrayList<Article> articleList =new ArrayList<Article>();
            try {
                Document doc = Jsoup.connect(path).timeout(5000).get();
                Elements masthead = doc.getElementsByClass("news_left");
                Elements articleElements =  masthead.select("ul.news_list li");

                if (doc != null) {
                    for(int i = 0; i < articleElements.size(); i++) {

                        Element articleElement = articleElements.get(i);

                        Elements titleElement = articleElement.select("h2 a");
                        Elements summaryElement = articleElement.select("p.news_info");
                        Elements timeElement = articleElement.select("div.news_other");
                        linkHref = articleElements.get(i).getElementsByTag("a").attr("href");

                        String title = titleElement.text();
                        String summary = summaryElement.text();
                        //if(summary.length() > 70)
                        //	summary = summary.substring(0, 70);
                        String postTime = timeElement.text();
//                        linkHref = uriElement.text();

                        Log.i("title", title);
                        Log.i("summary", summary);
                        Log.i("postTime", postTime);
                        Log.i("linkHref", linkHref);

                        Article article = new Article(title,summary,postTime);
                        articleList.add(article);

                    }
                }
            } catch (IOException e) {

                e.printStackTrace();

            }return articleList;
        }
        @Override
        protected void onPostExecute(ArrayList<Article> articleList) {

            super.onPostExecute(articleList);
            adapter = new MListAdapter(getActivity(),R.layout.item_article_list,articleList);
            Log.i("adapter", "----------"+adapter.isEmpty());
            Message msg = Message.obtain();
            msg.what=1;
            handler.sendMessage(msg);
        }

    }
}
