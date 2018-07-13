package media.merlins.binarykeyboardrelease;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * TODO: document your custom view class.
 */
public class UnicodeListView extends ListView {

    UnicodeListAdapter listAdapter;

    private OnUnicodeEntryClickedListener onUnicodeEntryClickedListener;


    public UnicodeListView(Context context) {
        super(context);
        init();
    }

    public UnicodeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnicodeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UnicodeListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {
        listAdapter = new UnicodeListAdapter(getContext());
        setAdapter(listAdapter);
    }

    public OnUnicodeEntryClickedListener getOnUnicodeEntryClickedListener() {
        return onUnicodeEntryClickedListener;
    }

    public void setOnUnicodeEntryClickedListener(OnUnicodeEntryClickedListener onUnicodeEntryClickedListener) {
        this.onUnicodeEntryClickedListener = onUnicodeEntryClickedListener;
    }


    interface OnUnicodeEntryClickedListener {
        void clicked(char unicodeChar);
    }
}
