package cn.xueden.edu.alivod.dto;

import lombok.Data;

/**视频分类dto
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.alivod.dto
 * @version:1.0
 */
@Data
public class CategoryDto {
    /**
     * 父级Id
     */
    private Long parentId;

    /**
     * 分类Id
     */
    private Long cateId;

    /**
     * 分类名称
     */
    private String cateName;

    /**
     * 分类级别
     */
    private Long level;
}
