package pl.ordin.authorchatforwordpress;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;

import java.util.Random;

/**
 * {@link Utility} is a bunch of other methods used by few classes.
 */
public class Utility extends ContextWrapper {

    public Utility(Context context) { // Context&activity constructor
        super(context);
    }

    //responsible for alerts pop-ups
    public void warningAlert(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.show();
    }

    //generate rgb color (string as a seed)
    public int hexColorGenerator(String seed) {
        long hash = seed.hashCode();
        Random rnd = new Random(hash);

        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        return color;
    }
}