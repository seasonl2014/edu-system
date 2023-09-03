package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：邮箱设置持久层
 * @author 梁志杰
 */
public interface EduEmailRepository extends JpaRepository<EduEmail, Integer>, JpaSpecificationExecutor<EduEmail> {

    /**
     * 获取一条记录
     * @return
     */
    EduEmail findFirstByOrderByIdDesc();
}