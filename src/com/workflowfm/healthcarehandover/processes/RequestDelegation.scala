package com.workflowfm.healthcarehandover.processes

import scala.concurrent._
import com.workflowfm.pew._
import com.workflowfm.pew.execution._
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._

trait RequestDelegation extends ((Patient, HealthcareActor, HealthcareService) => Future[(Delegation,(RequestedContract,(ServiceRequester,PendingHealthcareService)))]) with AtomicProcess {
	override val name = "RequestDelegation"
	override val output = (PiPair(Chan("RequestDelegation_0_l_a_Delegation"),PiPair(Chan("RequestDelegation_0_rl_a_RequestedContract"),PiPair(Chan("RequestDelegation_0_rrl_a_ServiceRequester"),Chan("RequestDelegation_0_rrr_a_PendingHealthcareService")))),"oRequestDelegation_lB_Delegation_x_lB_RequestedContract_x_lB_ServiceRequester_x_PendingHealthcareService_rB_rB_rB_")
	override val inputs = Seq((Chan("RequestDelegation_0__a_Patient"),"cRequestDelegation_Patient_1"),(Chan("RequestDelegation_1__a_HealthcareActor"),"cRequestDelegation_HealthcareActor_2"),(Chan("RequestDelegation_2__a_HealthcareService"),"cRequestDelegation_HealthcareService_3"))
	override val channels = Seq("cRequestDelegation_Patient_1","cRequestDelegation_HealthcareActor_2","cRequestDelegation_HealthcareService_3","oRequestDelegation_lB_Delegation_x_lB_RequestedContract_x_lB_ServiceRequester_x_PendingHealthcareService_rB_rB_rB_")

	def run(args:Seq[PiObject])(implicit ec:ExecutionContext):Future[PiObject] = args match {
		case Seq(o1,o2,o3) => this(PiObject.getAs[Patient](o1),PiObject.getAs[HealthcareActor](o2),PiObject.getAs[HealthcareService](o3)) map PiObject.apply
	}
}
