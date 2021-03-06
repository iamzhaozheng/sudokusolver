package com.hisrv.android.sudokusolver;

import java.util.Arrays;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hisrv.sudokusolver.R;

public class InputDialog extends Dialog {

	private static final int SIZE = 9;
	private static final int CHECK_OK = -1;
	private static final int CHECK_UNKNOWN = -2;
	private LinearLayout mGroup;
	private EditText[] mEditTexts = new EditText[SIZE];
	private OnBoardFilledListener mOnBoardFilledListener;
	private char[][] mBoard;

	public InputDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.input_group);
		mGroup = (LinearLayout) findViewById(R.id.group);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < SIZE; i ++) {
			View v = inflater.inflate(R.layout.item_input, null);
			TextView tv = (TextView) v.findViewById(R.id.label);
			tv.setText(context.getString(R.string.line, i + 1));
			EditText et = (EditText) v.findViewById(R.id.edit);
			mEditTexts[i] = et;
			mGroup.addView(v);
		}
		findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int line = checkValid();
				if (line == CHECK_OK) {
					if (mOnBoardFilledListener != null) {
						mOnBoardFilledListener.onBoardFilled(mBoard);
					}
					mOnBoardFilledListener = null;
					dismiss();
				} else {
					mEditTexts[line].startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.edit_invalid));
				}
			}
		});
		findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}

	private int checkValid() {
		mBoard = new char[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			if (mEditTexts[i].getText().length() != SIZE) {
				return i;
			}
			for (int j = 0; j < SIZE; j++) {
				mBoard[i][j] = mEditTexts[i].getText().charAt(j);
			}
		}
		int line = getInvalidSudokuLine(mBoard);
		return line;
	}

	public int getInvalidSudokuLine(char[][] board) {
		// Start typing your Java solution below
		// DO NOT write main() function
		int[] row = new int[9];
		int[] col = new int[9];
		int[] cell = new int[9];
		Arrays.fill(row, 0);
		Arrays.fill(col, 0);
		Arrays.fill(cell, 0);
		for (int i = 0; i < 81; i++) {
			int x = i % 9;
			int y = i / 9;
			int c = y / 3 * 3 + x / 3;
			if (board[y][x] == '.') {
			} else if (board[y][x] < '1' || board[y][x] > '9') {
				return y;
			} else {
				int t = board[y][x] - '1';
				t = 1 << t;
				if ((row[y] & t) != 0 || (col[x] & t) != 0
						|| (cell[c] & t) != 0) {
					return y;
				}
				row[y] |= t;
				col[x] |= t;
				cell[c] |= t;
			}
		}
		return CHECK_OK;
	}
	
	public InputDialog setOnBoardFilledListener(OnBoardFilledListener l) {
		mOnBoardFilledListener = l;
		return this;
	}
	
	public interface OnBoardFilledListener {
		public void onBoardFilled(char[][] board);
	}

}
