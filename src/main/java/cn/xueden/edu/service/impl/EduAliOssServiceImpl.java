package cn.xueden.edu.service.impl;

import cn.xueden.edu.domain.EduAliOss;
import cn.xueden.edu.repository.EduAliOssRepository;
import cn.xueden.edu.service.IEduAliOssService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：阿里云OSS信息配置业务接口实现
 * @author:梁志杰
 * @date:2023/9/3
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduAliOssServiceImpl implements IEduAliOssService {

    private final EduAliOssRepository eduAliOssRepository;

    public EduAliOssServiceImpl(EduAliOssRepository eduAliOssRepository) {
        this.eduAliOssRepository = eduAliOssRepository;
    }

    /**
     * 获取一条记录
     * @return
     */
    @Override
    public EduAliOss getOne() {
        return eduAliOssRepository.findFirstByOrderByIdDesc();
    }
}
