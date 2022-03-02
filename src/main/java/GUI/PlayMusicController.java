package GUI;

import java.util.Timer;
import java.util.TimerTask;


import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Window;

import utility.XmlPlayer;

public class PlayMusicController extends Application {
	/**
	 * MainViewController object to store parent mvc instance
	 */
	private MainViewController mvc;
	/**
	 * XmlPlayer object to use xmlplayer functionality 
	 */
	private XmlPlayer mp;
	/**
	 * String to store xml output as string from main view
	 */
	private String xmlstr;
	private static Window convertWindow = new Stage();
	private float time,temp;
	private boolean playing;
	private Timer timer;
	private TimerTask task;
	@FXML
	Button playButton;
	@FXML
	Button pauseButton;
	@FXML
	Slider songSlider;
	@FXML
	Slider tempSlider;
	@FXML
	Label tempLabelL;
	@FXML
	Label tempLabelH;
	@FXML 
	Label labelTimeEnd;
	@FXML 
	Label labelTimeCur;
	@FXML 
	ProgressBar songPB;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * Creates mvc instance using main view mvc and takes the converted 
	 * xml string from main view to play score
	 * 
	 * @param mvcInput
	 * @param str
	 * @throws Exception
	 */
	public void setMainViewController(MainViewController mvcInput, String str) throws Exception {
		mvc = mvcInput;
		xmlstr = str;
//		System.out.println(mvc.converter.getScore().getModel().getPartList().getScoreParts().get(0).getPartName());
		mp = new XmlPlayer(xmlstr, mvc.converter.getScore().getModel().getPartList().getScoreParts().get(0).getPartName());
		labelTimeCur.setText("00:00");
		
	}
//	converter.getScore().getModel(); PartList pl = sp.getPartList(); pl.getScoreParts().get(0).getPartName();
	
	@FXML
	public void initialize() throws Exception {
//		Image vol1 = new Image(getClass().getClassLoader().getResource("image_assets/Low-Volume-icon.png").toString());
//		ImageView vol1v = new ImageView(vol1);
//		volLabel.setGraphic(vol1v);

		songPB.setMinWidth(songSlider.getMaxWidth());
		songPB.setMaxWidth(songSlider.getMaxWidth());
		songSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
	            @Override
	            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasChanging, Boolean isChanging) {
	            	time = (float)songSlider.getValue()/100.0f;
	                if (!isChanging) {
	                   
	                   mp.seek(time);
	                }
	            }
	        });
		songSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			time = newValue.floatValue()/100.0f;
			songPB.setProgress(newValue.doubleValue()/100);
			
			if (mp.getManagedPlayer().isStarted()){
				labelTimeCur.setText(mp.getCurTime());
				labelTimeEnd.setText(mp.getDuration());
//				System.out.println(mp.getManagedPlayer());
				long cur = mp.getManagedPlayer().getTickPosition();
				long dur = mp.getManagedPlayer().getTickLength();
				 if (Math.abs(cur - (time*dur)) > 0.5) {
//					 mp.seek(time);
	                }
				
//				System.out.println(mp.getManagedPlayer().getTickPosition()/(double)mp.getManagedPlayer().getTickLength()*100.0);
//				songSlider.setValue(mp.getManagedPlayer().getTickPosition()/(double)mp.getManagedPlayer().getTickLength()*100.0);
			}
		});
		
		
		
		tempSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			temp = newValue.floatValue();
			
			if (mp.getManagedPlayer().isStarted()) {

				try {
					mp.setTempo(temp);
					labelTimeCur.setText(mp.getCurTime());
					labelTimeEnd.setText(mp.getDuration());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(mp.getTempo());
//				System.out.println(mp.getManagedPlayer().getTickPosition()/(double)mp.getManagedPlayer().getTickLength()*100.0);
//				songSlider.setValue(mp.getManagedPlayer().getTickPosition()/(double)mp.getManagedPlayer().getTickLength()*100.0);
				
			}
		});
		
	}

	@FXML
	private void playMusicHandle() throws Exception {

		mvc.converter.update();
		mp.play();
		beginTimer();
		
//		while(mp.getManagedPlayer().isPlaying()) {
//			songSlider.setValue(mp.getManagedPlayer().getTickPosition()/(double)mp.getManagedPlayer().getTickLength()*100.0);
//		}

	}
	
	public void beginTimer() {
		playing = mp.getManagedPlayer().isPlaying();
		
		timer = new Timer();
		task = new TimerTask() {
			public void run() {
				long cur = mp.getManagedPlayer().getTickPosition();
				long dur = mp.getManagedPlayer().getTickLength();
//				labelTimeCur.setText(mp.getCurTime());
				if (cur/dur==1) {
					cancelTimer();
				}
				Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		            	labelTimeCur.setText(mp.getCurTime());
		            	labelTimeEnd.setText(mp.getDuration());
		            	songSlider.setValue(((double)cur/(double)dur)*100);
		            }
		        });
				
			}
		};
		timer.scheduleAtFixedRate(task, 1000, 1000);
	}
	
	public void cancelTimer() {
		playing = false;
		timer.cancel();
	}

	@FXML
	private void pauseMusicHandle() {
		mp.pause();
		timer.cancel();
	}

	@FXML
	private void exit() {
		mp.getManagedPlayer().finish();
		mvc.convertWindow.hide();
		timer.cancel();
	}
}
