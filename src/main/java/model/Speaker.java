package model;

import java.util.UUID;

/**
 * speakerslist
 * Created by soenke on 21.01.16.
 */
public class Speaker {
    private String uid;
    private String name;
    private Gender sex;
    private boolean hasSpoken = false;

    public Speaker(String name, Gender sex) {
        this.uid = UUID.randomUUID().toString();
        this.name = name;
        this.sex = sex;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getSex() {
        return sex;
    }

    public void setSex(Gender sex) {
        this.sex = sex;
    }

    public boolean isHasSpoken() {
        return hasSpoken;
    }

    public void setHasSpoken(boolean hasSpoken) {
        this.hasSpoken = hasSpoken;
    }
}
