package com.ant.mall.member;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallMemberApplicationTests {

    @Test
    void contextLoads() {
        String hex = DigestUtils.md5Hex("123456");
        byte[] bytes = DigestUtils.md5("123456");
        System.out.println("hex: "+hex+"\n"+"s: "+bytes);
    }

}
