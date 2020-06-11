package com.jiopeel.core.constant;

public class OauthConstant {


    /**
     * access_token
     */
    public final static String ACCESS_TOKEN = "access_token";

    /**
     * reflesh_token
     */
    public final static String REFLESH_TOKEN = "reflesh_token";

    /**
     * code
     */
    public final static String CODE = "code";

    /**
     * Client id
     */
    public final static String CLIENT_ID = "client_id";

    /**
     * Client secret
     */
    public final static String CLIENT_SECRET = "client_secret";

    /**
     * 本地回调地址
     */
    public final static String REDIRECT_URI = "http://%s/oauth/redirect";

    /**
     * 本地授权登陆地址
     */
    public final static String local_url = "http://%s/index?client_id=%s&redirect_uri=%s";

    /**
     *  本地 token获取地址
     */
    public final static String local_token = "http://%s/oauth/access_token";


    /**
     * 本地 Client id
     */
    public final static String local_client_id = "2NJBuBGlG52PthBp";

    /**
     * 本地 Client Secret
     */
    public final static String local_client_secret = "GoN2CPwX5xfnn3mX3b4TcwbK9OuQPx7F";


    /**
     * github Client id
     */
    public final static String GITHUB_CLIENT_ID = "395270e87d56739d0e76";

    /**
     * github Client Secret
     */
    public final static String GITHUB_CLIENT_SECRET = "3bfd08ca179eab7c80b7929ef3678b64588f44d8";

    /**
     *  github token获取地址
     */
    public final static String GITHUB_TOKEN = "https://github.com/login/oauth/access_token";

    /**
     *  github user获取地址
     */
    public final static String GITHUB_USER = "https://api.github.com/user?access_token=%s";

    /**
     *  跳转 github url
     */
    public final static String GITHUB_URL = "https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s";



    /**
     * gitee Client id
     */
    public final static String GITEE_CLIENT_ID = "2458561c9d1dfb421125c04c197d71a80bc8508690f226e203d56cb41971de44";

    /**
     * gitee Client Secret
     */
    public final static String GITEE_CLIENT_SECRET = "87b41caa600cd946aad735122518f8450118c4dd612b8e2487efe2c723f69ced";


    /**
     *  gitee token获取地址
     */
    public final static String GITEE_TOKEN = "https://gitee.com/oauth/token";

    /**
     *  gitee user获取地址
     */
    public final static String GITEE_USER = "https://gitee.com/api/v5/user?access_token=%s";

    /**
     *  跳转 gitee url
     */
    public final static String GITEE_URL = "https://gitee.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code";

}
