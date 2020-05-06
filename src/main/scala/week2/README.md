## Week2
#### 2.1. Registers和Wires在控制流上非常相似：   

   - Registers也有last connect   

    最后一个赋值是有效的，但是与wire不同的是，Register的赋值语句是在always模块中的，而wire会转变为Verilog中的组合逻辑

   - 可以用when,elsewhen,otherwise有条件的被赋值   

    Register条件赋值会被转化成一堆“与或语句”，而这里会转会为always模块中的if...else语句

   > TODO: ElasticReg 在Boom源代码中

#### 2.2. ChiselTest

   - ChiselTest为`Decoupled`接口提供了一些有效的测试工具  
        - `initSource`和`setSourceClock`可以在测试开始之前正确的初始化`ready`和`valid`字段
        - `enqueueNow`可以向`Decoupled`Input接口中输入一个元素。`DequeueNow`可以从`Decoupled`Output接口中取一个元素
        - `enqueueSeq`可以一次性的将`Seq`中的元素输入到`Decoupled`Input接口中，`expectDequeueNow`将`Decoupled`Output
            接口中的所有元素一次性取出来，然后和`Seq`中的下一个元素比较。  


   > 但是如果`Seq`中的元素多于队列的深度，会溢出，可以通过fork和join来解决

#### 2.3. `fork`启动一个并发代码块，附加的fork在前面的fork的代码块末尾通过`.fork{...}`添加，

​            这样`fork {...}`和`.fork{...}`可并发的执行。join: 将多个相关分叉重新组合
​            （re-unities）重新组合到调用线程中。
​            