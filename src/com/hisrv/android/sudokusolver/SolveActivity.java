package com.hisrv.android.sudokusolver;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.hisrv.android.sudokusolver.InputDialog.OnBoardFilledListener;
import com.hisrv.sudokusolver.R;

public class SolveActivity extends Activity {
	
	private SudokuView mSudokuView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_solve);
		initViews();
	}
	
	private void initViews() {
		mSudokuView = (SudokuView) findViewById(R.id.sudoku_view);
	}
	
	private void showInputView() {
		new InputDialog(this).setOnBoardFilledListener(new OnBoardFilledListener() {
			
			public void onBoardFilled(char[][] board) {
				// TODO Auto-generated method stub
				mSudokuView.setValues(board);
			}
		}).show();
		
	}
	
	public void onBtnClick(View v) {
		switch (v.getId()) {
		case R.id.btn_input:
			showInputView();
			break;
		case R.id.btn_solve:
			new SudokuSolver().solveSudoku(mSudokuView.getValues());
			mSudokuView.invalidate();
			break;
		}
	}
}
