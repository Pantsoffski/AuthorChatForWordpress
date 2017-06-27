package pl.ordin.authorchatforwordpress;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * {@link CustomAdapter} this is my custom adapter, responsible for creating custom list.
 */
class CustomAdapter extends ArrayAdapter<CustomArrayList> {

    CustomAdapter(@NonNull Context context, @NonNull ArrayList<CustomArrayList> objects) {
        super(context, 0, objects);
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        CustomArrayList currentString = getItem(position);

        TextView chatView = (TextView) listItemView.findViewById(R.id.chatView);
        TextView nickView = (TextView) listItemView.findViewById(R.id.nickView);
        TextView dateView = (TextView) listItemView.findViewById(R.id.dateView);

        assert currentString != null : "currentString is null";

        nickView.setText(currentString.nick);
        dateView.setText(currentString.date);
        chatView.setText(currentString.chat);

        return listItemView;
    }
}
