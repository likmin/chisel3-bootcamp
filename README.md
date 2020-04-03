## chisel3-bootcamp

[chisel-bootcamp](https://github.com/freechipsproject/chisel-bootcamp)中的所有实例，可直接在本地运行，无需配置jupyter环境。



### 简介

这里只包含了[chisel-bootcamp](https://github.com/freechipsproject/chisel-bootcamp)的第2-4章，每章都放在一个`package`中，第***i***章放在了`package week`***i*** 中。


### 快速开始


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

     ```shell
     sbt:chisel3-bootcamp> test:runMain week2.Week2Main gradlife
     [warn] Multiple main classes detected.  Run 'show discoveredMainClasses' to see the list
     [warn] Multiple main classes detected.  Run 'show discoveredMainClasses' to see the list
     [info] running week2.Week2Main gradlife
     Starting tutorial gradlife
     [info] [0.001] Elaborating design...
     [info] [0.817] Done elaborating.
     Total FIRRTL Compile Time: 391.0 ms
     file loaded in 0.064239 seconds, 25 symbols, 19 statements
     [info] [0.001] SEED 1585547359762
     test GradLife Success: 32 tests passed in 5 cycles in 0.015611 seconds 320.28 Hz
     [info] [0.009] RAN 0 CYCLES PASSED
     Tutorials passing: 1
     [success] Total time: 3 s, completed 2020-3-30 13:49:21
     ```

### 生成Verilog
   
   - 生成LastConnect的Verilog代码

      ```shell
      test:runMain utils.getVerilog lastconnect
      ```
   
   

### 一些笔记
   - Week2
      - Registers和Wires在控制流上非常相似：  
        1.Registers也有last connect  
        最后一个赋值是有效的，但是与wire不同的是，Register的赋值语句是在always模块中的，而wire会转变为Verilog中的组合逻辑
        
        2.可以用when,elsewhen,otherwise有条件的被赋值  
         Register条件赋值会被转化成一堆“与或语句”，而这里会转会为always模块中的if...else语句
