前言

本文档描述了整个工程 的使用说明，由两部份组成.
第一部份描述开发中的各项规范.
第二部份描述本工程的使用说明.
使用前请仔细阅读.

====================================================第一部份、描述开发中的各项规范==============================================================
1、注释规范
	代码注释使用统一模板文件，模板文件在工程根目录下CodeTemplates.xml
	请按以下方式将代码模板文件导入eclipse
	设置注释模板的入口： Window->Preference->Java->Code Style->Code Template
	请自行修改该文件中的作者等信息
2、编码规范
	2.1、写干净整洁的代码，请尊重空间，去除多余空行，空行可以用来隔开相对独立的代码块,有利于阅读和理解,但是不要使用超过一行的空行.
    2.2、去除没有用到的类引用，eclipse里面Ctrl+Shilft+O.
    2.3、记得格式化代码，eclipse里面Ctrl+Shilft+F.
	2.4、不要吝惜废弃的老代码,大量运用注释来保留, eclipse里面Ctrl+D,随着代码的演变暂用非常大量的空间,如果那段代码非常精妙,舍不得删,那么请把它移到您的私人代码库.
    2.5、请不要写冗余无用的代码,if(a==true)之类的代码块完全不应该出现.
	2.6、请不要在两个地方出现完全相同的代码，请想办法重用.
	2.7、命名类,方法,变量慎用简写,除非大家都公认.
	2.8、把所有的类变量放到最前面,如果比较多请按用途分组排列,不要把变量散落在大江南北.
	2.9、拆分大的类,大的方法,如果您的类有一万行,如果您的方法有一千行，建议方法的代码行数不要超过50行.
3、命名规范
	第一原则：见名知意,类名最好用名字,方法名用动词,名称与内容统一,避免歧义.
	第二原则：层次清晰,方法、属性归于类,类归于包,划分有依据,归类有道理.
	第三原则：粒度明确,多态、继承、封装的使用要因时而变,设计模式的使用也要因需而选,代码结构不可没有设计规划也不可过度设计,要做到取舍得当.
	总       结：看看名称是否一眼就能看出类的涵义、方法的作用,自问一下这个方法应该属于这个类吗,这个类是否应该属于这个包吗,思考这块是否应该使用某种设计模式,权衡该模式的优
	缺点及可能给项目带来的影响.

	包的命名
		Java包的名字都是由小写单词组成。一般采用公司域名的反写+工程名+包实体意义名称。
		命名示例：com.suneee.projectname.dao
	接口命名
	         接口命名以I开头，实现类则以impl结尾.
	         命名示例:接口：IUserService ,实现类： UserServiceImpl
	类的命名
		类的命名以“见名知意”的原则进行。推荐类的名字由大写字母开头，一个单词中的其他字母均为小写。如果类名称由多个单词组成，则建议将每个单词的首字母均用大写，例如，UserService,
		如果类名称中包含单词缩写，则建议将这个词的每个字母均用大写，如：XMLManager。由于类是设计用来代表对象的，所以建议在命名类时应尽量选择名词。
		特殊情况实在不好用英文进行表达的可以使用拼音。
		命名示例：UserService
	方法的命名
		方法的名字的第一个单词应以小写字母开头，后面的单词首字母要大写，建议在方法命名时尽量选择动词。
		命名示例：addUser();
	常量命名
		常量的名字应该都使用大写字母，并且指出该常量完整含义。如果一个常量名称由多个单词组成，则建议用下划线分割这些单词。
		命名示例：MAX_VALUE
	参数的命名
		参数的命名规范和方法的命名规范相同，而且为了避免阅读程序时造成迷惑，请在尽量保证在参数名称为一个单词的情况下，参数的命名尽可能明确。
		命名示例：name
		
		
====================================================第二部份、描述工程的各项使用详情==============================================================
1、基础平台环境
项目基于maven项目管理工具,Maven开发项目已经成为主流,Maven能很好的对项目的层次及依赖关系进行管理。方便的解决大型项目中复杂的依赖关系.
项目编码:统一使用UTF-8
项目IDE:eclipse or myclipse
项目运行环境：JDK1.8 + tomcat 8
项目框架：Spring-mvc-3.1.4 + spring-3.1.4 + mybatis-3.2.7 
缓存使用： jedis-2.1.0
数据库：mysql or oracle
开发人员将项目工程从svn下载下来后将工程名修改成具体 的名称 并修改pom.xml文件
将项目工程目录下的db.sql 测试的数据导入mysql数据库即可运行demo

2、基础平台特点------------------------------------------------------------------------------------
2.1、提供 Spring免配置，只使用注解即可，方便分工开发
2.2、提供Spring mvc 
2.3、数据库连接池的实现
2.4、提供基于注解方式的事务处理
2.5、提供MyBatis Generator 自动生成model, mapper文件
2.6、集成可选redis缓存
2.7、提供通用分页支持
2.8、提供基于logback日志记录功能
2.9、提供junit的测试支持
2.10、提供国际化支持
2.11、提供统一的出错处理机制
2.12、提供baseControl基类，方便后续开发
2.13、提供JSR 303 前端数据校验支持
2.14、提供restful架构支持

