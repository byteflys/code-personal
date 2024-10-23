package com.easing.commons.android.journal;

import com.easing.commons.android.code.Code;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.greendao.journal.JournalGreenDaoHandler;
import com.easing.commons.android.time.Times;

import org.greenrobot.greendao.annotation.Id;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
@SuppressWarnings("all")
public class Journal {

    @Id
    private Long recordId;

    public String type;
    public String time;
    public String data;

    public String type1;
    public String type2;
    public String type3;
    public String arg1;
    public String arg2;
    public String arg3;
    public String arg4;
    public String arg5;
    public String arg6;
    public String arg7;
    public String arg8;
    public String arg9;

    public Long timemillis;

    //保存日志记录
    public static void save(Journal journal) {
        JournalDao dao = JournalGreenDaoHandler.session.getJournalDao();
        journal.time = Times.now();
        journal.timemillis = Times.millisOfNow();
        dao.insert(journal);
    }

    //保存日志记录
    public static void save(String type, String data) {
        JournalDao dao = JournalGreenDaoHandler.session.getJournalDao();
        Journal journal = new Journal();
        journal.type = type;
        journal.data = data;
        journal.time = Times.now();
        journal.timemillis = Times.millisOfNow();
        dao.insert(journal);
    }

    //保存日志记录
    public static void save(String type, String data, Object... args) {
        JournalDao dao = JournalGreenDaoHandler.session.getJournalDao();
        Journal journal = new Journal();
        journal.type = type;
        journal.data = data;
        journal.time = Times.now();
        journal.timemillis = Times.millisOfNow();
        int count = 0;
        if (args.length > count) journal.arg1 = Texts.toString(args[count++]);
        if (args.length > count) journal.arg2 = Texts.toString(args[count++]);
        if (args.length > count) journal.arg3 = Texts.toString(args[count++]);
        if (args.length > count) journal.arg4 = Texts.toString(args[count++]);
        if (args.length > count) journal.arg5 = Texts.toString(args[count++]);
        if (args.length > count) journal.arg6 = Texts.toString(args[count++]);
        if (args.length > count) journal.arg7 = Texts.toString(args[count++]);
        if (args.length > count) journal.arg8 = Texts.toString(args[count++]);
        if (args.length > count) journal.arg9 = Texts.toString(args[count++]);
        Texts.toString(args);
        dao.insert(journal);
    }

    //保存日志记录
    public static <T> void save(Class<T> clazz, String data) {
        save(clazz.getSimpleName(), data);
    }

    //保存日志记录
    public static <T> void save(Class<T> clazz, String data, Object... args) {
        save(clazz.getSimpleName(), data, args);
    }

    //保存异常信息
    public static void save(Throwable e) {
        if (e instanceof SocketTimeoutException) return;
        if (e instanceof ConnectException) return;
        JournalDao dao = JournalGreenDaoHandler.session.getJournalDao();
        Journal journal = new Journal();
        journal.type = "exception";
        journal.data = Code.getExceptionDetail(e);
        journal.time = Times.now();
        journal.timemillis = Times.millisOfNow();
        dao.insert(journal);
    }

}
