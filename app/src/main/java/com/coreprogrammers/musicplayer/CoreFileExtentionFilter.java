package com.coreprogrammers.musicplayer;

import java.io.File;
import java.io.FilenameFilter;

class CoreFileExtensionFilter implements FilenameFilter
{
	@Override
	public boolean accept(File dir, String name)
	{
		return (name.endsWith(".mp3") || name.endsWith(".MP3")|| name.endsWith(".FLAC")
				|| name.endsWith(".flac")|| name.endsWith(".AAC")|| name.endsWith(".aac")
				|| name.endsWith(".3GP")|| name.endsWith(".3gp")|| name.endsWith(".M4A")
				|| name.endsWith(".m4a")|| name.endsWith(".MIDI")|| name.endsWith(".midi")
				|| name.endsWith(".RTX")|| name.endsWith(".rtx")|| name.endsWith(".OGG")
				|| name.endsWith(".ogg")|| name.endsWith(".WAV")|| name.endsWith(".wav")
				|| name.endsWith(".WMA")|| name.endsWith(".wma")|| name.endsWith(".ALAC")
				|| name.endsWith(".alac")|| name.endsWith(".AIFF")|| name.endsWith(".aiff"));
	}
}