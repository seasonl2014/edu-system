package cn.xueden.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.edu.domain.EduWxCode;
import cn.xueden.edu.repository.EduWxCodeRepository;
import cn.xueden.edu.service.IEduWxCodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：微信扫码登录配置信息业务接口实现
 * @author:梁志杰
 * @date:2023/9/3
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduWxCodeServiceImpl implements IEduWxCodeService {

    private final EduWxCodeRepository eduWxCodeRepository;

    public EduWxCodeServiceImpl(EduWxCodeRepository eduWxCodeRepository) {
        this.eduWxCodeRepository = eduWxCodeRepository;
    }

    /**
     * 获取一条记录
     * @return
     */
    @Override
    public EduWxCode getOne() {
        return eduWxCodeRepository.findFirstByOrderByIdDesc();
    }

    /**
     * 保存记录
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdate(EduWxCode eduWxCode) {
        // 更新数据
        if(eduWxCode.getId()!=null&&eduWxCode.getId()>0){
            EduWxCode dbEduWxCode = eduWxCodeRepository.getReferenceById(eduWxCode.getId());
            BeanUtil.copyProperties(eduWxCode,dbEduWxCode, CopyOptions.create().setIgnoreError(true).setIgnoreNullValue(true));
            eduWxCodeRepository.save(dbEduWxCode);
        // 新增数据
        }else {
            eduWxCodeRepository.save(eduWxCode);
        }
    }
}
