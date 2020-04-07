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
  * 1.same basic operations between iotesters and ChiselTest
  *
    *                 iotesters	                ChiselTest
    * poke	    poke(c.io.in1, 6)	            c.io.in1.poke(6.U)
    * peek	    peek(c.io.out1)	                c.io.out1.peek(6.U)
    * expect    expect(c.io.out1, 6)	        c.io.out1.expect(6.U)
    * step	    step(1)	                        c.io.clock.step(1)
    * initiate	Driver.execute(...) { c =>	    test(...) { c =>
  *
  * 2.二者的不同
  *  1) ChiselTest中的 'test'方法需要很少的模板(boiler plate)
  *  2) 'poke'和 'expect'方法为每个独立的 'io'元素的一部分，这为测试人员提供了重要的提示，
  *     以便更好地检查类型， 'peek'和 'step'也是io元素的
  *  3) ChiselTest非常的简洁，但同时在更高级和有趣的示例中它也提供更强大的检测(checking)能力,
  *     This will be further enhanced with coming improvements in the ability to specify Bundle literals
  *    
  */

object PassthroughGeneratorChiselTester {
    def apply() {
        test(new PassthroughGenerator(16)) {
            c => c.io.in.poke(0.U)
                c.clock.step(1)
                c.io.out.expect(0.U)
                c.io.in.poke(1.U)
                c.clock.step(1)
                c.io.out.expect(1.U)
                c.io.in.poke(2.U)
                c.clock.step(1)
                c.io.out.expect(2.U)
        }
    }

}