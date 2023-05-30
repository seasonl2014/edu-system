package cn.xueden.edu.alivod;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.vod.upload.impl.VoDProgressListener;
import jakarta.servlet.http.HttpServletRequest;

/**功能描述：视频上传进度回调方法类
 * @author:梁志杰
 * @date:2023/5/30
 * @description:cn.xueden.edu.alivod
 * @version:1.0
 */
public class PutVodProgressListener implements VoDProgressListener {

    private HttpServletRequest request;

    /**
     * 已成功上传至OSS的字节数
     */
    private long bytesWritten = 0;
    /**
     * 原始文件的总字节数
     */
    private long totalBytes = -1;
    /**
     * 本次上传成功标记
     */
    private boolean succeed = false;
    /**
     * 视频ID
     */
    private String videoId;

    /**
     * 文件大小
     */
    private int fileSize;

    /**
     * 章节ID
     */
    private Long id;

    /**
     * 文件标志
     */
    private String fileKey;

    /**
     * 无参构造方法
     */
    public PutVodProgressListener() {
    }

    /**
     * 有参构造方法
     * @param request
     * @param fileSize 文件大小
     * @param id 章节ID
     * @param fileKey 文件唯一标志
     */
    public PutVodProgressListener(HttpServletRequest request, int fileSize, Long id, String fileKey) {
        this.request = request;
        this.fileSize = fileSize;
        this.id = id;
        this.fileKey = fileKey;
    }

    @Override
    public void onVidReady(String s) {

    }

    @Override
    public void onImageIdReady(String s) {

    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        this.totalBytes = fileSize;
        switch (eventType) {
            // 开始上传事件
            case TRANSFER_STARTED_EVENT:
                if (videoId != null) {
                    System.out.println("qqqqStart to upload videoId1 " + videoId + "......");
                }
                break;
            // 计算待上传文件总大小事件通知，只有调用本地文件方式上传时支持该事件
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                System.out.println(this.totalBytes + "bytes in total will be uploaded to OSS.");
                break;
            // 已经上传成功文件大小事件通知
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                if (this.totalBytes != -1) {
                    int percent = (int) (this.bytesWritten * 100.0 / this.totalBytes);
                    // 将进度percent放入request中
                    request.getServletContext().setAttribute("upload_video_percent"+id,percent);
                    request.getServletContext().setAttribute("upload_video_percent"+fileKey,percent);
                } else {
                    System.out.println(bytes + " bytes have been written at this time, upload sub total4 : " +
                            "(" + this.bytesWritten + ")");
                }
                break;
            // 文件全部上传成功事件通知
            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                if (videoId != null) {
                    System.out.println("Succeed to upload videoId " + videoId + " , " + this.bytesWritten + " bytes have been transferred in total.");
                }

                break;
            // 文件上传失败事件通知
            case TRANSFER_FAILED_EVENT:
                if (videoId != null) {
                    System.out.println("Failed to upload videoId " + videoId + " , " + this.bytesWritten + " bytes have been transferred.");
                }

                break;

            default:
                break;
        }
    }
}
