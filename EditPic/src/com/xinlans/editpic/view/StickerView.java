package com.xinlans.editpic.view;

import java.util.LinkedHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * ��ͼ�����ؼ�
 * 
 * @author panyi
 * 
 */
public class StickerView extends View {
	private static int STATUS_IDLE = 0;
	private static int STATUS_MOVE = 1;// �ƶ�״̬
	private int imageCount;// �Ѽ�����Ƭ������
	private Context mContext;
	private int currentStatus;// ��ǰ״̬
	private StickerItem currentItem;// ��ǰ��������ͼ����
	private float oldx, oldy;

	private LinkedHashMap<Integer, StickerItem> bank = new LinkedHashMap<Integer, StickerItem>();// ����ÿ����ͼ����

	public StickerView(Context context) {
		super(context);
		init(context);
	}

	public StickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public StickerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		this.mContext = context;
		currentStatus = STATUS_IDLE;
	}

	public void addBitImage(final Bitmap addBit) {
		StickerItem item = new StickerItem();
		item.bitmap = addBit;
		item.srcRect = new Rect(0, 0, addBit.getWidth(), addBit.getHeight());
		int bitWidth = 160;
		int bitHeight = 160;
		int left = (getWidth() >> 1) - (bitWidth >> 1);
		int top = (getHeight() >> 1) - (bitHeight >> 1);
		item.dstRect = new RectF(left, top, left + bitWidth, top + bitHeight);
		bank.put(++imageCount, item);
		this.invalidate();// �ػ���ͼ
	}

	/**
	 * ���ƿͻ�ҳ��
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		System.out.println("on draw!!~");
		for (Integer id : bank.keySet()) {
			StickerItem item = bank.get(id);
			canvas.drawBitmap(item.bitmap, item.srcRect, item.dstRect, null);
		}// end for each
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		System.out.println(w + "   " + h + "    " + oldw + "   " + oldh);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean ret = super.onTouchEvent(event);// �Ƿ����´����¼���־ trueΪ����

		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			for (Integer id : bank.keySet()) {
				StickerItem item = bank.get(id);
				if (item.dstRect.contains(x, y)) {
					// ��ѡ��һ����ͼ
					ret = true;
					currentItem = item;
					currentStatus = STATUS_MOVE;
					oldx = x;
					oldy = y;
					break;
				}// end if
			}// end for each
			System.out.println(x + "    " + y);
			break;
		case MotionEvent.ACTION_MOVE:
			ret = true;
			if (currentStatus == STATUS_MOVE) {// �ƶ���ͼ
				float dx = x - oldx;
				float dy = y - oldy;

				oldx = x;
				oldy = y;
				if (currentItem != null) {
					float h = currentItem.dstRect.height();
					float w = currentItem.dstRect.width();
					currentItem.dstRect.left += dx;
					currentItem.dstRect.top += dy;
					currentItem.dstRect.right = currentItem.dstRect.left + w;
					currentItem.dstRect.bottom = currentItem.dstRect.top + h;
					postInvalidate();
				}// end if
			}// endif
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			ret = false;
			currentStatus = STATUS_IDLE;
			break;
		}// end switch
		return ret;
	}

	private final class StickerItem {
		Bitmap bitmap;
		Rect srcRect;// ԭʼͼƬ����
		RectF dstRect;// ����Ŀ������
	}// end inner class

}// end class
