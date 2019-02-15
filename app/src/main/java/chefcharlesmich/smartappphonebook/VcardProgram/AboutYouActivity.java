package chefcharlesmich.smartappphonebook.VcardProgram;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import chefcharlesmich.smartappphonebook.R;

public class AboutYouActivity extends AppCompatActivity {

    EditText companyname, name, title, address, phone, email, info, birthday, website, social1, social2, blog, pictureurl;
    DBHandlerVcard mdb;
    int vcardid;
    boolean extras_recieved = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pick__about_you_vcard);

        Log.d("AYA", "onCreate: AboutYouActivity");
        mdb = new DBHandlerVcard(this);

        companyname = (EditText) findViewById(R.id.editText16);
        name = (EditText) findViewById(R.id.editText5);
        title = (EditText) findViewById(R.id.editText17);
        address = (EditText) findViewById(R.id.editText8);
        phone = (EditText) findViewById(R.id.editText6);
        email = (EditText) findViewById(R.id.editText7);
        info = (EditText) findViewById(R.id.editText15);
        birthday = (EditText) findViewById(R.id.editText9);
        website = (EditText) findViewById(R.id.editText);
        social1 = (EditText) findViewById(R.id.editText4);
        social2 = (EditText) findViewById(R.id.editText13);
        blog = (EditText) findViewById(R.id.editText10);
        pictureurl = (EditText) findViewById(R.id.editText14);

        if (getIntent().getExtras() != null) {
            extras_recieved = true;
            vcardid = getIntent().getExtras().getInt("vcardidpassed");
            VCardMide got = mdb.getVCardById(vcardid);
            companyname.setText("" + got.company_name);
            name.setText("" + got.name);
            title.setText("" + got.title);
            address.setText("" + got.address);
            phone.setText("" + got.phone);
            email.setText("" + got.email);
            info.setText("" + got.info);
            birthday.setText("" + got.birthday);
            website.setText("" + got.website);
            social1.setText("" + got.social1);
            social2.setText("" + got.social2);
            blog.setText("" + got.blog);
            pictureurl.setText("" + got.pic_link);
        }

        ((Button) findViewById(R.id.button17)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!extras_recieved) {
                    mdb.addVcardIfo(new VCardMide(0, companyname.getText().toString(), name.getText().toString(),
                            title.getText().toString(), address.getText().toString(), phone.getText().toString(),
                            email.getText().toString(), info.getText().toString(), birthday.getText().toString(),
                            website.getText().toString(), social1.getText().toString(), social2.getText().toString(),
                            blog.getText().toString(), pictureurl.getText().toString(), ""));
                    Toast.makeText(AboutYouActivity.this, "All Info is saved to the app", Toast.LENGTH_SHORT).show();
                } else {
                    mdb.updateVcard(new VCardMide(vcardid, companyname.getText().toString(), name.getText().toString(),
                            title.getText().toString(), address.getText().toString(), phone.getText().toString(),
                            email.getText().toString(), info.getText().toString(), birthday.getText().toString(),
                            website.getText().toString(), social1.getText().toString(), social2.getText().toString(),
                            blog.getText().toString(), pictureurl.getText().toString(), ""));
                    Toast.makeText(AboutYouActivity.this, "All Info Updated to the app", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
}
