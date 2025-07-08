package com.ouweicong.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Security extends BCryptPasswordEncoder  {
    //加密字符串
    @Override
    public String encode(CharSequence rawPassword) {
        return super.encode(rawPassword);
    }
    //对比明文和加密是否一致
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return super.matches(rawPassword, encodedPassword);
    }
    //判断是否由本身加密算法加密的
    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return super.upgradeEncoding(encodedPassword);
    }
}
