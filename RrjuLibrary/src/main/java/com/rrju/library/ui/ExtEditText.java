package com.rrju.library.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.rrju.library.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 扩展的EditText组件
 */
@SuppressLint("AppCompatCustomView")
public class ExtEditText extends EditText {
    // 特殊下标位置
    private static final int PHONE_INDEX_3 = 3;
    private static final int PHONE_INDEX_4 = 4;
    private static final int PHONE_INDEX_8 = 8;
    private static final int PHONE_INDEX_9 = 9;
    private static OndispatchKeyEventPreIme m_diKeyEventPreIme;
    private Drawable imgEnable;
    private boolean m_bEnableClearButton = true;
    private boolean isFormatPhone = false;
    private Context mContext;

    public ExtEditText(Context context) {
        super(context);
    }

    public ExtEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public ExtEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ExtEditText);
        isFormatPhone = array.getBoolean(R.styleable.ExtEditText_isFormatPhone, false);

        // 获取图片资源
        imgEnable = mContext.getResources().getDrawable(R.drawable.edit_clear);
        setCompoundDrawablePadding(mContext.getResources()
                .getDimensionPixelSize(R.dimen.padding_5));
        if (isFormatPhone) {
            setFilters(new InputFilter[]{
                    new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence source, int start, int end,
                                                   Spanned spanned, int dstart, int dend) {
                            if (" ".equals(source.toString()) || source.toString().contentEquals("\n") || dstart == 13) {
                                return "";
                            } else {
                                return null;
                            }
                        }
                    }
            });
        }
        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (isFormatPhone) {
                    if (s == null || s.length() == 0) {
                        return;
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < s.length(); i++) {
                        if (i != PHONE_INDEX_3 && i != PHONE_INDEX_8 && s.charAt(i) == ' ') {
                            continue;
                        } else {
                            sb.append(s.charAt(i));
                            if ((sb.length() == PHONE_INDEX_4 || sb.length() == PHONE_INDEX_9) && sb.charAt(sb.length() - 1) != ' ') {
                                sb.insert(sb.length() - 1, ' ');
                            }
                        }
                    }
                    if (!sb.toString().equals(s.toString())) {
                        int index = start + 1;
                        if (sb.charAt(start) == ' ') {
                            if (before == 0) {
                                index++;
                            } else {
                                index--;
                            }
                        } else {
                            if (before == 1) {
                                index--;
                            }
                        }

                        setText(sb.toString());
                        setSelection(index);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable(m_bEnableClearButton);
            }
        });
        setDrawable(m_bEnableClearButton);
        try {
            java.lang.reflect.Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(this, R.drawable.edit_cursor);
        } catch (Exception e) {
        }

    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (m_diKeyEventPreIme != null) {
            m_diKeyEventPreIme.dispatchKeyEventPreIme(event);
        }
        return super.dispatchKeyEventPreIme(event);
    }

    public void setDispatchKeyEventPreIme(
            OndispatchKeyEventPreIme ondispatchKeyEventPreIme) {
        m_diKeyEventPreIme = ondispatchKeyEventPreIme;
    }

    /**
     * 设置删除图片
     */
    public void setDrawable(boolean isShow) {
        m_bEnableClearButton = isShow;
        Drawable[] draw = getCompoundDrawables();
        if (draw != null) {
            if (!m_bEnableClearButton || getText().toString().length() == 0
                    || !isFocused()) {
                setCompoundDrawablesWithIntrinsicBounds(draw[0], draw[1], null,
                        draw[3]);
            } else {
                setCompoundDrawablesWithIntrinsicBounds(draw[0], draw[1],
                        imgEnable, draw[3]);
            }
            setCompoundDrawablePadding(mContext.getResources()
                    .getDimensionPixelSize(R.dimen.padding_10));
        }
    }

    /**
     * event.getX() 获取相对应自身左上角的X坐标 event.getY() 获取相对应自身左上角的Y坐标 getWidth()
     * 获取控件的宽度 getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离 getPaddingRight()
     * 获取删除图标右边缘到控件右边缘的距离 getWidth() - getTotalPaddingRight() 计算删除图标左边缘到控件左边缘的距离
     * getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (m_bEnableClearButton && imgEnable != null
                && event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            // 判断触摸点是否在水平范围内
            boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight()))
                    && (x < (getWidth() - getPaddingRight()));
            // 获取删除图标的边界，返回一个Rect对象
            Rect rect = imgEnable.getBounds();
            // 获取删除图标的高度
            int height = rect.height();
            int y = (int) event.getY();
            // 计算图标底部到控件底部的距离
            int distance = (getHeight() - height) / 2;
            // 判断触摸点是否在竖直范围内(可能会有点误差)
            // 触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
            boolean isInnerHeight = (y > distance) && (y < (distance + height));

            if (isInnerWidth && isInnerHeight) {
                setText("");
            }

        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        setDrawable(m_bEnableClearButton);
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    // 获得不包含空格的手机号
    public String getPhoneText() {
        String str = getText().toString();
        return replaceBlank(str);
    }

    private String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            if (m.find()) {
                dest = m.replaceAll("");
            }
        }
        return dest;
    }

    public interface OndispatchKeyEventPreIme {
        void dispatchKeyEventPreIme(KeyEvent event);
    }
}
