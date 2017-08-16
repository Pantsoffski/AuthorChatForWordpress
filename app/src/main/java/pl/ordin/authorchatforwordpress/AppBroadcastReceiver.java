package pl.ordin.authorchatforwordpress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static pl.ordin.authorchatforwordpress.MainActivity.handler;
import static pl.ordin.authorchatforwordpress.MainActivity.r;

/**
 * {@link AppBroadcastReceiver} is my custom broadcast receiver to wake up app when screen gets on.
 */

public class AppBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            handler.removeCallbacks(r); //clean up handler
            handler.postDelayed(r, 10000); //restart handler with longest delay
        }
    }
}
