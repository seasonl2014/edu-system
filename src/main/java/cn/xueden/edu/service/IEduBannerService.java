package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduBanner;
import cn.xueden.edu.service.dto.EduBannerQueryCriteria;
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

    /**
     * 获取轮播图列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    Object getList(EduBannerQueryCriteria queryCriteria, Pageable pageable);

    /**
     * 上传轮播图封面
     * @param bannerId
     * @param urlPath
     */
    void uploadBanner(Long bannerId, String urlPath);

    /**
     * 根据ID获取轮播图数据
     * @param id
     * @return
     */
    EduBanner findById(Long id);

    /**
     * 更新轮播图数据
     * @param eduBanner
     */
    void updateById(EduBanner eduBanner);

    /**
     * 新增轮播图数据
     * @param eduBanner
     */
    void add(EduBanner eduBanner);
}
