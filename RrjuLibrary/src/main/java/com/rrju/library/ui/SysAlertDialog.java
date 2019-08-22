package com.rrju.library.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.rrju.library.R;
import com.rrju.library.ui.datatime.JudgeDate;
import com.rrju.library.ui.datatime.ScreenInfo;
import com.rrju.library.ui.datatime.WheelDateMain;
import com.rrju.library.ui.datatime.WheelStartAndEndTimeMain;
import com.rrju.library.utils.DisplayUtil;
import com.rrju.library.utils.ThreadPoolUtils;

import java.lang.ref.SoftReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 显示系统级提示框
 */
public class SysAlertDialog {

    public interface CancelListener {
        void cancel();
    }

    public interface OnDialogTextClickListener {
        void onClick(DialogInterface arg0, int arg1, String text);
    }

    public interface OnStartAndEndTimeClickListener {
        void onClick(DialogInterface arg0, int arg1, String startTime,
                     String endTime);
    }

    private static ExtProgressDialog m_dlgLoading;
    public static final int LENGTH_SHORT = 2 * 1000;
    public static final int LENGTH_LONG = 5 * 1000;
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static SoftReference<Toast> wrToast = null;
    private static Object synObj = new Object();
    private static SoftReference<Toast> wrToastScore = null;
    private static Object synObjScore = new Object();

