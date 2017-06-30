package com.example.finance.AboutListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.finance.R;

public class AlertView {
    public static enum Style{
        Alert
    }
    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
    );
    public static final int HORIZONTAL_BUTTONS_MAXCOUNT = 2;
    private List<String> mOthers;
    private ArrayList<String> mDatas = new ArrayList<String>();
    private Context context;
    private ViewGroup contentContainer;
    private ViewGroup decorView;
    private ViewGroup rootView;
    private Style style = Style.Alert;
    private OnDismissListener onDismissListener;
    private OnItemClickListener onItemClickListener;
    private boolean isDismissing;
    private Animation outAnim;
    private Animation inAnim;
    private int gravity = Gravity.CENTER;
    public AlertView(String[] others, Context context, Style style,OnItemClickListener onItemClickListener){
        this.context = context;
        this.style = style;
        this.onItemClickListener = onItemClickListener;
        initData(others);
        initViews();
        init();
        initEvents();
    }
    protected void initData(String[] others) {
        if (others != null){
            this.mOthers = Arrays.asList(others);
            this.mDatas.addAll(mOthers);
        }
    }
    protected void initViews(){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        decorView = (ViewGroup) ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_alertview, decorView, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        contentContainer = (ViewGroup) rootView.findViewById(R.id.content_container);
        int margin_alert_left_right = 0;
        params.gravity = Gravity.CENTER;
        margin_alert_left_right = context.getResources().getDimensionPixelSize(R.dimen.margin_alert_left_right);
        params.setMargins(margin_alert_left_right,0,margin_alert_left_right,0);
        contentContainer.setLayoutParams(params);
        gravity = Gravity.CENTER;
        initAlertViews(layoutInflater);
    }
    protected void initListView(){
        ListView alertButtonListView = (ListView) contentContainer.findViewById(R.id.alertButtonListView);
        AlertViewAdapter adapter = new AlertViewAdapter(mDatas,null);
        alertButtonListView.setAdapter(adapter);
        alertButtonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(onItemClickListener != null)onItemClickListener.onItemClick(AlertView.this,position);
                dismiss();
            }
        });
    }
    protected void initAlertViews(LayoutInflater layoutInflater) {
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.layout_alertview_alert, contentContainer);
        int position = 0;
        if(mDatas.size()<=HORIZONTAL_BUTTONS_MAXCOUNT){
            ViewStub viewStub = (ViewStub) contentContainer.findViewById(R.id.viewStubHorizontal);
            viewStub.inflate();
            LinearLayout loAlertButtons = (LinearLayout) contentContainer.findViewById(R.id.loAlertButtons);
            for (int i = 0; i < mDatas.size(); i ++) {
                if (i != 0){
                    View divier = new View(context);
                    divier.setBackgroundColor(context.getResources().getColor(R.color.bgColor_divier));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)context.getResources().getDimension(R.dimen.size_divier), LinearLayout.LayoutParams.MATCH_PARENT);
                    loAlertButtons.addView(divier,params);
                }
                View itemView = LayoutInflater.from(context).inflate(R.layout.item_alertbutton, null);
                TextView tvAlert = (TextView) itemView.findViewById(R.id.tvAlert);
                tvAlert.setClickable(true);
                if(mDatas.size() == 1){
                    tvAlert.setBackgroundResource(R.drawable.bg_alertbutton_bottom);
                }
                else if(i == 0){
                    tvAlert.setBackgroundResource(R.drawable.bg_alertbutton_left);
                }
                else if(i == mDatas.size() - 1){
                    tvAlert.setBackgroundResource(R.drawable.bg_alertbutton_right);
                }
                String data = mDatas.get(i);
                tvAlert.setText(data);
                tvAlert.setOnClickListener(new OnTextClickListener(position));
                position++;
                loAlertButtons.addView(itemView,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            }
        }
        else{
            ViewStub viewStub = (ViewStub) contentContainer.findViewById(R.id.viewStubVertical);
            viewStub.inflate();
            initListView();
        }
    }
    protected void init() {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }
    protected void initEvents() {
    }
    private void onAttached(View view) {
        decorView.addView(view);
        contentContainer.startAnimation(inAnim);
    }
    public void show() {
        if (isShowing()) {
            return;
        }
        onAttached(rootView);
    }
    public boolean isShowing() {
        View view = decorView.findViewById(R.id.outmost_container);
        return view != null;
    }
    public AlertView setOnDismissListener(OnDismissListener onDismissListener) {
        View view = rootView.findViewById(R.id.outmost_container);
        view.setOnTouchListener(onCancelableTouchListener);
        this.onDismissListener = onDismissListener;
        return this;
    }
    public void dismiss() {
        if (isDismissing) {
            return;
        }
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                decorView.post(new Runnable() {
                    @Override
                    public void run() {
                        decorView.removeView(rootView);
                        isDismissing = false;
                        if (onDismissListener != null) {
                            onDismissListener.onDismiss(AlertView.this);
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        contentContainer.startAnimation(outAnim);
        isDismissing = true;
    }
    public Animation getInAnimation() {
        int res = AlertAnimateUtil.getAnimationResource(this.gravity, true);
        return AnimationUtils.loadAnimation(context, res);
    }
    public Animation getOutAnimation() {
        int res = AlertAnimateUtil.getAnimationResource(this.gravity, false);
        return AnimationUtils.loadAnimation(context, res);
    }
    class OnTextClickListener implements View.OnClickListener{
        private int position;
        public OnTextClickListener(int position){
            this.position = position;
        }
        @Override
        public void onClick(View view) {
            if(onItemClickListener != null)onItemClickListener.onItemClick(AlertView.this,position);
            dismiss();
        }
    }
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
            }
            return false;
        }
    };
}
