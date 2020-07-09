package com.workflowfm.healthcarehandover.instances

import scala.concurrent._
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._
import com.workflowfm.healthcarehandover.processes._

class RequestDelegationInstance extends RequestDelegation {
	override def apply( arg0 :Patient, arg1 :HealthcareActor, arg2 :HealthcareService ) :Future[(Delegation,(RequestedContract,(ServiceRequester,PendingHealthcareService)))] = {
		Future.successful((Delegation(),
	      (RequestedContract(arg1,arg2,arg0),
	          (ServiceRequester(arg1),PendingHealthcareService(arg2.name)))))
	}
}
