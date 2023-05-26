package cn.xueden.edu.vo;

import lombok.Data;

/**功能描述：密码实体对象
 * @author:梁志杰
 * @date:2023/5/26
 * @description:cn.xueden.edu.vo
 * @version:1.0
 */
@Data
public class PassWordModel {
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 验证码
     */
    private String code;

    /**
     * 原密码
     */
    private String passWord;

    /**
     * 新密码
     */
    private String newPassWord;

    /**
     * 确认新密码
     */
    private String resNewPassWord;
}
