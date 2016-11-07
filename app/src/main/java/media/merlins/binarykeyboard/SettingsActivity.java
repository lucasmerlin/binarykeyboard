package media.merlins.binarykeyboard;

import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        final ListView lv = (ListView) findViewById(R.id.unicodeList);

        findViewById(R.id.btnKeyboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showInputMethodPicker();
            }
        });


        lv.setAdapter(new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEnabled(int i) {
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public int getCount() {
                return 0xFFFF;
            }

            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {

                if (view == null) {
                    view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.unicode_list_element, null);
                }

                String dec = String.valueOf(i);
                while (dec.length() < 5) dec = "0" + dec;

                String hex = Integer.toHexString(i);
                while (hex.length() < 4) hex = "0" + hex;

                String bin = Integer.toBinaryString(i);
                while (bin.length() < 16) bin = "0" + bin;


                char letter = (char) i;

                TextView txtDec = (TextView) view.findViewById(R.id.txtDec);
                TextView txtHex = (TextView) view.findViewById(R.id.txtHex);
                TextView txtBin = (TextView) view.findViewById(R.id.txtBin);
                TextView txtLetter = (TextView) view.findViewById(R.id.txtLetter);

                txtDec.setText(dec);
                txtHex.setText(hex);
                txtBin.setText(bin);
                txtLetter.setText(String.valueOf(letter));

                return view;
            }

            @Override
            public int getItemViewType(int i) {
                return 1;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        });



        lv.post(new Runnable() {
            @Override
            public void run() {
                lv.setSelection(0x41);
            }
        });

    }
}
