package com.hisrv.android.sudokusolver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hisrv.sudokusolver.R;

public class WelcomeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
	}

	public void onBtnClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_solve:
			startActivity(new Intent(this, SolveActivity.class));
			break;
		case R.id.btn_game:
			break;
		}
	}
}
