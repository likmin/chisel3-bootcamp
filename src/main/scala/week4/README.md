## week4

### **4.1 什么是FIRRTL**  

- #### 什么是FIRRTL

​		在执行一个chisel设计时，为了构造生成器的实例，它会解析所有的Scala参数，执行周围的Scala代码，详细说明（elaborate）。

​		Chisel不是直接生成Verilog代码的，而是先生成一个中间表示，称为**FIRRTL**，该中间表示展示的是详细设计的RTL实例。它可以被序列化（转化成字符创写入文件），并且序列化的语法是人类可读的。

​		但是在内部，它并不会表示成一大长串字符串。其数据结构由一种称为抽象语法树（abstract-syntax-tree，AST）的节点树组织构成。

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
​		可看到，FIRRTL的代码和Chisel很像，所有的生成参数都已被解析。



- #### 第一个FRRTL抽象语法树

  正如前面提到的，一个FIRRTL representation可以被序列化为一个字符串，但是实际上，它是一种称为**抽象语法树**(AST, abstract syntax tree)的数据结构。这个数据结构是一个节点树，其中一个节点可以包含子节点，另外此数据结构中没有循环。

  ```scala
  val firrtlSerialization = chisel3.Driver.emit(() => new DelayBy2(4))
  val firrtlAST = firrtl.Parser.parse(firrtlSerialization.split("\n").toIterator, Parser.GenInfo("file.fir"))
  
  println(firrtlAST)
  
  Circuit( @[file.fir@2.0],ArrayBuffer(Module( @[file.fir@3.2],DelayBy2,ArrayBuffer(Port( @[file.fir@4.4],clock,Input,ClockType), Port( @[file.fir@5.4],reset,Input,UIntType(IntWidth(1))), Port( @[file.fir@6.4],io,Output,BundleType(ArrayBuffer(Field(in,Flip,UIntType(IntWidth(4))), Field(out,Default,UIntType(IntWidth(4))))))),Block(ArrayBuffer(DefRegister( @[file.fir@8.4],r0,UIntType(UnknownWidth),Reference(clock,UnknownType),UIntLiteral(0,IntWidth(1)),Reference(r0,UIntType(UnknownWidth))), Connect( @[file.fir@9.4],Reference(r0,UnknownType),SubField(Reference(io,UnknownType),in,UnknownType)), DefRegister( @[file.fir@10.4],r1,UIntType(UnknownWidth),Reference(clock,UnknownType),UIntLiteral(0,IntWidth(1)),Reference(r1,UIntType(UnknownWidth))), Connect( @[file.fir@11.4],Reference(r1,UnknownType),Reference(r0,UnknownType)), Connect( @[file.fir@12.4],SubField(Reference(io,UnknownType),out,UnknownType),Reference(r1,UnknownType)))))),DelayBy2)
  
  println(stringifyAST(firrtlAST))
  
  Circuit(
  | @[file.fir@2.0],
  | ArrayBuffer(
  | | Module(
  | | | @[file.fir@3.2],
  | | | DelayBy2,
  | | | ArrayBuffer(
  | | | | Port(
  | | | | | @[file.fir@4.4],
  | | | | | clock,
  | | | | | Input,
  | | | | | ClockType
  | | | | ),
  | | | | Port(
  | | | | | @[file.fir@5.4],
  | | | | | reset,
  | | | | | Input,
  | | | | | UIntType(
  | | | | | | IntWidth(
  | | | | | | | 1
  | | | | | | )
  | | | | | )
  | | | | ),
  | | | | Port(
  | | | | | @[file.fir@6.4],
  | | | | | io,
  | | | | | Output,
  | | | | | BundleType(
  | | | | | | ArrayBuffer(
  | | | | | | | Field(
  | | | | | | | | in,
  | | | | | | | | Flip,
  | | | | | | | | UIntType(
  | | | | | | | | | IntWidth(
  | | | | | | | | | | 4
  | | | | | | | | | )
  | | | | | | | | )
  | | | | | | | ),
  | | | | | | | Field(
  | | | | | | | | out,
  | | | | | | | | Default,
  | | | | | | | | UIntType(
  | | | | | | | | | IntWidth(
  | | | | | | | | | | 4
  | | | | | | | | | )
  | | | | | | | | )
  | | | | | | | )
  | | | | | | )
  | | | | | )
  | | | | )
  | | | ),
  | | | Block(
  | | | | ArrayBuffer(
  | | | | | DefRegister(
  | | | | | | @[file.fir@8.4],
  | | | | | | r0,
  | | | | | | UIntType(
  | | | | | | | UnknownWidth
  | | | | | | ),
  | | | | | | Reference(
  | | | | | | | clock,
  | | | | | | | UnknownType
  | | | | | | ),
  | | | | | | UIntLiteral(
  | | | | | | | 0,
  | | | | | | | IntWidth(
  | | | | | | | | 1
  | | | | | | | )
  | | | | | | ),
  | | | | | | Reference(
  | | | | | | | r0,
  | | | | | | | UIntType(
  | | | | | | | | UnknownWidth
  | | | | | | | )
  | | | | | | )
  | | | | | ),
  | | | | | Connect(
  | | | | | | @[file.fir@9.4],
  | | | | | | Reference(
  | | | | | | | r0,
  | | | | | | | UnknownType
  | | | | | | ),
  | | | | | | SubField(
  | | | | | | | Reference(
  | | | | | | | | io,
  | | | | | | | | UnknownType
  | | | | | | | ),
  | | | | | | | in,
  | | | | | | | UnknownType
  | | | | | | )
  | | | | | ),
  | | | | | DefRegister(
  | | | | | | @[file.fir@10.4],
  | | | | | | r1,
  | | | | | | UIntType(
  | | | | | | | UnknownWidth
  | | | | | | ),
  | | | | | | Reference(
  | | | | | | | clock,
  | | | | | | | UnknownType
  | | | | | | ),
  | | | | | | UIntLiteral(
  | | | | | | | 0,
  | | | | | | | IntWidth(
  | | | | | | | | 1
  | | | | | | | )
  | | | | | | ),
  | | | | | | Reference(
  | | | | | | | r1,
  | | | | | | | UIntType(
  | | | | | | | | UnknownWidth
  | | | | | | | )
  | | | | | | )
  | | | | | ),
  | | | | | Connect(
  | | | | | | @[file.fir@11.4],
  | | | | | | Reference(
  | | | | | | | r1,
  | | | | | | | UnknownType
  | | | | | | ),
  | | | | | | Reference(
  | | | | | | | r0,
  | | | | | | | UnknownType
  | | | | | | )
  | | | | | ),
  | | | | | Connect(
  | | | | | | @[file.fir@12.4],
  | | | | | | SubField(
  | | | | | | | Reference(
  | | | | | | | | io,
  | | | | | | | | UnknownType
  | | | | | | | ),
  | | | | | | | out,
  | | | | | | | UnknownType
  | | | | | | ),
  | | | | | | Reference(
  | | | | | | | r1,
  | | | | | | | UnknownType
  | | | | | | )
  | | | | | )
  | | | | )
  | | | )
  | | )
  | ),
  | DelayBy2
  )
  ```

  这就是内部的数据结构，由FIRRTL AST描述，该树结构的根节点为Circuit。



