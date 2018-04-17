package io.dcloud.H58E83894.http;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import io.dcloud.H58E83894.data.AdvertisingData;
import io.dcloud.H58E83894.data.CorretData;
import io.dcloud.H58E83894.data.DownloadData;
import io.dcloud.H58E83894.data.GlossaryData;
import io.dcloud.H58E83894.data.GlossaryWordData;
import io.dcloud.H58E83894.data.InforData;
import io.dcloud.H58E83894.data.JsonRootBean;
import io.dcloud.H58E83894.data.KnowMaxListData;
import io.dcloud.H58E83894.data.LeidouReData;
import io.dcloud.H58E83894.data.ListenRecordData;
import io.dcloud.H58E83894.data.ListsData;
import io.dcloud.H58E83894.data.MsgData;
import io.dcloud.H58E83894.data.MyLessonData;
import io.dcloud.H58E83894.data.PayData;
import io.dcloud.H58E83894.data.PayDatas;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.circle.CommunityData;
import io.dcloud.H58E83894.data.circle.RemarkBean;
import io.dcloud.H58E83894.data.circle.RemarkData;
import io.dcloud.H58E83894.data.circle.ReplyData;
import io.dcloud.H58E83894.data.commit.TodayListData;
import io.dcloud.H58E83894.data.instestlisten.AllInstListenData;
import io.dcloud.H58E83894.data.know.KnowTypeData;
import io.dcloud.H58E83894.data.know.KnowZoneData;
import io.dcloud.H58E83894.data.make.AllTaskData;
import io.dcloud.H58E83894.data.make.GrammarData;
import io.dcloud.H58E83894.data.make.ListenData;
import io.dcloud.H58E83894.data.make.ListenPracticeData;
import io.dcloud.H58E83894.data.make.ListenQuestionData;
import io.dcloud.H58E83894.data.make.ListenSecTpoData;
import io.dcloud.H58E83894.data.make.OnlyMineData;
import io.dcloud.H58E83894.data.make.PracticeData;
import io.dcloud.H58E83894.data.make.PracticeQuestionData;
import io.dcloud.H58E83894.data.make.ReadData;
import io.dcloud.H58E83894.data.make.ReadOgData;
import io.dcloud.H58E83894.data.make.ReadQuestionData;
import io.dcloud.H58E83894.data.make.ReadResultData;
import io.dcloud.H58E83894.data.make.ResultData;
import io.dcloud.H58E83894.data.make.SpeakQuestionData;
import io.dcloud.H58E83894.data.make.SpeakShare;
import io.dcloud.H58E83894.data.make.TodayData;
import io.dcloud.H58E83894.data.make.WriteQuestionData;
import io.dcloud.H58E83894.data.make.WriteTpoData;
import io.dcloud.H58E83894.data.make.core.CoreData;
import io.dcloud.H58E83894.data.prelesson.FreeCursorData;
import io.dcloud.H58E83894.data.prelesson.LessonDetailBean;
import io.dcloud.H58E83894.data.prelesson.PreLessonData;
import io.dcloud.H58E83894.data.prelesson.TeacherData;
import io.dcloud.H58E83894.data.record.ReadRecordData;
import io.dcloud.H58E83894.data.record.WriteRecordData;
import io.dcloud.H58E83894.data.setting.VersionInfo;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.data.user.UserInfo;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by fire on 2017/7/13 15:18.
 */
public interface RestApi {
    /**
     * 获取电话的验证码
     */
    @FormUrlEncoded
    @POST("cn/app-api/phone-code")
    Observable<ResultBean> numGetAuthCode(@Field("phoneNum") String phone, @Field("type") String type);

    /**
     * 获取邮箱的验证码
     */
    @FormUrlEncoded
    @POST("cn/app-api/send-mail")
    Observable<ResultBean> emailGetAuthCode(@Field("email") String email, @Field("type") String type);

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("cn/app-api/register")
    Observable<ResultBean> register(@Field("type") String type, @Field("registerStr") String registerStr,
                                    @Field("pass") String pass, @Field("code") String code,
                                    @Field("userName") String userName, @Field("source") String source, @Field("belong") String belong);


    @FormUrlEncoded
    @POST("cn/app-api/user-replenish")
    Observable<ResultBean> register(@Field("testTime") String testTime, @Field("isTest") int isTest,
                                    @Field("targetScore") int targetScore, @Field("status") String status,
                                    @Field("maxScore") int maxScore);
    /**
     * 正常登录
     */
    @FormUrlEncoded
    @POST("cn/app-api/check-login")
    Observable<UserInfo> login(@Field("userName") String userName, @Field("userPass") String userPass);


