package com.heima.musicplay_v3;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.heima.musicofsound.R;
import com.heima.song_db.dao.Song;
import com.heima.song_db.dao.SongDao;

public class MusicActivity extends Activity {
	private Button bt_dz;
	private TextView username;
	private TextView songname;
	private ListView lv;
	//数据库工具类
	private SongDao sdao;
	private boolean b = true;
	private Myconn conn;
	private IMusicSerivce ISerivce;
	private List<Song> list;
	private String[] sName;
	private String[] sPath;
	//当前播放歌曲索引+1
	private int index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_music);
		/**
		 * 初始控件和需要的值
		 */
		bt_dz = (Button) findViewById(R.id.bt_dz);
		username = (TextView) findViewById(R.id.username);
		songname = (TextView) findViewById(R.id.songname);
		lv = (ListView) findViewById(R.id.lv);
		sdao = new SongDao(this);
		list = sdao.select();
		sName = new String[list.size()];
		sPath = new String[list.size()];
		getNameAndPath();
		// 初始化页面的数据
		if (list != null) {
			SharedPreferences sp = getSharedPreferences("username", 0);
			String usern = sp.getString("username", "未设置");
			username.setText(usern);
			lv.setAdapter(new ArrayAdapter<String>(this, R.layout.item, sName));
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					ISerivce.callMethodInService(sPath[position], 1);
					songname.setText(sName[position]);
					index = position + 1;
					if (index >= list.size() - 1) {
						index = 0;
					}
				}
			});

		}

		// 创建服务
		Intent intent = new Intent(this, MusicService.class);
		startService(intent);
		// 点击初始化
		bt_dz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MusicActivity.this,
						ForsongActivity.class);
				startActivity(intent);
			}
		});
	}

	public void getNameAndPath() {
		for (int i = 0; i < list.size(); i++) {
			if (list == null) {
				return;
			}
			sName[i] = list.get(i).getName();
			sPath[i] = list.get(i).getPath();
		}
	}

	private class Myconn implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ISerivce = (IMusicSerivce) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	}

	public void play(View view) {
		if (b) {
			if (index!=0) {
				return;
			}
			ISerivce.callMethodInService(sPath[index], 2);
			songname.setText(sName[index]);
			index++;
			if (index >= list.size() - 1) {
				index = 0;
			}
			b = false;
		} else {
			ISerivce.callMethodInService(sPath[index - 1], 2);
			songname.setText(sName[index - 1]);
		}
	}

	public void next(View view) {
		ISerivce.callMethodInService(sPath[index], 3);
		songname.setText(sName[index]);
		index++;
		if (index >= list.size() - 1) {
			index = 0;
		}
	}

	public void pause(View view) {
		ISerivce.callMethodInService(sPath[index], 4);
	}

	public void stop(View view) {
		ISerivce.callMethodInService(sPath[index], 5);
	}

	@Override
	protected void onStart() {
		// 初始化页面的数据
		if (list != null) {
			SharedPreferences sp = getSharedPreferences("username", 0);
			String usern = sp.getString("username", "未设置");
			username.setText(usern);
		}
		System.out.println("尝试开启绑定");
		Intent intent = new Intent(this, MusicService.class);
		conn = new Myconn();
		bindService(intent, conn, BIND_DEBUG_UNBIND);
		super.onStart();
	}

	@Override
	protected void onStop() {
		unbindService(conn);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Intent intent = new Intent(this, MusicService.class);
		stopService(intent);
		super.onDestroy();
	}

}
