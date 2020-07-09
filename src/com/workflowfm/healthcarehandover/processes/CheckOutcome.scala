package com.workflowfm.healthcarehandover.processes

import scala.concurrent._
import com.workflowfm.pew._
import com.workflowfm.pew.execution._
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._

trait CheckOutcome extends ((OpenContract, CompletedHealthcareService, HealthcareActor) => Future[(CheckedHealthcareService,ClosedContract)]) with AtomicProcess {
	override val name = "CheckOutcome"
	override val output = (PiPair(Chan("CheckOutcome_0_l_a_CheckedHealthcareService"),Chan("CheckOutcome_0_r_a_ClosedContract")),"oCheckOutcome_lB_CheckedHealthcareService_x_ClosedContract_rB_")
	override val inputs = Seq((Chan("CheckOutcome_0__a_OpenContract"),"cCheckOutcome_OpenContract_1"),(Chan("CheckOutcome_1__a_CompletedHealthcareService"),"cCheckOutcome_CompletedHealthcareService_2"),(Chan("CheckOutcome_2__a_HealthcareActor"),"cCheckOutcome_HealthcareActor_3"))
	override val channels = Seq("cCheckOutcome_OpenContract_1","cCheckOutcome_CompletedHealthcareService_2","cCheckOutcome_HealthcareActor_3","oCheckOutcome_lB_CheckedHealthcareService_x_ClosedContract_rB_")

	def run(args:Seq[PiObject])(implicit ec:ExecutionContext):Future[PiObject] = args match {
		case Seq(o1,o2,o3) => this(PiObject.getAs[OpenContract](o1),PiObject.getAs[CompletedHealthcareService](o2),PiObject.getAs[HealthcareActor](o3)) map PiObject.apply
	}
}
