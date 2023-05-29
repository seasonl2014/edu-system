package cn.xueden.edu.alivod;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import jakarta.servlet.http.HttpServletRequest;

/**功能描述：上传到阿里云oss进度条
 * @author:梁志杰
 * @date:2023/5/28
 * @description:cn.xueden.edu.alivod
 * @version:1.0
 */
public class PutObjectProgressListener implements ProgressListener {

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
     * request
     */
    private HttpServletRequest request;

    /**
     * 文件唯一标志
     */
    private String fileKey;

    /**
     * 无参构造方法
     */
    public PutObjectProgressListener() {
    }

    /**
     * 文件大小
     */
    private int fileSize;

    public PutObjectProgressListener(HttpServletRequest request, String fileKey, int fileSize) {
        this.request = request;
        this.fileKey = fileKey;
        this.fileSize = fileSize;
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        this.totalBytes = fileSize;
        System.out.println(fileSize + ":fileSize");
        switch (eventType) {
            // 开始上传事件
            case TRANSFER_STARTED_EVENT:
                System.out.println("Start to upload......");
                break;
             // 计算待上传文件总大小事件通知，只有调用本地文件方式上传时支持该事件
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                System.out.println(this.totalBytes + " bytes in total will be uploaded to OSS");
                break;
             // 已经上传成功文件大小事件通知
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                if (this.totalBytes != -1) {
                    int percent = (int)(this.bytesWritten * 100.0 / this.totalBytes);
                    request.getServletContext().setAttribute("upload_percent"+fileKey,percent);
                    System.out.println(bytes + " bytes have been written at this time, upload progress: " +
                            percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
                } else {
                    System.out.println(bytes + " bytes have been written at this time, upload ratio: unknown" +
                            "(" + this.bytesWritten + "/...)");
                }
                break;
            // 文件全部上传成功事件通知
            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                request.getServletContext().removeAttribute("upload_percent"+fileKey);
                System.out.println("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
                break;
            // 文件上传失败事件通知
            case TRANSFER_FAILED_EVENT:
                System.out.println("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
                break;

            default:
                break;
        }
    }

    public boolean isSucceed() {
        return succeed;
    }


}
