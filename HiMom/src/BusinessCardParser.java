/**
 * William Henson
 * 3/24/18
 * This Class takes in a string that is seperated by new Lines (\n) and parses the information in order to get
 * the name, phoneNumber and email address of the person of interest.
 *
 * The class uses Stanfords CoreNLP
 * Manning, Christopher D., Surdeanu, Mihai, Bauer, John, Finkel, Jenny, Bethard, Steven J., and McClosky, David. 2014.
 * The Stanford CoreNLP Natural Language Processing Toolkit In Proceedings of 52nd Annual Meeting of the Association for
 * Computational Linguistics: System Demonstrations, pp. 55-60.[pdf][bib]
 * https://stanfordnlp.github.io/CoreNLP/index.html
 *
 * This is a Natural Language Processor that was used in determining what string was most likely the PERSON
 * While the algorithm is very accurate, it takes a bit since it is based on models collected about the English language
 *
 * This class will only work with text (aka the Business cards) that are in English, however, you can download and
 * use different models for different languages in order to increase the accuracy.
 *
 * In order to use Stanford's NLP, all of the information is on their github, linked above.  Without downloading
 * that code, this code, in its current state, may not find a name.  However, with a few tweaks, we could use other
 * methods to decide which string is most likely their name (like using levenshteinDistance to see if a string is close
 * to the email since many professional emails resemble their name).
 */




