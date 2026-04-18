4步：
1、编写JwtUtils
生成一个密钥
private static final String SECRET = "David_wannnng";

五个方法：
createJwt(String userId, String username, List<String> authority): 设置创建实现， 过期时间， 响应头(Map<String, Object>) 生成JWT(auth0包的)
verityJwt(String jwt)：
public static Boolean verifyJWT(String jwt){
        try{
            // 使用秘钥创建一个解析对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            //验证JWT
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

getUserId(String jwt)
public static String getUserId(String jwt){
        try{
            //使用密钥创建一个解析对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            //验证jwt
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
            //获取用户id
            Claim userId = decodedJWT.getClaim("userId");
            return userId.asString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

getUsername(String jwt)
大差不差
getAuthority(List<String> authority)
大差不差

2、编写JwtFilter
设置字符编码->获取url->判断是否是登录请求->如果是：则对其放行->如果不是：则获取初始jwt->判断jwt是否为空->获取真正jwt(replace("Bearer ", ""))->使用工具类对其进行验证->如果正确则继续，错误则中断->利用jwt获取用户id， 用户名， 用户权限->将它们存储在user对象中->将user对象放入security中

3、在successHandler添加
result后面—>获取user对象->提取userId, username, authority->创建jwt

4、config类
httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)，记得是在captchaFilter后




补充：
一、什么是“无状态”（Stateless）
无状态的本质：服务器不保存客户端的会话信息。
👉 换句话说：
每一次请求都是“独立的”
服务器不记得你是谁
客户端必须自己带上所有身份信息
✔ 举个对比
有状态（Stateful）
比如传统 Session：
用户登录
服务器生成 session，存到内存/Redis
返回 sessionId 给浏览器（cookie）
后续请求：
浏览器带 sessionId
服务器去查 session → 确认身份
👉 服务器保存了用户状态（session）
无状态（Stateless）
比如 JWT：
用户登录
服务器生成 JWT（里面包含用户信息）
返回给客户端
后续请求：
客户端带 JWT
服务器只做一件事：验证JWT是否合法
👉 服务器不存任何用户信息
二、为什么 JWT 是无状态的？
因为 JWT 本身就是一个“自包含”的令牌：
一个JWT结构：
Header.Payload.Signature
Payload里可能包含：
{
  "userId": 1001,
  "username": "rainer",
  "exp": 1710000000
}
👉 关键点：
用户信息 已经在 token 里了
服务器只需要：
验签（Signature）
判断是否过期
✔ 不需要：
查数据库
查 session
查缓存
👉 所以：JWT天然是无状态的
三、为什么“存入 Redis 就变成有状态”？
这一步是很多人容易混淆的重点。
🔥 核心变化点：你开始“依赖服务器存储了”
情况1：纯JWT（无状态）
请求流程：
客户端 → 带JWT → 服务器验证 → 通过
✔ 服务器：
不查任何存储
不保存任何数据
👉 无状态 ✅
情况2：JWT + Redis
你这样做了：
redis.set("token:xxx", userInfo)
请求时：
1. 解析JWT
2. 去Redis查这个token是否存在
👉 流程变成：
客户端 → JWT → 服务器 → 查Redis → 判断
🚨 本质变化
对比点	纯JWT	JWT + Redis
是否查服务器存储	❌	✅
是否保存用户状态	❌	✅
是否依赖外部存储	❌	✅
👉 一旦你依赖Redis中的数据来判断登录状态
✔ 那就是：有状态了
四、为什么很多项目“故意”把JWT做成有状态？
因为纯JWT有几个致命问题：
❗问题1：无法主动下线
纯JWT：
token没过期 → 永远有效
用户无法“强制退出”
👉 安全风险很大
❗问题2：无法踢人下线
比如：
用户改密码
管理员封号
👉 JWT仍然有效（因为你没存它）
❗问题3：无法做单点登录控制
比如：
一个账号只能一个设备登录
👉 纯JWT做不到
五、Redis的作用（让JWT“可控”）
引入Redis后：
✔ 可以做到：
token黑名单（注销）
强制下线
单点登录
登录状态控制
token续期
📌 实际项目常见方案
方案1：JWT + Redis（主流）
JWT：用于认证（身份）
Redis：用于状态控制
👉 本质：半无状态（Hybrid）
方案2：只用JWT（完全无状态）
适合：
简单系统
内部系统
安全要求不高
方案3：Session + Redis（传统）
完全有状态
六、一句话总结
👉 无状态 = 服务器不存你
👉 有状态 = 服务器记住你
七、再给你一个“面试级总结”⭐
你可以这样回答：
JWT之所以是无状态的，是因为它将用户信息编码在token中，服务器只需验证签名即可完成认证，不需要存储任何会话信息。
但如果将JWT存入Redis，并在每次请求时依赖Redis校验token是否存在或有效，就引入了服务器端状态，从而变成了有状态架构。本质上是因为认证过程依赖了服务端存储。