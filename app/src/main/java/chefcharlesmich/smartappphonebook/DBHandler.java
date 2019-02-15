package chefcharlesmich.smartappphonebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import chefcharlesmich.smartappphonebook.Models.Category;
import chefcharlesmich.smartappphonebook.Models.Contact;
import chefcharlesmich.smartappphonebook.Models.Group;

public class DBHandler extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "smartphonebook";
    private static final int DATABASE_VERSION = 1;

    // Contacts table name
    private static final String TABLE_GROUPS = "groups";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_CATEGORIES_CONTACTS = "category_contacts";

    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_GROUP_ID = "group_id";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_CATEGORY_CONTACT = "contact_Nnumber";
    private static final String KEY_CONTACT_ID = "contact_id";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_GROUP_NAME = "group_name";
    private static final String KEY_CATEGORY_NAME = "category_name";
    private static final String KEY_BUSINESS = "business";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_WEBSITE = "website";
    private static final String KEY_DESCRIPTION = "description";
    private static final String TAG = "DBhandler";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_GROUPS_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_GROUPS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_NAME
                + " TEXT);";
        db.execSQL(CREATE_GROUPS_TABLE);

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_CATEGORIES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_NAME
                + " TEXT," + KEY_GROUP_ID + " INTEGER NOT NULL,"
                + " FOREIGN KEY (" + KEY_GROUP_ID + ") REFERENCES " + TABLE_GROUPS + "(" + KEY_ID + "));";
        db.execSQL(CREATE_CATEGORIES_TABLE);

        String CREATE_CATEGORIES_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES_CONTACTS
                + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
//                + KEY_CATEGORY_CONTACT + " TEXT NOT NULL,"
                + KEY_CATEGORY_ID + " TEXT NOT NULL,"
                + KEY_GROUP_ID + " TEXT NOT NULL,"

                + KEY_CONTACT_ID + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_GROUP_NAME + " TEXT,"
                + KEY_CATEGORY_NAME + " TEXT,"
                + KEY_BUSINESS + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_WEBSITE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"


                + " FOREIGN KEY (" + KEY_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + KEY_ID + "));";
        db.execSQL(CREATE_CATEGORIES_CONTACTS_TABLE);
    }

    public void clearDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "clearDB: ");
//        Log.d("databasedata", " Delete Query result = " + db.delete(TABLE_GROUPS, null, null));
//        Log.d("databasedata", " Delete Query result = " + db.delete(TABLE_CATEGORIES, null, null));
//        Log.d("databasedata", " Delete Query result = " + db.delete(TABLE_CATEGORIES_CONTACTS, null, null));

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES_CONTACTS);

        onCreate(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(TAG, "onUpgrade: "+"database version upgraded.");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES_CONTACTS);

        // Creating tables again
        onCreate(db);
    }

    // Adding new GROUP
    public void addGroup(String groupname) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, groupname);

        // Inserting Row
        db.insert(TABLE_GROUPS, null, values);
        db.close(); // Closing database connection
    }

    public ArrayList<Group> getAllGroups() {
        String selectQuery = "SELECT * FROM " + TABLE_GROUPS;
//                 + " ORDER BY " + KEY_NAME + " DESC";
//                + " WHERE " + KEY_NUMBER + "=" + icomingNumber + ";";
        ArrayList<Group> mList = new ArrayList<Group>();

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                mList.add(new Group(cursor.getInt(0), cursor.getString(1)));
                Log.d(TAG, "getAllGroups: id="+cursor.getInt(0)+" name="+cursor.getString(1));
            }
            db.close();
            return mList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Adding new Category
    public void addCategory(int groupId, String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, categoryName);
        values.put(KEY_GROUP_ID, groupId);

        // Inserting Row
        db.insert(TABLE_CATEGORIES, null, values);
        db.close(); // Closing database connection
    }


    public ArrayList<Category> getAllCategories() {
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES;
//                 + " ORDER BY " + KEY_NAME + " DESC";
//                + " WHERE " + KEY_NUMBER + "=" + icomingNumber + ";";
        ArrayList<Category> mList = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                mList.add(new Category(cursor.getInt(0), cursor.getInt(2), cursor.getString(1)));
                Log.d(TAG, "getAllCategories: cat_id="+cursor.getInt(0)+" group_id="+cursor.getInt(2)+" category_ name="+cursor.getString(1));
            }
            db.close();
            return mList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Category> getCategoriesGroups(int groupId) {
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES +
                " WHERE " + KEY_GROUP_ID + "=" + groupId;
//                + " ORDER BY " + KEY_NAME + " DESC;";
        ArrayList<Category> mList = new ArrayList<Category>();

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                mList.add(new Category(cursor.getInt(0), cursor.getInt(2), cursor.getString(1)));
            }
            db.close();
            return mList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Adding new contact to category