3、基础平台开发规则和详细说明---------------------------------------------------------------------------
3.1、 基于此平台的DAO组件、 业务组件、Controller组件、其他组件都以注解方式实现 Spring容器的托管和依赖注入
            详见案例（参阅Spring注解）
3.2、基于spring mvc的前端controller,开发中统一将前端请求的动作控制层实现编写到com.suneee.*.web.controller包下
          并且要求全部继承com.suneee.base.web.controller包中的BaseController基类. 并且实现基类中的getPrefix和
    getCommService 方法    
          控制层java类及具体要调用的方法 全部使用@RequestMapping 注解来处理页面请求，并且要求按照restful架构风格.
    jsp页面跳转全部使用父类方法
          例如： localhost:8080/project/user/list  将跳转到WEB-INF/pages/users/list.jsp页面
         localhost:8080/project/user/view  将跳转到WEB-INF/pages/users/view.jsp页面
    BaseController基类己经封装了常用的删除改查通知方法
          例如： localhost:8080/project/user/del/1 将调用基类中使用@RequestMapping(value="/del/{id}")注解的方法
         localhost:8080/project/user/edit/1 将调用基类中使用@RequestMapping(value="/edit/{id}")注解的方法
                       以上将删除和编辑id为1的对象,具体实现详见demo

3.3、数据库连接池配置,基于第三方组件c3p0实现
	开发环境中默认连接数为5，生产环境根据需要调节applicationContext.xml中参数
	
3.3、事务处理
          事务处理使用spring注解方式，请在com.suneee.*.service.* 包下的具体实现类中需要事务的方法上加上@Transactional
          例如：     
    @Transactional
	public void add(User obj)  throws Exception 
	注意异常要往外抛，事务才会回滚
	
3.4、mybatis mapper文件生成与使用
    1、安装mybatis generator 插件根据工程目录下的generatorConfig.xml配置好数据连接，要生成的表及生成的model及mapper.xml
           的文件位置
    2、如果不安装插件也可以使用maven 命令的方式生成,pom.xml中己经引入了org.mybatis.generator插件，将工程目录下的
    generatorConfig.xml,copy到src下配置好相关项，使用maven命令 mybatis-generator:generate 即可生成model及mapper.xml
    
3.5、redis 缓存使用
	支持可选的redis缓存，如果需要使用可将com.suneee.base.cache.remote.RedisClient 依赖注入到使用的类中即可
	
3.6、分页使用
         开发平台提供com.suneee.base.util.Pager 件，统一封装分页参数和当前页的数据，其对外可使用的属性有：
    pager.pageNo：当前页
    pager.totalPages：共多少页
    pager.pageScale:页面大小（一般不用）
         前面页面分页使用参见demo
          
3.7、logback日志使用
	导入相关包
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	类中	private static Logger log = LoggerFactory.getLogger(UserController.class);  
	关键步骤完成后有必要的话记录INFO级别的日志，在异常处理中记录ERROR级别的日志.
	
3.8、junit的测试
          单元测试代码写在src/test/java包下
          例如：src/test/java/com/
          
3.9、前端 java bean数据校验
          支持前端传输数据到后台时自动对JavaBean进行字段值校验，校验方式只要给JavaBean加上@Valid注解即可
          例如：public JsonResult save(@Valid T item,BindingResult result,Model model)
          其中的item 对象对应的get方法上加上 @NotNull(message = "{user.id.notNull}") 注解   
          
3.10 restful架构使用
          例如：localhost:8080/project/user/view  将调用后台配置的相应查询user方法 
         localhost:8080/project/user/save  将调用后台配置的相应保存user方法 
	详细使用参见demo
	 
3.11 国际化使用
	通用国际key value放到com/suneee/base/resource/base的资源文件中
	非共用的 放到com/suneee/项目名/resource/文件名
         页面中可使用spring 标签展示
         
4、基础平台公共 API及扩展------------------------------------------------------------------------------
	com.suneee.base 提供公共API，  基础平台包请勿随意修改.

	 注意：根据开发分工的子模块不同以上包进而被分为以下结构
	com.suneee.系统名.dao为例
	com.suneee.系统名.dao.模块A  
	com.suneee.系统名.dao.模块B
 
	页面组织结构：
	动态资源：
	web根中为非鉴权页
	WEB-INF/page/public/ 存放公共页面 
	WEB-INF/page/子模块名/ 中为功能页,子系统开发
	       
	 静态资源：
	css/ 存放样式表文件   
	img/ 图片资源
	js/ javascript文件 
	others/ 其它资源
	 子系统定制需要 进一步建立子文件夹
       
5、其它声明-----------------------------------------------------------------------------------------
如有任何问题及更好的建议可以反馈forint.lee@qq.com
