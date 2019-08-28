package com.rrju.library.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rrju.library.R;
import com.rrju.library.interfaces.SelectTypeUtils;
import com.rrju.library.mode.FiltrateTitleModel;
import com.rrju.library.mode.LabelThreeSubModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: tanyan
 * Date: 2019-08-28
 * Time: 9:23
 */
public class DropDownMenu extends LinearLayout {

    //顶部菜单布局
    private LinearLayout tabMenuView;
    //底部容器，包含popupMenuViews，maskView
    private FrameLayout containerView;
    //弹出菜单父布局
    private FrameLayout popupMenuViews;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;

    //分割线颜色
    private int dividerColor = 0xffcccccc;
    //tab选中颜色
    private int textSelectedColor = 0xff890c85;
    //tab未选中颜色
    private int textUnselectedColor = 0xff111111;
    //遮罩颜色
    private int maskColor = 0x88888888;
    //tab字体大小
    private int menuTextSize = 14;
    //    private String menuUnselectedIcon = "¡";
//    private String menuSelectedIcon = "¢";
    private float menuHeighPercent = 0.5f;
    private List<FiltrateTitleModel> tabTopTexts = new ArrayList<>();
    //是否隐藏tabLayout
    private boolean isConcealLayout = false;
    /**
     * tab布局集合
     */
    private List<LinearLayout> tabLayoutList = new ArrayList<>();
    /**
     * tab回调内容
     */
    private List<LabelThreeSubModel> tabBack;
    /**
     * tab回调
     */
    private SelectTypeUtils tabSelectUtils;
    /**
     * tab回调
     */
    private SelectTypeUtils tabSingleShowClickListener;

    public DropDownMenu(Context context) {
        super(context, null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        //为DropDownMenu添加自定义属性
        int menuBackgroundColor = 0xffffffff;
        int underlineColor = 0xffcccccc;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        underlineColor = a.getColor(R.styleable.DropDownMenu_ddunderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.DropDownMenu_dddividerColor, dividerColor);
        textSelectedColor = a.getColor(R.styleable.DropDownMenu_ddtextSelectedColor, textSelectedColor);
        textUnselectedColor = a.getColor(R.styleable.DropDownMenu_ddtextUnselectedColor, textUnselectedColor);
        menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_ddmenuBackgroundColor, menuBackgroundColor);
        maskColor = a.getColor(R.styleable.DropDownMenu_ddmaskColor, maskColor);
        menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_ddmenuTextSize, menuTextSize);
//        menuSelectedIcon = a.getString(R.styleable.DropDownMenu_ddmenuSelectedIcon);
//        menuUnselectedIcon = a.getString(R.styleable.DropDownMenu_ddmenuUnselectedIcon);
        menuHeighPercent = a.getFloat(R.styleable.DropDownMenu_ddmenuMenuHeightPercent, menuHeighPercent);
        a.recycle();

