package com.fantasticapps.musiqproto;

import java.util.List;

/**
 * Created by khoavo on 10/15/17.
 * Properties/Values of a party
 */

public class Party {
    private Boolean Active;
    private String CurrentSong;
    private String Owner;
    private String Party_Name;
    private List<String> Queue;
    private List<String> Song;

    public Party() {
    }

    public Boolean getActive() {
        return Active;
    }

    public void setActive(Boolean Active) {
        this.Active = Active;
    }

    public String getCurrentSong() {
        return CurrentSong;
    }

    public void setCurrentSong(String CurrentSong) {
        this.CurrentSong = CurrentSong;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String Owner) {
        this.Owner = Owner;
    }

    public String getParty_Name() {
        return Party_Name;
    }

    public void setParty_Name(String Party_Name) {
        this.Party_Name = Party_Name;
    }

    public List<String> getQueue() {
        return Queue;
    }

    public void setQueue(List<String> queue) {
        Queue = queue;
    }

    public List<String> getSong() {
        return Song;
    }

    public void setSong(List<String> song) {
        Song = song;
    }
}