    /**
     * 创建并显示提示框
     *
     * @param context
     * @param strMessage
     */
    public static ExtProgressDialog showLoadingDialog(Context context, String strMessage) {
        return showLoadingDialog(context, strMessage, true,
                new OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancelLoadingDialog();
                    }
                });
    }

    /**
     * 创建并显示提示框
     *
     * @param context
     * @param strMessage
     */
    public static ExtProgressDialog showLoadingDialog(Context context,
                                                      String strMessage, boolean cancelable, OnCancelListener listener) {
        if (m_dlgLoading == null) {
            // context = context.getApplicationContext();
            m_dlgLoading = new ExtProgressDialog(context);
            m_dlgLoading.setMessage(strMessage);
            m_dlgLoading.setIndeterminate(true);
            // m_dlgLoading.getWindow().setType(
            // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            m_dlgLoading.setCanceledOnTouchOutside(false);
            m_dlgLoading.setCancelable(cancelable);
            m_dlgLoading.setOnCancelListener(listener);
        }
        try {
            if (null != m_dlgLoading) {
                m_dlgLoading.show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return m_dlgLoading;
    }

    /**
     * 取消加载中对话框
     */
    public static void cancelLoadingDialog() {
        try {
            if (m_dlgLoading != null) {
                m_dlgLoading.cancel();
                m_dlgLoading = null;
            }
        } catch (Exception e) {
        }
    }

    /**
     * 创建自定义进度条对话框
     *
     * @param context
     * @return
     */
    public static ExtProgressDialog createProgressDialog(Context context) {
        return new ExtProgressDialog(context);
    }

    /**
     * 创建自定义进度条对话框
     *
     * @param context
     * @param message
     * @param indeterminate
     * @param cancelable
     * @param cancelListener
     * @return
     */
    public static ExtProgressDialog showProgressDialog(Context context,
                                                       String message, boolean indeterminate, boolean cancelable,
                                                       OnCancelListener cancelListener) {
        ExtProgressDialog dialog = new ExtProgressDialog(context);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.show();
        return dialog;
    }

    public static Dialog showAlertDialog(Context context, int nMessageResId,
                                         int nPositiveBtnResId,
                                         DialogInterface.OnClickListener positiveButtonClickListener,
                                         int nNegativeBtnResId,
                                         DialogInterface.OnClickListener negativeButtonClickListener) {
        Resources res = context.getResources();
        return showAlertDialog(context, getString(res, nMessageResId),
                getString(res, nPositiveBtnResId), positiveButtonClickListener,
                getString(res, nNegativeBtnResId), negativeButtonClickListener);
    }

    public static Dialog showAlertDialog(Context context, int nTitleResId,
                                         int nMessageResId, int nPositiveBtnResId,
                                         DialogInterface.OnClickListener positiveButtonClickListener,
                                         int nNegativeBtnResId,
                                         DialogInterface.OnClickListener negativeButtonClickListener) {
        Resources res = context.getResources();
        return showAlertDialog(context, getString(res, nTitleResId),
                getString(res, nMessageResId),
                getString(res, nPositiveBtnResId), positiveButtonClickListener,
                getString(res, nNegativeBtnResId), negativeButtonClickListener);
    }

    private static String getString(Resources res, int nStringResId) {
        try {
            return res.getString(nStringResId);
        } catch (NotFoundException ex) {

        }
        return "";
    }

    public static Dialog showAlertDialog(Context context, String strMessage,
                                         String strPositiveBtnText,
                                         DialogInterface.OnClickListener positiveButtonClickListener,
                                         String strNegativeBtnText,
                                         DialogInterface.OnClickListener negativeButtonClickListener) {
        Dialog dlg = createAlertDialog(context, null, strMessage,
                strPositiveBtnText, positiveButtonClickListener,
                strNegativeBtnText, negativeButtonClickListener, true, null);
        dlg.show();
        return dlg;
    }

    public static Dialog showAlertDialog(Context context, String strTitle,
                                         String strMessage, String strPositiveBtnText,
                                         DialogInterface.OnClickListener positiveButtonClickListener,
                                         String strNegativeBtnText,
                                         DialogInterface.OnClickListener negativeButtonClickListener) {
        Dialog dlg = createAlertDialog(context, strTitle.equals("") ? ""
                        : strTitle, strMessage, strPositiveBtnText,
                positiveButtonClickListener, strNegativeBtnText,
                negativeButtonClickListener, true, null);
        try {
            dlg.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dlg;
    }

    public static void showAutoHideDialog(final Context context, final String strTitle, final String strMessage, final int duration) {
        ThreadPoolUtils.execute(() -> handler.post(() -> {
            synchronized (synObj) {
                if (wrToast != null) {
                    if (android.os.Build.VERSION.SDK_INT < 14) { // android
                        // 4.0以上不调用cancel,cancel会造成show生成异常！
                        if (wrToast.get() != null) {
                            wrToast.get().cancel();
                        }
                    }
                    if (wrToast.get() != null) {
                        refreshToast(context, strTitle, strMessage,
                                duration);
                    } else {
                        newToast(context, strTitle, strMessage,
                                duration);
                    }
                } else {
                    newToast(context, strTitle, strMessage,
                            duration);
                }
                if (wrToast.get() != null) {
                    wrToast.get().show();
                }
            }
        }));

    }

    /**
     * 创建一个自定义toast
     *
     * @param context
     * @param strTitle
     * @param strMessage
     * @param duration
     */
    @SuppressLint("ShowToast")
    private static void newToast(Context context, String strTitle,
                                 String strMessage, int duration) {
        try {
            Toast toast = Toast.makeText(context, strMessage, duration);
            wrToast = new SoftReference<Toast>(toast);
            refreshToast(context, strTitle, strMessage, duration);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 刷新自定义toast
     *
     * @param context
     * @param strTitle
     * @param strMessage
     * @param duration
     */
    private static void refreshToast(Context context, String strTitle,
                                     String strMessage, int duration) {
        LayoutInflater inflate = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.auto_hide_dialog, null);
        v.getBackground().setAlpha(180);
        TextView textView = (TextView) v.findViewById(R.id.tvMessage);
        textView.setText(strMessage);
        textView = (TextView) v.findViewById(R.id.tvTitle);
        // LinearLayout llAutoHideDialogTilte = (LinearLayout) v
        // .findViewById(R.id.llAutoHideDialogTilte);
        if (!TextUtils.isEmpty(strTitle)) {
            textView.setText(strTitle);
        } else {
            textView.setVisibility(View.GONE);
        }
        try {
            wrToast.get().setView(v);
            wrToast.get().setGravity(Gravity.CENTER, 0, 0);
            wrToast.get().setDuration(duration);
        } catch (Exception e) {

        }

    }
    public static Dialog createAlertDialog(Context context, String strTitle,
                                           String strMessage, String strPositiveBtnText,
                                           DialogInterface.OnClickListener positiveButtonClickListener,
                                           String strNegativeBtnText,
                                           DialogInterface.OnClickListener negativeButtonClickListener,
                                           boolean cancelable, OnCancelListener cancelListener) {
        AlertDialog ad = new AlertDialog(context);
        ad.setTitle(strTitle);
        ad.setMessage(strMessage);
        ad.setPositiveButton(strPositiveBtnText, positiveButtonClickListener);
        ad.setNegativeButton(strNegativeBtnText, negativeButtonClickListener);
        ad.setCancelable(cancelable);
        ad.setOnCancelListener(cancelListener);
        return ad;
    }

    /**
     * 显示列表对框菜单
     *
     * @param context
     * @param strTitle
     * @param arrItems
     * @param itemClickListener
     * @return
     */
    public static Dialog showListviewAlertMenu(Context context, String strTitle, CharSequence[] arrItems, DialogInterface.OnClickListener itemClickListener) {
        AlertListViewDialog ad = new AlertListViewDialog(context, strTitle, arrItems, itemClickListener);
        ad.setCancelable(true);
        ad.setCanceledOnTouchOutside(true);
        ad.show();
        return ad;
    }

    public static Dialog showSelectDialog(Context context, String strTitle,
                                          CharSequence[] arrItems,
                                          DialogInterface.OnClickListener itemClickListener) {
        AlertListViewDialog ad = new AlertListViewDialog(context, strTitle,
                arrItems, itemClickListener, true);
        ad.setCancelable(true);
        ad.setCanceledOnTouchOutside(true);
        ad.show();
        return ad;
    }



    /**
     * 显示日期和时间的对话框
     *
     * @param context
     * @param strTitle
     * @param dateTime                    选择的默认时间
     * @param strPositiveBtnText
     * @param positiveButtonClickListener
     * @param strNegativeBtnText
     * @param negativeButtonClickListener
     * @param isShowHasTime               是否显示小时分钟
     * @return
     */
    public static Dialog showAlertDateTimeDialog(Context context,
                                                 String strTitle, String dateTime, String strPositiveBtnText,
                                                 OnDialogTextClickListener positiveButtonClickListener,
                                                 String strNegativeBtnText,
                                                 DialogInterface.OnClickListener negativeButtonClickListener, boolean isShowHasTime) {
        AlertDateTimeDialog ad = new AlertDateTimeDialog(context);
        ad.setTitle(strTitle);
        ad.setDateTime(dateTime);
        ad.setHasTime(isShowHasTime);
        ad.setPositiveButton(strPositiveBtnText, positiveButtonClickListener);
        ad.setNegativeButton(strNegativeBtnText, negativeButtonClickListener);
        ad.show();
        return ad;
    }

    /**
     * 显示开始和结束时间的对话框
     *
     * @param context
     * @param strTitle
     * @param differTime                  相差时间（以分钟计算）
     * @param strPositiveBtnText
     * @param positiveButtonClickListener
     * @param strNegativeBtnText
     * @param negativeButtonClickListener
     * @return
     */
    public static Dialog showAlertStartAndEndTimeDialog(Context context,
                                                        String strTitle, int differTime, String strPositiveBtnText,
                                                        OnStartAndEndTimeClickListener positiveButtonClickListener,
                                                        String strNegativeBtnText,
                                                        DialogInterface.OnClickListener negativeButtonClickListener) {
        AlertTimeStartAndEndDialog ad = new AlertTimeStartAndEndDialog(context);
        ad.setTitle(strTitle);
        ad.setDifferTime(differTime);
        ad.setPositiveButton(strPositiveBtnText, positiveButtonClickListener);
        ad.setNegativeButton(strNegativeBtnText, negativeButtonClickListener);
        ad.show();
        return ad;
    }

    /**
     * 显示文本编辑框输入对话框
     *
     * @param context
     * @param strTitle
     * @param editName                    编辑框名称
     * @param editValue                   编辑框默认输入的值
     * @param strPositiveBtnText
     * @param positiveButtonClickListener
     * @param strNegativeBtnText
     * @param negativeButtonClickListener
     * @return
     */
    public static Dialog showAlertEditInputBoxDialog(Context context,
                                                     String strTitle, String editName, String editValue, String strPositiveBtnText,
                                                     OnDialogTextClickListener positiveButtonClickListener,
                                                     String strNegativeBtnText,
                                                     DialogInterface.OnClickListener negativeButtonClickListener) {
        AlertEditInputBoxDialog ad = new AlertEditInputBoxDialog(context);
        ad.setTitle(strTitle);
        ad.setEditKey(editName);
        ad.setEditValue(editValue);
        ad.setPositiveButton(strPositiveBtnText, positiveButtonClickListener);
        ad.setNegativeButton(strNegativeBtnText, negativeButtonClickListener);
        ad.show();
        return ad;
    }
}

class AlertDialog extends Dialog {

    private TextView m_tvMessage;
    private String m_strMessage;
    private TextView m_tvTitle;
    private TextView btnPositive, btnNegative;
    private String m_strTitle;
    private OnClickListener m_positiveButtonClickListener;
    private OnClickListener m_negativeButtonClickListener;
    private String m_strPositiveButtonText;
    private String m_strNegativeButtonText;
    private boolean m_bCreated = false;
    private View m_vContentView;
    private Context mContext;

    public AlertDialog(Context context) {
        super(context, R.style.dialog);
        mContext = context;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        m_vContentView = inflater.inflate(R.layout.alert_dialog, null);
        setContentView(m_vContentView);
        m_tvMessage = (TextView) m_vContentView.findViewById(R.id.tvMessage);
        m_tvTitle = (TextView) m_vContentView.findViewById(R.id.tvTitle);
        btnNegative = (TextView) m_vContentView.findViewById(R.id.btnNegative);
        doSetNegativeButton(m_strNegativeButtonText);
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(DialogInterface.BUTTON_NEGATIVE);
            }
        });
        btnPositive = (TextView) m_vContentView.findViewById(R.id.btnPositive);
        doSetPositiveButton(m_strPositiveButtonText);
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(DialogInterface.BUTTON_POSITIVE);
            }
        });
        setTitle(m_strTitle);
        setMessage(m_strMessage);
        if (TextUtils.isEmpty(m_strPositiveButtonText) && TextUtils.isEmpty(m_strNegativeButtonText)) {
            m_vContentView.findViewById(R.id.llButtons).setVisibility(View.GONE);
        }
        LayoutParams lp = getWindow().getAttributes();
        float widthInDp = DisplayUtil.getWidthInPx(mContext);
        lp.width = (int) (widthInDp / 1080 * 700);
        lp.gravity = Gravity.CENTER;
        this.onWindowAttributesChanged(lp);
        m_bCreated = true;
    }

    public View getContentView() {
        return m_vContentView;
    }

    public void setTitle(String strTitle) {
        m_strTitle = strTitle;
        if (null != m_tvTitle) {
            if (!TextUtils.isEmpty(m_strTitle)) {
                m_tvTitle.setText(strTitle);
                m_tvTitle.setVisibility(View.VISIBLE);
            } else {
                m_tvTitle.setVisibility(View.GONE);
            }
        }
    }

    public void setMessage(String strMessage) {
        m_strMessage = strMessage;
        if (null != m_tvMessage) {
            if (!TextUtils.isEmpty(strMessage)) {
                m_tvMessage.setText(strMessage);
                m_tvMessage.setVisibility(View.VISIBLE);
            } else {
                m_tvMessage.setVisibility(View.GONE);
            }
        }
    }

    public void setPositiveButton(String strText,
                                  OnClickListener clickListener) {
        m_positiveButtonClickListener = clickListener;
        m_strPositiveButtonText = strText;
        if (m_bCreated) {
            doSetPositiveButton(strText);
        }
    }

    private void doSetPositiveButton(String strText) {
        if (btnPositive == null)
            btnPositive = (TextView) this.findViewById(R.id.btnPositive);
        if (TextUtils.isEmpty(strText)) {
            btnPositive.setVisibility(View.GONE);
        } else {
            btnPositive.setText(strText);
            btnPositive.setVisibility(View.VISIBLE);
        }
    }

    public void setNegativeButton(String strText,
                                  OnClickListener clickListener) {
        m_negativeButtonClickListener = clickListener;
        m_strNegativeButtonText = strText;
        if (m_bCreated) {
            doSetNegativeButton(strText);
        }
    }

    private void doSetNegativeButton(String strText) {
        if (btnNegative == null)
            btnNegative = (Button) this.findViewById(R.id.btnNegative);
        if (TextUtils.isEmpty(strText)) {
            btnNegative.setVisibility(View.GONE);
        } else {
            btnNegative.setText(strText);
            btnNegative.setVisibility(View.VISIBLE);
        }
    }

    protected void onButtonClick(int whichButton) {
        if (whichButton == BUTTON_POSITIVE) {
            if (m_positiveButtonClickListener != null) {
                m_positiveButtonClickListener.onClick(this, whichButton);
            }
            this.cancel();
        } else if (whichButton == BUTTON_NEGATIVE) {
            if (m_negativeButtonClickListener != null) {
                m_negativeButtonClickListener.onClick(this, whichButton);
            }
            this.cancel();
        }
    }
}

