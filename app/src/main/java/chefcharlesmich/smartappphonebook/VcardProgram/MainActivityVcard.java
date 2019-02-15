package chefcharlesmich.smartappphonebook.VcardProgram;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import chefcharlesmich.smartappphonebook.Activity.InfoActivity;
import chefcharlesmich.smartappphonebook.R;

public class MainActivityVcard extends AppCompatActivity {

    String[] permissions = new String[]{
            Manifest.permission.SEND_SMS};

    Button add1, add2, add3, add4, add5, add6,contactsBtnToolbar;
    DBHandlerVcard mdb;

    ArrayList<VCardMide> mList;
    int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.vcard_page_vcard);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.actionbar_titletext_layout);
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setTitleMarginStart(200);
//        View customlayout= getLayoutInflater().inflate(R.layout.titlbar_vcard_layout, null);
//        toolbar.addView(customlayout);

        Log.d("MAVC", "onCreate: MainActivityVcard");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            } else {
//                startMainActivity();
            }
        } else {
//            startMainActivity();
        }

        add1 = (Button) findViewById(R.id.button3);
        add2 = (Button) findViewById(R.id.button);
        add3 = (Button) findViewById(R.id.button5);
        add4 = (Button) findViewById(R.id.button7);
        add5 = (Button) findViewById(R.id.button9);
        add6 = (Button) findViewById(R.id.btn_add_share_5);
        contactsBtnToolbar = (Button) findViewById(R.id.contacts_btn_id);
        mdb = new DBHandlerVcard(this);

        mList = mdb.getAllVcards();
        size = mList.size();

        contactsBtnToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityVcard.this, InfoActivity.class));
                finish();
            }
        });
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size == 0)
                    startActivity(new Intent(MainActivityVcard.this, AboutYouActivity.class));
                else
                    startActivity(new Intent(MainActivityVcard.this, AboutYouActivity.class).putExtra("vcardidpassed", mList.get(0).id));
            }
        });
        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size < 2)
                    startActivity(new Intent(MainActivityVcard.this, AboutYouActivity.class));
                else
                    startActivity(new Intent(MainActivityVcard.this, AboutYouActivity.class).putExtra("vcardidpassed", mList.get(1).id));
            }
        });
        add3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size < 3)
                    startActivity(new Intent(MainActivityVcard.this, AboutYouActivity.class));
                else
                    startActivity(new Intent(MainActivityVcard.this, AboutYouActivity.class).putExtra("vcardidpassed", mList.get(2).id));
            }
        });
        add4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size < 4)
                    startActivity(new Intent(MainActivityVcard.this, AboutYouActivity.class));
                else
                    startActivity(new Intent(MainActivityVcard.this, AboutYouActivity.class).putExtra("vcardidpassed", mList.get(3).id));
            }
        });
        add5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size < 5)
                    startActivity(new Intent(MainActivityVcard.this, AboutYouActivity.class));
                else
                    startActivity(new Intent(MainActivityVcard.this, AboutYouActivity.class).putExtra("vcardidpassed", mList.get(4).id));
            }
        });
        add6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size < 6)
                    startActivity(new Intent(MainActivityVcard.this, AboutYouActivity.class));
                else
                    startActivity(new Intent(MainActivityVcard.this, AboutYouActivity.class).putExtra("vcardidpassed", mList.get(5).id));
            }
        });
        ((Button) findViewById(R.id.button4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size >= 1)
                    sendVcard(mList.get(0));
//                Toast.makeText(MainActivityVcard.this, "Share options will be displayed", Toast.LENGTH_SHORT).show();
            }
        });
        ((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size >= 2)
                    sendVcard(mList.get(1));
//                Toast.makeText(MainActivityVcard.this, "Share options will be displayed", Toast.LENGTH_SHORT).show();
            }
        });
        ((Button) findViewById(R.id.button6)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size >= 3)
                    sendVcard(mList.get(2));
