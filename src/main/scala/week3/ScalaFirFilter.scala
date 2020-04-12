package week3

/**
  * A naive implementation of an FIR filter with an arbitrary number of taps.
  * 
  */
class ScalaFirFilter(taps: Seq[Int]) {
  var pseudoRegisters = List.fill(taps.length)(0)

  def poke(value: Int): Int = {

    pseudoRegisters = value :: pseudoRegisters.take(taps.length - 1)
    var accumulator = 0
    /**
      * taps.indices 返回类型为Range，返回结果为0 until taps.length
      */
    for(i <- taps.indices) { 
      accumulator += taps(i) * pseudoRegisters(i)
    }
    accumulator
  }
}