package cn.xueden.edu.service;

import org.springframework.data.domain.Pageable;

/**功能描述：首页幻灯片业务接口
 * @author:梁志杰
 * @date:2023/5/13
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduBannerService {

    /**
     * 根据条件分页获取幻灯片
     * @param pageable
     * @return
     */
    Object findByStatus(Pageable pageable);
}
