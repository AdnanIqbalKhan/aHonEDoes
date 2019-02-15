package chefcharlesmich.smartappphonebook.Models;

/**
 * Created by sibghat on 8/29/2017.
 */

public class Contact {
    public int category_id,group_id;
    public String contact_id,name,phone,email,group_name,category_name,business,address,website,description;


    public Contact(){

    }
    public Contact( int category_id, int group_id,String contact_id, String name, String phone, String email, String group_name, String category_name, String business, String address, String website, String description) {
        this.category_id = category_id;
        this.group_id = group_id;
        this.contact_id = contact_id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.group_name = group_name;
        this.category_name = category_name;
        this.business = business;
        this.address = address;
        this.website = website;
        this.description = description;

    }

    public Contact(int category_id, int group_id, String contact_id, String name) {
        this.category_id = category_id;
        this.group_id = group_id;
        this.contact_id = contact_id;
        this.name = name;
    }
}
