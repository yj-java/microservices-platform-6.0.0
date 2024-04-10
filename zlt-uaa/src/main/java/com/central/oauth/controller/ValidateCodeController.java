package com.central.oauth.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.central.common.constant.SecurityConstants;
import com.central.common.model.Result;
import com.central.oauth.service.IValidateCodeService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * 验证码提供
 * @author zlt
 * @date 2018/12/18
 */
@Controller
public class ValidateCodeController {
    @Autowired
    private IValidateCodeService validateCodeService;

    /**
     * 创建验证码
     *
     * @throws Exception
     */
    @GetMapping(SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/{deviceId}")
    public void createCode(@PathVariable String deviceId, HttpServletResponse response) throws IOException {
        Assert.notNull(deviceId, "机器码不能为空");
        Assert.notNull(deviceId, "机器码不能为空");
        // 设置请求头为输出图片类型
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 三个参数分别为宽、高、验证码字符数、干扰线宽度
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(100, 35, 4, 10);
        // 保存验证码
        validateCodeService.saveImageCode(deviceId, captcha.getCode().toLowerCase());
        // 输出图片流
        captcha.write(response.getOutputStream());
    }

    /**
     * 发送手机验证码
     * 后期要加接口限制
     *
     * @param mobile 手机号
     * @return R
     */
    @ResponseBody
    @GetMapping(SecurityConstants.MOBILE_VALIDATE_CODE_URL_PREFIX + "/{mobile}")
    public Result<String> createCode(@PathVariable String mobile) {
        Assert.notNull(mobile, "手机号不能为空");
        return validateCodeService.sendSmsCode(mobile);
    }
}
