package com.jiopeel.constant;

public class OauthConstant {


    /**
     * access_token
     */
    public final static String ACCESS_TOKEN = "access_token";

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
    public final static String EDIRECT_URI = "http://localhost:8088/oauth/redirect";

    /**
     * 本地授权登陆地址
     */
    public final static String local_url = "http://localhost:8088/index?client_id=%s&redirect_uri=%s";

    /**
     *  本地 token获取地址
     */
    public final static String local_token = "http://localhost:8088/login/oauth/access_token";


    /**
     * 本地 Client id
     */
    public final static String local_client_id = "2NJBuBGlG52PthBp";

    /**
     * 本地 Client Secret
     */
    public final static String local_client_secret = "GoN2CPwX5xfnn3mX3b4TcwbK9OuQPx7F";


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
     * github Client id
     */
    public final static String GITHUB_CLIENT_ID = "395270e87d56739d0e76";

    /**
     * github Client Secret
     */
    public final static String GITHUB_CLIENT_SECRET = "3bfd08ca179eab7c80b7929ef3678b64588f44d8";

}
