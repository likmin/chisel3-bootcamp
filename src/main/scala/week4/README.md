## week4

#### **4.1 什么是FIRRTL**  

- ##### 什么是FIRRTL

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



- ##### 第一个FRRTL抽象语法树

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



- ##### FIRRTL Node描述

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

    
  



#### 4.2 Firrtl 抽象语法树的遍历（Firrtl AST Traversal）

- ##### Understanding IR node children

  ​		写一个Firrtl通行证通常要写一个遍历Firrtl数据结构的函数，以收集信息或将IR节点替换为新的IR节点。

  IR数据结构是一个树，树上的每个IR节点都有一些孩子节点，这些孩子节点可以有更多的孩子节点，如果一个IR节点没有孩子则称为叶子。

  ​		不同的IR节点有不同的孩子类型，如下图所示：

  ```scala
  	      Circuit
  	         |
  	       DefModule
  		  /         \
           /           \
          /             \
  	   /                \
  	  /                   \
     Port                Statement
      |				 /      |      \
     /  \             /       |       \
   Type Direction Statement Expression Type
   /  \					   /      \ 
  Type Width				EXpression Type
  ```

  

- ##### The map function

  为了写一个可以遍历`Circuit`函数，我们需要理解一个函数编程概念——map，

  - 理解Seq.map

    Seq中map就是将序列中每个元素按照**指定的函数规则**映射到另一个新的序列，例如：

    ```scala
    val s = Seq("a", "b", "c")
    
    // 方法一：使用匿名函数
    s.map(x => x + x)	// Seq("aa", "bb", "cc")
    
    // 方法二：使用显示函数
    def f(x: String): String = x + x
    s.map(f)		   // Seq("aa", "bb", "cc")
    ```

  - 理解Firrtl中的map

    Firrtl中利用`mapping`的概念，在IR节点上创造我们自己自定义的`map`方法，假设我们有一个`DoPrim`表达式想要表示 1 + 1，这里可以被描述成一个带有`DoPrim`根节点的类似于树的表达式：

    ```scala
    	  DoPrim
         /      \
    UIntValue  UIntValue
    ```

    如果我们有一个函数`f`，这个函数`f`带有一个名为`Expression`参数，且返回一个新的`Expression`，我们将这个函数`f`映射到给定IR节点（就像DoPrim）的所有孩子`Expression`中，这样会返回一个新的`DoPrim`,它的孩子是对变为了`f(Expression)`。

    ```scala
    		DoPrim
    		/    \
    f(UIntValue) f(UIntValue)
    ```

    有时候IR节点的孩子节点有很多类型，例如，`Conditionally`同时拥有`Expression`和`Statement`两个孩子。在这种情况下，`map`只会讲其函数应用到与该函数参数类型匹配的孩子节点上。例如：

    ```scala
    val c = Conditionally(info, e, s1, s2) // e: Expression, s1, s2: Statement, info: FileInfo
    def fExp(e: Expression): Expression = ...
    def fStmt(s: Statement): Statement = ...
    c.map(fExp)  // Conditionally(fExp(e), s1, s2)
    c.map(fStmt) // Conditionally(e, fStmt(s1), fStmt(s2))
    
    // 也可以这样表示
    c map fExp
    c map fStmt
    ```

    

- ##### Pre-order traversal

  ​		为了遍历一颗Firrtl树，我们利用`map`去编写一个可以访问每个孩子的每个节点的递归方程。

  ​		假设我们想要收集设计中声明的每个寄存器的名称，我们必须访问每个`Statement`，然而一些`Statement`节点可以有`子Statement`，因此我们需要编写一个函数，这个函数可以同时检查输入参数是否为一个`DefRegister`，如果不是，该函数会递归的将`f`应用到所有输入参数的所有`Statement `的孩子上。

  ​		下述函数`f`和我们描述的函数非常相似，但是带有两个参数，一个可变的寄存器名称的哈希集，一个`Statement`。利用函数的局部套用（Currying），我们可以只传递第一个参数，这是返回一个新的有指定类型签名的函数（Statement => Statement）.

  ```scala
  def f(regNames: mutable.HashSet[String]())(s: Statement): Statement = s match {
      // 如果是寄存器，将name添加到regNames中
      case r: DefRegister => 
      	regNames += r.name
      	r // 返回未改变的参数，因为DefRegister没有子Statement  
      
      // 如果不是register，将f(regNames)应用到所有的子Statement
      case _ => s map f(regNames) 
  }
  ```

  ​		这种模式在Firrtl中是非常常见的，称为前序遍历，因为递归函数在递归应用其孩子节点前，首先匹配原始IR节点



