package com.easing.commons.android.code;

import com.easing.commons.android.io.ProjectFile;
import com.easing.commons.android.time.Times;
import com.easing.commons.android.io.Files;

import java.util.Date;

//文件日志模块
//默认按小时保存日志文件，同一小时内的info和error保存在同一个文件里面
@SuppressWarnings("all")
public class Logger {

    //保存记录数据到文件
    public static void info(Object data) {
        Logger.info(data, null);
    }

    //保存记录数据到文件
    public static void info(Object data, String directory) {
        //数据
        String info = data.toString();
        String time = Times.formatDate(new Date(), "yyyy-MM-dd HH:mm");
        info = "# " + time + " #\n" + info + "\n# " + time + " #\n\n\n\n";
        //路径
        if (directory == null)
            directory = ProjectFile.getProjectInfoDirectory();
        else
            directory = ProjectFile.getProjectDirectory(directory);
        String name = Times.formatDate(new Date(), "yyyy-MM-dd-HH") + ".info";
        String file = directory + name;
        //保存
        Files.createFile(file);
        Files.appendToFile(file, info);
    }

    //保存错误信息到文件
    public static void error(Throwable e) {
        Logger.error(e, null);
    }

    //保存错误信息到文件
    public static void error(Object data) {
        Logger.error(data, null);
    }

    //保存错误信息到文件
    public static void error(Throwable e, String directory) {
        String detail = Code.getExceptionDetail(e);
        Logger.error(detail, directory);
    }

    //保存错误信息到文件
    public static void error(Object data, String directory) {
        //数据
        String info = data.toString();
        String time = Times.formatDate(new Date(), "yyyy-MM-dd HH:mm");
        info = "# " + time + " #\n" + info + "\n# " + time + " #\n\n\n\n";
        //路径
        if (directory == null)
            directory = ProjectFile.getProjectErrorDirectory();
        else
            directory = ProjectFile.getProjectDirectory(directory);
        String name = Times.formatDate(new Date(), "yyyy-MM-dd-hh") + ".error";
        String file = directory + name;
        //保存
        Files.createFile(file);
        Files.appendToFile(file, info);
    }
}
