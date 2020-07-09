package com.workflowfm.healthcarehandover.processes

import scala.concurrent._
import com.workflowfm.pew._
import com.workflowfm.pew.execution._
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._

trait Copy_OpenContract_2 extends ((OpenContract) => Future[(OpenContract,OpenContract)]) with AtomicProcess {
	override val name = "Copy_OpenContract_2"
	override val output = (PiPair(Chan("Copy_OpenContract_2_0_l_a_OpenContract"),Chan("Copy_OpenContract_2_0_r_a_OpenContract")),"oCopy_OpenContract_2_lB_OpenContract_x_OpenContract_rB_")
	override val inputs = Seq((Chan("Copy_OpenContract_2_0__a_OpenContract"),"cCopy_OpenContract_2_OpenContract_1"))
	override val channels = Seq("cCopy_OpenContract_2_OpenContract_1","oCopy_OpenContract_2_lB_OpenContract_x_OpenContract_rB_")

	def run(args:Seq[PiObject])(implicit ec:ExecutionContext):Future[PiObject] = args match {
		case Seq(o1) => this(PiObject.getAs[OpenContract](o1)) map PiObject.apply
	}
}
