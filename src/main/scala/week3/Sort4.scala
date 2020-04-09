package week3

import chisel3._
import chisel3.util._

/**
  * 3.1_parameters 
  * Example: Parameterized 4-Input Sort
  */
  
/** Sort4 sorts its 4 inputs to its 4 outputs */
class Sort4(ascending: Boolean) extends Module {
  val io = IO(new Bundle {
    val in0 = Input(UInt(16.W))
    val in1 = Input(UInt(16.W))
    val in2 = Input(UInt(16.W))
    val in3 = Input(UInt(16.W))
    val out0 = Output(UInt(16.W))
    val out1 = Output(UInt(16.W))
    val out2 = Output(UInt(16.W))
    val out3 = Output(UInt(16.W))
  })
    
  // this comparison funtion decides < or > based on the module's parameterization
  def comp(l: UInt, r: UInt): Bool = {
      if (ascending) {
        l < r
      } else {
        l > r
    }
  }

  val row10 = Wire(UInt(16.W))
  val row11 = Wire(UInt(16.W))
  val row12 = Wire(UInt(16.W))
  val row13 = Wire(UInt(16.W))

  when(comp(io.in0, io.in1)) {
    row10 := io.in0            // preserve first two elements
    row11 := io.in1
  }.otherwise {
    row10 := io.in1            // swap first two elements
    row11 := io.in0
  }

  when(comp(io.in2, io.in3)) {
    row12 := io.in2            // preserve last two elements
    row13 := io.in3
  }.otherwise {
    row12 := io.in3            // swap last two elements
    row13 := io.in2
  }

  val row21 = Wire(UInt(16.W))
  val row22 = Wire(UInt(16.W))

  when(comp(row11, row12)) {
    row21 := row11            // preserve middle 2 elements
    row22 := row12
  }.otherwise {
    row21 := row12            // swap middle two elements
    row22 := row11
  }

  val row31 = Wire(UInt(16.W))
  val row32 = Wire(UInt(16.W))
  when(comp(row10, row13)) {
    row31 := row10            // preserve middle 2 elements
    row32 := row13
  }.otherwise {
    row31 := row13            // swap middle two elements
    row32 := row10
  }

  when(comp(row10, row21)) {
    io.out0 := row31            // preserve first two elements
    io.out1 := row21
  }.otherwise {
    io.out0 := row21            // swap first two elements
    io.out1 := row31
  }

  when(comp(row22, row13)) {
    io.out2 := row22            // preserve first two elements
    io.out3 := row32
  }.otherwise {
    io.out2 := row32            // swap first two elements
    io.out3 := row22
  }
}