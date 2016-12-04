package com.heima.song_db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.heima.song_openhelper.MySOpenHelper;

public class SongDao {
	private MySOpenHelper helper;

	public SongDao(Context context) {
		super();
		helper = new MySOpenHelper(context);
	}
	public long add(String name,String path){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("path", path);
		long result = db.insert("songs", null, values);
		db.close();
		return result;
	}
	public int delete(String name){
		SQLiteDatabase db = helper.getWritableDatabase();
		int result = db.delete("songs", "name=?",new String[]{name} );
		return result;
	}
	public int update(String name,String newpath){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("path", newpath);
		int result = db.update("songs", values, "name=?", new String[]{});
		return result;
	}
	public String find(String name){
		String path = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor =  db.query("songs", new String[]{"path"},"name=?", new String[]{name}, null, null, null);
		if (cursor.moveToNext()) {
			path = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return path;
	}
	public List<Song> select(){
		List<Song> list = new ArrayList<Song>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor =  db.query("songs", new String[]{"name","path"},null, null, null, null, null);
		while(cursor.moveToNext()){
			String name = cursor.getString(0);
			String path = cursor.getString(1);
			Song s = new Song();
			s.setName(name);
			s.setPath(path);
			list.add(s);
			
		}
		cursor.close();
		db.close();
		return list;
	}
}















