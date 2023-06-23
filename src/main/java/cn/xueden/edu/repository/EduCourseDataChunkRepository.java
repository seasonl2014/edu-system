package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduCourseDataChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 功能描述：上传资料分片持久层接口
 * @author Administrator
 */
public interface EduCourseDataChunkRepository extends JpaRepository<EduCourseDataChunk, Long>, JpaSpecificationExecutor<EduCourseDataChunk> {

    /**
     * 删除临时分片数据
     * @param md5
     */
    void deleteAllByMd5(String md5);

    /**
     * 根据文件唯一标志获取分片数据
     * @param fileKey
     * @return
     */
    List<EduCourseDataChunk> findByMd5(String fileKey);
}