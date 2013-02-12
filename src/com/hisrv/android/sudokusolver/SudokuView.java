package com.hisrv.android.sudokusolver;

import java.util.Arrays;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class SudokuView extends View {

	private final static int GRID_NUM = 9;
	private Paint mLinePaint, mTextPaint;
	private char[][] mTable;
	private int mBorder = 0, mStart = 0, mEnd = 0, mGridWidth;
	private double mTextSizeRatio;
	
	public SudokuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mTable = new char[GRID_NUM][GRID_NUM];
		for (int i = 0; i < GRID_NUM; i ++) {
			Arrays.fill(mTable[i], '.');
		}
		mTextSizeRatio = 3f / 4;
		initPaints();
	}
	
	private void initPaints() {
		mLinePaint = new Paint();
		mLinePaint.setStyle(Style.FILL_AND_STROKE);
		mTextPaint = new Paint();
		mTextPaint.setTextAlign(Align.CENTER);
	}
	
	public boolean setValue(int x, int y, char value) {
		mTable[y][x] = value;
		return true;
	}
	
	public void setValues(char[][] board) {
		mTable = board;
		invalidate();
	}
	
	public char[][] getValues() {
		return mTable;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawLines(canvas);
		drawNumbers(canvas);
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mode = MeasureSpec.getMode(heightMeasureSpec);
		if (mode == MeasureSpec.EXACTLY) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		} else {
			int size = MeasureSpec.getSize(widthMeasureSpec);
			setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(size, mode));
			super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(size, mode));
		}
	}
	
	private void drawNumbers(Canvas canvas) {
		mTextPaint.setTextSize((int)(mGridWidth * mTextSizeRatio));
		for (int i = 0; i < GRID_NUM; i ++) {
			for (int j = 0; j < GRID_NUM; j ++) {
				if (mTable[i][j] >= '1' && mTable[i][j] <= '9') {
					int x = mStart + j * mGridWidth + mGridWidth / 2;
					int y = mStart + i * mGridWidth + (int)(mGridWidth * mTextSizeRatio);
					canvas.drawText(String.valueOf(mTable[i][j]), x, y, mTextPaint);
				}
			}
		}
	}
	
	private void drawLines(Canvas canvas) {
		int w = getWidth();
		mBorder = w / 20;
		mStart = mBorder;
		mEnd = w - mBorder;
		mGridWidth = (mEnd - mStart) / GRID_NUM;
		for (int i = 0; i <= GRID_NUM; i ++) {
			if (i % 3 == 0) {
				mLinePaint.setStrokeWidth(3);
			} else {
				mLinePaint.setStrokeWidth(0);
			}
			int pos = mStart + i * mGridWidth;
			canvas.drawLine(mStart, pos, mEnd, pos, mLinePaint);
			canvas.drawLine(pos, mStart, pos, mEnd, mLinePaint);
		}
	}
}
