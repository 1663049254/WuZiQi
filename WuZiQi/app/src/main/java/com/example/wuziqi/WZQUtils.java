package com.example.wuziqi;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import android.R.bool;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;
import server.Match;



public class WZQUtils extends WuziqiPanel{
	private static Button btn_ok;
	private static Button btn_cancel;
	private static Button btn_login;
	private static Button btn_register;
	private static TextView txt_content;
	private static EditText edt_user;
	private static EditText edt_pwd;
	private static ImageView img;
	public WZQUtils(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}
	protected static void init(){
		mPaint.setColor(Color.BLACK);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Paint.Style.FILL);
		
		mPaint2.setColor(Color.RED);
		mPaint2.setAntiAlias(true);
		mPaint2.setDither(true);
		mPaint2.setStyle(Paint.Style.FILL);
		
		/*mCurrentPoint.x=-1;
		mCurrentPoint.y=-1;*/		
		
	}
	protected static Point getVaLidPoint(int x, int y) {
		return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
	}
	protected static boolean getmIsWhite(){return mIsWithe;}
	protected static void playMusic(Context context){
		//播放音效
		/*media=MediaPlayer.create(context, R.raw.play);
		media.start();*/
	}
	protected static void checkGameOver() {
		boolean whiteWin =WZQUtils.checkFiveInLine(mWhitearry);
		boolean blickWin =WZQUtils. checkFiveInLine(mBlackarry);
		if (whiteWin || blickWin) {			
			/*mIsGamOver = true;
			mIsWhiteWinner = whiteWin;*/
			mIsGamOver=true;
			mIsWhiteWinner=whiteWin;
			//WuziqiPanel.setIsWitheWinner(whiteWin);
			DownCounter.cancelCounter();
			String str = mIsWhiteWinner ? "白棋胜利" : "黑棋胜利";
			boolean b=true;
			if(Match.isMatchSuccess()){
				boolean isBlack=Match.isBlack();
				if((isBlack && whiteWin==false) || (isBlack==false && whiteWin)){
					writeCoins(1);
				}
				
				
				if(isBlack && whiteWin){
					b=false;
				}else if(isBlack==false && blickWin){
					b=false;
				}
			}
			SystemClock.sleep(1000);
			showWinDialog(mContext,str,b);
			
		}
	}
	public static void writeCoins(int count){
		SharedPreferences write=mContext.getSharedPreferences("config",mContext.MODE_PRIVATE);
		Editor editor= write.edit();
		int coins=getCoins();
		editor.putInt("coin", coins+count);
		editor.commit();
		MainActivity.setTextCoin(coins+count);
	}
	public static int getCoins(){
		SharedPreferences read=mContext.getSharedPreferences("config", mContext.MODE_PRIVATE);
		return read.getInt("coin",0);
		
	}
	public static void writeBg(String bg){
		SharedPreferences write=mContext.getSharedPreferences("config",mContext.MODE_PRIVATE);
		Editor editor= write.edit();
		editor.putInt(bg, 1);
		editor.commit();

	}
	public static int getBg(String bg){
		SharedPreferences read=mContext.getSharedPreferences("config", mContext.MODE_PRIVATE);
		return read.getInt(bg,0);
	}

	public static void showWinDialog(Context context,String content,boolean win){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		LayoutInflater inflater=LayoutInflater.from(context);			
		View v=inflater.inflate(R.layout.win_dialog, null);
		final Dialog dialog=builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		Window window=dialog.getWindow();			
		window.setContentView(v);
		if(win==false){
			img=(ImageView)window.findViewById(R.id.img_win);
			img.setImageResource(R.drawable.lose);
		}
		txt_content=(TextView)window.findViewById(R.id.txt_content);
		btn_ok=(Button) window.findViewById(R.id.btn_ok);
		btn_cancel=(Button) window.findViewById(R.id.btn_cancel);
		txt_content.setText(content);
		
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub		
				if(Match.isMatchSuccess() && mIsGamOver){
					Match.sendSomething("continue");
				}
				dialog.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(Match.isMatchSuccess()){
					Match.setMatchSuccess(false);
					Match.sendSomething("leave");
				}
				dialog.dismiss();
			}
		});
	}
	protected static void showLoginDialog(Context context){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		LayoutInflater inflater=LayoutInflater.from(context);			
		View v=inflater.inflate(R.layout.login, null);		
		final Dialog dialog=builder.create();
		dialog.setCanceledOnTouchOutside(false);			
		dialog.show();
		Window window=dialog.getWindow();
		window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM); 
		//弹出输入法
		//window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		window.setContentView(v);
		btn_login=(Button) window.findViewById(R.id.btn_login);
		btn_register=(Button) window.findViewById(R.id.btn_register);
		edt_user=(EditText) window.findViewById(R.id.edt_user);
		edt_pwd=(EditText) window.findViewById(R.id.edt_pwd);
		
		
		btn_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub						
				/*String user=edt_user.getText().toString();
				String pwd=edt_pwd.getText().toString();
				
				Register reg=new Register();
				if(reg.checkUP(user, pwd))
					reg.sendRegister(user, pwd);*/		
				dialog.dismiss();
			}
		});
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}
	protected static void drawBoard(Canvas canvas) {
		int w = mPanelWidth;
		float lineHeight = mLineHeight;
		//Log.e("","drawboard");
		for (int i = 0; i < MAX_LINE; i++) {
			int startX = (int) (lineHeight / 2);
			int endX = (int) (w - lineHeight / 2);
			
			int y = (int) ((i+0.5) * lineHeight);
			//Log.e("a",w+","+lineHeight+","+endX);
			
			//Log.e("a",startX+","+y+"  "+endX+","+y);
			canvas.drawLine(startX, y, endX, y, mPaint);
			canvas.drawLine(y, startX, y, endX, mPaint);
			
		}
		
		canvas.drawCircle(3.5f*lineHeight, 3.5f*lineHeight , lineHeight/6, mPaint);
		canvas.drawCircle(11.5f*lineHeight, 3.5f*lineHeight , lineHeight/6, mPaint);
		canvas.drawCircle(7.5f*lineHeight, 7.5f*lineHeight , lineHeight/6, mPaint);
		canvas.drawCircle(3.5f*lineHeight, 11.5f*lineHeight , lineHeight/6, mPaint);
		canvas.drawCircle(11.5f*lineHeight, 11.5f*lineHeight , lineHeight/6, mPaint);
			
	}
	
	protected static void drawPicec(Canvas canvas) {
		
		for (int i = 0, n = mWhitearry.size(); i < n; i++) {
			Point whitePoint = mWhitearry.get(i);
			canvas.drawBitmap(mWhitePiece, (whitePoint.x + (1 - ratioPieceOfLineHight)/2 ) * mLineHeight,
					(whitePoint.y + (1 - ratioPieceOfLineHight)/2) * mLineHeight, null);
		}		
		
		for (int i = 0, n = mBlackarry.size(); i < n; i++) {
			Point blackPoint = mBlackarry.get(i);
			//Log.d("point",blackPoint.x+","+blackPoint.y);
			canvas.drawBitmap(mBlackPiece, (blackPoint.x + (1 - ratioPieceOfLineHight) / 2) * mLineHeight,
					(blackPoint.y + (1 - ratioPieceOfLineHight) / 2) * mLineHeight, null);
		}
		
		if(mCurrentPoint.x>=0){
			
			canvas.drawCircle((mCurrentPoint.x+0.5f) *mLineHeight, (mCurrentPoint.y+0.5f)*mLineHeight, mLineHeight/8, mPaint2);
		}
					
		
	}
	
	protected static void drawLigature(Point start,Point stop,Canvas canvas){
		Paint paint=new Paint();
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.FILL);
		//Log.e("ligature",start.x+","+start.y+" "+stop.x+","+stop.y);
		canvas.drawLine((start.x+0.5f)*mLineHeight,(start.y+0.5f)*mLineHeight,( stop.x+0.5f)*mLineHeight,( stop.y+0.5f)*mLineHeight, mPaint2);				
		
	}
	
	protected static boolean checkFiveInLine(List<Point> arr) {
		for (Point p : arr) {
			int x = p.x;
			int y = p.y;
			

			boolean win =WZQUtils.checkHorizontal(x, y, arr);
			if (win) return true;
			win = WZQUtils.checkVertical(x, y, arr);
			if (win) return true;
			win = WZQUtils.checkRightDiagonl(x, y,arr);
			if (win) return true;
			win = WZQUtils.checkLeftDiagonal(x, y,arr);
			if (win) return true;
		}
		return false;
	}
	
	protected static boolean checkHorizontal(int x, int y, List<Point> arr) {
		
		int count = 1;		
		Ligature ligature=arr==mWhitearry?mWhiteLigature:mBlackLigature;
		
		for (int i = 1; i < 5; i++) {
			if (arr.contains(new Point(x-i,y))) {
				count++;				
				ligature.startPoint.x=x-i;
				ligature.startPoint.y=y;
				
				if(i==4){
					ligature.endPoint.x=x;
					ligature.endPoint.y=y;
				}					
			}else {
				break;
			}
			if (count==5) return true;
		}
		
		for (int i = 1; i < 5; i++) {
			if (arr.contains(new Point(x+i,y))) {
				count++;				
				ligature.endPoint.x=x+i;
				ligature.endPoint.y=y;
				if(i==4){
					ligature.startPoint.x=x;
					ligature.startPoint.y=y;
				}
			
			}else {
				break;
			}
			if (count==5) return true;
		}
		return false;
	}
	protected static boolean checkVertical(int x, int y, List<Point> arr) {
		int count = 1;
		Ligature ligature=arr==mWhitearry?mWhiteLigature:mBlackLigature;
		for (int i = 1; i < 5; i++) {
			if (arr.contains(new Point(x,y-i))) {
				count++;
				ligature.startPoint.x=x;
				ligature.startPoint.y=y-i;
				if(i==4){
					ligature.endPoint.x=x;
					ligature.endPoint.y=y;
				}
			}else {break;}
			if (count==5) return true;
		}
		
		for (int i = 1; i < 5; i++) {
			if (arr.contains(new Point(x,y+i))) {
				count++;
				ligature.endPoint.x=x;
				ligature.endPoint.y=y+i;
				if(i==4){
					ligature.startPoint.x=x;
					ligature.startPoint.y=y;
				}
			}else {break;}
			if (count==5) return true;
		}
		return false;
	}
	protected static boolean checkRightDiagonl(int x, int y, List<Point> arr) {
		int count = 1;
		Ligature ligature=arr==mWhitearry?mWhiteLigature:mBlackLigature;
		for (int i = 1; i < 5; i++) {
			if (arr.contains(new Point(x-i,y-i))) {				
				count++;
				ligature.startPoint.x=x-i;
				ligature.startPoint.y=y-i;
				//Log.v("count",count+"");
				//Log.v("i",i+"");	
				if(i==4){
					ligature.endPoint.x=x;
					ligature.endPoint.y=y;
				}
			}else {break;}
			
			if (count==5) {
				return true;
			}
		}
		
		for (int i = 1; i < 5; i++) {
			if (arr.contains(new Point(x+i,y+i))) {
				count++;
				ligature.endPoint.x=x+i;
				ligature.endPoint.y=y+i;
				if(i==4){
					
					ligature.startPoint.x=x;
					ligature.startPoint.y=y;
					//Log.v("startx",ligature.startPoint.x+"");
				}
			}else {break;}
			if (count==5) return true;
		}
		return false;
	}
	protected static boolean checkLeftDiagonal(int x, int y, List<Point> arr) {
		int count = 1;
		
		Ligature ligature=arr==mWhitearry?mWhiteLigature:mBlackLigature;
		
			
	
		for (int i = 1; i < 5; i++) {
			if (arr.contains(new Point(x-i,y+i))) {				
				count++;
				ligature.startPoint.x=x-i;
				ligature.startPoint.y=y+i;
				if(i==4){
					ligature.endPoint.x=x;
					ligature.endPoint.y=y;
				}
			}else {break;}
			if (count==5) return true;
		}
		
		for (int i = 1; i < 5; i++) {
			if (arr.contains(new Point(x+i,y-i))) {
				count++;
				ligature.endPoint.x=x+i;
				ligature.endPoint.y=y-i;
				if(i==4){
					ligature.startPoint.x=x;
					ligature.startPoint.y=y;
				}
			}else {break;}
			if (count==5) return true;
		}
		return false;
	}
	public static void showAlertDialog(Context context,String title,String content){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle(title)
		.setMessage(content)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		builder.create().show();
		
	}
	public static void clearStone(){
		mWhitearry.clear();
		mBlackarry.clear();
		
	}
	public static void clearCurrentPoint() {
		mCurrentPoint.x=-1;
		mCurrentPoint.y=-1;	
	}
	public static void addWhiteArray(Point p){
		if(mWhitearry.contains(p)==false){
			mWhitearry.add(p);
			mCurrentPoint.x=p.x;
			mCurrentPoint.y=p.y;
		}
	}
	public static void addBlackArray(Point p){
		if(mBlackarry.contains(p)==false){
			mBlackarry.add(p);
			mCurrentPoint.x=p.x;
			mCurrentPoint.y=p.y;
		}
	}
	public static void setmIsGamOver(boolean b) {
		mIsGamOver = b;
	}

}