        //初始化tabMenuView并添加到tabMenuView
        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView, 0);

        //为tabMenuView添加下划线
        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        //初始化containerView并将其添加到DropDownMenu
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 2);
    }

    public int getCurrent_tab_position() {
        return current_tab_position;
    }

    public void setCurrent_tab_position(int current_tab_position) {
        this.current_tab_position = current_tab_position;
    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     * @param contentView
     */
    public void setDropDownMenu(@NonNull List<FiltrateTitleModel> tabTexts,
                                @NonNull List<View> popupViews, @NonNull View contentView) {
        if (tabTexts.size() != popupViews.size()) {
//            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
            return;
        }
        tabTopTexts.addAll(tabTexts);
        tabLayoutList = new ArrayList<>();
        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }
        containerView.addView(contentView, 0);

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        containerView.addView(maskView, 1);
        maskView.setVisibility(GONE);
        if (containerView.getChildAt(2) != null) {
            containerView.removeViewAt(2);
        }

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        popupMenuViews.setVisibility(GONE);
        containerView.addView(popupMenuViews, 2);

        for (int i = 0; i < popupViews.size(); i++) {
            popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            popupMenuViews.addView(popupViews.get(i), i);
        }

    }

    public void setDropDownMenu(@NonNull List<FiltrateTitleModel> tabTexts, @NonNull ArrayList<LabelThreeSubModel> defaulttTabTexts,
                                @NonNull List<View> popupViews, @NonNull View contentView) {
        setDropDownMenu(tabTexts, popupViews, contentView);
        if (defaulttTabTexts == null || tabTexts.size() != defaulttTabTexts.size()) {
            return;
        }
        for (int i = 0; i < defaulttTabTexts.size(); i++) {
            if (!TextUtils.isEmpty(defaulttTabTexts.get(i).getDisplayName())) {
                setCurrent_tab_position(i);
                setTabText(defaulttTabTexts.get(i).getDisplayName());
                closeMenu();
            }
        }

    }

    private void addTab(@NonNull List<FiltrateTitleModel> tabTexts, int i) {
        final LinearLayout ll = new LinearLayout(getContext());
        //tv1设置权重是1
        if (isConcealLayout) {
            //隐藏头部筛选项
            ll.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1.0f));
        } else {
            ll.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f));
        }
        ll.setOrientation(HORIZONTAL);
        ll.setGravity(Gravity.CENTER);
        TextView tab = new TextView(getContext());
        TextView tabIcon = new TextView(getContext());
        tab.setSingleLine();
        tab.setEllipsize(TextUtils.TruncateAt.END);
        tab.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        tab.setText(tabTexts.get(i).getmTitleName());
        tab.setTextColor(textUnselectedColor);
        //tab.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
        //    tab.setVisibility(TextUtils.isEmpty(tabTexts.get(i).getmTitleName()) ? GONE : VISIBLE);
        if (TextUtils.equals(tabTexts.get(i).getmTitleName(), "0") || TextUtils.isEmpty(tabTexts.get(i).getmTitleName())) {
            // tab.setText(TextUtils.isEmpty(tabTexts.get(i).getmTitleName())?"1":tabTexts.get(i).getmTitleName());
            tab.setGravity(Gravity.CENTER);
            tabIcon.setGravity(Gravity.CENTER);
            tabIcon.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tab.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tab.setPadding(dpTpPx(0), dpTpPx(12), 0, dpTpPx(12));
            tab.setVisibility(INVISIBLE);
        } else {
            tabIcon.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            tab.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tabIcon.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tab.setPadding(dpTpPx(5), dpTpPx(12), 0, dpTpPx(12));
            //tab.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3.0f));
            tab.setPadding(0, dpTpPx(12), 0, dpTpPx(12));
        }
        tabIcon.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        tabIcon.setTextColor(textUnselectedColor);
        tabIcon.setText(tabTexts.get(i).getmUnSelectTextIcon());
        Typeface fontFace = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/RrjFonts.ttf");
        tabIcon.setTypeface(fontFace);
        ll.addView(tab);
        ll.addView(tabIcon);
        //添加点击事件
        ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(ll);
            }
        });
        tabLayoutList.add(ll);

        tabMenuView.addView(ll);
        //添加分割线
