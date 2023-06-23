package cn.xueden.edu.alivod.service.impl;

import cn.xueden.edu.alivod.AliOssMultipartUploadFileService;
import cn.xueden.edu.alivod.service.IChunkService;
import cn.xueden.edu.domain.EduCourseDataChunk;
import cn.xueden.edu.repository.EduCourseDataChunkRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**功能描述：分片上传资料接口实现类
 * @author:梁志杰
 * @date:2023/6/23
 * @description:cn.xueden.edu.alivod.service.impl
 * @version:1.0
 */
@Transactional(readOnly = true)
@Service
public class ChunkServiceImpl implements IChunkService {

    private final EduCourseDataChunkRepository eduCourseDataChunkRepository;



    public ChunkServiceImpl(EduCourseDataChunkRepository eduCourseDataChunkRepository) {
        this.eduCourseDataChunkRepository = eduCourseDataChunkRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveChunk(MultipartFile chunk, String md5, Integer index, Long chunkSize, String resultFileName,Long courseId) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(resultFileName, "rw")) {
            // 偏移量
            long offset = chunkSize * (index - 1);
            // 定位到该分片的偏移量
            randomAccessFile.seek(offset);
            // 写入
            randomAccessFile.write(chunk.getBytes());
            EduCourseDataChunk chunkPO = new EduCourseDataChunk(md5,index);
            eduCourseDataChunkRepository.save(chunkPO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除临时分片记录
     * @param md5
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteChunkByMd5(String md5) {
        eduCourseDataChunkRepository.deleteAllByMd5(md5);
    }

    /**
     * 根据文件唯一标志查找分片数据
     * @param fileKey
     * @return
     */
    @Override
    public List<Integer> selectChunkListByMd5(String fileKey) {
        List<EduCourseDataChunk> chunkPOList = eduCourseDataChunkRepository.findByMd5(fileKey);
        List<Integer> indexList = new ArrayList<>();
        for (EduCourseDataChunk chunkPO : chunkPOList) {
            indexList.add(chunkPO.getIndex());
        }
        return indexList;
    }
}
