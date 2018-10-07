package com.coreprogrammers.musicplayer;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Playlists;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PlayListActivity extends Activity 
{
	//listview is a class used to see the view that shows items in vertically scroll list
	
	ListView lstSong;
	AutoCompleteTextView autoCompleteSongSearch;
	
	// Songs list
	public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	public String[] songArr;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);
		
		autoCompleteSongSearch=(AutoCompleteTextView)findViewById(R.id.autoCompleteSongSearch);
		lstSong=(ListView)findViewById(R.id.lstSong);

		//ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();

		SongsManager _sngManager = new SongsManager();
		
		
		// get all songs from sdcard
		this.songsList = _sngManager.getPlayList();
		this.songArr=new String[this.songsList.size()];
		

		// looping through playlist
		for (int i = 0; i < songsList.size(); i++) 
		{
				// creating new HashMap
				HashMap<String, String> song = songsList.get(i);
	
				// adding HashList to ArrayList
				//songsListData.add(song);
				songArr[i]=new String();
				songArr[i]=songsList.get(i).get("songTitle").toString().trim();
		}
		
		
		
		ArrayAdapter _objArrayAdapter=new ArrayAdapter<String>(PlayListActivity.this, R.layout.playlist_item, songArr);

		// Adding menuItems to ListView
		ListAdapter _objListAdapter = new SimpleAdapter(PlayListActivity.this, songsList, R.layout.playlist_item, new String[] { "songTitle" }, new int[] {R.id.songTitle});
		
		autoCompleteSongSearch.setThreshold(1);
		autoCompleteSongSearch.setAdapter(_objArrayAdapter);
		
		
		/*public interface
		ListAdapter
		implements Adapter
		android.widget.ListAdapter
		
		ListAdapter's Known Indirect Subclasses
		ArrayAdapter<T>, BaseAdapter, CursorAdapter, HeaderViewListAdapter, ResourceCursorAdapter, SimpleAdapter, SimpleCursorAdapter, WrapperListAdapter
		
		Class Overview
		ListAdapter Extended Adapter that is the bridge between a ListView and the data list that backs the list. Frequently that data comes from a Cursor,
		but that is not required. The ListView can display any data provided that it is wrapped in a ListAdapter.*/
		
		

		lstSong.setAdapter(_objListAdapter);  //setListAdapter(adapter);

		
		
		// selecting single ListView item
	    //lv = getListView();
	    
		// listening to single listitem click
	    lstSong.setOnItemClickListener(new OnItemClickListener() 
		{
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
					{
						// getting listitem index
						
						// Starting new intent
						Intent in = new Intent(getApplicationContext(), GitsPlayerActivity.class);
						// Sending songIndex to PlayerActivity
						in.putExtra("songIndex", position);
						setResult(100, in);
						// Closing PlayListView
						finish();
					}
		});

	}
}
