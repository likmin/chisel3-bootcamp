package week3

import chisel3._

/**
  * 3.6 types
  * Example: Type Generic ShiftRegister
  * 
  */
class ShiftRegisterIO[T <: Data](gen: T, n: Int) extends Bundle {
    require(n >= 0, "Shift register must have non-negative shift")
    
    val in = Input(gen.cloneType)
    val out = Output(Vec(n + 1, gen.cloneType)) 
    
    /**
      * p.asInstanceOf[T]将p强制转化为T的实例对象
      */
    override def cloneType: this.type = (new ShiftRegisterIO(gen, n)).asInstanceOf[this.type]
}

class ShiftRegister[T <: Bits](gen: T, n: Int) extends Module {
    val io = IO(new ShiftRegisterIO(gen, n))

    io.out.foldLeft(io.in) {
        case (in, out) => out := in
                          RegNext(in)
    }
}