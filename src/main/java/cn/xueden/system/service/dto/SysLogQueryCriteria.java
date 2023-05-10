package cn.xueden.system.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

/**日志信息查询条件
 * @author:梁志杰
 * @date:2023/3/21
 * @description:cn.xueden.system.service.dto
 * @version:1.0
 */
@Data
public class SysLogQueryCriteria {

    /**
     * 根据日志类型、日志标题、请求方法、浏览器、省市模糊查询
     */
    @EnableXuedenQuery(blurry = "type,title,httpMethod,browser,province,city,isp")
    private String searchValue;
}
