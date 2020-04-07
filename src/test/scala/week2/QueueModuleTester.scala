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
  * EnqueueNow and expectDequeueNow
  * enqueueNow: Add one element to a Decoupled input interface              // enqueue 
  * expectDequeueNow: Removes one element from a Decoupled output interface // dequeue
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
                 val testVector = Seq.tabulate(100) {i => i.U}

               
                //  testVector.zip(testVector).foreach{ case (in,out) => 
                //     c.in.enqueueNow(in)
                //     c.out.expectDequeueNow(out)
                //  }

                 c.in.enqueueSeq(testVector)
                 c.out.expectDequeueSeq(testVector)
        }
    }
}