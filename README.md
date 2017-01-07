# <div align = middle><font size=7>ATM机设计文档</font></div>
<div align = middle><font size=5>71115115 王子卓</font></div>

***

# <div align = middle>效果</div>
![](https://raw.githubusercontent.com/ZizhuoWang/ImageBed/master/JavaATM/ATM1.png)
![](https://raw.githubusercontent.com/ZizhuoWang/ImageBed/master/JavaATM/ATM2.png)

&emsp;&emsp;服务器的守护进程启动之后可以显示在线人数和各个用户的账户总金额。
&emsp;&emsp;ATM机界面在没有登录的时候不可以点击存取款、查询等按钮，金额、编号文本框不可编辑。登陆之后，注册和登录按钮不可点击，用户名、密码文本框不可编辑。

***
# <div align = middle>测试方法</div>
&emsp;&emsp;修改Server.java中getConnection函数中的dbURL，将&user=root&password=`******`修改成您的MySQL用户名和密码。
***
# <div align = middle>程序结构</div>

![](https://raw.githubusercontent.com/ZizhuoWang/ImageBed/master/JavaATM/MYATM.png)


&emsp;&emsp;这不是一个标准的UML类图。
&emsp;&emsp;活期账户、半年期、一年期、五年期存款和贷款都实现了同一个Balance接口，Balance接口继承了Runnable类。活期账户、半年期、一年期、五年期存款和贷款都聚合到User类中。客户端Client和服务器Server都有User类的成员对象。
&emsp;&emsp;客户端Client和服务器守护进程ServerDaemon连接，由ServerDaemon动态创建Server线程。
&emsp;&emsp;每一笔存款自身都是线程，服务器打开之后，线程自动运行，实现加息。利息每季度结算，每季度利息是年利率开四次方。
&emsp;&emsp;客户端和服务器之间信息传递依靠对象输入输出流，信息载体是Command类对象和User类对象。
&emsp;&emsp;数据库的操作由Server类完成。
&emsp;&emsp;大部分对象都实现了可序列化Serializable接口，以便存入数据库。
&emsp;&emsp;采用了UTF-8编解码，所以**支持中文**。
***
# <div align = middle>数据库结构</div>

1. 数据库名：atm
2. 数据表名：user
3. 数据表字段：
    - name：varchar(200),非空，不可重复
    - object：longblob，非空
4. 字段作用：name负责存储用户的用户名。object负责存储用户类的对象，该对象中保存有包括用户密码在内的各种用户信息。
5. 优点：在一定程度上减少数据库的读取、写入次数。用户的信息并不以明文方式呈现，在一定程度上有加密的作用。

***

# <div align = middle>程序使用流程</div>
![](https://raw.githubusercontent.com/ZizhuoWang/ImageBed/master/JavaATM/ATM.png)