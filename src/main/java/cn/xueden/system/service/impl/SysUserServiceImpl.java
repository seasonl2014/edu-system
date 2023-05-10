package cn.xueden.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.system.domain.SysUser;
import cn.xueden.system.repository.SysUserRepository;
import cn.xueden.system.service.ISysUserService;
import cn.xueden.system.service.dto.UserQueryCriteria;

import cn.xueden.system.vo.ModifyPwdModel;
import cn.xueden.utils.Md5Util;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：系统用户业务实现类
 * @author:梁志杰
 * @date:2023/1/27
 * @description:cn.xueden.student.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class SysUserServiceImpl implements ISysUserService {

    private final SysUserRepository sysUserRepository;

    public SysUserServiceImpl(SysUserRepository sysUserRepository) {
        this.sysUserRepository = sysUserRepository;
    }

    /**
     * 登录
     * @param sysUser
     * @return
     */
    @Override
    public SysUser login(SysUser sysUser){
        SysUser dbSysUser = sysUserRepository.findByUsername(sysUser.getUsername());
        return dbSysUser;
    }

    /**
     * 获取用户列表数据
     * @param pageable
     * @return
     */
    @Override
    public Object getList(UserQueryCriteria queryCriteria, Pageable pageable){
        Page<SysUser> page = sysUserRepository.findAll((root,query,criteriaBuilder)->
                QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 添加用户信息
     * @param sysUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(SysUser sysUser){
        SysUser dbSysUser = sysUserRepository.save(sysUser);
        return dbSysUser.getId()!=null;
    }

    /**
     * 根据ID获取用户详情信息
     * @param id
     * @return
     */
    @Override
    public SysUser getById(Long id){
        return sysUserRepository.findById(id).orElseGet(SysUser::new);
    }

    /**
     * 更新用户信息
     * @param sysUser
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUser(SysUser sysUser){
        SysUser dbSysUser = sysUserRepository.getReferenceById(sysUser.getId());
        BeanUtil.copyProperties(sysUser,dbSysUser,CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        sysUserRepository.save(dbSysUser);
    }

    /**
     * 根据ID删除用户信息
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        sysUserRepository.deleteById(id);
    }

    /**
     * 更新个人密码
     * @param modifyPwdModel
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePwd(ModifyPwdModel modifyPwdModel) {

        // 根据用户ID获取用户信息
        SysUser dbSysUser = sysUserRepository.getReferenceById(modifyPwdModel.getUserId());
        // 判断输入的旧密码是否正确
        String dbPwd = dbSysUser.getPassword();
        String usePwd = Md5Util.Md5(modifyPwdModel.getUsedPass());
        if(!dbPwd.equals(usePwd)){
            return false;
        }else {
            String newPas = Md5Util.Md5(modifyPwdModel.getNewPass());
            dbSysUser.setPassword(newPas);
            sysUserRepository.save(dbSysUser);
            return true;
        }
    }
}
