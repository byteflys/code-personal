package com.easing.commons.android.download_file;

public class DownloadReqBean {
    private String downloadTypeTag;
    private BreakpointDownloadService.DownloadStatus status;
    private String value;

    public DownloadReqBean() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDownloadTypeTag() {
        return downloadTypeTag;
    }

    public void setDownloadTypeTag(String downloadTypeTag) {
        this.downloadTypeTag = downloadTypeTag;
    }

    public BreakpointDownloadService.DownloadStatus getStatus() {
        return status;
    }

    public void setStatus(BreakpointDownloadService.DownloadStatus status) {
        this.status = status;
    }
}
