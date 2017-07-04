package pl.ordin.authorchatforwordpress;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import static pl.ordin.authorchatforwordpress.ChatCreator.pin;

/**
 * {@link MainActivity} shows Author Chat from Wordpress website.
 */
public class MainActivity extends AppCompatActivity { // TODO: 01.07.2017 add comments

    final Handler handler = new Handler();
    Runnable r;
    SharedPreferences settings;
    ArrayList<CustomArrayList> firstResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        handler.postDelayed(r, 4000);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final FloatingActionButton downButton = (FloatingActionButton) findViewById(R.id.downFAB);

        settings = getSharedPreferences("AuthorChatSettings", 0);
        firstResult = getContent();

        if (firstResult != null) {
            //validate PIN code
            int userPin = settings.getInt("code", 0);
            if (pin != userPin) {
                new Utility(this).warningAlert("Info", "PIN code doesn't match, plz go back and check it again!");
                return;
            }
            //create adapter and connect it with RecyclerView
            final RecyclerViewAdapter adapter = new RecyclerViewAdapter(firstResult);
            recyclerView.setAdapter(adapter);
            recyclerView.scrollToPosition(firstResult.size() - 1); //scroll to bottom at start

            //new chat lines checking, if new line available, refresh adapter and view
            r = new Runnable() {
                public void run() {
                    ArrayList<CustomArrayList> refreshedResult = getContent();
                    if (refreshedResult.size() != firstResult.size()) {
                        adapter.setItems(refreshedResult); //send refreshedResult to adapter
                        adapter.notifyDataSetChanged(); //notify adapter that data was changed
                        recyclerView.scrollToPosition(refreshedResult.size() - 1); //scroll to bottom
                        downButton.setAlpha(0.25f);
                    }
                    handler.postDelayed(this, 4000);
                }
            };
        } else {
            new Utility(this).warningAlert("Info", "Oops! Something went wrong... Plz go back and check domain name!");
            return;
        }

        //scroll to bottom when floating button is pressed
        final int resultSize = firstResult.size();
        downButton.setAlpha(0.25f);
        downButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                downButton.setAlpha(1f);
                recyclerView.scrollToPosition(resultSize - 1);
            }
        });

        //hide action bar on chat view
        if (getSupportActionBar().isShowing()) {
            getSupportActionBar().hide();
        }
    }

    private ArrayList<CustomArrayList> getContent() {
        ArrayList<CustomArrayList> result;

        //Instantiate new instance of our class
        HttpGetRequest getRequest = new HttpGetRequest();

        //Perform the doInBackground method, passing in our url
        try {
            //get values from SharedPreferences
            String domain = settings.getString("domain", "none");

            result = getRequest.execute(domain + "/wp-json/author-chat/v2/chat/").get();
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }

        return result;
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