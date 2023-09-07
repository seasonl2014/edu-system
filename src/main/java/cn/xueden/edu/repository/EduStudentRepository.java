package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduStudent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 功能描述：学生管理持久层
 * @author Administrator
 */
public interface EduStudentRepository extends JpaRepository<EduStudent, Long>, JpaSpecificationExecutor<EduStudent> {

    /**
     * 根据手机号获取学员信息
     * @param phone
     * @return
     */
    EduStudent findByPhone(String phone);

    /**
     * 根据openid获取学员信息
     * @param openid
     * @return
     */
    EduStudent getByWxOpenId(String openid);

    /**
     * 根据学员编号获取信息
     * @param studentNo
     * @return
     */
    EduStudent findByStuNo(String studentNo);
}