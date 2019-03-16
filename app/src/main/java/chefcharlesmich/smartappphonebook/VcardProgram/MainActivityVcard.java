package chefcharlesmich.smartappphonebook.VcardProgram;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        requestPermission();
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

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityVcard.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    void editVCard(int id) {
        startActivity(new Intent(MainActivityVcard.this, AboutYouActivity.class).putExtra("vcardidpassed", id));
        finish();
    }

    void sendVcard(VCardMide mvCard) {
        File vcfFile = new File(this.getExternalFilesDir(null), mvCard.name.replace(" ", "_") + ".vcf");
        if (vcfFile.exists())
            vcfFile.delete();
        FileWriter fw = null;
        try {
            fw = new FileWriter(vcfFile);
            fw.write("BEGIN:VCARD\r\n");
            fw.write("VERSION:3.0\r\n");

            fw.write("N:" + mvCard.name + ";\r\n");
            fw.write("FN:" + mvCard.name + " \r\n");
            fw.write("ORG:" + mvCard.company_name + ";\r\n");
            fw.write("TITLE:" + mvCard.title + "\r\n");

            fw.write("TEL;TYPE=WORK:" + mvCard.phone + "\r\n");
            fw.write("ADR;TYPE=WORK:;;" + mvCard.address + "\r\n");

            fw.write("item3.URL;type=pref:" + mvCard.website + "\r\n");
            fw.write("item4.URL;type=pref:" + mvCard.social1 + "\r\n");
            fw.write("item5.URL;type=pref:" + mvCard.social2 + "\r\n");
            fw.write("item6.URL;type=pref:" + mvCard.weblink1 + "\r\n");
            fw.write("item7.URL;type=pref:" + mvCard.weblink2 + "\r\n");
//            fw.write("item8.URL;type=pref:" + mvCard.pic_link + "\r\n");

            fw.write("PHOTO;ENCODING=B;TYPE=JPEG: ," + convertPhotoToBase64(mvCard.picture) + "\r\n");

            fw.write("EMAIL;type=INTERNET;type=WORK;type=pref:" + mvCard.email + "\r\n");
            fw.write("BDAY:" + mvCard.birthday + "\r\n");
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

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            Intent sendIntent = new Intent(Intent.ACTION_SEND);

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
                card.loadImage();

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


    private String convertPhotoToBase64(Bitmap photo) {
        if (photo != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapData = bos.toByteArray();
            return Base64.encodeToString(bitmapData, Base64.DEFAULT).replaceAll("\n", "");
        }
        return "";
    }
}