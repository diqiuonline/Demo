剑指spring 容器javaBean 初始化源码及springAOP {.cjk}
=============================================

首先module的架构

![](image/doc_html_87c04de7752bcbbb.png)

配置类

\

@Configuration\
 @ComponentScan("com.dhcc")\
 @EnableAspectJAutoProxy\
 public class Appconfig {\
 \
 }

一个接口

\

public interface L {\
 public void query();\
 }

接口实现类

\

@Component("c")\
 //@EnableAspectJAutoProxy(proxyTargetClass= true)\
 public class CityService implements L {\
 \
 public CityService() {\
 System.*out*.println("原生电影被拍摄");\
 }\
 @Override\
 public void query() {\
 System.*out*.println("query db ----");\
 }\
 }

aop配置类

\

@Component\
 @Aspect\
 public class dhccAspect {\
 @Pointcut("execution(\* com.dhcc.service..\*.\*(..))")\
 public void pointCut() {\
 }\
 \
 @Before("pointCut()")\
 public void advice(JoinPoint pjp) {\
 System.*out*.println("aop --before--advice");\
 }\
 }

main方法

\

public class test {\
 public static void main(String[] args) {\
 AnnotationConfigApplicationContext ac = new
AnnotationConfigApplicationContext(Appconfig.class);\
 L l = (L) ac.getBean("c");\
 l.query();\
 }\
 \
 }

执行结果

![](image/doc_html_ee98b606cd1e7ef6.png)

猜测一：

将getBean（）中的参数换位cityservice.class 还能不能获取到呢？

![](image/doc_html_4e96f9260653d36b.png)

答案是否定的 可以看到cityservice已经初始化成功
但是spring容器中并没有cityservice这个类，这是应为使用了AOP
spring容器中存在的是一个cityservice的动态代理对象。

可以从这里看到

![](image/doc_html_c0bf003240331c45.png) ![](image/doc_html_bf823c4a70ae98b2.png)
![](image/doc_html_93073fcc1f9f7e0b.png) ![](image/doc_html_9c1f84e3087ccb51.png)
![](image/doc_html_e7e36d4b58df15c7.png) ![](image/doc_html_18645d9aa0c77cb2.png)

debug跟踪到从spring容器中获取 获取到的是一个动态代理对象

![](image/doc_html_2435aa49237e1673.png)

此时 源对象已经初始化。

思考，源对象是从哪里初始化的，又是在什么地方被代理？

想法，spring容器是一个单例池的hashmap singletonObjects 获取对象为

singletonObjects.get，因此 绝对存在一个singletonObjects.put的地方。

idea 全局搜索

![](image/doc_html_fbe826029a653372.png)

进行条件断点debug

![](image/doc_html_cf8ec4c80538afef.png)

![](image/doc_html_bd4d93a9cfd0dd11.png)

发现存入的是一个代理对象，从栈帧中观察

![](image/doc_html_75c1208df7039bb9.png)

从main方法入口 里面绝对存在初始化源对象并且动态代理的地方。挨个查找

![](image/doc_html_22c8514ad665d296.png)

找到一个方法，这个方法的下一步已经是一个代理对象。在这里条件debug

进入getSingleton(beanName, () -\> {

发现再次进入getSingleton(String beanName, ObjectFactory\<?\>
singletonFactory)方法中

![](image/doc_html_377460d539f8c14d.png)

此时获取map中的bean对象并未获取到 进入if判断 debug执行

![](image/doc_html_fb4f8bec10147abb.png)

发现这个try里面的语句执行后代理对象已经生成 debug进入

发现进入到前面出现的那个lambda表达式里面的代码

![](image/doc_html_88fcea7483189565.png)

继续进入这个createBean(beanName, mbd, args)方法

![](image/doc_html_432e4837e6f34f70.png)

继续debug

![](image/doc_html_c33cdd09d42c6a4e.png)

这句话执行完之后已经是一个代理对象 说明这句代码里面有东西

继续进入doCreateBean(beanName, mbdToUse, args)

![](image/doc_html_918e40a0d8f07fa9.png)

继续debug

![](image/doc_html_f40c2c3040c9a358.png)

这句执行完之后 cityservice 初始化完毕

进入

![](image/doc_html_36764b77382617e4.png)

一番操作，

![](image/doc_html_dc3794b583dec90c.png)

继续进入

![](image/doc_html_892aad1108c8ee26.png)

发现初始化源对象的语句 继续进入

![](image/doc_html_1dd9fe1f54b2a08e.png)

继续debug

![](image/doc_html_5f3e73eec4144f6f.png)

进入

![](image/doc_html_fc0aebc5287b3a15.png) 进入

![](image/doc_html_1b23b0fa8dfd75a2.png)

无参构造方法 源对象初始化完毕。

回到这里 原始bean初始化完毕 继续回到doCreateBean

![](image/doc_html_b1bb5bfc3ec0708.png)

![](image/doc_html_e7f9da67eb5f05c3.png)

这句话执行完毕后代理对象出现，debug进入

![](image/doc_html_862b128d64c010ec.png)

![](image/doc_html_c23feb756a768c85.png)

继续进入

![](image/doc_html_7b12afd0306af312.png)

这个方法就是生成aop代理对象的方法 循环中增加了

AnnotationAwareAspectJAutoProxyCreator 这个循环中生成代理对象

![](image/doc_html_544241648c3a5cac.png)

题外话 如果在配置类中将aop取消

\

@Configuration\
 @ComponentScan("com.dhcc")\
 //@EnableAspectJAutoProxy\
 public class Appconfig {\
 }

这个方法的循环中将没有这个类

![](image/doc_html_f01162ace1e36168.png)

回到方法循环中

![](image/doc_html_c6edc3963d16175c.png)

等到processor等于AnnotationAwareAspectJAutoProxyCreator这个类 进入

![](image/doc_html_9a2314505e5e0aea.png)

继续进入

![](image/doc_html_c2703621b83e40f2.png)

生成代理对象

![](image/doc_html_bcb2b2fa561d930f.png)

其中

![](image/doc_html_54eb8459457a2018.png)

获取所有切面

![](image/doc_html_d67b47b7ee1e1c53.png)

生成并返回代理对象，一直返回到getSingleton(String beanName,
ObjectFactory\<?\> singletonFactory)方法 这是已经生成代理对象

![](image/doc_html_e8ba38df816aa64d.png)

继续向下 这个方法进入

![](image/doc_html_b1dfaa667ccd0fdc.png)

![](image/doc_html_a7a4c85a1bd3d6e4.png)

将代理对象put到spring容器之中 至此 一个代理对象创建完毕

\

\

从源码可以看到 spring容器初始化的时候 会首先getbean 当获取不到
调用getSingleton方法初始化bean
