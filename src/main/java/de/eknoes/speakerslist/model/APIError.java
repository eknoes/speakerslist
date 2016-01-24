package de.eknoes.speakerslist.model;

/**
 * speakerslist
 * Created by soenke on 21.01.16.
 */
public class APIError {
    public int error;
    public String message;
    public APIError(int i, String s) {
        error = i;
        message = s;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
