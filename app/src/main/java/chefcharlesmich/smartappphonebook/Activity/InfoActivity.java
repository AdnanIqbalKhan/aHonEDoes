package chefcharlesmich.smartappphonebook.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import chefcharlesmich.smartappphonebook.R;

public class InfoActivity extends AppCompatActivity {

    private Button continueb, browse;
    private EditText name, imagepath;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String KEY_FIRST_RUN = "firstrun";
    public final static String KEY_USER_NAME = "username";
    public static final String KEY_USER_IMAGE_PATH = "imagepath";

    SharedPreferences sharedpreferences;

    final int RQS_IMAGE1 = 1;

    File file;
    Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.getBoolean(KEY_FIRST_RUN, true)) {
            init();
            setListners();

            startActivity((new Intent(InfoActivity.this, MainActivity.class)).putExtra(KEY_FIRST_RUN, true));
            sharedpreferences.edit().putBoolean(KEY_FIRST_RUN, false).apply();
            finish();


        } else {
            startActivity((new Intent(InfoActivity.this, MainActivity.class)).putExtra(KEY_FIRST_RUN, false));
            finish();
        }
    }

    private void init() {
        setContentView(R.layout.activity_main);

        continueb = (Button) findViewById(R.id.btn_continue);
        browse = (Button) findViewById(R.id.btn_photo_browse);

        name = (EditText) findViewById(R.id.et_name);
        imagepath = (EditText) findViewById(R.id.et_photo_path);
    }

    private void setListners() {
        continueb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sname, image;
                sname = name.getText().toString();
                image = imagepath.getText().toString();
                if (!sname.isEmpty() && !image.isEmpty()) {
                    startActivity((new Intent(InfoActivity.this, MainActivity.class)).putExtra(KEY_FIRST_RUN, true));
                    sharedpreferences.edit().putBoolean(KEY_FIRST_RUN, false).apply();
                    sharedpreferences.edit().putString(KEY_USER_NAME, sname).apply();
                    sharedpreferences.edit().putString(KEY_USER_IMAGE_PATH, image).apply();
                    finish();
                } else {
                    Toast.makeText(InfoActivity.this, "Information Required!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                file = new File(getExternalCacheDir(), String.valueOf(System.currentTimeMillis()) + ".jpg");
                fileUri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                startActivityForResult(intent, RQS_IMAGE1);
                Toast.makeText(InfoActivity.this, "Not Working yet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RQS_IMAGE1:
                    imagepath.setText(file.getAbsolutePath());
                    break;
            }
        }
    }

    /*public void saveImage(Bitmap capturedBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/req_images");
        myDir.mkdirs();
        String fname = "Image-" + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            capturedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            saveed = true;
            Toast.makeText(this, "Image Saved Sucessfully.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
