package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.edu.service.IEduRefundService;

import cn.xueden.edu.service.dto.EduRefundQueryCriteria;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**功能描述：退款记录前端控制器
 * @author:梁志杰
 * @date:2023/9/6
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("refund")
public class EduRefundController {

    private final IEduRefundService eduRefundService;

    public EduRefundController(IEduRefundService eduRefundService) {
        this.eduRefundService = eduRefundService;
    }

    @EnableSysLog("【后台】获取退款记录列表数据")
    @GetMapping
    public ResponseEntity<Object> getList(EduRefundQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(eduRefundService.getList(queryCriteria,pageable), HttpStatus.OK);
    }
}
