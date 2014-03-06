package edu.fsu.cs.scramd.data;
//THIS WILL REPLACE Account.java!!!


import java.sql.Blob;

public class Friend {
	//Private variables:
	String username;
	String status;
	byte[] accIMG;

	//Empty Constructor
	public Friend(){
	}
	
	public Friend(String un)
	{
		this.username = un;
		this.status = "fight";
		this.accIMG = null;
	}
	
	public Friend(String un, String s, byte[] IMG)
	{
		this.username = un;
		this.status = s;
		this.accIMG = IMG;
	}
	
	
	//Get USERNAME
	public String getUsername(){
		return this.username;
	}	
	
	
	//Get Status
	public String getStatus(){
		return this.status;
	}
	
	
	//Get IMG
	public byte[] getIMG(){
		return this.accIMG;
	}
	
	
	//Set USERNAME
	public void setUsername(String un){
		this.username = un;
	}
	
	//Set Status
	public void setStatus(String s){
		this.status = s;
	}
	
	//Set IMG
	public void setIMG(byte[] b){
		this.accIMG = b;
	}
	
}
