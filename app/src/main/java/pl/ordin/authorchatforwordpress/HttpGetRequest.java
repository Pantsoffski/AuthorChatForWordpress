package pl.ordin.authorchatforwordpress;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static pl.ordin.authorchatforwordpress.ChatCreator.pin;

/**
 * Class {@link HttpGetRequest} is responsible for url leech.
 */
class HttpGetRequest extends AsyncTask<URL, Void, ArrayList<CustomArrayList>> {
    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;
    private Activity activity;
    private boolean progressOnce = true;

    HttpGetRequest(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        //start progress bar visibility
        if (activity != null && progressOnce) {
            ProgressBar bar = (ProgressBar) activity.findViewById(R.id.progressBar);
            bar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected ArrayList<CustomArrayList> doInBackground(URL... urls) {
        ArrayList<CustomArrayList> firstResult;

        try {
            //Create a URL object holding our url
            URL myUrl = urls[0];

            //Create a connection
            HttpURLConnection connection = (HttpURLConnection)
                    myUrl.openConnection();

            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            //Connect to our url
            connection.connect();

            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());

            ChatCreator chatCreator = new ChatCreator();

            firstResult = chatCreator.readJsonStream(streamReader);

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            firstResult = null;
        }

        return firstResult;
    }

    @Override
    protected void onPostExecute(final ArrayList<CustomArrayList> result) {
        //end progress bar visibility
        if (activity != null) {
            ProgressBar bar = (ProgressBar) activity.findViewById(R.id.progressBar);
            bar.setVisibility(View.INVISIBLE);
            progressOnce = false;
        }

        final RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        SharedPreferences settings = activity.getSharedPreferences("AuthorChatSettings", 0);

        if (result != null) {
            //validate PIN code
            int userPin = settings.getInt("code", 0);
            if (pin != userPin) {
                new Utility(activity).warningAlert("Info", "PIN code doesn't match, plz go back and check it again!");
                return;
            }
            //create adapter and connect it with RecyclerView
            final RecyclerViewAdapter adapter = new RecyclerViewAdapter(result);
            recyclerView.setAdapter(adapter);
            recyclerView.scrollToPosition(result.size() - 1); //scroll to bottom at start

            //scroll to bottom when floating button is pressed
            final FloatingActionButton downButton = (FloatingActionButton) activity.findViewById(R.id.downFAB);
            final int resultSize = result.size();
            downButton.setAlpha(0.25f);
            downButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    downButton.setAlpha(1f);
                    recyclerView.scrollToPosition(resultSize - 1);
                }
            });
        } else {
            new Utility(activity).warningAlert("Info", "Oops! Something went wrong... Plz go back and check domain name!");
            return;
        }

        super.onPostExecute(result);
    }

}
