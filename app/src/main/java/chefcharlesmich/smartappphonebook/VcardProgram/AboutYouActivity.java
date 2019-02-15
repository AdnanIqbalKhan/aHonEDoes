package chefcharlesmich.smartappphonebook.VcardProgram;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import chefcharlesmich.smartappphonebook.R;

public class AboutYouActivity extends AppCompatActivity {

    EditText companyname, name, title, address, phone, email, birthday, website, social1, social2, description, weblink1, weblink2, pictureurl;
    ImageButton picture;
    DBHandlerVcard mdb;
    int vcardid;
    boolean extras_recieved = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pick__about_you_vcard);

        Log.d("AYA", "onCreate: AboutYouActivity");
        mdb = new DBHandlerVcard(this);

        companyname = findViewById(R.id.editTextCompanyName);
        name = findViewById(R.id.editTextName);
        title = findViewById(R.id.editTextTitle);
        address = findViewById(R.id.editTextAddress);
        phone = findViewById(R.id.editTextPhone);
        email = findViewById(R.id.editTextEmail);
        birthday = findViewById(R.id.editTextDate);
        website = findViewById(R.id.editTextWebsite);
        social1 = findViewById(R.id.editTextSocialMedia1);
        social2 = findViewById(R.id.editTextSocialMedia2);
        weblink1 = findViewById(R.id.editTextWebLink1);
        weblink2 = findViewById(R.id.editTextWebLink2);
        description = findViewById(R.id.editTextDescription);

        picture = findViewById(R.id.imageBtnPerson);
        pictureurl = description; // TODO

//        Bitmap bitmap = ((BitmapDrawable)picture.getDrawable()).getBitmap();
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//        byte[] byteArray = outputStream.toByteArray();


        if (getIntent().getExtras() != null) {
            extras_recieved = true;
            vcardid = getIntent().getExtras().getInt("vcardidpassed");
            if (vcardid != -1) {
                VCardMide got = mdb.getVCardById(vcardid);
                companyname.setText("" + got.company_name);
                name.setText("" + got.name);
                title.setText("" + got.title);
                address.setText("" + got.address);
                phone.setText("" + got.phone);
                email.setText("" + got.email);
                birthday.setText("" + got.birthday);
                website.setText("" + got.website);
                social1.setText("" + got.social1);
                social2.setText("" + got.social2);
            }
        }

        ((Button) findViewById(R.id.btnAddContact)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!extras_recieved || vcardid == -1) {
                    mdb.addVcardIfo(new VCardMide(0,
                            companyname.getText().toString(), name.getText().toString(),
                            title.getText().toString(), address.getText().toString(), phone.getText().toString(),
                            email.getText().toString(), description.getText().toString(), birthday.getText().toString(),
                            website.getText().toString(), social1.getText().toString(), social2.getText().toString(),
                            weblink1.getText().toString(), weblink2.getText().toString(),
                            pictureurl.getText().toString(), ""));
                    Toast.makeText(AboutYouActivity.this, "All Info is saved to the app", Toast.LENGTH_SHORT).show();
                } else {
                    mdb.updateVcard(new VCardMide(vcardid,
                            companyname.getText().toString(), name.getText().toString(),
                            title.getText().toString(), address.getText().toString(), phone.getText().toString(),
                            email.getText().toString(), description.getText().toString(), birthday.getText().toString(),
                            website.getText().toString(), social1.getText().toString(), social2.getText().toString(),
                            weblink1.getText().toString(), weblink2.getText().toString(),
                            pictureurl.getText().toString(), ""));
                    Toast.makeText(AboutYouActivity.this, "All Info Updated to the app", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(AboutYouActivity.this, MainActivityVcard.class));

                finish();
            }
        });
    }
}
