package com.workflowfm.healthcarehandover.processes

import scala.concurrent._
import com.workflowfm.pew._
import com.workflowfm.pew.execution._
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._

trait SetDelegationResponsible extends ((Delegation, ServiceRequester) => Future[HealthcareActor]) with AtomicProcess {
	override val name = "SetDelegationResponsible"
	override val output = (Chan("SetDelegationResponsible_0__a_HealthcareActor"),"oSetDelegationResponsible_HealthcareActor_")
	override val inputs = Seq((Chan("SetDelegationResponsible_0__a_Delegation"),"cSetDelegationResponsible_Delegation_1"),(Chan("SetDelegationResponsible_1__a_ServiceRequester"),"cSetDelegationResponsible_ServiceRequester_2"))
	override val channels = Seq("cSetDelegationResponsible_Delegation_1","cSetDelegationResponsible_ServiceRequester_2","oSetDelegationResponsible_HealthcareActor_")

	def run(args:Seq[PiObject])(implicit ec:ExecutionContext):Future[PiObject] = args match {
		case Seq(o1,o2) => this(PiObject.getAs[Delegation](o1),PiObject.getAs[ServiceRequester](o2)) map PiObject.apply
	}
}
