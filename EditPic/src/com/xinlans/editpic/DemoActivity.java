package com.xinlans.editpic;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

public final class DemoActivity extends FragmentActivity {
	public static final int SELECTED_IMAGE = 7;
	private View selectBtn;
	private Bitmap addBitmap;
	private AbsoluteLayout workSpace;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo);
		initView();
	}

	private void initView() {
		workSpace = (AbsoluteLayout) findViewById(R.id.workspace);
		selectBtn = findViewById(R.id.btn_image);
		selectBtn.setOnClickListener(new SelectBtnClick());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SELECTED_IMAGE:
			if (resultCode == RESULT_OK && null != data) {
				if (addBitmap != null) {
					addBitmap.recycle();
					addBitmap = null;
				}
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();

				System.out.println("Ñ¡ÔñÍ¼Æ¬Â·¾¶ =====>" + picturePath);
				addBitmap = BitmapFactory.decodeFile(picturePath);
				// getImageLayout(m_bitmap);
				addImageView(addBitmap);
			}
			break;
		}// end switch
	}

	protected void addImageView(Bitmap bitmap) {
		ImageView addImg = new ImageView(this);
		addImg.setImageBitmap(bitmap);
		workSpace.addView(addImg);
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

}// end class
