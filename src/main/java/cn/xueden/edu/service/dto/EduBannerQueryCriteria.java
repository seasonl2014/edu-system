package cn.xueden.edu.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

/**功能描述：轮播图查询参数
 * @author:梁志杰
 * @date:2023/5/31
 * @description:cn.xueden.edu.service.dto
 * @version:1.0
 */
@Data
public class EduBannerQueryCriteria {
    /**
     * 根据标题模糊查询
     */
    @EnableXuedenQuery(blurry = "title")
    private String searchValue;
}