class AlertListViewDialog extends Dialog {

    private SysAlertDialog.CancelListener ml;

    public void setcanclelistener(SysAlertDialog.CancelListener _ml) {
        ml = _ml;
    }

    private String m_strTitle;
    private CharSequence[] m_arrItems;
    private OnClickListener m_listenerItemClick;
    private boolean m_bSelect;

    public AlertListViewDialog(Context context, String strTitle, CharSequence[] arrItems, OnClickListener itemClick) {
        this(context, strTitle, arrItems, itemClick, false);
    }

    public AlertListViewDialog(Context context, String strTitle, CharSequence[] arrItems, OnClickListener itemClick, boolean select) {
        super(context, select ? R.style.selectDialog : R.style.listviewDialog);
        setCanceledOnTouchOutside(true);
        m_listenerItemClick = itemClick;
        m_arrItems = arrItems;
        m_strTitle = strTitle;
        m_bSelect = select;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.alert_listview_dialog, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        if (!TextUtils.isEmpty(m_strTitle)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(m_strTitle);
        }
        ListView lvContent = (ListView) view.findViewById(R.id.lvContent);
        lvContent.setAdapter(new ArrayAdapter<CharSequence>(getContext(),
                R.layout.alert_listview_dialog_item, m_arrItems));
        if (null != m_listenerItemClick) {
            lvContent
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            AlertListViewDialog.this.cancel();
                            m_listenerItemClick.onClick(
                                    AlertListViewDialog.this, position);
                        }
                    });
        }
        Button btnAlertCancel = (Button) view.findViewById(R.id.btnAlertCancel);
        if (m_bSelect) {
            btnAlertCancel.setVisibility(View.GONE);
        }
        btnAlertCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertListViewDialog.this.cancel();
                if (null != ml) {
                    ml.cancel();
                }
            }
        });
        setContentView(view);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        LayoutParams lp = getWindow().getAttributes();
        lp.gravity = m_bSelect ? Gravity.CENTER_VERTICAL : Gravity.BOTTOM;
        lp.width = d.getWidth();
        onWindowAttributesChanged(lp);
    }
}

