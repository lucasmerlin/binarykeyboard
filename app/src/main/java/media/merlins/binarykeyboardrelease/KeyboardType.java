package media.merlins.binarykeyboardrelease;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lucas on 07.11.2016.
 */
public interface KeyboardType {

    void setup(Context context, Settings settings);

    void addListener(KeyboardListener listener);

    View getView(ViewGroup parent);

    boolean backKeyPressed();

    String getDescText();

    void ready();

}