//        if (i < tabTexts.size() - 1) {
//            View view = new View(getContext());
//            view.setLayoutParams(new LayoutParams(dpTpPx(0.5f), ViewGroup.LayoutParams.MATCH_PARENT));
//            view.setBackgroundColor(dividerColor);
//            tabMenuView.addView(view);
//        }
    }


    /**
     * 设置选项点击
     *
     * @param position
     */
    public void setTabClick(int position) {
        switchMenu(tabLayoutList.get(position));
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        containerView.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        boolean mHasScroll = true;
//        if (ev != null && ev.getAction() == MotionEvent.ACTION_DOWN) {
//            mHasScroll = false;
//        } else if (ev != null && mHasScroll && ev.getAction() == MotionEvent.ACTION_UP) {
//            //如果产生了滑动，则不传递事件到子view了
//            containerView.onTouchEvent(ev);
//            return true;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }


    public void setTabSingleShowClickListener(SelectTypeUtils tabSingleShowClickListener) {
        this.tabSingleShowClickListener = tabSingleShowClickListener;
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(String text) {
        if (current_tab_position != -1) {
            boolean isChange = TextUtils.equals(text, ((TextView) (((LinearLayout)
                    tabMenuView.getChildAt(current_tab_position))
                    .getChildAt(0))).getText().toString());
            ((TextView) (((LinearLayout) tabMenuView.getChildAt(current_tab_position))
                    .getChildAt(0))).setText(TextUtils.isEmpty(text) ? "1" : text);
            ((TextView) (((LinearLayout) tabMenuView.getChildAt(current_tab_position))
                    .getChildAt(0))).setTextColor(isChange ? textSelectedColor : textUnselectedColor);
            ((TextView) (((LinearLayout) tabMenuView.getChildAt(current_tab_position))
                    .getChildAt(1))).setTextColor(isChange ? textSelectedColor : textUnselectedColor);
            ((TextView) (((LinearLayout) tabMenuView.getChildAt(current_tab_position))
                    .getChildAt(1))).setText(tabTopTexts.get(current_tab_position).getmUnSelectTextIcon());
        }
    }

    public String getTabTopId() {
        if (current_tab_position != -1) {
            return tabTopTexts.get(current_tab_position).getmId();
        }
        return "";
    }

    public void setTabClickable(boolean clickable) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            tabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (current_tab_position != -1) {
            boolean isChange = TextUtils.equals(
                    ((TextView) (((LinearLayout) tabMenuView.getChildAt(current_tab_position)).getChildAt(0)))
                            .getText().toString(), tabTopTexts.get(current_tab_position).getmTitleName());
//            ((TextView)(((LinearLayout) tabMenuView.getChildAt(current_tab_position))
//                    .getChildAt(0))).setText(text);
            ((TextView) (((LinearLayout) tabMenuView.getChildAt(current_tab_position))
                    .getChildAt(0))).setTextColor(isChange ? textUnselectedColor : textSelectedColor);
            ((TextView) (((LinearLayout) tabMenuView.getChildAt(current_tab_position))
                    .getChildAt(1))).setTextColor(isChange ? textUnselectedColor : textSelectedColor);
            ((TextView) (((LinearLayout) tabMenuView.getChildAt(current_tab_position))
                    .getChildAt(1))).setText(tabTopTexts.get(current_tab_position).getmUnSelectTextIcon());
            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
            maskView.setVisibility(GONE);
            // maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));

            if (isConcealLayout) {
                int currentTextColor = ((TextView) (((LinearLayout) tabMenuView.getChildAt(current_tab_position)).getChildAt(0))).getCurrentTextColor();
                LabelThreeSubModel labelThreeSubModel = new LabelThreeSubModel();
                String content = ((TextView) (((LinearLayout) tabMenuView.getChildAt(current_tab_position)).getChildAt(0))).getText().toString();
                String contentIocn = ((TextView) (((LinearLayout) tabMenuView.getChildAt(current_tab_position)).getChildAt(1))).getText().toString();
                labelThreeSubModel.setDisplayName(content);
                //图标内容  （只用于回掉）
                labelThreeSubModel.setDisplayNameCondition(contentIocn);
                if (currentTextColor == textSelectedColor) {
                    labelThreeSubModel.setSelect(true);
                } else {
                    labelThreeSubModel.setSelect(false);
                }
                if (tabSingleShowClickListener != null) {
                    tabSingleShowClickListener.getData(labelThreeSubModel, current_tab_position);
                }
            }
            current_tab_position = -1;
        }
    }

    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return current_tab_position != -1;
    }


    public void setTabSelectUtils(SelectTypeUtils tabSelectUtils) {
        this.tabSelectUtils = tabSelectUtils;
    }

    /**
     * 切换菜单
     *
     * @param target
     */
    private void switchMenu(View target) {
        tabBack = new ArrayList<>();
//        Log.e("tanyan", tabMenuView.getChildCount() + "");
        for (int i = 0; i < tabMenuView.getChildCount(); i++) {
            boolean isChange = TextUtils.equals(
                    ((TextView) (((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0)))
                            .getText().toString(), tabTopTexts.get(i).getmTitleName());
            if (target == tabMenuView.getChildAt(i)) {
                if (current_tab_position == i) {
                    closeMenu();
                } else {
                    if (current_tab_position == -1) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
                        maskView.setVisibility(VISIBLE);
                        // maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
                        popupMenuViews.getChildAt(i).setVisibility(View.VISIBLE);
                    } else {
                        popupMenuViews.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                    current_tab_position = i;
                    ((TextView) (((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0)))
                            .setTextColor(textSelectedColor);
                    ((TextView) (((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(1)))
                            .setTextColor(textSelectedColor);
                    ((TextView) (((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(1)))
                            .setText(tabTopTexts.get(i).getmSelectTextIcon());
                }
            } else {
                ((TextView) (((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0)))
                        .setTextColor(isChange ? textUnselectedColor : textSelectedColor);
                ((TextView) (((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(1)))
                        .setTextColor(isChange ? textUnselectedColor : textSelectedColor);
                ((TextView) (((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(1)))
                        .setText(tabTopTexts.get(i).getmUnSelectTextIcon());
//                ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
//                        getResources().getDrawable(menuUnselectedIcon), null);
                popupMenuViews.getChildAt(i).setVisibility(View.GONE);
            }
            int currentTextColor = ((TextView) (((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0))).getCurrentTextColor();
            LabelThreeSubModel labelThreeSubModel = new LabelThreeSubModel();
            String content = ((TextView) (((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0))).getText().toString();
            String contentIocn = ((TextView) (((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(1))).getText().toString();
            labelThreeSubModel.setDisplayName(content);
            //图标内容  （只用于回掉）
            labelThreeSubModel.setDisplayNameCondition(contentIocn);
            if (currentTextColor == textSelectedColor) {
                labelThreeSubModel.setSelect(true);
            } else {
                labelThreeSubModel.setSelect(false);
            }
            tabBack.add(labelThreeSubModel);
        }
        if (tabSelectUtils != null) {
            if (popupMenuViews.getVisibility() == VISIBLE) {
                tabSelectUtils.getData(tabBack, 0);
            } else {
                tabSelectUtils.getData(tabBack, 1);
            }
        }
    }

    public int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }

    public void setConcealLayout(boolean concealLayout) {
        isConcealLayout = concealLayout;
    }
}