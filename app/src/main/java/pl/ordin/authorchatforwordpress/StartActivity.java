package pl.ordin.authorchatforwordpress;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import static android.webkit.URLUtil.isValidUrl;

/**
 * {@link StartActivity} get user values to connect REST API from Wordpress website.
 */
public class StartActivity extends AppCompatActivity {

    static boolean notificationsOnOff;
    SharedPreferences settings;
    EditText login;
    EditText password;
    EditText domain;
    Utility utility;
    CheckBox notificationCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        utility = new Utility(this);
        settings = getSharedPreferences("AuthorChatSettings", 0);
        login = (EditText) findViewById(R.id.editLogin);
        password = (EditText) findViewById(R.id.editPassword);
        domain = (EditText) findViewById(R.id.editUrl);
        notificationCheckBox = (CheckBox) findViewById(R.id.notificationsCheckBox);

        String domainToPut = settings.getString("domain", "none");
        String userLoginToPut = settings.getString("login", "none");
        String userPasswordToPut = settings.getString("password", "none");
        notificationsOnOff = settings.getBoolean("notificationsOnOff", false);

        if (!userLoginToPut.equals("none") && !userPasswordToPut.equals("none") && !domainToPut.equals("none")) {
            domain.setText(domainToPut);
            login.setText(userLoginToPut);
            password.setText(userPasswordToPut);
            if (notificationsOnOff) {
                notificationCheckBox.setChecked(true);
            }
        }
    }

    //ignite when confirm button is pressed
    public void submitSignIn(View view) {
        if (isNetworkAvailable()) {

            SharedPreferences.Editor e = settings.edit();

            String domainName = domain.getText().toString();
            //validate url
            if (isValidUrl(domainName)) {
                e.putString("domain", domain.getText().toString());
                e.putString("login", login.getText().toString());
                e.putString("password", password.getText().toString());
                if (notificationCheckBox.isChecked()) {
                    e.putBoolean("notificationsOnOff", true);
                } else {
                    e.putBoolean("notificationsOnOff", false);
                }
                e.apply();

                notificationsOnOff = settings.getBoolean("notificationsOnOff", false);

                //start MainActivity.class
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                //show alert if url is invalid
                utility.warningAlert("Info", "Domain name invalid! Plz check it has \"http://\" or \"https://\" fragment...");
            }
        } else {
            //show alert if internet is unavailable
            utility.warningAlert("Info", "Internet not available, cross check your internet connectivity and try again!");
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
