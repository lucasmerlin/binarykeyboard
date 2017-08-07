package media.merlins.binarykeyboard;

/**
 * Created by lucas on 07.11.2016.
 */
public interface KeyboardListener {

    void previewTextChanged(String text);

    void sendKey(char letter);

    void previewLetterChanged(int letter);

}
