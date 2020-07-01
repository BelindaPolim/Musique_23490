package id.ac.umn.moosic_23490;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton();
    }

    public void LoginButton() {
        username = (EditText) findViewById(R.id.editText_user);
        password = (EditText) findViewById(R.id.editText_password);
        login_btn = (Button) findViewById(R.id.button_login);

        login_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (username.getText().toString().equals("musique") &&
                                password.getText().toString().equals("musique123")) {
                            Toast.makeText(Login.this, "Welcome~",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent("id.ac.umn.moosic_23490.MainActivity");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Wrong Username / Password!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
    }
}
