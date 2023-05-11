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
     * 根据班级ID获取所有学生
     * @param gradeClassId
     * @return
     */
    List<EduStudent> findAllByGradeClassId(Long gradeClassId);
}