package cn.xueden.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.edu.domain.EduBanner;
import cn.xueden.edu.domain.EduStudentId;
import cn.xueden.edu.repository.EduBannerRepository;
import cn.xueden.edu.service.IEduBannerService;
import cn.xueden.edu.service.dto.EduBannerQueryCriteria;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.springframework.data.domain.Page;
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

    /**
     * 获取轮播图列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(EduBannerQueryCriteria queryCriteria, Pageable pageable) {
        Page<EduBanner> page = eduBannerRepository.findAll((root, query, criteriaBuilder)->
                QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 上传轮播图封面
     * @param bannerId
     * @param urlPath
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void uploadBanner(Long bannerId, String urlPath) {
        EduBanner dbEduBanner =  eduBannerRepository.getReferenceById(bannerId);
        if(dbEduBanner!=null){
            dbEduBanner.setImg(urlPath);
            eduBannerRepository.save(dbEduBanner);
        }
    }

    /**
     * 根据ID获取轮播图数据
     * @param id
     * @return
     */
    @Override
    public EduBanner findById(Long id) {
        return eduBannerRepository.getReferenceById(id);
    }

    /**
     * 更新轮播图数据
     * @param eduBanner
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateById(EduBanner eduBanner) {
        EduBanner dbEduBanner = eduBannerRepository.getReferenceById(eduBanner.getId());
        BeanUtil.copyProperties(eduBanner,dbEduBanner, CopyOptions.create().setIgnoreError(true).setIgnoreNullValue(true));
        eduBannerRepository.save(dbEduBanner);
    }

    /**
     * 新增轮播图数据
     * @param eduBanner
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(EduBanner eduBanner) {
        eduBannerRepository.save(eduBanner);
    }
}
