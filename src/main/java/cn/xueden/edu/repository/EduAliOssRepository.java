package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduAliOss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：阿里云OSS信息配置持久层
 * @author Administrator
 */
public interface EduAliOssRepository extends JpaRepository<EduAliOss, Integer>, JpaSpecificationExecutor<EduAliOss> {

    /**
     * 获取一条记录
     * @return
     */
    EduAliOss findFirstByOrderByIdDesc();
}