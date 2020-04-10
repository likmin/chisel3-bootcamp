package week3

import chisel3._

/**
  * 3.1_parameters
  * Options for Parameters with Defaults
  * 在Scala中Options选项用来表示一个值是可选的（有值或无值）
  * 例如：Option[T]表示一个类型为T的可选值的容器:
  *         如果值存在，Option[T]就是一个Some[T],
  *         如果不存在,Option[T]就是对象None
  * 
  * Example: Optional Reset
  *    
  */

class DelayBy1(resetValue: Option[UInt] = None) extends Module {
    val io = IO(new Bundle {
        val in = Input(UInt(16.W))
        val out = Output(UInt(16.W))
    })

    // 方法一: 
    // val reg = if(resetValue.isDefined) { // resetValue = Some(number)
    //     RegInit(resetValue.get) 
    // } else {
    //     Reg(UInt())
    // }

    // 方法二：用Option模式匹配

    val reg = resetValue match {
    ////    case Some(x) => RegInit(UInt(x))不是这样初始化的
        case Some(x) => RegInit(x)
    ////    case None    => RegInit(UInt())
        case None   => Reg(UInt())

        /**
          * warning : RegInit不可以只用datatype，详情可看week2.RegInitModule.scala
          */
    }

    reg := io.in
    io.out := reg
}