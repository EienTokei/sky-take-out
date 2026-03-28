package com.sky.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT（JSON Web Token）工具类
 * <p>
 * 提供 JWT 的生成与解析功能。使用 HS256 算法（HMAC-SHA256）进行签名，
 * 密钥以字符串形式传入，内部转换为字节数组。
 * </p>
 */
public class JwtUtil {
    /**
     * 生成 JWT
     * <p>
     * 根据提供的密钥、有效期和自定义声明（claims）生成一个 JWT 字符串。
     * 使用 HS256 算法进行签名，过期时间设置为当前时间加上指定毫秒数。
     * </p>
     *
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return 生成的 JWT 字符串
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // 指定签名的时候使用的签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(exp);

        return builder.compact();
    }

    /**
     * Token解密
     *
     * @param secretKey jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token     加密后的token
     * @return JWT 的负载（Claims 对象），包含标准声明和自定义声明
     */
    public static Claims parseJWT(String secretKey, String token) {
        // 得到DefaultJwtParser
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置需要解析的jwt, 这个过程会自动验证签名和过期时间
                .parseClaimsJws(token)
                // 从解析结果中取 .getBody()，得到 Claims 对象
                .getBody();
    }

}
