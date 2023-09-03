package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduEmail;

/**功能描述：邮箱设置业务接口
 * @author:梁志杰
 * @date:2023/9/3
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduEmailService {

    /**
     * 获取一条记录
     * @return
     */
    EduEmail getOne();

    /**
     * 保存记录
     * @param eduEmail
     */
    void saveOrUpdate(EduEmail eduEmail);
}
