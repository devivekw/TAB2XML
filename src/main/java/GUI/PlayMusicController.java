package GUI;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
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
	Label timeLabel;

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
		timeLabel.setText(String.valueOf(0));
	}
//	converter.getScore().getModel(); PartList pl = sp.getPartList(); pl.getScoreParts().get(0).getPartName();
	
	@FXML
	public void initialize() throws Exception {
//		Image vol1 = new Image(getClass().getClassLoader().getResource("image_assets/Low-Volume-icon.png").toString());
//		ImageView vol1v = new ImageView(vol1);
//		volLabel.setGraphic(vol1v);
		
		songSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			time = newValue.floatValue()/100.0f;
			
			if (mp.getManagedPlayer().isStarted()) {
				timeLabel.setText(String.valueOf((long)(time*mp.getManagedPlayer().getTickLength())));
				System.out.println(mp.getManagedPlayer());
				mp.seek(time);
//				System.out.println(mp.getManagedPlayer().getTickPosition()/(double)mp.getManagedPlayer().getTickLength()*100.0);
//				songSlider.setValue(mp.getManagedPlayer().getTickPosition()/(double)mp.getManagedPlayer().getTickLength()*100.0);
			}
		});
		
		tempSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			temp = newValue.floatValue();
			
			if (mp.getManagedPlayer().isStarted()) {

				try {
					mp.setTempo(temp);
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
//		while(mp.getManagedPlayer().isPlaying()) {
//			songSlider.setValue(mp.getManagedPlayer().getTickPosition()/(double)mp.getManagedPlayer().getTickLength()*100.0);
//		}

	}

	@FXML
	private void pauseMusicHandle() {
		mp.pause();
	}

	@FXML
	private void exit() {
		mp.getManagedPlayer().finish();
		mvc.convertWindow.hide();
	}
}
