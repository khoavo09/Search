package com.fantasticapps.musiqproto;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.List;

/**
 * TODO: View and functionalities for the dashboard
 */
public class DashBoard extends Fragment {

    private Button btnLoginReg, btnNowPlaying, btnSearch;

    private final String TAG = "Dashboard";
    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
    private static YouTube youtube;
    //private static String YOUTUBE_API_KEY = "AIzaSyDmBo_Ue4UxXi-8I0-425d9EEJshlw-SwE";
    private static String YOUTUBE_API_KEY = "AIzaSyAv2DDmw3ndjk8qwawTRcGnJYF7We9JC2M";


    /**
     * @param inflaterTwo
     * @param container_nav
     * @param savedInstanceStateTwo
     * @return View of the dashboard
     * View and functionalities of Dashboard: switch to login screen, view recent playlist, recent parties,
     *          recently liked songs, and nearby parties
     */
    @Override
    public View onCreateView(final LayoutInflater inflaterTwo, final ViewGroup container_nav, Bundle savedInstanceStateTwo) {
        super.onCreate(savedInstanceStateTwo);

        //Set view to dashboard
        View rootView = inflaterTwo.inflate(R.layout.activity_dashboard, container_nav, false);

        //If the "Login/Register" button is clicked, view changes to the login screen
        btnLoginReg = (Button) rootView.findViewById(R.id.buttonLoginReg);
        btnLoginReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoard.this.getContext(), LoginRegisteration.class));

            }
        });


        btnNowPlaying= (Button) rootView.findViewById(R.id.buttonNowPlaying);
        btnNowPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nowPlaying = new Intent(DashBoard.this.getContext(), NowPlaying.class);
                startActivity(nowPlaying);

            }
        });

        btnSearch = (Button) rootView.findViewById(R.id.buttonSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySearchResult();
            }
        });

        return rootView;
    }

    private void displaySearchResult() {


        try {
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("MusiQ-Mobile").build();
            Toast.makeText(getContext(),"WOO HOO WOO " ,Toast.LENGTH_SHORT).show();

            YouTube.Search.List search = youtube.search().list("id,snippet");
            search.setKey(YOUTUBE_API_KEY);
            search.setQ("hello");
            search.setType("video");
            //    search.setFields("items(id/kind,id/videoId,snippet/title,snippet/publishedAt,snippet/thumbnails/default/url),nextPageToken");
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            Toast.makeText(getContext(),"WOO HOO " ,Toast.LENGTH_SHORT).show();
            if (searchResultList != null) {
                //Log.d("TAG","Hello" + searchResultList.toString());
                Toast.makeText(getContext(),"HELLO " + searchResultList.toString(),Toast.LENGTH_SHORT).show();
            }/*
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());*/
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


}

