package de.eknoes.speakerslist.model;

import sun.awt.image.ImageWatched;

import java.util.*;

/**
 * speakerslist
 * Created by soenke on 24.01.16.
 */
public class SpeakerQueue extends LinkedList<Speaker> {
    private boolean preferFirstWord = false;
    private boolean balanceGender = false;

    private Map<Gender, Integer> counter = new HashMap<>();

    private Map<Speaker, Long> times = new HashMap<>();

    public SpeakerQueue(boolean pFW, boolean bG) {
        this();
        preferFirstWord = pFW;
        balanceGender = bG;
    }

    public SpeakerQueue() {
        counter.put(Gender.male, 0);
        counter.put(Gender.female, 0);
        counter.put(Gender.mixed, 0);
    }

    public boolean isPreferFirstWord() {
        return preferFirstWord;
    }

    public void setPreferFirstWord(boolean preferFirstWord) {
        if(preferFirstWord == this.preferFirstWord) return;

        this.preferFirstWord = preferFirstWord;
        this.recalculate();
    }

    public boolean isBalanceGender() {
        return balanceGender;
    }

    public void setBalanceGender(boolean balanceGender) {
        if(balanceGender == this.balanceGender) return;
        this.balanceGender = balanceGender;
        this.recalculate();
    }

    public Map<Gender, Integer> getCounter() {
        return counter;
    }

    public void setCounter(Map<Gender, Integer> counter) {
        this.counter = counter;
    }

    @Override
    public boolean add(Speaker speaker) {
        for (int i = 0; i < this.size(); i++) {
            Speaker s = this.get(i);
            if(preferFirstWord) {
                if(s.isHasSpoken() && !speaker.isHasSpoken()) {
                    this.add(i, speaker);
                    return true;
                }
                if(s.isHasSpoken() == speaker.isHasSpoken()) {
                    if(balanceGender) {
                        if(counter.get(speaker.getSex()) < counter.get(s.getSex())) {
                            this.add(i, speaker);
                            return true;
                        }
                    }
                }
            } else {
                if(balanceGender) {
                    if(counter.get(speaker.getSex()) < counter.get(s.getSex())) {
                        this.add(i, speaker);
                        return true;
                    }
                }
            }
        }
        this.add(this.size(), speaker);
        return true;
    }

    @Override
    public void add(int index, Speaker element) {
        counter.replace(element.getSex(), counter.get(element.getSex()) + 1);
        System.out.println("M" + counter.get(Gender.male) + "|F" + counter.get(Gender.female) + "|T" + counter.get(Gender.mixed));
        times.put(element, new Date().getTime());
        super.add(index, element);
    }

    public boolean remove(Speaker s) {
        times.remove(s);
        counter.replace(s.getSex(), counter.get(s.getSex()) - 1);
        return super.remove(s);
    }

    public void recalculate() {
        counter.put(Gender.male, 0);
        counter.put(Gender.female, 0);
        counter.put(Gender.mixed, 0);

        LinkedList<Speaker> temp = new LinkedList<>();

        while(this.size() > 0) {
            temp.add(this.poll());
        }

        temp.sort(Speaker::compareTo);

        for(Speaker s : temp) {
            this.add(s);
        }


    }
}
