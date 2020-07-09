package com.workflowfm.healthcarehandover.processes

import scala.concurrent._
import com.workflowfm.pew._
import com.workflowfm.pew.execution._
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._

trait RequestAssignment extends ((Patient, HealthcareActor, HealthcareService) => Future[(Assignment,(RequestedContract,(ServiceRequester,PendingHealthcareService)))]) with AtomicProcess {
	override val name = "RequestAssignment"
	override val output = (PiPair(Chan("RequestAssignment_0_l_a_Assignment"),PiPair(Chan("RequestAssignment_0_rl_a_RequestedContract"),PiPair(Chan("RequestAssignment_0_rrl_a_ServiceRequester"),Chan("RequestAssignment_0_rrr_a_PendingHealthcareService")))),"oRequestAssignment_lB_Assignment_x_lB_RequestedContract_x_lB_ServiceRequester_x_PendingHealthcareService_rB_rB_rB_")
	override val inputs = Seq((Chan("RequestAssignment_0__a_Patient"),"cRequestAssignment_Patient_1"),(Chan("RequestAssignment_1__a_HealthcareActor"),"cRequestAssignment_HealthcareActor_2"),(Chan("RequestAssignment_2__a_HealthcareService"),"cRequestAssignment_HealthcareService_3"))
	override val channels = Seq("cRequestAssignment_Patient_1","cRequestAssignment_HealthcareActor_2","cRequestAssignment_HealthcareService_3","oRequestAssignment_lB_Assignment_x_lB_RequestedContract_x_lB_ServiceRequester_x_PendingHealthcareService_rB_rB_rB_")

	def run(args:Seq[PiObject])(implicit ec:ExecutionContext):Future[PiObject] = args match {
		case Seq(o1,o2,o3) => this(PiObject.getAs[Patient](o1),PiObject.getAs[HealthcareActor](o2),PiObject.getAs[HealthcareService](o3)) map PiObject.apply
	}
}