/**
 * 选择日期时间的对话框
 */
class AlertDateTimeDialog extends Dialog {

    private TextView m_tvTitle;
    private String m_strTitle;
    private SysAlertDialog.OnDialogTextClickListener m_positiveButtonClickListener;
    private OnClickListener m_negativeButtonClickListener;
    private String m_strPositiveButtonText;
    private String m_strNegativeButtonText;
    private boolean m_bCreated = false;
    private View m_vContentView;
    WheelDateMain wheelMain;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private boolean hasTime = true;
    private Context mContext;
    private String dateTime;
    String[] arr;

    public AlertDateTimeDialog(Context context) {
        super(context, R.style.dialog);
        this.mContext = context;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        m_vContentView = inflater.inflate(R.layout.select_date_time_dialog,
                null);
        setContentView(m_vContentView);
        m_tvTitle = (TextView) m_vContentView.findViewById(R.id.tvTitle);
        Button btnTmp = (Button) m_vContentView.findViewById(R.id.btnNegative);
        doSetNegativeButton(m_strNegativeButtonText);
        btnTmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(DialogInterface.BUTTON_NEGATIVE);
            }
        });
        Button btnTmp1 = (Button) m_vContentView.findViewById(R.id.btnPositive);
        doSetPositiveButton(m_strPositiveButtonText);
        btnTmp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(DialogInterface.BUTTON_POSITIVE);
            }
        });
        setTitle(m_strTitle);

        ScreenInfo screenInfo = new ScreenInfo(mContext);
        wheelMain = new WheelDateMain(m_vContentView, hasTime);
        wheelMain.setTextSize(mContext.getResources().getDimensionPixelSize(
                R.dimen.text_size_16));
        wheelMain.setSTART_YEAR(1900);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(dateTime, "yyyy-MM-dd HH:mm")) {
            try {
                calendar.setTime(dateFormat.parse(dateTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (dateTime.contains("-")) {
            String[] split = dateTime.split(" ");
            arr = split[0].split("-");
        } else {
            arr = new String[]{"1970", "1", "1"};
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        if (isHasTime()) {
            wheelMain.setWidthHeight(mContext.getResources()
                    .getDimensionPixelSize(R.dimen.margin_15), mContext
                    .getResources().getDimensionPixelSize(R.dimen.padding_10));
            wheelMain.setEND_YEAR(2100);
            wheelMain.initDateTimePicker(year, month, day, hour, min);
        } else {
            wheelMain.setWidthHeight(mContext.getResources()
                    .getDimensionPixelSize(R.dimen.margin_25), mContext
                    .getResources().getDimensionPixelSize(R.dimen.padding_10));
            wheelMain.setEND_YEAR(calendar.get(Calendar.YEAR));
            year = Integer.parseInt(arr[0]);
            month = Integer.parseInt(arr[1]) - 1;
            day = Integer.parseInt(arr[2]);
            wheelMain.initDateTimePicker(year, month, day);
        }

        LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        this.onWindowAttributesChanged(lp);
        m_bCreated = true;
    }

    public View getContentView() {
        return m_vContentView;
    }

    public boolean isHasTime() {
        return hasTime;
    }

    public void setHasTime(boolean hasTime) {
        this.hasTime = hasTime;
    }

    public void setTitle(String strTitle) {
        m_strTitle = strTitle;
        if (null != m_tvTitle) {
            if (!TextUtils.isEmpty(m_strTitle)) {
                m_tvTitle.setText(strTitle);
                m_tvTitle.setVisibility(View.VISIBLE);
            } else {
                m_tvTitle.setVisibility(View.GONE);
            }
        }
    }

    public void setPositiveButton(String strText,
                                  SysAlertDialog.OnDialogTextClickListener clickListener) {
        m_positiveButtonClickListener = clickListener;
        m_strPositiveButtonText = strText;
        if (m_bCreated) {
            doSetPositiveButton(strText);
        }
    }

    private void doSetPositiveButton(String strText) {
        Button btnPositive = (Button) this.findViewById(R.id.btnPositive);
        if (TextUtils.isEmpty(strText)) {
            btnPositive.setVisibility(View.GONE);
        } else {
            btnPositive.setText(strText);
            btnPositive.setVisibility(View.VISIBLE);
        }
    }

    public void setDateTime(String time) {
        this.dateTime = time;
    }

    public void setNegativeButton(String strText,
                                  OnClickListener clickListener) {
        m_negativeButtonClickListener = clickListener;
        m_strNegativeButtonText = strText;
        if (m_bCreated) {
            doSetNegativeButton(strText);
        }
    }

    private void doSetNegativeButton(String strText) {
        Button btnNegative = (Button) this.findViewById(R.id.btnNegative);
        if (TextUtils.isEmpty(strText)) {
            btnNegative.setVisibility(View.GONE);
        } else {
            btnNegative.setText(strText);
            btnNegative.setVisibility(View.VISIBLE);
        }
    }

    protected void onButtonClick(int whichButton) {
        if (whichButton == BUTTON_POSITIVE) {
            if (m_positiveButtonClickListener != null) {
                m_positiveButtonClickListener.onClick(this, whichButton,
                        wheelMain.getTime());
            }
            this.cancel();
        } else if (whichButton == BUTTON_NEGATIVE) {
            if (m_negativeButtonClickListener != null) {
                m_negativeButtonClickListener.onClick(this, whichButton);
            }
            this.cancel();
        }
    }
}

/**
 * 选择开始和结束时间的对话框
 */
class AlertTimeStartAndEndDialog extends Dialog {

    private TextView m_tvTitle;
    private String m_strTitle;
    private SysAlertDialog.OnStartAndEndTimeClickListener m_positiveButtonClickListener;
    private OnClickListener m_negativeButtonClickListener;
    private String m_strPositiveButtonText;
    private String m_strNegativeButtonText;
    private boolean m_bCreated = false;
    private View m_vContentView;
    private WheelStartAndEndTimeMain wheelMain;
    private Context mContext;
    private int differTime;

    public AlertTimeStartAndEndDialog(Context context) {
        super(context, R.style.dialog);
        this.mContext = context;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        m_vContentView = inflater.inflate(
                R.layout.select_start_and_end_time_dialog, null);
        setContentView(m_vContentView);
        m_tvTitle = (TextView) m_vContentView.findViewById(R.id.tvTitle);
        Button btnTmp = (Button) m_vContentView.findViewById(R.id.btnNegative);
        doSetNegativeButton(m_strNegativeButtonText);
        btnTmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(DialogInterface.BUTTON_NEGATIVE);
            }
        });
        Button btnTmp1 = (Button) m_vContentView.findViewById(R.id.btnPositive);
        doSetPositiveButton(m_strPositiveButtonText);
        btnTmp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(DialogInterface.BUTTON_POSITIVE);
            }
        });
        setTitle(m_strTitle);

        ScreenInfo screenInfo = new ScreenInfo(mContext);
        wheelMain = new WheelStartAndEndTimeMain(m_vContentView);
        wheelMain.setTextSize(mContext.getResources().getDimensionPixelSize(
                R.dimen.text_size_16));
        wheelMain.screenheight = screenInfo.getHeight();
        wheelMain.setWidthHeight(
                mContext.getResources()
                        .getDimensionPixelSize(R.dimen.margin_15),
                mContext.getResources().getDimensionPixelSize(
                        R.dimen.padding_10));
        wheelMain.initTimePicker(getDifferTime());
        LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        this.onWindowAttributesChanged(lp);
        m_bCreated = true;
    }

    public int getDifferTime() {
        return differTime;
    }

    public void setDifferTime(int differTime) {
        this.differTime = differTime;
    }

    public void setTitle(String strTitle) {
        m_strTitle = strTitle;
        if (null != m_tvTitle) {
            if (!TextUtils.isEmpty(m_strTitle)) {
                m_tvTitle.setText(strTitle);
                m_tvTitle.setVisibility(View.VISIBLE);
            } else {
                m_tvTitle.setVisibility(View.GONE);
            }
        }
    }

    public void setPositiveButton(String strText,
                                  SysAlertDialog.OnStartAndEndTimeClickListener clickListener) {
        m_positiveButtonClickListener = clickListener;
        m_strPositiveButtonText = strText;
        if (m_bCreated) {
            doSetPositiveButton(strText);
        }
    }

    private void doSetPositiveButton(String strText) {
        Button btnPositive = (Button) this.findViewById(R.id.btnPositive);
        if (TextUtils.isEmpty(strText)) {
            btnPositive.setVisibility(View.GONE);
        } else {
            btnPositive.setText(strText);
            btnPositive.setVisibility(View.VISIBLE);
        }
    }

    public void setNegativeButton(String strText,
                                  OnClickListener clickListener) {
        m_negativeButtonClickListener = clickListener;
        m_strNegativeButtonText = strText;
        if (m_bCreated) {
            doSetNegativeButton(strText);
        }
    }

    private void doSetNegativeButton(String strText) {
        Button btnNegative = (Button) this.findViewById(R.id.btnNegative);
        if (TextUtils.isEmpty(strText)) {
            btnNegative.setVisibility(View.GONE);
        } else {
            btnNegative.setText(strText);
            btnNegative.setVisibility(View.VISIBLE);
        }
    }

    protected void onButtonClick(int whichButton) {
        if (whichButton == BUTTON_POSITIVE) {
            if (m_positiveButtonClickListener != null) {
                m_positiveButtonClickListener.onClick(this, whichButton,
                        wheelMain.getStartTime(), wheelMain.getEndTime());
            }
            this.cancel();
        } else if (whichButton == BUTTON_NEGATIVE) {
            if (m_negativeButtonClickListener != null) {
                m_negativeButtonClickListener.onClick(this, whichButton);
            }
            this.cancel();
        }
    }
}


