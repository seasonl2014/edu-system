package cn.xueden.utils;

import lombok.Data;

/**功能描述：分页查询参数
 * @author:梁志杰
 * @date:2023/2/1
 * @description:cn.xueden.utils
 * @version:1.0
 */
@Data
public class PageVo {

    /**
     * 当前页码
     */
    private int pageIndex;

    /**
     * 每页显示条数
     */
    private int pageSize;

}