//                Toast.makeText(MainActivityVcard.this, "Share options will be displayed", Toast.LENGTH_SHORT).show();
            }
        });
        ((Button) findViewById(R.id.button8)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size >= 4)
                    sendVcard(mList.get(3));
            }
        });
        ((Button) findViewById(R.id.button10)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size >= 5)
                    sendVcard(mList.get(4));
            }
        });
        ((Button) findViewById(R.id.btn_share_5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size >= 6)
                    sendVcard(mList.get(5));
            }
        });

        ((Button) findViewById(R.id.btn_help)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.smssenderapp.com/smsbusinessteamapphelp/";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        ((Button) findViewById(R.id.btn_share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Get the app at www.smssenderapp.com/SMSBusinessTeam/";

                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("sms_body", message);
                startActivity(smsIntent);


//                SmsManager smsManager = SmsManager.getDefault();
//                smsManager.sendTextMessage("0000", null, message, null, null);
            }
        });
    }

    void sendVcard(VCardMide mvCard) {
        File vcfFile = new File(this.getExternalFilesDir(null), mvCard.company_name.replace(" ", "_") + ".vcf");
        if (vcfFile.exists())
            vcfFile.delete();
        FileWriter fw = null;
        try {
            fw = new FileWriter(vcfFile);
            fw.write("BEGIN:VCARD\r\n");
            fw.write("VERSION:3.0\r\n");

            String name = mvCard.name;// = ((EditText) v.findViewById(R.id.editText5)).getText().toString();
            String phone = mvCard.phone;// = ((EditText) v.findViewById(R.id.editText6)).getText().toString();
            String email = mvCard.email;// = ((EditText) v.findViewById(R.id.editText7)).getText().toString();
            String address = mvCard.address;// = ((EditText) v.findViewById(R.id.editText8)).getText().toString();
            String bday = mvCard.birthday;// = ((EditText) v.findViewById(R.id.editText9)).getText().toString();
            String org = mvCard.company_name;// = ((EditText) v.findViewById(R.id.editText10)).getText().toString();
            String title = mvCard.title;// = ((EditText) v.findViewById(R.id.editText10)).getText().toString();
            String website = mvCard.website;
            String note = mvCard.info;
            String social1 = mvCard.social1;
            String social2 = mvCard.social2;
            String blog = mvCard.blog;
            String pic_link = mvCard.pic_link;

//            fw.write("N:" + name + ";" + name + "\r\n");
//            fw.write("FN:" + name + " " + name + "\r\n");

            /*fw.write("N:" + name + ";\r\n");
            fw.write("FN:" + name + " \r\n");
            fw.write("ORG:" + org + "\r\n");
//                        fw.write("TITLE:" + p.getTitle() + "\r\n");
            fw.write("TITLE:" + title + "\r\n");
            fw.write("TEL;TYPE=WORK,VOICE:" + phone + "\r\n");
            fw.write("ADR;TYPE=WORK:;;" + address + "\r\n");
            fw.write("NOTE:" + note + "\r\n");
            fw.write("URL;type=pref:" + website + "\r\n");
            fw.write("URL:" + social1 + "\r\n");
            fw.write("URL:" + social2 + "\r\n");
            fw.write("URL:" + blog + "\r\n");
            fw.write("URL:" + pic_link + "\r\n");
            fw.write("VCF_PATH=" + vcfFile.getAbsolutePath() + "\r\n ");
            fw.write("EMAIL;" + email + "\r\n");
            fw.write("BDAY:" + bday + "\r\n");*/

            fw.write("N:" + name + ";\r\n");
            fw.write("FN:" + name + " \r\n");
            fw.write("ORG:" + org + "\r\n");
            fw.write("TITLE:" + title + "\r\n");
            fw.write("TEL;TYPE=WORK,VOICE:" + phone + "\r\n");
            fw.write("ADR;TYPE=WORK:;;" + address + "\r\n");
            fw.write("NOTE:" + note + "\r\n");
            fw.write("item1.URL:" + website + "\r\n");
            fw.write("item2.URL:" + social1 + "\r\n");
            fw.write("item3.URL:" + social2 + "\r\n");
            fw.write("item4.URL:" + blog + "\r\n");
            fw.write("item5.URL:" + pic_link + "\r\n");
//            fw.write("PHOTO;VALUE=URI;TYPE=JPG:http://www.chefcharlesmichael.com/chefpic" + "\r\n");

            fw.write("VCF_PATH=" + mvCard.vcf_path + "\r\n ");
            fw.write("EMAIL;TYPE=PREF,INTERNET:" + email + "\r\n");
            fw.write("BDAY:" + bday + "\r\n");

            /*fw.write("URL;TYPE=PREF:" + website + "\r\n ");

            fw.write("URL;type=pref:" + mvCard.social1 + "\r\n ");
//            fw.write("URL;type=pref:=SOCIAL1=" + mvCard.social1 + "\r\n ");
            fw.write("URL;type=pref:" + mvCard.social2 + "\r\n ");
//            fw.write("URL;type=pref:=SOCIAL2=" + mvCard.social2 + "\r\n ");
            fw.write("URL;type=pref:" + mvCard.blog + "\r\n ");//=BLOG=
            fw.write("item5.URL:" + mvCard.pic_link + "\r\n");
//            fw.write("PHOTO;TYPE=JPEG;VALUE=URI:" + mvCard.pic_link + "\r\n");
//            fw.write("PHOTO;VALUE=URI:=PICTURE_LINK=" + mvCard.pic_link + "\r\n ");
//            fw.write("VCF_PATH=" + mvCard.vcf_path + "\r\n ");
            fw.write("VCF_PATH=" + vcfFile.getAbsolutePath() + "\r\n ");
//                        fw.write("ADR;TYPE=WORK:;;" + p.getStreet() + ";" + p.getCity() + ";" + p.getState() + ";" + p.getPostcode() + ";" + p.getCountry() + "\r\n");
            fw.write("EMAIL;TYPE=PREF,INTERNET:" + email + "\r\n");
            fw.write("BDAY:" + bday + "\r\n");*/

            fw.write("END:VCARD\r\n");
            fw.close();

            /*


ID = "id";
ORG:=COMPANY_NAME = "company_name";
FN:=NAME = "name";
TITLE:=TITLE = "title";
ADR:=ADDRESS = "address";
PHOTO;VALUE=URI:https://www.google.com/maps/dir//3407+Drexel+Dr,+Dallas,+TX+75205/;TYPE=JPG:http://www.smartappphonebbok.com/dir_photos/navigation.jpg
CELL:=PHONE = "phone";
EMAIL:=EMAIL = "email";
NOTE:=INFO = "info";
BDAY:=BIRTHDAY = "bday";
URL;type=pref:=WEBSITE = "website";
item1.URL;type=pref:=SOCIAL1 = "social1";
item2.URL;type=pref:=SOCIAL2 = "social2";
item3.URL;type=pref:=BLOG = "blog";
PHOTO;VALUE=URI:=PICTURE_LINK = "piclink";
VCF_PATH = "vcf_path";

            */

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
//            sendIntent.putExtra("sms_body", "some text");
//            sendIntent.putExtra("address", "2146907715");
            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(vcfFile));
            sendIntent.setType("text/x-vcard");
            startActivity(sendIntent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pick_date_vcard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, AboutActivityVcard.class));
                Toast.makeText(this, "Thanks for using our app. :)", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkIfAlreadyhavePermission() {
        //int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        for (int i = 0; i < permissions.length; i++)
            if (ContextCompat.checkSelfPermission(this, permissions[i]) == PackageManager.PERMISSION_GRANTED) {
                continue;
                //return true;
            } else {
                return false;
            }
        return true;
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, permissions, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
//                    startMainActivity();
                } else {
                    //not granted
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void updateUI() {
        mList = mdb.getAllVcards();
        size = mList.size();

        if (size == 1) {
            ((TextView) findViewById(R.id.textView3)).setText("Company: " + mList.get(0).company_name);
            ((TextView) findViewById(R.id.textView4)).setText("Name: " + mList.get(0).name);
            ((TextView) findViewById(R.id.tv_phone0)).setText("Phone: " + mList.get(0).phone);

        } else if (size == 2) {
            ((TextView) findViewById(R.id.textView3)).setText("Company: " + mList.get(0).company_name);
            ((TextView) findViewById(R.id.textView4)).setText("Name: " + mList.get(0).name);
            ((TextView) findViewById(R.id.tv_phone0)).setText("Phone: " + mList.get(0).phone);

            ((TextView) findViewById(R.id.textView5)).setText("Company: " + mList.get(1).company_name);
            ((TextView) findViewById(R.id.textView7)).setText("Name: " + mList.get(1).name);
            ((TextView) findViewById(R.id.tv_phone1)).setText("Phone: " + mList.get(1).phone);
        } else if (size == 3) {
            ((TextView) findViewById(R.id.textView3)).setText("Company: " + mList.get(0).company_name);
            ((TextView) findViewById(R.id.textView4)).setText("Name: " + mList.get(0).name);
            ((TextView) findViewById(R.id.tv_phone0)).setText("Phone: " + mList.get(0).phone);

            ((TextView) findViewById(R.id.textView5)).setText("Company: " + mList.get(1).company_name);
            ((TextView) findViewById(R.id.textView7)).setText("Name: " + mList.get(1).name);
            ((TextView) findViewById(R.id.tv_phone1)).setText("Phone: " + mList.get(1).phone);

            ((TextView) findViewById(R.id.textView8)).setText("Company: " + mList.get(2).company_name);
            ((TextView) findViewById(R.id.textView9)).setText("Name: " + mList.get(2).name);
            ((TextView) findViewById(R.id.tv_phone2)).setText("Phone: " + mList.get(2).phone);
        } else if (size == 4) {
            ((TextView) findViewById(R.id.textView3)).setText("Company: " + mList.get(0).company_name);
            ((TextView) findViewById(R.id.textView4)).setText("Name: " + mList.get(0).name);
            ((TextView) findViewById(R.id.tv_phone0)).setText("Phone: " + mList.get(0).phone);

            ((TextView) findViewById(R.id.textView5)).setText("Company: " + mList.get(1).company_name);
            ((TextView) findViewById(R.id.textView7)).setText("Name: " + mList.get(1).name);
            ((TextView) findViewById(R.id.tv_phone1)).setText("Phone: " + mList.get(1).phone);

            ((TextView) findViewById(R.id.textView8)).setText("Company: " + mList.get(2).company_name);
            ((TextView) findViewById(R.id.textView9)).setText("Name: " + mList.get(2).name);
            ((TextView) findViewById(R.id.tv_phone2)).setText("Phone: " + mList.get(2).phone);

            ((TextView) findViewById(R.id.textView10)).setText("Company: " + mList.get(3).company_name);
            ((TextView) findViewById(R.id.textView11)).setText("Name: " + mList.get(3).name);
            ((TextView) findViewById(R.id.tv_phone3)).setText("Phone: " + mList.get(3).phone);
        } else if (size == 5) {
            ((TextView) findViewById(R.id.textView3)).setText("Company: " + mList.get(0).company_name);
            ((TextView) findViewById(R.id.textView4)).setText("Name: " + mList.get(0).name);
            ((TextView) findViewById(R.id.tv_phone0)).setText("Phone: " + mList.get(0).phone);

            ((TextView) findViewById(R.id.textView5)).setText("Company: " + mList.get(1).company_name);
            ((TextView) findViewById(R.id.textView7)).setText("Name: " + mList.get(1).name);
            ((TextView) findViewById(R.id.tv_phone1)).setText("Phone: " + mList.get(1).phone);

            ((TextView) findViewById(R.id.textView8)).setText("Company: " + mList.get(2).company_name);
            ((TextView) findViewById(R.id.textView9)).setText("Name: " + mList.get(2).name);
            ((TextView) findViewById(R.id.tv_phone2)).setText("Phone: " + mList.get(2).phone);

            ((TextView) findViewById(R.id.textView10)).setText("Company: " + mList.get(3).company_name);
            ((TextView) findViewById(R.id.textView11)).setText("Name: " + mList.get(3).name);
            ((TextView) findViewById(R.id.tv_phone3)).setText("Phone: " + mList.get(3).phone);

            ((TextView) findViewById(R.id.textView12)).setText("Company: " + mList.get(4).company_name);
            ((TextView) findViewById(R.id.textView13)).setText("Name: " + mList.get(4).name);
            ((TextView) findViewById(R.id.tv_phone4)).setText("Phone: " + mList.get(4).phone);
        } else if (size == 6) {
            ((TextView) findViewById(R.id.textView3)).setText("Company: " + mList.get(0).company_name);
            ((TextView) findViewById(R.id.textView4)).setText("Name: " + mList.get(0).name);
            ((TextView) findViewById(R.id.tv_phone0)).setText("Phone: " + mList.get(0).phone);

            ((TextView) findViewById(R.id.textView5)).setText("Company: " + mList.get(1).company_name);
            ((TextView) findViewById(R.id.textView7)).setText("Name: " + mList.get(1).name);
            ((TextView) findViewById(R.id.tv_phone1)).setText("Phone: " + mList.get(1).phone);

            ((TextView) findViewById(R.id.textView8)).setText("Company: " + mList.get(2).company_name);
            ((TextView) findViewById(R.id.textView9)).setText("Name: " + mList.get(2).name);
            ((TextView) findViewById(R.id.tv_phone2)).setText("Phone: " + mList.get(2).phone);

            ((TextView) findViewById(R.id.textView10)).setText("Company: " + mList.get(3).company_name);
            ((TextView) findViewById(R.id.textView11)).setText("Name: " + mList.get(3).name);
            ((TextView) findViewById(R.id.tv_phone3)).setText("Phone: " + mList.get(3).phone);

            ((TextView) findViewById(R.id.textView12)).setText("Company: " + mList.get(4).company_name);
            ((TextView) findViewById(R.id.textView13)).setText("Name: " + mList.get(4).name);
            ((TextView) findViewById(R.id.tv_phone4)).setText("Phone: " + mList.get(4).phone);

            ((TextView) findViewById(R.id.textView14)).setText("Company: " + mList.get(5).company_name);
            ((TextView) findViewById(R.id.textView15)).setText("Name: " + mList.get(5).name);
            ((TextView) findViewById(R.id.tv_phone5)).setText("Phone: " + mList.get(5).phone);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
//        startActivity(new Intent(this, MainActivityVcard.class));
    }
}
