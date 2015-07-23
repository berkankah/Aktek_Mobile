package com.aktekbilisim.aktekmobile;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aktekbilisim.interfaces.onAsyncTaskListener;
import com.aktekbilisim.modals.LoginDTO;
import com.aktekbilisim.services.GetWorker;
import com.blesh.sdk.classes.Blesh;
import com.google.gson.Gson;


public class LoginPage extends Activity  {
    ImageView btnLogin;
    EditText txtUserName;
    EditText txtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        btnLogin = (ImageView) findViewById(R.id.btnLogin);
        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(txtUserName.getText().toString(),txtPassword.getText().toString());
//                Login("kurulum", "aktek01");
                startBlesh("test");

            }
        });
    }
    public void startBlesh(String exampleUserId) {
        Intent blesh = new Intent(LoginPage.this, Blesh.class);
        blesh.putExtra("APIUser", "aktekbilisim");
        blesh.putExtra("APIKey", "KFpXDgEllP");
        blesh.putExtra("integrationType", "M");
        blesh.putExtra("integrationId", "");
        blesh.putExtra("optionalKey", "some_optional_key");
        startService(blesh);
    }

    public void Login(String username, String password) {
        final String[] params = new String[1];
        params[0] = "http://kesim.aksa.com/api/My?userName=" + username + "&userPass=" + password;

        onAsyncTaskListener listener = new onAsyncTaskListener() {
            ProgressDialog dialog;

            @Override
            public void onCancelled() {
                dialog.dismiss();
            }

            @Override
            public void onTaskStarted() {
                dialog = ProgressDialog.show(LoginPage.this, "Yükleniyor", "Lütfen Bekleyiniz Yükleniyor...", true, false);
            }

            @Override
            public void onTaskFinished(String result) {
                dialog.dismiss();
                Log.v("Login Result : ", result);
                try {
                    Gson gson = new Gson();
                    LoginDTO result_obj = gson.fromJson(result,
                            LoginDTO.class);
                    if (result_obj.getRetcode().equals("0")) {
                        Intent i = new Intent(LoginPage.this,MainPage.class);
                        i.putExtra("RetCode", result_obj.getRetcode());
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(LoginPage.this, "Kullanýcý Adý veya Þifre Yanlýþ.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onTaskError() {

                dialog.dismiss();
            }
        };
        GetWorker mWorker = new GetWorker(LoginPage.this, listener);
        mWorker.executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR, params);
    }
}
