package cn.xueden.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.edu.domain.EduEmail;
import cn.xueden.edu.repository.EduEmailRepository;
import cn.xueden.edu.service.IEduEmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：邮箱配置业务接口实现
 * @author:梁志杰
 * @date:2023/9/3
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduEmailServiceImpl implements IEduEmailService {

    private final EduEmailRepository eduEmailRepository;

    public EduEmailServiceImpl(EduEmailRepository eduEmailRepository) {
        this.eduEmailRepository = eduEmailRepository;
    }

    /**
     * 获取一条记录
     * @return
     */
    @Override
    public EduEmail getOne() {
        return eduEmailRepository.findFirstByOrderByIdDesc();
    }

    /**
     * 保存记录
     * @param eduEmail
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdate(EduEmail eduEmail) {
        // 更新数据
        if(eduEmail.getId()!=null&&eduEmail.getId()>0){
            EduEmail dbEduEmail = eduEmailRepository.getReferenceById(eduEmail.getId());
            BeanUtil.copyProperties(eduEmail,dbEduEmail, CopyOptions.create().setIgnoreError(true).setIgnoreNullValue(true));
            eduEmailRepository.save(dbEduEmail);
        // 新增数据
        }else {
            eduEmailRepository.save(eduEmail);
        }
    }
}
