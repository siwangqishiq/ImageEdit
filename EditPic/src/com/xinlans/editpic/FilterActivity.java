package com.xinlans.editpic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.xinlan.editpic.imagezoom.utils.BitmapUtils;
import com.xinlans.editpic.filter.PhotoProcessing;

/**
 * ÂË¾µ
 * 
 * @author panyi
 * 
 */
public final class FilterActivity extends FragmentActivity {
	private String[] filters = PhotoProcessing.FILTERS;
	private ImageView img;
	private ViewGroup filterLayout;

	private Bitmap currentBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		initView();
	}

	private void initView() {
		filterLayout = (ViewGroup) findViewById(R.id.filters_layout);
		img = (ImageView) findViewById(R.id.img);
		currentBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.jianniang);
		img.setImageBitmap(currentBitmap);
		addFilters();
	}

	private void loadPhoto() {
		img.setImageBitmap(null);
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

		if (currentBitmap != null) {
			currentBitmap.recycle();
		}

		currentBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.jianniang);

		if (currentBitmap != null && !currentBitmap.isMutable()) {
			currentBitmap = PhotoProcessing.makeBitmapMutable(currentBitmap);
		}
	}

	private void addFilters() {
		for (int i = 0, len = filters.length; i < len; i++) {
			Button btn = new Button(this);
			btn.setText(filters[i]);
			filterLayout.addView(btn);
			btn.setTag(i);
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					loadPhoto();
					int type = (Integer) v.getTag();
					System.out.println(type);
					currentBitmap = PhotoProcessing.filterPhoto(currentBitmap,
							type);
					img.setImageBitmap(currentBitmap);
				}
			});
		}// end for i
	}
}// end class
