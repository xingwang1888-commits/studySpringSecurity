4步
1、使用mybatis生成实体类，mapper跟mapper.xml
2、实体类的user对象实现Serializable和UserDetails对象
3、创建ServiceImpl：
实现UserDetails接口->用用户名查询用户信息->判断用户是否存在(存在则继续，不存在则抛出异常)->利用用户ID查询用户权限->将权限放入user对象中->返回用户对象
4、配置类
httpSecurity.userDetailsService(你的ServiceImpl)

