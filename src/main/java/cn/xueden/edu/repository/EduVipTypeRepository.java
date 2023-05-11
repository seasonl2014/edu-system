package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduVipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：VIP类别持久层
 * @author Administrator
 */
public interface EduVipTypeRepository extends JpaRepository<EduVipType, Long>, JpaSpecificationExecutor<EduVipType> {
}