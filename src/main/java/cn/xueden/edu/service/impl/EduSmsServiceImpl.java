package cn.xueden.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.edu.domain.EduSms;
import cn.xueden.edu.repository.EduSmsRepository;
import cn.xueden.edu.service.IEduSmsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：短信设置业务接口实现
 * @author:梁志杰
 * @date:2023/9/3
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduSmsServiceImpl implements IEduSmsService {

    private final EduSmsRepository eduSmsRepository;

    public EduSmsServiceImpl(EduSmsRepository eduSmsRepository) {
        this.eduSmsRepository = eduSmsRepository;
    }

    /**
     * 获取一条记录
     * @return
     */
    @Override
    public EduSms getOne() {
        return eduSmsRepository.findFirstByOrderByIdDesc();
    }

    /**
     * 保存记录
     * @param eduSms
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdate(EduSms eduSms) {
        // 更新数据
        if(eduSms.getId()!=null&&eduSms.getId()>0){
            EduSms dbEduSms = eduSmsRepository.getReferenceById(eduSms.getId());
            BeanUtil.copyProperties(eduSms,dbEduSms, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
            eduSmsRepository.save(dbEduSms);
        //新增数据
        }else {
            eduSmsRepository.save(eduSms);
        }
    }
}