import edu.stanford.nlp.simple.Sentence;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BusinessCardParser {

    public ContactInfo getContactInfo(String document) throws Exception {
        //Create the lists in order to store what possibly could be the different values wanted
        List<String> names = new ArrayList<String>();
        List<String> phone = new ArrayList<String>();
        List<String> email = new ArrayList<String>();
        //These will eventually be the final values passed into the ContactInfo object and returned
        String name = null;
        String phoneNumber = null;
        String emailAddress = null;
        //Assuming that the String is passed in with new lines, we split it based on the new line char
        String[] list = document.split("\n");
        //for each line, check using regex to see if it matches what a name would need, etc
        for (String x : list) {
            String s = x.trim();
            //this is where it can be tricky since there are so many different formats, however,
            //most business cards do not label 'Name:' and therefore we will assume to find only their name
            //We will allow a title (Dr. etc) First name, Middle name (or initial), Last name and identifier (jr. sr.)
            //without this assumption, we could change the order, and just check to make sure it's not an
            //email and there exists no numbers, then we check to see of the strings left, which has more
            // 'Person tags' as you will see later in the text.  However, this makes the amount of check later less
            //but still allows for almost all formats
            if (Pattern.matches("^[A-Z]?[a-z]*\\.?\\s?[A-Z][a-z]+\\s[A-Z]?[a-z]*\\.?" +
                    "\\s?[A-Z][a-z]+\\s?[A-Z]?[a-z]*\\.?$", s)) {
                    names.add(s);
            }
            //for a phone number, we look for mainly the 9-10 digits that can appear in any format.
            //it can have a prefix to it or not, at this stage we just want all possible values other
            else if(Pattern.matches("^.*[0-9\\W]{9,16}$", s)) {
                phone.add(s);
            }
            //for an email we look for the @ symbol and a . plus some characters
            else if (Pattern.matches("^.*[@].*\\.[a-z]+$", s)) {
                email.add(s);
            }
        }
        //we use these a lot so to just set them once saves minimal time but some time
        int namesSize = names.size();
        int phoneSize = phone.size();
        int emailSize = email.size();
        //we first check to see if we found an email because of all values, that should be the easiest to find
        //however if for some reason we were unable to find an email, we state that fact in our answer, we could throw
        //an error however for our case we will just state NO EMAIL FOUND due to the fact its probably more important
        //to just aggreate any information we can get about the person.
        //Furthermore, we are assuming that if we find more than one email, that we just select the first one because
        //it would be difficult to decipher which one they would want us to contact unless there was a personal vs
        //office (this issue shows up again in phone). However, in this case of email, either should be sufficient
        //(in phone I have decided to allow the set of preferences (cell vs office vs just phone etc)
        if (emailSize > 0) {
            //this is seperating the email from the possible 'Email:' prefix by splitting based on whiespace and then
            //checking again which one is the email.
            String[] e = email.get(0).split(" ");
            for (String s : e) {
                if (Pattern.matches("^.*[@].*\\.[a-z]*$", s))
                    emailAddress = s;
            }
        }
        //Again, if we were going to use this data in a mass script to send out recruitment emails, then we would want
        //it to send an exception when calling this method so that we know that "hey this card did not have an email
        else {
            throw new Exception("NO EMAIL FOUND");
        }
        // if there is only one name possible then we know that it's that name
        if (namesSize == 1) {
            //the reason we do not split name is because of the assumption that they will not label their name with
            //'Name: ' and therefore the entire line will consist of their name
            name = names.get(0);
        }
        // if there are still multiple name strings possilbe, we will check to see which is close to the beginning
        //of their email due to the fact that most people have some sort of email address that ties into their own name
        //this was actually an implementation I was thinking of at first when I was struggling to get the Stanford
        //NLP to work.  However, some people use emails that have no tie to their name so in order to be as accurate as
        //possible, I decided to use an NLP (Native Language Processor) which comes with a Name Entity Recognizer that
        //uses models based on the English language to decide whether a word is a Person, Location etc.  This does
        //take longer than running a string diff algorithm, however it is way more accurate
        else if (namesSize > 0){
            //the reason I am not using toString for a List is due to the fact that the Stanford NLP will get confused
            //with the brackets and processes everyword as a PERSON making it worthless.  Therefore, I had to build the
            //string a certain way
            StringBuilder listBuilder = new StringBuilder();
            for (String n : names) {
                listBuilder.append(n + " ");
            }
            Sentence sent = new Sentence(listBuilder.toString());
            List<String> nerTags = sent.nerTags();
            //returns the first value of PERSON tag
            int index = nerTags.indexOf("PERSON");
            //if we didnt find a person then the algorithm couldnt find it and therefore there may not be a name
            //however, its possible that there is a name but the NLP just did not catch it
            if (index == -1) {
                //another way to hand this is the commented code below, however that could falsely pick a name
                throw new Exception("NER could not find a name, suggest manually checking");
            }
            else {
                //the reason we use both the first index of person and the following is because there are possible
                //times where a name is similar to the job etc so we want to get enough information to be sure and
                //the following should always be the next part anyways.
                //it is possible that the NLP gets it wrong and thinks only the last part is a person however, given
                //all the information, the algorithm is relatively accurate and this is accompanied by a longer time to
                //calculate the tags
                String check = sent.word(index) + " " + sent.word(index+ 1);
                for (String n : names) {
                    if (Pattern.matches(check, n))
                        name = n;
                }
            }



            /*if (emailAddress != null) {
                int endIndex = emailAddress.indexOf('@');
                String possibleName = emailAddress.substring(0, endIndex);
                int currSmallest = levenshteinDistance(names.get(0), possibleName);
                name = names.get(0);
                int temp;
                //check to see which one is the least different compared to the beginning of the email
                for (int i = 1; i < namesSize; i++) {
                    String currName = names.get(i);
                    temp = levenshteinDistance(currName, possibleName);
                    if (temp < currSmallest)
                        name = currName;
                }

            }*/
        }
        else {
            throw new Exception("NO NAME FOUND");
        }

        //so we check to see how many possible phone numbers we found
        if (phoneSize > 0) {
            //these two are here if we would want to change exactly which phone we would want (for example if they had
            //a cell and office we should later choose if cell != -1 then we can use cell, else use other it could also
            //be as a hey reminder we are calling them on work or personal time aka if you prefer to call them on
            //personal time then you would want to make sure you select cell or personal as a priority
            int cell = -1;
            int office = -1;

            //go through possible phones, designate which one is a cell vs office vs fax
            for (int i = 0; i < phoneSize; i++) {
                String tempPhone = phone.get(i);
                if (Pattern.matches("(Cell)|(Personal)",tempPhone))
                    cell = i;

                else if (Pattern.matches("(Office)|(Work)",tempPhone))
                    office = i;
                //in this case we dont want fax so we get rid of it
                else if (Pattern.matches("Fax|FAX|fax", tempPhone))
                    phone.remove(i);
            }
            //use a stringbuilder to get only the numbers for the phone
            StringBuilder sb = new StringBuilder();
            //for this code we arbitrarily pick the first phone number because we do not care if it is the office or
            //cell but could easily do (if cell then get(cell) etc
            String[] e = phone.get(0).split("\\D");
            for (String s : e) {
                if (Pattern.matches("^\\d*$", s))
                    sb.append(s);
            }
            phoneNumber = sb.toString();
        }
        else {
            throw new Exception("NO PHONE NUMBER");
        }
        return new ContactInfo(name, phoneNumber, emailAddress);
    }
    // This is where i got the lenenshtein distance algorithm from, no need to design my own
    // https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
    //currently I am not using this since I am putting most trust in the NLP to decide on names, however, this is
    //included in case I wanted to add an extra measure
    public int levenshteinDistance (String lhs, String rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) cost[i] = i;

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;

            // transformation cost for each letter in s0
            for(int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost; cost = newcost; newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }
}
