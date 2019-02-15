package chefcharlesmich.smartappphonebook;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameter.EmailType;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Address;

import static android.content.ContentValues.TAG;

/**
 * Created by sibghat on 8/29/2017.
 */

public class Vcard {
    public static void main(String[] args) throws Throwable {
        VCard vcard = createVCard();

        //validate vCard for version 3.0
        System.out.println("Version 3.0 validation warnings:");
        System.out.println(vcard.validate(VCardVersion.V3_0));

        Log.d(TAG, "main: Version 3.0 validation warnings:");



        //write vCard
        File file = new File("aSmartAppPhoneBook.vcf");
        System.out.println("Writing " + file.getName() + "...");
        Ezvcard.write(vcard).version(VCardVersion.V3_0).go(file);
    }

    private static VCard createVCard() throws IOException {
        VCard vcard = new VCard();


//        vcard.setKind(Kind.individual());
//        vcard.setGender(Gender.male());

        vcard.addLanguage("en-US");

        vcard.setFormattedName("Jonathan Doe");
        vcard.addTelephoneNumber("1-555-555-5678", TelephoneType.WORK, TelephoneType.CELL);
        vcard.addEmail("doe.john@acme.com", EmailType.WORK);
        vcard.addExtendedProperty("Group","");
        vcard.setCategories("widgetphile");
        vcard.addExtendedProperty("Business","");
        vcard.addUrl("http://www.acme-co.com");


        vcard.addNote("");


//        StructuredName n = new StructuredName();
//        n.setFamily("Doe");
//        n.setGiven("Jonathan");
//        n.getPrefixes().add("Mr");
//        vcard.setStructuredName(n);


//        vcard.setNickname("John", "Jonny");
//
//        vcard.addTitle("Widget Engineer");
//
//        vcard.setOrganization("Acme Co. Ltd.", "Widget Department");

        Address adr = new Address();
//        adr.setStreetAddress("123 Wall St.");
//        adr.setLocality("New York");
//        adr.setRegion("NY");
//        adr.setPostalCode("12345");
//        adr.setCountry("USA");
//        adr.setLabel("123 Wall St.\nNew York, NY 12345\nUSA");
//        adr.getTypes().add(AddressType.WORK);
        vcard.addAddress(adr);





//        vcard.setGeo(37.6, -95.67);

        return vcard;
    }
}