- ##### Post-order traversal

  ​		上面的案例也可以用`后序遍历`编写：

  ```scala
  def f(regNames: mutbale.HashSet[String]())(s: Statement): Statement = {
      // 不是立刻递归到孩子节点再匹配
      s map f(regName) match {
          case r: DefRegister => 
          	regNames += r.name
          	r
          
          case _ => s
      }
  }
  ```

  

#### 4.3 Firrtl中常用成语（Firrtl Common Idioms）

- ##### 添加状态（Adding Statements）

  假设我们想要写一个拆分嵌套的`DOPrim表达式`的pass，如下：

  ```scala
  circuit Top :
  	module Top :
  	 input x: UInt<3>
  	 input y: UInt<3>
  	 input z: UInt<3>
  	 Output o: UInt<3>
  	 o <= add(x, add(y, z))
  ```

  转化为下面的形式：

  ```scala
  circuit Top :
  	module Top :
  	 input x: UInt<3>
  	 input y: UInt<3>
  	 input z: UInt<3>
  	 Output o: UInt<3>
  	 node GEN_1 = add(y, z)
  	 o <= add(x, GEN_1)
  ```

  我们首先需要遍历抽象语法树中的每一个`Statement`和`Expression`。然后，当我们查到一个`DoPrim`时，我们需要添加新的`DefNode`到`module`的主题中，并且插入对该`DefNode`的引用来代替`DoPrim`，下面的代码实现了这一功能，并且消息令牌，注意`Namespace`是 [Namespace.scala](https://github.com/ucb-bar/firrtl/blob/master/src/main/scala/firrtl/Namespace.scala).中的使用函数

  ```scala
  object Splitter extends Pass {
    def name = "Splitter!"
    /** Run splitM on every module **/
    def run(c: Circuit): Circuit = c.copy(modules = c.modules map(splitM(_)))
  
    /** Run splitS on the body of every module **/
    def splitM(m: DefModule): DefModule = m map splitS(Namespace(m))
  
    /** Run splitE on all children Expressions.
      * If stmts contain extra statements, return a Block containing them and 
      *    the new statement; otherwise, return the new statement. */
    def splitS(namespace: Namespace)(s: Statement): Statement = {
      val block = mutable.ArrayBuffer[Statement]()
      s match {
        case s: HasInfo => 
          val newStmt = s map splitE(block, namespace, s.info)
          block.length match {
            case 0 => newStmt
            case _ => Block(block.toSeq :+ newStmt)
          }
        case s => s map splitS(namespace)
    }
  
    /** Run splitE on all children expressions.
      * If e is a DoPrim, add a new DefNode to block and return reference to
      * the DefNode; otherwise return e.*/
    def splitE(block: mutable.ArrayBuffer[Statement], namespace: Namespace, 
               info: Info)(e: Expression): Expression = e map splitE(block, namespace, info) match {
      case e: DoPrim =>
        val newName = namespace.newTemp
        block += DefNode(info, newName, e)
        Ref(newName, e.tpe)
      case _ => e
    }
  }
  ```

- ##### 删除状态（Deleting statements）

  假设我们想要编写一个内联所有值为文本DefNodes的pass，如下：

  ```scala
  circuit Top:
  	module Top:
  	 input x: UInt<3>
  	 output o: UInt<4>
  	 node y = UInt(1)
  	 o <= add(x, y)
  ```

  转化为：

  ```scala
  circuit Top:
  	module Top:
  	 input x: UInt<3>
  	 output o: UInt<4>
  	 o <= add(x, UInt(1))
  ```

  首先我们需要将抽象语法树转化出每一个状态和表达式，然后，当我们查到一个DefNode指向一个文本是，我们将其存入Hashmap中，然后返回一个`EmptyStmt`(因此删除这个DefNode)。然后，无论什么时候我们查看到一个指向已删除的DefNode的引用，我们必须插入相应的文本。

  ```scala
  object Inliner extends Pass {
    def name = "Inliner!"
    /** Run inlineM on every module **/
    def run(c: Circuit): Circuit = c.copy(modules = c.modules map(inlineM(_)))
  
    /** Run inlineS on the body of every module **/
    def inlineM(m: DefModule): DefModule = m map inlineS(mutable.HashMap[String, Expression]())
  
    /** Run inlineE on all children Expressions, and then run inlineS on children statements.
      * If statement is a DefNode containing a literal, update values and
      *   return EmptyStmt; otherwise return statement. */
    def inlineS(values: mutable.HashMap[String, Expression])(s: Statement): Statement =
      s map inlineE(values) map inlineS(values) match {
        case d: DefNode => d.value match {
          case l: Literal =>
            values(d.name) = l
            EmptyStmt
          case _ => d
        }
        case o => o 
      }
  
    /** If e is a reference whose name is contained in values, 
      *   return values(e.name); otherwise run inlineE on all 
      *   children expressions.*/
    def inlineE(values: mutable.HashMap[String, Expression])(e: Expression): Expression = e match {
      case e: Ref if values.contains(e.name) => values(e.name)
      case _ => e map inlineE(values)
    }
  }
  ```

  



#### 4.4 Firrtl add ops per module

​	这里的AnalyzeCircuit转换会遍历`firrtl.ir.Circuit`，并记录每个模块发现的add ops。

- ##### 统计每个模块中的加法器

  一个Firrtl电路是利用一个树的表达方式表示的：

  - 一个Firrtl的`Circuit`包含一系列的`DefModule`
  - 一个`DefModule`包含一系列的`Port`，可能有一个`Statement`
  - 一个`Statement`可以包含其他`Statement`或`Expression`
  - 一个`Expression`可以包含其他`Expression`

  为了访问到电路中所有的Firrtl IR节点，我们编写了一个可以递归遍历这棵树的函数，要记录统计数据，我们将传递一个`Ledger`类，在遇到`add op`时使用它。

  ```scala
  class Ledger {
    import firrtl.Utils
    private var moduleName: Option[String] = None
    private val modules = mutable.Set[String]()
    private val moduleAddMap = mutable.Map[String, Int]()
    def foundAdd(): Unit = moduleName match {
      case None => sys.error("Module name not defined in Ledger!")
      case Some(name) => moduleAddMap(name) = moduleAddMap.getOrElse(name, 0) + 1
    }
    def getModuleName: String = moduleName match {
      case None => Utils.error("Module name not defined in Ledger!")
      case Some(name) => name
    }
    def setModuleName(myName: String): Unit = {
      modules += myName
      moduleName = Some(myName)
    }
    def serialize: String = {
      modules map { myName =>
        s"$myName => ${moduleAddMap.getOrElse(myName, 0)} add ops!"
      } mkString "\n"
    }
  }
  ```

  现在我们定义一个可以遍历电路并且当遇到一个`adder`(DoPrim with op argument `Add`)时可以更新我们的Ledger的FIRRTL装换器.

  花费一些时间去理解`walkModule`,`walkStatement`,`walkExpression`是怎样遍历**FIRRTLAST**所有的`DeffModule`,`Statement`和`Expression`.

  问题：

  - 为什么`walkModule`不去调用`walkExpression`
  - 为什么`walkExpression`要后序遍历（post-order traversal）
  - 你可以将`walkExpression`修改为前序遍历吗

  ```scala
  class AnalyzeCircuit extends firrtl.Transform {
    import firrtl._
    import firrtl.ir._
    import firrtl.Mappers._
    import firrtl.Parser._
    import firrtl.annotations._
    import firrtl.PrimOps._
      
    // Requires the [[Circuit]] form to be "low"
    def inputForm = LowForm
    // Indicates the output [[Circuit]] form to be "low"
    def outputForm = LowForm
  
    // Called by [[Compiler]] to run your pass. [[CircuitState]] contains
    // the circuit and its form, as well as other related data.
    def execute(state: CircuitState): CircuitState = {
      val ledger = new Ledger()
      val circuit = state.circuit
  
      // Execute the function walkModule(ledger) on every [[DefModule]] in
      // circuit, returning a new [[Circuit]] with new [[Seq]] of [[DefModule]].
      //   - "higher order functions" - using a function as an object
      //   - "function currying" - partial argument notation
      //   - "infix notation" - fancy function calling syntax
      //   - "map" - classic functional programming concept
      //   - discard the returned new [[Circuit]] because circuit is unmodified
      circuit map walkModule(ledger)
  
      // Print our ledger
      println(ledger.serialize)
  
      // Return an unchanged [[CircuitState]]
      state
    }
  
    // Deeply visits every [[Statement]] in m.
    def walkModule(ledger: Ledger)(m: DefModule): DefModule = {
      // Set ledger to current module name
      ledger.setModuleName(m.name)
  
      // Execute the function walkStatement(ledger) on every [[Statement]] in m.
      //   - return the new [[DefModule]] (in this case, its identical to m)
      //   - if m does not contain [[Statement]], map returns m.
      m map walkStatement(ledger)
    }
  
    // Deeply visits every [[Statement]] and [[Expression]] in s.
    def walkStatement(ledger: Ledger)(s: Statement): Statement = {
  
      // Execute the function walkExpression(ledger) on every [[Expression]] in s.
      //   - discard the new [[Statement]] (in this case, its identical to s)
      //   - if s does not contain [[Expression]], map returns s.
      s map walkExpression(ledger)
  
      // Execute the function walkStatement(ledger) on every [[Statement]] in s.
      //   - return the new [[Statement]] (in this case, its identical to s)
      //   - if s does not contain [[Statement]], map returns s.
      s map walkStatement(ledger)
    }
  
    // Deeply visits every [[Expression]] in e.
    //   - "post-order traversal" - handle e's children [[Expression]] before e
    def walkExpression(ledger: Ledger)(e: Expression): Expression = {
  
      // Execute the function walkExpression(ledger) on every [[Expression]] in e.
      //   - return the new [[Expression]] (in this case, its identical to e)
      //   - if s does not contain [[Expression]], map returns e.
      val visited = e map walkExpression(ledger)
  
      visited match {
        // If e is an adder, increment our ledger and return e.
        case DoPrim(Add, _, _, _) =>
          ledger.foundAdd
          e
        // If e is not an adder, return e.
        case notadd => notadd
      }
    }
  }
  ```

  

- ##### Running our Transform

  现在我们已经定义好了，让我们在其上面运行一个Chisel设计，先定一个Chisel模块

  ```scala
  // Chisel stuff
  import chisel3._
  import chisel3.util._
  
  class AddMe(nInputs: Int, width: Int) extends Module {
    val io = IO(new Bundle {
      val in  = Input(Vec(nInputs, UInt(width.W)))
      val out = Output(UInt(width.W))
    })
    io.out := io.in.reduce(_ +& _)
  }
  ```

  接下来，我们详细说明进入FIRRTLAST语法

  ```scala
  val firrtlSerialization = chisel3.Driver.emit(() => new AddMe(8, 4))
  ```

  最后，让我们将FIRRTL编译成Verilog代码，但将我们自定义的转换器添加到编译过程。注意，它将会打印出其发现的`add ops`

  ```scala
  val verilog = compileFIRRTL(firrtlSerialization, new firrtl.VerilogCompiler(), Seq(new AnalyzeCircuit()))
  ```

  这里的`compileFIRRTL`函数是这里定义的，在未来的章节中，我们会描述插入自定义转换器的处理过程。

