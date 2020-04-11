package week3


object VerbosityImplicit {
    sealed trait Verbosity
    implicit case object Silent extends Verbosity
    case object Verbose extends Verbosity
}
