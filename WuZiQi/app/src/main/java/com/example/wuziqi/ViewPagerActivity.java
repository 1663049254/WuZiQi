package com.example.wuziqi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ViewPagerActivity extends Activity {
    private int index=0;
    private TextView txt_title;
    private Button btn_purchase;
    private ViewPager pager=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        txt_title=(TextView)findViewById(R.id.txt_title);
        btn_purchase=(Button) findViewById(R.id.btn_purchase);
        //创建viewpager的适配器
        int[] imgs={R.drawable.bg2,R.drawable.bg3,R.drawable.bg4,R.drawable.bg5,R.drawable.bg6};
        pager=(ViewPager)findViewById(R.id.vp);
        PagerAdapter adapter=new ViewAdapter(this,imgs);
        pager.setAdapter(adapter);
        if(WZQUtils.getBg("willow")==1)
            btn_purchase.setText("使用");
        pager.addOnPageChangeListener(new PageChange());
        btn_purchase.setOnClickListener(new PurchaseClick());
    }
    class PageChange implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            switch(position){
                case 0:
                    txt_title.setText("垂柳");
                    if(WZQUtils.getBg("willow")==1)
                        btn_purchase.setText("使用");
                    else
                        btn_purchase.setText("购买");
                    break;
                case 1:
                    txt_title.setText("墨竹");
                    if(WZQUtils.getBg("bamboo")==1)
                        btn_purchase.setText("使用");
                    else
                        btn_purchase.setText("购买");
                    break;
                case 2:
                    txt_title.setText("白鹤");
                    if(WZQUtils.getBg("crane")==1)
                        btn_purchase.setText("使用");
                    else
                        btn_purchase.setText("购买");
                    break;
                case 3:
                    txt_title.setText("游鱼");
                    if(WZQUtils.getBg("fish")==1)
                        btn_purchase.setText("使用");
                    else
                        btn_purchase.setText("购买");
                    break;
                case 4:
                    txt_title.setText("七夕");
                    if(WZQUtils.getBg("lovers")==1)
                        btn_purchase.setText("使用");
                    else
                        btn_purchase.setText("购买");
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    class PurchaseClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int index=pager.getCurrentItem();
            //System.out.println("当前："+index);
            if(btn_purchase.getText().equals("使用")){
                switch(index){
                    case 0:
                        MainActivity.getwR().setBackgroundResource(R.drawable.bg2);
                        break;
                    case 1:
                        MainActivity.getwR().setBackgroundResource(R.drawable.bg3);
                        break;
                    case 2:
                        MainActivity.getwR().setBackgroundResource(R.drawable.bg4);
                        break;
                    case 3:
                        MainActivity.getwR().setBackgroundResource(R.drawable.bg5);
                        break;
                    case 4:
                        MainActivity.getwR().setBackgroundResource(R.drawable.bg6);
                        break;
                }
                ViewPagerActivity.this.finish();
            }else{
                switch (index){
                    case 0:
                        purchaseBg("willow");
                            //MainActivity.getwR().setBackgroundResource(R.drawable.bg2);
                            break;
                    case 1:
                        purchaseBg("bamboo");
                            //MainActivity.getwR().setBackgroundResource(R.drawable.bg3);
                            break;
                    case 2:
                        purchaseBg("crane");
                            //MainActivity.getwR().setBackgroundResource(R.drawable.bg4);
                            break;
                    case 3:
                        purchaseBg("fish");
                            //MainActivity.getwR().setBackgroundResource(R.drawable.bg5);
                            break;
                    case 4:
                        purchaseBg("lovers");
                            //MainActivity.getwR().setBackgroundResource(R.drawable.bg6);
                            break;
                }
            }

        }
    }
    private boolean purchaseBg(final String bg){
        if(WZQUtils.getBg(bg)==0){
            if(WZQUtils.getCoins()>=30){
                AlertDialog.Builder builder=new AlertDialog.Builder(ViewPagerActivity.this);
                builder.setTitle("提示")
                        .setMessage("是否花费30积分解锁此背景")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        // TODO Auto-generated method stub
                                        WZQUtils.writeCoins(-30);
                                        WZQUtils.writeBg(bg);
                                        WZQUtils.showAlertDialog(ViewPagerActivity.this, "提示","购买成功" );
                                        btn_purchase.setText("使用");
                                    }
                                }
                        );
                builder.create().show();

                return true;
            }else{
                WZQUtils.showAlertDialog(ViewPagerActivity.this, "积分不足","解锁此背景需要30积分" );
                return false;
            }

        }else{
            WZQUtils.showAlertDialog(ViewPagerActivity.this,"提示","你已解锁该背景，无需重复购买");
            return false;
        }
    }
}
