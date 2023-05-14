package cn.xueden.edu.service.impl;

import cn.xueden.edu.repository.EduBannerRepository;
import cn.xueden.edu.service.IEduBannerService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：首页幻灯片业务接口实现
 * @author:梁志杰
 * @date:2023/5/13
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduBannerServiceImpl implements IEduBannerService {

    private final EduBannerRepository eduBannerRepository;

    public EduBannerServiceImpl(EduBannerRepository eduBannerRepository) {
        this.eduBannerRepository = eduBannerRepository;
    }

    /**
     * 根据条件分页获取数据
     * @param pageable
     * @return
     */
    @Override
    public Object findByStatus(Pageable pageable) {
        return eduBannerRepository.findByStatus(1,pageable);
    }
}
