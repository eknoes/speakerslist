package de.eknoes.speakerslist.model;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * speakerslist
 * Created by soenke on 21.01.16.
 */
public class SpeakerList {
    private String id;
    private boolean sexBalanced = false;
    private boolean preferNewSpeaker = false;

    private List<Speaker> knownSpeakers = new LinkedList<>();
    private List<Speaker> queue = new LinkedList<>();

    public SpeakerList() {
        this.id = UUID.randomUUID().toString();
    }


    public List<Speaker> getKnownSpeakers() {
        return knownSpeakers;
    }

    public void setKnownSpeakers(List<Speaker> knownSpeakers) {
        this.knownSpeakers = knownSpeakers;
    }

    public List<Speaker> getSpeakers() {
        return queue;
    }

    public void setSpeakers(List<Speaker> speakers) {
        this.queue = speakers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSexBalanced() {
        return sexBalanced;
    }

    public void setSexBalanced(boolean sexBalanced) {
        this.sexBalanced = sexBalanced;
    }

    public boolean isPreferNewSpeaker() {
        return preferNewSpeaker;
    }

    public void setPreferNewSpeaker(boolean preferNewSpeaker) {
        this.preferNewSpeaker = preferNewSpeaker;
    }
}
