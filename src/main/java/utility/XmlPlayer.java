package utility;


import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.jfugue.integration.MusicXmlParser;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.ManagedPlayer;
import org.jfugue.player.Player;
import org.staccato.StaccatoParserListener;

import javafx.util.Duration;


public class XmlPlayer {
	/**
	 * String to store score instrument
	 */
	private String inst;
	/**
	 *String to store xml string representation of score
	 */
	private String xmlString;
	/**
	 * MusicXmlParser object to parse xml string 
	 */
	private MusicXmlParser parser;
	/**
	 * Listener object to help with playing function
	 */
	private StaccatoParserListener listener;
	/**
	 * Pattern to help with playing function
	 */
	private Pattern staccatoPattern;
	/**
	 * Player object to play guitar music
	 */
	private Player gPlayer;
	/**
	 * Player object to play drum music
	 */

	/**
	 * Constructor takes xml output as string, creates new instances of our
	 * fields and calls update method
	 * 
	 * @param str
	 * @throws Exception
	 */
	public XmlPlayer(String str, String instrument) throws Exception {
		xmlString = str;
		parser = new MusicXmlParser();
		listener = new StaccatoParserListener();
		staccatoPattern = new Pattern();
		gPlayer = new Player();
		inst=instrument;
		update();
	}

	/**
	 * Helper method to add listener to parser, parse given xml string and create
	 * pattern object from listener
	 * 
	 * @throws Exception
	 */
	private void update() throws Exception {
		// TODO Auto-generated method stub
		parser.addParserListener(listener);
		parser.parse(xmlString);
		
		if(this.inst.equals("Guitar")) {
			staccatoPattern = listener.getPattern().setTempo(120).setInstrument(24);
		}
		else {
			staccatoPattern = listener.getPattern();
		}
		//System.out.println(Integer.valueOf(staccatoPattern.getTokens().get(0).toString().substring(1)));
	}

	/**
	 * Play music method Plays the music from xml string
	 * starting from beginning if the playback hasn't started
	 * 
	 * @throws Exception
	 */
	public void play() throws Exception {
		// If playback has never been started, start playback
		if (!gPlayer.getManagedPlayer().isStarted()) {
			gPlayer.getManagedPlayer().start(gPlayer.getSequence(staccatoPattern));
		}
		// Otherwise resume playback
		else {
			if(gPlayer.getManagedPlayer().isFinished()) {
				gPlayer.getManagedPlayer().start(gPlayer.getSequence(staccatoPattern));
			}
			else {
			gPlayer.getManagedPlayer().resume();
			}
		}
		

	}

	/**
	 * pauses music playback
	 */
	public void pause() {
		gPlayer.getManagedPlayer().pause();
	}
	/**
	 * Given a time from start to end (0.0-1.0) of the track,
	 * this method moves the audio playback to the specified time
	 * 
	 * @param time
	 */
	public void seek(float time) {
		long tk = gPlayer.getManagedPlayer().getTickLength();
		gPlayer.getManagedPlayer().seek((long)(time*tk));
	}
	
	/**
	 * set tempo method to change the tempo of the playback
	 * @param vol
	 * @throws Exception 
	 * 
	 */
	public void setTempo(float temp) throws Exception {
		staccatoPattern.setTempo((int)(temp));
		if(gPlayer.getManagedPlayer().isPlaying()) {
		long pos = gPlayer.getManagedPlayer().getTickPosition();
		gPlayer.getManagedPlayer().finish();
		gPlayer.getManagedPlayer().start(gPlayer.getSequence(staccatoPattern));
		gPlayer.getManagedPlayer().seek(pos);
//		gPlayer.getSequence(staccatoPattern)
//		gPlayer.getManagedPlayer().getTickPosition()
		}
	}
	
	/**
	 * get tempo method to get the tempo of the playback
	 * @param vol
	 */
	public int getTempo() {
		int temp = Integer.valueOf(staccatoPattern.getTokens().get(0).toString().substring(1));
		System.out.println(staccatoPattern.getTokens());
		return temp;
		
	}
	/**
	 * Method to get the sequences duration in the format HH:MM:SS 
	 * @return
	 */
	public String getDuration() {
	Duration time= new Duration(gPlayer.getSequence(staccatoPattern).getMicrosecondLength()/1000);
	
	int hours = (int) time.toHours();
    int minutes = (int) time.toMinutes();
    int seconds = (int) time.toSeconds();

    // Fix the issue with the timer going to 61 and above for seconds, minutes, and hours
    if (seconds > 59) seconds = seconds % 60;
    if (minutes > 59) minutes = minutes % 60;
    if (hours > 59) hours = hours % 60;

    // Don't show the hours unless the song is for an hour or longer
    if (hours > 0) 
    	return String.format("%d:%02d:%02d",
            hours,
            minutes,
            seconds);
    else return String.format("%02d:%02d",
            minutes,
            seconds);

	}
	
	/**
	 * Method to get the sequences current in the format HH:MM:SS 
	 * @return
	 */
	public String getCurTime() {
	double totUS= (double)(gPlayer.getSequence(staccatoPattern).getMicrosecondLength());
	double cur=totUS*(double) gPlayer.getManagedPlayer().getTickPosition()/gPlayer.getManagedPlayer().getTickLength();
	Duration time = new Duration(cur/1000);
	
	int hours = (int) time.toHours();
    int minutes = (int) time.toMinutes();
    int seconds = (int) time.toSeconds();

    // Fix the issue with the timer going to 61 and above for seconds, minutes, and hours
    if (seconds > 59) seconds = seconds % 60;
    if (minutes > 59) minutes = minutes % 60;
    if (hours > 59) hours = hours % 60;

    // Don't show the hours unless the song is for an hour or longer
    if (hours > 0) 
    	return String.format("%d:%02d:%02d",
            hours,
            minutes,
            seconds);
    else return String.format("%02d:%02d",
            minutes,
            seconds);

	}
	
	/**
	 * Return instance of ManagedPlayer Object of player
	 * 
	 * @return
	 */
	public ManagedPlayer getManagedPlayer() {
		return this.gPlayer.getManagedPlayer();
	} 
	
//	ScorePartwise sp = converter.getScore().getModel(); PartList pl = sp.getPartList(); pl.getScoreParts().get(0).getPartName();

}
