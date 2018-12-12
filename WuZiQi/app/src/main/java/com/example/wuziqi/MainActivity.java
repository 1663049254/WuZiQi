package com.example.wuziqi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import server.Match;


public class MainActivity extends Activity {
	protected static Button btn_restart;
	protected static WuziqiPanel wO;
	protected static RelativeLayout wR;
	protected static WuziqiPanel wV;	
	protected static Button btn_change_stone;
	protected static Button btn_change_bg;
	protected static Button btn_change_wood;
	protected static Button btn_match;
	protected static Button btn_shop;
	//protected static Button btn_login;
	protected static ImageView img_chat;
	protected static TextView txt_count_white;
	protected static TextView txt_count_black;
	protected static TextView txt_coin;
	protected static ProgressBar pb;
	private static long mExitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_main);

		findView();
		getCoins();
		ClickEvent ce=new ClickEvent(MainActivity.this);
		ce.bindClickEvent();
		DownCounter.newCounter(txt_count_white, txt_count_black);



	}//oncreate

	private void findView(){
		btn_restart=(Button)findViewById(R.id.btn_restart);
		btn_change_stone=(Button)findViewById(R.id.btn_change_stone);
		btn_change_bg=(Button)findViewById(R.id.btn_change_bg);
		btn_change_wood=(Button)findViewById(R.id.btn_change_wood);
		btn_match=(Button)findViewById(R.id.btn_match);
		btn_shop=(Button)findViewById(R.id.btn_shop);
		//btn_login=(Button)findViewById(R.id.btn_login);
		img_chat=(ImageView)findViewById(R.id.img_chat);
		txt_count_black=(TextView)findViewById(R.id.txt_count_black);
		txt_count_white=(TextView)findViewById(R.id.txt_count_white);
		txt_coin=(TextView)findViewById(R.id.txt_coin);
		pb=(ProgressBar)findViewById(R.id.pb);
		wR=(RelativeLayout)findViewById(R.id.root);
		wV=(WuziqiPanel)findViewById(R.id.action_settings);
		wO=new WuziqiPanel(MainActivity.this);
	}




	public static void setPbVisibility(int i){
		pb.setVisibility(i);
	}
	public static WuziqiPanel getwV() {
		return wV;
	}
	public static RelativeLayout getwR() {
		return wR;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Match.sendSomething("exit");
		super.onDestroy();
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private void exit(){
		if(System.currentTimeMillis()-mExitTime>2000){
			Toast.makeText(MainActivity.this, "再按一次退出五子棋", Toast.LENGTH_LONG).show();
			mExitTime=System.currentTimeMillis();
		}else{
			onDestroy();
			System.exit(0);
		}
	}
	
	public void getCoins(){
		SharedPreferences read=getSharedPreferences("config", MODE_PRIVATE);
		txt_coin.setText(Integer.toString(read.getInt("coin",0)));		
		
	}
	
	public static void setTextCoin(int coin){
		txt_coin.setText(Integer.toString(coin));
	}
	
	



	
	
}//MainActivity





	

