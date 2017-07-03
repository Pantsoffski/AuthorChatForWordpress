package pl.ordin.authorchatforwordpress;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import static pl.ordin.authorchatforwordpress.ChatCreator.pin;

/**
 * {@link MainActivity} shows Author Chat from Wordpress website.
 */
public class MainActivity extends AppCompatActivity { // TODO: 01.07.2017 add comments and auto-refresh

    private FloatingActionButton downButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //downButton = (FloatingActionButton) findViewById(R.id.downFAB);

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
            //create adapter and connect it with RecyclerView
            recyclerView.setAdapter(new RecyclerViewAdapter(result, recyclerView));
        } else {
            new Utility(this).warningAlert("Info", "Oops! Something went wrong... Plz go back and check domain name!");
            return;
        }

        //scroll to bottom when floating button is pressed
/*        downButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listView.setSelection(adapterRefreshed.getCount() - 1);
            }
        });*/

        //hide action bar on chat view
        if (getSupportActionBar().isShowing()) {
            getSupportActionBar().hide();
        }
    }
}