package cn.xueden.edu.wechat.controller;

import cn.xueden.edu.wechat.handle.WeChatMessageHandle;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**功能描述：微信公众平台前端控制器
 * @author:梁志杰
 * @date:2023/9/8
 * @description:cn.xueden.edu.wechat.controller
 * @version:1.0
 */
@RestController
@RequestMapping("/edu/wechat")
public class WeChatController {

    @Autowired
    private WeChatMessageHandle weChatService;

    @GetMapping
    public String validate(@RequestParam("signature") String signature,
                           @RequestParam("timestamp") String timestamp,
                           @RequestParam("nonce") String nonce,
                           @RequestParam("echostr") String echostr) {
        // 验证服务器配置
        if (weChatService.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }
        return "Invalid request";
    }

    @RequestMapping(method = {RequestMethod.POST})
    public String handleMessage(HttpServletRequest request) {
        // 处理微信消息
        return weChatService.handleMessage(request);
    }


}
