package com.uzykj.mall.entity;

public class UpResult {
	
    public String fileName; //上传后的文件名
    public String originalName; //原文件名
    public long fileSize; //文件大小 单位（Byte）
    public String mimeType;
    public String suffix;//后缀名
    public String zoneName; //空间名称
    public String hash;
    
    
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	@Override
	public String toString() {
		return "UpResult [fileName=" + fileName + ", originalName=" + originalName + ", fileSize=" + fileSize
				+ ", mimeType=" + mimeType + ", suffix=" + suffix + ", zoneName=" + zoneName + ", hash=" + hash + "]";
	} 
    

    
    
    
    
}
