package io.dcloud.H58E83894.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.dcloud.H58E83894.ToeflApplication;
import io.dcloud.H58E83894.data.RandomData;
import io.dcloud.H58E83894.data.make.GrammarRecordData;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.TimeUtils;

/**
 * Created by fire on 2017/7/18  14:47.
 */

public class RecordManager {
    private static RecordDbHelper mDBHelper = null;
    private static RecordManager instance;
    private static SQLiteDatabase writableDatabase;
    private static SQLiteDatabase readableDatabase;

    public static RecordManager getInstance() {
        if (null == instance) {
            synchronized (RecordManager.class) {
                if (null == instance) {
                    instance = new RecordManager();
                }
            }
        }
        return instance;
    }

    private RecordManager() {
        if (mDBHelper == null) {
            mDBHelper = new RecordDbHelper(ToeflApplication.getInstance());
            writableDatabase = mDBHelper.getWritableDatabase();
            readableDatabase = mDBHelper.getReadableDatabase();
        }
    }
    //===========================随机数=====================================

    public RandomData queryNumber(String courseId, int type) {
        if (type == C.TRIAL_COURSE) {
            return queryFreeRandomNumber(courseId, type);
        }
        return null;
    }

    private RandomData queryFreeRandomNumber(String courseId, int type) {//试听课随机数
        RandomData randomData = queryRandomNumber(courseId, type);
        int baseNum = 0;
        int incrementNum = 103;
        if (TextUtils.equals(courseId, C.FREE_SC)) {
            baseNum = 1300;
        } else if (TextUtils.equals(courseId, C.FREE_RC)) {
            baseNum = 1125;
        } else if (TextUtils.equals(courseId, C.FREE_CR)) {
            baseNum = 1562;
        } else if (TextUtils.equals(courseId, C.FREE_MATH)) {
            baseNum = 1023;
        }
        incrementNumNewRule(randomData, baseNum, incrementNum, courseId);
        //存储或更新数据库
        insertOrUpdateData(randomData, courseId, type);
        return randomData;
    }

    /**
     * 新的自增规则
     * APP抢先学课，按照日期固定增长：从6.20开始。
     * 4个免费试听课程：6.20基数分别为1300、1125、1562、1023，每日固定增长103，以此类推。
     * 4个入门课程：6.20基数3300、4300、3100、2890，每日增长104，以此类推。
     * 3个本月热门课程：6.20基数203、335、289，每日增长27，以此类推。
     * 人在雷哥网备考：6.20基数39351，每日增长128，比如6.21即为39479，以此类推。
     * 专家讲师和公开课不统一了。。。
     */
    private void incrementNumNewRule(RandomData randomData, int baseNum, int incrementTimes, String courseId) {
        int times = randomData.getTimes();//已有的数

        Date date = new Date();
        //判读是否是同一天
        String newDate = TimeUtils.longToString(date.getTime(), "yyyy年MM月dd日");
        if (!TextUtils.equals(randomData.getDate(), newDate)) {//XXXX年XX月XX日 旧数据日期存储格式
            //不是同一天，根据天数差，自增
            randomData.setDate(newDate);
            long baseDate = TimeUtils.stringToLong("2017年06月20日", "yyyy年MM月dd日");
            int diff = TimeUtils.dateLess(baseDate, date.getTime());
            //日期不相同，另算日期
            times = baseNum;
            times = times + incrementTimes * diff;
        } /*else {
            //是同一天，不增加
        }*/
        randomData.setTimes(times);
    }


    public void insertOrUpdateData(RandomData randomData, String courseId, int type) {
        ContentValues values = new ContentValues();
        values.put(MakeTable.TIMES, randomData.getTimes());
        values.put(MakeTable.DATE, randomData.getDate());
        values.put(MakeTable.COURSEID, courseId);
        values.put(MakeTable.TYPE, type);
        int update = writableDatabase.update(MakeTable.RANDOM_NAME, values, MakeTable.COURSEID + "=? and " + MakeTable.TYPE + "=?",
                new String[]{courseId, String.valueOf(type)});
        if (update != 1) {
            writableDatabase.insert(MakeTable.RANDOM_NAME, null, values);
        }
    }

