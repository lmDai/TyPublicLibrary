package com.rrju.library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.ColorRes;

import java.util.Collection;

/**
 * 单位转换
 * Created by Android Studio.
 * User: tanyan
 * Date: 2019-08-20
 * Time: 16:17
 */
public class DisplayUtil {
    public static final float getHeightInPx(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }
    /**
     * 获取 显示信息
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm;
    }

    /**
     * 打印 显示信息
     */
    public static DisplayMetrics printDisplayInfo(Context context) {
        DisplayMetrics dm = getDisplayMetrics(context);
        StringBuilder sb = new StringBuilder();
        sb.append("_______  显示信息:  ");
        sb.append("\ndensity         :").append(dm.density);
        sb.append("\ndensityDpi      :").append(dm.densityDpi);
        sb.append("\nheightPixels    :").append(dm.heightPixels);
        sb.append("\nwidthPixels     :").append(dm.widthPixels);
        sb.append("\nscaledDensity   :").append(dm.scaledDensity);
        sb.append("\nxdpi            :").append(dm.xdpi);
        sb.append("\nydpi            :").append(dm.ydpi);
        return dm;
    }
    public static final float getWidthInPx(Context context) {
        final float width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    public static final int getHeightInDp(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        int heightInDp = px2dip(context, height);
        return heightInDp;
    }

    public static final int getWidthInDp(Context context) {
        final float height = context.getResources().getDisplayMetrics().widthPixels;
        int widthInDp = px2dip(context, height);
        return widthInDp;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dp转换成px
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * 转换为当前手机分辨率dip值
     */
    public static int dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scale);
    }
    public static boolean isGif(String url) {
        return "gif".equals(getPathFormat(url));
    }
    public static String getPathFormat(String path) {
        if (!TextUtils.isEmpty(path)) {
            int lastPeriodIndex = path.lastIndexOf('.');
            if (lastPeriodIndex > 0 && lastPeriodIndex + 1 < path.length()) {
                String format = path.substring(lastPeriodIndex + 1);
                if (!TextUtils.isEmpty(format)) {
                    return format.toLowerCase();
                }
            }
        }
        return "";
    }
    public static Drawable getTextDrawable(Context context, int width, int height, int radius, String text, int textSize, @ColorRes int bgColor) {
        return new BitmapDrawable(getTextBitmap(context, width, height, radius, text, textSize, bgColor));
    }

    public static Bitmap getTextBitmap(Context context, int width, int height, int radius, String text, int textSize, @ColorRes int bgColor) {
        radius = dip2px(context, radius);
        Bitmap bitmap = Bitmap.createBitmap(dip2px(context, width), dip2px(context, height), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        RectF rect = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(bgColor));
        canvas.drawRoundRect(new RectF(0, 0, rect.width(), rect.height()), radius, radius, paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize(dip2px(context, textSize));
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        float baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(text, rect.centerX(), baseline, paint);
        return bitmap;
    }
    public static int getSize(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }
}