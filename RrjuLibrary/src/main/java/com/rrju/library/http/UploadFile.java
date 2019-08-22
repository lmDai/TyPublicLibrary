package com.rrju.library.http;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

public class UploadFile {

    /**
     * 上传文件数据
     */
    private byte[] data;

    /**
     * 输入流
     */
    private InputStream inputStream;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 请求参数名称
     */
    private String formName;

    /**
     * 上传文件的后缀
     */
    private String mSuffix = "jpg";
    private File file;

    public UploadFile(String filePath, String formName, String suffix) {

        this.filePath = filePath;
        this.formName = formName;
        if (suffix != null) {
            this.mSuffix = suffix;
        }
    }

    /**
     * 以二进制流的形式构造数据
     */
    public UploadFile(byte[] data, String filePath, String formName,
                      String suffix) {

        this.data = data;
        this.filePath = filePath;
        this.formName = formName;
        if (suffix != null) {
            this.mSuffix = suffix;
        }
    }

    /**
     * 以文件流的形式构造数据
     */
    public UploadFile(InputStream inputStream, String filePath,
                      String formName, String suffix) {
        this.inputStream = inputStream;
        this.filePath = filePath;
        this.formName = formName;
        if (suffix != null) {
            this.mSuffix = suffix;
        }
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getSuffix() {
        return mSuffix;
    }

    public void setContentType(String suffix) {
        this.mSuffix = suffix;
    }

    public File getFile() {
        if (file == null) {
            file = new File(filePath);
        }
        return file;
    }

    @Override
    public String toString() {
        return "UploadFile [data=" + Arrays.toString(data) + ", inputStream="
                + inputStream + ", filePath=" + filePath + ", formName="
                + formName + ", mSuffix=" + mSuffix + "]";
    }

}
