package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;

import cn.xueden.edu.domain.EduStudentBuyVip;
import cn.xueden.edu.domain.EduVipType;
import cn.xueden.edu.service.IEduStudentBuyVipService;
import cn.xueden.edu.service.IEduVipTypeService;

import cn.xueden.edu.service.dto.EduVipTypeQueryCriteria;
import cn.xueden.utils.PageVo;
import cn.xueden.utils.XuedenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**功能描述：VIP类别前端控制器
 * @author:梁志杰
 * @date:2023/5/11
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("vipType")
public class EduVipTypeController {

    private final IEduVipTypeService eduVipTypeService;

    private final IEduStudentBuyVipService eduStudentBuyVipService;

    public EduVipTypeController(IEduVipTypeService eduVipTypeService, IEduStudentBuyVipService eduStudentBuyVipService) {
        this.eduVipTypeService = eduVipTypeService;
        this.eduStudentBuyVipService = eduStudentBuyVipService;
    }

    @EnableSysLog("【后台】获取VIP类别列表数据")
    @GetMapping
    public ResponseEntity<Object> getList(EduVipTypeQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(eduVipTypeService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    @EnableSysLog("【后台】添加类别数据")
    @PostMapping
    public BaseResult addEduVipType(@RequestBody EduVipType eduVipType){
        eduVipTypeService.addEduVipType(eduVipType);
        return BaseResult.success("添加成功");
    }

    @EnableSysLog("【前台】获取所有会员类型")
    @GetMapping("getAllVip")
    public BaseResult getAllVip(){
        return BaseResult.success(eduVipTypeService.findAll());
    }

    @EnableSysLog("【前台】购买会员")
    @GetMapping("buyVip/{id}")
    public BaseResult buyVip(@PathVariable Long id,
                             HttpServletRequest request){
        String studentToken = request.getHeader("studentToken");
        if(studentToken==null||studentToken.equals("null")){
            return BaseResult.fail("购买失败，请先登录！");
        }
        if(id==null){
            return BaseResult.fail("购买失败");
        }
        // 获取用户IP地址
        String ipAddress = XuedenUtil.getClientIp(request);
        EduStudentBuyVip eduStudentBuyVip = eduStudentBuyVipService.buyVip(id,studentToken,ipAddress);
        return BaseResult.success(eduStudentBuyVip);
    }

    @EnableSysLog("【前台】根据订单编号获取订单详情")
    @GetMapping("getOrderInfo/{orderNo}")
    public BaseResult getOrderInfo(@PathVariable String orderNo){
       return BaseResult.success(eduStudentBuyVipService.getOrderInfo(orderNo)) ;
    }

    @EnableSysLog("【前台】去支付生成支付二维码链接")
    @GetMapping("payBuy/{orderNo}")
    public BaseResult payBuy(@PathVariable String orderNo){
        return BaseResult.success(eduStudentBuyVipService.payBuy(orderNo)) ;
    }

}