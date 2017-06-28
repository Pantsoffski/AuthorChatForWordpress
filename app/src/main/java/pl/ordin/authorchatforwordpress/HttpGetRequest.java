package pl.ordin.authorchatforwordpress;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class {@link HttpGetRequest} is responsible for url leech.
 */
class HttpGetRequest extends AsyncTask<String, Void, ArrayList<CustomArrayList>> {
    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;

    @Override
    protected ArrayList<CustomArrayList> doInBackground(String... params) {
        String stringUrl = params[0];
        ArrayList<CustomArrayList> result;

        try {
            //Create a URL object holding our url
            URL myUrl = new URL(stringUrl);

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

            result = chatCreator.readJsonStream(streamReader);

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            result = null;
        }

        return result;
    }

}