//    public void addContactToCategory(String contactNumber, int category_id, int contact_id) {
    public void addContactToCategory(int category_id, int group_id,String contact_id,String name,String phone,String email,String group_name,
                                     String category_name, String business,String address,String website,String description)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_ID, category_id);
        values.put(KEY_GROUP_ID,group_id);

        values.put(KEY_CONTACT_ID,contact_id);
        values.put(KEY_NAME,name);
        values.put(KEY_PHONE,phone);
        values.put(KEY_EMAIL,email);
        values.put(KEY_GROUP_NAME,group_name);
        values.put(KEY_CATEGORY_NAME,category_name);
        values.put(KEY_BUSINESS,business);
        values.put(KEY_ADDRESS,address);
        values.put(KEY_WEBSITE,website);
        values.put(KEY_DESCRIPTION, description);

        // Inserting Row
//        if(db.rawQuery("SELECT contact_id FROM "+TABLE_CATEGORIES_CONTACTS+" WHERE category_id = "+ category_id, null) != null)
//            db.update(TABLE_CATEGORIES_CONTACTS, values,"category_id= " + category_id,null);
//        else
        db.insert(TABLE_CATEGORIES_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    public void updateContact(int category_id, int group_id,String contact_id,String name,String phone,String email,String group_name,
                              String category_name, String business,String address,String website,String description){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_ID, category_id);
        values.put(KEY_GROUP_ID,group_id);

        values.put(KEY_CONTACT_ID,contact_id);
        values.put(KEY_NAME,name);
        values.put(KEY_PHONE,phone);
        values.put(KEY_EMAIL,email);
        values.put(KEY_GROUP_NAME,group_name);
        values.put(KEY_CATEGORY_NAME,category_name);
        values.put(KEY_BUSINESS,business);
        values.put(KEY_ADDRESS,address);
        values.put(KEY_WEBSITE,website);
        values.put(KEY_DESCRIPTION, description);

        // Inserting Row

        db.update(TABLE_CATEGORIES_CONTACTS, values,"category_id= " + category_id,null);
        db.close(); // Closing database connection
    }

    public String getCategoryContact(int categoryId) {
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES_CONTACTS +
                " WHERE " + KEY_CATEGORY_ID + "=" + categoryId;
//                + " ORDER BY " + KEY_NAME + " DESC;";
        Contact contactObj;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                return cursor.getString(3);  // contact_id
            }
            db.close();
            return "";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public Contact getContact(int categoryId) {
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES_CONTACTS +
                " WHERE " + KEY_CATEGORY_ID + "=" + categoryId;
//                + " ORDER BY " + KEY_NAME + " DESC;";
        Contact contactObj = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {

                contactObj = new Contact(cursor.getInt(1),cursor.getInt(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)
                        ,cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10)
                        ,cursor.getString(11),cursor.getString(12));

            }
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactObj;
    }

    public ArrayList<Contact> getAllContacts()
    {
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES_CONTACTS ;
        ArrayList<Contact> mList = new ArrayList<Contact>();

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                mList.add(new Contact(cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4)));
            }
            db.close();
            return mList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getContactIdForUpdate(int categoryId){
        String selectQuery = "SELECT "+KEY_CONTACT_ID+" FROM " + TABLE_CATEGORIES_CONTACTS +
        " WHERE " + KEY_CATEGORY_ID + "=" + categoryId;

        String contact_id = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                contact_id = cursor.toString();
            }
            db.close();
            return contact_id;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getGroupTableCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_GROUPS);
        db.close();
        return cnt;
    }
    public long getCategoriesTableCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_CATEGORIES);
        db.close();
        return cnt;
    }
    public long getCategoriesContactTableCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_CATEGORIES_CONTACTS);
        db.close();
        return cnt;
    }

   public void deleteCategoryWithContactDetails(int category_id){

//        String selectQuery = "DELETE "+KEY_CONTACT_ID+" FROM " + TABLE_CATEGORIES_CONTACTS +
//                " WHERE " + KEY_CATEGORY_ID + "=" + categoryId;
       Log.d("dbHandler:", "deleteCategoryWithContactDetails: Deleted Category_id="+category_id);
       SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CATEGORIES + " WHERE " + KEY_ID + "= '" + category_id + "'");
        db.execSQL("DELETE FROM " + TABLE_CATEGORIES_CONTACTS + " WHERE " + KEY_CATEGORY_ID + "= '" + category_id + "'");
        db.close();
    }

    public void deleteGroupWithAllCategoriesAndDetails(String group_name){

        String query = "SELECT "+KEY_ID+ " FROM "+TABLE_GROUPS+" WHERE "+KEY_NAME+"= '" + group_name+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( query, null);
        String id=null;
        if(cursor != null) {
            cursor.moveToFirst();
            id = cursor.toString();
        }
        Log.d(TAG, "deleteGroupWithAllCategoriesAndDetails: deleted id ="+id);

        db.execSQL("DELETE FROM " + TABLE_GROUPS + " WHERE " + KEY_NAME + "= '" + group_name + "'");
        db.execSQL("DELETE FROM " + TABLE_CATEGORIES + " WHERE " + KEY_GROUP_ID + "= '" + id + "'");
        db.execSQL("DELETE FROM " + TABLE_CATEGORIES_CONTACTS + " WHERE " + KEY_GROUP_ID + "= '" + id + "'");
        db.close();
    }


    public ArrayList<Contact> getallContactsToExport() {
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES_CONTACTS ;
        ArrayList<Contact> mList = new ArrayList<Contact>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {

                mList.add(new Contact(cursor.getInt(1),cursor.getInt(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)
                        ,cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10)
                        ,cursor.getString(11),cursor.getString(12)));

            }
            Log.d(TAG, "getallContactsToExport: length"+mList.size());
            db.close();
            return mList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public Group getGroup(String groupName) {
        String selectQuery = "SELECT * FROM " + TABLE_GROUPS+ " WHERE " + KEY_NAME + " = '" + groupName + "';";
//                 + " ORDER BY " + KEY_NAME + " DESC";

        Group group= null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                group = new Group(cursor.getInt(0), cursor.getString(1));
                Log.d(TAG, "getGroup: id="+cursor.getInt(0)+" name="+cursor.getString(1));
            }
            db.close();
            return group;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getCategoryId(String categoryName,int group_id) {
        String selectQuery = "SELECT "+KEY_ID+" FROM " + TABLE_CATEGORIES+ " WHERE " + KEY_NAME + " = '" + categoryName +"' AND "+
                KEY_GROUP_ID + " = '" +group_id+ "';";
//                 + " ORDER BY " + KEY_NAME + " DESC";

        int category_id = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                 category_id = cursor.getInt(0);
                Log.d(TAG, "getCategoryId: id="+cursor.getInt(0));
            }
            db.close();
            return category_id;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /*public boolean isNumberAllReadyAvaliable(String icomingNumber) {
        String selectQuery = "SELECT * FROM " + TABLE_RECENTS
                + " WHERE " + KEY_NUMBER + "='" + icomingNumber + "';";

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                return true;
            }
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getFrequency(String icomingNumber) {
        String selectQuery = "SELECT * FROM " + TABLE_RECENTS
                + " WHERE " + KEY_NUMBER + "='" + icomingNumber + "';";

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                return cursor.getInt(2);
            }
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int setFrequency(String icomingNumber, int frequency) {
        String where = KEY_NUMBER + "=?";
        String[] whereArgs = new String[]{icomingNumber};

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FREQUENCY, frequency);

        // Update Row
        int status = db.update(TABLE_RECENTS, values, where, whereArgs);
        db.close(); // Closing database connection
        return status;
    }

    public String[] getRecentContactsNames() {
        String selectQuery = "SELECT * FROM " + TABLE_RECENTS +
                " ORDER BY " + KEY_FREQUENCY + " DESC";
//                + " WHERE " + KEY_NUMBER + "=" + icomingNumber + ";";

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            String[] str = new String[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                str[i++] = cursor.getString(3);
            }
            db.close();
            return str;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addDefaultMessage(String message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGES, message);
        values.put(KEY_FREQUENCY, "0");

        // Inserting Row
        db.insert(TABLE_MESSAGES, null, values);
        db.close(); // Closing database connection
    }

    // Adding NEW message
    public void addMessage(String message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGES, message);
        values.put(KEY_FREQUENCY, "0");

        // Inserting Row
        db.insert(TABLE_MESSAGES, null, values);
        db.close(); // Closing database connection
    }

    // Fetching all messages
    public String[] getMessages() {
        String selectQuery = "SELECT * FROM " + TABLE_MESSAGES +
                " ORDER BY " + KEY_FREQUENCY + " DESC";

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            String[] str = new String[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                str[i++] = cursor.getString(0);
            }
            db.close();
            return str;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getMessageFrequency(String message) {
        String selectQuery = "SELECT * FROM " + TABLE_MESSAGES
                + " WHERE " + KEY_MESSAGES + "='" + message + "';";

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                return cursor.getInt(1);
            }
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int IncreamentMessageFrequency(String icomingNumber) {
        int freq = getMessageFrequency(icomingNumber) + 1;
        String where = KEY_MESSAGES + "=?";
        String[] whereArgs = new String[]{icomingNumber};

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FREQUENCY, freq);
        Log.d("freq", icomingNumber + "  =  " + freq + "  <==freq");

        // Update Row
        int status = db.update(TABLE_MESSAGES, values, where, whereArgs);
        Log.d("freq", status + "  <==status");
        db.close(); // Closing database connection
        return status;
    }


    // Adding NEW message
    public void removeMessage(String message) {
        String where = KEY_MESSAGES + "=?";
        String[] whereArgs = new String[]{message};

        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        db.delete(TABLE_MESSAGES, where, whereArgs);
        db.close(); // Closing database connection
    }

    // Adding new CALL log
    public void addQuedMsg(String number, String message, String at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TO, number);
        values.put(KEY_MSG, message);
        values.put(KEY_AT, at);

        // Inserting Row
        db.insert(TABLE_QUED_MESSAGES, null, values);
        db.close(); // Closing database connection
    }

    // removing message
    public void removeQuedMessage(String to, String message) {
        String where = KEY_TO + "=? and " + KEY_MSG + "=?";
        String[] whereArgs = new String[]{to, message};

        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        db.delete(TABLE_QUED_MESSAGES, where, whereArgs);
        db.close(); // Closing database connection
    }

    // Fetching all messages
    public Contact[] getQuedMessages() {
        String selectQuery = "SELECT * FROM " + TABLE_QUED_MESSAGES;

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Contact[] str = new Contact[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                str[i] = new Contact("", "", false);
                str[i].id = cursor.getString(3);
                str[i].number = cursor.getString(1);
                str[i].name = cursor.getString(2);
                Log.d("getQuedMessages", "=> " + str[i].id + " =  " + str[i].number + "  =  " + str[i].name);
                i++;
            }
            db.close();
            return str;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addProjMessage(String text) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGES, text);

        // Inserting Row
        db.insert(TABLE_PROJ_MESSAGES, null, values);
        db.close(); // Closing database connection
    }

    // Fetching all messages
    public String[] getProjMessages() {
        String selectQuery = "SELECT * FROM " + TABLE_PROJ_MESSAGES;

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            String[] str = new String[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                str[i++] = cursor.getString(0);
            }
            db.close();
            return str;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // removing message
    public void removeProjMessage(String text) {
        String where = KEY_MESSAGES + "=?";
        String[] whereArgs = new String[]{text};

        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        db.delete(TABLE_PROJ_MESSAGES, where, whereArgs);
        db.close(); // Closing database connection
    }*/
}
