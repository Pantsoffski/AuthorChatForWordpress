package pl.ordin.authorchatforwordpress;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import java.net.URL;
import java.util.ArrayList;

import static pl.ordin.authorchatforwordpress.HttpGetRequest.firstRun;

/**
 * {@link MainActivity} shows Author Chat from Wordpress website.
 */
public class MainActivity extends AppCompatActivity { // TODO: 02.07.2017 add more comments

    public static boolean onBackground;
    final Handler handler = new Handler();
    public RecyclerView recyclerView;
    public SharedPreferences settings;
    Runnable r;
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapter(new ArrayList<CustomArrayList>())); //set empty ArrayList for adapter to avoid error: "RecyclerView﹕ No adapter attached; skipping layout"
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        message = (EditText) findViewById(R.id.editChatMessage);

        //set listener to scroll recyclerView to bottom when keyboard pop-up
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }
        });

        getContent(recyclerView);

        handler.postDelayed(r, 4000); //delay

        //new chat lines checking, if new line available, refresh adapter and view
        r = new Runnable() {
            public void run() {
                getContent(recyclerView);
                handler.postDelayed(this, 4000);
            }
        };

        //hide action bar on chat view
        if (getSupportActionBar().isShowing()) {
            getSupportActionBar().hide();
        }
    }

    //get content from AsyncTask
    private void getContent(RecyclerView recyclerView) {
        settings = getSharedPreferences("AuthorChatSettings", 0);
        //Perform the doInBackground method, passing in our url
        try {
            //get values from SharedPreferences
            URL domain = new URL(settings.getString("domain", "none") + "/wp-json/author-chat/v2/chat/");
            String login = settings.getString("login", "none");
            String password = settings.getString("password", "none");

            new HttpGetRequest(this, recyclerView, login, password).execute(domain);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //put content to AsyncTask
    public void putContent(View view) {
        String messageText = message.getText().toString();
        if (!messageText.equals("")) {
            settings = getSharedPreferences("AuthorChatSettings", 0);
            //Perform the doInBackground method, passing in our url
            try {
                //get values from SharedPreferences
                URL domain = new URL(settings.getString("domain", "none") + "/wp-json/author-chat/v2/chat/");
                String login = settings.getString("login", "none");
                String password = settings.getString("password", "none");

                new HttpGetRequest(messageText, login, password).execute(domain);

                message.setText(""); //remove text after send

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(r); //clean up handler
        handler.postDelayed(r, 10000); //restart handler with longest delay
        onBackground = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstRun = true; //reset to first run variable in HttpGetRequest class
        getContent(recyclerView); //leech new content immediately
        handler.removeCallbacks(r); //clean up handler
        handler.postDelayed(r, 4000); //restart handler delay
        onBackground = false;
    }
}