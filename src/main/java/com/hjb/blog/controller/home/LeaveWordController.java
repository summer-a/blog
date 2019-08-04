package com.hjb.blog.controller.home;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.dto.ResultVO;
import com.hjb.blog.entity.enums.OrderField;
import com.hjb.blog.entity.normal.LeaveWord;
import com.hjb.blog.service.normal.LeaveWordService;
import com.hjb.blog.util.CommonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 留言板控制器
 * @author 胡江斌
 * @version 1.0
 * @title: LeaveWordController
 * @projectName blog
 * @description: TODO
 * @date 2019/7/23 17:24
 */
@Controller
@RequestMapping("/ly")
public class LeaveWordController {

    @Resource
    private LeaveWordService leaveWordService;

    @GetMapping("/leaveWord")
    public String leaveWordPage(Model model,
                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        LeaveWord lw = new LeaveWord();
        lw.setStatus(true);
        PageInfo<LeaveWord> list = leaveWordService.page(pageNum, pageSize, lw, OrderField.orderByDesc("createTime"));
        model.addAttribute("leaveWords", list);
        return "Home/Page/leaveWord";
    }

    /**
     * 留言
     *
     * @param email
     * @param nickName
     * @param content
     * @param imgcode
     * @return
     */
    @PostMapping("/send")
    @ResponseBody
    public ResultVO send(
            HttpServletRequest request,
            @SessionAttribute("imgcode") String trueCode,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "content", required = true) String content,
            @RequestParam(value = "imgcode", required = true) String imgcode) {
        if (!StringUtils.isEmpty(trueCode) && !StringUtils.isEmpty(imgcode)) {
            if (trueCode.equals(imgcode)) {
                LeaveWord leaveWord = new LeaveWord();
                leaveWord.setAvatar(CommonUtils.getGravatar(email));
                leaveWord.setEmail(email);
                leaveWord.setNickName(nickName);
                leaveWord.setContent(content);
                leaveWord.setIp(CommonUtils.getIpAddr(request));
                leaveWordService.insertSelective(leaveWord);
                request.getSession().removeAttribute("imgcode");
                return ResultVO.ok("留言成功");
            }
        }
        return ResultVO.fail("验证码有误！");
    }
}
