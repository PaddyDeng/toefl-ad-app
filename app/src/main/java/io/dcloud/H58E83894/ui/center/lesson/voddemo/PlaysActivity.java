package io.dcloud.H58E83894.ui.center.lesson.voddemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.gensee.doc.OnDocViewEventListener;
import com.gensee.entity.ChatMsg;
import com.gensee.entity.DocInfo;
import com.gensee.entity.PageInfo;
import com.gensee.media.PlaySpeed;
import com.gensee.media.VODPlayer;
import com.gensee.media.VODPlayer.OnVodPlayListener;
import com.gensee.pdu.DocViewImpl;
import com.gensee.pdu.IGSDocView;

import com.gensee.utils.GenseeLog;
import com.gensee.utils.StringUtil;
import com.gensee.view.GSDocViewGx;
import com.gensee.view.GSVideoView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import butterknife.OnTouch;
import io.dcloud.H58E83894.MainActivity;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;

public class PlaysActivity extends BaseActivity implements OnDocViewEventListener,
        OnVodPlayListener, OnSeekBarChangeListener {

	private static final String TAG = "PlayActivity";
	private VODPlayer mVodPlayer;
	private GSVideoView mGSVideoView;
	//private GSDocView mDocView;
	private GSDocViewGx mGlDocView;
	private SeekBar mSeekBarPlayViedo;
	private Button btnDocList;
	private ListView lvChapterList;
	private ChapterListAdapter chapterListAdapter;

	private Button stopVeidoPlay;
	private Button replyVedioPlay;
	private TextView mNowTimeTextview;
	private TextView mAllTimeTextView;

	private ImageButton mPauseScreenplay;
	private boolean isTouch = false;

	private static final int DURITME = 2000;
	private static final String DURATION = "DURATION";

	private int VIEDOPAUSEPALY = 0;
	private int speedItem = 0;
	private LinearLayout linear_linear;
	private RelativeLayout linear_linerae_01;
	MyHandler mHandler= new MyHandler(PlaysActivity.this);

	CountTimeThread countTimeThread;

	private List<ChapterInfo> chapterList;

	private int lastPostion = 0;
	interface MSG {
		int MSG_ON_INIT = 1;
		int MSG_ON_STOP = 2;
		int MSG_ON_POSITION = 3;
		int MSG_ON_VIDEOSIZE = 4;
		int MSG_ON_PAGE = 5;
		int MSG_ON_SEEK = 6;
		int MSG_ON_AUDIOLEVEL = 7;
		int MSG_ON_ERROR = 8;
		int MSG_ON_PAUSE = 9;
		int MSG_ON_RESUME = 10;
	}

	protected Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG.MSG_ON_INIT:

				int max = msg.getData().getInt(DURATION);
				mSeekBarPlayViedo.setMax(max);
				max = max / 1000;
				GenseeLog.i(TAG, "MSG_ON_INIT duration = " + max);
				mAllTimeTextView.setText(getTime(max));
				mVodPlayer.seekTo(lastPostion);
				mPauseScreenplay.setImageResource(R.drawable.icon_pause);

				if (null != chapterListAdapter) {
					chapterList.clear();
					if (null != msg.obj) {
						List<DocInfo> docInfoList = (List<DocInfo>) msg.obj;
						for (DocInfo docInfo : docInfoList) {
							List<PageInfo> pageInfoList = docInfo.getPages();
							if (null != pageInfoList && pageInfoList.size() > 0) {
								for (PageInfo pageInfo : pageInfoList) {

									ChapterInfo chapterInfo = new ChapterInfo();
									chapterInfo.setDocId(docInfo.getDocId());
									chapterInfo
											.setDocName(docInfo.getDocName());
									chapterInfo.setDocPageNum(docInfo
											.getPageNum());
									chapterInfo.setDocType(docInfo.getType());

									chapterInfo.setPageTimeStamp(pageInfo
											.getTimeStamp());
									chapterInfo.setPageTitle(pageInfo
											.getTitle());
									chapterList.add(chapterInfo);
								}
							}
						}

					}
					chapterListAdapter.notifyData(chapterList);
				}
				break;
			case MSG.MSG_ON_STOP:

				break;
			case MSG.MSG_ON_VIDEOSIZE:

				break;
			case MSG.MSG_ON_PAGE:
				int position = (Integer)msg.obj;
				int nSize = chapterList.size();
				for(int i = 0; i < nSize; i++)
				{
					ChapterInfo chapterInfo  = chapterList.get(i);
					if(chapterInfo.getPageTimeStamp() == position)
					{
						if(null != chapterListAdapter)
						{
							chapterListAdapter.setSelectedPosition(i);
						}
						break;
					}
				}
				break;
			case MSG.MSG_ON_PAUSE:
				VIEDOPAUSEPALY = 1;
				mPauseScreenplay.setImageResource(R.drawable.icon_player_play);
				break;
			case MSG.MSG_ON_RESUME:
				VIEDOPAUSEPALY = 0;
				mPauseScreenplay.setImageResource(R.drawable.icon_player_pause);
				break;
			case MSG.MSG_ON_POSITION:
				if (isTouch) {
					return;
				}
			case MSG.MSG_ON_SEEK:
				isTouch = false;
				int anyPosition = (Integer) msg.obj;
				mSeekBarPlayViedo.setProgress(anyPosition);
				anyPosition = anyPosition / 1000;
				mNowTimeTextview.setText(getTime(anyPosition));
				break;
			case MSG.MSG_ON_AUDIOLEVEL:

				break;
			case MSG.MSG_ON_ERROR:
				int errorCode = (Integer) msg.obj;
				switch (errorCode) {
				case ERR_PAUSE:
					Toast.makeText(getApplicationContext(), "暂停失败", DURITME)
							.show();
					break;
				case ERR_PLAY:
					Toast.makeText(getApplicationContext(), "播放失败", DURITME)
							.show();
					break;
				case ERR_RESUME:
					Toast.makeText(getApplicationContext(), "恢复失败", DURITME)
							.show();
					break;
				case ERR_SEEK:
					Toast.makeText(getApplicationContext(), "进度变化失败", DURITME)
							.show();
					break;
				case ERR_STOP:
					Toast.makeText(getApplicationContext(), "停止失败", DURITME)
							.show();
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};


	private String getTime(int time) {
		return String.format("%02d", time / 3600) + ":"
				+ String.format("%02d", time % 3600 / 60) + ":"
				+ String.format("%02d", time % 3600 % 60);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showLoadDialog();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_play);
		linear_linear = (LinearLayout)findViewById(R.id.linear_linear);
		linear_linerae_01 = (RelativeLayout) findViewById(R.id.linear_linerae_01);
		startCountTimeThread();
		lastPostion = getPreferences(MODE_PRIVATE).getInt("lastPos", 0);
		mGSVideoView = (GSVideoView) findViewById(R.id.gsvideoview);
		mGlDocView = (GSDocViewGx) findViewById(R.id.playGlDocView);
		mSeekBarPlayViedo = (SeekBar) findViewById(R.id.seekbarpalyviedo);
		stopVeidoPlay = (Button) findViewById(R.id.stopveidoplay);
		mPauseScreenplay = (ImageButton) findViewById(R.id.pauseresumeplay);
		replyVedioPlay = (Button) findViewById(R.id.replayvedioplay);
		mNowTimeTextview = (TextView) findViewById(R.id.palynowtime);
		mAllTimeTextView = (TextView) findViewById(R.id.palyalltime);
		btnDocList = (Button) findViewById(R.id.doc_list_btn);
//		btnDocList.setOnClickListener();
		chapterListAdapter = new ChapterListAdapter();
		chapterList = new ArrayList<ChapterInfo>();
		lvChapterList = (ListView) findViewById(R.id.doc_lv);
		lvChapterList.setAdapter(chapterListAdapter);
//		mGlDocView.setOnClickListener(this);

		lvChapterList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
				ChapterInfo chapterInfo = chapterList.get(position);
				if (null != mVodPlayer) {
					mVodPlayer.seekTo(chapterInfo.getPageTimeStamp());
				}
			}
		});

		mSeekBarPlayViedo.setOnSeekBarChangeListener(this);
		//mDocView.setOnDocViewClickedListener(this);
		// mDocView.showAdaptViewWidth();


		initPlayer();


	}



