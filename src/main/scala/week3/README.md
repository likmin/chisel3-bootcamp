## week3

3.1 getOrElse : 对于`Map`或`Option`通常通过`get`方法获得数据——如果有则返回的数据类型为Some()类型，
                    否则会出错返回`None`类型，这时候我们可以通过`getOrElse`方法指定在出错时的默认值
3.2 Match/Case Statements : Scala中提供了多种match模式
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

3.3 implicit
   - `implicit arguments`:
           函数的变量名中如果有用`implicit`来定义的变量，那在调用该函数时，该变量可以不指定。
           那该变量的值有谁指定呢？编译器。编译器会找到一个**唯一**的该类型的变量，
           如果有多个满足要求或者根本就没有编译器会报错！

   - `implicit Conversion`:
          除了`implicit arguments`也可以使用`implicit functions`也就是`implicit conversions`去减少模板的代码。
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

3.4 ***Chisel Standard Library***

   - [Chisel3 Cheatsheet](https://github.com/freechipsproject/chisel-cheatsheet/releases/latest/download/chisel_cheatsheet.pdf)
     
   - **Decoupled** : 一个标准的 ***Ready-Valid*** 接口  
      Decoupled(io)有三个值：  
      valid : Output(Bool()), 当valid信号有效时表示**发送端**数据准备好传输了  
      ready :  Input(Bool()), 当ready信号有效时表明**接收端**准备好接收数据了  
      bits : io,没有添加ready和valid信号之前的数据

      当ready和valid在一个周期中同时有效时，数据就可以传输了。

      >  注意：  
      1.ready和valid不应该是组合耦合（combinationally coupled），否则可能导致无法综合的组合循环（unsynthesizable combinational loops）  
      2.valid应该只取决于数据的发送端是否有数据  
      3.ready应该只取决于数据的接收端是否有能力接受数据  
      4.当传输完毕后，即下一个周期才更新数据

   - **Queues** : `Queue`可以为Decoupled的接口创建一个FIFO队列，允许`backpressure`数据类型和个数都是可配置的。  
   > 1.backpressure:在数据流从上游生产者向下游消费者传输的过程中，上游生产速度大于下游消费速度，导致下游的 **Buffer 溢出**，这种现象就叫做 Backpressure 出现。  
     2.允许**Buffer溢出**，溢出的元素就不管了。详情查看`src/test/week3/QueueTester.scala`

   - **Arbiter** : 在给定的优先级(prioritization)下，仲裁器将 ***n*** 个`DecoupleIO源`路由(routes)到一个`DecoupledIO接收器`，Chisel中有两种类型
        - Arbiter: 系数越低，优先级越高。(prioritizes lower-index produces)
        - RRArbiter: 按照循环次序运行。(runs in round-robin order)
        
   - **Bitwise Utilities(按位实用工具)**
        - PopCount: 返回UInt中1的个数，测试文件`src/test/week3/PopCountTester`
        - Reverse：按位反转，测试文件`src/test/week3/ReverseTester`
      
   - **OneHot encoding utilities** : **OneHot**是整数的编码，其中每个值只有一条wire，而恰好一根导线很高。这可以非常高效的创建一些功能，例如多路复用器。
     
     > 但是如果多个线保持高电平状态，那行为可能不确定  
     
   - **UIntToOH** : UInt to OneHot, 例如4.U 转化为OneHot就是'10000'.
   - **OHToUInt** : OneHot to UInt, OneHot中高电平的最高位，但高电平必须是唯一的，如果有多个高电平的话，会直接输出的的是最高位值。  
            测试文件`src/test/week3/OneHotTester`
   
   - **Muxes** : Chisel3中提供了4种Mux
       - **Mux** : `Mux(cond, con, alt)`表示`if(cond) con else alt`
       - **MuxCase** : `MuxCase(default, mapping)`,mapping类型为Seq[(Bool, T)],MuxCase返回mapping中第一个元素为true的相关联的值，如果第一个元素都为false，返回默认值default
            例如：
            ```scala
               MuxCase(0.U,Array((false.B, 1.U), (true.B,2.U),(false.B,3.U))) // 返回值为3.U
            ```
       - **MuxLookup** : 创建n个Mux的级联以搜索key值,其中MuxLookup的apply函数：
            ```scala
            def apply[S <: UInt, T <: Data] (key: S, default: T, mapping: Seq[(S, T)]): T = {...} 
            ```
       - **Mux1H** : 返回选择信号为true的相关联的值，如果有多个同时为high，输出则不确定。
       - **PriorityMux** :从低到高，输出第一个选择信号为true的相关联的值。如果没有选择信号为true，则返回最后一个。
         
       - **Counter** : 计数器，见`src/test/week3/CounterTester`
       

3.5 higher-order functions  
    **higher-order functions**就是可以把**functions**当做参数的函数，例如`map`、`reduce`他们的参数为函数。Scala中函数的表达形式:   `(参数列表) => 函数体`，例如，
         

   ```scala
   (a, b) => a + b // 传递参数a,b。 返回参数a + b的值
   a => println(a) // 打印参数a
     
   List(1, 2, 3, 4).foreach{ a => println(a) } //打印List中的所有元素
   List(1, 2, 3, 4).map(a => a * a)            // List中所有元素都平方，得到新的List，返回值为List(1, 4, 9, 16),
                                               // 注意这是一个新的List
   List(1, 2, 3, 4).map(_ => _ * _)            // 同上一条效果相同，下划线类似于填空，在这里指的是List中任意元素都要做这样的操作
   ```

   Scala中很多 ***higher-order functions*** 函数，具体如下：
         `Map`、
         `zipWithIndex`
         `Reduce`
         `Fold`
         `zip`、`unzip`...(还有很多)，具体功能如下：

   - **Map**  
     List[A].map的类型签名如下：
     ```scala
     map[A](f: (A) => B): List[B]
     ```
     一个List[A]调用该函数后，根据传入的函数 ***f*** 映射成一个新的List[B].
     
     ```scala
      List(1, 2, 3, 4).map(x => x + 1) // List(2, 3, 4, 5)
      List(1, 2, 3, 4).map(_ + 1)      // 与上一条等同
      List(1, 2, 3, 4).map(_.toString + "a") // List[String] = List(1a, 2a, 3a, 4a)
      
      List((1, 5), (2, 6), (3, 7), (4, 8)).map { case (x, y) => x*y } // List(5, 12, 21, 32)
      
      val myList = List("a", "b", "c", "d")
      myList(_) // 这是一个Lambda函数：Int => String 
      
      (0 to 4) // 0,1,2,3,4
      (0 until 4) // 0,1,2,3
      (0 to 4 by 2) // 0,2,4
      (0 until 4 by 2) // 0,2
      /**
        * 这些都是range类型的，可以用来生成索引(indices),但是不能大于List的数量
        */
      
      (0 until 4).map{myList(_)} // Vector(a, b, c, d)
      (0 until 4 by 2).map{myList(_)} // Vector(a, c)
      (0 to 4 by 2).map{myList(_)} // Error:java.lang.IndexOutOfBoundsException: 4
     ```
     
   - **zipWithIndex**  
     List.zipWithIndex的类型签名如下:
     
      ```scala
      zipWithIndex: List[(A, Int)]
      ```
      没有参数，返回值为tuple类型，tuple中第一个元素为原来的List中的元素，第二个为系数，从0开始的。
     
      ```scala
      List(1, 2, 3, 4).zipWithIndex // List((1,0), (2,1), (3,2), (4,3))
      List("a", "b", "c", "d").zipWithIndex // List((a,0), (b,1), (c,2), (d,3))
      List(("a", "b"), ("c", "d"), ("e", "f"), ("g", "h")).zipWithIndex // List(((a,b),0), ((c,d),1), ((e,f),2), ((g,h),3))
      ```
     
   - **reduce**  
     List[A].reduce的类型签名如下：
            
     ```scala
     reduce(op: (A, A) ⇒ A): A
     ```
     reduce的作用是在List[A]的所有元素中添加一个二元运算，根据运算的顺序，Scala中`reduce`有`reduceLeft`和`reduceRight`,
            如果List开头开始运算，则用`reduceLeft`，反之则使用`reduceRight`.如果直接调用reduce，默认使用`reduceLeft`.例如：
   
     ```scala
     List(1, 2, 3, 4).reduceLeft((a, b) => a * b)
          
              *
             / \
            *   4
           / \
          *   3
         / \
        1   2
          
     计算顺序(1 * 2) -> (2 * 3) -> ((6 * 4) -> 24
          
     可以通过添加println函数体现
     List(1, 2, 3, 4).reduceLeft((a, b) => {println(a + " * " + b + " = " + a * b);a * b})
     结果为：
     1 * 2 = 2
     2 * 3 = 6
     6 * 4 = 24
     res15: Int = 24
             
     reduceRight
       List(1, 2, 3, 4).reduceRight((a, b) => {println(a + " * " + b + " = " + a * b);a * b})
       3 * 4 = 12
       2 * 12 = 24
       1 * 24 = 24
       res16: Int = 24
     ```
     `reduce`的两种表达形式
     
     ```scala
     println(List(1, 2, 3, 4).reduce((a, b) => a + b))  // returns the sum of all the elements
     println(List(1, 2, 3, 4).reduce(_ * _))  // returns the product of all the elements
     ```

   - **Fold**  
      `fold`和`reduce`很像，但是`fold`需要给定一个初始化一个累记值。`fold`的类型签名：
   
        ```scala
        fold(z: A)(op: (A, A) => A): A 
        ```
    
        其中z就是累计值的初始值。和`reduce`一样，`fold`同样也有`foldLeft` 和 `foldRight`.
      
        ```scala
        List(1, 2, 3, 4).fold(0)(_ + _)
                
          foldLeft:
                        +
                       / \
                      +   4
                     / \
                    +   3
                   / \
                  +   2
                 / \
                0   1
          
      计算顺序(0 + 1) -> ((0 + 1) + 2) -> (((0 + 1) + 2) + 3) -> ((((0 + 1) + 2) + 3) + 4) -> 10
          
          foldRight:
                  +
                 / \
                1   +
                   / \
                  2   +
                     / \
                    3   +
                       / \
                      4   0  
        ```
     
        通过添加println测试  
          
        ```scala
        List(1, 2, 3, 4).foldLeft(0)((a, b) => {println(a + " + " + b + " = " + (a + b));a + b})
        0 + 1 = 1
        1 + 2 = 3
        3 + 3 = 6
        6 + 4 = 10
        res21: Int = 10

        List(1, 2, 3, 4).foldRight(0)((a, b) => {println(a + " + " + b + " = " + (a + b));a + b})
        4 + 0 = 4
        3 + 4 = 7
        2 + 7 = 9
        1 + 9 = 10
        res22: Int = 10
        ```
    
        > `reduce`和`fold`还有一点不同就是，List为空时，`reduce`不可以操作，但`fold`可以



3.6 Functional Programming in Scala  
Scala中函数是第一类对象(first-class objects),所以我们可以将一个函数指定为`val`，也可以作为一个参数传递给类(classes)，对象(objects)以及其他函数(functions)。  

   - 定义一个函数有两种方法：`def`和`val`。二者在定义和调用上各有不同：
       ```scala
       // These are normal functions.
       def plus1funct(x: Int): Int = x + 1
       def times2funct(x: Int): Int = x * 2
       
       // These are functions as vals.
       // The first one explicitly specifies the return type.
       val plus1val: Int => Int = x => x + 1
       val times2val = (x: Int) => x * 2
       
       // Calling both looks the same.
       plus1funct(4)
       plus1val(4)
       plus1funct(x=4)
       //plus1val(x=4) // this doesn't work,Error: not found: value x
       ```
    
   - 在 ***higher-order functions***中，函数是可以被当做参数传递的.
     
     > 实验Scala2.12，不论是`val`还是`def`都可以被传递的，但并没有出现只有`val`才可以被传递的情况啊  
    
     ```scala
      def plus1funct(x: Int): Int = x + 1
      val plus1val: Int => Int = x => x + 1
      def op(x: Int, f: Int => Int) =f(x)
        
      // 这两种情况都是可以的
      op(3,plus1funct)
      op(3,plus1val)
     ```
     
   - `val`和`def`的区别在定义一个无参数的函数时将非常明显，如下：
   
       ```scala
       import scala.util.Random
        
       // both x and y call the nextInt function, but x is evaluated immediately and y is a function
       val x = Random.nextInt // 定义时就已经计算好了值
       def y = Random.nextInt // 只是一个函数，调用时才计算
        
       // x was previously evaluated, so it is a constant
       println(s"x = $x")
       println(s"x = $x")
        
       // y is a function and gets reevaluated at each call, thus these produce different results
       println(s"y = $y")
       println(s"y = $y")
       ```
       - **Anonymous Functions**
       - **Functional Programming in Chisel (Chisel 函数式编程)**



3.7 Object Oriented Programming (面向对象编程)   

   - Abstract Class (抽象类): 定义一些没有具体实现的函数或值，这些函数或者值在子类中必须具体实现。任何对象只能单继承

   - Traits(特征): `Traits`可以定义没有实现的函数和值，这和`abstract class`很像。但和抽象类不同的是，一个类可以继承多个`traits`
                        , 一个`trait`不可以有构造参数。
                       
            
            > 通常，除非你确定要强制执行抽象类的单继承限制，否则始终对抽象类使用特征。
            
   - Objects: Scala 对于这些单例类有一个语言特点，称为**objects**，实例化时不需要new关键字，直接调用即可。这和Java的`static classes`很像。

   - Companion Objects: 当一个类和一个**object** 有相同的名字时，我们就称这个**object**为**companion object**。
           
        ```scala
        object Lion {
          def roar(): Unit = println("I'M AN OBJECT!")
        
        class Lion {
          def roar(): Unit = println("I'M A CLASS!")
        
        new Lion().roar() // I'M A CLASS!
        Lion.roar()       // I'M AN OBJECT!
        ```
        
        为什么要用`companion object`呢？  
        - 为一个类创建多个构造函数
        
        - 可以在类构造函数执行之前或之后执行代码
        
        - 包含与类相关的常量
        
        
        
        实例如下：  
            
        ```scala
         object Animal {
            val defaultName = "Bigfoot"
            private var numberOfAnimals = 0
            def apply(name: String): Animal = {
               numberOfAnimals += 1
               new Animal(name, numberOfAnimals)
            }
            def apply(): Animal = apply(defaultName)
         }
         class Animal(name: String, order: Int) {
     def info: String = s"Hi my name is $name, and I'm $order in line!"
     }
            
         val bunny = Animal.apply("Hopper") // Calls the Animal factory method
         println(bunny.info) // Hi my name is Hopper, and I'm 1 in line!
         val cat = Animal("Whiskers")       // Calls the Animal factory method
         println(cat.info) // Hi my name is Whiskers, and I'm 2 in line!
         val yeti = Animal()                // Calls the Animal factory method
         println(yeti.info) // Hi my name is Bigfoot, and I'm 3 in line!
     ```
     
   - Case Class  
        - `Case Class`允许外部访问类参数
        - 实例化时不需要用`new`,**因为Scala编译器自动为每一个case class生成了一个*companion object*，这个*companion object*为case class包含着一个apply方法**
        - 自动创建了一个`unapply`方法用于访问所有的**类参数**，对于`class`,如果要是访问**类参数**的话需要添加`val`,而在`case class`中则不需要。
        - 不可以被继承
          
          ```scala
          class Nail(length: Int) // Regular class
          val nail = new Nail(10) // Requires the `new` keyword
          // println(nail.length) // Illegal! Class constructor parameters are not by default externally visible
        
          class Screw(val threadSpace: Int) // By using the `val` keyword, threadSpace is now externally visible
          val screw = new Screw(2)          // Requires the `new` keyword
          println(screw.threadSpace)
        
          case class Staple(isClosed: Boolean) // Case class constructor parameters are, by default, externally visible
          val staple = Staple(false)           // No `new` keyword required
          println(staple.isClosed)
        
          case class Stapled(isClosed: Boolean) extends Staple(isClosed) // 不可以被继承
          // Error: case class Stapled has case ancestor Staple, 
          // but case-to-case inheritance is prohibited. 
          // To overcome this limitation, use extractors to pattern match on non-leaf nodes
          ```
          
          `case class`对于含有很多参数的`generator`来说是一个很好的容器，它的构造函数可以**定义派生参数**和**验证有效输入**
​         

            ```scala
            case class SomeGeneratorParameters(
                 someWidth: Int,
                 someOtherWidth: Int = 10,
                 pipelineMe: Boolean = false
            ) {
                 require(someWidth >= 0)
                 require(someOtherWidth >= 0)
                 val totalWidth = someWidth + someOtherWidth
            }
            ```
​            
        
   - inheritance With Chisel  
     Chisel中自定义的module都是继承于**Module**基类。
     每个自定义的IO都继承于`Bundle`基类，在一些特殊情况下，Bundle的supertype为`Record`。
     Chisel中的类似于`UInt`和`Bundle`的supertype为`Data`。
     
   - Module  
     在Chisel中，当你希望创建一个硬件对象时，你需要将`Module`作为父类（superclass）。
     继承可能不总是最好的方法去重用，因为组合优先于继承是一个常见规则，但是它依旧是一个很强大的方法。
     
     

3.8 types      

   - Scala中所有的对象(object)都有一个类型(type),这个类型通常是这个对象对应的类，可以通过getClass获知：
        ```scala
        println(10.getClass) // Int
              
        class MyClass {
            def myMethod = ???
        }
        println(new MyClass().getClass) // class $line6.$read$$iw$$iw$MyClass
        ```
    
   - 另外强烈建议所有的函数声明都要定义输入和输出类型，这样可以让Scala编译器发现到不合适的函数使用。
    
   - `UInt`和`Int`,`Bool`和`Boolean`是不同的，因为Scala的静态类型，所以如果你混用Scala编译器会发现到该错误，
            在编译期间，编译器会区分Chisel的类型和Scala的类型。
                
        ```scala
           val a = Wire(UInt(4.W)) // Chisel type
           a := 0.U // legal
           a := 0   // 0 is Int, a Scala type, is a illegal type for here
        
           val bool = Wire(Bool())
           val boolean: Boolean = false
        
           when (bool) {...}  // when() expects a Bool, legal
           if (boolean) {...} // if() expects a Boolean, legal
        
           if (bool) {...} // illegal
           when (boolean) {...} // illegal
        
        ```

   - Scala类型强制(Scala Coercion)
        - asInstanceOf: x.asInstanceOf[T]表示将x的类型强制转化为类型T，如果不可以强制转化为类型T将会抛出一个异常。
            ```scala
               val x: UInt = 3.U
               try {
                  println(x.asInstanceOf[Int])
               } catch {
                  case e: java.lang.ClassCastException => println("As expected, we can't cast UInt to Int")
               }
        
               // 我们可以将UInt转化为Data类型，因为UInt继承于Data
               println(x.asInstanceOf[Data]) // UInt<2>(3)
            ```
        
   - Chisel中的类型转换(Type Casting in Chisel)  
        - 最经常用的是`asTypeOf()`
        - 还有`asUInt()`和`asSInt()`
        
          可查看`test.week3.TupeConvertDemo`
        
   - 类型匹配(Type Matching)
        - Match Operator: 当我们尝试编写一个通用类型的生成器时，类型匹配是非常有用的。
                              需要注意的是Chisel类型不应该有**值匹配**（value matched），
                              因为Scala的匹配在**circuit elaboration**期间执行，
                              但是我们想要的是一个`post-elaboration`的比较.
        
            ```scala
            class InputIsZero extends Module {
               val io = IO(new Bundle {
                  val in  = Input(UInt(16.W))
                  val out = Output(Bool())
               })
               io.out := (io.in match {
                  // note that case 0.U is an error
                  case (0.U) => true.B
                  case _   => false.B
               })
            }
            ```
        
   - **Unapply 方法**  
            apply可以无需通过new操作就可以创建对象，unapply则是apply的方向操作，
            unapply接收一个对象，然后藏对象中提取值，提取的值通常是用来构造该对象的值。
            所以unapply可以为match语句在匹配期间同时提供在**类型**和**提取值**上匹配的能力。
                
            每个`case class`都会创建一个companion object，而companion object中也包含着一个unapply函数。
                
            ```scala
            case class SomeGeneratorParameters(
               someWidth: Int,
               someOtherWidth: Int = 10,
               pipelineMe: Boolean = false
            ) {
               require(someWidth >= 0)
               require(someOtherWidth >= 0)
               val totalWidth = someWidth + someOtherWidth
            }
                
            def delay(p: SomeGeneratorParameters): Int = p match {
               /**
                 * 如果写成sg: SomeGeneratorParameters(_, _, true) => sg.totalWidth * 3 将不会编译通过
                 * 写成@可以编译通过并将值赋给sg
                 */
               case sg @ SomeGeneratorParameters(_, _, true) => sg.totalWidth * 3
               case SomeGeneratorParameters(_, sw, false) => sw * 2
            }
                
            println(delay(SomeGeneratorParameters(10, 10)))       // 20
            println(delay(SomeGeneratorParameters(10, 10, true))) // 60 
            ```
        
            观察`delay`函数，可以观察到除了匹配每个字符的类型外，我们还可以直接访问内部参数的值，直接匹配内部参数的值。
            这归因为编译器实现了`unapply`方法，这只是一个语法糖，例如，下面两种语句是等价的：
            ```scala
            case p: SomeGeneratorParameters => p.sw * 2
            case SomeGeneratorParameters(_, sw, _) => sw * 2
            ```
        
            下面两句也是等价的,但是第二句除了可以直接访问内部值外仍然可以访问`parent value`
                
            ```scala
            case SomeGeneratorParameters(_, sw, true) => sw
            case sg@SomeGeneratorParameters(_, sw, true) => sw
            ```
           
            你可以直接将状态监测放入模式声明中，以下三种情况也是等价的
                
            ```scala
            case SomeGeneratorParameters(_, sw, false) => sw * 2
            case s@SomeGeneratorParameters(_, sw, false) => s.sw * 2
            case s: SomeGeneratorParameters if s.pipelineMe => s.sw * 2
            ```
            这些语法都是由类的半生对象包含的unapply方法启用，如果你想使用unapply一个类，但是又不想创建case class，可以手动实现unapply方法：
                
            ```scala
            class Boat(val name: String, val length: Int) 
            object Boat {
               def unapply(b: Boat): Option[(String, Int)] = Some((b.name, b.length))
               def apply(name: String, length: Int): Boat = new Boat(name, length)
            }
                
            def getSmallBoats(seq: Seq[Boat]): Seq[Boat] = seq.filter {
               b => b.match {
                  case Boat(_, length) if length < 60 => true
                  case Boat(_, _) => false
               }
            }
                
            val boats = Seq(Boat("Santa Maria", 62), Boat("Pinta", 56), Boat("Nina", 50))
            println(getSmallBoats(boats).map(_.name).mkString(" and ") + " are small boats!")
            ```
        
   - 偏函数(Partial Functions)
            偏函数是指只在其输入子集上定义的函数，有点类似`Option`，
            一个偏函数可能对一个特定的输入没有值，这可以通过`isDefinedAt(...)`定义。
            `isDefinedAt`是偏函数的一个方法，可以用来决定偏函数是否会接收一个给定的参数。
                
            可以将偏函数与`orElse`联系在一起。
                
            调用一个没有定义输入的偏函数将会导致运行时错误，例如，当偏函数的输入是用户定义的时将会发生，所以为了类型安全（type-safe），建议函数的返回值类型为`Option`
                
            ```scala
            val partialFunc1: PartialFunction[Int, String] = {
               case i if (i + 1) % 3 == 0 => "SomeThing"
            }
                
            partialFunc1.isDefinedAt(2) // true
            partialFunc1.isDefinedAt(5) // true
            partialFunc1.isDefinedAt(1) // false
            partialFunc1.isDefinedAt(0) // false
                
            partialFunc1(2) // SomeThing
                
            try {
               partialFunc1(0) //将会发生scala.MatchError
            } catch {
               case e: scala.MatchError => println("partialFunc1(0) = can't apply PartialFunctions where they are not defined")
            }
                
            // 一个小实验，如果返回值为Option类型
            val partialFunc2: PartialFunction[Int, Option[String]] = {
               case i if (i + 1) % 3 == 0 => Some("SomeThing")
            }
                
            partialFunc2(2) // Some(SomeThing)
            partialFunc2(0) // 还是会发生scala.MatchError,
                            // 返回值类型的改变并不影响偏函数的本质，
                            // 偏函数只受输入的影响
            
            val partialFunc3: PartialFunction[Int, String] = {
               case i if (i + 2) % 3 == 0 => "Something else"
            }
                
            val partialFunc4 = partialFunc1 orElse partialFunc3
            
            partialFunc4.isDefinedAt(0) // false
            partialFunc4.isDefinedAt(1) // true
            partialFunc4.isDefinedAt(2) // true
            partialFunc4.isDefinedAt(3) // false
            partialFunc4(1) // Something else
            partialFunc4(2) // SomeThing
            ```
           
   - Type Safe Connections  
            Chisel会对很多连接检查类型，包括`Bool/UInt to Clock`。
        对于其他类型，Chisel会允许连接，但是适当的填充或截断位，例：`src/week3/BadTypeModule.scala`
        
   - 泛型（Type Generics）/ 多态（polymorphic）
        -  Classes在类型上可以是多态的，例如：sequences，sequences要求知道它所包含的元素类型：
               
               ```scala
               val seq1 = Seq("1", "2", "3") // Type is Seq[String]
               val seq2 = Seq(1, 2, 3)       // Type is Seq[Int]
               val seq3 = Seq(1, "2", true)  // Type is Seq[Any]
               ```
        -  有时，在决定一个多态类型时，Scala编译器需要用户指定类型
        
               ```scala
               //val default = Seq() // Error!
               val default = Seq[String]() // User must tell compiler that default is of type Seq[String]
               Seq(1, "2", true).foldLeft(default){ (strings, next) =>
                  next match {
                     case s: String => strings ++ Seq(s)
                     case _ => strings
                  }
               }
               ```
        -  函数的输入输出类型也可以表现为多态，下面的实例中定义了一个time的函数，这个函数会测算执行一个代码段花费的时间，
               它参数化了返回值类型是基于代码段的返回值类型的。
               
                > `=> T`表示一个没有参数的匿名函数
           
           ```scala
            def time[T](block: => T): T = { // block只有在使用时才运行，调用的时候不必计算出其结果，这叫Call-By-Name。
                                            // 如果不加=>，称为Call-By-Value
               val t0 = System.nanoTime()
               val result = block           // 只有在这里，block才运行
               val t1 = System.nanoTime()
               val timeMillis = (t1 - t0) / 1000000.0
               println(s"Block took $timeMillis milliseconds!")
               result
            }
            
            // Adds 1 through a million
            val int = time { (1 to 1000000).reduce(_ + _) }    // Block took 836.2001 milliseconds!
            println(s"Add 1 through a million is $int")        // Add 1 through a million is 1784293664
           
            // Finds the largest number under a million that, in hex, contains "beef"
            val string = time {
               (1 to 1000000).map(_.toHexString).filter(_.contains("beef")).last
            }                                                                       // Block took 2800.7824 milliseconds!
            println(s"The largest number under a million that has beef: $string")   // The largest number under a million that has beef: ebeef
              
           ```
        
   -  Chisel Type Hierarchy
           
            Chisel3.Data是Chisel硬件类型的基类，`UInt`,`SInt`,`Vec`,`Bundle`等都是`Data`的实例化，如图：
            ![](../../../../img/type_hierarchy.svg)  
        `Data`可以被用于IOs中，并且支持`:=`,wire,reg等。  
            
            在Chisel中，Register是有一个多态代码的好例子，通过查看`RegEnable`的代码实现我们发现，该函数是`[T <: Data]`的模板，
        也就是说RegEnable可以应用于所有Chisel硬件类型。一些操作符只适合于`Bits`,如`+`,只适用于SInt和UInt，但不适用于Vec和Bundle
            
            > 这里的[A <: B]表示，A必须是B的子类，[A >: B]表示A必须是B的父类
            
            在Scala中，除了objects和functions可以被当做函数外，类型（types）也可以被当做函数。
            
            我们通常需要提供一个类型限制，如上面提到的`[T <: Data]`,若有了该限制，那传入的类型只能是Data的子类，例如`:=3`是非法的，因为3是Scala中的Int 类型，
        不是Chisel中的UInt类型。
            
        
            `week3/ShiftRegister.scala`实现了一个把类型当做参数的shift register
        
   -  Type Generics with Typeclasses
           
        前面的例子只能限制用于`Data`实例的一些简单的操作符，例如`:=`,`RegNext()`,当我们生成DSP电路时，
        我们会使用一些数学操作符，如加和乘。`dsptools`的库提供了一些工具可以参数化DSP生成器。
            
            这里有一个multiply-accumulate（MAC）的模块，它可以为`FixedPoint`,`SInt`,甚至`DspComplex[T]`（dsptools 提供的一个复杂的函数类型）生成一个multiply-accumulate。
            
            `A <: B : C `表示A是B的子类，同时也是C类型。
            
            ```scala
            import chisel3.experimental._
            import dsptools.numbers._
            
            /**
              * [T <: Data : Ring] 表示T是Data的子类，同时也是Ring类型，Ring在dsptools中定义为带有+和*De数
              * Ring的替代品是Real，但并不允许生成一个DspComplex()的MAC，因为complex numbers不是Real
              */
            class Mac[T <: Data : Ring](genIn : T, genOut: T) extends Module {
               val io = IO(new Bundle {
                  val a = Input(genIn.cloneType)
                  val b = Input(genIn.cloneType)
                  val c = Input(genIn.cloneType)
                  val out = Output(genOut.cloneType)
            })
               io.out := io.a * io.b + io.c
            
            }
            
            // verilog代码生成 
            // UInt: test:runMain utils.getVerilog macforuint
            // SInt: test:runMain utils.getVerilog macforsint
            // FixedPoint: test:runMain utils.getVerilog macforfixedpoint
             
            println(getVerilog(new Mac(UInt(4.W), UInt(6.W)) ))
            
            println(getVerilog(new Mac(SInt(4.W), SInt(6.W)) ))
            
            println(getVerilog(new Mac(FixedPoint(4.W, 3.BP), FixedPoint(6.W, 4.BP))))
               
            ```


   - 创建自定义的类型  
      ​  