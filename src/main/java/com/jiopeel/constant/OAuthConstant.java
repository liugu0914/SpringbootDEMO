package com.jiopeel.constant;

public class OAuthConstant {

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
    public final static String local_toekn = "http://localhost:8088/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s";


    /**
     *  本地 user获取地址
     */
    public final static String local_user = "http://localhost:8088/login/oauth/user?client_id=%s&client_secret=%s&code=%s";

    /**
     * 本地 Client id
     */
    public final static String local_client_id = "2NJBuBGlG52PthBp";

    /**
     * 本地 Client Secret
     */
    public final static String local_client_select = "GoN2CPwX5xfnn3mX3b4TcwbK9OuQPx7F";


    /**
     *  github token获取地址
     */
    public final static String GITHUB_TOKEN = "https://github.com/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s";

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
