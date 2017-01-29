package com.gfycat.album.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.gfycat.album.R;
import com.gfycat.album.constants.GfyConstants;
import com.gfycat.album.interfaces.RetrofitInterface;
import com.gfycat.album.models.GrantResponsePojo;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Michael Yoon Huh on 1/28/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private Unbinder unbinder;

    @BindView(R.id.login_button) Button loginButton;
    @BindView(R.id.login_field) EditText loginField;
    @BindView(R.id.password_field) EditText passwordField;
    @BindView(R.id.activity_login_layout) RelativeLayout loginActivityLayout;

    @Inject Retrofit retrofitAdapter;

    @OnClick(R.id.login_button)
    public void loginButton() {

        Log.d(LOG_TAG, "loginButton()");

        String username = loginField.getText().toString();
        String password = passwordField.getText().toString();

        loginUser(username, password);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void initView() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton();
            }
        });
    }

    private void loginUser(@NonNull String username, @NonNull String password) {

        Log.d(LOG_TAG, "loginUser(): Username: " + username);

        RetrofitInterface loginRequest = retrofitAdapter.create(RetrofitInterface.class);
        Call<GrantResponsePojo> call = loginRequest.loginUser(GfyConstants.GFY_GRANT_TYPE_PASSWORD,
                getString(R.string.gfycat_client_id), getString(R.string.gfycat_client_secret),
                username, password);
        call.enqueue(new Callback<GrantResponsePojo>() {

            @Override
            public void onResponse(Call<GrantResponsePojo> call, Response<GrantResponsePojo> response) {

                Log.d(LOG_TAG, "onResponse(): Response success: " + response.isSuccessful());
                Log.d(LOG_TAG, "onResponse(): Response message: " + response.message());

                if (response.isSuccessful()) {
                    displayLoginResponseSnackbar("SUCCESS!");
                    // TODO: Launch intent to MainActivity.
                } else {
                    displayLoginResponseSnackbar(getString(R.string.login_error_message));
                }
            }

            @Override
            public void onFailure(Call<GrantResponsePojo> call, Throwable t) {
                displayLoginResponseSnackbar(getString(R.string.login_error_message));
            }
        });
    }

    private void displayLoginResponseSnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(loginActivityLayout, message, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginButton();
                    }
                });
        snackbar.show();
    }
}
