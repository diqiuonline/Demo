spring的生命周期 
----------------

![](image/doc_html_fa5a4b3afe5fbe01.png) \

配置类

\

@ComponentScan("com.dhcc")\
 public class Appconfig {\
 }

X类

\

@Component\
 public class X {\
 @Autowired\
 private Y y;\
 \
 public X() {\
 System.*out*.println("create X");\
 }\
 }

\

Y类

\

@Component\
 public class Y {\
 @Autowired\
 private X x;\
 \
 public Y() {\
 System.*out*.println("create Y");\
 }\
 }

双方相互注入 行程循环依赖

spring容器初始化

\

public class Test {\
 public static void main(String[] args) {\
 AnnotationConfigApplicationContext ac = new
AnnotationConfigApplicationContext(Appconfig.class);\
 X bean = ac.getBean(X.class);\
 System.*out*.println(bean);\
 }\
 }

启动test类 在spring容器初始化的时候 他会解析Appconfig这个配置类
它会解析配置类中的ComponentScan
注解，扫秒com.dhcc这个包下所有的有Component注解的类 并且初始化

第一步 扫描类 
-------------

debug进入

![](image/doc_html_296f47ba54f0013c.png) \

第一步执行一个父类的无参构造方法 进入

![](image/doc_html_36d073c37f0ff615.png)

在执行无参构造方法的时候
先看看有没有父类有的话首先执行父类的无参构造方法

![](image/doc_html_57f3355fdb94a10d.png)

![](image/doc_html_6a6b514d20403468.png)

初始化了一个类型位DefaultListableBeanFactory 的beanFactory

这个beanFactory就是spring中常说的bean工厂

DefaultListableBeanFactory继承自beanFactory

![](image/doc_html_494a03b3f15ecd31.png)

由此可以知道 AnnotationConfigApplicationContext 容器使用的是

DefaultListableBeanFactory bean工厂 不同的spring容器使用的bean工厂不同
这些bean工厂都继承自beanFactory 这个接口

继续向下debug

![](image/doc_html_1b04ace812ca3fe3.png)

继续

![](image/doc_html_fe2302301bd9402a.png)

会发现beanDefinitionMap这个map中多了一个appconfig

说明register方法 将我们提供的appconfig放到这个map中

beanDefinitionMap这个map的主要作用是实例化bean 说明将来有一天 spring会将

appconfig这个类实例化

继续debug

![](image/doc_html_53668ebf83424c0c.png)

第二步 向beanDefinitionMap 这个map中putbd对象 
---------------------------------------------

运行完refresh（） 方法后 这个map中多了x，y 两个对象
说明refresh（）方法完成了扫秒

点进来

![](image/doc_html_ce15eb4fb3eb763f.png)

继续

![](image/doc_html_4cd345db7008cde1.png)

当运行完图中方法的时候 beanDefinitionMap 这个map中多了x和y

说明这个方法完成了扫秒

按照方法名字理解
它还完成了程序员或者spring内部的BeanFactoryPostProcessor
也就是常说的后置处理器 这里程序员可以展开扩展

![](image/doc_html_a0423b45eed8f760.png)

程序员实现这个接口 可以完成扩展

![](image/doc_html_c215a9581cc6c4c4.png)

这个时候spring还并没有实例化bean

继续向下debug

![](image/doc_html_a388d0578f19362f.png)

finishBeanFactoryInitialization 这个方法完成了x和y的实例化

继续debug进入

![](image/doc_html_f4e127065211f96b.png)

向下

![](image/doc_html_70a43a51f6f0a2c4.png)

![](image/doc_html_49cec44dbc54d09d.png)

beanFactory.preInstantiateSingletons(); 这个方法完成了x和y的实例化

继续debug进入

第三步 遍历这个map 
------------------

![](image/doc_html_299be191af9f0dd5.png)

首先获取到所有的bean的名字 这个beanNames 是
beanDefinitionMap这个map中的额key

第四步 验证 
-----------

![](image/doc_html_f4d952b741f49d8e.png)

获取到这个RootBeanDefinition
会对这个类进行验证，验证是不是抽象的，是不是单例的
是不是懒加载。。。。。

BeanDefinition中记录了这个类的所有信息 是不是懒加载 是不是单例
他的beanclass

题外话 则会个beanclass 可以通过后置处理器BeanFactoryPostProcessor修改
也就是说 你在x上加了@Component注解 你最后得到的不一定是x 也可以是y

![](image/doc_html_6c92ed7c2c7702f5.png)

这个if判断是不是抽象的，判断是不是单例的，判断是不是懒加载

然后判断是不是也给FactoryBean

题外话 这里是factoryBean 前面有beanFactory
这两有什么区别是spring常见的面试题

![](image/doc_html_8d79c27b2212ea53.png)

进入getBean

![](image/doc_html_919d85c13384211f.png)

![](image/doc_html_6bd07adc9d07a9f8.png)

进入dogetbean

final String beanName = transformedBeanName(name);

验证名字是否合法 一般都合法

![](image/doc_html_91ebd07ff1bc0991.png)

验证父子容器

![](image/doc_html_fc2ddcd6388b6ef6.png)

验证有没有加dependsOn注解

![](image/doc_html_4d73efff5f603046.png)

进入createBean

![](image/doc_html_4da257d562355342.png)

第五步 通过bd对象获取到class 
----------------------------

得到类的class文件

继续向下

![](image/doc_html_d2e0e280618fef98.png)

![](image/doc_html_559705a1d1f8221.png)

![](image/doc_html_dfafaddf296cc993.png)

![](image/doc_html_a1d120300a126862.png)

第六步 根据class文件推断构造方法—通过构造方法反射实例化对象 
-----------------------------------------------------------

