package week2

import chisel3._

class Sort4 extends Module{
  val io = IO(new Bundle{
    val in0 = Input(UInt(16.W))
    val in1 = Input(UInt(16.W))
    val in2 = Input(UInt(16.W))
    val in3 = Input(UInt(16.W))
    val out0 = Output(UInt(16.W))
    val out1 = Output(UInt(16.W))
    val out2 = Output(UInt(16.W))
    val out3 = Output(UInt(16.W))
  })

  val row10 = Wire(UInt(16.W))
  val row11 = Wire(UInt(16.W))
  val row12 = Wire(UInt(16.W))
  val row13 = Wire(UInt(16.W))
  val row20 = Wire(UInt(16.W))
  val row21 = Wire(UInt(16.W))
  val row22 = Wire(UInt(16.W))
  val row23 = Wire(UInt(16.W))

   /* way1 : 组合逻辑*/

    row10 := Mux(io.in0 < io.in1, io.in0, io.in1)
    row11 := Mux(io.in0 > io.in1, io.in0, io.in1)
    row12 := Mux(io.in2 < io.in3, io.in2, io.in3)
    row13 := Mux(io.in2 > io.in3, io.in2, io.in3)

    row20 := Mux(row10 < row13, row10, row13)
    row21 := Mux(row11 < row12, row11, row12)
    row22 := Mux(row11 < row12, row12, row11)
    row23 := Mux(row10 < row13, row13, row10)

    io.out0 := Mux(row20 < row21, row20, row21)
    io.out1 := Mux(row20 < row21, row21, row20)
    io.out2 := Mux(row22 < row23, row22, row23)
    io.out3 := Mux(row22 < row23, row23, row22)


  /*
    way2 : use when(){...} .elsewhen(){...}
  */

  /*
    /* 1.The first round sort */
    when(io.in0 < io.in1) {
      row10 := io.in0
      row11 := io.in1
    } .otherwise {
      row10 := io.in1
      row11 := io.in0
    }

    when(io.in2 < io.in3) {
      row12 := io.in2
      row13 := io.in3
    } .otherwise {
      row12 := io.in3
      row13 := io.in2
    }

    /* 2.The second round sort */
    when(row10 < row13) {
      row20 := row10
      row23 := row13
    } .otherwise {
      row20 := row13
      row23 := row10
    }

    when(row11 < row12) {
      row21 := row11
      row22 := row12
    } .otherwise {
      row21 := row12
      row22 := row11
    }

    /* 3.The third round sort */
    when(row20 < row21) {
      io.out0 := row20
      io.out1 := row21
    } .otherwise {
      io.out0 := row21
      io.out1 := row20
    }

    when(row22 < row23) {
      io.out2 := row22
      io.out3 := row23
    } .otherwise {
      io.out2 := row23
      io.out3 := row22
    }
*/


}
