package com.hjb.blog.field;

/**
 * session常量名
 *
 * @author 胡江斌
 * @version 1.0
 * @title: SessionFields
 * @projectName blog
 * @description: TODO
 * @date 2019/12/16 18:30
 */
public final class SessionFields {

    /**
     * 后台用户
     */
    public static final String ADMIN_USER = "user";
    /**
     * 后台用户id
     */
    public static final String ADMIN_USER_ID = "ADMIN_USER_ID";

    /**
     * 前台用户
     */
    public static final String BLOG_USER = "blogUser";

    /**
     * 登录前的请求地址
     */
    public static final String REQUEST_URL = "request_url";
    /**
     * 设置分页时当前的url
     */
    public static final String PAGE_URL_PREFIX = "pageUrlPrefix";
    /**
     * 文章分页
     */
    public static final String BLOG_PAGE_INFO = "pageInfo";
    /**
     * 标签列表
     */
    public static final String BLOG_TAG = "tag";
    /**
     * 标签列表
     */
    public static final String BLOG_TAG_LIST = "tagList";
    /**
     * 文章
     */
    public static final String BLOG_ARTICLE = "article";
    /**
     * 分类
     */
    public static final String BLOG_CATEGORY = "category";
    /**
     * 分类列表
     */
    public static final String BLOG_CATEGORY_LIST = "categoryList";
    /**
     * 文章列表
     */
    public static final String BLOG_ARTICLE_LIST = "articleList";
    /**
     * 文章评论列表
     */
    public static final String BLOG_COMMENT_LIST = "commentList";
    /**
     * 前台用户列表
     */
    public static final String BLOG_USER_LIST = "userList";

    /**
     * 相应到前端的信息
     */
    public static final String MESSAGE = "message";


    /**
     * jvtc用户
     */
    public static final String JVTC_USER = "jvtc_user";
    /**
     * jvtc用户id
     */
    public static final String JVTC_USER_ID = "JVTC_USER_ID";
    /**
     * jvtc机器人信息
     */
    public static final String JVTC_ROBOT_INFO = "robot_info";


    /**
     * 课表html代码
     */
    public static final String TABLE_HTML = "html";
    /**
     * 课表强制刷新按钮链接
     */
    public static final String TABLE_REFRESH_URL = "refreshUrl";


    /**
     * 图片验证码
     */
    public static final String IMG_CODE = "imgcode";


    /**
     * 分类列表
     */
    public static final String CATEGORIES = "categories";
    /**
     * 菜单列表
     */
    public static final String MENUS = "menus";
    /**
     * 通用信息
     */
    public static final String OPTION = "option";
    /**
     * 随机图片数量
     */
    public static final String IMAGES_QUANTITY = "images_quantity";
    /**
     * 异常信息
     */
    public static final String EXCEPTION_MSG = "exception_msg";
    /**
     * 教务处cookie
     */
    public static final String JVTC_COOKIE = "cookie";
    /**
     * 职教云验证码
     */
    public static final String VERIFY_CODE_COOKIE = "verify_code_cookie";

    /**
     * 职教云登录cookie
     */
    public static final String ICVE_COOKIE = "icve_cookie";


    private SessionFields() {

    }
}