- #### FIRRTL Node描述

  ​	这一节所描述的共同的FirrtlNodes可在`firrtl/src/main/scala/firrtl/ir/IR.scala`找到，更多这里没有提到的细节可以参考[The FIRRTL  Specification](https://github.com/ucb-bar/firrtl/blob/master/spec/spec.pdf)

  - Circuit：任何Firrtl数据结构的根节点，这里只有一个电路，并且这个电路包含了一系列模块定义以及顶层模块的姓名

    - FirrtlNode 描述

      ```scala
      Circuit(info: Info, modules: Seq[DefModule], main: String )
      ```

    - 具体语法

      ```scala
      circuit Adder:
      	... // List of modules
      ```

    - 在内存中的表现形式

      ```scala
      Circuit(NoInfo, Seq(...), "Adder")
      ```

    

  - Module

    Modules是Firrtl中模块化的组成单元，并且永远不可以直接嵌套，描述一个模块的实例有自己的具体语法和AST 表示。每一个模块含有一个姓名，一系列接口和一个主题包含其实现过程

    - FirrtlNode declaration

      ```scala
      Module(info: Info, name: String, ports: Seq[Port], body: Stmt) extends DefModule
      ```

    - 具体语法

      ```scala
      module Adder:
      	... // list of ports
      	... // statements
      ```

    - 在内存中的表示形式

      ```scala
      Module(NoInfo, "Adder", Seq(...), )
      ```

    

  - Port

    一个port被定义为一个Module io的一部分，它拥有一个姓名，方向（input 或者Output），以及类型

    - FirrtlNode 描述

      ```scala
      class Port(info: Info, name: String, direction: Direction, tpe: Type)
      ```

    - 具体语法

      ```scala
      input x: UInt
      ```

    - 在内存中的表示形式

      ```scala
      Port(NoInfo, "x", INPUT, UIntType(UnknownWidth))
      ```

      

  - Statement

    一个statement用来藐视一个module的组成以及它们之间联系，下面是一些经常用到的statements

    - Block of Statements：一组声明，在一个Module描述中通常用在主体域中

    - Wire Declaration：包含一个姓名和一个类型，它可以同时是一个source(连接*from*)和一个sink(连接*to*)

      - FirrtlNode 描述

        ```scala
        DefWire(info: Info, name: String, tpe: Type)
        ```

      - 具体语法

        ```scala
        wire w: UInt
        ```

      - 在内存中的表示形式

        ```scala
        DefWire(NoInfo, "w", UIntType(UnknownWidth))
        ```

        

    - Register Declaration：包含姓名，类型时钟信号，复位信号以及一个复位时候的值。

      - FirrtlNode描述

        ```scala
        DefRegister(info: Info, name: String, tpe: Type, clock: Expression, reset: Expression, init: Expression)
        ```

        

    - Connection：表示从源到接收器的定向连接，注意它遵从`last-connect-semantics`，即多条对一个端口的赋值只有最有一个是有效的

      - FirrtlNode描述

        ```scala
        Connect(info: Info, loc: Expression, expr: Expression)
        ```

        

    - Other Statements

      其他statements还有`DefMemory`,`DefNode`,`IsInvalid`,`Conditionally`,可以参考 [firrtl/src/main/scala/firrtl/ir/IR.scala](https://github.com/freechipsproject/firrtl/blob/master/src/main/scala/firrtl/ir/IR.scala)

  - Expression：对声明组件或逻辑运算和算术运算的引用

    - Reference：对已声明组件的引用，如wire，register或port。它拥有姓名和类型，需要注意的是其并不包含一个指向声明的指针，而只是将名称作为字符串包含。

      - FirrtlNode描述

        ```scala
        Reference(name: String, tpe: Type)
        ```

    - DoPrim：一个匿名的原语操作，例如`Add`,`Sub`,或`And`,`Or`或子字符串选择器`Bits`,操作类型由`op: PrimOp`字段指示，需要注意的是，所需参数和常量的数量由op决定：

      - FirrtlNode描述

        ```scala
        DoPrim(op: PrimOp, args: Seq[Expression], consts: Seq[BitInt], tpe: Type)
        ```

    - Other Expression

      还有一些其他expression包括：`SubField`,`SubIndex`,`SubAccess`,`Mux`,`ValidIf`等，更多细节可以查看 [firrtl/src/main/scala/firrtl/ir/IR.scala](https://github.com/ucb-bar/firrtl/blob/master/src/main/scala/firrtl/ir/IR.scala) 和 [The FIRRTL Specification](https://github.com/ucb-bar/firrtl/blob/master/spec/spec.pdf).

  - 现在可以回头查看



