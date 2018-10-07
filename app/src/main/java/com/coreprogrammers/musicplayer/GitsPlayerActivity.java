package com.coreprogrammers.musicplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;



public class GitsPlayerActivity extends Activity  
{

	private ImageButton btnPlay;
	private ImageButton btnForward;
	private ImageButton btnBackward;
	private ImageButton btnNext;
	private ImageButton btnPrevious;
	private ImageButton btnPlaylist;
	private ImageButton btnRepeat;
	private ImageButton btnShuffle;
	private SeekBar songProgressBar;
	private TextView songTitleLabel;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	// Media Player
	private  MediaPlayer mp;
	// Handler to update UI timer, progress bar etc,.
	private Handler mHandler = new Handler();
	
	private SongsManager _objSongManager;
	
	private Utilities utils;
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000; // 5000 milliseconds
	private int currentSongIndex = 0; 
	
	private boolean isShuffle = false;
	private boolean isRepeat = false;
	
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	
//////////////////////code for speech to text////////////////////////////////////////////////////////////////////////////////////////
	
	private TextView txtSpeechInput;
	private ImageButton btnSpeak;
	private final int REQ_CODE_SPEECH_INPUT = 200;
	
	//oncreate is where you initialize ur activity or used to start an activity
	//bundle is used to save & recover state info for ur activity 
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//if any case that is killing of ur app oncreate call again then savedinstance used to reload the previous state information
		super.onCreate(savedInstanceState);
		//setcontentview is to set the xml
		setContentView(R.layout.player);
//		<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	//	<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>


		
		
		// All player buttons
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnForward = (ImageButton) findViewById(R.id.btnForward);
		btnBackward = (ImageButton) findViewById(R.id.btnBackward);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
		btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
		btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
		btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		songTitleLabel = (TextView) findViewById(R.id.songTitle);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
		
		// Mediaplayer
		 mp = new MediaPlayer();
		_objSongManager = new SongsManager();
		 utils = new Utilities();
		
		// Listeners
		//songProgressBar.setOnSeekBarChangeListener(this); // Important
		
		 OnSeekBarChangeListener _objOnSeekBarChangeListener = new OnSeekBarChangeListener() 
		 { 
			    @Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch)
				{
					
				}

