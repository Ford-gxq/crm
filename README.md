# crm
这是一个基于SpringBoot和mybatis的客户关系管理系统
## 一、项目目录说明

>main.java.com.xxxx.crm
>
>* base (公共类)
>
> >+ annotations （自定义注解）
> >+ aspect（切面类）
> >+ config（配置类）
> >+ controller（controler层）
> >+ enums（枚举类）
> >+ exceptions（全局异常配置）
> >+ generator（代码生成器）
> >+ interceptor（拦截器）
> >+ mapper（对应的mybatis接口文件）
> >+ pojo（实体类）
> >+ query（查询类）
> >+ service（service层）
> >+ task（定时任务，定时将定义流失的用户添加到流失表）
> >+ utils（工具类）
> >+ vo（自定义实体）
> >
> >
>
>* resources(资源文件夹，主要放在mapper.xml文件和前端静态页面、js等资源)
>
> >- mappers文件夹-（主要放置的是mapper.xml文件，定义的SQL语句）
> >
> >- static 文件夹（主要的静态资源css/images/js/jQuery等）
> >- views文件夹（对应的Freemark前端视图页面、application.yml核心的项目配置文件相当于properties文件）
> >- generatorConfig.xml逆向工程代码生成器
>
>* target文件夹(存放的是已经编译好的class文件，此文件夹可删除！)

**声明:**

​    本项目是个人为了学习springboot和Layui、mybatis、Freemark等框架的运用开发的一个实战项目，参考了一些开源项目和学习视频，项目中的一些功能注释都已经写好，只需按照对应的配置文件改为你本地的环境配置即可。

***

## 二、技术栈

**后端技术栈：**

1. springboot
2. 数据库 MySQL 8.0
3. Maven
4. 前端模板 [layui](http://www.layui.com/doc)
5. 持久层 mybatis
6. 模板引擎 [freemark](http://freemarker.foofun.cn/)
7. 生成目录树插件 [Z-tree](http://www.treejs.cn/v3/faq.php#_206)
8. 数据连接池 [Druid](https://github.com/alibaba/druid/)
9. 接口测试工具 [swagger2](https://swagger.io/)
10. 图标展示工具 [echars](https://echarts.apache.org/zh/index.html)
11. 分页工具 PageHelper

***

## 三、工具

1. IDEA
2. Navicat  Premium
3. MySQL 8.018
4. Chrome浏览器
5. jdk11

***

## 四、使用方式

1.新建一个名为 **crm** 的数据库，然后导入本项目中的sql文件
2.IDEA连接mysql数据库

3.idea窗口file-->setting-->找到Build，Execution，Deployment--->Build Tools-->Maven在右边的窗口中设置你自己的maven安装路径和本地仓库

4.在pom.xml中修改成你本地的jdk版本等运行环境

5.点击starter运行编译完成后，启动访问 **http://localhost:8081/crm** , 目前只开发了两个核心模块：营销管理模块和系统设置模块，后期会继续更新...

![登录截图](https://github.com/Ford-gxq/crm/blob/main/screenshot/tu01.jpg)
![角色管理-权限认证](https://github.com/Ford-gxq/crm/blob/main/screenshot/tu04.jpg)
