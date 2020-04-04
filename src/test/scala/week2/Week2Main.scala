package week2


import chisel3._
import chisel3.iotesters.{TesterOptionsManager, Driver, PeekPokeTester}
import utils.TutorialRunner

object Week2Main {

  val tests = Map(
    "passthrough" -> {
      (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new Passthrough, manager) {
        (c) => new PassthroughTester(c)
      }
    },

    "arbiter" -> {
      (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new Arbiter,manager) {
        (c) => new ArbiterTester(c)
      }
    },

    "passthrough10" -> {
      (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new PassthroughGenerator(10), manager){
        (p) => new PassthroughGeneratorTester(p)
      }
    },

    "mymodule" -> {
      (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new MyModule, manager){
        (c) => new MyModuleTester(c)
      }
    },

    "myoperators" -> {
      (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new MyOperators, manager){
        (c) => new MyOperatorsTester(c)
      }
    },

    "lastconnect" -> {
      (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new LastConnect, manager){
        (c) => new LastConnectTester(c)
      }
    },

    "max3" -> {
      (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new Max3, manager){
        (c) => new Max3Tester(c)
      }
    },

    "sort4" -> {
      (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new Sort4, manager){
        (c) => new Sort4Tester(c)
      }
    },

    "bettersort4" -> {
      (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new Sort4, manager){
        (c) => new BetterSort4Tester(c)
      }
    },

    "polynomial" -> {
      (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new Polynomial, manager){
        (c) => new PolynomialTester(c)
      }
    },

    "gradlife" -> {
      (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new GradLife, manager){
        (c) => new GradLifeTester(c)
      }
    },

    "registermodule" -> {
      (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new RegisterModule, manager){
        (c) => new RegisterModuleTest(c)
      }
    },

    "regnextmodule" -> {
      (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new RegNextModule, manager){
        (c) => new RegNextModuleTest(c)
      }
    },

    "myshiftregister" -> {
      (manager: TesterOptionsManager) => iotesters.Driver.execute(() => new MyShiftRegister(n = 64), manager){
        (c) => new MyShiftRegisterTester(c)
      }
      
    } 
  )

  def main(args: Array[String]): Unit = {

    /**
      * 参数化测试myshiftregister
      * TODO: 1.将测试的格式统一起来。2.可以传递参数
      * 
      */
    if(args.length == 1 && args(0).toLowerCase == "myshiftregister") {
      for (i <- Seq(3, 4, 8, 24, 65)) {
        println(s"Testing n=$i")
        assert(Driver(() => new MyShiftRegister(n = i)) {
          c => new MyShiftRegisterTester(c)
        })
      }
    } else if(args.length == 1 && args(0).toLowerCase == "my4elementfir") {
      println("Test for My4Element start!")

      println("Test 1 : Simple sanity check: a element with all zero coefficients should always produce zero")

      Driver(() => new My4ElementFir(0, 0, 0, 0)) {
        c => new PeekPokeTester(c) {
          poke(c.io.in, 0)
          expect(c.io.out, 0)
          step(1)
          poke(c.io.in, 4)
          expect(c.io.out, 0)
          step(1)
          poke(c.io.in, 5)
          expect(c.io.out, 0)
          step(1)
          poke(c.io.in, 2)
          expect(c.io.out, 0)
        }
      }
      
      println("Test 2 : Simple 4-point moving average")
      Driver(() => new My4ElementFir(1, 1, 1, 1)) {
        c => new PeekPokeTester(c) {
          poke(c.io.in, 1)
          expect(c.io.out, 1)  // 1, 0, 0, 0
          step(1)
          poke(c.io.in, 4)
          expect(c.io.out, 5)  // 4, 1, 0, 0
          step(1)
          poke(c.io.in, 3)
          expect(c.io.out, 8)  // 3, 4, 1, 0
          step(1)
          poke(c.io.in, 2)
          expect(c.io.out, 10)  // 2, 3, 4, 1
          step(1)
          poke(c.io.in, 7)
          expect(c.io.out, 16)  // 7, 2, 3, 4
          step(1)
          poke(c.io.in, 0)
          expect(c.io.out, 12)  // 0, 7, 2, 3
        }
      }

      println("Test 3 : Nonsymmetric filter")
      Driver(() => new My4ElementFir(1, 2, 3, 4)) {
        c => new PeekPokeTester(c) {
          poke(c.io.in, 1)
          expect(c.io.out, 1)  // 1*1, 0*2, 0*3, 0*4
          step(1)
          poke(c.io.in, 4)
          expect(c.io.out, 6)  // 4*1, 1*2, 0*3, 0*4
          step(1)
          poke(c.io.in, 3)
          expect(c.io.out, 14)  // 3*1, 4*2, 1*3, 0*4
          step(1)
          poke(c.io.in, 2)
          expect(c.io.out, 24)  // 2*1, 3*2, 4*3, 1*4
          step(1)
          poke(c.io.in, 7)
          expect(c.io.out, 36)  // 7*1, 2*2, 3*3, 4*4
          step(1)
          poke(c.io.in, 0)
          expect(c.io.out, 32)  // 0*1, 7*2, 2*3, 3*4
        }
      }
      println("Test for My4Element end!")

    } else TutorialRunner("Week2Main",tests, args)
  }
}