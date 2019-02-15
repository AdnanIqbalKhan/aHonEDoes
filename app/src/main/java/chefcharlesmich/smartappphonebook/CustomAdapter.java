package chefcharlesmich.smartappphonebook;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import chefcharlesmich.smartappphonebook.Activity.MainActivity;
import chefcharlesmich.smartappphonebook.Models.Category;
import chefcharlesmich.smartappphonebook.Models.Contact;

public class CustomAdapter extends BaseAdapter implements View.OnClickListener {

    private static final String TAG ="CA" ;
    /***********
     * Declare Used Variables
     *********/
    private Activity activity;
    private ArrayList<Category> data;
    private static LayoutInflater inflater = null;
    public Resources res;
    private Category tempValues = null;
    int i = 0;
    DBHandler mdb;
    Context context;

    /*************
     * CustomAdapter Constructor
     *****************/
    public CustomAdapter(){

    }
    public CustomAdapter(Activity a, ArrayList<Category> d, Resources resLocal, DBHandler mdb,Context mcontext) {

        /********** Take passed values **********/
        activity = a;
        data = d;
        res = resLocal;
        this.mdb = mdb;
        this.context =mcontext;



        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    public void updateData(ArrayList<Category> d) {
        data = d;
        notifyDataSetChanged();
    }

    /********
     * What is the size of Passed Arraylist Size
     ************/
    public int getCount() {

//        if (data.size() <= 0)
//            return 1;
        if (data != null)
            return data.size();
        return 0;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /*********
     * Create a holder Class to contain inflated xml file elements
     *********/
    public static class ViewHolder {

        public TextView name_text;
        public TextView type_text;
        public ImageView pick, call, share;
    }

    /******
     * Depends upon data size called for each row , Create each ListView row
     *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.listitem_services, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.name_text = (TextView) vi.findViewById(R.id.tv_name);
            holder.type_text = (TextView) vi.findViewById(R.id.tv_type);
            holder.pick = (ImageView) vi.findViewById(R.id.iv_pick);
            holder.call = (ImageView) vi.findViewById(R.id.iv_call);
            holder.share = (ImageView) vi.findViewById(R.id.iv_share);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            holder.name_text.setText("No Data");
        } else {
            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = (Category) data.get(position);

//            Log.d(TAG, "getView: data.get(position)="+data.get(position));
//            Log.d(TAG, "getView: tempValues.id = "+tempValues.id);
            Contact contact = mdb.getContact(tempValues.id);

            String name;
            if(contact !=null) {
                name = contact.name;
                Log.d(TAG, "getView: contact.name = " + contact.name);
            }
            else
                name = "";
            /************  Set Model values in Holder elements ***********/
            holder.name_text.setText(tempValues.name);
            holder.type_text.setText(name);
            /******** Set Item Click Listner for LayoutInflater for each row *******/
//            vi.setOnClickListener(new OnItemClickListener(position));
            holder.pick.setOnClickListener(new OnItemClickListener(position));
            holder.call.setOnClickListener(new OnItemClickListener(position));
            holder.share.setOnClickListener(new OnItemClickListener(position));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    /*********
     * Called when Item click in ListView
     ************/
    private class OnItemClickListener implements View.OnClickListener{
        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            MainActivity sct = (MainActivity) activity;
            Log.d(TAG, "onClick: mPosition ="+mPosition);
            switch (v.getId()) {
                case R.id.iv_pick:
                    sct.onPickClick(mPosition);
//                    Toast.makeText(activity, "Pick Clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.iv_call:
                    sct.onCallItemClick(mPosition);
//                    Toast.makeText(activity, "Call Clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.iv_share:
                    try {

                            sct.createVcard(mPosition);
//                            sct.sendEmail();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    Toast.makeText(activity, "Share Clicked", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/
                    sct.onItemClick(mPosition);
            }
        }


    }
}
