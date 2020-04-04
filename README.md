## chisel3-bootcamp

[chisel-bootcamp](https://github.com/freechipsproject/chisel-bootcamp)中的所有实例，可直接在本地运行，无需配置jupyter环境。



### 简介

这里只包含了[chisel-bootcamp](https://github.com/freechipsproject/chisel-bootcamp)的第2-4章，每章都放在一个`package`中，第***i***章放在了`package week`***i*** 中。

### 环境要求
   - 系统要求：Windows, Linux  

   - 软件要求：[sbt](https://www.scala-sbt.org/)和[Scala](https://scala-lang.org) 

   > sbt在国内可能会比较慢，建议切换成阿里源配置如下:  

   1. ~/.sbt中创建`repositories`文件，并添加如下内容
      
         ```shell
         [repositories]
         local
         osc: https://maven.aliyun.com/nexus/content/groups/public/
         typesafe: https://repo.typesafe.com/typesafe/ivy-releases/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly
         sonatype-oss-releases
         maven-central
         sonatype-oss-snapshots
         ```

   2. 添加`-Dsbt.override.build.repos=true`到sbt软件的配置文件中，  
      
      如果是Linux系统，添加到`path_to_installation/sbt/conf/sbtopts`末尾.
      如果是windows，添加到`path_to_installation/sbt/conf/sbtconfig.txt`末尾.



### 快速开始
1. 打开sbt

2. 测试week2包中Sort4
   ```shell
   test:runMain week2.Week2Main sort4
   ```
   最后一行会出现[Success]

3. 生成Sort4的Verilog代码
   ```shell
   test:runMain utils.getVerilog sort4
   ```
   最终会在该项目的根目录中生成三个文件：`Sort4.v, Sort4.fir, Sort4.anno.json`

### 测试项目

1. 格式

   ```shell
   test:runMain [package name].[main name] [testfile name]
   ```

2. 说明

   - package name : 包名称，这里有`week2`, `week3`, `week4`

   - main name : 每个包中都有一个包含main函数的object文件，用于测试根据[testfilen name]测试选择测试文件的

   - testfile name : 测试文件姓名，需要自己到[main name]的文件中指定。

     例如，如果想测试week2中的GradLife，Week2Main中的test添加相应的映射，然后打开sbt，执行

     ```shell
     test:runMain week2.WeekMain gradlife
     ```


### 生成Verilog

   生成Verilog的步骤：

   1. 在utils中getVerilog中添加相应的子项

   2. 打开sbt，生成Verilog

      ```shell
      test:runMain utils.getVerilog 文件名
      ```

   


### 一些笔记
   - Week2
      - Registers和Wires在控制流上非常相似： 
        1.Registers也有last connect 
        最后一个赋值是有效的，但是与wire不同的是，Register的赋值语句是在always模块中的，而wire会转变为Verilog中的组合逻辑
        
        2.可以用when,elsewhen,otherwise有条件的被赋值 
         Register条件赋值会被转化成一堆“与或语句”，而这里会转会为always模块中的if...else语句
