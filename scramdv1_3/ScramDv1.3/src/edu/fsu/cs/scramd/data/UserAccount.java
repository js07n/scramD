package edu.fsu.cs.scramd.data;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("UserAccount")
public class UserAccount extends ParseObject {

	public UserAccount(){
		//Default Constructor
	}
	
	//SETTERS AND GETTERS
	
	public void setSendTo(ParseUser user){
		put("someDude" , user);
	}
	
	public ParseUser getSendTo(){
		return getParseUser("someDude");
	}
	
	public void setStatus(String status){
		put("datDamStatus", status);
	}
	
	public String getStatus(){
		return getString("datDamStatus");
	}
	
	public void setPhotoFile(ParseFile file){
		put("photo", file);
	}
	
	public ParseFile getPhotoFile(){
		return getParseFile("photo");
	}
}
