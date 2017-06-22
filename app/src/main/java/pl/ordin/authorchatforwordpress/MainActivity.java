package pl.ordin.authorchatforwordpress;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView justText = (TextView) findViewById(R.id.justText);

        //Instantiate new instance of our class
        HttpGetRequest getRequest = new HttpGetRequest();

        String result;
        //Perform the doInBackground method, passing in our url
        try {
            result = getRequest.execute("https://ordin.pl/wp-json/author-chat/v2/chat/").get();
            if (result == null) {
                result = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "";
        }

        justText.setText(result);
    }
}
