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
	private Paint dstPaint = new Paint();
	private Paint helpBoxPaint = new Paint();

	private static Bitmap deleteBit;
	private static Bitmap rotateBit;

	public StickerItem(Context context) {

		helpBoxPaint.setColor(Color.BLACK);
		helpBoxPaint.setStyle(Style.STROKE);
		helpBoxPaint.setStrokeWidth(4);

		dstPaint = new Paint();
		dstPaint.setColor(Color.RED);
		dstPaint.setAlpha(100);

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
		updateHelpBoxRect();

		helpToolsRect = new Rect(0, 0, deleteBit.getWidth(),
				deleteBit.getHeight());

		deleteRect = new RectF(helpBox.left - BUTTON_WIDTH, helpBox.top
				- BUTTON_WIDTH, helpBox.left + BUTTON_WIDTH, helpBox.top
				+ BUTTON_WIDTH);
		rotateRect = new RectF(helpBox.right - BUTTON_WIDTH, helpBox.bottom
				- BUTTON_WIDTH, helpBox.right + BUTTON_WIDTH, helpBox.bottom
				+ BUTTON_WIDTH);
	}

	private void updateHelpBoxRect() {
		this.helpBox.left -= HELP_BOX_PAD;
		this.helpBox.right += HELP_BOX_PAD;
		this.helpBox.top -= HELP_BOX_PAD;
		this.helpBox.bottom += HELP_BOX_PAD;

	}

	/**
	 * λ�ø���
	 * 
	 * @param dx
	 * @param dy
	 */
	public void updatePos(final float dx, final float dy) {
		this.matrix.postTranslate(dx, dy);// ��¼��������

		dstRect.offset(dx, dy);

		// ���߰�ť��֮�ƶ�
		helpBox.offset(dx, dy);
		deleteRect.offset(dx, dy);
		rotateRect.offset(dx, dy);

	}

	/**
	 * ��ת ���� ����
	 * 
	 * @param dx
	 * @param dy
	 */
	public void updateRotateAndScale(final float oldx, final float oldy,
			final float dx, final float dy) {
		float x = oldx;
		float y = oldy;

		float c_x = dstRect.centerX();
		float c_y = dstRect.centerY();

		float n_x = x + dx;
		float n_y = y + dy;

		float xa = x - c_x;
		float ya = y - c_y;

		float xb = n_x - c_x;
		float yb = n_y - c_y;

		float srcLen = (float) Math.sqrt(xa * xa + ya * ya);
		float curLen = (float) Math.sqrt(xb * xb + yb * yb);

		float scale = curLen / srcLen;// �������ű�

		this.matrix.postScale(scale, scale, this.dstRect.centerX(),
				this.dstRect.centerY());// ����scale����
		// this.matrix.postRotate(5, this.dstRect.centerX(),
		// this.dstRect.centerY());
		scaleRect(this.dstRect, scale);// ����Ŀ�����

		// ���¼��㹤��������
		helpBox.set(dstRect);
		updateHelpBoxRect();//���¼���
		rotateRect.offsetTo(helpBox.right - BUTTON_WIDTH, helpBox.bottom
				- BUTTON_WIDTH);
		deleteRect.offsetTo(helpBox.left - BUTTON_WIDTH, helpBox.top
				- BUTTON_WIDTH);
		// System.out
		// .println(srcLen + "     " + curLen + "    scale--->" + scale);

	}

	/**
	 * ������ͼԪ��
	 * 
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		canvas.drawBitmap(this.bitmap, this.matrix, null);// ��ͼԪ�ػ���
		// canvas.drawRect(this.dstRect, dstPaint);

		if (this.isDrawHelpTool) {// ���Ƹ���������
			canvas.save();
			canvas.rotate(0, helpBox.centerX(), helpBox.centerY());
			canvas.drawRoundRect(helpBox, 10, 10, helpBoxPaint);
			// ���ƹ��߰�ť
			canvas.drawBitmap(deleteBit, helpToolsRect, deleteRect, null);
			canvas.drawBitmap(rotateBit, helpToolsRect, rotateRect, null);
			canvas.restore();
		}// end if
	}

	/**
	 * ����ָ������
	 * 
	 * @param rect
	 * @param scale
	 */
	private static void scaleRect(RectF rect, float scale) {
		float w = rect.width();
		float h = rect.height();

		float newW = scale * w;
		float newH = scale * h;

		float dx = (newW - w) / 2;
		float dy = (newH - h) / 2;

		rect.left -= dx;
		rect.top -= dy;
		rect.right += dx;
		rect.bottom += dy;
	}
}// end class
