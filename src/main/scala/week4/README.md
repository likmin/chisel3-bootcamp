## week4

4.1 什么是FIRRTL  
   在执行一个chisel设计时，为了构造生成器的实例，
   它会解析所有的Scala参数，执行周围的Scala代码，详细说明（elaborate）。
    
   Chisel不是直接生成Verilog代码的，而是先生成一个中间表示，称为**FIRRTL**，该中间表示展示的是详细设计的RTL实例。
   它可以被序列化（转化成字符创写入文件），并且序列化的语法是人类可读的。
   
   但是在内部，它并不会表示成一大长串字符串。其数据结构由一种称为抽象语法树（abstract-syntax-tree，AST）的节点树组织构成。
   
   例如：定义一个chisel Module，输入信号要等两个周期才会输出：
   ```scala
    class DelayBy2(width: Int) extends Module {
      val io = IO(new Bundle {
        val in  = Input(UInt(width.W))
        val out = Output(UInt(width.W))
      })
      val r0 = RegNext(io.in)
      val r1 = RegNext(r0)
      io.out := r1
    }
   ```
   接下来，详细说明（elaborate），序列化（serialize）并打印出其生成的FIRRTL代码：
   ```scala
    println(chisel3.Driver.emit(() => new DelayBy2(4)))
   ```
   这里可以在sbt生成
   ```shell script
    test:runMain utils.getVerilog delayby2
   ```
   生成的FIRRTL代码如下；
   ```firrtl
    ;buildInfoPackage: chisel3, version: 3.2.5, scalaVersion: 2.12.10, sbtVersion: 1.3.2
    circuit DelayBy2 : 
      module DelayBy2 : 
        input clock : Clock
        input reset : UInt<1>
        output io : {flip in : UInt<4>, out : UInt<4>}
        
        reg r0 : UInt, clock @[DelayBy2.scala 9:19]
        r0 <= io.in @[DelayBy2.scala 9:19]
        reg r1 : UInt, clock @[DelayBy2.scala 10:19]
        r1 <= r0 @[DelayBy2.scala 10:19]
        io.out <= r1 @[DelayBy2.scala 11:10]
   ```
   可看到，FIRRTL的代码和Chisel很像，所有的生成参数都已被解析。