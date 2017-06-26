package pl.ordin.authorchatforwordpress;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list);

        //Instantiate new instance of our class
        HttpGetRequest getRequest = new HttpGetRequest();

        ArrayList<CustomArrayList> result = new ArrayList<CustomArrayList>();
        //Perform the doInBackground method, passing in our url
        try {
            result = getRequest.execute("https://ordin.pl/wp-json/author-chat/v2/chat/").get();
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }

        if (result != null) {
            CustomAdapter adapterRefreshed = new CustomAdapter(getApplicationContext(), result);
            listView.setAdapter(adapterRefreshed);
        }
    }
}