class AlertEditInputBoxDialog extends Dialog {

    private TextView m_tvEditKey;
    private EditText m_etvEditValue;
    private String m_strEditKey;
    private String m_strEditValue;
    private TextView m_tvTitle;
    private Button btnPositive, btnNegative;
    private String m_strTitle;
    private SysAlertDialog.OnDialogTextClickListener m_positiveButtonClickListener;
    private OnClickListener m_negativeButtonClickListener;
    private String m_strPositiveButtonText;
    private String m_strNegativeButtonText;
    private boolean m_bCreated = false;
    private View m_vContentView;

    public AlertEditInputBoxDialog(Context context) {
        super(context, R.style.dialog);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        m_vContentView = inflater.inflate(R.layout.alert_edit_input_box_dialog, null);
        setContentView(m_vContentView);
        m_tvEditKey = (TextView) m_vContentView.findViewById(R.id.tv_edit_key);
        m_etvEditValue = (EditText) m_vContentView.findViewById(R.id.etv_edit_value);
        m_tvTitle = (TextView) m_vContentView.findViewById(R.id.tvTitle);
        btnNegative = (Button) m_vContentView.findViewById(R.id.btnNegative);
        doSetNegativeButton(m_strNegativeButtonText);
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(DialogInterface.BUTTON_NEGATIVE);
            }
        });
        btnPositive = (Button) m_vContentView.findViewById(R.id.btnPositive);
        doSetPositiveButton(m_strPositiveButtonText);
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(DialogInterface.BUTTON_POSITIVE);
            }
        });
        setTitle(m_strTitle);
        setEditKey(m_strEditKey);
        setEditValue(m_strEditValue);
        if (TextUtils.isEmpty(m_strPositiveButtonText) && TextUtils.isEmpty(m_strNegativeButtonText)) {
            m_vContentView.findViewById(R.id.llButtons).setVisibility(View.GONE);
        }
        LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        this.onWindowAttributesChanged(lp);
        m_bCreated = true;
    }

    public View getContentView() {
        return m_vContentView;
    }

    public void setTitle(String strTitle) {
        m_strTitle = strTitle;
        if (null != m_tvTitle) {
            if (!TextUtils.isEmpty(m_strTitle)) {
                m_tvTitle.setText(strTitle);
                m_tvTitle.setVisibility(View.VISIBLE);
            } else {
                m_tvTitle.setVisibility(View.GONE);
            }
        }
    }

    public void setEditKey(String strEditKey) {
        m_strEditKey = strEditKey;
        if (null != m_tvEditKey) {
            if (!TextUtils.isEmpty(strEditKey)) {
                m_tvEditKey.setText(strEditKey);
                m_tvEditKey.setVisibility(View.VISIBLE);
            } else {
                m_tvEditKey.setVisibility(View.GONE);
            }
        }
    }

    public void setEditValue(String strEditValue) {
        m_strEditValue = strEditValue;
        if (null != m_etvEditValue) {
            if (!TextUtils.isEmpty(strEditValue)) {
                m_etvEditValue.setText(strEditValue);
                m_etvEditValue.setSelection(strEditValue.length());
            }
        }
    }

    public void setPositiveButton(String strText,
                                  SysAlertDialog.OnDialogTextClickListener clickListener) {
        m_positiveButtonClickListener = clickListener;
        m_strPositiveButtonText = strText;
        if (m_bCreated) {
            doSetPositiveButton(strText);
        }
    }

    private void doSetPositiveButton(String strText) {
        if (btnPositive == null)
            btnPositive = (Button) this.findViewById(R.id.btnPositive);
        if (TextUtils.isEmpty(strText)) {
            btnPositive.setVisibility(View.GONE);
        } else {
            btnPositive.setText(strText);
            btnPositive.setVisibility(View.VISIBLE);
        }
    }

    public void setNegativeButton(String strText,
                                  OnClickListener clickListener) {
        m_negativeButtonClickListener = clickListener;
        m_strNegativeButtonText = strText;
        if (m_bCreated) {
            doSetNegativeButton(strText);
        }
    }

    private void doSetNegativeButton(String strText) {
        if (btnNegative == null)
            btnNegative = (Button) this.findViewById(R.id.btnNegative);
        if (TextUtils.isEmpty(strText)) {
            btnNegative.setVisibility(View.GONE);
        } else {
            btnNegative.setText(strText);
            btnNegative.setVisibility(View.VISIBLE);
        }
    }

    protected void onButtonClick(int whichButton) {
        if (whichButton == BUTTON_POSITIVE) {
            if (m_positiveButtonClickListener != null) {
                m_positiveButtonClickListener.onClick(this, whichButton, m_etvEditValue.getText().toString());
            }
            this.cancel();
        } else if (whichButton == BUTTON_NEGATIVE) {
            if (m_negativeButtonClickListener != null) {
                m_negativeButtonClickListener.onClick(this, whichButton);
            }
            this.cancel();
        }
    }
}