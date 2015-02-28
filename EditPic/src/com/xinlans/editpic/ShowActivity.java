package com.xinlans.editpic;

import com.xinlan.editpic.imagezoom.ImageViewTouch;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

public final class ShowActivity extends FragmentActivity {
	private ImageView img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show);
		initView();
	}

	private void initView() {
		img = (ImageView) findViewById(R.id.show_image);
		img.setImageBitmap(Bridge.bit);
	}
}// end class
