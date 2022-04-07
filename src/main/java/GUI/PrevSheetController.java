package GUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import converter.Score;
import converter.measure.TabMeasure;
import custom_exceptions.TXMLException;
import draw.score.DrawDrumNotes;
import draw.score.DrawGuitarNotes;
import draw.score.DrumNotesList;
import draw.score.DrumStaff;
import draw.score.GuitarNotesList;
import draw.score.GuitarStaff;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;


import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;


import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.scene.paint.Color;
import models.Part;

import models.measure.note.Note;

import models.measure.note.Notehead;
import models.part_list.PartList;
import models.part_list.ScorePart;
import utility.Range;
import utility.XmlPlayer;


public class PrevSheetController extends Application {
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
	private float time, temp;
	public boolean playing;
	private Timer timer;
	private TimerTask task;
	@FXML
	TextField gotoMeasureField;
	@FXML
	Button playMusic;
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

	@FXML
	public CodeArea mxlTextPre;
	@FXML
	ScrollPane scrollPane;


	// public VBox myVBox;

	public String clef;


	private String instrumentName;
	DrumNotesList drumNotesList = new DrumNotesList();
	GuitarNotesList guitarNotesList = new GuitarNotesList();
	DrawDrumNotes drawDrumNotes = new DrawDrumNotes();
	DrawGuitarNotes drawGuitarNotes = new DrawGuitarNotes();
	@FXML



	public void initialize() {
		// mxlTextPre.setParagraphGraphicFactory(LineNumberFactory.get(mxlTextPre));
		Button button1 = new Button("button1");

		
//		Initialize player labels and sliders
		songPB.setMinWidth(songSlider.getMaxWidth());
		songPB.setMaxWidth(songSlider.getMaxWidth());
		songSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasChanging,
					Boolean isChanging) {
				time = (float) songSlider.getValue() / 100.0f;
				if (!isChanging) {

					mp.seek(time);
				}
			}
		});
		songSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			time = newValue.floatValue() / 100.0f;
			songPB.setProgress(newValue.doubleValue() / 100);

			if (mp.getManagedPlayer().isStarted()) {
				labelTimeCur.setText(mp.getCurTime());
				labelTimeEnd.setText(mp.getDuration());

				long cur = mp.getManagedPlayer().getTickPosition();
				long dur = mp.getManagedPlayer().getTickLength();
				if (Math.abs(newValue.doubleValue() - (oldValue.doubleValue())) > 1.4) {
//					mp.seek(time);
				}
			}
		});

		tempSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			temp = newValue.floatValue();
			
				if (mp.getManagedPlayer()!=null && !mp.getManagedPlayer().isFinished()) {

					try {

						labelTimeCur.setText(mp.getCurTime());
						labelTimeEnd.setText(mp.getDuration());
						mp.setTempo(temp);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			
		});






		mvc = mvcInput;


	}



	@Override

	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
//
//		Scene scene = new Scene(scrollPane, 800, 600, Color.AZURE);
//
//		primaryStage.setTitle("music sheet");
//
//		primaryStage.setScene(scene);
//
//		primaryStage.show();
	}


	public void setMainViewController(MainViewController mvcInput) throws Exception{

		mvc = mvcInput;
		

//		Draw sheet music on scroll pane
		Score score = mvc.converter.getScore();
		String instrumentName = getInstrumentName(score);

		Text instrumentNameTitle = new Text(450, 100, instrumentName);
		instrumentNameTitle.setFont(Font.font("Verdana", 50));
		Group box = new Group();
    Group drawing=new Group();
		box.getChildren().add(instrumentNameTitle);
		drawing.getChildren().add(instrumentNameTitle);
		// Draw Staff
		if (instrumentName.contains("Drum")) {
			DrumStaff drumStaff = new DrumStaff();
			drumStaff.draw(drawing, 0);
			int[][] notesPositionList = drumNotesList.notesList(mvc);

			//hashmap of drum notes map
			HashMap <Integer, List<Note>>drumNotesMap = drumNotesList.getDrumNotesMap();
			//draw with 2D array
			//drawDrumNotes.draw(mvc,box, notesPositionList);
			drawDrumNotes.drawEverything(drumNotesMap,mvc);
			drawing=drawDrumNotes.getDrawing();

			
		} else if (instrumentName.contains("Guitar")) {
			GuitarStaff guitarStaff = new GuitarStaff();
			guitarStaff.draw(drawing, 0);
			int[][] notesPositionList = guitarNotesList.notesList(mvc);

			drawGuitarNotes.draw(mvc,drawing, notesPositionList);

		}


		scrollPane.setContent(box);
		scrollPane.setPannable(true);
		
//		Player initialization
		xmlstr = mvc.converter.getMusicXML();
		playing =false;
		mp = new XmlPlayer(mvc, xmlstr);
		labelTimeCur.setText("00:00");

	}





	public String getInstrumentName(Score score) throws TXMLException {


		String instrumentName = score.getModel().getPartList().getScoreParts().get(0).getPartName();
		return instrumentName;
	}




	@FXML
	private void playMusicHandle() throws Exception {
		playing=true;
		mvc.converter.update();
		beginTimer();
		mp.play();
		
	}

	public void beginTimer() {

		timer = new Timer();
		task = new TimerTask() {
			public void run() {
				if (mp.getManagedPlayer().isStarted()) {
					long cur = mp.getManagedPlayer().getTickPosition();
					long dur = mp.getManagedPlayer().getTickLength();
//				System.out.println(cur+" <-pos len-> "+dur);
//				labelTimeCur.setText(mp.getCurTime());
					if (dur != 0 && (cur / dur == 1)) {
						cancelTimer();
					}
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							labelTimeCur.setText(mp.getCurTime());
							labelTimeEnd.setText(mp.getDuration());
							if (dur != 0) {
								songSlider.setValue(((double) cur / (double) dur) * 100);
							}
						}
					});
				}
			}

		};
		timer.scheduleAtFixedRate(task, 1000, 1000);
	}
	
	public void playMusicHandleTest() throws Exception {

		mvc.converter.update();
		mp.play();
		beginTimer();
		
	}
	
	public void cancelTimer() {
		timer.cancel();
	}
	
	public boolean getPlaying() {
		return playing;
	}

	@FXML
	private void pauseMusicHandle() throws InterruptedException {
		playing=false;
		mp.pause();
		cancelTimer();
	}
	
	public boolean isPlaying() {
		return this.playing;
	}


	public void setBPM(double bpm) {
		tempSlider.setValue(bpm);
	}
	@FXML
	private void handleGotoMeasure() {
		int measureNumber = Integer.parseInt(gotoMeasureField.getText());
//		if (!goToMeasure(measureNumber)) {
//			Alert alert = new Alert(Alert.AlertType.ERROR);
//			alert.setContentText("Measure " + measureNumber + " could not be found.");
//			alert.setHeaderText(null);
//			alert.show();
//		}
	}

	private boolean goToMeasure(int measureCount) {
//		TabMeasure measure = converter.getScore().getMeasure(measureCount);
//		if (measure == null)
//			return false;
//		List<Range> linePositions = measure.getRanges();
//		int pos = linePositions.get(0).getStart();
//		mainText.moveTo(pos);
//		mainText.requestFollowCaret();
//		mainText.requestFocus();
		return true;
	}

	@FXML
	public void exit() {
		playing=false;
		mp.getManagedPlayer().finish();
		mvc.convertWindow.hide();
		cancelTimer();
	}


}
