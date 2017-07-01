package pl.ordin.authorchatforwordpress;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static android.webkit.URLUtil.isValidUrl;

/**
 * {@link StartActivity} get user values to connect REST API from Wordpress website.
 */
public class StartActivity extends AppCompatActivity {

    Utility utility = new Utility(this);
    SharedPreferences settings;
    EditText code;
    EditText domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        settings = getSharedPreferences("AuthorChatSettings", 0);
        code = (EditText) findViewById(R.id.editCode);
        domain = (EditText) findViewById(R.id.editUrl);

        int userPinToPut = settings.getInt("code", 0);
        String domainToPut = settings.getString("domain", "none");

        if (userPinToPut != 0 && !domainToPut.equals("none")) {
            domain.setText(domainToPut);
            code.setText(String.valueOf(userPinToPut));
        }
    }

    //ignite when confirm button is pressed
    public void submitCodeDomain(View view) {
        if (isNetworkAvailable()) {

            SharedPreferences.Editor e = settings.edit();

            String domainName = domain.getText().toString();
            //validate url
            if (isValidUrl(domainName)) {
                e.putString("domain", domain.getText().toString());
                e.putInt("code", Integer.parseInt(code.getText().toString()));
                e.apply();

                //start MainActivity.class
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                //show alert if url is invalid
                utility.warningAlert("Info", "Domain name invalid! Plz check it has \"http://\" or \"https://\" fragment...");
            }
        } else {
            //show alert if internet is unavailable
            utility.warningAlert("Info", "Internet not available, Cross check your internet connectivity and try again!");
        }
    }

    //detect whether there is an Internet connection available, return true if available
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
