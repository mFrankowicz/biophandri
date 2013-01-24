package data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.File;


public class Resource {

	static public FileInputStream getStream(String path){
		File f = new File(path);
		FileInputStream is = null;
		try {
			is = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;
	}
	
	static public String getPath(String path){
		return path;
	}
}
