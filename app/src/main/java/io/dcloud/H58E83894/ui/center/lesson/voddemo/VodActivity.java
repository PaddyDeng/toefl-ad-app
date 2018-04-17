package io.dcloud.H58E83894.ui.center.lesson.voddemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.gensee.common.ServiceType;
import com.gensee.download.ErrorCode;
import com.gensee.download.VodDownLoadEntity;
import com.gensee.download.VodDownLoadStatus;
import com.gensee.download.VodDownLoader;
import com.gensee.download.VodDownLoader.OnDownloadListener;
import com.gensee.entity.ChatMsg;
import com.gensee.entity.InitParam;
import com.gensee.entity.QAMsg;
import com.gensee.entity.VodObject;

import com.gensee.taskret.OnTaskRet;
import com.gensee.utils.GenseeLog;
import com.gensee.utils.StringUtil;
import com.gensee.vod.VodSite;
import com.gensee.vod.VodSite.OnVodListener;

import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.MyLessonData;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.ui.center.lesson.config.ConfigApp;

import static com.gensee.common.ServiceType.TRAINING;

public class VodActivity extends BaseActivity implements
		OnDownloadListener, OnItemClickListener, OnVodListener {


	public static void startVod(Context mContext, MyLessonData.DataBean.DatailsBean data) {
		Intent intent = new Intent(mContext, VodActivity.class);
		intent.putExtra(Intent.EXTRA_TEXT, data);
		mContext.startActivity(intent);
	}
	private String TAG = "MainActivity";
	private ListView mListView;
	private MyAdapter mMyAdapter;
	private VodDownLoader mVodDownLoader;

	private View optionSelect;
	private View btnDownLoad, btnPlay;

	@BindView(R.id.type_title)
	TextView titleName;


	private VodSite vodSite;
	private SharedPreferences preferences;
	private MyLessonData.DataBean.DatailsBean  detailData;


	private static final int DURTIME = 2000;

	public interface RESULT {
		int DOWNLOAD_ERROR = 2;
		int DOWNLOAD_STOP = 3;
		int DOWNLOADER_INIT = 4;
		int DOWNLOAD_START = 5;
		int ON_GET_VODOBJ = 100;
	}

	protected void getArgs() {
		Intent intent = getIntent();
		if (intent == null) return;
//		titleName = intent.getStringExtra(Intent.EXTRA_TITLE);
		detailData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
	}



	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
				case RESULT.DOWNLOAD_ERROR:
					// notifyData();
					Toast.makeText(getApplicationContext(), "下载出错", DURTIME).show();
					break;
				case RESULT.DOWNLOAD_STOP:
					notifyData();
					Toast.makeText(getApplicationContext(), "下载停止", DURTIME).show();

					break;
				case RESULT.DOWNLOAD_START:
					notifyData();
					Toast.makeText(getApplicationContext(), "下载开始", DURTIME).show();
					break;
				case RESULT.ON_GET_VODOBJ:

					optionSelect.setVisibility(View.VISIBLE);
					final String vodId = (String) msg.obj;
					// download(vodId);
					// notifyData();
					// 在线播放
					btnPlay.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							optionSelect.setVisibility(View.GONE);
							Intent i = new Intent(VodActivity.this,
									PlaysActivity.class);
							i.putExtra("play_param", vodId);
							startActivity(i);
							finish();
						}
					});
					// 下载
					btnDownLoad.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							optionSelect.setVisibility(View.GONE);
							download(vodId);
						}
					});
					break;
				case RESULT.DOWNLOADER_INIT:
					if (mVodDownLoader != null) {
						mMyAdapter.notifyData(mVodDownLoader.getDownloadList());
					}
					break;
				default:
					break;
			}
		}

	};

	private void download(String vodId) {
		mVodDownLoader.setAutoDownloadNext(true);
		int ret = mVodDownLoader.download(vodId);
		switch (ret) {
			case ErrorCode.SUCCESS:
				Toast.makeText(this, "下载开始", DURTIME).show();
				// 调用成功，刷新列表
				notifyData();
				break;
			case ErrorCode.DOWNLOADING_HAVE_EXIST:
				Toast.makeText(this, "已有下载任务 。将被添加到队列稍后下载", DURTIME).show();
				break;
			case ErrorCode.DOWNLOADING_URL_NULL:
				Toast.makeText(this, "下载地址为空", DURTIME).show();
				break;
			case ErrorCode.OBJECT_HAVE_EXIST:
				Toast.makeText(this, "录制件已在下载队列中", DURTIME).show();
				break;
			case ErrorCode.ERR_UN_INVOKE_GETOBJECT:
				Toast.makeText(this, "请先调用vodSite.getObject(),onVodObject 响应后再下载", DURTIME).show();
				break;
			case ErrorCode.SDCARD_ERROR:
				Toast.makeText(this, "SD卡异常", DURTIME).show();
				break;
			default:
				break;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vod_layout);
		getArgs();
		preferences = getPreferences(MODE_PRIVATE);
		startLogService();
		mMyAdapter = new MyAdapter(this);
//		mListView = (ListView) findViewById(R.id.downloadlist);

		/*
		 * 代理使用，如果app有自己的网络代理，调用setTcpProxy， 然后在IGSOLProxy的ip和端口回调中返回相应的代理ip和代理端口，
		 * 没有代理则无需调用。此函数任何时候都可以调用。
		 */
		/*
		 * VodSite.setTcpProxy(new IProxy() {
		 *
		 * @Override public int getProxyPort(int port) { // 返回代理端口 return port;
		 * }
		 *
		 * @Override public String getProxyIP(String ip) { // 返回代理ip地址 return
		 * ip; } });
		 */

		/**
		 * 优先调用进行组件加载，有条件的情况下可以放到application启动时候的恰当时机调用
		 */
		VodSite.init(this, null);

		//如果需要区分多用户，请使用带用户id的instance进行初始化，默认情况下用户id为0
		mVodDownLoader = VodDownLoader.instance(this, this, null);
		// 启动已存在且未完成的任务
		mVodDownLoader.download();
		// 刷新列表
		mHandler.sendEmptyMessage(RESULT.DOWNLOADER_INIT);
//		mListView.setAdapter(mMyAdapter);
		initView();
		initData();
	}

	private void notifyData() {
		if (null != mMyAdapter) {
			mMyAdapter.notifyData(mVodDownLoader.getDownloadList());
		}
	}

	public void initView() {
		optionSelect = findViewById(R.id.optionSelect);
		btnDownLoad = optionSelect.findViewById(R.id.btnDownLoad);
		btnPlay = optionSelect.findViewById(R.id.btnDownPlay);
	}


	protected void initData(){
		titleName.setText(detailData.getName().trim());
		UserData data = GlobalUser.getInstance().getUserData();
		InitParam initParam = new InitParam();
		// 设置域名
		Log.i("tmd", "sdk = " +detailData.getSdk()+"pass = "+detailData.getPwd() );
		initParam.setDomain("bjsy.gensee.com");
//		initParam.setLiveId(detailData.getSdk());
		initParam.setLiveId(detailData.getSdk().trim());
//		// 设置站点登录帐号（根据配置可选）
//		initParam.setLoginAccount(data.getUserName());
////		// 设置站点登录密码（根据配置可选）
//		initParam.setLoginPwd(data.getUserPass());
		// 设置显示昵称 不能为空,请传入正确的昵称，有显示和统计的作用
		// 设置显示昵称，如果设置为空，请确保
//		showTip(true, "正在玩命加入...");
		Log.i("NickName", data.getNickname());
		initParam.setNickName(data.getNickname().trim());


		// 设置加入口令（根据配置可选）
		initParam.setVodPwd(detailData.getPwd().trim());

//		initParam.setVodPwd(detailData.getPwd());
		// 设置服务类型，如果站点是webcast类型则设置为ServiceType.ST_CASTLINE，
		// training类型则设置为ServiceType.ST_TRAINING
		initParam.setServiceType(ServiceType.ST_TRAINING);

//		InitParam initParam = ConfigApps.getIns().getInitParam();
		vodSite = new VodSite(this);
		vodSite.setVodListener(this);
		Log.i("tmd", "vodsite = "+initParam+initParam.getJoinPwd());
		vodSite.getVodObject(initParam);
	}

	@Override
	public void onDLFinish(String downLoadId, String localPath) {
		GenseeLog.i(TAG, "onDLFinish downLoadId = " + downLoadId);
		preferences.edit().putString(downLoadId, localPath).commit();
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mMyAdapter.notifyData(mVodDownLoader.getDownloadList());
			}
		});
	}

	@Override
	public void onDLPosition(String downLoadId, final int percent) {
		GenseeLog.i(TAG, "onDLPosition downLoadId = " + downLoadId
				+ " percent = " + percent);
		// 下载过程中
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				mMyAdapter.notifyData(mVodDownLoader.getDownloadList());
			}
		});
	}

	public void onDLPrepare(String downLoadId) {
		GenseeLog.i(TAG, "onDLPrepare downLoadId = " + downLoadId);
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				mMyAdapter.notifyData(mVodDownLoader.getDownloadList());
			}
		});
	}

	@Override
	public void onDLStart(String downLoadId) {
		GenseeLog.i(TAG, "onDLStart downLoadId = " + downLoadId);
		// 下载开始
		mHandler.sendEmptyMessage(RESULT.DOWNLOAD_START);
	}

	@Override
	public void onDLStop(String downLoadId) {
		// 下载停止
		GenseeLog.i(TAG, "onDLStop downLoadId = " + downLoadId);
		mHandler.sendEmptyMessage(RESULT.DOWNLOAD_STOP);

	}

	@Override
	public void onDLError(String downLoadId, int errorCode) {
		GenseeLog.i(TAG, "onDLError downLoadId = " + downLoadId
				+ " errorCode = " + errorCode);
		mHandler.sendEmptyMessage(RESULT.DOWNLOAD_ERROR);
	}

	class MyAdapter extends BaseAdapter {
		private List<VodDownLoadEntity> entities = null;
		private Context mContext;

		public MyAdapter(Context mContext) {
			this.mContext = mContext;
		}

		public void notifyData(List<VodDownLoadEntity> entities) {
			this.entities = entities;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return entities == null ? 0 : entities.size();
		}

		@Override
		public Object getItem(int position) {
			return entities.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
							ViewGroup parent) {

			if (null == convertView) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.listitem, null);
			}
			final VodDownLoadEntity entity = (VodDownLoadEntity) getItem(position);
			TextView mTextView = (TextView) convertView
					.findViewById(R.id.downloadcontextid);
			TextView mProTextView = (TextView) convertView
					.findViewById(R.id.downloadprogressitem);
			ImageButton deleteButton = (ImageButton) convertView
					.findViewById(R.id.deleteDownload);
			ImageButton stopButton = (ImageButton) convertView
					.findViewById(R.id.stopdownload);
			final int status = entity.getnStatus();
			if (status == VodDownLoadStatus.FINISH.getStatus()) {
				stopButton.setVisibility(View.INVISIBLE);
			} else {
				stopButton.setVisibility(View.VISIBLE);
//				if (status == VodDownLoadStatus.STOP.getStatus()) {
//					stopButton.setImageResource(R.drawable.down_loading);
//				} else if (status == VodDownLoadStatus.BEGIN.getStatus()
//						|| status == VodDownLoadStatus.START.getStatus()) {
//					stopButton.setImageResource(R.drawable.down_normal);
//				} else {
//					stopButton.setImageResource(R.drawable.down_loading);
//				}
			}
			ImageButton playButton = (ImageButton) convertView
					.findViewById(R.id.palydownload);
			mTextView.setText(entity.getVodSubject() + " ");
			mProTextView.setText(entity.getnPercent() + "%");
			deleteButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mVodDownLoader.delete(entity.getDownLoadId());
					// 同时删除已经存放的本地路径
					preferences.edit().remove(entity.getDownLoadId());
					// 删除之后刷新
					notifyData(mVodDownLoader.getDownloadList());
				}
			});

			stopButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (status == VodDownLoadStatus.FAILED.getStatus()
							|| status == VodDownLoadStatus.STOP.getStatus()
							|| status == VodDownLoadStatus.DENY.getStatus()) {
						mVodDownLoader.download(entity.getDownLoadId());
					} else if (status == VodDownLoadStatus.BEGIN.getStatus()
							|| status == VodDownLoadStatus.START.getStatus()) {
						mVodDownLoader.stop(entity.getDownLoadId());
					}
				}
			});

			playButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String loacalPath = preferences.getString(
							entity.getDownLoadId(), "");
					// 离线播放
					if (!StringUtil.isEmpty(loacalPath)) {
						Intent intent = new Intent(VodActivity.this,
								PlaysActivity.class);
						intent.putExtra("play_path", loacalPath);
						startActivity(intent);
					}
				}
			});
			return convertView;
		}
	}

	@Override
	public void onBackPressed() {
		// 退出下载相关的功能时，释放掉
		mVodDownLoader.release();
		mVodDownLoader = null;
		stopLogService();
		super.onBackPressed();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
	}

	/********************* OnVodListener **************************/


	/**
	 * 聊天记录 getChatHistory 响应 vodId 点播id list 聊天记录
	 */
	@Override
	public void onChatHistory(String vodId, List<ChatMsg> list,int pageIndex,boolean more) {
		GenseeLog.d(TAG, "onChatHistory vodId = " + vodId + " " + list);
		// ChatMsg msg;
		// msg.getContent();//消息内容
		// msg.getSenderId();//发送者用户id
		// msg.getSender();//发送者昵称
		// msg.getTimeStamp();//发送时间，单位毫秒
	}

	/**
	 * 问答记录 getQaHistory 响应 list 问答记录 vodId 点播id
	 */
	@Override
	public void onQaHistory(String vodId, List<QAMsg> list,int pageIndex,boolean more) {
		GenseeLog.d(TAG, "onQaHistory vodId = " + vodId + " " + list);
		if (more) {
			// 如果还有更多的历史，还可以继续获取记录（pageIndex+1）页的记录
		}
		// QAMsg msg;
		// msg.getQuestion();//问题
		// msg.getQuestId();//问题id
		// msg.getQuestOwnerId();//提问人id
		// msg.getQuestOwnerName();//提问人昵称
		// msg.getQuestTimgstamp();//提问时间 单位毫秒
		//
		// msg.getAnswer();//回复的内容
		// msg.getAnswerId();//“本条回复”的id 不是回答者的用户id
		// msg.getAnswerOwner();//回复人的昵称
		// msg.getAnswerTimestamp();//回复时间 单位毫秒
	}


	/**
	 * 获取点播详情
	 */
	@Override
	public void onVodDetail(VodObject vodObj) {
		GenseeLog.d(TAG, "onVodDetail " + vodObj);
		if (vodObj != null) {
			vodObj.getDuration();// 时长
			vodObj.getEndTime();// 录制结束时间 始于1970的utc时间毫秒数
			vodObj.getStartTime();// 录制开始时间 始于1970的utc时间毫秒数
			vodObj.getStorage();// 大小 单位为Byte
		}
	}

	// int ERR_DOMAIN = -100; // ip(domain)74
	// int ERR_TIME_OUT = -101; // 超时
	// int ERR_UNKNOWN = -102; // 未知错误
	// int ERR_SITE_UNUSED = -103; // 站点不可用
	// int ERR_UN_NET = -104; // 无网络
	// int ERR_DATA_TIMEOUT = -105; // 数据过期
	// int ERR_SERVICE = -106; // 服务不正确
	// int ERR_PARAM = -107; // 参数不正确
	// int ERR_PARAM = -107; // 参数不正确
	// int ERR_THIRD_CERTIFICATION_AUTHORITY //第三方认证失败
	// int ERR_UN_INVOKE_GETOBJECT = -201; //没有调用getVodObject
	// int ERR_VOD_INTI_FAIL = 14; //点播getVodObject失败
	// int ERR_VOD_NUM_UNEXIST = 15; //点播编号不存在或点播不存在
	// int ERR_VOD_PWD_ERR = 16; //点播密码错误
	// int ERR_VOD_ACC_PWD_ERR = 17; //帐号或帐号密码错误
	// int ERR_UNSURPORT_MOBILE = 18; //不支持移动设备
	@Override
	public void onVodErr(final int errCode) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				String msg = getErrMsg(errCode);
				if (!"".equals(msg)) {
					Toast.makeText(VodActivity.this, msg, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

	}

	/**
	 * getVodObject的响应，vodId 接下来可以下载后播放
	 */
	@Override
	public void onVodObject(String vodId) {
		GenseeLog.d(TAG, "onVodObject vodId = " + vodId);

		mHandler.sendMessage(mHandler
				.obtainMessage(RESULT.ON_GET_VODOBJ, vodId));

//		vodSite.getChatHistory(vodId, 1);//获取聊天历史记录，起始页码1
//		vodSite.getQaHistory(vodId, 1);//获取问答历史记录，起始页码1
	}

	/**
	 * 错误码处理
	 *
	 * @param errCode
	 *            错误码
	 * @return 错误码文字表示内容
	 */
	private String getErrMsg(int errCode) {
		String msg = "";
		switch (errCode) {
			case ERR_DOMAIN:
				msg = "domain 不正确";
				break;
			case ERR_TIME_OUT:
				msg = "超时";
				break;
			case ERR_SITE_UNUSED:
				msg = "站点不可用";
				break;
			case ERR_UN_NET:
				msg = "无网络请检查网络连接";
				break;
			case ERR_DATA_TIMEOUT:
				msg = "数据过期";
				break;
			case ERR_SERVICE:
				msg = "请检查填写的serviceType";
				break;
			case ERR_PARAM:
				msg = "请检查参数";
				break;
			case ERR_UN_INVOKE_GETOBJECT:
				msg = "请先调用getVodObject";
				break;
			case ERR_VOD_INTI_FAIL:
				msg = "调用getVodObject失败";
				break;
			case ERR_VOD_NUM_UNEXIST:
				msg = "点播编号不存在或点播不存在";
				break;
			case ERR_VOD_PWD_ERR:
				msg = "点播密码错误";
				break;
			case ERR_VOD_ACC_PWD_ERR:
				msg = "登录帐号或登录密码错误";
				break;
			case ERR_UNSURPORT_MOBILE:
				msg = "不支持移动设备";
				break;

			default:
				break;
		}
		return msg;
	}



	private void startLogService() {
		startService(new Intent(this, LogCatService.class));
	}

	private void stopLogService() {
		stopService(new Intent(this, LogCatService.class));
	}
}
