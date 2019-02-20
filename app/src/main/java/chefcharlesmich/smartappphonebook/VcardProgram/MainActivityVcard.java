package chefcharlesmich.smartappphonebook.VcardProgram;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import chefcharlesmich.smartappphonebook.Activity.InfoActivity;
import chefcharlesmich.smartappphonebook.R;

public class MainActivityVcard extends AppCompatActivity {

    int MAX_CONTACT_LIMIT = 6;   // 0 means unlimited
    String[] permissions = new String[]{
            Manifest.permission.SEND_SMS};

    Button contactsBtnToolbar;
    ImageButton addContact;
    DBHandlerVcard mdb;
    RecyclerView contactList;
    ArrayList<VCardMide> mList;
    int size = 0;
    private LinearLayoutManager layoutManager;
    private MyAdapter mAdapter;

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

        contactList = findViewById(R.id.recycler_view_contact_list);
        contactsBtnToolbar = (Button) findViewById(R.id.contacts_btn_id);
        addContact = findViewById(R.id.button_add_contact);
        mdb = new DBHandlerVcard(this);

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mList.size() < MAX_CONTACT_LIMIT || MAX_CONTACT_LIMIT == 0) {
                    mList.add(new VCardMide(-1, "", "", "",
                            "", "", "", "", "", "", "",
                            "", "", "", "", "", "", null));
                    updateUI(true);
                } else {
                    Toast.makeText(MainActivityVcard.this, "Max Contacts Allowed " + MAX_CONTACT_LIMIT, Toast.LENGTH_SHORT).show();
                }
            }
        });

        contactsBtnToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityVcard.this, InfoActivity.class));
                finish();
            }
        });


        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        contactList.setLayoutManager(layoutManager);

        updateUI(false);

//        ((Button) findViewById(R.id.btn_help)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = "http://www.smssenderapp.com/smsbusinessteamapphelp/";
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//            }
//        });
//        ((Button) findViewById(R.id.btn_share)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String message = "Get the app at www.smssenderapp.com/SMSBusinessTeam/";
//
//                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//                smsIntent.setType("vnd.android-dir/mms-sms");
//                smsIntent.putExtra("sms_body", message);
//                startActivity(smsIntent);
//
//
////                SmsManager smsManager = SmsManager.getDefault();
////                smsManager.sendTextMessage("0000", null, message, null, null);
//            }
//        });
    }

    void editVCard(int id) {
        startActivity(new Intent(MainActivityVcard.this, AboutYouActivity.class).putExtra("vcardidpassed", id));
        finish();
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

//            String name = mvCard.name;// = ((EditText) v.findViewById(R.id.editText5)).getText().toString();
//            String phone = mvCard.phone;// = ((EditText) v.findViewById(R.id.editText6)).getText().toString();
//            String email = mvCard.email;// = ((EditText) v.findViewById(R.id.editText7)).getText().toString();
//            String address = mvCard.address;// = ((EditText) v.findViewById(R.id.editText8)).getText().toString();
//            String bday = mvCard.birthday;// = ((EditText) v.findViewById(R.id.editText9)).getText().toString();
//            String org = mvCard.company_name;// = ((EditText) v.findViewById(R.id.editText10)).getText().toString();
//            String title = mvCard.title;// = ((EditText) v.findViewById(R.id.editText10)).getText().toString();
//            String website = mvCard.website;
//            String note = mvCard.info;
//            String social1 = mvCard.social1;
//            String social2 = mvCard.social2;
//            String blog = mvCard.blog;
//            String pic_link = mvCard.pic_link;

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

//            fw.write("N:" + name + ";\r\n");
//            fw.write("FN:" + name + " \r\n");
//            fw.write("ORG:" + org + "\r\n");
//            fw.write("TITLE:" + title + "\r\n");
//            fw.write("TEL;TYPE=WORK,VOICE:" + phone + "\r\n");
//            fw.write("ADR;TYPE=WORK:;;" + address + "\r\n");
//            fw.write("NOTE:" + note + "\r\n");
//            fw.write("item1.URL:" + website + "\r\n");
//            fw.write("item2.URL:" + social1 + "\r\n");
//            fw.write("item3.URL:" + social2 + "\r\n");
//            fw.write("item4.URL:" + blog + "\r\n");
//            fw.write("item5.URL:" + pic_link + "\r\n");
////            fw.write("PHOTO;VALUE=URI;TYPE=JPG:http://www.chefcharlesmichael.com/chefpic" + "\r\n");
//
//            fw.write("VCF_PATH=" + mvCard.vcf_path + "\r\n ");
//            fw.write("EMAIL;TYPE=PREF,INTERNET:" + email + "\r\n");
//            fw.write("BDAY:" + bday + "\r\n");

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

    void updateUI(boolean add) {
        if (!add) {
            mList = mdb.getAllVcards();
        }
        size = mList.size();
        mAdapter = new MyAdapter(mList);
        contactList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI(true);
//        startActivity(new Intent(this, MainActivityVcard.class));
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private ArrayList<VCardMide> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public View View;

            public MyViewHolder(View v) {
                super(v);
                View = v;
            }

            public void bind(final VCardMide card) {
                TextView companyName = View.findViewById(R.id.textViewCompany);
                TextView personName = View.findViewById(R.id.textViewName);
                TextView phoneNumber = View.findViewById(R.id.textViewPhone);

                Button edit = View.findViewById(R.id.buttonListEdit);
                Button share = View.findViewById(R.id.buttonListShare);

                companyName.setText(card.company_name);
                personName.setText(card.name);
                phoneNumber.setText(card.phone);

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View view) {
                        editVCard(card.id);
                    }
                });

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View view) {
                        sendVcard(card);
                        Toast.makeText(View.getContext(), "Share options will be displayed", Toast.LENGTH_SHORT).show();
                    }
                });

                View.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(android.view.View view) {
                        new AlertDialog.Builder(View.getContext())
                                .setTitle("Delete Contact")
                                .setMessage("Do you really want to delete contact with name: " + card.name + "?")
                                .setIcon(R.drawable.ic_warning_black_24dp)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        if (mdb.deleteVCardByID(card.id)) {
                                            File sd = Environment.getExternalStorageDirectory();
                                            File file = new File(sd, card.pic_link);
                                            if (file.delete()) {
                                                Toast.makeText(View.getContext(), "Contact deleted", Toast.LENGTH_SHORT).show();
                                            } else
                                                Toast.makeText(View.getContext(), "Error Occur", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(View.getContext(), "Error Occur", Toast.LENGTH_SHORT).show();
                                        }
                                        updateUI(false);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                        return false;
                    }
                });
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(ArrayList<VCardMide> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vcard_list_single_layout, parent, false);

            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.bind(mDataset.get(position));

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}