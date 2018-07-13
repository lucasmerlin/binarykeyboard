package media.merlins.binarykeyboardrelease;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    public static final int INPUT_METHOD_RESULT = 947;
    InputMethodManager manager;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INPUT_METHOD_RESULT && checkIfEnabled()) {
            manager.showInputMethodPicker();
        }
    }

    boolean checkIfEnabled() {
        return manager.getEnabledInputMethodList().toString().contains("media.merlins.binarykeyboard");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        final ListView lv = (ListView) findViewById(R.id.unicodeList);

        findViewById(R.id.btnKeyboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkIfEnabled()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setMessage(R.string.imeDialogMessage);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent in = new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS);
                            startActivityForResult(in, INPUT_METHOD_RESULT);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    manager.showInputMethodPicker();
                }


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
