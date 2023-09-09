# edu-system在线教育平台
## 项目地址

前端项目地址github:( [edu-system-web](https://github.com/seasonl2014/edu-system-web.git))

后台项目地址github:( [edu-system](https://github.com/seasonl2014/edu-system.git))

## 系统功能说明

本系统主要分学员端和后台管理端，其中：

学员端主要功能有：注册和登录，首页信息展示、列表页信息展示、详情页信息展示、学员购买课程、学员购买VIP、会员中心、个人信息修改、我的课程、我的购买记录、我的优惠券、在线播放视频课程等。

后台管理段主要功能有：课程中心、网站设置、财务中心、营销中心，数据中心、个人信息展示和修改、用户管理、角色管理、日志管理等。

## 技术栈

1. web框架：SpringBoot3.X
2. 数据库框架：Sping Data JPA
3. 数据库：MySql8.0
4. 项目构建工具：Maven、vite
5. 前端框架：Vue3.X、element plus、pina、axios、Vue Router
6. 数据图表：ECharts
7. 阿里云视频点播
8. 阿里云OSS对象存储
9. 基于微信开放平台扫码登录
10. 基于微信支付代金券
11. 基于微信公众平台

## 开发环境的安装和配置

由于Spring Boot 3.x版本要求Java 17作为最低语言版本，因此需要安装JDK 17或者以上版本运行。

#### 下载JDK安装包

JDK的安装包可以在Oracle官网免费下载。在下载之前，需要确定所使用电脑的系统信息，这里以Windows系统为例。首先在电脑桌面上用鼠标右键点击“计算机”或“此电脑”，然后点开属性面板，之后可以在“属性”栏中查看“系统属性”。如果是64位操作系统，则需要下载对应的64位JDK安装包；如果是32位操作系统，则需要下载对应的32位JDK安装包。
打开浏览器，在Oracle官网找到对应的JDK下载页面。

>下载地址:https://www.oracle.com/java/technologies/downloads/#java17

![image-20230307200702307](http://picture.xueden.cn/typora/image-20230307200702307.png)

#### 安装JDK

在JDK安装包下载完成后，首先鼠标左键双击“jdk-17_windows-x64_bin.msi”文件进行安装，然后会出现JDK安装界面，如图所示。

![image-20230307201253977](http://picture.xueden.cn/typora/image-20230307201253977.png)



按照JDK安装界面的提示，依次点击“下一步”按钮即可完成安装。
需要注意的是，此步骤中JDK的安装路径，可以选择安装到C盘的默认路径，也可以自行更改安装路径，比如将安装路径修改为E:\anzhuang\jdk17。在安装步骤完成后，可以看到E:\anzhuang\jdk17目录下的文件（如图所示），这就表示JDK安装成功了。

![image-20230307201439266](http://picture.xueden.cn/typora/image-20230307201439266.png)

#### 配置环境变量

在安装成功后，还需要配置Java的环境变量，具体如下所示。首先在电脑桌面上鼠标右键点击“计算机”或者“此电脑”，然后点开属性面板，点击“高级系统设置”，在弹出的“系统属性”面板中点击“高级”选项卡，最后点击“环境变量”按钮。

在环境变量面板中，点击“系统变量”下方的“新建”按钮，在“变量名”输入框中输入“JAVA_HOME”；在“变量值”输入框中输入安装步骤中选择的JDK安装目录，比如“E:\anzhuang\jdk17”，点击“确定”按钮，如图所示。

![image-20211226124603946](http://picture.xueden.cn/typora/image-20211226124603946-1640565816461.png)



编辑Path变量，在变量的末尾添加：

```java
.;%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;
```

具体如图所示。

![image-20211226125308380](http://picture.xueden.cn/typora/image-20211226125308380-1640565816461.png)



增加CLASS_PATH变量，与添加JAVA_HOME变量的过程一样，变量名为“CLASS_PATH”，变量值为“;%JAVA_HOME%\lib;%JAVA_HOME%\lib\tools.jar”。
至此，环境变量设置完成。

#### JDK环境变量验证

在完成环境变量配置后，还需要验证是否配置正确。
打开cmd命令窗口，输入java -version命令。这里演示安装的JDK版本为17.0.2，如果环境变量配置正确，命令窗口会输出正确的JDK版本号：

```java
java version "17.0.2"
```

如果验证结果如图所示，则表示JDK安装成功。如果输入命令后报错，则需要检查在环境变量配置步骤中是否存在路径错误或者拼写错误并进行改正。

![](http://picture.xueden.cn/typora/image-20230307201800124.png)

### Maven的安装和配置

Maven是Apache的一个软件项目管理和构建工具，它可以对Java项目进行构建和依赖管理。本课中所有源码都选择了Maven作为项目依赖管理工具，本节内容将讲解Maven的安装和配置。


#### 下载Maven安装包

打开浏览器，在Apache官网找到Maven下载页面，其下载文件列表如图1-11所示。点击“apache-maven-3.8.4-bin.zip”即可完成下载。

下载地址：https://maven.apache.org/download.cgi

![image-20211226131825731](http://picture.xueden.cn/typora/image-20211226131825731-1640565816464.png)

#### 安装并配置Maven

首先安装Maven并不像安装JDK一样需要执行安装程序，直接将下载的安装包解压到相应的目录下即可。这里笔者解压到E:\anzhuang\apache-maven-3.8.4目录下，如图所示。

![image-20211226132845053](http://picture.xueden.cn/typora/image-20211226132845053-1640565816462.png)

然后需要配置Maven命令的环境变量，步骤与配置JDK环境变量类似。在环境变量面板中，点击“系统变量”下方的“新建”按钮，在“变量名”输入框中输入“MAVEN_HOME”，在“变量值”输入框中输入目录，比如“E:\anzhuang\apache-maven-3.8.4”，点击“确定”按钮，具体如图所示。

![image-20211226133046485](http://picture.xueden.cn/typora/image-20211226133046485-1640565816462.png)



#### Maven环境变量验证

在Maven环境变量配置完成后，同样需要验证是否配置正确。
打开cmd命令窗口，输mvn -v命令。这里安装的Maven版本为3.8.4，安装目录为E:\anzhuang\apache-maven-3.8.4。如果环境变量配置正确，在命令窗口会输出如图所示的验证结果，表示Maven安装成功。

![image-20211226133748667](http://picture.xueden.cn/typora/image-20211226133748667-1640565816462.png)

如果在输入命令后报错，则需要检查在环境变量配置步骤中是否存在路径错误或者拼写错误并进行改正。



#### 配置国内Maven镜像

在完成以上步骤后就可以正常使用Maven工具了。为了获得更好的使用体验，建议国内开发人员修改一下Maven的配置文件。
国内开发人员在使用Maven下载项目的依赖文件时，通常会面临下载速度缓慢的情况，甚至出现“编码5分钟，启动项目半小时”的窘境。

由于每次下载新的依赖文件都需要通过外网访问Maven中央仓库，如果不进行配置的优化处理则会极大地影响开发流程。笔者建议使用国内公司提供的中央仓库镜像，比如阿里云的镜像、华为云的镜像。另外一种做法是自己搭建一个私有的中央仓库，并修改Maven配置文件中的mirror标签来设置镜像仓库。
这里以阿里云镜像仓库为例，介绍如何配置国内Maven镜像加快依赖的访问速度。

进入Maven安装目录E:\anzhuang\apache-maven-3.8.4，在conf文件夹中打开settings.xml配置文件。添加阿里云镜像仓库的链接，修改后的settings.xml配置文件如下：

```xml
<localRepository>E:\anzhuang\apache-maven-3.8.4\repo</localRepository>

<mirror>
     <id>alimaven</id> 
      <name>aliyun maven</name> 
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>    
      <mirrorOf>central</mirrorOf> 
    </mirror>
```

在配置完成后，可以直接访问国内的镜像仓库，从而使Maven下载jar包依赖的速度变得更快，可以节省很多时间。

### 开发工具IDEA的安装和配置

Java开发人员常使用的开发工具包括Eclipse、MyEclipse和IDEA。
关于在Spring Boot项目开发时编辑器的选择，笔者推荐IDEA作为主要的开发工具。IDEA对于开发人员非常友好和方便，本课程关于项目的开发和演示都会选择使用IDEA编辑器。

IDEA全称IntelliJ IDEA，是用Java语言开发的集成环境（也可用于其他语言）。IntelliJ在业界被公认为最好的Java开发工具之一，尤其在智能代码助手、代码自动提示、重构、J2EE支持、JUnit单元测试、CVS版本控制、代码审查、 创新的GUI设计等方面的功能可以说是超常的。



#### 下载IDEA安装包

打开浏览器，进入JetBrains官网。在进入IDEA页面后能够查看其基本信息和特性介绍，如图所示。感兴趣的可以在该页面了解IDEA编辑器更多的信息。

下载地址：https://www.jetbrains.com/idea/

![](http://picture.xueden.cn/typora/image-20230307202244277.png)

点击页面中的“下载”按钮，进入IDEA编辑器的下载页面，如图所示。目前IDEA编辑器的最新版本为2021.3。

![](http://picture.xueden.cn/typora/image-20230307202348566.png)





在IDEA编辑器的下载页面可以看到两种收费模式的版本。
（1）Ultimate为商业版本，需要付费购买使用，功能更加强大，插件也更多，使用起来也会更加顺手，可以免费试用30天。
（2）Community为社区版本，可以免费使用，功能和插件相较于付费版本有一定的减少，不过对于项目开发并没有太大的影响。

大家根据所使用的系统版本下载对应的安装包即可，本书将以Ultimate版本为例进行讲解。



#### 安装IDEA及其功能介绍

在下载完成后，双击下载的安装包程序，按照IDEA安装界面的提示，依次点击“Next”按钮即可完成安装，如图所示。

![image-20211226184509254](http://picture.xueden.cn/typora/image-20211226184509254-1640565816462.png)



首次打开IDEA编辑器可以看到它的欢迎页面，如图所示。

![image-20211226185142225](http://picture.xueden.cn/typora/image-20211226185142225-1640565816462.png)



功能区域有三个按钮，功能分别如下所示。

（1）New Project：创建一个新项目。

（2）Open：打开一个计算机中已有的项目。

（3）Get from VCS：通过在版本控制系统中打开项目获取一个项目，比如通过GitHub、Gitee、GitLab以及自建的版本控制系统。
在创建或者打开一个项目后，则进入IDEA编辑器的主界面。这里以一个基础的Spring Boot项目为例进行介绍。在打开项目后，IDEA编辑器界面如图所示。

![](http://picture.xueden.cn/typora/image-20211226185458780-1640565816462.png)



由上至下，依次为菜单栏区域、代码操作区域、控制台和终端区域。代码操作区域是开发时主要操作的区域，包括项目结构、代码编辑区、Maven工具栏。菜单栏区域主要的作用是放功能配置的按钮和增强功能的按钮。控制台和终端区域主要显示项目信息、程序运行日志、代码的版本提交记录、终端命令行等内容。



#### 配置IDEA的Maven环境

想要之前安装的Maven可以正常在IDEA中使用，则需要进行以下配置。依次点击菜单栏中的按钮“File→Settings→Build,Execution,Deployment→Build Tools→Maven”，在Maven设置面板中配置Maven目录和settings.xml配置文件位置，，如图所示。

![image-20211226190135551](http://picture.xueden.cn/typora/image-20211226190135551-1640565816464.png)



### 开发工具WebStorm的安装和配置

前端开发工具还是比较多的，对于java开发人员来说，WebStrom绝对是不错的选择，因为它跟IDEA是同一家公司出的，熟练使用IDEA的java开发人员，对于使用WebStrom还是比较容易上手的。

 WebStorm 是一个适用于 JavaScript 和相关技术的集成开发环境。类似于其他 JetBrains IDE，它也会使您的开发体验更有趣，自动执行常规工作并帮助您轻松处理复杂任务。 



#### 下载WebStrom安装包

打开浏览器，进入JetBrains官网。在进入WebStrom页面后能够查看其基本信息和特性介绍，如图所示。感兴趣的读者可以在该页面了解WebStrom编辑器更多的信息。

下载地址：https://www.jetbrains.com/zh-cn/webstorm/

![image-20211226191048929](http://picture.xueden.cn/typora/image-20211226191048929-1640565816464.png)



点击页面中的“下载”按钮，就可以直接下载WebStrom编辑器。目前WebStrom编辑器的最新版本为2022.3.2。



#### 安装WebStrom及其功能介绍

在下载完成后，双击下载的安装包程序，按照WebStrom安装界面的提示，依次点击“Next”按钮即可完成安装，如图所示。

![image-20211226192844788](http://picture.xueden.cn/typora/image-20211226192844788-1640565816464.png)



打开WebStrom编辑器可以看到它的欢迎页面，如图所示。

![image-20211226193027197](http://picture.xueden.cn/typora/image-20211226193027197-1640565816464.png)



功能区域有三个按钮，功能分别如下所示。
（1）New Project：创建一个新项目。
（2）Open：打开一个计算机中已有的项目。
（3）Get from VCS：通过在版本控制系统中打开项目获取一个项目，比如通过GitHub、Gitee、GitLab以及自建的版本控制系统。

