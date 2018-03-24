Name: William Henson
Date: 3/24/18
Project: Parsing information from Business Card
Description: In this project, I have implemented Stanford's NLP in order to parse information off a business card.  The
    fields this program gathers are the NAME, PHONE NUMBER, and EMAIL ADDRESS.  I use the Stanford NLP in order to
    best decipher which of the possible lines is a name based on models they have already collected after using a
    regex to make sure it has the qualities for a name (no non word characters). There are other ways I thought that
    could have small success (such as check the similarity to their email address), however, Stanford's NLP is an
    open source that is way more accurate.  For the email and phone, I simply used regex in order to choose based on
    patterns that are necessary.

    The project has 2 main classes, BusinessCardParser and ContactInfo.

    I have included a Main class in order to show how the classes can be used and the output it gives

Helpful hints: When using the Stanford NLP, if you are using Java 9 SDK, you will need to include Jase EE API onto
    your build path in order for it to work.  If you do not do this, it will not be able to build the relationships
    in order to use the models and will therefore just give errors everytime you try to run.
    
    You can find all of the files under HiMom/src
    
    
 The class BusinessCardPArser uses Stanfords CoreNLP
 Manning, Christopher D., Surdeanu, Mihai, Bauer, John, Finkel, Jenny, Bethard, Steven J., and McClosky, David. 2014.
 The Stanford CoreNLP Natural Language Processing Toolkit In Proceedings of 52nd Annual Meeting of the Association for
 Computational Linguistics: System Demonstrations, pp. 55-60.[pdf][bib]
 https://stanfordnlp.github.io/CoreNLP/index.html
 
This is where i got the lenenshtein distance algorithm from, no need to design my own
https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
