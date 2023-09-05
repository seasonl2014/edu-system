package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.service.IEduStudentBuyCourseService;
import cn.xueden.edu.service.IEduStudentBuyVipService;
import cn.xueden.edu.service.dto.EduOrderCourseQueryCriteria;
import cn.xueden.edu.service.dto.EduStudentQueryCriteria;
import cn.xueden.edu.vo.RefundOrderCourseModel;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**功能描述：课程订单和VIP订单前端控制器
 * @author:梁志杰
 * @date:2023/9/4
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("order")
public class EduOrderController {

    private final IEduStudentBuyCourseService eduStudentBuyCourseService;

    private final IEduStudentBuyVipService eduStudentBuyVipService;

    public EduOrderController(IEduStudentBuyCourseService eduStudentBuyCourseService, IEduStudentBuyVipService eduStudentBuyVipService) {
        this.eduStudentBuyCourseService = eduStudentBuyCourseService;
        this.eduStudentBuyVipService = eduStudentBuyVipService;
    }

    @EnableSysLog("【后台】获取课程订单明细列表数据")
    @GetMapping("/course")
    public ResponseEntity<Object> getList(EduOrderCourseQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(eduStudentBuyCourseService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    @EnableSysLog("【后台】根据id获取课程订单详情信息")
    @GetMapping("/course/{id}")
    public BaseResult getCourseOrderById(@PathVariable Long id){
        return BaseResult.success(eduStudentBuyCourseService.getById(id));
    }

    @EnableSysLog("【后台】课程订单退款")
    @PostMapping("/course")
    public BaseResult refundCourseOrder(@RequestBody RefundOrderCourseModel refundOrderCourseModel){
        eduStudentBuyCourseService.refundCourseOrder(refundOrderCourseModel);
        return BaseResult.success("退款成功");
    }

    @EnableSysLog("【后台】获取VIP订单明细列表数据")
    @GetMapping("/vip")
    public ResponseEntity<Object> getVipList(EduOrderCourseQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(eduStudentBuyVipService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    @EnableSysLog("【后台】根据id获取VIP订单详情信息")
    @GetMapping("/vip/{id}")
    public BaseResult getVipOrderById(@PathVariable Long id){
        return BaseResult.success(eduStudentBuyVipService.getById(id));
    }

    @EnableSysLog("【后台】VIP订单退款")
    @PostMapping("/vip")
    public BaseResult refundVipOrder(@RequestBody RefundOrderCourseModel refundOrderCourseModel){
        eduStudentBuyVipService.refundVipOrder(refundOrderCourseModel);
        return BaseResult.success("退款成功");
    }
}
