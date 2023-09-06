package cn.xueden.edu.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

/**功能描述：VIP类别查询条件类
 * @author:梁志杰
 * @date:2023/5/11
 * @description:cn.xueden.edu.service.dto
 * @version:1.0
 */
@Data
public class EduVipTypeQueryCriteria {

    /**
     * 关键字搜索，VIP名称查询
     */
    @EnableXuedenQuery(blurry = "vipName")
    private String searchValue;
}