//	@Override
//	public boolean On(MotionEvent event) {
//		boolean result;
//
//		countTimeThread.reset();//重置时间
//		if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			Log.i("kkkk","sssssssk");
//			boolean isVisible = (linear_linear.getVisibility() ==View.VISIBLE);
//
//			if (!isVisible) {
//				linear_linear.setVisibility(View.VISIBLE);
//				return false;
//			}
//		}
//		return true;
//
////		System.out.println("activity touch: " + result);
////		return result;
//	}

//		countTimeThread.reset();//重置时间
//		if (event.getAction() == MotionEvent.ACTION_UP) {
//			Log.i("kkkk","sssssssk");
//			boolean isVisible = (linear_linear.getVisibility() ==View.VISIBLE);
//
//			if (!isVisible) {
//				linear_linear.setVisibility(View.VISIBLE);
//				return true;
//			}
//		}
//		return super.onTouchEvent(event);

//	}


	private void initPlayer() {

		String vodIdOrLocalPath = getVodIdOrLocalPath();
		if (vodIdOrLocalPath == null) {
			Toast.makeText(this, "路径不对", Toast.LENGTH_SHORT).show();
			return;
		}
		if (mVodPlayer == null) {
			mVodPlayer = new VODPlayer();
			mVodPlayer.setGSVideoView(mGSVideoView);
			mVodPlayer.setGSDocViewGx(mGlDocView);
			mVodPlayer.play(vodIdOrLocalPath, this, "",false);
		}
	}

	//如果有触摸事件，，则重置显示时间




	/**

	 * 开启监听控件隐藏的线程

	 */

	private void startCountTimeThread(){

		countTimeThread = new CountTimeThread(10);

		countTimeThread.start();

	}



	/**

	 * 隐藏需要隐藏的按钮

	 */

	private void hide(){

		if (linear_linear.getVisibility() == View.VISIBLE) {

			linear_linear.setVisibility(View.GONE);
			btnDocList.setVisibility(View.GONE);
		}
	}



	/**

	 * Handler消息传递机制

	 */

	class MyHandler extends Handler {

		//发送消息的id

		private final int MSG_HIDE = 0x001;

		//WeakReference垃圾回收机制

		private WeakReference<PlaysActivity> weakRef;

		public MyHandler(PlaysActivity pMainActivity) {

			weakRef = new WeakReference <PlaysActivity > (pMainActivity);

		}


		@Override

		public void handleMessage(Message msg) {

			final PlaysActivity mainActivity = weakRef.get();
			if (mainActivity != null) {
				switch (msg.what) {
					case MSG_HIDE:
						mainActivity.hide();
						break;
				}
			}
			super.handleMessage(msg);
		}


		/**
		 * 发送消息
		 */

		public void sendHideControllMessage() {

			obtainMessage(MSG_HIDE).sendToTarget();

		}
	}

	/**
	 * 用进程监听按钮控件的显示时间，定时隐藏
	 *
	 *
	 */

	private class CountTimeThread extends Thread{

		private final long maxVisibleTime;
		private long startVisibleTime;
		/**
		 * 设置控件显示时间 second单位是秒
		 * * @param second
		 */

		public CountTimeThread(int second){
			maxVisibleTime = second * 1000;//换算为毫秒
			setDaemon(true);//设置为后台进程

		}
		/**
		 * 如果用户有操作，则重新开始计时隐藏时间
		 */

		private synchronized void reset() {
			startVisibleTime = System.currentTimeMillis();
		}

		@Override
		public void run() {

			startVisibleTime = System.currentTimeMillis();//初始化开始时间
			while (true) {
				//如果时间达到最大时间，则发送隐藏消息
				if (startVisibleTime + maxVisibleTime < System.currentTimeMillis()){
					mHandler.sendHideControllMessage();
					startVisibleTime = System.currentTimeMillis();
				}
				try {
					Thread.sleep(1000);
				}catch(Exception e) {
				}
			}
		}

	}

	private String getVodIdOrLocalPath() {

		String vodId = getIntent().getStringExtra("play_param");
		String localpath = getIntent().getStringExtra("play_path");
		GenseeLog.d(TAG, "path = " + localpath + " vodId = " + vodId);
		String vodIdOrLocalPath = null;
		if (!StringUtil.isEmpty(localpath)) {
			vodIdOrLocalPath = localpath;
		} else if (!StringUtil.isEmpty(vodId)) {
			vodIdOrLocalPath = vodId;
		}
		return vodIdOrLocalPath;
	}

	@Override
	public void onInit(int result, boolean haveVideo, int duration,
			List<DocInfo> docInfos) {
		dismissLoadDialog();
		if (lastPostion >= duration-1000) {
			lastPostion = 0;
		}
		Message message = new Message();
		message.what = MSG.MSG_ON_INIT;
		message.obj = docInfos;
		Bundle bundle = new Bundle();
		bundle.putInt(DURATION, duration);
		message.setData(bundle);
		myHandler.sendMessage(message);
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public void onPlayStop() {
		myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_STOP, 0));
	}

	@Override
	public void onPosition(int position) {
		GenseeLog.d(TAG, "onPosition pos = " + position);
		lastPostion = position;
		myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_POSITION,
				position));
	}

	@Override
	public void onVideoSize(int position, int videoWidth, int videoHeight) {
		myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_VIDEOSIZE, 0));
	}


	@Override
	public void onSeek(int position) {
		myHandler.sendMessage(myHandler
				.obtainMessage(MSG.MSG_ON_SEEK, position));
	}

	@Override
	public void onAudioLevel(int level) {
		myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_AUDIOLEVEL,
				level));
	}

	@Override
	public void onError(int errCode) {
		myHandler.sendMessage(myHandler
				.obtainMessage(MSG.MSG_ON_ERROR, errCode));
	}

	@Override
	public void onPlayPause() {
		myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_PAUSE, 0));
	}

	@Override
	public void onPlayResume() {
		myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_RESUME, 0));
	}

	

	@Override
	public void onPageSize(int position, int w, int h) {
		//文档翻页切换，开始显示
		myHandler.sendMessage(myHandler
				.obtainMessage(MSG.MSG_ON_PAGE, position));
		
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		isTouch = true;

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (null != mVodPlayer) {
			int pos = seekBar.getProgress();
			GenseeLog.d(TAG, "onStopTrackingTouch pos = " + pos);
			mVodPlayer.seekTo(pos);

		}

	}

	@OnClick({R.id.stopveidoplay, R.id.pauseresumeplay, R.id.replayvedioplay, R.id.doc_list_btn, R.id.speed, R.id.hide})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.stopveidoplay://暂停
				boolean ret = mVodPlayer.stop();
				mSeekBarPlayViedo.setMax(0);
				Toast.makeText(this, ret ? "操作成功" : "操作失败", DURITME).show();
				break;
			case R.id.pauseresumeplay:
				if (VIEDOPAUSEPALY == 0) {
					mVodPlayer.pause();
				} else if (VIEDOPAUSEPALY == 1) {
					mVodPlayer.resume();
				}
				break;
			case R.id.replayvedioplay:
				isTouch = false;
				String vodIdOrLocalPath = getVodIdOrLocalPath();
				if (vodIdOrLocalPath == null) {
					Toast.makeText(this, "路径不对", Toast.LENGTH_SHORT).show();
					return;
				}
				//重新播放时  将速度恢复为正常速度，如果要保持上一次速度播放，设置为上一次速度
				speedItem = 0;
				mVodPlayer.setSpeed(PlaySpeed.SPEED_NORMAL, null);
				mVodPlayer.setGSVideoView(mGSVideoView);
				mVodPlayer.setGSDocViewGx(mGlDocView);
				mVodPlayer.play(vodIdOrLocalPath, this, "",false);
				break;
			case R.id.doc_list_btn:
				if (lvChapterList.getVisibility() == View.VISIBLE) {
					lvChapterList.setVisibility(View.GONE);
				} else {
					lvChapterList.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.speed:
				switchSpeed();
				break;
			case R.id.hide:
				countTimeThread.reset();
				boolean isVisible = (linear_linear.getVisibility() ==View.VISIBLE);
			if (!isVisible) {
				linear_linear.setVisibility(View.VISIBLE);
				btnDocList.setVisibility(View.VISIBLE);
			}
				break;
			default:
				break;
		}
	}



