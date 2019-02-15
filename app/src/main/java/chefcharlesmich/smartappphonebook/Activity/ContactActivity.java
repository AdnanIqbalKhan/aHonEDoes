package chefcharlesmich.smartappphonebook.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import chefcharlesmich.smartappphonebook.DBHandler;
import chefcharlesmich.smartappphonebook.Models.Contact;
import chefcharlesmich.smartappphonebook.R;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Address;
import ezvcard.property.StructuredName;

public class ContactActivity extends AppCompatActivity {

    private static final String TAG = "CA";
    private TextView contactName,phone_number, email, group, category, business, address, website, description;
    Button backBtn;
    ImageView call,share,map,emailBtn,smsBtn;
    String contact_address;
    DBHandler mdb;
    int cat_id,contact_position;
    public static String fileName = "file.vcf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
//Customize the ActionBar
        final ActionBar abar = getSupportActionBar();
//        abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Contact Information");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
//        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);


        Bundle extras = getIntent().getExtras();

        Contact contact = new Contact();


        if (extras != null) {
            cat_id = extras.getInt(MainActivity.KEY_EXTRA_CAT_ID);
            contact_position = extras.getInt(MainActivity.KEY_EXTRA_CONTACT_POSITION);
            contact = mdb.getContact(cat_id);

//            String cat_name = extras.getString(MainActivity.KEY_EXTRA_CAT_NAME);
//            String contact_name =extras.getString(MainActivity.KEY_EXTRA_CONTACT_NAME);
//            String contact_number =extras.getString(MainActivity.KEY_EXTRA_CONTACT_NUMBER);
//            String contact_email =extras.getString(MainActivity.KEY_EXTRA_CONTACT_EMAIL);
//            contact_address =extras.getString(MainActivity.KEY_EXTRA_CONTACT_ADDRESS);
//            String contact_website =extras.getString(MainActivity.KEY_EXTRA_CONTACT_WEBISTE);

            String contact_name = contact.name;
            String contact_number =contact.phone;
            String contact_email =contact.email;
            contact_address =contact.address;
            String contact_website =contact.website;
            String group_name = contact.group_name;
            String category_name = contact.category_name;
            String description_text  = contact.description;
            String business_text = contact.business;


            Log.d(TAG, "onCreate: cat_id = "+cat_id);
            Log.d(TAG, "onCreate: contact_name = "+ contact_name);
            Log.d(TAG, "onCreate: contact_number = "+ contact_number);
            Log.d(TAG, "onCreate: contact_email = "+ contact_email);
            Log.d(TAG, "onCreate: contact_address = "+ contact_address);
            Log.d(TAG, "onCreate: contact_website = "+ contact_website);
            Log.d(TAG, "onCreate: category_name = "+ category_name);
            Log.d(TAG, "onCreate: group = "+ group_name);
            Log.d(TAG, "onCreate: description = "+ description_text);



            contactName.setText(contact_name);
            phone_number.setText(contact_number);
            email.setText(contact_email);
            group.setText(group_name);
            category.setText(category_name);
            business.setText(business_text);
            address.setText(contact_address);
            website.setText(contact_website);
            description.setText(description_text);
        } else {
            Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
        }