    /**
     * 重置密码
     */
    @FormUrlEncoded
    @POST("cn/app-api/find-pass")
    Observable<ResultBean> retrievePwd(@Field("type") String type, @Field("registerStr") String registerStr,
                                       @Field("pass") String pass, @Field("code") String code);


    //============================================================
    /*
    * 登录后重置session
    *   "http://www.toeflonline.cn/cn/api/unify-login",
	    "http://smartapply.gmatonline.cn/cn/api/unify-login",
	    "http://www.gmatonline.cn/index.php?web/webapi/unifyLogin",
	    "http://gossip.gmatonline.cn/cn/app-api/unify-login"
    */

    //    @GET("cn/api/unify-login")
    @GET("cn/app-api/unify-login")
    Observable<Response<Void>> toefl(@QueryMap Map<String, String> info);

    //    @GET("cn/api/unify-login")
    @GET("cn/app-api/unify-login")
    Observable<Response<Void>> smartapply(@QueryMap Map<String, String> info);

    //    @FormUrlEncoded
//    @GET("index.php?web/webapi/unifyLogin")
    @GET("index.php?web/appapi/unifyLogin")
    Observable<Response<Void>> gmatl(@QueryMap Map<String, String> info);

    //    @FormUrlEncoded
    @GET("cn/app-api/unify-login")
    Observable<Response<Void>> gossip(@QueryMap Map<String, String> info);

//======================================================================================

    /**
     * 获取用户详细信息
     */
    @GET("cn/person/manage?data-type=json")
    Observable<ResultBean<UserData>> getUserDetailInfo();

    @FormUrlEncoded
    @POST("cn/app-api/change-nickname")
    Observable<ResultBean> modifyName(@Field("nickname") String nickName);

    @FormUrlEncoded
    @POST("cn/app-api/gossip-list")
    Observable<ResultBean<RemarkBean>> getRemarkList(@Field("page") String page, @Field("pageSize") String pageSize, @Field("belong") String belong);

    @FormUrlEncoded
    @POST("cn/app-api/post-list")
    Observable<ResultBean<List<CommunityData>>> getPostList(@Field("selectId") String selectId, @Field("page") String page, @Field("pageSize") String pageSize);


    @FormUrlEncoded
    @POST("cn/app-api/add-like")
    Observable<ResultBean> praiseOrCancel(@Field("gossipId") String id, @Field("belong") String belong);

    @FormUrlEncoded
    @POST("cn/app-api/reply")
    Observable<ResultBean> reply(@Field("content") String content, @Field("type") String type, @Field("id") String id,
                                 @Field("gossipUser") String gossipUser, @Field("uName") String uName,
                                 @Field("userImage") String userImage, @Field("replyUser") String replyUser,
                                 @Field("replyUserName") String replyUserName, @Field("belong") String belong);

    @FormUrlEncoded
    @POST("cn/app-api/gossip-details")
    Observable<RemarkData> getRemarkDetail(@Field("gossipId") String id);

    @FormUrlEncoded
    @POST("cn/app-api/post-details")
    Observable<CommunityData> getPostDeail(@Field("postId") String postId);

    @FormUrlEncoded
    @POST("cn/app-api/post-reply")
    Observable<ResultBean> postReply(@Field("postId") String postId, @Field("content") String content);

    @FormUrlEncoded
    @POST("cn/app-api/add-post")
    Observable<ResultBean> addPost(@Field("catId") String catId, @Field("title") String title, @Field("content") String content);

    @FormUrlEncoded
    @POST("cn/app-api/add-gossip")
    Observable<ResultBean> releaseStatus(@Field("title") String title, @Field("content") String content, @Field("image[]") List<String> list,
                                         @Field("video") List<String> video, @Field("audio") List<String> audio, @Field("icon") String icon,
                                         @Field("publisher") String publisher, @Field("belong") String belong);

    @Multipart
    @POST("cn/app-api/app-image")
    Observable<ResultBean> upload(@Part MultipartBody.Part file);

