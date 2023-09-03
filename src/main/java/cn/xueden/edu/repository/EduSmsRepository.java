package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduSms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：短信设置持久层
 * @author Administrator
 */
public interface EduSmsRepository extends JpaRepository<EduSms, Integer>, JpaSpecificationExecutor<EduSms> {
}