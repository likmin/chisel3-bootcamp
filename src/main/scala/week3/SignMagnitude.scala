package week3

import chisel3._
import dsptools.numbers.Ring
/**
  * 3.6 types
  * Exercise: Sign-magnitude Numbers，符号-幅度码，又称原码
  * 假设你想使用一个原码表示形式并想重用所有的DSP生成器，
  * Typeclasses支持这种特殊的多态。
  *
  * 这里将给出原码类型的初级实现以及Ring类型的实现
  * */

class SignMagnitude(val magnitudeWidth: Option[Int] = None) extends Bundle {
  val sign = Bool()
  val magnitude = magnitudeWidth match {
    case Some(w)  => UInt(w.W)
    case None     => UInt()
  }

  def +(that: SignMagnitude): SignMagnitude = {
    val result = Wire(new SignMagnitude())
    val signsTheSame = this.sign === that.sign

    when(signsTheSame) {
      result.sign      := this.sign
      result.magnitude := this.magnitude + that.magnitude
    } .otherwise {
      when(this.magnitude > that.magnitude) {
        result.sign       := this.sign
        result.magnitude  :=  this.magnitude - that.magnitude
      } .otherwise {
        result.sign       :=  that.sign
        result.magnitude  := that.magnitude - this.magnitude
      }
    }
    result
  }

  def -(that: SignMagnitude): SignMagnitude = {
    this.+(that)
  }
  def unary_-(): SignMagnitude = {
    val result = Wire(new SignMagnitude())
    result.sign      := !this.sign
    result.magnitude := this.magnitude
    result
  }

  def *(that: SignMagnitude): SignMagnitude = {
    val result = Wire(new SignMagnitude())
    result.sign      := this.sign ^ that.sign
    result.magnitude := this.magnitude * that.magnitude
    result
  }

  override def cloneType: this.type = new SignMagnitude(magnitudeWidth).asInstanceOf[this.type]
}

trait SignMagnitudeRing extends Ring[SignMagnitude] {
  def plus(f: SignMagnitude, g: SignMagnitude): SignMagnitude = {
    f + g
  }

  def times(f: SignMagnitude, g: SignMagnitude): SignMagnitude = {
    f * g
  }

  def one: SignMagnitude = {
    val one = Wire(new SignMagnitude(Some(1)))
    one.sign  :=  false.B
    one.magnitude := 1.U
    one
  }

  def zero: SignMagnitude = {
    val zero = Wire(new SignMagnitude(Some(0)))
    zero.sign := false.B
    zero.magnitude  := 0.U
    zero
  }
  def negate(f: SignMagnitude): SignMagnitude = {
    -f
  }

  def minusContext(f: SignMagnitude, g: SignMagnitude): SignMagnitude = {
    val minus = Wire(new SignMagnitude())
    when((f - g).sign === false.B) { minus := g }
    .otherwise{ minus := f }
    minus
  }
  def negateContext(f: SignMagnitude): SignMagnitude = -f

  def plusContext(f: SignMagnitude, g: SignMagnitude): SignMagnitude = f + g

  def timesContext(f: SignMagnitude, g: SignMagnitude): SignMagnitude = ???
}



