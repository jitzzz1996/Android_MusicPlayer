package com.coreprogrammers.musicplayer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class SongsManager 
{
	// SDCard Path
	//final String MEDIA_PATH = new String("/sdcard/");
	
	final String MEDIA_PATH = "/sdcard/";
	
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	
	// Constructor
	public SongsManager()
	{

		
	}
	
	/**
	 * getplaylist Function to read all mp3 files from sdcard
	 * and store the details in ArrayList
	 * */
	public ArrayList<HashMap<String, String>> getPlayList()
	{
		
		try
		{
				File home = new File(MEDIA_PATH);
		
				CoreFileExtensionFilter _objCoreFileExtensionFilter=new CoreFileExtensionFilter();
				
				
				if (home.listFiles(_objCoreFileExtensionFilter).length > 0) 
				{
					for (File file : home.listFiles(_objCoreFileExtensionFilter)) 
					{
						HashMap<String, String> song = new HashMap<String, String>();
						
						song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
						song.put("songPath", file.getPath());
						
						// Adding each song to SongList
						songsList.add(song);
					}
				}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		// return songs list array
		return songsList;
	}
	
	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	
}