        final MainActivity mainActivity = new MainActivity();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallItemClick();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createVcard();
//                    mainActivity.sendEmail();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMapLocation(contact_address);
            }
        });
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS();
            }
        });
    }

    private void init() {
        mdb = new DBHandler(this);
        setContentView(R.layout.contact_screen);
        contactName = (TextView) findViewById(R.id.contactName_textview_id);
        phone_number = (TextView) findViewById(R.id.phoneNumber_textview_id);
        email = (TextView) findViewById(R.id.email_textview_id);
        group = (TextView) findViewById(R.id.group_textview_id) ;
        category = (TextView) findViewById(R.id.category_textview_id) ;
        business = (TextView) findViewById(R.id.bussiness_textview_id);
        address = (TextView) findViewById(R.id.address_textview_id);
        website = (TextView) findViewById(R.id.website_textview_id);
        description = (TextView) findViewById(R.id.description_textview_id);
        backBtn = (Button) findViewById(R.id.back_btn_contact);
        call = (ImageView) findViewById(R.id.call_imageview_contactscreen);
        share = (ImageView) findViewById(R.id.share_imageview_contactscreen);
        map = (ImageView) findViewById(R.id.map_imageview_contactscreen);
        emailBtn = (ImageView) findViewById(R.id.email_imageview_contact);
        smsBtn = (ImageView) findViewById(R.id.sms_imageview_contact);

    }

    public void getMapLocation(String address)
    {
        String url = "http://maps.google.com/maps?daddr="+address;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
        startActivity(intent);
    }

    public void createVcard() throws IOException {
//        Category tempValues = (Category) CustomListViewValuesArr.get(position);
        fileName = mdb.getContact(cat_id).name+".vcf";
//        fileName = "Test.vcf";

        File file = new File(this.getExternalFilesDir(null), fileName);

        VCard vcard = new VCard();
        vcard.setVersion(VCardVersion.V3_0);


//        vcard.setKind(Kind.individual());
//        vcard.setGender(Gender.male());
        Contact contact;
        contact = mdb.getContact(cat_id);
//        Log.d(TAG, "createVcard: position ="+position);

        if (mdb.getCategoryContact(cat_id).equals(""))
            Toast.makeText(this,"No contact registered!",Toast.LENGTH_SHORT).show();
        else {

//        Log.d(TAG, "createVcard: position =" +position);
//        contact = mdb.getContact(position);
            contact = mdb.getContact(cat_id);

            String contact_name = contact.name;
            String contact_number = contact.phone;
            String contact_email = contact.email;
            String contact_address = contact.address;
            String contact_website = contact.website;
            String group_name = contact.group_name;
            String category_name = contact.category_name;
            String description_text = contact.description;
            Address adr = new Address();
            adr.setStreetAddress(contact_address);
//        adr.setLocality("");
//        adr.setRegion("");
//        adr.setPostalCode("");
//        adr.setCountry("");
//        adr.setLabel("123 Wall St.\nNew York, NY 12345\nUSA");
//        adr.getTypes().add(AddressType.WORK);


            vcard.setFormattedName(contact_name);
            vcard.addTelephoneNumber(contact_number, TelephoneType.WORK, TelephoneType.CELL);
            vcard.addEmail(contact_email);
            vcard.addExtendedProperty("Group", group_name);
            vcard.setCategories(category_name);
            vcard.addUrl(contact_website);
            vcard.addAddress(adr);

            vcard.addNote(description_text);


            StructuredName n = new StructuredName();
//        n.setFamily("");
            n.setGiven(contact_name);
//        n.getPrefixes().add("");
            vcard.setStructuredName(n);


//        vcard.setNickname("John", "Jonny");
//
//        vcard.addTitle("Widget Engineer");
//
//        vcard.setOrganization("Acme Co. Ltd.", "Widget Department");

//        vcard.setGeo(37.6, -95.67);

//        return vcard;


            //validate vCard for version 3.0
            System.out.println("Version 3.0 validation warnings:");
            System.out.println(vcard.validate(VCardVersion.V3_0));

            Log.d(TAG, "main: Version 3.0 validation warnings:");


            //write vCard
//        File file = new File("aSmartAppPhoneBook.vcf");
            System.out.println("Writing " + file.getName() + "...");
            Ezvcard.write(vcard).version(VCardVersion.V3_0).go(file);

            sendEmailVcard();
        }
    }

    public void sendEmailVcard()
    {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {""});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        File file = new File(this.getExternalFilesDir(null), fileName);
        if (!file.exists() || !file.canRead()) {
            return;
        }
        Uri uri = Uri.fromFile(file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Pick To Share!"));

    }
    public void sendEmail()
    {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{  email.getText().toString()});
        startActivity(Intent.createChooser(emailIntent, "Email"));

    }

    private void sendSMS(){

        Uri sms_uri = Uri.parse("smsto:" + phone_number.getText());
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        startActivity(sms_intent);
    }

    private void onCallItemClick() {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        Log.d(TAG, "onCallItemClick: cat_id = "+cat_id);
        intent.setData(Uri.parse("tel:" + mdb.getContact(cat_id).phone));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
