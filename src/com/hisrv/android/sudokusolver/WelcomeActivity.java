package com.hisrv.android.sudokusolver;

import java.io.IOException;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hisrv.sudokusolver.R;

public class WelcomeActivity extends Activity {

	private ImageView mImageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		mImageView = (ImageView) findViewById(R.id.image);
		boolean ret = OpenCVLoader.initDebug();
		if (ret) {
			Toast.makeText(this, "loading cv first step pass.", Toast.LENGTH_LONG).show();
			process();
		} else {
			Toast.makeText(this, "error on loading cv", Toast.LENGTH_LONG).show();
		}
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

	private void process() {
		try {
			Mat mat = Utils.loadResource(getApplicationContext(),
					R.raw.sudoku1, 0);
			Mat outerBox = new Mat(mat.size(), CvType.CV_8UC1);
			Imgproc.GaussianBlur(mat, mat, new Size(11, 11), 0);
			Imgproc.adaptiveThreshold(mat, outerBox, 255,
					Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 5, 2);
			Core.bitwise_not(outerBox, outerBox);
			Mat kernel = new Mat(3, 3, CvType.CV_8UC1);
			kernel.put(0, 0, new byte[] { 0, 1, 0, 1, 1, 1, 0, 1, 0 });
			Imgproc.dilate(outerBox, outerBox, kernel);
			int max = -1;
			Point maxPt = new Point(0, 0);
			byte[] temp = new byte[1];

			for (int y = 0; y < outerBox.size().height; y++) {
				for (int x = 0; x < outerBox.size().width; x++) {
					outerBox.get(y, x, temp);
					int b = temp[0] & 0xff;
					if (b >= 128) {
						int area = Imgproc.floodFill(outerBox, new Mat(),
								new Point(x, y), new Scalar(64));

						if (area > max) {
							maxPt = new Point(x, y);
							max = area;
						}
					}
				}
			}
			Imgproc.floodFill(outerBox, new Mat(), maxPt, new Scalar(255));
			for (int y = 0; y < outerBox.size().height; y++) {
				for (int x = 0; x < outerBox.size().width; x++) {
					outerBox.get(y, x, temp);
					int b = temp[0] & 0xff;
					if (b == 64) {
						Imgproc.floodFill(outerBox, new Mat(), new Point(x, y),
								new Scalar(0));

					}
				}
			}
			Imgproc.erode(outerBox, outerBox, kernel);
			Mat lines = new Mat();
			Imgproc.HoughLines(outerBox, lines, 1, 3.14159 / 180, 200);
			for (int i = 0; i < lines.cols(); i++) {
				double[] vec = lines.get(0, i);
				double rho = vec[0];
				double theta = vec[1];
				double m = -1 / Math.tan(theta);
				double c = rho / Math.sin(theta);
				Point start = new Point(0, c);
				Point end = new Point(outerBox.size().width, m * outerBox.size().width + c);
				Core.line(outerBox, start, end, new Scalar(255, 0, 0), 3);
			}
			Bitmap bmp = Bitmap.createBitmap((int) mat.size().width,
					(int) mat.size().height, Config.RGB_565);
			Utils.matToBitmap(outerBox, bmp);
			mImageView.setImageBitmap(bmp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