				/**
				 * When user starts moving the progress handler
				 * */
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) 
				{
					// remove message Handler from updating progress bar
					mHandler.removeCallbacks(mUpdateTimeTask);
			    }
				
				/**
				 * When user stops moving the progress hanlder
				 * */
				@Override
			    public void onStopTrackingTouch(SeekBar seekBar)
				{
					mHandler.removeCallbacks(mUpdateTimeTask);
					
					int totalDuration = mp.getDuration();
					int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
					
					// forward or backward to certain seconds
					mp.seekTo(currentPosition);
					
					// update timer progress again
					updateProgressBar();
			    }
		};

		songProgressBar.setOnSeekBarChangeListener(_objOnSeekBarChangeListener);
		 
				 
		//mp.setOnCompletionListener(this); // Important*/
		 
		 OnCompletionListener _objOnCompletionListener = new MediaPlayer.OnCompletionListener() 
		 { 
			
			 /**
				 * On Song Playing completed
				 * if repeat is ON play same song again
				 * if shuffle is ON play random song
				 * */
			 
			@Override
			public void onCompletion(MediaPlayer mp) 
			{
				// check for repeat is ON or OFF
				if(isRepeat)
				{
					// repeat is on play same song again
					playSong(currentSongIndex);
				} 
				else if(isShuffle)
				{
					// shuffle is on - play a random song
					Random rand = new Random();
					currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
					playSong(currentSongIndex);
				} 
				else
				{
					// no repeat or shuffle ON - play next song
					if(currentSongIndex < (songsList.size() - 1))
					{
						playSong(currentSongIndex + 1);
						currentSongIndex = currentSongIndex + 1;
					
					}
					else
					{
						// play first song
						playSong(0);
						currentSongIndex = 0;
					}
				}
				
			}
		};
		//setonCompletionlistener is a function in media player and the objonCompletion listener is anonymous inner class as an argument
		mp.setOnCompletionListener(_objOnCompletionListener);
		
		// Getting all songs list
		songsList = _objSongManager.getPlayList();
		
		// By default play first song
		playSong(0);
				
		/**
		 * Play button click event
		 * plays a song and changes button to pause image
		 * pauses a song and changes button to play image
		 * */
		OnClickListener _objOnClickListner=new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				// check for already playing
				if(mp.isPlaying())
				{
					if(mp!=null)
					{
						mp.pause();
						// Changing button image to play button
						btnPlay.setImageResource(R.drawable.btn_play);
					}
				}
				else
				{
					// Resume song
					if(mp!=null)
					{
						mp.start();
						// Changing button image to pause button
						btnPlay.setImageResource(R.drawable.btn_pause);
					}
				}
				
			}
		};
		
		
		btnPlay.setOnClickListener(_objOnClickListner);
		
		/**
		 * Forward button click event
		 * Forwards song specified seconds
		 * */
		btnForward.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				// get current song position				
				int currentPosition = mp.getCurrentPosition();
				// check if seekForward time is lesser than song duration
				if(currentPosition + seekForwardTime <= mp.getDuration())
				{
					// forward song
					mp.seekTo(currentPosition + seekForwardTime);
				}
				else
				{
					// forward to end position
					mp.seekTo(mp.getDuration());
				}
			}
		});
		
		
		
		/**
		 * Backward button click event
		 * Backward song to specified seconds
		 * */
		btnBackward.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View arg0) 
			{
				// get current song position				
				int currentPosition = mp.getCurrentPosition();
				// check if seekBackward time is greater than 0 sec
				if(currentPosition - seekBackwardTime >= 0)
				{
					// forward song
					mp.seekTo(currentPosition - seekBackwardTime);
				}
				else
				{
					// backward to starting position
					mp.seekTo(0);
				}
				
			}
		});
		
		/**
		 * Next button click event
		 * Plays next song by taking currentSongIndex + 1
		 * */
		btnNext.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				// check if next song is there or not
				if(currentSongIndex < (songsList.size() - 1))
				{
					playSong(currentSongIndex + 1);
					currentSongIndex = currentSongIndex + 1;
				}
				else
				{
					// play first song
					playSong(0);
					currentSongIndex = 0;
				}
				
			}
		});
		
		/**
		 * Back button click event
		 * Plays previous song by currentSongIndex - 1
		 * */
		btnPrevious.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				if(currentSongIndex > 0)
				{
					playSong(currentSongIndex - 1);
					currentSongIndex = currentSongIndex - 1;
				}
				else
				{
					// play last song
					playSong(songsList.size() - 1);
					currentSongIndex = songsList.size() - 1;
				}
				
			}
		});
		
		/**
		 * Button Click event for Repeat button
		 * Enables repeat flag to true
		 * */
		btnRepeat.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				if(isRepeat)
				{
					isRepeat = false;
					Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
					btnRepeat.setImageResource(R.drawable.btn_repeat);
				}
				else
				{
					// make repeat to true
					isRepeat = true;
					Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
					// make shuffle to false
					isShuffle = false;
					btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
				}	
			}
		});
		
		/**
		 * Button Click event for Shuffle button
		 * Enables shuffle flag to true
		 * */
		btnShuffle.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				if(isShuffle)
				{
					isShuffle = false;
					Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
				}
				else
				{
					// make repeat to true
					isShuffle= true;
					Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
					// make shuffle to false
					isRepeat = false;
					btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
					btnRepeat.setImageResource(R.drawable.btn_repeat);
				}	
			}
		});
		
		/**
		 * Button Click event for Play list click event
		 * Launches list activity which displays list of songs
		 * */
		btnPlaylist.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				Intent i = new Intent(getApplicationContext(), PlayListActivity.class);
				startActivityForResult(i, 100);			
			}
		});
		
		
