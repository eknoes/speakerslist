package de.eknoes.speakerslist;

import de.eknoes.speakerslist.model.Gender;

/**
 * speakerslist
 * Created by soenke on 24.01.16.
 */
public class genderIdentification {
    private static genderIdentification instance;

    private genderIdentification() {

    }

    public static genderIdentification getInstance() {
        if(instance == null) {
            instance = new genderIdentification();
        }
        return instance;
    }

    public Gender getGender(String name) {
        switch (name) {
            case "Tom":
                return Gender.male;
            case "Gesine":
                return Gender.female;
            case "Erik":
                return Gender.male;
            case "Julius":
                return Gender.male;
        }
        return Gender.mixed;
    }
}


