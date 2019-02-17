package chefcharlesmich.smartappphonebook.VcardProgram;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import chefcharlesmich.smartappphonebook.R;

public class VCardMide {
    public int id;
    public String company_name, name, title, address, phone, email, description, birthday, website,
            industry, social1, social2, weblink1, weblink2, pic_link, vcf_path;
    public Bitmap picture;

    public VCardMide(int id,
                     String company_name, String name, String title, String address,
                     String phone, String email, String description, String birthday, String website,
                     String industry, String social1, String social2, String weblink1, String weblink2,
                     String pic_link, String vcf_path, Bitmap picture) {
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
        this.industry = industry;
        this.social1 = social1;
        this.social2 = social2;
        this.weblink1 = weblink1;
        this.weblink2 = weblink2;
        this.pic_link = pic_link;
        this.vcf_path = vcf_path;
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "VCardMide{" +
                "id=" + id +
                ", company_name='" + company_name + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", birthday='" + birthday + '\'' +
                ", website='" + website + '\'' +
                ", industry='" + industry + '\'' +
                ", social1='" + social1 + '\'' +
                ", social2='" + social2 + '\'' +
                ", weblink1='" + weblink1 + '\'' +
                ", weblink2='" + weblink2 + '\'' +
                ", pic_link='" + pic_link + '\'' +
                ", vcf_path='" + vcf_path + '\'' +
                '}';
    }
}
