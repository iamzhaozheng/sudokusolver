package com.hisrv.android.sudokusolver;

import android.app.Activity;
import android.os.Bundle;

import com.hisrv.sudokusolver.R;

public class SolveActivity extends Activity {
	
	private SudokuView mSudokuView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_solve);
		initViews();
		showInputView();
	}
	
	private void initViews() {
		mSudokuView = (SudokuView) findViewById(R.id.sudoku_view);
		mSudokuView.setValue(0, 0, 4);
	}
	
	private void showInputView() {
		new InputDialog(this).show();
	}
}
