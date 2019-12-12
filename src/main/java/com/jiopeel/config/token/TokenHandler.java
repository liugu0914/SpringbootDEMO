package com.jiopeel.config.token;

import com.jiopeel.bean.User;

/**
 * token处理
 * @author lyc
 */
public interface TokenHandler {

    /**
     *  create token
     * @param user
     */
    public  String create(User user);

    /**
     * check token
     * @param token
     * @return boolean
     */
    public  boolean chk(String token);

    /**
     *  get user info from token
     * @param token
     * @return User
     */
    public User get(String token);


    /**
     * delete
     * @param token
     */
    public void del(String token);
}
