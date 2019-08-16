package com.hjb.blog.util;

import com.alibaba.fastjson.JSON;
import com.hjb.blog.entity.vo.CqpVO;
import com.xiaoleilu.hutool.http.HttpUtil;
import org.nutz.lang.util.NutMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: CoolqUtils
 * @projectName blog
 * @description: TODO
 * @date 2019/6/24 22:26
 */
public class CoolqUtils {

    String HTTP_SERVER_HOST = "http://120.77.215.148:5700/";
    String SEND_PRIVATE_MSG = "send_private_msg";
    String SEND_GROUP_MSG = "send_group_msg";
    String SEND_DISCUSS_MSG = "send_discuss_msg";
    String SEND_MSG = "send_msg";
    String DELETE_MSG = "delete_msg";
    String SEND_LIKE = "send_like";
    String SET_GROUP_KICK = "set_group_kick";
    String SET_GROUP_BAN = "set_group_ban";
    String SET_GROUP_ANONYMOUS_BAN = "set_group_anonymous_ban";
    String SET_GROUP_WHOLE_BAN = "set_group_whole_ban";
    String SET_GROUP_ADMIN = "set_group_admin";
    String SET_GROUP_ANONYMOUS = "set_group_anonymous";
    String SET_GROUP_CARD = "set_group_card";
    String SET_GROUP_LEAVE = "set_group_leave";
    String SET_GROUP_SPECIAL_TITLE = "set_group_special_title";
    String SET_DISCUSS_LEAVE = "set_discuss_leave";
    String SET_FRIEND_ADD_REQUEST = "set_friend_add_request";
    String SET_GROUP_ADD_REQUEST = "set_group_add_request";
    String GET_LOGIN_INFO = "get_login_info";
    String GET_STRANGER_INFO = "get_stranger_info";
    String GET_GROUP_LIST = "get_group_list";
    String GET_GROUP_MEMBER_INFO = "get_group_member_info";
    String GET_GROUP_MEMBER_LIST = "get_group_member_list";
    String GET_VERSION_INFO = "get_version_info";
    String SET_RESTART = "set_restart";
    String SET_RESTART_PLUGIN = "set_restart_plugin";

    int timeOut = 3000;

    private static CoolqUtils instance = new CoolqUtils();

    private CoolqUtils() {
    }


    public static CoolqUtils getInstance() {
        return instance;
    }

    /**
     * 发送私聊消息
     *
     * @param qq      QQ号
     * @param message 消息
     * @return
     */
    public CqpVO sendPrivateMsg(long qq, String message) {
        NutMap map = new NutMap().setv("user_id", qq).setv("message", message);
        return sendMsg(SEND_PRIVATE_MSG, map);
    }

    /**
     * 发送群聊消息
     *
     * @param groupId 群ID
     * @param message 消息
     * @return
     */
    public CqpVO sendGroupMsg(long groupId, String message) {
        NutMap map = new NutMap("group_id", groupId).setv("message", message);
        return sendMsg(SEND_GROUP_MSG, map);
    }

    /**
     * 发送讨论组消息
     *
     * @param groupId 讨论组ID
     * @param message 消息
     * @return
     */
    public CqpVO sendDisCussMsg(long groupId, String message) {
        NutMap map = new NutMap("discuss_id", groupId).setv("message", message);
        return sendMsg(SEND_DISCUSS_MSG, map);
    }

    /**
     * 撤回消息
     *
     * @param messageId 消息ID
     * @return
     */
    public CqpVO deleteMsg(long messageId) {
        NutMap map = new NutMap().setv("message_id", messageId);
        String resp = HttpUtil.post(HTTP_SERVER_HOST + DELETE_MSG, map, timeOut);
        return JSON.parseObject(resp, CqpVO.class);
    }