    @Multipart
    @POST("cn/api/app-image")
    Observable<ResultBean> replaceHeader(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("index.php?web/webapi/opinion")
    Observable<ResultBean> commitFeedBack(@Field("phone") String phone, @Field("opinion") String content);

    @FormUrlEncoded
    @POST("cn/app-api/update-user")
    Observable<ResultBean> modifyPwd(@Field("uid") String uid, @Field("oldPass") String oldPass, @Field("newPass") String newPass);

    @FormUrlEncoded
    @POST("cn/app-api/update-user")
    Observable<ResultBean> modifyPhone(@Field("uid") String uid, @Field("phone") String phone, @Field("code") String code);

    @FormUrlEncoded
    @POST("cn/app-api/update-user")
    Observable<ResultBean> modifyEmail(@Field("uid") String uid, @Field("email") String phone, @Field("code") String code);

    @GET("cn/api/grammar-learning")
    Observable<GrammarData> grammarLearn();

    @GET("cn/api/key-words")
    Observable<CoreData> keyWords();

    @GET("cn/api/pan-listens-practice")
    Observable<ListenPracticeData> listenPractice();

    @GET("cn/api/intensive-listening")
    Observable<ListenData> listen();


    @GET("cn/app-api/info-details")
    Observable<CommunityData> getPostDownDeail(@Query("id") int id);

    @FormUrlEncoded
    @POST("cn/api/task-next")
    Observable<ResultBean> taskNext(@Field("type") String type);

    @POST("cn/api/today-task")
    Observable<TodayData> todayTask();

    @POST("cn/app-api/is-user-replenish")
    Observable<InforData> userInfor();

    @GET("cn/class/index?data-type=json")
    Observable<PreLessonData> preLesson();

    @GET("cn/teacher/index?data-type=json")
    Observable<TeacherData> teacher();

    @GET("cn/class/details?data-type=json")
    Observable<ResultBean<LessonDetailBean>> lessonDetail(@Query("id") String id);

    @FormUrlEncoded
    @POST("cn/api/add-content")
    Observable<ResultBean> addContent(@Field("catId") int catId, @Field("name") String name, @Field("extend[]") String... extend);

    @FormUrlEncoded
    @POST("cn/app-api/suggestion")
    Observable<ResultBean> addContent(@Field("name") String name, @Field("phone") String phone, @Field("source") String source, @Field("message") String message, @Field("type") int type);
    @GET("cn/api/app-ad")
    Observable<AdvertisingData> getAdvertisingInfo();

    @FormUrlEncoded
    @POST("cn/api/independence")
    Observable<List<PracticeData>> independence(@Field("page") String page);

    @GET("cn/writing/details?data-type=json")
    Observable<WriteQuestionData> independenceDetail(@Query("id") String id);

    @FormUrlEncoded
    @POST("cn/api/save-writing")
    Observable<ResultBean> commitWrite(@Field("contentId") String id, @Field("answer") String content, @Field("belong") String type, @Field("time") String time);

    @FormUrlEncoded
    @POST("cn/api/writing-result")
    Observable<ResultData> writeResult(@Field("contentId") String id);

    @FormUrlEncoded
    @POST("cn/api/writing-tpo")
    Observable<List<WriteTpoData>> writeTpo(@Field("category") String category);

    @FormUrlEncoded
    @POST("cn/api/spoken-tpo")
    Observable<List<WriteTpoData>> speakTpo(@Field("category") String category);

    @FormUrlEncoded
    @POST("cn/api/change-gold")
    Observable<List<PracticeData>> goldSpeak(@Field("page") String page, @Field("pageSize") String pageSize);

    @GET("cn/spoken/details?data-type=json")
    Observable<SpeakQuestionData> speakDetail(@Query("id") String id);

    @GET("cn/spoken/spoken-practice?data-type=json")
    Observable<ResultBean> spokenUp(@Query("id") String id);

    @Multipart
    @POST("cn/api/spoken-upload")
    Observable<ResultBean> spokenUpToken(@Part("token") int token, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("cn/api/save-spoken")
    Observable<ResultBean> spokenSave(@Field("contentId") String contentId, @Field("answer") String answer, @Field("belong") String belong);

    @FormUrlEncoded
    @POST("cn/api/spoken-like")
    Observable<ResultBean> speakPraise(@Field("id") String id);

    @FormUrlEncoded
    @POST("cn/api/change-read-tpo")
    Observable<ReadData> readTpo(@Field("id") String id);

    @GET("cn/read/index?data-type=json")
    Observable<ReadOgData> readOg();

    @FormUrlEncoded
    @POST("cn/api/read-details")
    Observable<ResultBean<ReadQuestionData>> readQuestionDetail(@Field("id") String id);

    @FormUrlEncoded
    @POST("cn/api/save-read")
    Observable<ResultBean> saveRead(@Field("contentId") String contentId, @Field("pid") String pid,
                                    @Field("answer") String answer, @Field("trueAnswer") String trueAnswer,
                                    @Field("belong") String belong, @Field("time") String time);

    @FormUrlEncoded
    @POST("cn/api/app-read-result")
    Observable<ReadResultData> readResult(@Field("id") String id);

    @FormUrlEncoded
    @POST("cn/api/set-read")
    Observable<ResultBean> resetRead(@Field("id") String id);

    @FormUrlEncoded
    @POST("cn/api/read-page")
    Observable<ResultBean<List<ReadRecordData>>> readRecord(@Field("type") String type, @Field("page") String page, @Field("pageSize") String pageSize);

    @FormUrlEncoded
    @POST("cn/api/writing-page")
    Observable<ResultBean<List<WriteRecordData>>> writeRecord(@Field("type") String type, @Field("page") String page, @Field("pageSize") String pageSize);

    @GET("cn/heard/part-two?data-type=json")
    Observable<ListenSecTpoData> listenTpo(@Query("catId") String id, @Query("page") String page, @Query("pageSize") String pageSize);

    @GET("cn/heard/part-two?data-type=json")
    Observable<ListenQuestionData> listenTopicRequest(@Query("id") String id, @Query("isDel") String isDel);

    @FormUrlEncoded
    @POST("cn/api/tpo-cat-change")
    Observable<ListenSecTpoData> listenClassification(@Field("catId") String id, @Field("page") String page, @Field("pageSize") String pageSize);

    @FormUrlEncoded
    @POST("cn/api/check-answer")
    Observable<ResultBean> chechAnswer(@Field("contentId") String id, @Field("pid") String pid,
                                       @Field("belong") String belong, @Field("answer") String answer,
                                       @Field("app") String app);

    @FormUrlEncoded
    @POST("cn/api/delete-listen")
    Observable<ResultBean> deleteListen(@Field("id") String contentid);

    @GET("cn/heard/careful-listening?data-type=json")
    Observable<PracticeQuestionData> fineListen(@Query("id") String id);


    /**
     * 注册及修改密码初始化
     */
    @GET("cn/app-api/phone-request")
    Observable<JsonObject> phone_request();

    @GET("cn/person/news?data-type=json")
    Observable<ResultBean<List<MsgData>>> getMsg(@Query("type") String type, @Query("page") String page, @Query("pageSize") String pageSize);

    @FormUrlEncoded
    @POST("cn/api/vocab-list-page")
    Observable<ResultBean<List<GlossaryData>>> glossaryList(@Field("page") String page, @Field("pageSize") String pageSize);

    @FormUrlEncoded
    @POST("cn/api/get-words")
    Observable<ResultBean<List<GlossaryWordData>>> getGlossaryWords(@Field("keywords") String startTime, @Field("page") String page, @Field("pageSize") String pageSize);

    @FormUrlEncoded
    @POST("cn/api/practice-page")
    Observable<ListenRecordData> makeListenRecord(@Field("page") String page, @Field("pageSize") String pageSize);

    @FormUrlEncoded
    @POST("cn/api/listening-page")
    Observable<ListenRecordData> fineListenRecord(@Field("page") String page, @Field("pageSize") String pageSize);

    @FormUrlEncoded
    @POST("cn/api/dictation-page")
    Observable<ListenRecordData> simpleListenRecord(@Field("page") String page, @Field("pageSize") String pageSize);

    @FormUrlEncoded
    @POST("cn/api/spoken-page")
    Observable<ResultBean<List<SpeakShare>>> speakRecord(@Field("type") int type, @Field("page") int page, @Field("pageSize") String pageSize);

    @FormUrlEncoded
    @POST("cn/api/sub-report")
    Observable<ResultBean> questionReport(@Field("contentId") String contentId, @Field("description") String description, @Field("reportCat") String reportCat,
                                          @Field("reportType") String reportType, @Field("type") String type);
    @FormUrlEncoded
    @POST("cn/api/reference-page")
    Observable<ResultBean<List<DownloadData>>> MaryList(@Field("catId") int catId, @Field("page") int page);

    @FormUrlEncoded
    @POST("cn/app-api/post-list")
    Observable<ResultBean<List<DownloadData>>> HighList(@Field("selectId") int selectId, @Field("page") int page, @Field("pageSize") int pageSize);


    @GET("cn/app-api/reply-list")
    Observable<List<ReplyData>> replyList(@Query("uid") int uid);

    @GET("cn/app-api/class")
    Observable<MyLessonData> lessonList(@Query("page") int page);

    @GET("cn/wap-api/version")
    Observable<VersionInfo> getUpdate();

    @GET("cn/app-api/audition-course")
    Observable<List<FreeCursorData>> getFreeCursor();

    @GET("cn/app-api/my-knowledge")
    Observable<ListsData> getMyKnowLists(@Query("page") int page);

    //知识库 http://www.toeflonline.cn/cn/app-api/knowledge-list
    @POST("cn/app-api/knowledge")
    Observable<KnowMaxListData> getKnowBase();

    @FormUrlEncoded
    @POST("cn/app-api/knowledge-cate")
    Observable<KnowTypeData> getKnowType(@Field("id") int id);

    //, @Field("pageSize") int pageSize
    @FormUrlEncoded
    @POST("cn/app-api/knowledge-list-new")
    Observable<KnowZoneData> getKnowLists(@Field("id") int id, @Field("page") int page, @Field("pageSize") int pageSize);

    @FormUrlEncoded
    @POST("cn/app-api/knowledge-details")
    Observable<JsonRootBean> getKnowInfo(@Field("id") int id);

    @POST("cn/app-api/get-integral")//雷豆管理
    Observable<LeidouReData> getLeidou();

    @FormUrlEncoded
    @POST("cn/app-api/buy-knowledge")//雷豆付款
    Observable<PayData> getLeidouPay(@Field("id") int id, @Field("integral") int integral, @Field("type") int type);

    @FormUrlEncoded
    @POST("/pay/app-api/integral-pay")//下单方法
    Observable<PayDatas> getZfbPay(@Field("uid") int uid, @Field("money") String money);

    @FormUrlEncoded
    @POST("/pay/app-api/pay")//下单方法
    Observable<PayDatas> getOrderInfo(@Field("orderId") int orderId);

    @GET("cn/app-api/spoken-today")//今日口语批改
    Observable<TodayListData> getTodayList();

    @GET("cn/app-api/spoken-check-details")//查看全部
    Observable<AllTaskData> getPastAllList();

    @GET("cn/app-api/my-spoken")//只看我的
    Observable<OnlyMineData> getOnlyMineList();

    @FormUrlEncoded
    @POST("cn/app-api/sale-spoken-mark")//抢购kuyu名额
    Observable<PayDatas>  getPlaces(@Field("integral") int integral);

    @GET("cn/app-api/content-id")//上传获得id
    Observable<CorretData>  getVoiceId(@Query("catId") int uid);

    @GET("cn/app-api/writing-today")//今日作文批改
    Observable<TodayListData> getEaTodayList();

    @GET("/cn/app-api/writing-check")//查看全部 zuowen
    Observable<AllTaskData> getEaPastAllList();

    @GET("cn/app-api/my-writing")//只看我的
    Observable<OnlyMineData> getEaOnlyMineList();

    @GET("cn/app-api/daily-listening")//听丽丽
    Observable<AllInstListenData> getInstAll(@Query("page") int page);

    @Multipart
    @POST("cn/app-api/spoken-upload")//口语上传
    Observable<ResultBean> spokenUpTokens(@Part("token") int token, @Part MultipartBody.Part file);

    @Multipart
    @POST("cn/app-api/up-writing-pic")//作文上传
    Observable<ResultBean> spokenUpTokenss(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("cn/app-api/writing-mark")//抢购作文名额
    Observable<PayDatas>  getEaPlaces(@Field("integral") int integral);

    @FormUrlEncoded
    @POST("cn/app-api/data-updata")
    Observable<ResultBean> spokenSaves(@Field("contentId") int contentId, @Field("url") String answer);


    @GET("cn/app-api/listen-details")//趣味详情
    Observable<AllInstListenData> getQuListen(@Query("id") int id);

    @FormUrlEncoded
    @POST("cn/app-api/add-collect")//shoucang
    Observable<AllInstListenData>  getQuListenColl(@Field("collectType") int integral, @Field("num") int num, @Field("contentId") int contentId);

}
