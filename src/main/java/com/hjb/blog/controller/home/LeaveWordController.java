package com.hjb.blog.controller.home;

import com.github.pagehelper.PageInfo;
import com.hjb.blog.entity.enums.OrderField;
import com.hjb.blog.entity.normal.LeaveWord;
import com.hjb.blog.entity.vo.ResultVO;
import com.hjb.blog.geetest.GeetestVerify;
import com.hjb.blog.service.normal.LeaveWordService;
import com.hjb.blog.util.CodeFilterUtils;
import com.hjb.blog.util.CommonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

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

    /**
     * 留言分页
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = {"/leaveWord", "/leaveWord/{pageNum}", "/leaveWord/{pageNum}/{pageSize}"})
    public String leaveWordPage(Model model,
                                @PathVariable(value = "pageNum", required = false) Integer pageNum,
                                @PathVariable(value = "pageSize", required = false) Integer pageSize) {
        // 设置默认值
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;

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
     * @return
     */
    @PostMapping("/send")
    @ResponseBody
    public ResultVO send(
            HttpServletRequest request,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "content", required = true) String content) {

        // 进行极验二次验证
        boolean result = false;
        try {
            result = GeetestVerify.verify(request, email);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (result) {
            LeaveWord leaveWord = new LeaveWord();
            leaveWord.setAvatar(CommonUtils.getGravatar(email));
            leaveWord.setEmail(email);
            leaveWord.setNickName(nickName);
            leaveWord.setContent(CodeFilterUtils.replaceGtAndLt(content));
            leaveWord.setIp(CommonUtils.getIpAddr(request));
            // 留言限制
            leaveWord.setRole(0);
            leaveWord.setStatus(true);
            leaveWordService.insertSelective(leaveWord);
            return ResultVO.ok("留言成功");
        }
        return ResultVO.fail("验证失败！");
    }
}
