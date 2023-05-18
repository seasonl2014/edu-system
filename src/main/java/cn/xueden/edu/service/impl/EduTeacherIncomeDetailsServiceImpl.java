package cn.xueden.edu.service.impl;

import cn.xueden.edu.domain.EduStudentBuyCourse;
import cn.xueden.edu.domain.EduTeacher;
import cn.xueden.edu.domain.EduTeacherIncomeDetails;
import cn.xueden.edu.repository.EduStudentBuyCourseRepository;
import cn.xueden.edu.repository.EduTeacherIncomeDetailsRepository;
import cn.xueden.edu.repository.EduTeacherRepository;
import cn.xueden.edu.service.IEduTeacherIncomeDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**功能描述：讲师收入明细业务接口实现
 * @author:梁志杰
 * @date:2023/5/18
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduTeacherIncomeDetailsServiceImpl implements IEduTeacherIncomeDetailsService {

    private final EduTeacherIncomeDetailsRepository teacherIncomeDetailsRepository;

    private final EduStudentBuyCourseRepository studentBuyCourseRepository;

    private final EduTeacherRepository teacherRepository;

    public EduTeacherIncomeDetailsServiceImpl(EduTeacherIncomeDetailsRepository teacherIncomeDetailsRepository, EduStudentBuyCourseRepository studentBuyCourseRepository, EduTeacherRepository teacherRepository) {
        this.teacherIncomeDetailsRepository = teacherIncomeDetailsRepository;
        this.studentBuyCourseRepository = studentBuyCourseRepository;
        this.teacherRepository = teacherRepository;
    }

    /**
     * 学员购买课程成功后计算讲师收入
     * @param orderNo
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void teacherIncome(String orderNo) {
        // 根据订单编号获取购买课程订单
        EduStudentBuyCourse dbEduStudentBuyCourse = studentBuyCourseRepository.getByOrderNo(orderNo);
        // 获取讲师信息
        EduTeacher dbEduTeacher = teacherRepository.findById(dbEduStudentBuyCourse.getTeacherId()).orElseGet(EduTeacher::new);
        BigDecimal ratio = new BigDecimal(0.5);// 讲师提成比例50%
        // 计算提成金额
        BigDecimal amount = dbEduStudentBuyCourse.getPrice().multiply(ratio);

        // 处理null值
        if(dbEduTeacher.getIncomeAmount()==null){
            dbEduTeacher.setIncomeAmount(BigDecimal.ZERO);
        }
        if(dbEduTeacher.getCashOutMoney()==null){
            dbEduTeacher.setCashOutMoney(BigDecimal.ZERO);
        }
        dbEduTeacher.setIncomeAmount(dbEduTeacher.getIncomeAmount().add(amount));// 累计收入
        dbEduTeacher.setCashOutMoney(dbEduTeacher.getCashOutMoney().add(amount));// 可提现金额
        teacherRepository.save(dbEduTeacher);
        // 把记录保存到收益记录表
        EduTeacherIncomeDetails tempEduIncomeDetails = new EduTeacherIncomeDetails();
        tempEduIncomeDetails.setIncome(amount.doubleValue());// 收益金额
        tempEduIncomeDetails.setPrice(dbEduStudentBuyCourse.getPrice().doubleValue());// 课程金额
        tempEduIncomeDetails.setStudentId(dbEduStudentBuyCourse.getStudentId()); // 购买者ID
        tempEduIncomeDetails.setTeacherId(dbEduStudentBuyCourse.getTeacherId());// 讲师ID
        tempEduIncomeDetails.setOrderNo(orderNo);// 订单编号
        tempEduIncomeDetails.setCourseId(dbEduStudentBuyCourse.getCourseId());// 课程ID
        tempEduIncomeDetails.setCreateBy(dbEduStudentBuyCourse.getTeacherId()); // 创建者ID
        tempEduIncomeDetails.setUpdateBy(dbEduStudentBuyCourse.getTeacherId());// 更新者ID
        teacherIncomeDetailsRepository.save(tempEduIncomeDetails);

    }
}
