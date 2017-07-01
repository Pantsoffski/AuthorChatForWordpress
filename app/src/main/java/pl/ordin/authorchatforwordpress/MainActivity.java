package pl.ordin.authorchatforwordpress;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import static pl.ordin.authorchatforwordpress.ChatCreator.pin;

/**
 * {@link MainActivity} shows Author Chat from Wordpress website.
 */
public class MainActivity extends AppCompatActivity { // TODO: 01.07.2017 add comments and auto-refresh

    private FloatingActionButton downButton;
    private ListView listView;
    private CustomAdapter adapterRefreshed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);

        downButton = (FloatingActionButton) findViewById(R.id.downFAB);

        //Instantiate new instance of our class
        HttpGetRequest getRequest = new HttpGetRequest();

        SharedPreferences settings = getSharedPreferences("AuthorChatSettings", 0);

        ArrayList<CustomArrayList> result;
        //Perform the doInBackground method, passing in our url
        try {
            //get values from SharedPreferences
            String domain = settings.getString("domain", "none");

            result = getRequest.execute(domain + "/wp-json/author-chat/v2/chat/").get();
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }

        if (result != null) {
            //validate PIN code
            int userPin = settings.getInt("code", 0);
            if (pin != userPin) {
                new Utility(this).warningAlert("Info", "PIN code doesn't match, plz go back and check it again!");
                return;
            }
            adapterRefreshed = new CustomAdapter(getApplicationContext(), result);
            listView.setAdapter(adapterRefreshed);
            listView.setSelection(adapterRefreshed.getCount() - 1); //scroll down at start
        } else {
            new Utility(this).warningAlert("Info", "Oops! Something went wrong... Plz go back and check domain name!");
            return;
        }

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                //hide/show down button when scroll up&down
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    if (downButton.getVisibility() == View.VISIBLE) {
                        downButton.setVisibility(View.INVISIBLE);
                    }

                } else {
                    if (downButton.getVisibility() == View.INVISIBLE) {
                        downButton.setVisibility(View.VISIBLE);
                    }


                }
            }
        });

        //scroll to bottom when floating button is pressed
        downButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listView.setSelection(adapterRefreshed.getCount() - 1);
            }
        });

        //hide action bar on chat view
        if (getSupportActionBar().isShowing()) {
            getSupportActionBar().hide();
        }
    }
}