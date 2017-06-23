package pl.ordin.authorchatforwordpress;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * {@link CustomAdapter} this is my custom adapter, responsible for creating custom list.
 */
class CustomAdapter extends ArrayAdapter<String> { // TODO: 23.06.2017 develop adapter so it can show 3 textviews - for name, date and msg text 

    CustomAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, 0, objects);
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        String currentString = getItem(position);

        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.textView);

        defaultTextView.setBackgroundColor(Color.parseColor("#d3b8ae")); //set day number background color
        defaultTextView.setTypeface(Typeface.DEFAULT_BOLD); //set day number text to bold
        defaultTextView.setGravity(Gravity.CENTER); //set day number text center

        defaultTextView.setText(currentString);


        return listItemView;
    }
}
