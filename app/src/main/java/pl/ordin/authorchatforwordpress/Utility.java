package pl.ordin.authorchatforwordpress;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.NotificationCompat;

import java.util.Random;

/**
 * {@link Utility} is a bunch of other methods used by few classes.
 */
public class Utility extends ContextWrapper {

    Context context;

    public Utility(Context context) { // Context&activity constructor
        super(context);
        this.context = context;
    }

    //responsible for alerts pop-ups
    public void warningAlert(String title, String message) {
        final Intent startIntent = new Intent(this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(startIntent);
            }
        });
        alertDialog.show();
    }

    //generate rgb color (string as a seed)
    public int hexColorGenerator(String seed) {
        long hash = seed.hashCode();
        Random rnd = new Random(hash);

        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        return color;
    }

    //notifications
    public void pushNotification() {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_announcement)
                        .setContentTitle("Author Chat")
                        .setContentText("New message is here!");
        int NOTIFICATION_ID = 54387;

        Intent targetIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }
}