//	@Override
//	public void onClick(View currenView) {
//		if (currenView.getId() == R.id.stopveidoplay) {
//
//		} else if (currenView.getId() == R.id.replayvedioplay) {
//			isTouch = false;
//			String vodIdOrLocalPath = getVodIdOrLocalPath();
//			if (vodIdOrLocalPath == null) {
//				Toast.makeText(this, "路径不对", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			//重新播放时  将速度恢复为正常速度，如果要保持上一次速度播放，设置为上一次速度
//			speedItem = 0;
//			mVodPlayer.setSpeed(PlaySpeed.SPEED_NORMAL, null);
//			mVodPlayer.setGSVideoView(mGSVideoView);
//			mVodPlayer.setGSDocViewGx(mGlDocView);
//			mVodPlayer.play(vodIdOrLocalPath, this, "",false);
//
//		} else if (currenView.getId() == R.id.pauseresumeplay) {
//			if (VIEDOPAUSEPALY == 0) {
//				mVodPlayer.pause();
//			} else if (VIEDOPAUSEPALY == 1) {
//				mVodPlayer.resume();
//			}
//		} else if (currenView.getId() == R.id.doc_list_btn) {
//			if (lvChapterList.getVisibility() == View.VISIBLE) {
//				lvChapterList.setVisibility(View.GONE);
//			} else {
//				lvChapterList.setVisibility(View.VISIBLE);
//			}
//		} else if(currenView.getId() == R.id.speed){
//			switchSpeed();
//		}else if(currenView.getId() == R.id.playGlDocView){
//			countTimeThread.reset();
//			linear_linear.setVisibility(View.VISIBLE);
//				//重置时间
//		}
//	}
	
	/**
	 * 变速播放
	 */
	private void switchSpeed() {
		new AlertDialog.Builder(this)
				.setSingleChoiceItems(R.array.speeds, speedItem,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								speedItem = which;
								PlaySpeed ps = PlaySpeed.SPEED_NORMAL;
								switch (which) {
								case 0:
									ps = PlaySpeed.SPEED_NORMAL;
									break;
								case 1:
									ps = PlaySpeed.SPEED_125;
									break;
								case 2:
									ps = PlaySpeed.SPEED_150;
									break;
								case 3:
									ps = PlaySpeed.SPEED_175;
									break;
								case 4:
									ps = PlaySpeed.SPEED_200;
									break;
								case 5:
									ps = PlaySpeed.SPEED_250;
									break;
								case 6:
									ps = PlaySpeed.SPEED_300;
									break;
								case 7:
									ps = PlaySpeed.SPEED_350;
									break;
								case 8:
									ps = PlaySpeed.SPEED_400;
									break;

								default:
									break;
								}
								mVodPlayer.setSpeed(ps, null);
								dialog.dismiss();
							}
						}).create().show();
	}

	private void stopPlay() {
		if (mVodPlayer != null) {
			mVodPlayer.stop();
		}
	}

	private void release() {
		stopPlay();
		if (mVodPlayer != null) {
			mVodPlayer.release();
		}
	}

	@Override
	public void onBackPressed() {
//		getPreferences(MODE_PRIVATE).edit().putInt("lastPos", lastPostion).commit();
		release();
		super.onBackPressed();
	}

	@Override
	public boolean onDoubleClicked(IGSDocView arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	/**
	 * type 边界类型
	 * eventType  MotionEvent.ACTION_MOVE or MotionEvent.ACTION_UP
	 */
	public boolean onEndHDirection(IGSDocView arg0, int arg1, int eventType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSingleClicked(IGSDocView arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCaching(boolean isCatching) {
		// TODO Auto-generated method stub

	}

	private class ChapterListAdapter extends BaseAdapter {
		private List<ChapterInfo> pageList;
		private int selectedPosition = 0;

		public void setSelectedPosition(int position) {
			selectedPosition = position;
			notifyDataSetChanged();
			lvChapterList.setSelection(position);
		}

		public ChapterListAdapter() {
			pageList = new ArrayList<ChapterInfo>();
		}

		public void notifyData(List<ChapterInfo> pageList) {
			this.pageList.clear();
			this.pageList.addAll(pageList);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return pageList.size();
		}

		@Override
		public Object getItem(int position) {
			return pageList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.doc_list_item_ly, null);
				viewHolder = new ViewHolder(convertView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.init((ChapterInfo) getItem(position), position);
			return convertView;
		}

		private class ViewHolder {
			private TextView tvChapter;
			private TextView tvTitle;
			private TextView tvTime;
			private LinearLayout lyChapter;

			private String getChapterTime(long time) {
				return String.format("%02d", time / (3600 * 1000))
						+ ":"
						+ String.format("%02d", time % (3600 * 1000)
								/ (60 * 1000))
						+ ":"
						+ String.format("%02d", time % (3600 * 1000)
								% (60 * 1000) / 1000);
			}

			public ViewHolder(View view) {
				tvChapter = (TextView) view.findViewById(R.id.chapter_title);
				tvTitle = (TextView) view.findViewById(R.id.doc_title);
				tvTime = (TextView) view.findViewById(R.id.chapter_time);
				lyChapter = (LinearLayout) view.findViewById(R.id.chapter_ly);
			}

			public void init(ChapterInfo chapterInfo, int position) {
				tvChapter.setText(chapterInfo.getPageTitle());
				tvTime.setText(getChapterTime(chapterInfo.getPageTimeStamp()));
				tvTitle.setText(chapterInfo.getDocName());

				if (selectedPosition == position) {
					lyChapter.setBackgroundResource(R.color.color_text_green);
				} else {
					lyChapter.setBackgroundResource(R.color.color_default_gray);
				}
			}
		}

	}

	@Override
	public void onVideoStart() {
		
	}

	@Override
	public void onChat(List<ChatMsg> arg0) {
		//ChatMsg msg = chatMsgs.get(0);
				// msg.getRichText()//富文本
				// msg.getSender()//发送者名称
				// msg.getSenderId()//发送者id
				// msg.getContent()//纯文本
				// msg.getTimeStamp()//相对与播放开始的时间  单位毫秒		
	}

	@Override
	public void onDocInfo(List<DocInfo> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 *  msg 的时候，id代表要删除的聊天消息id，user的时候，代表用户id，强转为long型后进行用户id匹配删除该id说有的聊天消息
	 * @param type msg(CHATCENSOR_MSG) / user(CHATCENSOR_USER)
	 * @param id   msgId/userId
	 */
	@Override
	public void onChatCensor(String type, String id) {
		
	}

	@Override
	protected void onDestroy() {
		dismissLoadDialog();
		super.onDestroy();
		mVodPlayer.release();
	}
}
