package chefcharlesmich.smartappphonebook.VcardProgram;

public class VCardMide {
    public int id;
    public String company_name, name, title, address, phone, email, description, birthday, website,
            social1, social2, weblink1, weblink2, pic_link, vcf_path;

    public VCardMide(int id,
                     String company_name, String name, String title, String address,
                     String phone, String email, String description, String birthday, String website,
                     String social1, String social2, String weblink1, String weblink2,
                     String pic_link, String vcf_path) {
        this.id = id;
        this.company_name = company_name;
        this.name = name;
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.description = description;
        this.birthday = birthday;
        this.website = website;
        this.social1 = social1;
        this.social2 = social2;
        this.weblink1 = weblink1;
        this.weblink2 = weblink2;
        this.pic_link = pic_link;
        this.vcf_path = vcf_path;
    }
}
