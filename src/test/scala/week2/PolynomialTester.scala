package week2

import chisel3.iotesters.PeekPokeTester

// verify that the computation is correct
class PolynomialTester(c: Polynomial) extends PeekPokeTester(c) {
  
  def poly0(x: Int): Int = x*x - 2*x + 1
  def poly1(x: Int): Int = 2*x*x + 6*x + 3
  def poly2(x: Int): Int = 4*x*x - 10*x - 5
  
  def poly(select: Int, x: Int): Int = {
    if(select == 0) poly0(x)
    else if(select == 1) poly1(x)
    else poly2(x)
  }

  for(x <- 0 to 20) {
    for(select <- 0 to 2) {
      poke(c.io.select, select)
      poke(c.io.x, x)
      expect(c.io.fOfX, poly(select, x))
    }
  }
}