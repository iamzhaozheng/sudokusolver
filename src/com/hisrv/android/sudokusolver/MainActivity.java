package com.hisrv.android.sudokusolver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.hisrv.sudokusolver.R;

public class MainActivity extends Activity {
	
	private SudokuView mSudokuView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		showInputView();
	}
	
	private void initViews() {
		mSudokuView = (SudokuView) findViewById(R.id.sudoku_view);
		mSudokuView.setValue(0, 0, 4);
	}
	
	private void showInputView() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.input_group, null);
		new AlertDialog.Builder(this).setView(v).setPositiveButton(R.string.ok, new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).setNegativeButton(R.string.cancel, null).create().show();
	}
}
