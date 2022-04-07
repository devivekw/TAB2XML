package draw.score;


import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;

import GUI.MainViewController;
import converter.Score;
import custom_exceptions.TXMLException;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.Part;

import models.measure.note.Note;

public class DrawDrumNotes {
	private Group group = new Group();

	public DrawDrumNotes() {
		super();
		// TODO Auto-generated constructor stub

	}
	

	//draw based on 2D array, to be deleted

	public void draw(MainViewController mvc,Group box, int[][] notesPositionList) throws TXMLException {
		int noteHeightDrum = 0;
		// get clef	
		Score score1 = mvc.converter.getScore();
		List<Part> partList = score1.getModel().getParts();	
		//System.out.println("score counts: "+score1.getModel().getScoreCount());		
		String clef = partList.get(0).getMeasures().get(0).getAttributes().clef.sign;

		Line line1 = new Line(105, 220, 105, 240);
		Line line2 = new Line(110, 220, 110, 240);
		line1.setStrokeWidth(4);
		line2.setStrokeWidth(4);
		box.getChildren().add(line2);
		box.getChildren().add(line1);

		System.out.printf("drum note span size" + String.valueOf(notesPositionList.length));
		for (int i = 0; i < notesPositionList.length; i++) {
//			for(int ii=0;i<i%30;ii++) {

			int dividend = i, divisor = 30;
			int height = dividend / divisor;
			int horizontalSpan = i % 30;
			System.out.println("i/30 " + height);
			System.out.println("i%30 " + horizontalSpan);
			DrumStaff drumStaff = new DrumStaff();
			drumStaff.draw(box, height);
			for (int j = 0; j < notesPositionList[i].length; j++) {
				noteHeightDrum = notesPositionList[i][j];
				if (noteHeightDrum < 20) {
					Line noteLine = new Line(135 + 25.0 * horizontalSpan, 200 - 5.0 * noteHeightDrum + 90 * height,
							135 + 25.0 * horizontalSpan, 250 - 5.0 * noteHeightDrum + 90 * height);
					Circle note = new Circle(130 + 25.0 * horizontalSpan,
							250.0 - 5.0 * noteHeightDrum + 90 * height, 5);
					note.setFill(Color.MIDNIGHTBLUE);

					box.getChildren().add(noteLine);
					box.getChildren().add(note);
				}

			}
//			}

		}

	

	}
	

	public void drawNote(int horizontalPosition, int stepOctave, int rowIndex) {
		Line noteLine = new Line(135 + 25.0 * horizontalPosition, 
				210 - 5.0 * stepOctave + 90 * rowIndex,
				135 + 25.0 * horizontalPosition, 
				250 - 5.0 * stepOctave + 90 * rowIndex);
		Circle note = new Circle(130 + 25.0 * horizontalPosition,
				250.0 - 5.0 * stepOctave + 90 * rowIndex, 5);
		note.setFill(Color.MIDNIGHTBLUE);
		group.getChildren().add(noteLine);
		group.getChildren().add(note);
		
	}
	
	
	
	
	public void drawNoteHead(int horizontalPosition, int stepOctave, int rowIndex) {
		Line cross1= new Line(130 + 25.0 * horizontalPosition-5,
				250.0 - 5.0 * stepOctave + 90 * rowIndex-2.5,
				130 + 25.0 * horizontalPosition+5,
				250.0 - 5.0 * stepOctave + 90 * rowIndex+2.5);
		Line cross2 = new Line(130 + 25.0 * horizontalPosition-5,
				250.0 - 5.0 * stepOctave + 90 * rowIndex+2.5,
				130 + 25.0 * horizontalPosition+5,
				250.0 - 5.0 * stepOctave + 90 * rowIndex-2.5);
		Line line = new Line(135 + 25.0 * horizontalPosition, 
							210 - 5.0 * stepOctave + 90 * rowIndex,
							135 + 25.0 * horizontalPosition, 
							250 - 5.0 * stepOctave + 90 * rowIndex);
		group.getChildren().add(cross1);
		group.getChildren().add(cross2);
		group.getChildren().add(line);
		
	}
	
	public void drawDuration4(int horizontalPosition, int stepOctave, int rowIndex) {
		Line line1 = new Line(135 + 25.0 * horizontalPosition, 
				210 - 5.0 * stepOctave + 90 * rowIndex,
				135 + 25.0 * horizontalPosition+10, 
				210 - 5.0 * stepOctave + 90 * rowIndex);
		line1.setStroke(Color.RED);
		Line line2 = new Line(135 + 25.0 * horizontalPosition, 
				210 - 5.0 * stepOctave + 90 * rowIndex+3,
				135 + 25.0 * horizontalPosition+10, 
				210 - 5.0 * stepOctave + 90 * rowIndex+3);
		line2.setStroke(Color.RED);
		group.getChildren().add(line1);
		group.getChildren().add(line2);
		

	}
	
