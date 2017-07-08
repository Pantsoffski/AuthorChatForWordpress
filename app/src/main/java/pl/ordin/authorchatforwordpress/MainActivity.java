package pl.ordin.authorchatforwordpress;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.net.URL;

/**
 * {@link MainActivity} shows Author Chat from Wordpress website.
 */
public class MainActivity extends AppCompatActivity { // TODO: 01.07.2017 add comments

    final Handler handler = new Handler();
    Runnable r;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        handler.postDelayed(r, 4000);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getContent();

        //new chat lines checking, if new line available, refresh adapter and view
        r = new Runnable() {
            public void run() {
                getContent();
                handler.postDelayed(this, 4000);
            }
        };

        //hide action bar on chat view
        if (getSupportActionBar().isShowing()) {
            getSupportActionBar().hide();
        }
    }

    //get content from AsyncTask
    private void getContent() {
        settings = getSharedPreferences("AuthorChatSettings", 0);
        //Perform the doInBackground method, passing in our url
        try {
            //get values from SharedPreferences
            URL domain = new URL(settings.getString("domain", "none") + "/wp-json/author-chat/v2/chat/");

            new HttpGetRequest(this).execute(domain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(r);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(r);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(r, 4000);
    }
}