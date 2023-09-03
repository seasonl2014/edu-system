package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduWxCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：微信扫码登录配置信息持久层
 * @author Administrator
 */
public interface EduWxCodeRepository extends JpaRepository<EduWxCode, Integer>, JpaSpecificationExecutor<EduWxCode> {

    /**
     * 获取第一条记录
     * @return
     */
    EduWxCode findFirstByOrderByIdDesc();
}