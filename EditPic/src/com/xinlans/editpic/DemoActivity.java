package com.xinlans.editpic;

import java.util.LinkedHashMap;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
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

	private Bitmap currentBit;

	private Button mGenBit;// Éú³ÉÎ»Í¼

	private Button cropButton;
	private Button genCropButton;// Éú³É¼ô²ÃÍ¼Æ¬
	private CropImageView cropImageView;

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
		currentBit = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.jianniang2);
		imageViewTouch.setImageBitmap(currentBit);
		imageViewTouch.setDisplayType(DisplayType.FIT_TO_SCREEN);// ÊÊÓ¦ÆÁÄ»

		cropButton = (Button) findViewById(R.id.crop_image);
		genCropButton = (Button) findViewById(R.id.gen_crop_image);
		cropImageView = (CropImageView) findViewById(R.id.cropImageView);
		cropImageView.setGuidelines(2);
		cropButton.setOnClickListener(new CropClick());
		genCropButton.setOnClickListener(new GenCropClick());
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

				// System.out.println("Ñ¡ÔñÍ¼Æ¬Â·¾¶ =====>" + picturePath);
				Options opts = new Options();
				opts.inSampleSize = 2;
				addImageView(BitmapFactory.decodeFile(picturePath, opts));
			}
			break;
		}// end switch
	}

	protected void addImageView(Bitmap bitmap) {
		stickerView.addBitImage(bitmap);
	}

	/**
	 * ²Ã¼ôÄ£Ê½
	 * 
	 * @author panyi
	 * 
	 */
	private final class CropClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			imageViewTouch.setVisibility(View.GONE);
			stickerView.setVisibility(View.GONE);

			cropImageView.setImageBitmap(currentBit);
			cropImageView.setVisibility(View.VISIBLE);
		}
	}// end inner class
	
	private final class GenCropClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			Bitmap newBitmap = cropImageView.getCroppedImage();
			changeCurrentBitmap(newBitmap);
			
			imageViewTouch.setVisibility(View.VISIBLE);
			stickerView.setVisibility(View.VISIBLE);
			cropImageView.setVisibility(View.GONE);
		}
	}//end inner class

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
			imageViewTouch.setVisibility(View.VISIBLE);
			stickerView.setVisibility(View.VISIBLE);
			cropImageView.setVisibility(View.GONE);
		}
	}// end inner class

	private void changeCurrentBitmap(Bitmap newBit) {
		if (currentBit != null) {
			currentBit.recycle();
			currentBit = null;
			System.gc();
		}
		currentBit = newBit;
		imageViewTouch.setImageBitmap(currentBit);
		imageViewTouch.setDisplayType(DisplayType.FIT_TO_SCREEN);// ÊÊÓ¦ÆÁÄ»
	}

	private final class GenClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			Matrix touchMatrix = imageViewTouch.getImageViewMatrix();
			Bitmap srcBit = currentBit.copy(Bitmap.Config.ARGB_8888, true);
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
			stickerView.clear();

			// if (Bridge.bit != null) {
			// Bridge.bit.recycle();
			// Bridge.bit = null;
			// }// end if
			// Bridge.bit = retBit;
			changeCurrentBitmap(retBit);
			// Intent it = new Intent(DemoActivity.this, ShowActivity.class);
			// DemoActivity.this.startActivity(it);
		}
	}// end inner class
}// end class
