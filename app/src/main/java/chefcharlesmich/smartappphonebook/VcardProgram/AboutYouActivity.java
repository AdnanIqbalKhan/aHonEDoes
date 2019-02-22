package chefcharlesmich.smartappphonebook.VcardProgram;

import android.content.ContentUris;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            if (vcardid != -1) {
                VCardMide got = mdb.getVCardById(vcardid);

                got.loadImage();
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
                    picture.setBackground(new BitmapDrawable(getResources(), got.picture));
                } else {
                    Toast.makeText(AboutYouActivity.this, "load Image Error", Toast.LENGTH_SHORT).show();
                    picture.setBackground(getResources().getDrawable(R.drawable.ic_person));
                }
            }
        }

        (findViewById(R.id.btnAddContact)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VCardMide card = new VCardMide(vcardid,
                        companyname.getText().toString(), name.getText().toString(),
                        title.getText().toString(), address.getText().toString(), phone.getText().toString(),
                        email.getText().toString(), description.getText().toString(), birthday.getText().toString(),
                        website.getText().toString(), industry.getText().toString(), social1.getText().toString(),
                        social2.getText().toString(), weblink1.getText().toString(), weblink2.getText().toString(),
                        "File_" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".png",
                        "", ((BitmapDrawable) picture.getBackground()).getBitmap());
                if (!extras_recieved || vcardid == -1) {
                    card.id = 0;
                    mdb.addVcardIfo(card);
                } else {
                    VCardMide got = mdb.getVCardById(vcardid);
                    card.pic_link = got.pic_link;
                    mdb.updateVcard(card);
                }
                card.saveImage();
                Toast.makeText(AboutYouActivity.this, "All Info Updated to the app", Toast.LENGTH_SHORT).show();
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
            VCardMide card = retrieveContact(data.getData());

            if (card != null) {
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

                if (card.picture != null) {
                    picture.setBackground(new BitmapDrawable(getResources(), card.picture));
                } else {
                    picture.setBackground(getResources().getDrawable(R.drawable.ic_person));
                }
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
        Cursor cursorID = getContentResolver().query(uriContact, null,
                null, null, null);

        Log.d(TAG, "retrieveContact: " + uriContact.toString());
        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }
        cursorID.close();
        Log.d(TAG, "Contact ID from Pick: " + contactID);

        VCardMide card = (new ContactData(AboutYouActivity.this, contactID)).getcontact();
        Log.d(TAG, "retrieveContact: from class  \n " + card.toString());
        return card;
    }
}


class ContactData {
    private static final String TAG = "T1";
    private final Context context;
    private final String contactID;

    ContactData(Context context, String contactID) {
        this.context = context;
        this.contactID = contactID;
    }

    public VCardMide getcontact() {
        return new VCardMide(0, getCompanyName(), getName(), getTitle(), getAddress(), getNumber(),
                getEmail(), getNote(), getBirthdate(), getWebsite(), "", "", "", "", ""
                , "", "", getPhoto());
    }

    private Bitmap getPhoto() {
        Bitmap photo = null;
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));
            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
            }
            if (inputStream != null)
                inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }

    private String getNumber() {
        String contactNumber = null;
        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        cursorPhone.close();
        return contactNumber;
    }

    private String getName() {
        String contactName = null;
        // querying contact data store
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactID},
                null);
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        return contactName;
    }

    private String getEmail() {
        String contactEmail = null;
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{contactID},
                null);

        while (cursor.moveToNext()) {
            String a = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            if (a != null) {
                contactEmail = a;
                break;
            }
        }
        cursor.close();
        return contactEmail;
    }

    private String getBirthdate() {
        String birthdate = null;
        Cursor cursor = context.getContentResolver().query(android.provider.ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Event.DATA},
                android.provider.ContactsContract.Data.CONTACT_ID + " = " +
                        contactID + " AND " + ContactsContract.Contacts.Data.MIMETYPE + " = '" +
                        ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' AND " +
                        ContactsContract.CommonDataKinds.Event.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY, null,

                android.provider.ContactsContract.Data.DISPLAY_NAME);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String a = cursor.getString(0);
                if (a != null) {
                    birthdate = a;
                    break;
                }
            }
        }
        cursor.close();
        return birthdate;
    }

    private String getAddress() {
        String address = null;
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.StructuredPostal.STREET,
                        ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                        ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY
                },
                ContactsContract.Data.CONTACT_ID + " = " + contactID,
                null,
                null);

        while (cursor.moveToNext()) {
            String Strt = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
            String Cty = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
            String cntry = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));

            if (Strt != null || Cty != null || cntry != null) {
                address = Strt + " " + Cty + " " + cntry;
            }
        }

        cursor.close();
        return address;
    }

    private String getCompanyName() {
        String orgWhere = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] orgWhereParams = new String[]{getRawContactId(contactID),
                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                null, orgWhere, orgWhereParams, null);

        if (cursor == null) return null;
        String name = null;
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
        }
        cursor.close();
        return name;
    }

    private String getTitle() {
        String orgWhere = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] orgWhereParams = new String[]{getRawContactId(contactID),
                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                null, orgWhere, orgWhereParams, null);

        if (cursor == null) return null;
        String name = null;
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
        }
        cursor.close();
        return name;
    }

    private String getRawContactId(String contactId) {
        String[] projection = new String[]{ContactsContract.RawContacts._ID};
        String selection = ContactsContract.RawContacts.CONTACT_ID + "=?";
        String[] selectionArgs = new String[]{contactId};
        Cursor c = context.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection, selection, selectionArgs, null);
        if (c == null) return null;
        int rawContactId = -1;
        if (c.moveToFirst()) {
            rawContactId = c.getInt(c.getColumnIndex(ContactsContract.RawContacts._ID));
        }
        c.close();
        return String.valueOf(rawContactId);
    }

    private String getWebsite() {
        String website = null;

        String orgWhere = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] orgWhereParams = new String[]{getRawContactId(contactID),
                ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE};
        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                null, orgWhere, orgWhereParams, null);

        while (cursor.moveToNext()) {
            String a = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL));
            if (a != null) {
                website = a;
            }
        }
        cursor.close();
        return website;
    }

    private String getNote() {
        String note = null;

        String orgWhere = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] orgWhereParams = new String[]{getRawContactId(contactID),
                ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                null, orgWhere, orgWhereParams, null);

        while (cursor.moveToNext()) {
            String a = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
            if (a != null) {
                note = a;
            }
        }
        cursor.close();
        return note;
    }
}
