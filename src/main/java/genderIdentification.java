import model.Gender;

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
        return Gender.male;
    }
}


