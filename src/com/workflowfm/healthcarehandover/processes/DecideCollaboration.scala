package com.workflowfm.healthcarehandover.processes

import scala.concurrent._
import com.workflowfm.pew._
import com.workflowfm.pew.execution._
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._

trait DecideCollaboration extends ((RequestedContract) => Future[Either[(AcceptedContract,ServiceProvider),RejectedContract]]) with AtomicProcess {
	override val name = "DecideCollaboration"
	override val output = (PiOpt(PiPair(Chan("DecideCollaboration_0_ll_a_AcceptedContract"),Chan("DecideCollaboration_0_lr_a_ServiceProvider")),Chan("DecideCollaboration_0_r_a_RejectedContract")),"oDecideCollaboration_lB_lB_AcceptedContract_x_ServiceProvider_rB_Plus_RejectedContract_rB_")
	override val inputs = Seq((Chan("DecideCollaboration_0__a_RequestedContract"),"cDecideCollaboration_RequestedContract_1"))
	override val channels = Seq("cDecideCollaboration_RequestedContract_1","oDecideCollaboration_lB_lB_AcceptedContract_x_ServiceProvider_rB_Plus_RejectedContract_rB_")

	def run(args:Seq[PiObject])(implicit ec:ExecutionContext):Future[PiObject] = args match {
		case Seq(o1) => this(PiObject.getAs[RequestedContract](o1)) map PiObject.apply
	}
}
