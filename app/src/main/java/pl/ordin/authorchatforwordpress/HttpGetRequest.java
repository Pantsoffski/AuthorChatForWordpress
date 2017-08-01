package pl.ordin.authorchatforwordpress;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class {@link HttpGetRequest} is responsible for url leech.
 */
class HttpGetRequest extends AsyncTask<URL, Void, ArrayList<CustomArrayList>> {
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;
    private static final String REQUEST_METHOD = "POST";
    static boolean firstRun = true;
    private static RecyclerViewAdapter adapter;
    private static ArrayList<CustomArrayList> newResult, oldResult;
    private String message = "2358";
    private String l = "";
    private String p = "";
    private Activity activity;
    private RecyclerView recyclerView;

    HttpGetRequest(Activity activity, RecyclerView recyclerView, String login, String pass) {
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.l = login;
        this.p = pass;
    }

    HttpGetRequest(String msg, String login, String pass) {
        this.message = msg;
        this.l = login;
        this.p = pass;
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

        if (!message.equals("2358")) { // TODO: 31.07.2017 clean up this mess in code (slim)
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
                connection.setDoInput(true);
                connection.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("msg", message)
                        .appendQueryParameter("l", l)
                        .appendQueryParameter("p", p);
                String query = builder.build().getEncodedQuery();

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                outputStream.close();

                //Connect to our url
                connection.connect();
                int responseCode = connection.getResponseCode();
                Log.v("Response: ", Integer.toString(responseCode));
                //connection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
                newResult = null;
            }
        } else if (message.equals("2358")) {

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
                connection.setDoInput(true);
                connection.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("msg", message)
                        .appendQueryParameter("l", l)
                        .appendQueryParameter("p", p);
                String query = builder.build().getEncodedQuery();

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                outputStream.close();

                //Connect to our url
                connection.connect();
                int responseCode = connection.getResponseCode();
                Log.v("Response: ", Integer.toString(responseCode));

                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());

                ChatCreator chatCreator = new ChatCreator();

                newResult = chatCreator.readJsonStream(streamReader);

                //connection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
                newResult = null;
            }
        }

        return newResult;
    }

    @Override
    protected void onPostExecute(final ArrayList<CustomArrayList> result) {
        super.onPostExecute(result);

        if (message.equals("2358")) {
            if (result != null) {
                if (firstRun) {
                    //create adapter and connect it with RecyclerView
                    adapter = new RecyclerViewAdapter(result); //adapter needs to be static in AsyncTask, because it overwriting him in each AsyncTask run
                    recyclerView.setAdapter(adapter);
                    recyclerView.scrollToPosition(result.size() - 1); //scroll to bottom at start
                } else if (oldResult != null) {
                    if (oldResult.size() != newResult.size()) {//compare old and new ArrayList<CustomArrayList> results, if now equal than refresh adapter and scroll to new message
                        adapter.setItems(result);
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(result.size() - 1);
                    }
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
                new Utility(activity).warningAlert("Info", "Oops! Something went wrong... Plz go back and check domain name or internet connection (or maybe your site is down)!");
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

}
