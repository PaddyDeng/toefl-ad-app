package io.dcloud.H58E83894.http;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.dcloud.H58E83894.data.AdvertisingData;
import io.dcloud.H58E83894.data.GlossaryData;
import io.dcloud.H58E83894.data.GlossaryWordData;
import io.dcloud.H58E83894.data.ListenRecordData;
import io.dcloud.H58E83894.data.MsgData;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.circle.CommunityData;
import io.dcloud.H58E83894.data.circle.RemarkBean;
import io.dcloud.H58E83894.data.circle.RemarkData;
import io.dcloud.H58E83894.data.make.ListenPracticeData;
import io.dcloud.H58E83894.data.make.ListenQuestionData;
import io.dcloud.H58E83894.data.make.ListenSecTpoData;
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
import io.dcloud.H58E83894.data.make.GrammarData;
import io.dcloud.H58E83894.data.make.ListenData;
import io.dcloud.H58E83894.data.prelesson.FreeCursorData;
import io.dcloud.H58E83894.data.prelesson.LessonDetailBean;
import io.dcloud.H58E83894.data.prelesson.PreLessonData;
import io.dcloud.H58E83894.data.prelesson.TeacherData;
import io.dcloud.H58E83894.data.record.ReadRecordData;
import io.dcloud.H58E83894.data.record.WriteRecordData;
import io.dcloud.H58E83894.data.setting.VersionInfo;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.data.user.UserInfo;
import io.dcloud.H58E83894.utils.C;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;

public class HttpUtil {
    private HttpUtil() {
    }

    private static RestApi getRestApi(@HostType.HostTypeChecker int hostType) {
        return RetrofitProvider.getInstance(hostType).create(RestApi.class);
    }