	public void drawDuration8(int horizontalPosition, int stepOctave, int rowIndex) {
		Line line1 = new Line(135 + 25.0 * horizontalPosition, 
				211 - 5.0 * stepOctave + 90 * rowIndex,
				135 + 25.0 * horizontalPosition+10, 
				211 - 5.0 * stepOctave + 90 * rowIndex);
		line1.setStroke(Color.RED);

		group.getChildren().add(line1);

		
	}
	public void drawMeasures(int horizontalPosition,int rowIndex) {
		Line line = new Line(120,210+90*rowIndex,120,250+90*rowIndex);
		line.setStrokeWidth(3);
		group.getChildren().add(line);
	}
	public void drawEverything(HashMap <Integer, List<Note>>drumNotesMap,MainViewController mvc ) throws TXMLException {
		//Loop through drum notes hashmap
		Score score1 = mvc.converter.getScore();
		List<Part> partList = score1.getModel().getParts();	
		//System.out.println("score counts: "+score1.getModel().getScoreCount());		
		String clef = partList.get(0).getMeasures().get(0).getAttributes().clef.sign;

		Line clef1 = new Line(105, 220, 105, 240);
		Line clef2 = new Line(110, 220, 110, 240);
		clef1.setStrokeWidth(4);
		clef2.setStrokeWidth(4);
		group.getChildren().add(clef1);
		group.getChildren().add(clef2);
		for(HashMap.Entry<Integer,List<Note>> entry: drumNotesMap.entrySet()) {
			int keyValue=entry.getKey();
			System.out.printf("hashmap key: %d",keyValue);
			//20 notes each row
			int dividend = keyValue, divisor = 20;
			int rowIndex = dividend / divisor;
			int horizontalPosition = keyValue % 20;
			DrumStaff drumStaff = new DrumStaff();
			drumStaff.draw(group, rowIndex);
			drawMeasures(1,rowIndex);
			//get display-step
			String step =entry.getValue().get(0).getUnpitched().getDisplayStep();
			//get display-octave
			int octaveInt = entry.getValue().get(0).getUnpitched().getDisplayOctave();
			String octave = String.valueOf(octaveInt);
			//combine step and octave to use noteToNumber to get position on staff
			String stepWithOctave = step+octave;
			int stepOctave = noteToNumber(stepWithOctave);
		
			
			if (entry.getValue().get(0).getDuration()==null) {}
			else {int duration=entry.getValue().get(0).getDuration();
	
			if(duration==4) {  drawDuration4( horizontalPosition, stepOctave, rowIndex);}else if(duration==8) {
				drawDuration8( horizontalPosition, stepOctave, rowIndex);
			}}
				
			List<Note> notesIncludeChord = entry.getValue();
			for (Note note: notesIncludeChord) {
				/*
				 * if(note.getNotehead()==null) {
				 * System.out.printf("note head is null %d",keyValue); }else {
				 * System.out.printf("note head is not null %d",keyValue); }
				 */
				
				//get display-step
				step =note.getUnpitched().getDisplayStep();
				//get display-octave
			 octaveInt = note.getUnpitched().getDisplayOctave();
				 octave = String.valueOf(octaveInt);
				//combine step and octave to use noteToNumber to get position on staff
				stepWithOctave = step+octave;
				stepOctave = noteToNumber(stepWithOctave);
				System.out.printf("hashmap %s is %d",stepWithOctave,stepOctave);
				String type = note.getType();
				  if(note.getNotehead()==null) { 
					  drawNote(horizontalPosition,stepOctave,rowIndex);
					  } 
				  else {
					  drawNoteHead(horizontalPosition,stepOctave,rowIndex); }
				  
				
				 
			}
			
		}
	}
	

	public int noteToNumber (String noteWithOctive) {
    	// noteNumber indicates the location of the note on staff
    	System.out.print(noteWithOctive);

    	int noteNumber=0;
    	//noteNumber set as 0 means E4 so it sits on the bottom line of 5 staff lines
    	if (noteWithOctive.matches("C4")){   	
    		
    		noteNumber=-2;	  		
    	}
    	if (noteWithOctive.matches("D4")){
    		noteNumber=-1;	
    	}
    	if (noteWithOctive.matches("E4")){
    		noteNumber=0;	
    	}
    	if (noteWithOctive.matches("F4")){
    		noteNumber=1;	
    	}
    	if (noteWithOctive.matches("G4")){
    		noteNumber=2;	
    	}
    	if (noteWithOctive.matches("A4")){
    		noteNumber=3;	
    	}
    	if (noteWithOctive.matches("B4")){
    		noteNumber=4;	
    	}
    	if (noteWithOctive.matches("C5")){
    		noteNumber=5;	
    	}
    	if (noteWithOctive.matches("D5")){
    		noteNumber=6;	
    	}
    	if (noteWithOctive.matches("E5")){
    		noteNumber=7;	
    	}
    	if (noteWithOctive.matches("F5")){
    		noteNumber=8;	
    	}
    	if (noteWithOctive.matches("G5")){
    		noteNumber=9;	
    	}
    	if (noteWithOctive.matches("A5")){
    		noteNumber=10;	
    	}
    	return noteNumber;
    }

	public Group getDrawing() {
		return this.group;
	}
	

}
