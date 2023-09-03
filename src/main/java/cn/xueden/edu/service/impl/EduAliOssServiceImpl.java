package cn.xueden.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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

    /**
     * 保存记录
     * @param eduAliOss
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdate(EduAliOss eduAliOss) {
        // 更新数据
        if(eduAliOss.getId()!=null && eduAliOss.getId()>0){
            EduAliOss dbEduAliOss = eduAliOssRepository.getReferenceById(eduAliOss.getId());
            BeanUtil.copyProperties(eduAliOss,dbEduAliOss, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
            eduAliOssRepository.save(dbEduAliOss);
        // 新增数据
        }else {
            eduAliOssRepository.save(eduAliOss);
        }
    }
}