/////////////////////////////Code for Speech to text/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

		
		btnSpeak.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v)
			{
				promptSpeechInput();
			}
		});

		
		
		
	}//end of on create
	
	
	
	
	
	
	/**
	 * Receiving song index from playlist view
	 * and play the song
	 * */
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent in) 
	{
        super.onActivityResult(requestCode, resultCode, in);
        
        if(resultCode == 100)
        {
         	 currentSongIndex = in.getExtras().getInt("songIndex");
         	 
         	 // play selected song
             playSong(currentSongIndex);
        }
        else if(REQ_CODE_SPEECH_INPUT==200)
	    {
		     if (resultCode == RESULT_OK && null != in) 
		     {

			      ArrayList<String> result = in.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			      txtSpeechInput.setText(result.get(0));
		     }
		   
	    }
 
    }
	
	/**
	 * Function to play a song
	 * @param songIndex - index of song
	 * */
	public void  playSong(int songIndex)
	{
		// Play song
		try 
		{
        	mp.reset();
			mp.setDataSource(songsList.get(songIndex).get("songPath"));/************/
			mp.prepare();
			mp.start();
			// Displaying Song title
			
			String songTitle = songsList.get(songIndex).get("songTitle");
        	songTitleLabel.setText(songTitle);
			
        	// Changing Button Image to pause image
			
        	btnPlay.setImageResource(R.drawable.btn_pause);
			
			// set Progress bar values
			songProgressBar.setProgress(0);
			songProgressBar.setMax(100);
			
			// Updating progress bar
			updateProgressBar();			
		} 
		catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalStateException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch(Exception ex) {
			
			final AlertDialog.Builder builder = new AlertDialog.Builder(GitsPlayerActivity.this);
			builder.setMessage(ex.toString());
			AlertDialog alertdialog = builder.create();
			alertdialog.show();
			
		}
	}
	
	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar()
	{
        mHandler.postDelayed(mUpdateTimeTask, 100);  
        
       /* postDelayed
        Added in API level 1
        boolean postDelayed (Runnable r, 
                        long delayMillis)
        Causes the Runnable r to be added to the message queue, to be run after the specified amount of time elapses. 
        The runnable will be run on the thread to which this handler is attached. The time-base is uptimeMillis(). 
        Time spent in deep sleep will add an additional delay to execution.

        Parameters
        r	Runnable: The Runnable that will be executed.
        delayMillis	long: The delay (in milliseconds) until the Runnable will be executed.
        Returns
        boolean	Returns true if the Runnable was successfully placed in to the message queue. 
        Returns false on failure, usually because the looper processing the message queue is exiting. 
        Note that a result of true does not mean the Runnable will be processed -- if the looper is quit before the 
        delivery time of the message occurs then the message will be dropped.*/
        
    }	
	
	
	
	
	
	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable()
	{
           @Override		
		   public void run() 
		   {
			   long totalDuration = mp.getDuration();
			   long currentDuration = mp.getCurrentPosition();
			  
			   // Displaying Total Duration time
			   songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
			   // Displaying time completed playing
			   songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));
			   
			   // Updating progress bar
			   int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
			   //Log.d("Progress", ""+progress);
			   songProgressBar.setProgress(progress);
			   
			   // Running this thread after 100 milliseconds
		       mHandler.postDelayed(this, 100);
		   }
	};
		
	/**
	 * 
	 * */
	

	
	
	
	 @Override
	 public void onDestroy()
	 {
	       super.onDestroy();
	       mp.release();
	 }
	 
	 
	 
/////////////////Code for speech to Text//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /**
		 * Showing google speech input dialog
		 * */
		private void promptSpeechInput() 
		{
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

			//Constants for supporting speech recognition through starting an Intent
			
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
			
			
			try
			{
				startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
			} 
			catch (ActivityNotFoundException a) 
			{
				Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
			}
		}
	 
	 
	 
	
}