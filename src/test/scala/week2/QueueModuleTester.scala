package week2

import chisel3._
import chisel3.util._
import chisel3.experimental._
import chisel3.experimental.BundleLiterals._
import chisel3.tester._
import chisel3.tester.RawTester.test // brings in version of test(...) 


/**
  * 2.6_testers2
  *
  * Modules with Decoupled Interfaces
  * `Decoupled`采用了Chisel的数据结构，并且提供了`ready`和`valid`信号，ChiselTest提供了
  * 一些工具可用于自动和有效的测试这些接口
  * 
  *   EnqueueNow and expectDequeueNow : insert and extract values from the queue
  *   enqueueNow: Add one element to a `Decoupled` input interface              // enqueue 
  *   expectDequeueNow: Removes one element from a `Decoupled` output interface // dequeue
  *
  *   EnqueueSeq and DequeueSeq : deal with queuing and dequeuing operations in single operations
  *   enqueueSeq: 继续从Seq中添加元素到`Decoupled`输入接口中，注意是一次性的将sequence中的元素都添加进去，
  *               所以，sequence中的元素个数不可以大于queue的总量，否则会出现`TimeoutException`错误。
  *               EnqueueNow and expectDequeueNow不会出现
  *   expectDequeueSeq: 将`Decoupled`输出接口中的元素一次性全部移出去，并且和`Seq`中元素比较
  *
  *   Fork and Join in ChiselTest : running sections of a unit test concurrently
  *   fork: 启动一个并发代码块，附加的fork在前面的fork的代码块末尾通过`.fork{...}`添加，
  *         这样`fork {...}`和`.fork{...}`可并发的执行
  *   join: 将多个相关分叉重新组合（re-unities）重新组合到调用线程中
  */
object QueueModuleTester {
    def apply() {
        test(new QueueModule(UInt(9.W), entries = 200)) {
            /**
              * initSource()，setSourceClock(c.clock)等
              * 为了确保在测试开始的时候正确的初始化ready和valid字段
              * 
              */

            c => c.in.initSource()
                 c.in.setSourceClock(c.clock)
                 c.out.initSink()
                 c.out.setSinkClock(c.clock)

                /**
                  * 
                  */
                //  val testVector = Seq.tabulate(200) {i => i.U}

                //  testVector.zip(testVector).foreach{ case (in,out) => 
                //     c.in.enqueueNow(in)
                //     c.out.expectDequeueNow(out)
                //  }

                /**
                  * warning: 这里testVector中元素的个数不可以大于200，否则会出现`TimeoutException`错误
                  * TODO: queue溢出了怎么出现了`TimeoutException`错误呢
                  * 
                  */
                //  val testVector = Seq.tabulate(100) {i => i.U}
                //  c.in.enqueueSeq(testVector)
                //  c.out.expectDequeueSeq(testVector)


                /**
                  * c.in.enqueueSeq(testVector)和 c.out.expectDequeueSeq(testVector)可并发执行
                  */
                val testVector = Seq.tabulate(300){ i => i.U }

                fork {
                    c.in.enqueueSeq(testVector)
                }.fork {
                    c.out.expectDequeueSeq(testVector)
                }.join()

        }
    }
}