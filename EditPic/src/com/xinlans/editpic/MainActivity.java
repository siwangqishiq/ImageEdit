package com.xinlans.editpic;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends FragmentActivity {
	private long exitTime;
	private ViewFlipper bottomToolsFlipper;

	private View stickerButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initView();
	}

	private void initView() {
		bottomToolsFlipper = (ViewFlipper) findViewById(R.id.bottom_fliper);
		bottomToolsFlipper.setInAnimation(this, R.anim.in_bottom_to_top);
		bottomToolsFlipper.setOutAnimation(this, R.anim.out_bottom_to_top);
		// bottomToolsFlipper.startFlipping();

		stickerButton = findViewById(R.id.stickers_btn);
		stickerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				bottomToolsFlipper.showNext();
			}
		});
	}

	/**
     * 
     */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (bottomToolsFlipper.getCurrentView().getId() == R.id.main_tools) {
				if ((System.currentTimeMillis() - exitTime) > 1500) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"在按一次返回退出", Toast.LENGTH_SHORT);
					toast.show();
					exitTime = System.currentTimeMillis();
				} else {
					finish();
				}
			} else {
				bottomToolsFlipper.showPrevious();
			}// end if
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}// end class
