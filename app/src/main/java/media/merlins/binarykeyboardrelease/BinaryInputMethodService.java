package media.merlins.binarykeyboardrelease;

import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lucas on 1entryLength - 1.06.2016.
 */
public class BinaryInputMethodService extends InputMethodService {
    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        //sendKeyChar('*');
    }

    TextView output;

    UnicodeListView unicodeList;

    Settings settings;
    KeyboardType keyboard;

    Timer backButtonTimer = new Timer();
    boolean isLongClick = false;

    @Override
    public View onCreateInputView() {
        settings = new Settings();

        keyboard = new TypeBinaryKeyboard();
        keyboard.setup(this, settings);



        LayoutInflater lif = LayoutInflater.from(this);
        final View v = lif.inflate(R.layout.keyboard, null);
        output = (TextView) v.findViewById(R.id.textView);
        unicodeList = (UnicodeListView) v.findViewById(R.id.unicodeList);

        final SharedPreferences prefs = getSharedPreferences("default", MODE_PRIVATE);
        v.findViewById(R.id.unicodeList).setVisibility(prefs.getBoolean("unicodeListVisible", true) ? View.VISIBLE : View.GONE);
        settings.setBitLength((byte) prefs.getInt("bitLength", 8));

        ((RadioButton) v.findViewById(R.id.bit8Button)).setChecked(settings.getBitLength() == 8);
        ((RadioButton) v.findViewById(R.id.bit16Button)).setChecked(settings.getBitLength() == 16);


        ((LinearLayout) v.findViewById(R.id.keyboardContainer)).addView(keyboard.getView(((LinearLayout) v.findViewById(R.id.keyboardContainer))));


        keyboard.addListener(new KeyboardListener() {

            @Override
            public void previewTextChanged(String text) {
                updateDescText(text);
            }

            @Override
            public void sendKey(char letter) {
                sendKeyChar(letter);
            }

            @Override
            public void previewLetterChanged(int letter) {
                unicodeList.setSelection(letter);
            }
        });




        v.findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (keyboard.backKeyPressed())
                    getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));

                isLongClick = false;

            }
        });
        v.findViewById(R.id.buttonBack).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                backButtonTimer = new Timer();
                backButtonTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {

                        v.post(new Runnable() {
                            @Override
                            public void run() {
                                if (keyboard.backKeyPressed())
                                    getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                            }
                        });

                    }
                }, 100, 100);

                isLongClick = true;

                return true;
            }
        });
        v.findViewById(R.id.buttonBack).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (isLongClick)
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        try {
                            backButtonTimer.cancel();
                        } catch (Exception ignore) {}

                        return false;
                    }

                return false;
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
                settings.setBitLength((byte) 8);
                updateDescText(keyboard.getDescText());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("bitLength", 8);
                editor.apply();
            }
        });
        v.findViewById(R.id.bit16Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.setBitLength((byte) 16);
                updateDescText(keyboard.getDescText());
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

//        unicodeList.setOnUnicodeEntryClickedListener(new UnicodeListView.OnUnicodeEntryClickedListener() {
//            @Override
//            public void clicked(char unicodeChar) {
//                letter = unicodeChar;
//                counter = bitLength;
//                sendKeyChar(letter);
//            }
//        });


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



        keyboard.ready();

        return v;

    }


    void updateDescText(String htmlText) {
        output.setText(Html.fromHtml(htmlText));
    }

}