    public static Observable<ResultBean> numGetAuthCode(String num, String type) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).numGetAuthCode(num, type)
                .compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> emailGetAuthCode(String email, String type) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).emailGetAuthCode(email, type).
                compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> register(String type, String registerStr, String pass, String code) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).register(type, registerStr, pass, code, "android_toefl", "2", "2")
                .compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<UserInfo> login(String name, String pwd) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).login(name, pwd);
    }

    public static Observable<ResultBean> retrievePwd(String type, String registerStr, String pwd, String code) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).retrievePwd(type, registerStr, pwd, code).
                compose(new SchedulerTransformer<ResultBean>());
    }

    //重置session
    //================================================================
    public static Observable<Response<Void>> toefl(Map<String, String> userInfo) {
//        return getRestApi(HostType.TOEFL_URL_HOST).toefl(uid, userName, password, email, phone).compose(new SchedulerTransformer<>());
        return getRestApi(HostType.TOEFL_URL_HOST).toefl(userInfo);
    }

    public static Observable<Response<Void>> smartapply(Map<String, String> userInfoe) {
        return getRestApi(HostType.SMARTAPPLY_URL_HOST).smartapply(userInfoe);
    }

    public static Observable<Response<Void>> gmatl(Map<String, String> userInfoee) {
        return getRestApi(HostType.BASE_URL_HOST).gmatl(userInfoee);
    }

    public static Observable<Response<Void>> gossip(Map<String, String> userInfoe) {
        return getRestApi(HostType.GOSSIP_URL_HOST).gossip(userInfoe);
    }
    //================================================================

    public static Observable<ResultBean<UserData>> getUserDetailInfo() {
        return getRestApi(HostType.TOEFL_URL_HOST).getUserDetailInfo();
    }

    public static Observable<ResultBean> modifyName(String nickName) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).modifyName(nickName).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean<RemarkBean>> getRemarkList(String page) {
        return getRestApi(HostType.GOSSIP_URL_HOST).getRemarkList(page, "10", "2").compose(new SchedulerTransformer<ResultBean<RemarkBean>>());
    }

    public static Observable<ResultBean<List<CommunityData>>> getPostList(String page) {
        return getRestApi(HostType.GOSSIP_URL_HOST).getPostList("2", page, C.PAGESIZE).compose(new SchedulerTransformer<ResultBean<List<CommunityData>>>());
    }

    public static Observable<ResultBean> praiseOrCancel(String id) {
        return getRestApi(HostType.GOSSIP_URL_HOST).praiseOrCancel(id, "1").compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> replyFloor(String content, String id, String uid, String uName, String photo, String replyUid, String replayUName) {
        return getRestApi(HostType.GOSSIP_URL_HOST).reply(content, "2", id, uid, uName, photo, replyUid, replayUName, "2").compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> reply(String content, String id, String gossipUser, String uName, String image) {
        return getRestApi(HostType.GOSSIP_URL_HOST).reply(content, "1", id, gossipUser, uName, image, "0", "", "2").compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<RemarkData> getRemarkDetail(String id) {
        return getRestApi(HostType.GOSSIP_URL_HOST).getRemarkDetail(id).compose(new SchedulerTransformer<RemarkData>());
    }

    public static Observable<CommunityData> getPostDeail(String postId) {
        return getRestApi(HostType.GOSSIP_URL_HOST).getPostDeail(postId).compose(new SchedulerTransformer<CommunityData>());
    }

    public static Observable<ResultBean> postReply(String postId, String content) {
        return getRestApi(HostType.GOSSIP_URL_HOST).postReply(postId, content).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> addPost(String title, String content) {
        return getRestApi(HostType.GOSSIP_URL_HOST).addPost("1", title, content).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> releaseStatus(String title, String content, List<String> images, String icon, String publisher) {
        return getRestApi(HostType.GOSSIP_URL_HOST).releaseStatus(title, content, images, new ArrayList<String>(), new ArrayList<String>(), icon, publisher, "2").compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> upload(MultipartBody.Part file) {
        return getRestApi(HostType.GOSSIP_URL_HOST).upload(file);
    }

    public static Observable<ResultBean> replaceHeader(MultipartBody.Part file) {
        return getRestApi(HostType.TOEFL_URL_HOST).replaceHeader(file).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> commitFeedBack(String phone, String content) {
        return getRestApi(HostType.BASE_URL_HOST).commitFeedBack(phone, content).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> modifyPwd(String uid, String oldPass, String newPass) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).modifyPwd(uid, oldPass, newPass).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> modifyPhone(String uid, String phone, String code) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).modifyPhone(uid, phone, code).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> modifyEmail(String uid, String email, String code) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).modifyEmail(uid, email, code).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<GrammarData> grammarLearn() {
        return getRestApi(HostType.TOEFL_URL_HOST).grammarLearn().compose(new SchedulerTransformer<GrammarData>());
    }

    public static Observable<CoreData> keyWords() {
        return getRestApi(HostType.TOEFL_URL_HOST).keyWords().compose(new SchedulerTransformer<CoreData>());
    }

    public static Observable<ListenPracticeData> listenPractice() {
        return getRestApi(HostType.TOEFL_URL_HOST).listenPractice().compose(new SchedulerTransformer<ListenPracticeData>());
    }

    public static Observable<ListenData> listen() {
        return getRestApi(HostType.TOEFL_URL_HOST).listen().compose(new SchedulerTransformer<ListenData>());
    }

    public static Observable<ResultBean> taskNext(String type) {
        return getRestApi(HostType.TOEFL_URL_HOST).taskNext(type).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<TodayData> todayTask() {
        return getRestApi(HostType.TOEFL_URL_HOST).todayTask().compose(new SchedulerTransformer<TodayData>());
    }

    public static Observable<PreLessonData> preLesson() {
        return getRestApi(HostType.TOEFL_URL_HOST).preLesson().compose(new SchedulerTransformer<PreLessonData>());
    }

    public static Observable<TeacherData> teacher() {
        return getRestApi(HostType.TOEFL_URL_HOST).teacher().compose(new SchedulerTransformer<TeacherData>());
    }

    public static Observable<ResultBean<LessonDetailBean>> lessonDetail(String id) {
        return getRestApi(HostType.TOEFL_URL_HOST).lessonDetail(id).compose(new SchedulerTransformer<ResultBean<LessonDetailBean>>());
    }

    public static Observable<ResultBean> addContent(String name, String... extend) {
        return getRestApi(HostType.SMARTAPPLY_URL_HOST).addContent(236, name, extend).compose(new SchedulerTransformer<ResultBean>());
    }

    //注册及修改密码初始化
    public static Observable<JsonObject> phone_request(){
        return getRestApi(HostType.LOGIN_REGIST_HOST).phone_request().compose(new SchedulerTransformer<JsonObject>());
    }

    public static Observable<AdvertisingData> getAdvertisingInfo() {
        return getRestApi(HostType.TOEFL_URL_HOST).getAdvertisingInfo().compose(new SchedulerTransformer<AdvertisingData>());
    }

    public static Observable<List<PracticeData>> independence(String page) {
        return getRestApi(HostType.TOEFL_URL_HOST).independence(page).compose(new SchedulerTransformer<List<PracticeData>>());
    }

    public static Observable<WriteQuestionData> independenceDetail(String id) {
        return getRestApi(HostType.TOEFL_URL_HOST).independenceDetail(id).compose(new SchedulerTransformer<WriteQuestionData>());
    }

    public static Observable<ResultBean> commitWrite(String id, String content, String belong, String useTime) {
        return getRestApi(HostType.TOEFL_URL_HOST).commitWrite(id, content, belong, useTime).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultData> writeResult(String id) {
        return getRestApi(HostType.TOEFL_URL_HOST).writeResult(id).compose(new SchedulerTransformer<ResultData>());
    }

    public static Observable<List<WriteTpoData>> writeTpo(String category) {
        return getRestApi(HostType.TOEFL_URL_HOST).writeTpo(category).compose(new SchedulerTransformer<List<WriteTpoData>>());
    }

    public static Observable<List<WriteTpoData>> speakTpo(String category) {
        return getRestApi(HostType.TOEFL_URL_HOST).speakTpo(category).compose(new SchedulerTransformer<List<WriteTpoData>>());
    }

    public static Observable<List<PracticeData>> goldSpeak(String page) {
        return getRestApi(HostType.TOEFL_URL_HOST).goldSpeak(page, C.PAGESIZE).compose(new SchedulerTransformer<List<PracticeData>>());
    }

    public static Observable<SpeakQuestionData> speakDetail(String id) {
        return getRestApi(HostType.TOEFL_URL_HOST).speakDetail(id).compose(new SchedulerTransformer<SpeakQuestionData>());
    }

    public static Observable<SpeakQuestionData> zipSpeakDetail(String id) {
        return getRestApi(HostType.TOEFL_URL_HOST).speakDetail(id);
    }

    public static Observable<ResultBean> speakPraise(String id) {
        return getRestApi(HostType.TOEFL_URL_HOST).speakPraise(id).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> spokenUp(String id) {
        return getRestApi(HostType.TOEFL_URL_HOST).spokenUp(id);
    }

    public static Observable<ResultBean> spokenUpToken(int token, MultipartBody.Part file) {
        return getRestApi(HostType.TOEFL_URL_HOST).spokenUpToken(token, file);
    }

    public static Observable<ResultBean> spokenSave(String contentId, String answer) {
        return getRestApi(HostType.TOEFL_URL_HOST).spokenSave(contentId, answer, "spoken");
    }

    public static Observable<ReadData> readTpo(String id) {
        return getRestApi(HostType.TOEFL_URL_HOST).readTpo(id).compose(new SchedulerTransformer<ReadData>());
    }

    public static Observable<ReadOgData> readOg() {
        return getRestApi(HostType.TOEFL_URL_HOST).readOg().compose(new SchedulerTransformer<ReadOgData>());
    }

    public static Observable<ResultBean<ReadQuestionData>> readQuestionDetail(String id) {
        return getRestApi(HostType.TOEFL_URL_HOST).readQuestionDetail(id).compose(new SchedulerTransformer<ResultBean<ReadQuestionData>>());
    }

    public static Observable<ResultBean<ReadQuestionData>> readNextQuestion(String nextId) {
        return getRestApi(HostType.TOEFL_URL_HOST).readQuestionDetail(nextId);
    }

    public static Observable<ResultBean> saveRead(String contentId, String pid, String answer, String trueAnswer, String belong, String time) {
        return getRestApi(HostType.TOEFL_URL_HOST).saveRead(contentId, pid, answer, trueAnswer, belong, time);
    }

    public static Observable<ReadResultData> readResult(String id) {
        return getRestApi(HostType.TOEFL_URL_HOST).readResult(id).compose(new SchedulerTransformer<ReadResultData>());
    }

    public static Observable<ResultBean> resetRead(String id) {
        return getRestApi(HostType.TOEFL_URL_HOST).resetRead(id).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean<List<ReadRecordData>>> readRecord(String type, String page) {
        return getRestApi(HostType.TOEFL_URL_HOST).readRecord(type, page, C.PAGESIZE).compose(new SchedulerTransformer<ResultBean<List<ReadRecordData>>>());
    }

    public static Observable<ResultBean<List<WriteRecordData>>> writeRecord(String type, String page) {
        return getRestApi(HostType.TOEFL_URL_HOST).writeRecord(type, page, C.PAGESIZE).compose(new SchedulerTransformer<ResultBean<List<WriteRecordData>>>());
    }

    public static Observable<ListenSecTpoData> listenTpo(String id, String page) {
        return getRestApi(HostType.TOEFL_URL_HOST).listenTpo(id, page, C.PAGESIZE).compose(new SchedulerTransformer<ListenSecTpoData>());
    }

    public static Observable<ListenQuestionData> listenTopicRequest(String id) {
        return getRestApi(HostType.TOEFL_URL_HOST).listenTopicRequest(id, "1").compose(new SchedulerTransformer<ListenQuestionData>());
    }

    public static Observable<ListenSecTpoData> listenClassification(String id, String page) {
        return getRestApi(HostType.TOEFL_URL_HOST).listenClassification(id, page, C.PAGESIZE).compose(new SchedulerTransformer<ListenSecTpoData>());
    }

    public static Observable<ResultBean> chechAnswer(String id, String pid, String userAnswer) {
        return getRestApi(HostType.TOEFL_URL_HOST).chechAnswer(id, pid, "practise", userAnswer, "1").compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> deleteListen(String id) {
        return getRestApi(HostType.TOEFL_URL_HOST).deleteListen(id).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<PracticeQuestionData> fineListen(String id) {
        return getRestApi(HostType.TOEFL_URL_HOST).fineListen(id).compose(new SchedulerTransformer<PracticeQuestionData>());
    }

    public static Observable<ResultBean<List<MsgData>>> getMsg(String type, String page) {
        return getRestApi(HostType.TOEFL_URL_HOST).getMsg(type, page, C.PAGESIZE).compose(new SchedulerTransformer<ResultBean<List<MsgData>>>());
    }

    public static Observable<ResultBean<List<GlossaryData>>> glossaryList(String page) {
        return getRestApi(HostType.TOEFL_URL_HOST).glossaryList(page, C.PAGESIZE).compose(new SchedulerTransformer<ResultBean<List<GlossaryData>>>());
    }

    public static Observable<ResultBean<List<GlossaryWordData>>> getGlossaryWords(String startTime, String page) {
        return getRestApi(HostType.TOEFL_URL_HOST).getGlossaryWords(startTime, page, C.PAGESIZE).compose(new SchedulerTransformer<ResultBean<List<GlossaryWordData>>>());
    }

    public static Observable<ListenRecordData> makeListenRecord(String page) {
        return getRestApi(HostType.TOEFL_URL_HOST).makeListenRecord(page, C.PAGESIZE).compose(new SchedulerTransformer<ListenRecordData>());
    }

    public static Observable<ListenRecordData> fineListenRecord(String page) {
        return getRestApi(HostType.TOEFL_URL_HOST).fineListenRecord(page, C.PAGESIZE).compose(new SchedulerTransformer<ListenRecordData>());
    }

    public static Observable<ListenRecordData> simpleListenRecord(String page) {
        return getRestApi(HostType.TOEFL_URL_HOST).simpleListenRecord(page, C.PAGESIZE).compose(new SchedulerTransformer<ListenRecordData>());
    }

    public static Observable<ResultBean<List<SpeakShare>>> speakRecord(int type, int page) {
        return getRestApi(HostType.TOEFL_URL_HOST).speakRecord(type, page, C.PAGESIZE).compose(new SchedulerTransformer<ResultBean<List<SpeakShare>>>());
    }

    public static Observable<ResultBean> questionReport(String id, String description) {
        return getRestApi(HostType.TOEFL_URL_HOST).questionReport(id, description, "3", "3", "1").compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> lessonList(int page) {
        return getRestApi(HostType.TOEFL_URL_HOST).lessonList(page, C.PAGESIZE).compose(new SchedulerTransformer<ResultBean>());
    }

    //版本更新
    public static Observable<VersionInfo> getUpdate() {
        return getRestApi(HostType.TOEFL_URL_HOST).getUpdate().compose(new SchedulerTransformer<VersionInfo>());
    }

    public static Observable<List<FreeCursorData>> getFreeCursor() {
        return getRestApi(HostType.TOEFL_URL_HOST).getFreeCursor().compose(new SchedulerTransformer<List<FreeCursorData>>());
    }
}
