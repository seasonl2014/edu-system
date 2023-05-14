package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduBanner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**幻灯片持久层
 * @author Administrator
 */
public interface EduBannerRepository extends JpaRepository<EduBanner, Long>, JpaSpecificationExecutor<EduBanner> {
    /**
     * 根据状态分页获取数据
     * @param status
     * @param pageable
     * @return
     */
    List<EduBanner> findByStatus(int status, Pageable pageable);
}