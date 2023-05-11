package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduTeacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 功能描述：讲师师管理持久层
 * @author Administrator
 */
public interface EduTeacherRepository extends JpaRepository<EduTeacher, Long>, JpaSpecificationExecutor<EduTeacher> {
}