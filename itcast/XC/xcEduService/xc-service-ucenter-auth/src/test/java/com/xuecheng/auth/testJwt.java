package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.HashMap;
import java.util.Map;

/**
 * User: 李锦卓
 * Time: 2019/5/18 18:06
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class testJwt {

    @Test
    public void createJwt(){
        //创建jwt令牌
        //密钥库文件
        String keystore = "xc.keystore";
        //密钥库的密码
        String keystore_password = "xuechengkeystore";
        //密钥库文件路径
        ClassPathResource classPathResource = new ClassPathResource(keystore);
        //密钥别名
        String alias  = "xckey";
        //密钥的访问密码
        String key_password = "xuecheng";
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, keystore_password.toCharArray());
        //密钥对
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, key_password.toCharArray());
        //获取私钥
        RSAPrivateCrtKey aPrivate = (RSAPrivateCrtKey) keyPair.getPrivate();
        Map<String, String> map = new HashMap<>();
        map.put("name", "dhcc");
        String jsonString = JSON.toJSONString(map);
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(jsonString, new RsaSigner(aPrivate));
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
        //eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiZGhjYyJ9.PSGgqkuVD4JwSVLPkX46T2dA5sevl4v8Xc1TGd3_xLJzOVUbRFOqNZAwxVFJHigcEF2-U8RiZJAk6i0bZt0jagA99TgyeW2BlLZOHrrrR92WutIska8nJeYF66SCmmkSvlHErXUm8d5jW5b9Ij_brkUdeUzd_RF5gF1sQfMAz2ojIureksvr8VeB1vzAuwzLfhd-ntCMGvkx1pdwKu54sGpS17TV8cWeLoEE6-UvHiNZ0s_9T_ABCHjgYqA0E7QNV3vnNNHrSB9XRT1n_0GT52t45w-MV9TJGpXNc3aT1riiZCb3TGH9U0G9fEWGkfmdmzPCw_1xUD_vDOq37AZ91A


    }

    //验证jwt令牌
    @Test
    public void testVerify(){
        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoe6FLQShhESdjSlhbuUjyajguPYXjcKpsC5onPhf59i2aUoiuma2jrBZe4Rhueg2bSVVekyHa3ZjvW4SUYrQgOL7RRLHPZ17xTGdmCWmraUI1YpxZlkkgwPZDKTkNEuyv8bqDqjpQWVTOMRtUFAHiW0AIEAQGOwq8rDqy7vMsZph3qePQgaiKIAtviQ3KWmAaTSgg4MgoeS3vrdLrCD02HA8it2yfwViL5ZdTOqw+P2nEm1rkZjBEmO7IMX0FPinsdI4kE4wDKSq079OtBjE1YZfqwOuJBo4w/dl6LLN7NDDe/113xQVjW+ebzdlfCY6kBILUlHZZ1UKwncjSjEvIwIDAQAB-----END PUBLIC KEY-----";
        //jwt令牌
        String jwtstring = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiaXRjYXN0Iiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiLmnY7plKbljZMiLCJ1dHlwZSI6IjEwMTAwMiIsImlkIjoiNDkiLCJleHAiOjE1NjEyNjE4MjMsImF1dGhvcml0aWVzIjpbInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYmFzZSIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfZGVsIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9saXN0IiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9wbGFuIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZSIsImNvdXJzZV9maW5kX2xpc3QiLCJ4Y190ZWFjaG1hbmFnZXIiLCJ4Y190ZWFjaG1hbmFnZXJfY291cnNlX21hcmtldCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfcHVibGlzaCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYWRkIl0sImp0aSI6IjU4OTk4NmNhLThmMzktNGFjZC1iZjdkLWZiNjczZDdjMzc5NCIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.gHccfv5ySKFpUlbQfdjC9iY_9laVb9iT_5hmiLrhMIh3D4j9q_2tAfHlGyqDfKWsy8VuJe8-GpNHfnfN3e_RvBA8XkQ55m7Y0IcguC8mm1mmwAb1afXkV3b8LYt5LBeutfgkPH7Mumdgn7Kfkf7sNFjkslctQmNVKmvf1aq4iM5an1YZdzgIXwW6w-3jZzLi6vuxzgKCFp6zNUsgbaZcefd6uiZll8wF_Zccil01qEikazrMnbKVDfoUL7jgyQFizkz5FGVlgfK9Kg0DLR1P0qqr0uk3cOG9D7efgdRbnEeiCHpuRcslFBbtabY-qj22DV_B8o13uPOACsILQIMOgg";
        //校验jwt令牌
        Jwt jwt = JwtHelper.decodeAndVerify(jwtstring, new RsaVerifier(publickey));
        String claims = jwt.getClaims();
        System.out.println(claims);

    }

}