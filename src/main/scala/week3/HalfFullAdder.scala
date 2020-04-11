package week3

import chisel3._

/**
  * 3.1_parameters
  * IOs with Optional Fields
  * 在IO中有些信号我们希望在debugging时保留，但是在真正的时候不希望这些信号存在，
  * 再Chisel中可以用两种方法解决
  *   1.信号用Option来定义。
  *     Option的信号有两种选择，有值的时候为Some(Input(UInt(width.W))), 
  *     没有值时为None，我们可以利用getOrElse为其定义默认值
  *   
  *   2.信号用Zero-Width Wires
  *     在Chisel中允许信号的宽度为0，当信号宽度为0时，在生成Verilog时不
  *     会生成相应的信号，并且任何使用宽度为0的Wire的值时都会得到常数0.
  *     可通过以下命令分别生成hasCarry分别为true和false时的verilog代码。
  *     {{{
  *       test:runMain utils.getVerilog halffulladderwithcarry
  *     }}}
  *     
  *     {{{
  *       test:runMain utils.getVerilog halffulladderwithoutcarry
  *     }}}
  *     
  */
class HalfFullAdder(val hasCarry: Boolean) extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(1.W))
    val b = Input(UInt(1.W))
    
    // 1.信号用Option来定义
    // val carryIn = if (hasCarry) Some(Input(UInt(1.W))) else None
  
    // 2.信号用Zero-Width Wires
    val carryIn = Input(if(hasCarry) UInt(1.W) else UInt(0.W))
    val s = Output(UInt(1.W))
    val carryOut = Output(UInt(1.W))
  })
  
  // 1.信号用Option来定义,由于可能取值为None，所以用getOrElse设定为None时的默认值
  // val sum = io.a +& io.b +& io.carryIn.getOrElse(0.U)

  // 2.信号用Zero-Width Wires
  val sum = io.a +& io.b +& io.carryIn

  io.s := sum(0)
  io.carryOut := sum(1)
}

