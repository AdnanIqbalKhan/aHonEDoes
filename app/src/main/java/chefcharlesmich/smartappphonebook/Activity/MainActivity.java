package chefcharlesmich.smartappphonebook.Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import chefcharlesmich.smartappphonebook.CustomAdapter;
import chefcharlesmich.smartappphonebook.DBHandler;
import chefcharlesmich.smartappphonebook.Models.Category;
import chefcharlesmich.smartappphonebook.Models.Contact;
import chefcharlesmich.smartappphonebook.Models.Group;
import chefcharlesmich.smartappphonebook.R;
import chefcharlesmich.smartappphonebook.VcardProgram.MainActivityVcard;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Address;
import ezvcard.property.StructuredName;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_EXTRA_CAT_ID = "cat_id";
    public static final String KEY_EXTRA_CAT_NAME = "cat_name";
    private static final String TAG ="MainActivity" ;
    private static final int PERMISSIONS_CODE = 1;
    public static final String KEY_EXTRA_CONTACT_POSITION ="contact_position" ;
    public static final String KEY_EXTRA_CONTACT_NUMBER ="contact_number" ;
    public static final String KEY_EXTRA_CONTACT_EMAIL ="contact_email" ;
    public static final String KEY_EXTRA_CONTACT_ADDRESS = "contact_address";
    public static final String KEY_EXTRA_CONTACT_WEBISTE = "contact_website";
    private ImageView back, next;
    private TextView service_tag,type_textview;
    private Button group_add, category_add, promote_btn_toolbar;

    ListView list;
    CustomAdapter adapter;
    public MainActivity CustomListView = null;
    public ArrayList<Category> CustomListViewValuesArr = new ArrayList<Category>();

    public static int current_group_count = 0;
    String[] service_tags;
    public static String fileName = "file.vcf";
    public static String fileExportName = "AllData.txt";

    DBHandler mdb;

    public static ArrayList<Group> _groups;

    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;     // contacts unique ID

    int position;

    final int RQS_CONTACT_PICK = 1;
    static File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar_asmart);
        setSupportActionBar(toolbar);
        setListeners();
        askForPermission();
//        allSIMContact();
        if (getIntent().getExtras().getBoolean(InfoActivity.KEY_FIRST_RUN)) {
            mdb.clearDB();
            mdb.addGroup("Personal Services");
            mdb.addGroup("Professional Services");
            mdb.addGroup("Home Services");

            _groups = mdb.getAllGroups();


            service_tags = new String[_groups.size()];
            for (int i = 0; i < _groups.size(); i++)
                service_tags[i] = _groups.get(i).name;


            String[] data = getResources().getString(R.string.Personal_Services).split("\n");
            for (String aData : data) mdb.addCategory(_groups.get(0).id, aData);

            data = getResources().getString(R.string.Professional_Services).split("\n");
            for (String aData : data) mdb.addCategory(_groups.get(1).id, aData);

            data = getResources().getString(R.string.Home_Services).split("\n");
            for (String aData : data) mdb.addCategory(_groups.get(2).id, aData);

            Log.d("databasedata", "Size is = " + _groups.size() + "");
            Log.d(TAG, "onCreate: mdb.getGroupTableCount()="+ mdb.getGroupTableCount());
            Log.d(TAG, "onCreate: mdb.getCategoriesTableCount()="+ mdb.getCategoriesTableCount());
            Log.d(TAG, "onCreate: mdb.getCategoriesContactTableCount()="+ mdb.getCategoriesContactTableCount());

            startActivity(new Intent(MainActivity.this,InfoActivity.class));

        } else {
            _groups = mdb.getAllGroups();

            service_tags = new String[_groups.size()];
            for (int i = 0; i < _groups.size(); i++)
                service_tags[i] = _groups.get(i).name;
            Log.d("databasedata", "group size is = " + _groups.size() + "");
            Log.d("databasedata", " _group.tostig result = >  " + _groups.toString());
            Log.d(TAG, "onCreate: mdb.getGroupTableCount()="+ mdb.getGroupTableCount());
            Log.d(TAG, "onCreate: mdb.getCategoriesTableCount()="+ mdb.getCategoriesTableCount());
            Log.d(TAG, "onCreate: mdb.getCategoriesContactTableCount()="+ mdb.getCategoriesContactTableCount());

        }

        ArrayList<Contact> allContactList = mdb.getAllContacts();

        Log.d(TAG, "onCreate: allContactList.size:" +allContactList.size());
        for (int i = 0; i < allContactList.size(); i++) {
            Contact contact = allContactList.get(i);
            Log.d(TAG, "onCreate: DB Contacts:" + " Category_id= " + contact.category_id + " Group_id= " + contact.group_id +
                    " Name =" + contact.name);
        }


        setCustomAdapter();
