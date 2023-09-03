package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduWxCode;

/**功能描述：微信扫码登录配置信息业务接口
 * @author:梁志杰
 * @date:2023/9/3
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduWxCodeService {

    /**
     * 获取一条记录
     * @return
     */
    EduWxCode getOne();

    /**
     * 保存记录
     */
    void saveOrUpdate(EduWxCode eduWxCode);
}
