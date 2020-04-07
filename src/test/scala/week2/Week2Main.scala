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
      My4ElementFirTester()
    } else if(args.length == 1 && args(0).toLowerCase == "firblock") {
      FIRBlockTester()
    } else if(args.length == 1 && args(0).toLowerCase == "passthroughgeneratorchiseltester") {
      PassthroughGeneratorChiselTester()
    } else if(args.length == 1 && args(0).toLowerCase == "queuemodule") {
      QueueModuleTester()
    }else TutorialRunner("Week2Main",tests, args)
  }
}