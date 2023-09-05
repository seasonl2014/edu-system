package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduStudentBuyVip;
import cn.xueden.edu.service.dto.EduOrderCourseQueryCriteria;
import cn.xueden.edu.vo.RefundOrderCourseModel;
import cn.xueden.edu.vo.StudentBuyVipModel;
import org.springframework.data.domain.Pageable;

/**功能描述：学员购买VIP业务接口
 * @author:梁志杰
 * @date:2023/5/16
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduStudentBuyVipService {
    /**
     * 购买VIP
     * @param id
     * @param studentToken
     * @param ipAddress
     * @return
     */
    EduStudentBuyVip buyVip(Long id, String studentToken, String ipAddress);

    /**
     * 根据订单编号获取订单详情
     * @param orderNo
     * @return
     */
    EduStudentBuyVip getOrderInfo(String orderNo);

    /**
     * 去支付生成支付二维码链接
     * @param orderNo
     * @return
     */
    String payBuy(String orderNo);

    /**
     * 更新订单信息
     * @param pay
     */
    void updatePayment(EduStudentBuyVip pay);

    /**
     * 根据学员ID查询记录
     * @param studentId
     * @return
     */
    EduStudentBuyVip findByStudentId(Long studentId);


    /**
     * 获取VIP列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    Object getList(EduOrderCourseQueryCriteria queryCriteria, Pageable pageable);

    /**
     * 根据id获取VIP订单详情信息
     * @param id
     * @return
     */
    StudentBuyVipModel getById(Long id);

    /**
     * VIP订单退款
     * @param refundOrderCourseModel
     */
    void refundVipOrder(RefundOrderCourseModel refundOrderCourseModel);
}
