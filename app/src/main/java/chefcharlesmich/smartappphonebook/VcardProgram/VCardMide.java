package chefcharlesmich.smartappphonebook.VcardProgram;

public class VCardMide {
    public int id;
    public String company_name, name, title, address, phone, email, info, birthday, website, social1, social2, blog, pic_link, vcf_path;

    public VCardMide(int id, String company_name, String name, String title, String address, String phone, String email, String info, String birthday, String website, String social1, String social2, String blog, String pic_link, String vcf_path) {
        this.id = id;
        this.company_name = company_name;
        this.name = name;
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.info = info;
        this.birthday = birthday;
        this.website = website;
        this.social1 = social1;
        this.social2 = social2;
        this.blog = blog;
        this.pic_link = pic_link;
        this.vcf_path = vcf_path;
    }
}
