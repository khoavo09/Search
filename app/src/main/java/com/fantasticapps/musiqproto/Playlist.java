package com.fantasticapps.musiqproto;

/**
 * Created by Master on 10/6/2017.
 * Properties/Values of Playlists
 */

public class Playlist {
    private String title;
    private String id;


    public Playlist() {

    }

    public String getTitle()
    {return title;}

    public void setTitle(String s) {
        title = s;
    }

    public String getID() {
        return id;
    }

        public void setID(String s) {
        id = s;
    }
}