//        Log.d("thisom", getResources().getString(R.string.Home_Services));

    }

    private void setCustomAdapter(){
        CustomListView = this;
        /******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/
        setListData(_groups.get(current_group_count).id);    // first time current_group_data == 0
        updateServiceTag(service_tags[current_group_count]);

        Resources res = getResources();

        /**************** Create Custom Adapter *********/
//        Category tempValues = (Category) CustomListViewValuesArr.get(position);
        adapter = new CustomAdapter(CustomListView, CustomListViewValuesArr, res,mdb,this );
        list.setAdapter(adapter);
    }
    /******
     * Function to set data in ArrayList
     *************/
    public void setListData(int id) {
        CustomListViewValuesArr.clear();
//        current_group_count = id;
//        ArrayList<Category> mList = mdb.getCategoriesGroups(id);
        CustomListViewValuesArr = mdb.getCategoriesGroups(id);
    }

    /*****************
     * This function used by adapter
     ****************/
    public void onItemClick(int mPosition) {
        Category tempValues = (Category) CustomListViewValuesArr.get(mPosition);
        position =mPosition;

        Log.d(TAG, "onItemClick: called from CustomAdapter");

        // SHOW ALERT
        Toast.makeText(CustomListView, "" + tempValues.name, Toast.LENGTH_SHORT).show();


    }


    private void setListeners() {
        final int[] strings_ids = {R.string.Personal_Services, R.string.Professional_Services, R.string.Home_Services};
//        final String[] service_tags = {"Personal Services", "Professional Services", "Home Services"};
        promote_btn_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MainActivityVcard.class));
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(current_group_count > 0)
                {
                    current_group_count --;
                    setListData(_groups.get(current_group_count).id);
                    updateServiceTag(service_tags[current_group_count]);
                    updateListAdapter();
                    Log.d(TAG, "onClick: current_group_count="+current_group_count);
                }


            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(current_group_count < mdb.getGroupTableCount()-1)
                {
                    current_group_count ++;
                    setListData(_groups.get(current_group_count).id);
                    updateServiceTag(service_tags[current_group_count]);
                    updateListAdapter();
                    Log.d(TAG, "onClick: current_group_count="+current_group_count);

                }

            }
        });
        group_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewGroup();
            }
        });
        category_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewCategory();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Log.v("clicked","pos: " + pos);
                position=pos;
                Category temp_cat = CustomListViewValuesArr.get(pos);
                retreiveContactIDfromDB(temp_cat);
                if(!mdb.getCategoryContact(temp_cat.id).equals("")) {
                    Intent mintent = new Intent(MainActivity.this, ContactActivity.class);
                    mintent.putExtra(KEY_EXTRA_CAT_ID, temp_cat.id);
                    mintent.putExtra(KEY_EXTRA_CONTACT_POSITION, pos);
                    startActivity(mintent);
                }
                else {
                    Toast.makeText(MainActivity.this,"No contact registered!",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void updateListAdapter() {
        adapter.updateData(CustomListViewValuesArr);
    }

    private void init() {
        mdb = new DBHandler(this);

        setContentView(R.layout.services_list);

        back = (ImageView) findViewById(R.id.iv_back);
        next = (ImageView) findViewById(R.id.iv_next);

        service_tag = (TextView) findViewById(R.id.tv_tag_services);
        type_textview = (TextView) findViewById(R.id.tv_type);

        group_add = (Button) findViewById(R.id.btn_add_group);
        category_add = (Button) findViewById(R.id.btn_add_category);

        list = (ListView) findViewById(R.id.lv_services);  // List defined in XML ( See Below )
        promote_btn_toolbar = (Button) findViewById(R.id.promote_btn_id);
        registerForContextMenu(list);
        registerForContextMenu(service_tag);

    }

    private void updateServiceTag(String text) {
        service_tag.setText("" + text);
    }



    public void saveContact(int category_id, int group_id,String contact_id, String name,String phone,String email,String group_name,
                            String category_name, String business,String address,String website,String description) {
//        mdb.addContactToCategory(tempValues.id, contact_id);
        mdb.addContactToCategory(category_id, group_id,contact_id,name,phone,email,group_name,category_name,business,address,website,description);
        Log.d(TAG, "saveContact:\n "+category_id+"\n"+group_id+"\n"+contact_id+"\n"+
                name+"\n"+phone+"\n"+email+"\n"+group_name+"\n"+category_name+"\n"+business+"\n"+address+"\n"+website+"\n"+description);
    }

    public void updateContact(int category_id, int group_id,String contact_id, String name,String phone,String email,String group_name,
                            String category_name, String business,String address,String website,String description) {

        mdb.updateContact(category_id, group_id,contact_id,name,phone,email,group_name,category_name,business,address,website,description);

        Log.d(TAG, "updateContact:\n "+category_id+"\n"+group_id+"\n"+contact_id+"\n"+
                name+"\n"+phone+"\n"+email+"\n"+group_name+"\n"+category_name+"\n"+business+"\n"+address+"\n"+website+"\n"+description);
    }

    public void onPickClick(int mPosition) {
        position = mPosition;
        // user BoD suggests using Intent.ACTION_PICK instead of .ACTION_GET_CONTENT to avoid the chooser
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        // BoD con't: CONTENT_TYPE instead of CONTENT_ITEM_TYPE
//
//        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
//        startActivityForResult(intent, RQS_CONTACT_PICK);

        onClickSelectContact(getCurrentFocus());
    }
    public void onCallItemClick(int position) {
        Category tempValues = (Category) CustomListViewValuesArr.get(position);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if (mdb.getCategoryContact(tempValues.id).equals(""))
            Toast.makeText(MainActivity.this,"No contact registered!",Toast.LENGTH_SHORT).show();
//            intent.setData(Uri.parse("tel:12345676"));
        else {
            intent.setData(Uri.parse("tel:" + mdb.getContact(tempValues.id).phone));
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_menu:
                String url = "http://www.smartappphonebook.com/help/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                break;
            case R.id.share_menu:

                break;
            case R.id.visit_menu:
                break;
            case R.id.gettemplate_menu:
                String url1 = "http://www.smartappphonebook.com/template/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url1));
                startActivity(intent);
                break;
//            case R.id.import_menu:
//                showImportDialog();
//                break;
            case R.id.export_all_menu:
                exportCompleteData();
                break;
            case R.id.import_all_menu:
                showImportAllCautionDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(TAG, "onCreateContextMenu: ");
        if (v.getId() == R.id.lv_services) {
//            ListView lv = (ListView) v;
//            AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;
//            YourObject obj = (YourObject) lv.getItemAtPosition(acmi.position);

            String title = "Select";
            menu.setHeaderTitle(title);

            menu.add(Menu.NONE, 0, Menu.NONE, "Add Category");
            menu.add(Menu.NONE, 1, Menu.NONE, "Delete Category");
            menu.add(Menu.NONE, 2, Menu.NONE, "Duplicate Category");
        }
        else
            if(v.getId() == R.id.tv_tag_services) {
            menu.setHeaderTitle("Select");
            menu.add(Menu.NONE, 3, Menu.NONE, "Add Group");
            menu.add(Menu.NONE, 4, Menu.NONE, "Delete Group");

        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(info!=null)
        position = info.position;

        if (info!=null)
        Log.d(TAG, "onContextItemSelected: info.position="+info.position);
        switch (item.getItemId()) {
            case 0:
                addNewCategory();
                return true;

            case 1:
//                adapter_messages.remove(adapter_messages.getItem(info.position));
//                mdb.removeMessage(adapter_messages.getItem(info.position));
//                setAdapters();
                deleteCategory();

                return true;
            case 2:
                duplicateCategory();
                return true;
            case 3:
                addNewGroup();
                return true;
            case 4:
                showdeleteGroupDialog();
                return true;
        }
        return super.onContextItemSelected(item);
    }


    // Asking for permissions on runtime, Used in Marshmallow devices

    private void askForPermission() {
        String[] permissions = new String[]{Manifest.permission.READ_CONTACTS};
        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.READ_CONTACTS)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {


                    } else {

                        Snackbar.make(getCurrentFocus(),"Application needs to have Contact permission to work properly!",Snackbar.LENGTH_INDEFINITE)
                                .setAction("Action", null).show();


                    }
                }
            }
        }
    }






    /////////////////////////////////////////////------------------------------------------------



    public void onClickSelectContact(View btnSelectContact) {

        // using native contacts selection
        // Intent.ACTION_PICK = Pick an item from the data, returning what was selected.
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: uriContact= " + data.toString());
            uriContact = data.getData();

            retreiveContactIDfromPick();
            String name = retrieveContactName();
            String phone = retrieveContactNumber();
            String email = retreiveEmail();
            String address = retreiveAddress();
            String website = retreiveWebsite();
            String description = retrieveNotes();
            String contact_business = retreiveBusiness();

//            int group_id = current_group_count-1;
            int group_id = _groups.get(current_group_count).id;

            Category category = (Category) CustomListViewValuesArr.get(position);
            int category_id = category.id;
            String category_name = category.name;

            Group group = (Group) _groups.get(current_group_count);
            String groupname = group.name;

            if(mdb.getContactIdForUpdate(category_id) == null)
            saveContact(category_id,group_id,contactID,name,phone,email,groupname,category_name,contact_business,address,website,description);

            else
                updateContact(category_id,group_id,contactID,name,phone,email,groupname,category_name,contact_business,address,website,description);

            setCustomAdapter();

        }
    }

    public void retreiveContactIDfromPick()
    {
        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID from Pick: " + contactID);
    }
    public void retreiveContactIDfromDB(Category cat_value)
    {

        contactID = mdb.getCategoryContact(cat_value.id);
        Log.d(TAG, "retreiveContactIDfromDB: tempValues:"+cat_value.id);
        Log.d(TAG, "Contact ID from DB: " + contactID);
    }
    public String retrieveContactNumber() {

        String contactNumber = null;



        // Using the contact ID now we will get contact phone number
        Log.d(TAG, "retrieveContactNumber: CONTENT_URI"+ContactsContract.CommonDataKinds.Phone.CONTENT_URI);

        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
         new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?" + " AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE ,
                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        if(contactNumber == null){
                cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?" + " AND " +
                            ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK ,
                    new String[]{contactID},
                    null);

            if (cursorPhone.moveToFirst()) {
                contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
        }

        if(contactNumber == null){
            cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?" + " AND " +
                            ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME ,
                    new String[]{contactID},
                    null);

            if (cursorPhone.moveToFirst()) {
                contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);

        return contactNumber;
    }

    public String retrieveContactName() {

        String contactName = null;

        // querying contact data store

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
                , ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?" , new String[]{contactID}, null);

//        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName);

        return contactName;

    }
    public String retreiveEmail(){
        String contactEmail = null;
        ContentResolver resolver = getContentResolver();
//        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup._ID));

        Cursor emailCur = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null
                , ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?" , new String[]{contactID}, null);

        if(emailCur.moveToFirst())
        {
            contactEmail = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

        }

        Log.d(TAG, "Contact Email: " + contactEmail);
        return  contactEmail;
    }

    public String retreiveAddress(){
        String contactAddress = null;
        ContentResolver resolver = getContentResolver();
        Cursor addCursor = resolver.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null
                , ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?" , new String[]{contactID}, null);

        if(addCursor.moveToFirst())
        {
            contactAddress = addCursor.getString(addCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));

        }

        Log.d(TAG, "Contact Address: " + contactAddress);
        return contactAddress;
    }

    public String retreiveWebsite(){
        String contactWebsite = null;


        final String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Website.URL,
                ContactsContract.CommonDataKinds.Website.TYPE
        };
        String selection = ContactsContract.Data.CONTACT_ID + " = " + contactID + " AND " + ContactsContract.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE + "'";

        final Cursor contactData = getContentResolver().query(ContactsContract.Data.CONTENT_URI, projection, selection, null, null);

        if(contactData.moveToFirst())
        {
            contactWebsite = contactData.getString(contactData.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

        }
        Log.d(TAG, "Contact Website: " + contactWebsite);

        return contactWebsite;
    }
    public String retrieveNotes() {

        String contactNotes = null;
        // querying contact data store
        Cursor cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null
                , ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE + "'"
//                        " = ?"
                , new String[]{contactID}, null);

        if (cursor.moveToFirst()) {
            contactNotes = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
        }

        cursor.close();

        Log.d(TAG, "Contact Note: " + contactNotes);

        return contactNotes;

    }
    private String retreiveBusiness()
    {
        // Get Organizations
        String contactBusiness = null;

        String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND "
                + ContactsContract.Data.MIMETYPE + " = ?";
        String[] orgWhereParams = new String[]{contactID,
                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
        Cursor orgCur = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null,
                orgWhere, orgWhereParams, null);
        if (orgCur.moveToFirst())
        {
            contactBusiness = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY)) + ",";
            if(orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE))!=null){
                contactBusiness += orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
            }

        }

        Log.d(TAG, "retreiveBusiness: contactBusiness="+contactBusiness);
        orgCur.close();
        return contactBusiness;
    }

    private void allSIMContact()
    {
        try
        {
            String ClsSimPhonename = null;
            String ClsSimphoneNo = null;

            Uri simUri = Uri.parse("content://icc/adn");
            Cursor cursorSim = this.getContentResolver().query(simUri,null,null,null,null);

            Log.i("PhoneContact", "total: "+cursorSim.getCount());

            while (cursorSim.moveToNext())
            {
                ClsSimPhonename =cursorSim.getString(cursorSim.getColumnIndex("name"));
                ClsSimphoneNo = cursorSim.getString(cursorSim.getColumnIndex("number"));
                ClsSimphoneNo.replaceAll("\\D","");
                ClsSimphoneNo.replaceAll("&", "");
                ClsSimPhonename=ClsSimPhonename.replace("|","");

                Log.i("PhoneContact", "name: "+ClsSimPhonename+" phone: "+ClsSimphoneNo);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }


    public void createVcard(int position) throws IOException {
        Category tempValues = (Category) CustomListViewValuesArr.get(position);
        fileName = mdb.getContact(tempValues.id).name+".vcf";
//        fileName = "Test.vcf";

        File file = new File(this.getExternalFilesDir(null), fileName);

        VCard vcard = new VCard();
        vcard.setVersion(VCardVersion.V3_0);


//        vcard.setKind(Kind.individual());
//        vcard.setGender(Gender.male());
        Contact contact;

        Log.d(TAG, "createVcard: position ="+position);
        Log.d(TAG, "createVcard: tempValues.id ="+tempValues.id);

        if (mdb.getCategoryContact(tempValues.id).equals(""))
            Toast.makeText(this,"No contact registered!",Toast.LENGTH_SHORT).show();
        else {

//        Log.d(TAG, "createVcard: position =" +position);
//        contact = mdb.getContact(position);
            contact = mdb.getContact(tempValues.id);

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

            sendEmail();
        }
    }

    public void sendEmail()
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


    private void showImportDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Import Template");
        alertDialog.setMessage("Enter Template Content Here.");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        input.setSingleLine(true);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String importText = input.getText().toString();
                        if(importText.length() ==  0 || !importText.contains("|")){
                            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            if(importText.length()== 0) {
                                alertDialog.setTitle("No Data Entered!");
                                alertDialog.setMessage("You did not entered any template data to import!");
                            } else{
                                alertDialog.setTitle("Invalid Content!");
                                alertDialog.setMessage("You entered some invalid content, make sure that you copy and paste content from template properly.");
                            }
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();

                                }
                            });
                            alertDialog.show();
                        }else {
                            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("Successfully imported");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // restarting activity to refresh code spinner
                                    importTemplate(importText);
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            });alertDialog.show();

                        }

                    }
                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    public void importTemplate(String text){
        String[] data = text.split("\\|");
        mdb.addGroup(data[0]);          // Adding group in database
        _groups = mdb.getAllGroups();   // adding newly added group to
            for (int i =1; i < data.length; i++) {
                String aData = data[i];
                mdb.addCategory(_groups.get(_groups.size() - 1).id, aData);
            }

        Log.d("importTemplate", "_group size is = " + _groups.size() + "");
        Log.d(TAG, "importTemplate: mdb.getGroupTableCount()="+ mdb.getGroupTableCount());
        Log.d(TAG, "importTemplate: mdb.getCategoriesTableCount()="+ mdb.getCategoriesTableCount());
        Log.d(TAG, "importTemplate: mdb.getCategoriesContactTableCount()="+ mdb.getCategoriesContactTableCount());

        service_tags = new String[_groups.size()];
        for (int i = 0; i < _groups.size(); i++)
            service_tags[i] = _groups.get(i).name;
    }

    void addNewGroup(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Add New Group");
        alertDialog.setMessage("Enter group name");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String importText = input.getText().toString();
                        if(importText.length() ==  0){
                            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle("Invalid Name Entered!");
                                alertDialog.setMessage("Please add a valid name");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }else {
                            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("Successfully Added");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // restarting activity to refresh code spinner
                                    mdb.addGroup(importText);
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            });alertDialog.show();

                        }

                    }
                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    void addNewCategory(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Add New Category");
        alertDialog.setMessage("Enter category name");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String importText = input.getText().toString();
                        if(importText.length() ==  0){
                            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("Invalid Name Entered!");
                            alertDialog.setMessage("Please add a valid name");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }else {
                            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("Successfully Added");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // restarting activity to refresh code spinner
                                    mdb.addCategory(_groups.get(current_group_count).id,importText);
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            });alertDialog.show();

                        }

                    }
                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void showdeleteGroupDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Delete Group!");
        alertDialog.setMessage("If you delete a Group it will delete all the categories and Contacts associated with it. Are You Sure?");
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteGroup();
                    }
                });
        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }
    private void deleteGroup(){
        mdb.deleteGroupWithAllCategoriesAndDetails(service_tag.getText().toString());
        mdb.getAllGroups();
        if(current_group_count > 0){
            current_group_count--;
        }

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void deleteCategory(){

        Category temp_cat = CustomListViewValuesArr.get(position);
//        retreiveContactIDfromDB(temp_cat);
        int category_id = temp_cat.id;

        Log.d(TAG, "deleteCategory:   position ="+position+"   Category deleted ID ="+category_id+"   name="+temp_cat.name);
        mdb.deleteCategoryWithContactDetails(category_id);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    private void duplicateCategory(){
        Category temp_cat = CustomListViewValuesArr.get(position);
        String cat_name = temp_cat.name;
        mdb.addCategory(_groups.get(current_group_count).id,cat_name);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    
    private void exportCompleteData(){
        String string =  "Group| Category| BusinessName| Phone| Name| email| Address| Website| About ] \n";
        ArrayList<Category> categoryList =  mdb.getAllCategories();
        ArrayList<Contact> contactList = mdb.getallContactsToExport();
        ArrayList<Group> groupList = mdb.getAllGroups();



        for(Category category: categoryList)
        {
            String bus="";
            String phone="";
            String name="";
            String email="";
            String addr="";
            String web="";
            String desc="";

            boolean inContactList = false;

            for (Contact contact :contactList) {
                if (category.id == contact.category_id) {

                    if(contact.business != null){bus = contact.business;}
                    if(contact.phone != null){phone = contact.phone;}
                    if(contact.name != null){name = contact.name;}
                    if(contact.email != null){email = contact.email;}
                    if(contact.address != null){addr = contact.address;}
                    if(contact.website != null){web = contact.website;}
                    if(contact.description != null){desc = contact.description;}
//                    string += contact.group_name + "| " + contact.category_name + "| " + contact.business + "| " + contact.phone + "| " +
//                            contact.name + "| " + contact.email + "| " + contact.address + "| " + contact.website + "| " + contact.description + "\n";

                    string += contact.group_name + "| " + contact.category_name + "| " + bus + "| " + phone + "| " +
                            name + "| " + email + "| " + addr + "| " + web + "| " + desc + "]"+"\n";

                    Log.d(TAG, "exportCompleteData: removed contact from list = "+string);
                    contactList.remove(true);
                    Log.d(TAG, "exportCompleteData: category_id found in CONTACT LIST, written on string, removed from contact list.");
                    inContactList = true;
                    break;
                }
            }
                if(!inContactList)
                {
                    String groupName = null;
                    for(Group group : groupList){
                        if(group.id == category.group_id)
                        {
                            groupName = group.name;
                            Log.d(TAG, "exportCompleteData: groupName = "+groupName);
                            string += groupName+"| "+category.name+"| "+"| "+"| "+
                                    "| "+"| "+"| "+"| "+"]\n";
//                            Log.d(TAG, "exportCompleteData: string="+string);
                            break;
                        }
                    }

                }

        }

        try {
            file = new File(MainActivity.this.getExternalFilesDir(null), fileExportName);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(string);
            bw.close();
            Log.d(TAG, "export: string = "+ string);
            sendEmailForExportData();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(MainActivity.this,"File Saved! in "+MainActivity.this.getExternalFilesDir(null),Toast.LENGTH_LONG).show();

    }


    private void importCompleteData(String data){
        String[] lines = data.split("]");
        String[] linedata =null;
        String groupName,categoryName,business,phone,name,email,address,website,description;
        String contactId="1";
        int groupId = 0,categoryId;

        for (int i = 1; i < lines.length; i++){
            String line = lines[i];
            Log.d(TAG, "importCompleteData: Line:"+line);

            if(line.equals("\n"))break;

            linedata = line.split("\\|");

//            Log.d(TAG, "importCompleteData: " + linedata[0] + ";" + linedata[1] + ";" + linedata[2] + ";" + linedata[3] + ";");

            groupName = linedata[0].trim();
            categoryName = linedata[1].trim();
            business = linedata[2].trim();
            phone = linedata[3].trim();
            name = linedata[4].trim();
            email = linedata[5].trim();
            address = linedata[6].trim();
            website = linedata[7].trim();
            description = linedata[8].trim();



            ArrayList<Group> groups = mdb.getAllGroups();
            boolean groupPresent = false;
            for (Group group : groups) {
//                Log.d(TAG, "importCompleteData: group.name:" + group.name + " groupName:" + groupName);
                if (group.name.equals(groupName)) {
                    groupPresent = true;
                    groupId = group.id;

                    mdb.addCategory(groupId, categoryName);
                    break;
                }

            }
            if (!groupPresent) {
//                Log.d(TAG, "importCompleteData: groupPresent=" + groupPresent);
                mdb.addGroup(groupName);
                Log.d(TAG, "importCompleteData: group added="+groupName);
                // Potential error if db not refreshed
                Group group = mdb.getGroup(groupName);
                groupId = group.id;

                mdb.addCategory(groupId, categoryName);
            }

            if(!name.isEmpty()) {           // if Category also has data then we need to add in Categories_contact table too.
                Log.d(TAG, "importCompleteData: name = "+name);
                categoryId = mdb.getCategoryId(categoryName, groupId);
//                Log.d(TAG, "importCompleteData: categoryId: " + categoryId + " categoryName: " + categoryName + " groupName: " + groupName);
                mdb.addContactToCategory(categoryId, groupId, contactId, name, phone, email, groupName, categoryName, business, address, website, description);
            }

        }
    }
    private void showImportAllCautionDialog(){
        final AlertDialog alertDialog= new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Caution!");
        alertDialog.setMessage("It will remove all current data and override with imported data.\nPress OK to continue.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showImportAllDialog();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();

    }
    private void showImportAllDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Import All Data");
        alertDialog.setMessage("Enter Data Content Here.");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        input.setSingleLine(true);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String importText = input.getText().toString();
                        if(importText.length() ==  0 || !importText.contains("Group| Category| BusinessName| Phone| Name| email| Address| Website| About")){
                            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            if(importText.length()== 0) {
                                alertDialog.setTitle("No Data Entered!");
                                alertDialog.setMessage("You did not entered any template data to import!");
                            } else{
                                alertDialog.setTitle("Invalid Content!");
                                alertDialog.setMessage("You entered some invalid content, make sure that you copy and paste content from file properly.");
                            }
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();

                                }
                            });
                            alertDialog.show();
                        }else {
                            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("Successfully imported");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // restarting activity to refresh code spinner
                                    mdb.clearDB();
                                    importCompleteData(importText);
                                    Intent intent = getIntent();

                                    finish();
                                    startActivity(intent);
                                }
                            });alertDialog.show();

                        }

                    }
                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
    public void sendEmailForExportData()
    {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "body text");
        File file = new File(this.getExternalFilesDir(null), fileExportName);
        if (!file.exists() || !file.canRead()) {
            return;
        }
        Uri uri = Uri.fromFile(file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Pick To Share!"));
    }
}