得到所有的构造方法 实例化对象x

![](image/doc_html_e4c11e8c3fe408bd.png)

向下 这个时候对象x已经实例化 注入其中的y还是个null

![](image/doc_html_d20c9af5ece86579.png)

第七步 往一个map中存入一个工厂对象 
----------------------------------

将一个工厂put到singletonFactories中

![](image/doc_html_bbc41852c2dac017.png)

第八步 判断属性是否需要注入 
---------------------------

第九步 完成属性注入，自动装配 
-----------------------------

完成属性注入

debug进入

![](image/doc_html_a53cd3be6ef73bbf.png)

有个if判断 如果不需要属性注入 直接return 返回 有属性注入 向下

![](image/doc_html_22d46c7034800da1.png)

执行完毕 x中已经有y y中有x

![](image/doc_html_98c1500dcea7116.png)

第十步 执行aware 接口 
---------------------

第十一步 执行所有生命周期初始化回调方法—三种形式 
------------------------------------------------

initializeBean（） 方法执行一部分Aware接口

继续向下

![](image/doc_html_c8fca5a14c426288.png)

这里也是一部分执行awaer接口

![](image/doc_html_c6a624d0aa007914.png)

这个也是

![](image/doc_html_3ecccdefae822f90.png)

第十二步 完成aop 
----------------

完成aop

第十三部 put到单例池 
--------------------

put到单例池 可以查看另一个剑指aop

第十四步 销毁 
-------------

问题 
----

### 问题一 为什么在createbean 之前要从单例池中获取一次 

![](image/doc_html_6ac38143a7a50170.png)

两个原因

一是这个dogetBean 并不是只有refresh（） 方法调用

spring 在实例化容器的时候调用一次 在spring容器获取bean的
时候也会调用一次

在这里判断 如果可以获取到 返回获取到的bean

如果获取不到 就会create

![](image/doc_html_6120ab2a48fc3425.png)

二是 循环依赖

当spring容器初始化的时候 第一次进入dogetbean方法
通过getSingleton(beanName)从单例池池中获取对象 获取不到 进入

![](image/doc_html_a77f7e112edf667d.png)

这个重载的getSingleton方法

![](image/doc_html_44295c7852471bbd.png)

向一个set集合中存入x

![](image/doc_html_c19d6584ca76d8ae.png)

![](image/doc_html_e5d1afc61d69b719.png)

继续向下

![](image/doc_html_b9f2c63e6f2e3ba7.png)

在这里实例化x

![](image/doc_html_52b4230f4ceb6ef4.png)

进入

![](image/doc_html_344ac95431b1f0e3.png)

这个方法 向singletonFactories中put一个接口，接口是

![](image/doc_html_f1c11decbe114ca3.png)

![](image/doc_html_ea214a1a883950dc.png)

![](image/doc_html_29de4f8d3eaef35f.png)

这里属性注入 注入y

首先getBean y 获取不到 创建y 创建y是 注入x 这个时候x还没有初始化完毕
还没有put到单例池中 获取不到 又一次创建x 形成死循环 spring是怎么解决的呢

![](image/doc_html_9a1126df614ceb8e.png)

![](image/doc_html_f9793aebca760d68.png)

创建y

![](image/doc_html_16370252e0087fe3.png)

继续向这个set中存入一个y

![](image/doc_html_8c5b4f50b24c87b2.png)

继续创建y

![](image/doc_html_4b3c1e1efb82316b.png)

再次执行这个

![](image/doc_html_8375ca6e7b9eea1f.png)

![](image/doc_html_ea214a1a883950dc.png)

这个时候第二个map中缓存了两个objectFactory对象

![](image/doc_html_90e58d3cee49b911.png)

有执行到这里 注入x 执行getbean

![](image/doc_html_df3c3029a7fe09ae.png)

已经可以获取到了 但是问题是x的初始化还没有走完，还没有put到单例池中
他是怎么初始化完毕的呢 同时 如果拿不到 也就死循环了 所以肯定会拿到的
那么 怎样的机制呢？

进入

这个时候if和刚才就不一样了 就是true

![](image/doc_html_cd2da18d1914f9c9.png)

![](image/doc_html_fddc707abdc7353a.png)

这个set中已经有两个刚才存储的对象了

继续 又多了一个map earlySingletonObjects 着三个map就是spring的三级缓存

![](image/doc_html_e299884962c5169e.png)

![](image/doc_html_d6c199c8eaa5130f.png)

这个singletonFactories中有刚才存入的两个objectFactory工厂对象

将x对象赋值给singletonObject

将二级缓存中的x 存入到 三级缓存中 将二级缓存中的对象删除

这是 就拿到x了 拿到x之后注入给y

y的生命周期继续向下走 y走完后

将y注入x x拿到y之后 x的生命周期同样继续向下走

spring容器初始化完毕 循环依赖解决

### 问题二 为什么在设计二级缓存的时候要弄一个工厂对象 而不是直接一个对象呢 

当加入aop的时候 创建x 需要注入y 创建y 注入x 这个时候 x已经初始化完毕
但是并不是一个aop代理对象 因为spring
生命周期还没有走到aop的那一步，但是实际上这个x已经被代理了

所以 如果这里不是一个工厂对象 而是一个bean的话 它就永远拿不到aop代理对象

![](image/doc_html_384b40c8088449f6.png)

这里 完成了aop的代理

为什么需要一个工厂 可以定制任何想要的类

### 问题三 为什么要三级缓存 二级缓存也可以啊 

多个循环引用的时候 提高效率 当别的bean 也需要循环依赖的时候
可以直接从三级缓存中获取 三级缓存中删除 是为了gc 提高内存
