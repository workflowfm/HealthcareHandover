package com.workflowfm.healthcarehandover.processes

import scala.concurrent._
import com.workflowfm.pew._
import com.workflowfm.pew.execution._
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._

trait ProvideService extends ((OpenContract, PendingHealthcareService) => Future[Either[CompletedHealthcareService,(Obstacle,PendingHealthcareService)]]) with AtomicProcess {
	override val name = "ProvideService"
	override val output = (PiOpt(Chan("ProvideService_0_l_a_CompletedHealthcareService"),PiPair(Chan("ProvideService_0_rl_a_Obstacle"),Chan("ProvideService_0_rr_a_PendingHealthcareService"))),"oProvideService_lB_CompletedHealthcareService_Plus_lB_Obstacle_x_PendingHealthcareService_rB_rB_")
	override val inputs = Seq((Chan("ProvideService_0__a_OpenContract"),"cProvideService_OpenContract_1"),(Chan("ProvideService_1__a_PendingHealthcareService"),"cProvideService_PendingHealthcareService_2"))
	override val channels = Seq("cProvideService_OpenContract_1","cProvideService_PendingHealthcareService_2","oProvideService_lB_CompletedHealthcareService_Plus_lB_Obstacle_x_PendingHealthcareService_rB_rB_")

	def run(args:Seq[PiObject])(implicit ec:ExecutionContext):Future[PiObject] = args match {
		case Seq(o1,o2) => this(PiObject.getAs[OpenContract](o1),PiObject.getAs[PendingHealthcareService](o2)) map PiObject.apply
	}
}
