package cn.xueden.edu.service;

/**功能描述：讲师收入明细业务接口
 * @author:梁志杰
 * @date:2023/5/18
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduTeacherIncomeDetailsService {
    /**
     * 学员购买课程成功后计算讲师收入
     * @param orderNo
     */
    void teacherIncome(String orderNo);
}
