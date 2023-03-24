package se.liu.noaan869;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class HighscoreList {
	private final String url = "C:/Users/nooaa/Desktop/tetris.txt";
	private final String urlTemp = "C:/Users/nooaa/Desktop/TempTetris.txt";
	private static final HighscoreList HSList = new HighscoreList(); 
	private final List<Highscore> highSList;
	private ErrorHandeling errorHandel;
	public HighscoreList() {
		this.highSList = readDataFromHScoreFile();
		this.errorHandel = new ErrorHandeling(this);
	}
	public static HighscoreList getList() {
		return HSList;
	}
	public void add(Highscore score) {
		highSList.add(score);
		//need sort system.
		//follow instructions.
		sortList();
	}
	public void sortList() {
		highSList.sort(new ScoreComp());
		saveToFile();
	}
	public List<Highscore> readDataFromHScoreFile() {
		Gson gson = new Gson();
		List<Highscore> list;
		try {
			Type listType = new TypeToken<ArrayList<Highscore>>(){}.getType();
			FileReader r = new FileReader(url);
			list = gson.fromJson(r, listType);
			try {
				r.close();
			} catch (IOException e) {
				errorHandel.ErrorIOException(e);
			}
			
			if(list == null) {
				list = new ArrayList<>();
			}
	    } catch (FileNotFoundException e) {
	      
	       try {
	           File file = new File(url);
	           boolean createdFile = file.createNewFile();
			   errorHandel.ErrorCreateFile(createdFile);
	       } catch(IOException ee) {
			   errorHandel.ErrorIOException(ee);
	       }
	      list = new ArrayList<>();
	      
	    }
		return list;

	}
	
	public void saveToFile() {
	   Gson gson = new Gson();
		
	   try(FileWriter writer = new FileWriter(urlTemp)) {
	    	
	    	gson.toJson(highSList, writer);
	    	writer.close();
	    	renameAndRemove();
		   
       }
       catch (IOException e) {
    	   errorHandel.ErrorIOException(e);
       }
	}
	
	public void renameAndRemove() {
		File file = new File(url);

	    errorHandel.ErrorDeleteFile(file.delete());
		 
	
        File filee = new File(urlTemp);
  
        File rename = new File(url);
  
       
        boolean flag = filee.renameTo(rename);
  
		errorHandel.ErrorRenameFile(flag);

		
	}
	
	
	public String showList() {
		StringBuilder stringBuilder = new StringBuilder();

		highSList.forEach((element) -> {
			stringBuilder.append(element.getName());
		    stringBuilder.append(" has score: ");
		    stringBuilder.append(element.getScore());
		    stringBuilder.append("\n");
		
		});
		    
		

		return stringBuilder.toString();
	}
	
}
