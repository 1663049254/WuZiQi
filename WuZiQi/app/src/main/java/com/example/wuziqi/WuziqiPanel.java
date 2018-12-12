package com.example.wuziqi;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;



import android.R.bool;
import android.R.color;
import android.R.raw;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.MediaPlayer;
import android.os.CancellationSignal;
import android.os.CountDownTimer;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import server.Match;

public class WuziqiPanel extends View {
	protected static int mPanelWidth;
	protected int mPanelHeight;
	protected static float mLineHeight;
	
	protected static int MAX_LINE = 15;

	protected static Paint mPaint = new Paint();
	protected static Paint mPaint2 = new Paint();
	protected static Bitmap mWhitePiece;
	protected static Bitmap mBlackPiece;
	protected static float ratioPieceOfLineHight = 3 * 1.0f / 4;//0.75

	protected static boolean mIsWithe = false;
	protected static Point mCurrentPoint=new Point(-1,-1);
	protected static MediaPlayer media;
	
	
	protected static List<Point> mWhitearry = new ArrayList<Point>();
	protected static List<Point> mBlackarry = new ArrayList<Point>();
		
	
	protected static boolean mIsGamOver=false;
	protected static boolean mIsWhiteWinner;
	
	protected static Ligature mWhiteLigature=new Ligature();
	protected static Ligature mBlackLigature=new Ligature();
	protected static Context mContext;
	private static int mPiceWidth=0;
	private CountDownTimer timer;
	/*private TextView txt_count_white;
	private TextView txt_count_black;*/
	/*protected static void setmIsGamOver(boolean mIsGamOver) {
		WuziqiPanel.mIsGamOver = mIsGamOver;
	}*/
	protected static void setmIsWithe(boolean mIsWithe) {
		WuziqiPanel.mIsWithe = mIsWithe;
	}
	protected static void setIsWitheWinner(boolean mIsWitheWinner) {
		WuziqiPanel.mIsWhiteWinner = mIsWitheWinner;
	}
	/*static boolean getmIsWithe() {
		return mIsWithe;
	}*/
	/*private Button btn_ok;
	private Button btn_cancel;
	private TextView txt_content;*/
	
	public WuziqiPanel(Context context, AttributeSet attrs) {
		super(context, attrs);	
		mContext=context;
		WZQUtils.init();
		loadStoneResource(R.drawable.stone_w1, R.drawable.stone_b1);
		
		//downCounter();
		//Log.e("","structor");
	}
	public WuziqiPanel(Context context) {
		super(context);		
		//Log.v("a","hello");
		
	}	
	
	void loadStoneResource(int white_id,int black_id){
		//Log.e("loadstoneresource",black_id+"");
		mWhitePiece = BitmapFactory.decodeResource(getResources(), white_id);
		mBlackPiece = BitmapFactory.decodeResource(getResources(), black_id);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mIsGamOver) {
			return false;
		}
		boolean matchSuccess=Match.isMatchSuccess();
		if(matchSuccess && Match.isMePlay()==false){
			return false;
		}
		
		int action = event.getAction();
		if (action == MotionEvent.ACTION_UP) {
			int x = (int) event.getX();
			int y = (int) event.getY();			
			Point p = WZQUtils.getVaLidPoint(x, y);
			
			if (mWhitearry.contains(p) || mBlackarry.contains(p)) {
				return false;
			}
			mCurrentPoint.x=p.x;
			mCurrentPoint.y=p.y;
			if(matchSuccess){
				if(Match.isBlack())
					mBlackarry.add(p);
				else 
					mWhitearry.add(p);				
				//Match.sendPoint(p);				
				Match.sendSomething(p.x+","+p.y);
				Match.setMePlay(false);
			}else{			
				if (mIsWithe) {
					mWhitearry.add(p);
				} else {
					mBlackarry.add(p);
				}
				mIsWithe = !mIsWithe;
				
			}
			DownCounter.cancelCounter();
			DownCounter.startCounter();	
			invalidate();
		}

		
	return true;
		
}

	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		int heighSize = MeasureSpec.getSize(heightMeasureSpec);
		int heighMode = MeasureSpec.getMode(heightMeasureSpec);

		int width = Math.min(widthSize, heighSize);

		if (widthMode == MeasureSpec.UNSPECIFIED) {
			width = heighSize;
		} else if (heighMode == MeasureSpec.UNSPECIFIED) {
			width = widthSize;
		}
		
		setMeasuredDimension(width, width);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mPanelWidth = w;
		mPanelHeight=h;
		mLineHeight = mPanelWidth * 1.0f / MAX_LINE;
		mPiceWidth = (int) (mLineHeight * ratioPieceOfLineHight);
		setStoneSize();
		/*mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, mPiceWidth , mPiceWidth , false);
		mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, mPiceWidth , mPiceWidth , false);*/
	}
	void setStoneSize(){
		mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, mPiceWidth, mPiceWidth, false);
		mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, mPiceWidth, mPiceWidth, false);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//Log.e("硬件加速",canvas.isHardwareAccelerated()+"");
		//Log.i("arr",mWhitearry.size()+"");
		
		//Log.e("调用","ondraw");
		super.onDraw(canvas);		
		WZQUtils.drawBoard(canvas);
		WZQUtils.drawPicec(canvas);	

		
		if(mIsGamOver==false){		
			
			WZQUtils.checkGameOver();
		}
		if(mIsGamOver){
			if(mIsWhiteWinner){
				WZQUtils.drawLigature(mWhiteLigature.startPoint,mWhiteLigature.endPoint, canvas);
			}
			else {
				WZQUtils.drawLigature(mBlackLigature.startPoint,mBlackLigature.endPoint, canvas);
			}					
			//mIsGamOver=false;		
		}
		
		
	}

	
	public static Context getmContext(){
		return mContext;
	}
	
	
}
