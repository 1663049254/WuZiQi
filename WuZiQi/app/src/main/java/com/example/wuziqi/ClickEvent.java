package com.example.wuziqi;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import server.Match;


public class ClickEvent extends MainActivity{
	private static Context mContext;	
	/*public static void setContext(Context context){
		mContext=context;
	}*/
	public ClickEvent(Context ctx){
		this.mContext=ctx;
	}
	public OnClickListener restarClick=new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			DownCounter.cancelCounter();
			//wO.clearStones();	
			if(Match.isMatchSuccess() && WZQUtils.mIsGamOver==false){
				WZQUtils.showAlertDialog(mContext, "你正在游戏中", "当前无法重新开始");
				return;
			}
			WZQUtils.clearStone();
			WZQUtils.clearCurrentPoint();
			WZQUtils.setmIsGamOver(false);
			WZQUtils.setmIsWithe(false);
			//wV.setBackgroundColor(Color.BLACK);
			wV.invalidate();
		}
	};
		
	public OnClickListener changeStoneClick =new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder=new Builder(mContext);
			builder.setTitle("选择棋子");
			String[] options={"默认","校园"};
			builder.setItems(options, clickItem);
			Dialog dialog=builder.create();
			dialog.show();
						
			
		}
		DialogInterface.OnClickListener clickItem=new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int id) {
				// TODO Auto-generated method stub
				switch(id){
					case 0:
						wO.loadStoneResource(R.drawable.stone_w1,R.drawable.stone_b1);							
						wO.setStoneSize();						
						break;
					case 1:
						wO.loadStoneResource(R.drawable.stone_w2,R.drawable.stone_b2);						
						wO.setStoneSize();								
						break;

					default:						
						break;
				}
				wV.invalidate();
								
			}
		};
		
	};//class ChangeStoneClick
	public OnClickListener changeBgClick =new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			/*String path=Environment.getExternalStorageDirectory()+"/wzqbg.jpg";		
			Log.v("path",path);
			File file=new File(path);
			Bitmap bitmap;
			try {
				bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
				BitmapDrawable drawable=new BitmapDrawable(getResources(),bitmap);
				//wR.setBackgroundResource(R.drawable.bg2);
				wR.setBackgroundDrawable(drawable);;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			AlertDialog.Builder builder=new Builder(mContext);
			builder.setTitle("选择背景");
			String[] options={"默认","垂柳","墨竹","白鹤","游鱼","七夕"};
			builder.setItems(options, clickItem);
			Dialog dialog=builder.create();
			dialog.show();
						
		
		}
		DialogInterface.OnClickListener clickItem=new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int id) {
				// TODO Auto-generated method stub
				SharedPreferences read=mContext.getSharedPreferences("config", MODE_PRIVATE);
				
				switch(id){
					case 0:
						wR.setBackgroundResource(R.drawable.bg);				
						break;
					case 1:
						if(read.getInt("willow", 0)==1)
							wR.setBackgroundResource(R.drawable.bg2);
						else
							WZQUtils.showAlertDialog(mContext,"提示","该背景未解锁");
						break;
					case 2:
						if(read.getInt("bamboo", 0)==1)
							wR.setBackgroundResource(R.drawable.bg3);
						else
							WZQUtils.showAlertDialog(mContext,"提示","该背景未解锁");
						break;
					case 3:
						if(read.getInt("crane", 0)==1)
							wR.setBackgroundResource(R.drawable.bg4);
						else
							WZQUtils.showAlertDialog(mContext,"提示","该背景未解锁");
						break;
					case 4:
						if(read.getInt("fish", 0)==1)
							wR.setBackgroundResource(R.drawable.bg5);
						else
							WZQUtils.showAlertDialog(mContext,"提示","该背景未解锁");
						break;
					case 5:
						if(read.getInt("lovers", 0)==1)
							wR.setBackgroundResource(R.drawable.bg6);
						else
							WZQUtils.showAlertDialog(mContext,"提示","该背景未解锁");
						break;
					default:						
						break;
				}
				wV.invalidate();
								
			}
		};
		
	};
	/*private void purchaseBg(final String bg){
		if(WZQUtils.getCoins()>=30){
			AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
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
					SharedPreferences write=mContext.getSharedPreferences("config",MODE_PRIVATE);
					Editor editor=write.edit();
					editor.putInt(bg, 1);
					editor.commit();
					if(bg.equals("willow"))
						wR.setBackgroundResource(R.drawable.bg2);
					else if(bg.equals("bamboo"))
						wR.setBackgroundResource(R.drawable.bg3);
					else if(bg.equals("crane"))
						wR.setBackgroundResource(R.drawable.bg4);
					else if(bg.equals("fish"))
						wR.setBackgroundResource(R.drawable.bg5);
					else if(bg.equals("loveres"))
						wR.setBackgroundResource(R.drawable.bg6);
					}
				}
			);
			builder.create().show();
			
		}else{
			WZQUtils.showAlertDialog(mContext, "积分不足","解锁此背景需要30积分" );
		}
	}*/
	public OnClickListener changeWoodClick =new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			if(btn_change_wood.getText().toString().equals("显示棋盘")){
				//System.out.println("显示棋盘");
				wV.setBackgroundColor(Color.parseColor("#ebc27e"));
				btn_change_wood.setText("隐藏棋盘");
			}else if(btn_change_wood.getText().toString().equals("隐藏棋盘")){
				//System.out.println("隐藏棋盘");
				wV.setBackgroundColor(0);
				btn_change_wood.setText("显示棋盘");
			}

		}
		
	};
	public OnClickListener matchClick =new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//System.out.println("读取数据");
			SharedPreferences read=mContext.getSharedPreferences("config", MODE_PRIVATE);
			final String ip=read.getString("serverip","");
			System.out.println(ip);
			//让用户输入服务器地址
			
			final EditText et=new EditText(mContext);
			if(ip!="") et.setText(ip);
			AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
			builder.setTitle("请输入服务器ip")
			.setView(et)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					String str=et.getText().toString();					
					if(str!="" && str.equals(ip)==false){
						//System.out.println("写数据");
						SharedPreferences write=mContext.getSharedPreferences("config",MODE_PRIVATE);
						Editor editor=write.edit();
						editor.putString("serverip", et.getText().toString());
						editor.commit();
					}
					Match.startMatch(et.getText().toString());
				}
			});
			builder.create().show();

		}
		
	};
	

	/*public OnClickListener loginClick =new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//WZQUtils.showLoginDialog(mContext);			
		}
		
	};*/
	public OnClickListener chatClick =new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder=new Builder(mContext);
			builder.setTitle("聊天");
			String[] options={"阁下，神速些吧","稍等片刻，容我思量思量","不要走，决战到天亮","你的棋下得也太好了","一着不慎，满盘皆输","落子无悔大丈夫","棋逢对手，将遇良才，痛快，痛快","真抱歉，我有急事要离开"};
			builder.setItems(options, clickItem);
			Dialog dialog=builder.create();
			dialog.show();
			
		}
		DialogInterface.OnClickListener clickItem=new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				if(Match.isMatchSuccess()){					
					//Match.sendChat("chat"+arg1);
					Match.sendSomething("chat"+arg1);
				}
			}
		};
		
	};
	public OnClickListener shopClick=new OnClickListener() {
		@Override
		public void onClick(View v) {
			System.out.println("启动activity");
			//System.out.println(mContext);
            Intent intent=new Intent();
            intent.setClass(mContext,ViewPagerActivity.class);
            mContext.startActivity(intent);
		}
	};
	public void bindClickEvent(){
		btn_restart.setOnClickListener(restarClick);			
		btn_change_stone.setOnClickListener(changeStoneClick);			
		btn_change_bg.setOnClickListener(changeBgClick);	
		btn_change_wood.setOnClickListener(changeWoodClick);
		btn_shop.setOnClickListener(shopClick);
		btn_match.setOnClickListener(matchClick);	
		//btn_login.setOnClickListener(loginClick);	
		img_chat.setOnClickListener(chatClick);	
	}
}
