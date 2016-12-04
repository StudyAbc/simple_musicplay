package com.heima.musicplay_v3;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.heima.musicofsound.R;
import com.heima.song_db.dao.SongDao;

public class ForsongActivity extends Activity {
	private EditText et_username;
	private Button bt_save;
	private String username;
	//private ArrayList<String> list;
	private String songpath;
	private SongDao sdao;
	@SuppressLint("CommitPrefEdits") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forsong_activity);
		et_username = (EditText) findViewById(R.id.et_username);
		bt_save = (Button) findViewById(R.id.bt_save);
		//list = new ArrayList<String>();
		sdao = new SongDao(this);
		songpath = new File(Environment.getExternalStorageDirectory(),"").toString();
		bt_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				username = et_username.getText().toString().trim();
				SharedPreferences sp = getSharedPreferences("username", 0);
				Editor ed = sp.edit();
				ed.putString("username", username);
				ed.commit();
				
				new Thread(){
					public void run() {
						
						getSongPath(songpath);
						//Toast.makeText(ForsongActivity.this, "遍历成功", Toast.LENGTH_LONG).show();
					};
				}.start();
				Toast.makeText(ForsongActivity.this, "用户名保存成功", Toast.LENGTH_LONG).show();
			}
		});
	}
	public void getSongPath(String s){
		File f = new File(s);
		File[] files = f.listFiles();
		if (files != null) {
			for (File file : files) {
				String songname = file.toString().substring(file.toString().lastIndexOf("/")+1);
				if (file.isFile()&&songname.endsWith(".mp3")) {
					String path = sdao.find(songname);
					if (!file.toString().equals(path)&&file.length()>1024*1024*2) {
						sdao.add(songname, file.toString());
						System.out.println(songname + path);
					}
				}else if (file.isDirectory()) {
					getSongPath(file.toString());
				}
			}
		}
	}
}
