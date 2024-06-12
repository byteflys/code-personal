package com.easing.commons.android.ui.control.attach_viewer;


import java.util.List;

import lombok.Data;
import okhttp3.MultipartBody;

//图片附件上传参数
@Data
public class PictureRelation<T> {

    //   @Deprecated
    //  private String relationId;   //关联id   标注点用离线id
    // @Deprecated
    // private String pointId;      //样点id
    private T tag; //参数携带 用于 EventBus data

    private String eventTag; //用于EventBus type 发送
    private String eventData1; //用于EventBus data1 发送

    private String logicSysNo; // 是 系统逻辑编号
    private String eventId; // 是 附件所属事件id
    private String eventSourceId; // 是 数据源id
    private Integer eventSourceType; // 是 数据源类型(对于 后台的设备类型 0: 用户, 1: 设备 2 ：红外相机)

    private String localStorage; // 否 离线缓存位置（app专用);
    private String offlineFileId; // 否 附件离线id（app专用）
    private String remark; // 否 备注
    private String fileType; // 否 文件类型（根据业务定制，方便分类查询）
    private String additionalId; // 否 拓展id（根据业务定制，方便子级查询）
    private String tenantNo; // 否 租户编号
    private String describeType; // 否 描述类型（根据业务定制，方便按大项类型查询）
    private String eventCreateTime; // 否 创建时间(yyyy-MM-dd HH:mm:ss)
    private String functionNo; // 否 所属功能编号（用于消息推送）
    private String to; // 否 接收人（用于消息推送）
    private Integer toType; // 否 接收人类型（用于消息推送)
    private String addInfo; // 否 附件信息（用于消息推送）
    private String eventType;   // 否 附件所属事件类型（根据业务定制，方便按事件类型查询）  样点生活照 = infraredEnvironmentFile  标注点附件 = signPointFile

    private List<String> filePathList; //批量上传文件的数组
    private String filePath;  //单个上传的文件
    // private List<Attach> attachList;

    /**
     * 添加附件上传头
     */
    public static void handFromData(MultipartBody.Builder requestBody, PictureRelation pictureRelation) {
        if (pictureRelation.getFileType() != null)
            requestBody.addFormDataPart("fileType", pictureRelation.getFileType());

        if (pictureRelation.getToType() != null)
            requestBody.addFormDataPart("toType", pictureRelation.getToType().toString());

        if (pictureRelation.getAddInfo() != null)
            requestBody.addFormDataPart("addInfo", pictureRelation.getAddInfo());

        if (pictureRelation.getTo() != null)
            requestBody.addFormDataPart("to", pictureRelation.getTo());

        if (pictureRelation.getEventCreateTime() != null)
            requestBody.addFormDataPart("eventCreateTime", pictureRelation.getEventCreateTime());

        if (pictureRelation.getDescribeType() != null)
            requestBody.addFormDataPart("describeType", pictureRelation.getDescribeType());

        if (pictureRelation.getTenantNo() != null)
            requestBody.addFormDataPart("tenantNo", pictureRelation.getTenantNo());

        if (pictureRelation.getLocalStorage() != null)
            requestBody.addFormDataPart("localStorage", pictureRelation.getLocalStorage());

        if (pictureRelation.getOfflineFileId() != null)
            requestBody.addFormDataPart("offlineFileId", pictureRelation.getOfflineFileId());

        if (pictureRelation.getRemark() != null)
            requestBody.addFormDataPart("remark", pictureRelation.getRemark());

        if (pictureRelation.getLogicSysNo() != null)
            requestBody.addFormDataPart("logicSysNo", pictureRelation.getLogicSysNo());

        if (pictureRelation.getEventId() != null)
            requestBody.addFormDataPart("eventId", pictureRelation.getEventId());

        if (pictureRelation.getEventType() != null)
            requestBody.addFormDataPart("eventType", pictureRelation.getEventType());

        if (pictureRelation.getEventSourceType() != null)
            requestBody.addFormDataPart("eventSourceType", pictureRelation.getEventSourceType().toString());

        if (pictureRelation.getEventSourceId() != null)
            requestBody.addFormDataPart("eventSourceId", pictureRelation.getEventSourceId());

        if (pictureRelation.getFunctionNo() != null)
            requestBody.addFormDataPart("functionNo", pictureRelation.getFunctionNo());

        if (pictureRelation.getAdditionalId() != null)
            requestBody.addFormDataPart("additionalId", pictureRelation.getAdditionalId());
    }
}
