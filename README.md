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

      - ChiselTest
        1.ChiselTest为`Decoupled`接口提供了一些有效的测试工具
         - `initSource`和`setSourceClock`可以在测试开始之前正确的初始化`ready`和`valid`字段
         - `enqueueNow`可以向`Decoupled`Input接口中输入一个元素。`DequeueNow`可以从`Decoupled`Output接口中取一个元素
         - `enqueueSeq`可以一次性的将`Seq`中的元素输入到`Decoupled`Input接口中，`expectDequeueNow`将`Decoupled`Output
            接口中的所有元素一次性取出来，然后和`Seq`中的下一个元素比较。  
            
            > 但是如果`Seq`中的元素多于队列的深度，会溢出，可以通过fork和join来解决

         - `fork`启动一个并发代码块，附加的fork在前面的fork的代码块末尾通过`.fork{...}`添加，
            这样`fork {...}`和`.fork{...}`可并发的执行。join: 将多个相关分叉重新组合
            （re-unities）重新组合到调用线程中。

   - week3
      - getOrElse : 对于`Map`或`Option`通常通过`get`方法获得数据——如果有则返回的数据类型为Some()类型，
                    否则会出错返回`None`类型，这时候我们可以通过`getOrElse`方法指定在出错时的默认值
      
      - Match/Case Statements : Scala中提供了多种match模式
         - Value Matching : match的标准取决Value值的大小
         - Multiple Value Matching : 
            ```scala
            def animalType(biggerThanBreadBox: Boolean, meanAsCanBe: Boolean): String = {
               (biggerThanBreadBox, meanAsCanBe) match {
                  case (true, true) => "wolverine"
                  case (true, false) => "elephant"
                  case (false, true) => "shrew"
                  case (false, false) => "puppy"
               }
            }
            ``` 
         - Type Matching : 根据元素的类型决定
            ```scala
            val sequence = Seq("a", 1, 0.0)
            sequence.foreach { x =>
               x match {
                  case s: String => println(s"$x is a String")
                  case s: Int    => println(s"$x is an Int")
                  case s: Double => println(s"$x is a Double")
                  case _ => println(s"$x is an unknown type!")
               }
            }
            ```
         - Multiple Type Matching : 
            ```scala
            val sequence = Seq("a", 1, 0.0)
               sequence.foreach { x =>
               x match {
                  case _: Int | _: Double => println(s"$x is a number!")
                  case _ => println(s"$x is an unknown type!")
               }
            }
            ```
         - Option Matching : 对于Option[T]有两个子类别：Some()和None，例如，
            ```scala
            def show(x: Option[String]) = x match {
               case Some(s) => s
               case None    => "?"
            }
            ```
            `DelayBy1.scala`可以用Option的模式匹配构造：
            ```scala
            class DelayBy1(resetValue: Option[UInt] = None) extends Module {
               val io = IO(new Bundle {
                  val in = Input(UInt(16.W))
                  val out = Output(UInt(16.W))
               })

               val reg = resetValue match {
                  case Some(x) => RegInit(UInt(x))
                  case None    => RegInit(UInt())
               }

               reg := io.in
               io.out := reg
            }
            ```

            > 注意: `Type Matching`也是有局限的，因为Scala在JVM上运行的，JVM在运行时是不保留多态类型，所以在运行时你不可以正确的匹配，例如：
            > ```scala
            > val sequence = Seq(Seq("a"), Seq(1), Seq(0.0))
            > sequence.foreach { x =>
            >  x match {
            >     case s: Seq[String] => println(s"$x is a String")
            >     case s: Seq[Int]    => println(s"$x is an Int")
            >     case s: Seq[Double] => println(s"$x is a Double")
            >  }
            > }
            > ```
            > 运行期间[String],[Int],[Double]都会抹去，只会匹配Seq,所以只会匹配到第一个
         - 在IO中也可以使用Optional Fields   

      - `implicit arguments`
         - 函数的变量名中如果有用`implicit`来定义的变量，那在调用该函数时，该变量可以不指定。
           那该变量的值有谁指定呢？编译器。编译器会找到一个**唯一**的该类型的变量，
           如果有多个满足要求或者根本就没有编译器会报错！
      - `implicit Conversion`
         - 除了`implicit arguments`也可以使用`implicit functions`也就是`implicit conversions`去减少模板的代码。
           具体来说，就是编译器会自动将一个`Scala object`转化为另一个。例如：
           ```scala
            class Animal(val name: String, val species: String)
            class Human(val name: String)
            implicit def human2animal(h: Human): Animal = new Animal(h.name, "Homo sapiens")
            val me = new Human("Adam")
            println(me.species)
           ```
            Human没有`species`数值，但Animal有，我们可以实现一个`implicit conversion`以此给Human添加上species
         > 但是要注意`implicit`不推荐使用，它会使我们的代码扑朔迷离，万不得已不要用，建议使用inheritance，trait，method overloading

      - ***Chisel Standard Library***
         - [Chisel3 Cheatsheet](https://github.com/freechipsproject/chisel-cheatsheet/releases/latest/download/chisel_cheatsheet.pdf)
         - **Decoupled** : 一个标准的 ***Ready-Valid*** 接口  
            Decoupled(io)有三个值：  
               valid : Output(Bool()), 当valid信号有效时表示**发送端**数据准备好传输了  
               ready :  Input(Bool()), 当ready信号有效时表明**接收端**准备好接收数据了  
               bits : io,没有添加ready和valid信号之前的数据

            当ready和valid在一个周期中同时有效时，数据就可以传输了。

            > 注意：  
            >  1.ready和valid不应该是组合耦合（combinationally coupled），否则可能导致无法综合的组合循环（unsynthesizable combinational loops）  
            >  2.valid应该只取决于数据的发送端是否有数据  
            >  3.ready应该只取决于数据的接收端是否有能力接受数据  
            >  4.当传输完毕后，即下一个周期才更新数据

         - **Queues** : `Queue`可以为Decoupled的接口创建一个FIFO队列，允许`backpressure`数据类型和个数都是可配置的。  
            > backpressure:在数据流从上游生产者向下游消费者传输的过程中，上游生产速度大于下游消费速度，导致下游的 **Buffer 溢出**，这种现象就叫做 Backpressure 出现。
            