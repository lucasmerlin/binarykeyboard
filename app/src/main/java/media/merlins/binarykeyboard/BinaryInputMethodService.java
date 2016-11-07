package media.merlins.binarykeyboard;

import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by lucas on 1entryLength - 1.06.2016.
 */
public class BinaryInputMethodService extends InputMethodService {
    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        //sendKeyChar('*');
    }

    byte bitLength = 16;
    
    char letter = 0;
    byte counter = 0;

    TextView output;

    UnicodeListView unicodeList;

    @Override
    public View onCreateInputView() {

        LayoutInflater lif = LayoutInflater.from(this);
        final View v = lif.inflate(R.layout.keyboard, null);
        output = (TextView) v.findViewById(R.id.textView);
        unicodeList = (UnicodeListView) v.findViewById(R.id.unicodeList);

        final SharedPreferences prefs = getSharedPreferences("default", MODE_PRIVATE);
        v.findViewById(R.id.unicodeList).setVisibility(prefs.getBoolean("unicodeListVisible", true) ? View.VISIBLE : View.GONE);
        bitLength = (byte) prefs.getInt("bitLength", 8);

        ((RadioButton) v.findViewById(R.id.bit8Button)).setChecked(bitLength == 8);
        ((RadioButton) v.findViewById(R.id.bit16Button)).setChecked(bitLength == 16);




        v.findViewById(R.id.btn0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter == bitLength) {
                    letter = 0;
                    counter = 0;
                }
                set0(counter++);
                updateDescText();
                if (counter == bitLength) {
                    sendKeyChar(letter);
                }
            }
        });
        v.findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter == bitLength) {
                    letter = 0;
                    counter = 0;
                }
                set1(counter++);
                updateDescText();
                if (counter == bitLength) {
                    sendKeyChar(letter);
                }
                unicodeList.setSelection((int)letter);
            }
        });
        v.findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter == bitLength || counter == 0) {
                    getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                }
                if (counter > 0) {
                    set0(--counter);
                    updateDescText();
                }
            }
        });
        v.findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showInputMethodPicker();
            }
        });

        v.findViewById(R.id.bit8Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitLength = 8;
                updateDescText();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("bitLength", 8);
                editor.apply();
            }
        });
        v.findViewById(R.id.bit16Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitLength = 16;
                updateDescText();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("bitLength", 16);
                editor.apply();
            }
        });
        v.findViewById(R.id.btnSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View loutSettings = v.findViewById(R.id.loutSettings);
                loutSettings.setVisibility(loutSettings.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
        final CheckBox chkUnicodeList = (CheckBox) v.findViewById(R.id.chkUnicodeList);
        chkUnicodeList.setChecked(prefs.getBoolean("unicodeListVisible", true));
        chkUnicodeList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                unicodeList.setVisibility(b ? View.VISIBLE : View.GONE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("unicodeListVisible", b);
                editor.apply();
            }
        });

/*
        unicodeList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                Log.d("BK", "Scroll: " + i + ", " + i1 + ", " + i2);
            }
        });



        unicodeList.setFastScrollEnabled(true);
        unicodeList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("BK", motionEvent.toString());
                Log.d("BK", "x: " + motionEvent.getX() + "y: " + motionEvent.getY());

                if ((int) motionEvent.getY() == (float) motionEvent.getY() && motionEvent.getAction()==MotionEvent.ACTION_MOVE) {
                    v.dispatchTouchEvent(MotionEvent.obtain(motionEvent.getDownTime(), motionEvent.getEventTime(), MotionEvent.ACTION_UP, motionEvent.getX(), motionEvent.getY(), motionEvent.getMetaState()));
                    return true;
                }

                return false;
            }
        });*/

        unicodeList.setOnUnicodeEntryClickedListener(new UnicodeListView.OnUnicodeEntryClickedListener() {
            @Override
            public void clicked(char unicodeChar) {
                letter = unicodeChar;
                counter = bitLength;
                sendKeyChar(letter);
            }
        });


        /*((CheckBox) v.findViewById(R.id.checkBox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    bitLength = 16;
                } else {
                    bitLength = 8;
                }
            }
        });*/


        return v;

    }

    void set0(byte at) {
        letter = (char) (letter & ~(1 << (bitLength - 1 - at)));
    }

    void set1(byte at) {
        letter = (char) (letter | (1 << (bitLength - 1 - at)));
    }

    void updateDescText() {
        output.setText(Html.fromHtml(getDescText()));
    }

    String getDescText() {
        String text = "";
        for (int i = bitLength - 1; i >= 0; i-- ) {
            if (i == bitLength - 1 - counter) text+="<u>";
            text += (letter & 1 << i) == 0 ? "0" : "1";
            if (i == bitLength - 1 - counter) text+="</u>";

            if (bitLength > 8 && i % 8 == 0 && i != 0) text += " ";
        }
        text += ": " + letter;
        return text;
    }
}
