/**
 * William Henson
 * 3/24/18
 * This class is just an object to hold all the information of the contact with Getters and a constructor
 */
public class ContactInfo {
  private String name;
  private String phoneNumber;
  private String emailAddress;

  public ContactInfo(String name, String phoneNumber, String emailAddress) {
      this.name = name;
      this.phoneNumber = phoneNumber;
      this.emailAddress = emailAddress;
  }

  public String getName() {
      return name;
  }

  public String getPhoneNumber() {
      return phoneNumber;
  }

  public String getEmailAddress() {
      return emailAddress;
  }
}
