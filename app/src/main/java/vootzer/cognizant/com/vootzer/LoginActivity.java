package vootzer.cognizant.com.vootzer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    DatabaseHelper helper = new DatabaseHelper(this);

    SharedPreferences session;

//    EditText phn = (EditText) findViewById(R.id.ETphone);
//    String phone = phn.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create an InitializerBuilder

        final Context context = this;
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

// Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

// Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(context)
        );

// Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

// Initialize Stetho with the Initializer
        Stetho.initialize(initializer);

        session = getApplicationContext().getSharedPreferences("Session", MODE_PRIVATE);

        if (session.getBoolean("is_logged_in", true)) {
            Intent in = new Intent(this, MainActivity.class);
            startActivity(in);
        } else {
            setContentView(R.layout.activity_login);
        }
    }

    // ********Phone Number Validation code******
    public static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of 10 digit

        if (phoneNo.matches(("\\d{10}"))) {
            return true;
        } else {
            return false;
        }
    }

    //*** Letter Validation
    public static boolean validateLetter(String letter) {
        //validate name

        if (letter.matches("^[\\p{L} .'-]+$")) {
            return true;
        } else {
            return false;
        }
    }

    // On sign click button functionality logic will come here
    public void onSign(View v) {
        if (v.getId() == R.id.sign_in_button) {
            //***SignUp Ke details import name,number company*****
            EditText name = (EditText) findViewById(R.id.ETname);
            EditText number = (EditText) findViewById(R.id.ETphone);
            EditText company = (EditText) findViewById(R.id.ETcompany);

            RadioButton host = (RadioButton) findViewById(R.id.RBhost);

//*** Radio button data fetching
            String usability = "";
            if (host.isChecked()) {
                usability = "host";
            } else usability = "user";
            //** Uppar wale into strings *****

            String name_str = name.getText().toString();
            String number_str = number.getText().toString();
            String company_str = company.getText().toString();
            //***** Pop ups****
            Toast nom = Toast.makeText(LoginActivity.this, "Please enter a valid name", Toast.LENGTH_SHORT);
            Toast pop = Toast.makeText(LoginActivity.this, "Welcome " + name_str, Toast.LENGTH_SHORT);
            Toast po = Toast.makeText(LoginActivity.this, "Please enter correct Phone Number", Toast.LENGTH_SHORT);
            //** Checking whether name is valid or not
            if (validateLetter(name_str)) {
                //** Nested if for phone validation if name validation is true
                if (validatePhoneNumber(number_str)) {
                    //**** Inserting details in database
                    Users user = new Users();
                    user.setName(name_str);
                    user.setNumber(number_str);
                    user.setCompany(company_str);
                    user.setUsability(usability);
                    //**** Insert user details to database
                    helper.insertUser(user);
                    // set is_logged_in preference to truw
                    SharedPreferences.Editor editor = session.edit();
                    editor.putBoolean("is_logged_in", true);
                    editor.apply();
                    // navigate to the main home page
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    // show popup message to welcom user
                    pop.show();
                } else
                    po.show();
            } else
                nom.show();

        }
    }
}