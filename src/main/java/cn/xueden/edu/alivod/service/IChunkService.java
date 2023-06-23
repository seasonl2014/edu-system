package cn.xueden.edu.alivod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 功能描述：分片上传资料接口
 * @author:梁志杰
 * @date:2023/6/23
 * @description:cn.xueden.edu.alivod.service
 * @version:1.0
 */
public interface IChunkService {
    /**
     * 保存临时分片记录
     * @param chunk
     * @param md5
     * @param index
     * @param chunkSize
     * @param resultFileName
     */
    void saveChunk(MultipartFile chunk, String md5, Integer index, Long chunkSize, String resultFileName,Long courseId);

    /**
     * 删除临时分片数据
     * @param md5
     */
    void deleteChunkByMd5(String md5);

    /**
     * 根据文件唯一标志查找分片数据
     * @param fileKey
     * @return
     */
    List<Integer> selectChunkListByMd5(String fileKey);
}
