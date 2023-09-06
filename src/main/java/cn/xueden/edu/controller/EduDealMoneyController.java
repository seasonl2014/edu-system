package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.edu.service.IEduDealMoneyService;
import cn.xueden.edu.service.dto.EduDealMoneyQueryCriteria;

import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**功能描述：成交金额前端控制器
 * @author:梁志杰
 * @date:2023/9/5
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("dealMoney")
public class EduDealMoneyController {

    private final IEduDealMoneyService eduDealMoneyService;

    public EduDealMoneyController(IEduDealMoneyService eduDealMoneyService) {
        this.eduDealMoneyService = eduDealMoneyService;
    }

    @EnableSysLog("【后台】获取成交金额列表数据")
    @GetMapping
    public ResponseEntity<Object> getList(EduDealMoneyQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(eduDealMoneyService.getList(queryCriteria,pageable), HttpStatus.OK);
    }
}
