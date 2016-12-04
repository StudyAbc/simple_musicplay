package com.heima.musicplay_v3;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {
	private MediaPlayer mp;
	private boolean b = true;
	
	private class MyBinder extends Binder implements IMusicSerivce {
		public void callMethodInService(String path, int x) {
			switch (x) {
			case 1:// �������
				play(path, x);
				break;

			case 2:// ��ͣ����
				play(path, x);
				break;
			case 3:// ��һ��
				play(path, x);
				break;

			case 4:// ��ͣ
				pause();
				break;

			case 5:// ֹͣ
				stop();
				break;
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("�����˱�����");
		return new MyBinder();
	}

	@Override
	public void onCreate() {
		mp = new MediaPlayer();
		System.out.println("����Ͳ�������������");
		super.onCreate();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		System.out.println("��������");
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		System.out.println("���°�");
		super.onRebind(intent);
	}

	@Override
	public void onDestroy() {
		System.out.println("����������");
		super.onDestroy();
	}

	public void play(String path, int x) {
		// 1.������� 2.��ͣ���� 3.��һ��
		switch (x) {
		case 1:

			if (mp != null && mp.isPlaying()) {
				try {
					mp.stop();
					mp.reset();
					mp.setDataSource(path);
					mp.setLooping(true);
					mp.prepare();
					mp.start();
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (mp != null && !mp.isPlaying()) {
				try {
					mp.reset();
					mp.setDataSource(path);
					mp.setLooping(true);
					mp.prepare();
					mp.start();
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else {
				return;
			}
			break;

		case 2:
			if (b) {
				try {
					mp.reset();
					mp.setDataSource(path);
					mp.setLooping(true);
					mp.prepare();
					mp.start();
				} catch (Exception e) {
					// TODO: handle exception
				}
				b = false;
			}
			if (mp != null && mp.isPlaying()) {
				return;
			} else if (mp != null && !mp.isPlaying()) {
					mp.start();

			} else {
				return;
			}
			break;
		case 3:
			if (mp != null && mp.isPlaying()) {
				try {
					mp.stop();
					mp.reset();
					mp.setDataSource(path);
					mp.setLooping(true);
					mp.prepare();
					mp.start();
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (mp != null && !mp.isPlaying()) {
				try {
					mp.reset();
					mp.setDataSource(path);
					mp.setLooping(true);
					mp.prepare();
					mp.start();
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else {
				return;
			}

			break;
		}
		if (mp != null && !mp.isPlaying()) {
			mp.start();
		} else {
			try {
				mp.setDataSource(path);
				mp.setLooping(false);
				mp.prepare();
				mp = new MediaPlayer();
				mp.setDataSource(path);
				mp.setLooping(false);
				mp.prepare();
				mp.start();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	public void pause() {
		if (mp != null && mp.isPlaying()) {
			mp.pause();
		} else {
			return;
		}
	}

	public void stop() {
		try {
			if (mp != null && mp.isPlaying()) {
				mp.stop();
			} else {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
