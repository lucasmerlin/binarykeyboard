package media.merlins.binarykeyboard;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Created by lucas on 21.06.2016.
 */
public class UnicodeListAdapter implements ListAdapter {
    Context context;

    UnicodeListAdapter(Context context) {
        this.context = context;
    }

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
        return 1000;
    }

    @Override
    public Object getItem(int i) {
        return i<10?i:null;
    }

    @Override
    public long getItemId(int i) {
        return i<10?i:0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.unicode_list_element, null);
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
}