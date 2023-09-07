package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduCouponStock;
import cn.xueden.edu.service.IEduCouponStockService;
import cn.xueden.edu.service.dto.EduCouponQueryCriteria;

import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**功能描述：代金券前端控制器
 * @author:梁志杰
 * @date:2023/9/6
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("coupon")
public class EduCouponController {

    private final IEduCouponStockService eduCouponService;

    public EduCouponController(IEduCouponStockService eduCouponService) {
        this.eduCouponService = eduCouponService;
    }

    @EnableSysLog("【后台】获取代金券列表数据")
    @GetMapping
    public ResponseEntity<Object> getList(EduCouponQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(eduCouponService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    @EnableSysLog("【后台】创建代金券批次")
    @PostMapping
    public BaseResult createCouponStock(@RequestBody EduCouponStock eduCoupon){
        eduCouponService.createCouponStock(eduCoupon);
        return BaseResult.success("创建成功");
    }

    @EnableSysLog("【后台】生成代金券批次")
    @PostMapping("generate/{id}")
    public BaseResult generateCouponStock(@PathVariable Long id){
        eduCouponService.generateCouponStock(id);
        return BaseResult.success("创建成功");
    }

    @EnableSysLog("【后台】发放代金券")
    @PostMapping("send")
    public BaseResult sendCoupon(@RequestParam Long couponId,
                                 @RequestParam String studentNo){
        eduCouponService.sendCoupon(couponId,studentNo);
        return BaseResult.success("发放代金券到学员"+studentNo+"成功");
    }

    @EnableSysLog("【后台】激活开启批次")
    @PostMapping("start/{couponId}")
    public BaseResult startStock(@PathVariable Long couponId){
        eduCouponService.startStock(couponId);
        return BaseResult.success("ID为"+couponId+"的代金券批次启动成功");
    }

    @EnableSysLog("【后台】查询指定批次详情")
    @GetMapping("stock/query/{couponId}")
    public BaseResult queryStock(@PathVariable Long couponId){
        eduCouponService.queryStock(couponId);
        return BaseResult.success("ID为"+couponId+"的代金券查询成功");
    }


}
