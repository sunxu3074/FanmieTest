package fanmie.com.fanmie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.http.callback.GsonCallback;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    private EditText etAccount;
    private EditText etPassword;
    private TextView tvBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initListener() {
        tvBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.FanFou.USERNAME = etAccount.getText().toString().trim();
                Constants.FanFou.PASSWORD = etPassword.getText().toString().trim();
                getAccessToken();


            }
        });
    }

    private void initView() {
        etAccount = (EditText) findViewById(R.id.etAccount);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvBind = (TextView) findViewById(R.id.tvBind);
    }

    public void getAccessToken(){
        HttpUtil.getToken(API.getAccessToken("token")).execute(new GsonCallback<String>() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(MainActivity.this, "出现错误", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(int ret, int code, String name) {
                Toast.makeText(MainActivity.this, "出现错误2", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();

            }
        });
    }
}
