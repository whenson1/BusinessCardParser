Name: William Henson
Date: 3/24/18
Project: Parsing information from Busniness Card
Description: In this project, I have implemented Stanford's NLP in order to parse information off a business card.  The
    fields this program gathers are the NAME, PHONE NUMBER, and EMAIL ADDRESS.  I use the Stanford NLP in order to
    best decipher which of the possible lines is a name based on models they have already collected after using a
    regex to make sure it has the qualities for a name (no non word characters). There are other ways I thought that
    could have small success (such as check the similarity to their email address), however, Stanford's NLP is an
    open source that is way more accurate.  For the email and phone, I simply used regex in order to choose based on
    patterns that are necessary.

    The project has 2 main classes, BusinessCardParser and ContactInfo.

    I have included a main class in order to show how the classes can be used and the output it gives

Helpful hints: When using the Stanford NLP, if you are using Java 9 SDK, you will need to include Jase EE API onto
    your build path in order for it to work.  If you do not do this, it will not be able to build the relationships
    in order to use the models and will therefore just give errors everytime you try to run.