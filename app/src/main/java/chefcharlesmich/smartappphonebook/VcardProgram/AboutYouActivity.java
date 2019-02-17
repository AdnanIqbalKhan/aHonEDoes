package chefcharlesmich.smartappphonebook.VcardProgram;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import chefcharlesmich.smartappphonebook.R;

public class AboutYouActivity extends AppCompatActivity {

    private static final String TAG = "T1";
    EditText companyname, name, title, address, phone, email, birthday, website, industry, social1, social2, description, weblink1, weblink2, pictureurl;
    private ImageButton picture;
    DBHandlerVcard mdb;
    int vcardid;
    boolean extras_recieved = false;


    int REQUEST_CODE_PICK_CONTACTS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pick__about_you_vcard);
        getSupportActionBar().hide();

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
        industry = findViewById(R.id.editTextIndustry);
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


        (findViewById(R.id.button_pick_contact)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_PICK_CONTACTS);

            }
        });

        if (getIntent().getExtras() != null) {
            extras_recieved = true;
            vcardid = getIntent().getExtras().getInt("vcardidpassed");
            Toast.makeText(AboutYouActivity.this, Integer.toString(vcardid), Toast.LENGTH_LONG).show();
            if (vcardid != -1) {
                VCardMide got = mdb.getVCardById(vcardid);
                companyname.setText(got.company_name);
                name.setText(got.name);
                title.setText(got.title);
                address.setText(got.address);
                phone.setText(got.phone);
                email.setText(got.email);
                birthday.setText(got.birthday);
                website.setText(got.website);
                industry.setText(got.industry);
                social1.setText(got.social1);
                social2.setText(got.social2);
                weblink1.setText(got.weblink1);
                weblink2.setText(got.weblink2);
                if (got.picture != null) {
                    Toast.makeText(AboutYouActivity.this, "load Image from DB", Toast.LENGTH_SHORT).show();
                    picture.setBackground(new BitmapDrawable(getResources(), got.picture));
                }
                else {
                    Toast.makeText(AboutYouActivity.this,"load Image Error",Toast.LENGTH_SHORT).show();
                    picture.setBackground(getResources().getDrawable(R.drawable.ic_person));
                }
            }
        }

        (findViewById(R.id.btnAddContact)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!extras_recieved || vcardid == -1) {
                    mdb.addVcardIfo(new VCardMide(0,
                            companyname.getText().toString(), name.getText().toString(),
                            title.getText().toString(), address.getText().toString(), phone.getText().toString(),
                            email.getText().toString(), description.getText().toString(), birthday.getText().toString(),
                            website.getText().toString(), industry.getText().toString(), social1.getText().toString(),
                            social2.getText().toString(), weblink1.getText().toString(), weblink2.getText().toString(),
                            pictureurl.getText().toString(), "", ((BitmapDrawable) picture.getBackground()).getBitmap()));
                    Toast.makeText(AboutYouActivity.this, "All Info is saved to the app1", Toast.LENGTH_SHORT).show();
                } else {
                    mdb.updateVcard(new VCardMide(vcardid,
                            companyname.getText().toString(), name.getText().toString(),
                            title.getText().toString(), address.getText().toString(), phone.getText().toString(),
                            email.getText().toString(), description.getText().toString(), birthday.getText().toString(),
                            website.getText().toString(), industry.getText().toString(), social1.getText().toString(),
                            social2.getText().toString(), weblink1.getText().toString(), weblink2.getText().toString(),
                            pictureurl.getText().toString(), "", ((BitmapDrawable) picture.getBackground()).getBitmap()));
                    Toast.makeText(AboutYouActivity.this, "All Info Updated to the app", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(AboutYouActivity.this, MainActivityVcard.class));
                finish();
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1).start(AboutYouActivity.this);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Toast.makeText(AboutYouActivity.this, "Inside IF ", Toast.LENGTH_SHORT).show();
            VCardMide card = retrieveContact(data.getData());

            if (card != null) {
                Toast.makeText(AboutYouActivity.this, "Card is not null", Toast.LENGTH_SHORT).show();
                Toast.makeText(AboutYouActivity.this, card.toString(), Toast.LENGTH_SHORT).show();
                name.setText(card.name);
                phone.setText(card.phone);
                email.setText(card.email);
                address.setText(card.address);
                website.setText(card.website);
                description.setText(card.description);

                birthday.setText(card.birthday);
                companyname.setText(card.company_name);
                title.setText(card.title);
                social1.setText(card.social1);
                social2.setText(card.social2);
                weblink1.setText(card.weblink1);
                weblink2.setText(card.weblink2);
            } else {
                Toast.makeText(AboutYouActivity.this, "Card is null", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resultUri = result.getUri();

            InputStream is;
            Drawable icon;
            try {
                is = this.getContentResolver().openInputStream(resultUri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 10;
                Bitmap preview_bitmap = BitmapFactory.decodeStream(is, null, options);

                icon = new BitmapDrawable(getResources(), preview_bitmap);

            } catch (FileNotFoundException e) {
                icon = getResources().getDrawable(R.drawable.ic_person);
            }

            picture.setBackground(icon);

        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Toast.makeText(AboutYouActivity.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public VCardMide retrieveContact(Uri uriContact) {
        String contactID = null;
        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact, null,
                null, null, null);

        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
            String abc = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            Log.d(TAG, "Contact Name from Pick: " + abc);
        }
        cursorID.close();
        Log.d(TAG, "Contact ID from Pick: " + contactID);

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactID},
                null);


        VCardMide card = null;
        if (cursor.moveToFirst()) {
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactEmail = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            String contactAddress = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
            String contactWebsite = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL));
            String companyName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
            String title = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
            String department = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DEPARTMENT));

            card = new VCardMide(0, companyName, contactName, title, contactAddress, phoneNumber,
                    contactEmail, "", "", contactWebsite, "", "", "", "",
                    "", department,
                    "", null);


            Log.d(TAG, "Contact Pick: " + card.toString());
        }
        cursor.close();
        return card;
    }


}
