package com.xinlans.editpic;

import java.util.LinkedHashMap;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xinlan.editpic.imagezoom.ImageViewTouch;
import com.xinlan.editpic.imagezoom.ImageViewTouchBase.DisplayType;
import com.xinlans.editpic.view.StickerItem;
import com.xinlans.editpic.view.StickerView;

public final class DemoActivity extends FragmentActivity {
	public static final int SELECTED_IMAGE = 7;
	private View selectBtn;
	private Bitmap addBitmap;

	private ImageViewTouch imageViewTouch;
	private StickerView stickerView;

	private Button mGenBit;// Éú³ÉÎ»Í¼

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo);
		initView();
	}

	private void initView() {
		selectBtn = findViewById(R.id.btn_image);
		selectBtn.setOnClickListener(new SelectBtnClick());

		mGenBit = (Button) findViewById(R.id.btn_gen);
		mGenBit.setOnClickListener(new GenClick());
		stickerView = (StickerView) findViewById(R.id.stick_layer);
		imageViewTouch = (ImageViewTouch) findViewById(R.id.touch_image);
		imageViewTouch.setImageBitmap(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.asd));
		imageViewTouch.setDisplayType(DisplayType.FIT_TO_SCREEN);// ÊÊÓ¦ÆÁÄ»

		// imageViewTouch.setScaleEnabled(false);
		// imageViewTouch.setScrollEnabled(false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SELECTED_IMAGE:
			if (resultCode == RESULT_OK && null != data) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();

				System.out.println("Ñ¡ÔñÍ¼Æ¬Â·¾¶ =====>" + picturePath);
				addImageView(BitmapFactory.decodeFile(picturePath));
			}
			break;
		}// end switch
	}

	protected void addImageView(Bitmap bitmap) {
		stickerView.addBitImage(bitmap);
	}

	/**
	 * Í¼Æ¬Ñ¡Ôñ
	 * 
	 * @author panyi
	 * 
	 */
	private final class SelectBtnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, SELECTED_IMAGE);
		}
	}// end inner class

	private final class GenClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			Matrix touchMatrix = imageViewTouch.getImageViewMatrix();
			Bitmap srcBit = BitmapFactory.decodeResource(
					DemoActivity.this.getResources(), R.drawable.asd).copy(
					Bitmap.Config.ARGB_8888, true);
			Bitmap retBit = Bitmap.createBitmap(imageViewTouch.getWidth(),
					imageViewTouch.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(retBit);
			canvas.drawBitmap(srcBit, touchMatrix, null);

			LinkedHashMap<Integer, StickerItem> addItems = stickerView
					.getBank();
			for (Integer id : addItems.keySet()) {
				StickerItem item = addItems.get(id);
				canvas.drawBitmap(item.bitmap, item.matrix, null);
			}// end for

			if (Bridge.bit != null) {
				Bridge.bit.recycle();
				Bridge.bit = null;
			}// end if
			Bridge.bit = retBit;

			Intent it = new Intent(DemoActivity.this, ShowActivity.class);
			DemoActivity.this.startActivity(it);
		}
	}// end inner class

}// end class
