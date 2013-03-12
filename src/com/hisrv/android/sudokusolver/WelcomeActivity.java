package com.hisrv.android.sudokusolver;

import java.io.IOException;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hisrv.sudokusolver.R;

public class WelcomeActivity extends Activity {

	private ImageView mImageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		mImageView = (ImageView) findViewById(R.id.image);
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3,
				getApplicationContext(), new LoaderCallbackInterface() {

					public void onPackageInstall(int operation,
							InstallCallbackInterface callback) {
						// TODO Auto-generated method stub

					}

					public void onManagerConnected(int status) {
						// TODO Auto-generated method stub
						if (status == LoaderCallbackInterface.SUCCESS) {
							process();
						}
					}
				});
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
					R.raw.sudoku1, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
			Mat outerBox = new Mat(mat.size(), CvType.CV_8UC1);
			Imgproc.GaussianBlur(mat, mat, new Size(11, 11), 0);
			Imgproc.adaptiveThreshold(mat, outerBox, 255,
					Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,
					5, 2);
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
						int area = Imgproc.floodFill(outerBox, new Mat(), new Point(
								x, y), new Scalar(64));

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
						Imgproc.floodFill(outerBox, new Mat(), new Point(
								x, y), new Scalar(0));

					}
				}
			}
			Imgproc.erode(outerBox, outerBox, kernel);
//			Imgproc.HoughLines(image, lines, rho, theta, threshold);
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