    /**
     * 发送好友赞
     *
     * @param qq    QQ号
     * @param times 赞的次数，每个好友每天最多 10 次
     * @return
     */
    public CqpVO sendLike(long qq, long times) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", qq);
        map.put("times", times);
        String resp = HttpUtil.post(HTTP_SERVER_HOST + SEND_LIKE, map, timeOut);
        return JSON.parseObject(resp, CqpVO.class);
    }

    /**
     * 群组踢人
     *
     * @param qq      QQ
     * @param groupId 群号
     * @return
     */
    public CqpVO setGroupKick(long qq, long groupId) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", qq);
        map.put("group_id", groupId);
        String resp = HttpUtil.post(HTTP_SERVER_HOST + SET_GROUP_KICK, map, timeOut);
        return JSON.parseObject(resp, CqpVO.class);
    }

    /**
     * 群组单人禁言
     *
     * @param qq       QQ
     * @param groupId  群号
     * @param duration 禁言时长，单位秒，0 表示取消禁言
     * @return
     */
    public CqpVO setGroupBan(long qq, long groupId, long duration) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("user_id", qq);
        map.put("group_id", groupId);
        map.put("duration", duration);
        String resp = HttpUtil.post(HTTP_SERVER_HOST + SET_GROUP_BAN, map, timeOut);
        return JSON.parseObject(resp, CqpVO.class);
    }

    /**
     * 群组匿名用户禁言
     *
     * @param flag     要禁言的匿名用户的 flag（需从群消息上报的数据中获得）
     * @param groupId  群号
     * @param duration 禁言时长，单位秒，无法取消匿名用户禁言
     * @return
     */
    public CqpVO setGroupAnonymousBan(String flag, long groupId, long duration) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("flag", flag);
        map.put("group_id", groupId);
        map.put("duration", duration);
        String resp = HttpUtil.post(HTTP_SERVER_HOST + SET_GROUP_ANONYMOUS_BAN, map, timeOut);
        return JSON.parseObject(resp, CqpVO.class);
    }

    /**
     * 群组全员禁言
     *
     * @param groupId 群号
     * @param enable  是否禁言
     * @return
     */
    public CqpVO setGroupWholeBan(long groupId, boolean enable) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("group_id", groupId);
        map.put("enable", enable);
        String resp = HttpUtil.post(HTTP_SERVER_HOST + SET_GROUP_WHOLE_BAN, map, timeOut);
        return JSON.parseObject(resp, CqpVO.class);
    }

    /**
     * 群组设置管理员
     *
     * @param groupId 群号
     * @param qq      要设置管理员的 QQ 号
     * @param enable  true 为设置，false 为取消
     * @return
     */
    public CqpVO setGroupAdmin(long groupId, long qq, boolean enable) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("group_id", groupId);
        map.put("user_id", qq);
        map.put("enable", enable);
        String resp = HttpUtil.post(HTTP_SERVER_HOST + SET_GROUP_WHOLE_BAN, map, timeOut);
        return JSON.parseObject(resp, CqpVO.class);
    }

    /**
     * 获取群列表
     *
     * @return
     */
    public CqpVO getGroupList() {
        String resp = HttpUtil.get(HTTP_SERVER_HOST + GET_GROUP_LIST);
        return JSON.parseObject(resp, CqpVO.class);
    }

    /**
     * 获取群成员信息
     *
     * @param groupId 群号
     * @param qq      QQ 号（不可以是登录号）
     * @return
     */
    public CqpVO getGroupMemberInfo(long groupId, long qq) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("group_id", groupId);
        map.put("user_id", qq);
        String resp = HttpUtil.post(HTTP_SERVER_HOST + GET_GROUP_MEMBER_INFO, map, timeOut);
        return JSON.parseObject(resp, CqpVO.class);
    }

    /**
     * 获取群成员列表
     *
     * @param groupId 群号
     * @return
     */
    public CqpVO getGroupMemberList(long groupId) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("group_id", groupId);
        String resp = HttpUtil.post(HTTP_SERVER_HOST + GET_GROUP_MEMBER_LIST, map, timeOut);
        return JSON.parseObject(resp, CqpVO.class);
    }

    /**
     * 获取酷 Q 及 HTTP API 插件的版本信息
     *
     * @return
     */
    public CqpVO getVersionInfo() {
        return sendMsg(GET_VERSION_INFO);
    }

    /**
     * 重启酷 Q，并以当前登录号自动登录（需勾选快速登录）
     *
     * @return
     */
    public CqpVO setRestart() {
        return sendMsg(SET_RESTART);
    }

    /**
     * 重启 HTTP API 插件
     *
     * @return
     */
    public CqpVO setRestartPlugin() {
        return sendMsg(SET_RESTART_PLUGIN);
    }

    /**
     * 发送消息
     *
     * @param msgType
     * @return
     */
    public CqpVO sendMsg(String msgType) {
        String resp = HttpUtil.get(HTTP_SERVER_HOST + msgType, timeOut);
        return JSON.parseObject(resp, CqpVO.class);
    }

    /**
     * 发送消息
     *
     * @param msgType
     * @param msg
     * @return
     */
    public CqpVO sendMsg(String msgType, Map<String, Object> msg) {
        String resp = HttpUtil.post(HTTP_SERVER_HOST + msgType, msg, timeOut);
        return JSON.parseObject(resp, CqpVO.class);
    }

}
