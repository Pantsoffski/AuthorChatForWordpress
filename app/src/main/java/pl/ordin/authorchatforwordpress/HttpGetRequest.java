package pl.ordin.authorchatforwordpress;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
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
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;
    private static boolean firstRun = true;
    private static RecyclerViewAdapter adapter;
    private static ArrayList<CustomArrayList> newResult, oldResult;
    private String requestMethod = "";
    private Activity activity;
    private RecyclerView recyclerView;

    HttpGetRequest(Activity activity, RecyclerView recyclerView, String method) {
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.requestMethod = method;
    }

    @Override
    protected void onPreExecute() {
        //start progress bar visibility
        if (activity != null && firstRun) {
            ProgressBar bar = (ProgressBar) activity.findViewById(R.id.progressBar);
            bar.setVisibility(View.VISIBLE);
        }

        if (newResult != null) {
            oldResult = newResult;
        }
    }

    @Override
    protected ArrayList<CustomArrayList> doInBackground(URL... urls) {

        try {
            //Create a URL object holding our url
            URL myUrl = urls[0];

            //Create a connection
            HttpURLConnection connection = (HttpURLConnection)
                    myUrl.openConnection();

            //Set methods and timeouts
            connection.setRequestMethod(requestMethod);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            //Connect to our url
            connection.connect();

            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());

            ChatCreator chatCreator = new ChatCreator();

            newResult = chatCreator.readJsonStream(streamReader);

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            newResult = null;
        }

        return newResult;
    }

    @Override
    protected void onPostExecute(final ArrayList<CustomArrayList> result) {
        super.onPostExecute(result);

        SharedPreferences settings = activity.getSharedPreferences("AuthorChatSettings", 0);

        if (result != null) {
            //validate PIN code
            int userPin = settings.getInt("code", 0);
            if (pin != userPin) {
                new Utility(activity).warningAlert("Info", "PIN code doesn't match, plz go back and check it again!");
                return;
            }

            if (firstRun) {
                //create adapter and connect it with RecyclerView
                adapter = new RecyclerViewAdapter(result); //adapter needs to be static in AsyncTask, because it overwriting him in each AsyncTask run
                recyclerView.setAdapter(adapter);
                recyclerView.scrollToPosition(result.size() - 1); //scroll to bottom at start
            } else if (oldResult.size() != newResult.size()) { //compare old and new ArrayList<CustomArrayList> results, if now equal than refresh adapter and scroll to new message
                adapter.setItems(result);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(result.size() - 1);
            }

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

        //end progress bar visibility
        if (activity != null) {
            ProgressBar bar = (ProgressBar) activity.findViewById(R.id.progressBar);
            bar.setVisibility(View.INVISIBLE);
            firstRun = false;
        }
    }

}
