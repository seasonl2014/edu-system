package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduStudentBuyCourse;
import cn.xueden.edu.service.dto.EduOrderCourseQueryCriteria;
import cn.xueden.edu.vo.RefundOrderCourseModel;
import cn.xueden.edu.vo.StudentBuyCourseModel;
import org.springframework.data.domain.Pageable;

/**功能描述：学员购买课程业务接口
 * @author:梁志杰
 * @date:2023/5/17
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduStudentBuyCourseService {
    /**
     * 学员购买课程
     * @param id
     * @param token
     * @param ipAddress
     * @return
     */
    EduStudentBuyCourse buy(Long id, String token, String ipAddress);

    /**
     * 根据订单编号获取订单信息
     * @param orderNo
     * @return
     */
    EduStudentBuyCourse getByOrderNumber(String orderNo);


    /**
     * 购买课程立即付款
     * @param orderNo
     * @return
     */
    Object pay(String orderNo);

    /**
     * 更新记录购买状态
     * @param pay
     */
    void updatePayment(EduStudentBuyCourse pay);

    /**
     * 根据课程ID和学员ID查找记录
     * @param courseId
     * @param studentId
     * @return
     */
    EduStudentBuyCourse findByCourseIdAndStudentId(Long courseId, Long studentId);

    /**
     * 获取课程订单明细列表数据
     * @param pageable
     * @return
     */
    Object getList(EduOrderCourseQueryCriteria queryCriteria,Pageable pageable);

    /**
     * 根据id获取课程订单详情信息
     * @return
     */
    StudentBuyCourseModel getById(Long id);

    /**
     * 课程订单退款
     * @param refundOrderCourseModel
     */
    void refundCourseOrder(RefundOrderCourseModel refundOrderCourseModel);
}
