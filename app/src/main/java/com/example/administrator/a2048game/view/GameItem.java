package com.example.administrator.a2048game.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.a2048game.R;

/**
 * Created by Administrator on 2017/6/28.
 */

public class GameItem extends View {


	private int mNumber;    //数字
	private String mNumberVal;
	private Paint mPaint;

	private Rect mBound;



	public GameItem(Context context) {
		this(context,null);
	}

	public GameItem(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs,0);
	}

	public GameItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mPaint = new Paint();
	}

	public void setNumber(int number){
		mNumber = number;
		mNumberVal = ""+number;
		mPaint.setTextSize(30.0f);
		mBound = new Rect();
		mPaint.getTextBounds(mNumberVal, 0, mNumberVal.length(), mBound);
		invalidate();
	}

	public int getNumber(){
		return mNumber;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		switch (mNumber){
			case 0:
				mPaint.setColor(getResources().getColor(R.color.number0));
				break;
			case 2:
				mPaint.setColor(getResources().getColor(R.color.number2));
				break;
			case 4:
				mPaint.setColor(getResources().getColor(R.color.number4));
				break;
			case 8:
				mPaint.setColor(getResources().getColor(R.color.number8));
				break;
			case 16:
				mPaint.setColor(getResources().getColor(R.color.number16));
				break;
			case 32:
				mPaint.setColor(getResources().getColor(R.color.number32));
				break;
			case 64:
				mPaint.setColor(getResources().getColor(R.color.number64));
				break;
			case 128:
				mPaint.setColor(getResources().getColor(R.color.number128));
				break;
			case 256:
				mPaint.setColor(getResources().getColor(R.color.number256));
				break;
			case 512:
				mPaint.setColor(getResources().getColor(R.color.number512));
				break;
			case 1024:
				mPaint.setColor(getResources().getColor(R.color.number1024));
				break;
			case 2048:
				mPaint.setColor(getResources().getColor(R.color.number2048));
				break;
			default:
				mPaint.setColor(getResources().getColor(R.color.tacitly));
				break;
		}

		mPaint.setStyle(Paint.Style.FILL);
		canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);

		if(mNumber!=0){
			drawText(canvas);
		}

	}

	private void drawText(Canvas canvas) {
		mPaint.setColor(Color.BLACK);
		float x = (getWidth() - mBound.width()) / 2;
		float y = getHeight() / 2 + mBound.height() / 2;
		canvas.drawText(mNumberVal, x, y, mPaint);
	}
}
