package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduSms;

/**功能描述：短信设置业务接口
 * @author:梁志杰
 * @date:2023/9/3
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduSmsService {

    /**
     * 获取一条记录
     * @return
     */
    EduSms getOne();

    /**
     * 保存记录
     * @param eduSms
     */
    void saveOrUpdate(EduSms eduSms);
}
