package com.example.administrator.a2048game.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/6/29.
 */

public class GameView extends RelativeLayout {

	private int mColumn = 5;    //设置item
	private int mPadding;
	private int mMargin = 10;
	private GameItem[] mGameItems;
	private GestureDetector mGestureDetector;   //检测用户滑动的手势
	// 用于确认是否需要生成一个新的值
	private boolean isMergeHappen = true;
	private boolean isMoveHappen = true;
	private int mScore;     //分数
	private boolean once;

	public interface GameListener {
		void onScoreChange(int score);

	}

	private GameListener mGameListener;

	public void setGameListener(GameListener mGameListener) {
		this.mGameListener = mGameListener;
	}

	private enum ACTION {
		LEFT, RIGHT, UP, DOWM
	}

	public GameView(Context context) {
		this(context, null);
	}

	public GameView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				mMargin, getResources().getDisplayMetrics());
		// 设置Layout的内边距，四边一致，设置为四内边距中的最小值
		mPadding = min(getPaddingLeft(), getPaddingTop(), getPaddingRight(),
				getPaddingBottom());

		mGestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {

			int FLING_MIN_DISTANCE = 50;

			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {

			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {

			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				float x = e2.getX() - e1.getX();
				float y = e2.getY() - e1.getY();

				if (x > FLING_MIN_DISTANCE && Math.abs(velocityX) > Math.abs(velocityY)) {
					action(ACTION.RIGHT);
				} else if (x < -FLING_MIN_DISTANCE
						&& Math.abs(velocityX) > Math.abs(velocityY)) {
					action(ACTION.LEFT);
				} else if (y > FLING_MIN_DISTANCE && Math.abs(velocityY) > Math.abs(velocityX)) {
					action(ACTION.DOWM);
				} else if (y < -FLING_MIN_DISTANCE
						&& Math.abs(velocityX) < Math.abs(velocityY)) {
					action(ACTION.UP);
				}
				return true;
			}
		});
	}

	/**
	 * @param action 根据用户运动，整体进行移动合并值等
	 */
	private void action(ACTION action) {
		//行
		for (int i = 0; i < mColumn; i++) {
			//记录不为0的数字
			List<GameItem> no_zero = new ArrayList<GameItem>();
			for (int j = 0; j < mColumn; j++) {
				//得到下标
				int index = getIndexByAction(action, i, j);
				GameItem item = mGameItems[index];
				// 记录不为0的数字
				if (item.getNumber() != 0) {
					no_zero.add(item);
				}
			}
			//列
			for (int j = 0; j < mColumn && j < no_zero.size(); j++) {
				int index = getIndexByAction(action, i, j);
				GameItem item = mGameItems[index];
				if (item.getNumber() != no_zero.get(j).getNumber()) {
					isMoveHappen = true;
				}
			}
			// 合并相同的数字
			mergeItem(no_zero);
			// 设置合并后的值
			for (int j = 0; j < mColumn; j++) {
				int index = getIndexByAction(action, i, j);
				if (no_zero.size() > j) {
					mGameItems[index].setNumber(no_zero.get(j).getNumber());
				} else {
					mGameItems[index].setNumber(0);
				}
			}
			//生成数字
			generateNum();
		}
	}

	private void mergeItem(List<GameItem> no_zero) {
		if (no_zero.size() < 2)
			return;
		for (int j = 0; j < no_zero.size() - 1; j++) {
			GameItem item1 = no_zero.get(j);
			GameItem item2 = no_zero.get(j + 1);
			if (item1.getNumber() == item2.getNumber()) {
				isMergeHappen = true;
				int var = item1.getNumber() + item2.getNumber();
				item1.setNumber(var);
				// 加分
				mScore += var;
				if (mGameListener != null) {
					mGameListener.onScoreChange(mScore);
				}
				// 向前移动
				for (int k = j + 1; k < no_zero.size() - 1; k++) {
					no_zero.get(k).setNumber(no_zero.get(k + 1).getNumber());
				}
				no_zero.get(no_zero.size() - 1).setNumber(0);
				return;
			}

		}
	}


	/**
	 * 得到多值中的最小值
	 *
	 * @param params
	 * @return
	 */
	private int min(int... params) {
		int min = params[0];
		for (int param : params) {
			if (min > param) {
				min = param;
			}
		}
		return min;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int length = Math.min(getMeasuredHeight(), getMeasuredWidth());
		int childWidth = (length - mPadding * 2 - mMargin * (mColumn - 1))
				/ mColumn;
		if (!once) {
			if (mGameItems == null) {
				mGameItems = new GameItem[mColumn * mColumn];
			}
			Log.e("aaaaaa", "onMeasure: " + mGameItems.length);
			for (int i = 0; i < mGameItems.length; i++) {
				GameItem gameItem = new GameItem(getContext());
				mGameItems[i] = gameItem;
				gameItem.setId(i + 1);
				RelativeLayout.LayoutParams layoutParams = new LayoutParams(childWidth,
						childWidth);
				// 设置横向边距,不是最后一列
				if ((i + 1) % mColumn != 0) {
					layoutParams.rightMargin = mMargin;
				}
				// 如果不是第一列
				if (i % mColumn != 0) {
					layoutParams.addRule(RelativeLayout.RIGHT_OF,//
							mGameItems[i - 1].getId());
				}

				// 如果不是第一行，//设置纵向边距，非最后一行
				if ((i + 1) > mColumn) {
					layoutParams.topMargin = mMargin;
					layoutParams.addRule(RelativeLayout.BELOW,
							mGameItems[i - mColumn].getId());
				}
				addView(gameItem, layoutParams);
			}
			generateNum(); //生成随机数据
		}
		once = true;
		setMeasuredDimension(length, length);
	}

	private boolean isFull() {
		// 检测是否所有位置都有数字
		for (int i = 0; i < mGameItems.length; i++) {
			if (mGameItems[i].getNumber() == 0) {
				return false;
			}
		}
		return true;
	}

	private void generateNum() {
		if (!isFull()) {
			if (isMoveHappen || isMergeHappen) {
				Random random = new Random();
				int next = random.nextInt(16);
				GameItem item = mGameItems[next];

				while (item.getNumber() != 0) {
					next = random.nextInt(16);
					item = mGameItems[next];
				}
				item.setNumber(Math.random() > 0.75 ? 4 : 2);

				isMergeHappen = isMoveHappen = false;
			}
		}
	}

	private int getIndexByAction(ACTION action, int i, int j) {
		int index = -1;
		switch (action) {
			case LEFT:
				index = i * mColumn + j;
				break;
			case RIGHT:
				index = i * mColumn + mColumn - j - 1;
				break;
			case UP:
				index = i + j * mColumn;
				break;
			case DOWM:
				index = i + (mColumn - 1 - j) * mColumn;
				break;
		}
		return index;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return true;
	}


}