    private RandomData queryRandomNumber(String courseId, int type) {//试听课随机数
        RandomData randomData = new RandomData();
        Cursor cursor = readableDatabase.query(MakeTable.RANDOM_NAME, null, MakeTable.COURSEID + "=? and " + MakeTable.TYPE + "=?",
                new String[]{courseId, String.valueOf(type)}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                randomData.setTimes(cursor.getInt(cursor.getColumnIndex(MakeTable.TIMES)));
                randomData.setDate(cursor.getString(cursor.getColumnIndex(MakeTable.DATE)));
            }
            cursor.close();
        }
        return randomData;
    }

    //===========================随机数=====================================

    public void deleteNoToday() {
//        String date = TimeUtils.longToString(new Date().getTime(), "yyyy-MM-dd");
//        String date = "2017-07-19";
//        writableDatabase.delete(MakeTable.TABLE_NAME, MakeTable.MAKE_DATE + " not like ?", new String[]{date});
    }

    public List<GrammarRecordData> queryMakeGrammar() {
        return queryMakeGrammar(MakeTable.TABLE_NAME);
    }

    public List<GrammarRecordData> queryCore() {
        return queryMakeGrammar(MakeTable.CORE_TABLE_NAME);
    }

    public List<GrammarRecordData> queryMakeGrammar(String tableName) {
        List<GrammarRecordData> list = new ArrayList<>();
        int uid = MakeTable.DEFAULT_UID;
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            uid = GlobalUser.getInstance().getUserData().getUid();
        }
        String date = TimeUtils.longToString(new Date().getTime(), "yyyy-MM-dd");
        Cursor cursor = readableDatabase.query(tableName, new String[]{MakeTable.GRAMMAR_JSON, MakeTable.YOU_RESULT}, MakeTable.USER_UID + " = ? and " +
                        MakeTable.MAKE_DATE + " = ?",
                new String[]{String.valueOf(uid), date}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                GrammarRecordData data = new GrammarRecordData();
                data.setJson(cursor.getString(cursor.getColumnIndex(MakeTable.GRAMMAR_JSON)));
                data.setUserAnswer(cursor.getString(cursor.getColumnIndex(MakeTable.YOU_RESULT)));
                list.add(data);
            }
            cursor.close();
        }
        return list;
    }

    public int queryMakeNum() {
        return queryMakeNum(MakeTable.TABLE_NAME);
    }

    public int queryCoreNum() {
        return queryMakeNum(MakeTable.CORE_TABLE_NAME);
    }


    public int queryMakeNum(String tableName) {
        int uid = MakeTable.DEFAULT_UID;
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            uid = GlobalUser.getInstance().getUserData().getUid();
        }
        String date = TimeUtils.longToString(new Date().getTime(), "yyyy-MM-dd");
        Cursor cursor = readableDatabase.query(tableName, new String[]{MakeTable.GRAMMAR_ID}, MakeTable.USER_UID + " = ? and " +
                        MakeTable.MAKE_DATE + " = ?",
                new String[]{String.valueOf(uid), date}, null, null, null);
        int size = 0;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                size++;
            }
            cursor.close();
        }
        return size;
    }

    public void insertGrammarData(ContentValues values) {
//        String insertQueId = (String) values.get(MakeTable.GRAMMAR_ID);
//        int userUid = (int) values.get(MakeTable.USER_UID);
//        int update = writableDatabase.update(MakeTable.TABLE_NAME, values, MakeTable.GRAMMAR_ID + "=? and " +
//                MakeTable.USER_UID + "=?", new String[]{insertQueId, String.valueOf(userUid)});
//        if (update == 0) {
//            writableDatabase.insert(MakeTable.TABLE_NAME, null, values);
//        }
        insertData(values, MakeTable.TABLE_NAME);
    }

    public void insertCoreData(ContentValues values) {
        insertData(values, MakeTable.CORE_TABLE_NAME);
    }

    public void insertData(ContentValues values, String tableName) {
        String insertQueId = (String) values.get(MakeTable.GRAMMAR_ID);
        int userUid = (int) values.get(MakeTable.USER_UID);
        int update = writableDatabase.update(tableName, values, MakeTable.GRAMMAR_ID + " =? and " +
                MakeTable.USER_UID + " =?", new String[]{insertQueId, String.valueOf(userUid)});
        if (update == 0) {
            writableDatabase.insert(tableName, null, values);
        }
    }
}
