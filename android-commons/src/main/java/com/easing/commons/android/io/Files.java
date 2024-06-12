package com.easing.commons.android.io;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.functional.Filter;
import com.easing.commons.android.data.Result;
import com.easing.commons.android.manager.Uris;
import com.easing.commons.android.media.MediaType;
import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.time.Times;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class Files {

    public static boolean exist(String path) {
        return new File(path).exists();
    }

    public static boolean isFileSystem(String path) {
        try {
            new File(path).getName();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //判断文件是普通文件
    public static boolean isFile(String path) {
        if (!Files.exist(path))
            Files.createFile(path);
        return new File(path).isFile();
    }

    //判断文件是目录
    public static boolean isDirectory(String path) {
        if (!Files.exist(path))
            Files.createDirectory(path);
        return new File(path).isDirectory();
    }

    //获得文件名，包括后缀
    public static String getFileName(String path) {
        return new File(path).getName().toLowerCase();
    }

    //获得文件名，不包括后缀
    public static String getFileNameWithoutSuffix(String path) {
        String fileName = getFileName(path);
        int index = fileName.lastIndexOf('.');
        if (index >= 0)
            fileName = fileName.substring(0, index);
        return fileName;
    }

    public static boolean isSameFile(String f1, String f2) {
        return new File(f1).equals(new File(f2));
    }

    public static boolean isSameFile(File f1, File f2) {
        return f1.equals(f2);
    }

    public static boolean isAncestorFile(String parent, String child) {
        while (child != null) {
            if (Files.isSameFile(parent, child))
                return true;
            child = new File(child).getParent();
        }
        return false;
    }

    public static boolean isParentFile(String parent, String child) {
        return Files.isParentFile(new File(parent), new File(child));
    }

    public static boolean isParentFile(File parent, File child) {
        return Files.isSameFile(parent, child.getParentFile());
    }

    @SneakyThrows
    public static String createFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory())
            Files.deleteFile(file);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        if (!file.exists())
            file.createNewFile();
        return file.getAbsolutePath().toLowerCase();
    }

    //创建目录
    @SneakyThrows
    public static File createDirectory(String path) {
        File directory = new File(path);
        if (directory.exists() && !directory.isDirectory())
            Files.deleteFile(directory);
        if (!directory.exists())
            directory.mkdirs();
        return directory;
    }

    @SneakyThrows
    public static File file(String path) {
        return new File(path);
    }

    /**
     * 删除文件
     **/
    @SneakyThrows
    public static void deleteFile(String file) {
        Files.deleteFile(new File(file));
    }

    /**
     * 删除文件
     **/
    @SneakyThrows
    public static void deleteFile(File file) {
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            File[] child_files = file.listFiles();
            for (File child_file : child_files)
                Files.deleteFile(child_file.getAbsolutePath());
        }
        file.delete();
    }

    //清空文件夹
    public static void clearFolder(String path) {
        Files.deleteFile(path);
        Files.createDirectory(path);
    }

    //复制文件
    //target是要复制到的文件路径
    @SneakyThrows
    public static String copyToFile(String source, String target) {
        String parent = Files.file(target).getParent();
        //先拷贝到同一目录下，再重命名
        File firstPath = Files.copyToFolder(source, parent);
        Files.renameFile(firstPath.getAbsolutePath(), target);
        return target;
    }

    //复制文件
    //target是存放拷贝文件的目录
    @SneakyThrows
    public static File copyToFolder(String source, String target) {
        return copyToFolder(new File(source), new File(target));
    }

    //复制文件
    //target是存放拷贝文件的目录
    @SneakyThrows
    public static File copyToFolder(File source, File target) {
        if (!target.exists())
            target.mkdirs();

        if (source.isDirectory()) {
            target = new File(target, source.getName());
            target.mkdir();
            if (Files.isSameFile(source, target))
                return target;

            File[] child_files = source.listFiles();
            for (File old_file : child_files) {
                File new_file = new File(target, old_file.getName());
                Files.copyToFolder(old_file, target);
            }
        } else {
            target = new File(target, source.getName());
            target.createNewFile();
            if (Files.isSameFile(source, target))
                return target;

            FileInputStream fis = new FileInputStream(source);
            BufferedInputStream bis = new BufferedInputStream(fis);
            FileOutputStream fos = new FileOutputStream(target);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[1024 * 1024];
            int len;
            while ((len = bis.read(buffer)) != -1)
                bos.write(buffer, 0, len);
            bos.flush();

            bis.close();
            bos.close();
            fis.close();
            fos.close();
        }
        return target;
    }

    /**
     * 复制文件结构，但不复制数据
     **/
    public static void copyFileWithNodata(File source, File target) throws Exception {
        if (!target.getParentFile().exists())
            target.getParentFile().mkdirs();

        if (source.isDirectory()) {
            if (target.isFile())
                Files.deleteFile(target.getAbsolutePath());
            if (!target.exists())
                target.mkdirs();

            File[] child_files = source.listFiles();
            for (File old_file : child_files) {
                File new_file = new File(target, old_file.getName());
                Files.copyToFolder(old_file, new_file);
            }
        } else {
            Files.deleteFile(target.getAbsolutePath());
            target.createNewFile();
        }
    }

    /**
     * 剪切文件
     **/
    public static void cutFile(String source, String target) {
        Files.cutFile(new File(source), new File(target));
    }

    /**
     * 剪切文件
     **/
    @SneakyThrows
    public static void cutFile(File source, File target) {
        Files.copyToFolder(source, target);
        Files.deleteFile(source.getAbsolutePath());
    }

    //重命名文件
    @SneakyThrows
    public static void renameFile(String source, String target) {
        Files.renameFile(new File(source), new File(target));
    }

    //重命名文件
    @SneakyThrows
    public static void renameFile(File source, File target) {
        source.renameTo(target);
    }

    //获取文件扩展名
    public static String getExtensionName(String file) {
        return Files.getExtensionName(new File(file));
    }

    //获取文件扩展名
    public static String getExtensionName(File file) {
        String name = file.getName().toLowerCase();
        if (name.lastIndexOf('.') == -1)
            return "";
        else
            return name.substring(name.lastIndexOf('.') + 1);
    }

    //获取文件类型
    public static String getFileType(String file) {
        if (MediaType.isText(file))
            return "text";
        if (MediaType.isImage(file))
            return "image";
        if (MediaType.isAudio(file))
            return "audio";
        if (MediaType.isVideo(file))
            return "video";
        if (MediaType.isDocument(file))
            return "doc";
        if (MediaType.isXls(file))
            return "xls";
        if (MediaType.isPdf(file))
            return "pdf";
        return "other";


    }

    public static List<String> sortFiles(List<String> files) {
        Collections.sort(files, (left, right) -> {
            String left_name = left.toLowerCase();
            String right_name = right.toLowerCase();

            if (new File(left).isDirectory() && new File(right).isFile())
                return -1;

            if (new File(left).isFile() && new File(right).isDirectory())
                return 1;

            if (left_name.startsWith("#") && !right_name.startsWith("#"))
                return -1;

            if (left_name.startsWith("_") && !right_name.startsWith("_"))
                return -1;

            if (!left_name.startsWith(".") && right_name.startsWith("."))
                return -1;

            if (!left_name.startsWith("#") && right_name.startsWith("#"))
                return 1;

            if (!left_name.startsWith("_") && right_name.startsWith("_"))
                return 1;

            if (left_name.startsWith(".") && !right_name.startsWith("."))
                return 1;

            return left_name.compareTo(right_name);
        });
        return files;
    }

    /**
     * 对子文件进行排序
     **/
    public static List<File> listSortedChildFile(File dir) {
        if (!dir.exists() || !dir.isDirectory())
            throw new RuntimeException("dir not exists, or not a directory");

        List<File> child_files = Arrays.asList(dir.listFiles());
        Collections.sort(child_files, (left, right) -> {
            String left_name = left.getAbsolutePath().toLowerCase();
            String right_name = right.getAbsolutePath().toLowerCase();

            if (left.isDirectory() && right.isFile())
                return -1;

            if (left.isFile() && right.isDirectory())
                return 1;

            if (left_name.startsWith("#") && !right_name.startsWith("#"))
                return -1;

            if (left_name.startsWith("_") && !right_name.startsWith("_"))
                return -1;

            if (!left_name.startsWith(".") && right_name.startsWith("."))
                return -1;

            if (!left_name.startsWith("#") && right_name.startsWith("#"))
                return 1;

            if (!left_name.startsWith("_") && right_name.startsWith("_"))
                return 1;

            if (left_name.startsWith(".") && !right_name.startsWith("."))
                return 1;

            return left_name.compareTo(right_name);
        });
        return child_files;
    }

    public static List<String> getChildNames(String dir) {
        List<File> child_files = Files.listSortedChildFile(new File(dir));
        List<String> child_names = Collections.emptyList();
        for (File file : child_files)
            child_names.add(file.getName());
        return child_names;
    }

    //递归查找所有子目录下的文件
    public static List<String> findChildFileRecursively(String parent) {
        List<String> sum = new LinkedList();
        findChildFileRecursively(sum, new File(parent));
        return sum;
    }

    //递归查找所有子目录下的文件
    public static void findChildFileRecursively(List<String> sum, File parent) {
        if (!parent.exists() || !parent.isDirectory()) return;
        for (File file : parent.listFiles()) {
            if (file.isDirectory())
                findChildFileRecursively(sum, file);
            else
                sum.add(file.getAbsolutePath());
        }
    }

    /**
     * 从文件输入流中读取字符串
     **/
    @SneakyThrows
    public static String streamToString(FileInputStream fis, String encode) {
        if (encode == null)
            encode = "UTF-8";
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        Streams.close(fis, null);
        return new String(buffer, encode);
    }

    //读取文件内容
    @SneakyThrows
    public static String readStringFromFile(String file) {
        return streamToString(new FileInputStream(file), null);
    }

    /**
     * 按行读入文件中的字符串
     **/
    @SneakyThrows
    public static List<String> readLines(File file) {
        ArrayList list = new ArrayList();

        FileReader reader = new FileReader(file);
        BufferedReader bf = new BufferedReader(reader);
        String line = bf.readLine();
        while (line != null) {
            list.add(line);
            bf.readLine();
        }
        bf.close();
        return list;
    }

    //按行读取需要的字符串，检查通过，则返回该行
    @SneakyThrows
    public static String readLine(File file, Filter<String> filter) {
        FileReader reader = new FileReader(file);
        BufferedReader bf = new BufferedReader(reader);
        String line = bf.readLine();
        while (line != null) {
            if (filter.keep(line))
                break;
            else
                bf.readLine();
        }
        bf.close();
        reader.close();
        return line;
    }

    //将字节写入文件指定位置
    @SneakyThrows
    public static void randomWrite(String file, byte[] bytes, long length, long offset) {
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.setLength(length);
        raf.seek(offset);
        raf.write(bytes);
        raf.close();
    }

    //将字节集写入文件
    @SneakyThrows
    public static void writeToFile(String path, byte[] bytes) {
        Files.createFile(path);
        FileOutputStream os = new FileOutputStream(path);
        os.write(bytes);
        os.flush();
        os.close();
    }

    //将输入流写入文件
    @SneakyThrows
    public static void writeToFile(String path, InputStream is) {
        Files.createFile(path);
        FileOutputStream os = new FileOutputStream(path);
        byte[] buffer = new byte[1024 * 1024];
        for (int len = -1; is.read(buffer) != -1; )
            os.write(buffer, 0, len);
        os.flush();
        os.close();
        is.close();
    }

    //将字符串写入文件
    @SneakyThrows
    public static void writeToFile(File file, String content) {
        Files.createFile(file.getAbsolutePath());
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    //将字符串写入文件
    @SneakyThrows
    public static void writeToFile(String file, String content) {
        Files.createFile(file);
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    //将字符串追加到文件结尾
    @SneakyThrows
    public static void appendToFile(String path, String content) {
        File file = new File(path);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(file.length());
        raf.write(content.getBytes(StandardCharsets.UTF_8));
        raf.close();
    }

    //将字符串追加到文件结尾
    @SneakyThrows
    public static void appendToFile(String path, byte[] bytes) {
        File file = new File(path);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(file.length());
        raf.write(bytes);
        raf.close();
    }

    public static List<String> listChildFiles(String dir) {
        List<String> paths = new ArrayList();
        if (new File(dir).listFiles() != null)
            for (File file : new File(dir).listFiles())
                paths.add(file.getPath());
        return paths;
    }

    public static List<String> listSortedChildFiles(String dir) {
        List<File> files = Files.listSortedChildFile(new File(dir));
        List<String> paths = new ArrayList();
        for (File file : files)
            paths.add(file.getPath());
        return paths;
    }

    public static List<String> listVisibleFiles(String dir) {
        List<File> files = Files.listSortedChildFile(new File(dir));
        List<String> paths = new ArrayList();
        for (File file : files)
            if (!file.isHidden())
                paths.add(file.getPath());
        return paths;
    }

    public static List<String> listAndroidPictures(String dir) {
        List<String> paths = Files.listChildFiles(dir);
        List<String> filter_paths = new ArrayList();
        for (String path : paths)
            if (path.indexOf('.') >= 0) {
                int index = path.lastIndexOf('.');
                String suffix = path.substring(index).toLowerCase();
                if (suffix.equals(".jpg") || suffix.equals(".jpeg") || suffix.equals(".bmp") || suffix.equals(".png") || suffix.equals(".gif"))
                    filter_paths.add(path);
            }
        return filter_paths;
    }

    public static List<String> listAndroidAudios(String dir) {
        List<String> paths = Files.listChildFiles(dir);
        List<String> filteredPaths = new ArrayList();
        for (String path : paths)
            if (path.indexOf('.') >= 0) {
                int index = path.lastIndexOf('.');
                String suffix = path.substring(index).toLowerCase();
                if (suffix.equals(".mp3") || suffix.equals(".wav") || suffix.equals(".ogg") || suffix.equals(".aac") || suffix.equals(".flac"))
                    filteredPaths.add(path);
            }
        return filteredPaths;
    }

    public static List<String> listAndroidVideos(String dir) {
        List<String> paths = Files.listChildFiles(dir);
        List<String> filter_paths = new ArrayList();
        for (String path : paths)
            if (path.indexOf('.') >= 0) {
                int index = path.lastIndexOf('.');
                String suffix = path.substring(index).toLowerCase();
                if (suffix.equals(".mp4") || suffix.equals(".avi") || suffix.equals(".mov"))
                    filter_paths.add(path);
            }
        return filter_paths;
    }

    public static void openFile(Context context, String path, String authority) {
        Uri data = Uris.fromFile(context, path, authority);
        String type = MediaType.getMimeType(path);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(data, type);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            intent.setDataAndType(data, MediaType.TYPE_ALL);
            context.startActivity(intent);
        }
    }

    public static void openWebFile(Context context, String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(path));
        context.startActivity(intent);
    }

    public static Result<String> openFile(String path) {
        if (!Files.exist(path))
            return Result.fail(Result.FAIL, "文件不存在", path);
        try {
            Files.openFile(CommonApplication.ctx, path, Uris.AUTHORITY_FILE_PROVIDER);
            return Result.success();
        } catch (Exception e) {
            String extension = Files.getExtensionName(path);
            return Result.fail(Result.FAIL, "不支持该文件类型", extension);
        }
    }

    @SneakyThrows
    public static void writeStream(InputStream is, String dest) {
        Files.createFile(dest);
        writeStream(is, new FileOutputStream(dest));
    }

    @SneakyThrows
    private static void writeStream(InputStream is, OutputStream os) {
        byte[] buffer = new byte[10 * 1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1)
            os.write(buffer, 0, len);
        os.flush();
        os.close();
        is.close();
    }

    @SneakyThrows
    public static void writeStreamWithCallback(InputStream is, OutputStream os, ByteWriteCallback callback) {
        double total = is.available();
        double written = 0;
        byte[] buffer = new byte[10 * 1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
            written += len;
            if (callback != null)
                callback.onByteWrite(total, written);
        }
        os.flush();
        os.close();
        is.close();
    }

    @SneakyThrows
    private static void zip(ZipOutputStream zos, File source, String base) {
        // 如果是文件夹，创建目录并遍历子文件
        if (source.isDirectory()) {
            File[] childs = source.listFiles();
            // 空文件夹，则写入一个目录
            if (childs.length == 0)
                zos.putNextEntry(new ZipEntry(base + "/"));
                // 非空文件夹，则递归遍历子文件
            else
                for (File child : childs)
                    zip(zos, child, base + "/" + child.getName());
            return;
        }

        // 如果是文件，直接写数据
        zos.putNextEntry(new ZipEntry(base));
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(source));
        byte[] buffer = new byte[1024 * 1024];
        for (int len; (len = bis.read(buffer)) != -1; )
            zos.write(buffer, 0, len);
        bis.close();
    }

    //source 要压缩的资源，可以是文件夹，也可以是文件
    //dest 要生成的目标压缩文件
    @SneakyThrows
    public static void zip(String source, String dest) {
        Files.deleteFile(dest);
        Files.createFile(dest);
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(dest)));
        zip(zos, new File(source), new File(source).getName());
        zos.flush();
        zos.close();
    }

    @SneakyThrows
    public static String zipHere(String source) {
        String dest = new File(source).getParent() + "/" + new File(source).getName() + ".zip";
        Files.zip(source, dest);
        return dest;
    }

    @SneakyThrows
    public static void unzip(String source, String dest) {
        Files.createDirectory(dest);
        ZipFile zipFile = new ZipFile(source);
        Enumeration entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String file = dest + "/" + entry.getName();
            if (entry.isDirectory())
                Files.createDirectory(file);
            else {
                Files.createFile(file);
                InputStream is = zipFile.getInputStream(entry);
                BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                byte[] buffer = new byte[1024 * 1024];
                for (int len; (len = is.read(buffer)) != -1; )
                    os.write(buffer, 0, len);
                os.flush();
                os.close();
                is.close();
            }
        }
        zipFile.close();
    }

    @SneakyThrows
    public static void unzipHere(String source) {
        File sourceFile = Files.file(source);
        String name = sourceFile.getName();
        // int index = name.lastIndexOf(".zip");
        // String suffix = (index < 0) ? name : name.substring(0, index);
        // String dest = sourceFile.getParent() + "/" + suffix;
        Files.unzip(source, sourceFile.getParent());
    }

    //拷贝asset资源到存储卡
    @SneakyThrows
    public static void copyFromAsset(Context ctx, String source, String dest) {
        AssetManager manager = ctx.getAssets();
        InputStream is = manager.open(source);
        String to = dest + "/" + source;
        FileOutputStream os = new FileOutputStream(to);
        Files.writeStream(is, os);
    }

    //拷贝asset资源到存储卡
    @SneakyThrows
    public static void copyAssets(Context ctx, String[] sources, String dest) {
        Files.createDirectory(dest);
        for (String source : sources)
            Files.copyFromAsset(ctx, source, dest);
    }

    //获取文件长度
    public static long length(String path) {
        return new File(path).length();
    }

    //获取文件后缀
    public static String getFileSuffix(String path, boolean withDot) {
        String[] splits = path.split("[.]");
        if (splits.length == 0)
            return "";
        String suffix = splits[splits.length - 1];
        return withDot ? "." + suffix : suffix;
    }

    //复制文件到项目目录，并生成一个随机名称
    public static String copyToProjectDirectory(String path) {
        String name = Files.getFileName(path);
        String parent = CommonApplication.getFolderPath("file");
        String ext = Files.getExtensionName(path);

        //如果已经在项目目录下，不需要复制
        //超过100M的文件不复制
        if (Texts.startWithIgnoreCase(path, parent) || Files.size(path) > 1024 * 1024 * 100)
            return path;

        //复制并返回新的路径
        Files.copyToFolder(path, parent);
        String oldPath = parent + name;
        String newPath = parent + Texts.random() + "." + ext;
        Files.renameFile(oldPath, newPath);
        return newPath;
    }

    //获取文件长度
    public static long size(String path) {
        File file = Files.file(path);
        long length = file.length();
        return length;
    }

    //获取父级目录
    public static String getParent(String path) {
        return new File(path).getParent().toLowerCase();
    }

    //获取文件更新时间
    public static String updateTime(String path) {
        if (!Files.exist(path))
            return "";
        long ms = new File(path).lastModified();
        String updateTime = Times.formatDate(ms, Times.FORMAT_03);
        return updateTime;
    }

    public interface ByteWriteCallback {
        void onByteWrite(double total, double written);
    }

}
