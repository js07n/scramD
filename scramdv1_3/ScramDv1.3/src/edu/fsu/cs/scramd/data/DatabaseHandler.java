package edu.fsu.cs.scramd.data;
//********************************************************************************
//*
//* DatabaseHandler class
//* 	extends SQLiteOpenHelper
//*
//* Description:
//* 	Creates Database.
//*
//*	Needed:
//*		Schema might need to be changed and implemented to hold images,
//*			friends lists.
//********************************************************************************

import java.util.ArrayList;
import java.util.List;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHandler extends SQLiteOpenHelper{

	//All Static Variables
	//Database Version
	private static final int DATABASE_VERSION = 1;

	//Database Name
	private static final String DATABASE_NAME = "FriendList";
	
	//Friends Table Name
	private static final String TABLE_FRIENDS = "friends";
	
	//Friends Table Column names:
	private static final String KEY_USERNAME = "username";
	
	private static final String KEY_STATUS = "status";
	private static final String KEY_IMG = "imgId";

	public DatabaseHandler(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	//Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db){
		String CREATE_FRIEND_TABLE = "CREATE TABLE " + TABLE_FRIENDS + "("
				+ KEY_USERNAME + " TEXT PRIMARY KEY," 
				+ KEY_STATUS + " TEXT," + KEY_IMG + " BLOB" + ")";
		db.execSQL(CREATE_FRIEND_TABLE);
	}
	
	//Upgrading Database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		//Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
		
		//Create Tables Again
		onCreate(db);
	}
	
/**
* All CRUD(Create, Read, Update, Delete) Operations
**/
	
	//Adding new FRIEND
	public void addFriend(Friend friend){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, friend.getUsername());
		values.put(KEY_STATUS, friend.getStatus());
		values.put(KEY_IMG, friend.getIMG());
		
		//Inserting Row
		db.insert(TABLE_FRIENDS, null, values);
		db.close();
	}
	
	//Getting single friend
	public Friend getFriend(String un){
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_FRIENDS, new String[]{KEY_USERNAME, KEY_STATUS, KEY_IMG}, 
				KEY_USERNAME + "=?", new String[]{String.valueOf(un)}, null, null, null);
		if(cursor != null)
		{
			cursor.moveToFirst();
		}
		
		Friend friend = new Friend(cursor.getString(0), 
				cursor.getString(1), cursor.getBlob(2));
		
		return friend;
	}

	//Getting all friends
	public List<Friend> getAllFriends(){
		//Getting All Query
		List<Friend> friendList = new ArrayList<Friend>();
		
		//Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_FRIENDS;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		//Looping through all rows and adding to list.
		if(cursor.moveToFirst()){
			do{
				Friend friend = new Friend();
				
				friend.setUsername(cursor.getString(0));
				friend.setIMG(cursor.getBlob(2));
				//Adding contact to list
				friendList.add(friend);
			}while(cursor.moveToNext());
		}
		
		return friendList;
	}

	//Getting Friends count
	public int getFriendsCount(){
		   int count = 0;
		    String countQuery = "SELECT  * FROM " + TABLE_FRIENDS;

		    SQLiteDatabase db = this.getReadableDatabase();
		    Cursor cursor = db.rawQuery(countQuery, null);



		    if(cursor != null && !cursor.isClosed()){
		        count = cursor.getCount();
		        cursor.close();
		    }   
		    return count;
	}

	//Updating single Friend
	public int updateFriend(Friend friend){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, friend.getUsername());
		values.put(KEY_STATUS, friend.getStatus());
		values.put(KEY_IMG, friend.getIMG());
		
		//not using
		System.out.println(friend.getUsername() + " " + friend.getStatus());
		
		//Updating row
		return db.update(TABLE_FRIENDS, values, KEY_USERNAME + " =?", new String[]{String.valueOf(friend.getUsername())} );
	}

	//Delete single friend
	public void deleteFriend(Friend friend){
		SQLiteDatabase db = this.getWritableDatabase();
		
		//not using
		System.out.println(friend.getUsername() + " " + friend.getUsername());
		
		db.delete(TABLE_FRIENDS, KEY_USERNAME + " = ?", new String[]{String.valueOf(friend.getUsername())});
		db.close();
	}


}
