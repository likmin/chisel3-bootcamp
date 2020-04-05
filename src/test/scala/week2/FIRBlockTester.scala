package week2

import chisel3._
import chisel3.iotesters.{Driver, PeekPokeTester}

import dsptools.tester.MemMasterModel
import freechips.rocketchip.amba.axi4

import dspblocks._
import freechips.rocketchip.amba.axi4._
import freechips.rocketchip.amba.axi4stream._
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.regmapper._

abstract class FIRBlockTester[D, U, EO, EI, B <: Data](c: FIRBlock[D, U, EO, EI, B]) extends PeekPokeTester(c.module) with MemMasterModel {
    // check that address 0 is the number of filters
    require(memReadWord(0) == c.nFilters)
    // write 1 to all the taps
    for (i <- 0 until c.nFilters * c.nTaps) {
        memWriteWord(8 + i * 8, 1)
    }
}

// specialize the generic tester for axi4
class AXI4FIRBlockTester(c: AXI4FIRBlock with AXI4StandaloneBlock) extends FIRBlockTester(c) with AXI4MasterModel {
    def memAXI = c.ioMem.get
}

object FIRBlockTester {
    def apply(){
        // invoking testers on lazymodules is a little strange.
        // note that the firblocktester takes a lazymodule, not a module (it calls .module in "extends PeekPokeTester()").
        val lm = LazyModule(new AXI4FIRBlock(1, 8)(Parameters.empty) with AXI4StandaloneBlock)
        chisel3.iotesters.Driver(() => lm.module) { _ => new AXI4FIRBlockTester(lm) }
    }
}
