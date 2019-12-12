package com.jiopeel.constant;

public class OAuthConstant {

    /**
     * 本地
     */
    public final static String EDIRECT_URI = "http://localhost:8088/oauth/redirect";

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
