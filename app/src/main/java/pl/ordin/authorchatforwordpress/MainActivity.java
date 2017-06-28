package pl.ordin.authorchatforwordpress;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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

        ArrayList<CustomArrayList> result;
        //Perform the doInBackground method, passing in our url
        try {
            result = getRequest.execute("https://ordin.pl/wp-json/author-chat/v2/chat/").get();
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }

        if (result != null) {
            adapterRefreshed = new CustomAdapter(getApplicationContext(), result);
            listView.setAdapter(adapterRefreshed);
            listView.setSelection(adapterRefreshed.getCount() - 1); //scroll down at start
        }

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //hide actionbar when scroll down
                if (visibleItemCount > firstVisibleItem) {
/*                    if (getSupportActionBar().isShowing()) { // TODO: 28.06.2017 try to handle NPE here
                        getSupportActionBar().hide();
                    }*/

                    if (downButton.getVisibility() == View.VISIBLE) { // TODO: 28.06.2017 fix show and hie down button
                        downButton.setVisibility(View.INVISIBLE);
                    }
                }

                //hide/show floating button
                if (visibleItemCount < firstVisibleItem) {
/*                    if (!getSupportActionBar().isShowing()) {
                        getSupportActionBar().show();
                    }*/

                    if (downButton.getVisibility() == View.INVISIBLE) {
                        downButton.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        //scroll to top when floating button is pressed
        downButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listView.setSelection(adapterRefreshed.getCount() - 1);
            }
        });
    }
}