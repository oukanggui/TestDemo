package com.example.testdemo.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;

/**
 * @author oukanggui
 * @date 2021/7/2
 * @description 支持设置圆角带有stroke边框的自定义Span
 */
public class RoundStrokeTextSpan extends ReplacementSpan {
    private int mSize;
    private int mStrokeColor;
    private int mStrokeWidth;
    private int mRadius;
    //字体大小px
    private int mTextSize;
    private int mTextColor;
    RectF oval = new RectF();

    /**
     * @param radius      圆角半径
     * @param strokeColor 边框颜色
     * @param strokeWidth 边框宽度
     * @param textSize    字体大小
     * @param textColor   字体颜色
     */
    public RoundStrokeTextSpan(int radius, int strokeColor, int strokeWidth, int textSize, int textColor) {
        mStrokeColor = strokeColor;
        mStrokeWidth = strokeWidth;
        mRadius = radius;
        mTextSize = textSize;
        mTextColor = textColor;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        text = text.subSequence(start, end);
        Paint p = getCustomTextPaint(paint);
        mSize = (int) (p.measureText(text, start, end) + 2 * mRadius);
        //mSize就是span的宽度，span有多宽，开发者可以在这里随便定义规则
        //我的规则：这里text传入的是SpannableString，start，end对应setSpan方法相关参数
        //可以根据传入起始截至位置获得截取文字的宽度，最后加上左右两个圆角的半径得到span宽度
        return mSize;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        text = text.subSequence(start, end);
        // 注意：需要先绘制文本，再绘制背景，否绘制的字体会变粗，无法解决
        Paint p = getCustomTextPaint(paint);
        Paint.FontMetricsInt fm = p.getFontMetricsInt();
        //此处重新计算y坐标，使字体居中
        canvas.drawText(text.toString(), x, y - ((y + fm.descent + y + fm.ascent) / 2 - (bottom + top) / 2), p);

        paint.setColor(mStrokeColor);//设置背景颜色
        paint.setStrokeWidth(mStrokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);// 设置画笔的锯齿效果
        //oval.set(x, y + paint.ascent() + 15, x + mSize, y + paint.descent() - 10);
        // 使绘制的矩阵背景居中
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        oval.set(x, (bottom + top) / 2 - (fontMetrics.bottom - fontMetrics.top) / 2 + 20, mSize, (bottom + top) / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - 15);
        //设置文字背景矩形，x为span其实左上角相对整个TextView的x值，y为span左上角相对整个View的y值。paint.ascent()获得文字上边缘，paint.descent()获得文字下边缘
        canvas.drawRoundRect(oval, mRadius, mRadius, paint);//绘制圆角矩形，第二个参数是x半径，第三个参数是y半径
    }

    private TextPaint getCustomTextPaint(Paint srcPaint) {
        TextPaint paint = new TextPaint(srcPaint);
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        paint.setTextSize(mTextSize);
        paint.setColor(mTextColor);
        return paint;
    }
}
