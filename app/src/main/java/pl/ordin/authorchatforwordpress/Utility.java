package pl.ordin.authorchatforwordpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;

/**
 * {@link Utility} is a bunch of other methods used by few classes.
 */
public class Utility extends ContextWrapper {
    Activity activity;
    Context context;

    public Utility(Context context, Activity activity) { // Context&activity constructor
        super(context);
        this.activity = activity;
    }

    public Utility(Context context) { // Context constructor
        super(context);
        this.context = context;
    }

    //responsible for alerts pop-ups
    public void warningAlert(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.show();
    }
}