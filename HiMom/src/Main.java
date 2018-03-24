/**
 * William Henson
 * 3/24/18
 * This is just a main class in order to test the functionality of the code
 */

import java.lang.*;


public class Main {

    public static void main(String[] args) {
        //this gives an error since there is no name
        //i tested others but this just so happens to be the last test I ran
        String practice = "Acme Technologies\n" +
                "Analytic Developer\n" +
                "1234 Roadrunner Way\n" +
                "Columbia, MD 12345\n" +
                "Phone: 410-555-1234\n" +
                "Fax: 410-555-4321\n" +
                "Jane.doe@acmetech.com\n ";
        BusinessCardParser Bobs = new BusinessCardParser();
        try {
            ContactInfo b = Bobs.getContactInfo(practice);
            System.out.println(b.getName());
            System.out.println(b.getPhoneNumber());
            System.out.println(b.getEmailAddress());

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}

