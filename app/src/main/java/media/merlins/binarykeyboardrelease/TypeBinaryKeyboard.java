package media.merlins.binarykeyboardrelease;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 07.11.2016.
 */
public class TypeBinaryKeyboard implements KeyboardType {

    LinearLayout mainLayout;

    List<KeyboardListener> listeners = new ArrayList<>();

    Context context;

    Settings settings;


    char letter = 0;
    byte counter = 0;


    @Override
    public void setup(Context context, final Settings settings) {
        this.settings = settings;
        this.context = context;
    }

    @Override
    public void addListener(KeyboardListener listener) {
        listeners.add(listener);
    }

    @Override
    public View getView(ViewGroup parent) {

        if (mainLayout == null) {

            mainLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.binary_keyboard, parent, false);


            mainLayout.findViewById(R.id.btn0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (counter == settings.getBitLength()) {
                        letter = 0;
                        counter = 0;
                    }
                    set0(counter++);
                    updateDescText();
                    if (counter == settings.getBitLength()) {
                        sendKeyChar(letter);
                    }
                }
            });
            mainLayout.findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (counter == settings.getBitLength()) {
                        letter = 0;
                        counter = 0;
                    }
                    set1(counter++);
                    updateDescText();
                    if (counter == settings.getBitLength()) {
                        sendKeyChar(letter);
                    }
                    //unicodeList.setSelection((int)letter);
                    listeners.get(0).previewLetterChanged(letter);
                }
            });
        }

        return mainLayout;
    }

    @Override
    public boolean backKeyPressed() {

        if (counter > 0) {
            set0(--counter);
            updateDescText();
            listeners.get(0).previewLetterChanged(letter);
        }

        return counter == settings.getBitLength() || counter == 0;
    }


    void set0(byte at) {
        letter = (char) (letter & ~(1 << (settings.getBitLength() - 1 - at)));
    }

    void set1(byte at) {
        letter = (char) (letter | (1 << (settings.getBitLength() - 1 - at)));
    }


    @Override
    public String getDescText() {
        String text = "";
        for (int i = settings.getBitLength() - 1; i >= 0; i-- ) {
            if (i == settings.getBitLength() - 1 - counter) text+="<u>";
            text += (letter & 1 << i) == 0 ? "0" : "1";
            if (i == settings.getBitLength() - 1 - counter) text+="</u>";

            if (settings.getBitLength() > 8 && i % 8 == 0 && i != 0) text += " ";
        }
        text += ": " + letter;
        return text;
    }

    @Override
    public void ready() {
        updateDescText();
    }

    void updateDescText() {
        for (KeyboardListener listener : listeners) {
            listener.previewTextChanged(getDescText());
        }
    }

    void sendKeyChar(char letter) {
        for (KeyboardListener listener : listeners) {
            listener.sendKey(letter);
        }
    }
}
