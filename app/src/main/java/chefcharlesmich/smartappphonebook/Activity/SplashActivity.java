package chefcharlesmich.smartappphonebook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import chefcharlesmich.smartappphonebook.R;
import chefcharlesmich.smartappphonebook.VcardProgram.MainActivityVcard;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivityVcard.class));
                finish();
            }
        }, 2000);
    }
}
