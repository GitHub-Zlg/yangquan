package app.com.yangquan.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;

import com.zxy.tiny.core.FileKit;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class FileSaveUtil {
    public static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().toString()+"/Android/data/app.com.yangquan/";
    public static final String my_voice_dir = SD_CARD_PATH
            + "voice_data/";
    public static final String my_picture_dir = FileKit.getDefaultFileCompressDirectory().getAbsolutePath();

    /**
     * SD卡是否存在
     **/
    private boolean hasSD = false;
    /**
     * 当前程序包的路径
     **/
    private String FILESPATH;

    String path="/sdcard/letterschat/";
    /**
     * 从文本中读取 省市区城市列表
     * @return
     */
    public static String getText(Context context){
        InputStream in=null;
        ByteArrayOutputStream out=null;
        try {
            in=context.getAssets().open("city.txt");
            out=new ByteArrayOutputStream();
            byte[] b=new byte[1024];
            int length=-1;
            while ((length=in.read(b))!=-1){
                out.write(b,0,length);
            }
            return new String(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 把Bitmap转Byte
     */
    public static byte[] bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    public static String createAarFileName(){
        Calendar now = new GregorianCalendar();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = simpleDate.format(new Date());
        return fileName+".amr";
    }
    public static String createPictureFileName(){
        Calendar now = new GregorianCalendar();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = simpleDate.format(new Date());
        return fileName+".jpg";
    }
    /**
     * 文件保存
     * @param filename
     * @param in
     */
    public static void saveFile(String filename,InputStream in){
        String filepath = "";
        File file = null;
        if (filename.contains(".amr")){
            filepath = my_voice_dir;
        }else {
            filepath = my_picture_dir;
        }
        file=new File(filepath);
        if(!file.exists()){
            if(!file.mkdirs()){//若创建文件夹不成功
            }
        }
        File targetfile=new File(filepath+filename);
        OutputStream os=null;
        try{
            os=new FileOutputStream(targetfile);
            int ch=0;
            while((ch=in.read())!=-1){
                os.write(ch);
            }
            os.flush();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                os.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static boolean isFileExists(File file) {
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * 获取文件夹下的所有文件名
     */
    public static List<String> getFileName(String fileName) {
        List<String> fileList = new ArrayList<String>();
        String path = fileName; // 路径
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return null;
        }

        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (!fs.isDirectory()) {
                fileList.add(fs.getName());
            }
        }
        return fileList;
    }

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public static File createSDFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!isFileExists(file))
            if (file.isDirectory()) {
                file.mkdirs();
            } else {
                file.createNewFile();
            }
        return file;
    }

    /**
     * 在SD卡上创建文件夹
     *
     * @throws IOException
     */
    public static File createSDDirectory(String fileName) throws IOException {
        File file = new File(fileName);
        if (!isFileExists(file))
            file.mkdirs();
        return file;
    }

//    /**
//     * @content 存储内容
//     * @file 文件目录
//     * @isAppend 是否追加
//     */
//    public synchronized static void writeString(String content, String file, boolean isAppend) {
//        try {
//            createSDDirectory(saveFn);
//            createSDDirectory(savelistFn);
//            createSDDirectory(savechannelFn);
//            byte[] data = content.getBytes("utf-8");
//            writeBytes(file, data, isAppend);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//    }

    public synchronized static boolean writeBytes(String filePath, byte[] data,
                                                  boolean isAppend) {
        try {
            FileOutputStream fos;
            if (isAppend)
                fos = new FileOutputStream(filePath, true);
            else
                fos = new FileOutputStream(filePath);
            fos.write(data);
            fos.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * 根据文件名读取文件
     * @param fileName 文件名
     * @return 输入流
     */
    public synchronized static InputStream readFile(String fileName){
        File f1 = new File(fileName);
        InputStream is = null;
        try {
            is = new FileInputStream(f1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return is;
    }
    /**
     * 读取SD卡中文本文件
     *
     * @param fileName
     * @return
     */
    public synchronized static String readSDFile(String fileName) {
        StringBuffer sb = new StringBuffer();
        File f1 = new File(fileName);
        String str = null;
        try {
            InputStream is = new FileInputStream(f1);
            InputStreamReader input = new InputStreamReader(is, "UTF-8");
            @SuppressWarnings("resource")
            BufferedReader reader = new BufferedReader(input);
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getFILESPATH() {
        return FILESPATH;
    }

    public boolean hasSD() {
        return hasSD;
    }

    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     *
     * @param filePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        // 如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        // 遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                // 删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {
                // 删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前空目录
        return dirFile.delete();
    }

    public static boolean saveBitmap(Bitmap bm, String picName) {
        try {
            File f = new File(picName);
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 把文件转换成base64
     *
     * @param path
     * @return
     */
    public static String encodeBase64File(String path) throws Exception {
        byte[] videoBytes;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        @SuppressWarnings("resource")
        FileInputStream fis = new FileInputStream(new File(path));
        byte[] buf = new byte[1024];
        int n;
        while (-1 != (n = fis.read(buf)))
            baos.write(buf, 0, n);
        videoBytes = baos.toByteArray();
        return Base64.encodeToString(videoBytes, Base64.NO_WRAP);
    }

    /**
     * 根据相册媒体库路径转换成sd卡路径
     *
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isOverKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isOverKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    @SuppressLint("NewApi")
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * 读取手机中所有图片信息
     */
    public static void saveOppoLogAppMesTxt(String str){
        try {
            File file_dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/dnwlog1.txt");
            if (file_dir.exists()){
                file_dir.delete();
            }
            file_dir.createNewFile();

            FileWriter fw = new FileWriter(file_dir);//SD卡中的路径
            fw.flush();
            fw.write(str);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void saveOppoLogSptMesTxt(String str){
        try {
            File file_dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/dnwlog2.txt");
            if (file_dir.exists()){
                file_dir.delete();
            }
            file_dir.createNewFile();
            FileWriter fw = new FileWriter(file_dir);//SD卡中的路径
            fw.flush();
            fw.write(str);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void saveMzStringTxt(String str){
        try {
            File file_dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/dnwlog3.txt");
            if (file_dir.exists()){
                file_dir.delete();
            }
            file_dir.createNewFile();
            FileWriter fw = new FileWriter(file_dir);//SD卡中的路径
            fw.flush();
            fw.write(str);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}