package com.xinlans.editpic.view;

import com.xinlans.editpic.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 * 
 * @author panyi
 * 
 */
public class StickerItem {
	private static final int HELP_BOX_PAD = 25;

	private static final int BUTTON_WIDTH = 25;

	public Bitmap bitmap;
	public Rect srcRect;// ԭʼͼƬ����
	public RectF dstRect;// ����Ŀ������
	private Rect helpToolsRect;
	public RectF deleteRect;// ɾ����ťλ��
	public RectF rotateRect;// ��ת��ťλ��

	RectF helpBox;
	public Matrix matrix;// �仯����

	boolean isDrawHelpTool = false;
	private Paint helpBoxPaint = new Paint();

	private static Bitmap deleteBit;
	private static Bitmap rotateBit;

	public StickerItem(Context context) {

		helpBoxPaint.setColor(Color.BLACK);
		helpBoxPaint.setStyle(Style.STROKE);
		helpBoxPaint.setStrokeWidth(4);

		// ���빤�߰�ťλͼ
		if (deleteBit == null) {
			deleteBit = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.delete);
		}// end if
		if (rotateBit == null) {
			rotateBit = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.rotate);
		}// end if
	}

	public void init(Bitmap addBit, View parentView) {
		this.bitmap = addBit;
		this.srcRect = new Rect(0, 0, addBit.getWidth(), addBit.getHeight());
		int bitWidth = Math.min(addBit.getWidth(), parentView.getWidth() >> 1);
		int bitHeight = (int) bitWidth * addBit.getHeight() / addBit.getWidth();
		int left = (parentView.getWidth() >> 1) - (bitWidth >> 1);
		int top = (parentView.getHeight() >> 1) - (bitHeight >> 1);
		this.dstRect = new RectF(left, top, left + bitWidth, top + bitHeight);
		this.matrix = new Matrix();
		this.matrix.postTranslate(this.dstRect.left, this.dstRect.top);
		this.matrix.postScale((float) bitWidth / addBit.getWidth(),
				(float) bitHeight / addBit.getHeight(), this.dstRect.left,
				this.dstRect.top);
		// item.matrix.setScale((float)bitWidth/addBit.getWidth(),
		// (float)bitHeight/addBit.getHeight());
		this.isDrawHelpTool = true;
		this.helpBox = new RectF(this.dstRect);
		this.helpBox.left -= HELP_BOX_PAD;
		this.helpBox.right += HELP_BOX_PAD;
		this.helpBox.top -= HELP_BOX_PAD;
		this.helpBox.bottom += HELP_BOX_PAD;

		helpToolsRect = new Rect(0, 0, deleteBit.getWidth(),
				deleteBit.getHeight());

		deleteRect = new RectF(helpBox.left - BUTTON_WIDTH, helpBox.top
				- BUTTON_WIDTH, helpBox.left + BUTTON_WIDTH, helpBox.top
				+ BUTTON_WIDTH);
		rotateRect = new RectF(helpBox.right - BUTTON_WIDTH, helpBox.bottom
				- BUTTON_WIDTH, helpBox.right + BUTTON_WIDTH, helpBox.bottom
				+ BUTTON_WIDTH);
	}

	/**
	 * λ�ø���
	 * 
	 * @param dx
	 * @param dy
	 */
	public void updatePos(final float dx, final float dy) {
		dstRect.offset(dx, dy);
		helpBox.offset(dx, dy);

		// ���߰�ť��֮�ƶ�
		deleteRect.offset(dx, dy);
		rotateRect.offset(dx, dy);

		this.matrix.postTranslate(dx, dy);// ��¼��������
	}

	/**
	 * ������ͼԪ��
	 * 
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		canvas.drawBitmap(this.bitmap, this.matrix, null);// ��ͼԪ�ػ���
		if (this.isDrawHelpTool) {// ���Ƹ���������
			canvas.drawRoundRect(helpBox, 10, 10, helpBoxPaint);

			// ���ƹ��߰�ť
			canvas.drawBitmap(deleteBit, helpToolsRect, deleteRect, null);
			canvas.drawBitmap(rotateBit, helpToolsRect, rotateRect, null);

			// canvas.drawBitmap(deleteBit, helpBox.left, helpBox.top, null);
			// canvas.drawBitmap(rotateBit, helpBox.right, helpBox.bottom,
			// null);
		}// end if
	}
}// end class
