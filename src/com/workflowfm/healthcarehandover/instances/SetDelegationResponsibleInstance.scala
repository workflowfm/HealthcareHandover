package com.workflowfm.healthcarehandover.instances

import scala.concurrent._
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._
import com.workflowfm.healthcarehandover.processes._

class SetDelegationResponsibleInstance extends SetDelegationResponsible {
	override def apply( arg0 :Delegation, arg1 :ServiceRequester ) :Future[HealthcareActor] = {
		Future.successful(arg1.actor)
	}
	
	//TODO naming way!
}
