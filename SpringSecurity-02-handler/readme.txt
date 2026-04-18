handler包含四个
AuthenticationSuccessHandler、AuthenticationFailureHandler、 AccessDeniedHandler、 LogOutSuccessHandler
记得加上@Component注解
编写完后去SecurityConfiguration进行配置
handler编写步骤
配置字符编码->设置Result->转json格式->相应json
SuccessHandler比较特殊
配置字符编码->设置Result->创建jwt->将jwt数据存入redis中->转json格式->相应json
