package chefcharlesmich.smartappphonebook.VcardProgram;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandlerVcard extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "prompt1";
    private static final int DATABASE_VERSION = 1;

    // Contacts table name
    private static final String TABLE_VCARD = "vcard";

    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_COMPANY_NAME = "company_name";
    private static final String KEY_NAME = "name";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_BIRTHDAY = "bday";
    private static final String KEY_WEBSITE = "website";
    private static final String KEY_SOCIAL1 = "social1";
    private static final String KEY_SOCIAL2 = "social2";
    private static final String KEY_WEBLINK1 = "weblink1";
    private static final String KEY_WEBLINK2 = "weblink2";
    private static final String KEY_PICTURE_LINK = "piclink";
    private static final String KEY_VCF_PATH = "vcf_path";

    public DBHandlerVcard(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_SMSLOGS_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_VCARD + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_COMPANY_NAME
                + " TEXT," + KEY_NAME + " TEXT," + KEY_TITLE + " TEXT," + KEY_ADDRESS + " TEXT," +
                KEY_PHONE + " TEXT," + KEY_EMAIL + " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_BIRTHDAY + " TEXT," +
                KEY_WEBSITE + " TEXT," + KEY_SOCIAL1 + " TEXT," + KEY_SOCIAL2 + " TEXT," + KEY_WEBLINK1 + " TEXT,"
                + KEY_WEBLINK2 + " TEXT," + KEY_VCF_PATH + " TEXT," + KEY_PICTURE_LINK + " TEXT);";
        db.execSQL(CREATE_SMSLOGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VCARD);
        // Creating tables again
        onCreate(db);
    }

    // Adding new CALL log
    public long addVcardIfo(VCardMide vCard) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COMPANY_NAME, vCard.company_name);
        values.put(KEY_NAME, vCard.name);
        values.put(KEY_TITLE, vCard.title);
        values.put(KEY_ADDRESS, vCard.address);
        values.put(KEY_PHONE, vCard.phone);
        values.put(KEY_EMAIL, vCard.email);
        values.put(KEY_DESCRIPTION, vCard.description);
        values.put(KEY_BIRTHDAY, vCard.birthday);
        values.put(KEY_WEBSITE, vCard.website);
        values.put(KEY_SOCIAL1, vCard.social1);
        values.put(KEY_SOCIAL2, vCard.social2);
        values.put(KEY_WEBLINK1, vCard.weblink1);
        values.put(KEY_WEBLINK2, vCard.weblink2);
        values.put(KEY_PICTURE_LINK, vCard.pic_link);
        values.put(KEY_VCF_PATH, vCard.pic_link);

        // Inserting Row
        long id = db.insert(TABLE_VCARD, null, values);
        Log.d("insertlog", "" + id);
        db.close(); // Closing database connection
        return id;
    }

    public boolean deleteVCardByID(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_VCARD, KEY_ID + "=" + id, null) > 0;

    }

    public VCardMide getVCardById(int id) {
        String selectQuery = "SELECT * FROM " + TABLE_VCARD
                + " WHERE " + KEY_ID + "=" + id + ";";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                return new VCardMide(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10), cursor.getString(11),
                        cursor.getString(12), cursor.getString(13), cursor.getString(14)
                        , cursor.getString(15));
            }
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<VCardMide> getAllVcards() {
        String selectQuery = "SELECT * FROM " + TABLE_VCARD;
        /*+ " WHERE " + KEY_COMPANY_NAME + "='" + icomingNumber + "';";*/

        ArrayList<VCardMide> mList = new ArrayList<VCardMide>();

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                mList.add(new VCardMide(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10), cursor.getString(11),
                        cursor.getString(12), cursor.getString(13), cursor.getString(14)
                        , cursor.getString(15)));
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mList;
    }

    public int updateVcard(VCardMide vCard) {
        String where = KEY_ID + "=?";
        String[] whereArgs = new String[]{vCard.id + ""};

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COMPANY_NAME, vCard.company_name);
        values.put(KEY_NAME, vCard.name);
        values.put(KEY_TITLE, vCard.title);
        values.put(KEY_ADDRESS, vCard.address);
        values.put(KEY_PHONE, vCard.phone);
        values.put(KEY_EMAIL, vCard.email);
        values.put(KEY_DESCRIPTION, vCard.description);
        values.put(KEY_BIRTHDAY, vCard.birthday);
        values.put(KEY_WEBSITE, vCard.website);
        values.put(KEY_SOCIAL1, vCard.social1);
        values.put(KEY_SOCIAL2, vCard.social2);
        values.put(KEY_WEBLINK1, vCard.weblink1);
        values.put(KEY_WEBLINK2, vCard.weblink2);
        values.put(KEY_PICTURE_LINK, vCard.pic_link);
        values.put(KEY_VCF_PATH, vCard.pic_link);


        // Update Row
        int status = db.update(TABLE_VCARD, values, where, whereArgs);
        db.close(); // Closing database connection
        return status;
    }

    public String[] getRecentContactsNumbers() {
        String selectQuery = "SELECT * FROM " + TABLE_VCARD +
                " ORDER BY " + KEY_TITLE + " DESC";
//                + " WHERE " + KEY_NUMBER + "=" + icomingNumber + ";";

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            String[] str = new String[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                str[i++] = cursor.getString(1);
            }
            db.close();
            return str;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] getRecentContactsNames() {
        String selectQuery = "SELECT * FROM " + TABLE_VCARD +
                " ORDER BY " + KEY_TITLE + " DESC";
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

  /*
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
