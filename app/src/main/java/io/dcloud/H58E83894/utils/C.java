package io.dcloud.H58E83894.utils;

public interface C {

    String PAGESIZE = "15";
    String MULT_CHOOSE = "mult_choose";

    int CONT_GUIDE_IMG = 1;

    //随机数类型
    int TRIAL_COURSE = 1;

    int TYPE_SPEAK_GLOD = 10;
    int TYPE_SPEAK_TPO = TYPE_SPEAK_GLOD + 1;

    int TYPE_TITLE = 1;
    int TYPE_DES = TYPE_TITLE + 1;

    String BELONG_INDENPDENT = "writingIndependent";
    String BELONG_TPO = "writingTpo";

    //随机存储数标识  免费试听
    String FREE_SC = "free_sc";
    String FREE_RC = "free_rc";
    String FREE_CR = "free_cr";
    String FREE_MATH = "free_math";

    //首页试听链接,与试听列表
//    String TRIAL_SC = "http://bjsy.gensee.com/training/site/v/47958422?nickname=listen";
//    String TRIAL_RC = "http://bjsy.gensee.com/training/site/v/99630114?nickname=speak";
//    String TRIAL_CR = "http://bjsy.gensee.com/training/site/v/76246022?nickname=read";
//    String TRIAL_MATH = "http://bjsy.gensee.com/training/site/v/42305149?nickname=write";

    //    http://p.qiao.baidu.com/im/index?siteid=7905926&ucid=18329536&cp=&cr=&cw=
    //    要把 header 里面的 Referer 设置为 http://www.gmatonline.cn/
    int DEAL_ADD_HEADER = 1;
    int DEAL_GO_MAIN = DEAL_ADD_HEADER + 1;

    String DEAL_TYPE = "deal_type";//

    //用于首页进入详情type
    int TYPE_PUBLIC_LESSON = 1;
    int TYPE_HOT_LESSON = TYPE_PUBLIC_LESSON + 1;
    int TYPE_ONLINE_LESSON = TYPE_HOT_LESSON + 1;

    //自动登录涉及到的int
    int RESPONSE_FAIL = 2;//响应失败
    int RESPONSE_SUCCESS = RESPONSE_FAIL + 1;//响应数据成功
    int AUTO_LOGIN_SUCCESS = RESPONSE_SUCCESS + 1;//自动登录成功
    int AUTO_LOGIN_FAIL = AUTO_LOGIN_SUCCESS + 1;//自动登录失败
    int NO_LOGIN = AUTO_LOGIN_FAIL + 1;//未登录

    int REQUST_CODE_UPDATE = 100;

    //请求数据成功
    int REQUEST_RESULT_SUCCESS = 1;
    //登录请求
    int REQUEST_LOGIN = REQUEST_RESULT_SUCCESS + 1;
    //公共请求
    int COM_REQUEST_CODE = REQUEST_LOGIN + 1;
    //核心词汇
    int CORE_REQUEST_CODE = COM_REQUEST_CODE + 1;
    //提交答案
    int REQUEST_COMMIT_ANSWER = CORE_REQUEST_CODE + 1;

    String LOGIN_INFO = "login_info";//登录成功，退出登录
    String MODIFY_INFO = "modify_info";//用户信息修改
    String MAKE_LISTEN_REFRESH = "make_listen_refresh";//今日听力做题ui
    String MAKE_CORE_REFRESH = "make_core_refresh";//今日核心词汇做题ui
    String MAKE_FINE_LISTEN_REFRESH = "make_fine_listen_refresh";//泛听练习做题ui
    String REFRESH_WRITE_LIST = "refresh_write_list";//刷新写作练习列表
    String REFRESH_WRITE_TPO_LIST = "refresh_write_tpo_list";//刷新写作TPO练习列表
    String REFRESH_READ_LIST = "refresh_read_list";//刷新阅读list
    String REFRESH_LISTEN_LIST = "refresh_listen_list";//刷新听力列表
    String MAKE_GRAMMAR = "make_grammar";//刷新语法练习后的ui
    String TOEFL_CIRCLE_POST_REMARK = "toefl_circle_post_remark";
    String TOEFL_CIRCLE_POST_COMMUNITY = "toefl_circle_post_community";

    int MAKEING = 1;
    int MAKE_END = 2;

    int MODIFY_NICKNAME = 1;

    //获取验证码
    String REGISTER_TYPE = "1";//注册
    String RETRIEVE_TYPE = "2";//找回密码
    String MODIFY_INFO_TYPE = "3";//修改用户信息

    String ACCOUNT_PASSWORD_ISEMPTY = "account_password_is_empty";

    int DEFALUT = 0;
    int CORRECT = DEFALUT + 1;
    int ERROR = CORRECT + 1;

    String GRAMMAR_LEARNING = "grammarLearning";
    String CORE_WORD = "keyWords";
    String LISTEN = "intensiveListening";
    String FINE_LISTEN = "panListensPractice";//泛听练习

    String READ_BELONG_TPO = "readTpo";
    String READ_BELONG_OG = "readog";
}
