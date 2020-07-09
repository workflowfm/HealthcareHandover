package com.workflowfm.healthcarehandover.instances

import scala.concurrent._
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._
import com.workflowfm.healthcarehandover.processes._

class RequestAssignmentInstance extends RequestAssignment {
	override def apply( arg0 :Patient, arg1 :HealthcareActor, arg2 :HealthcareService ) :Future[(Assignment,(RequestedContract,(ServiceRequester,PendingHealthcareService)))] = {
		Future.successful((Assignment(),
	      (RequestedContract(arg1,arg2,arg0),
	          (ServiceRequester(arg1),PendingHealthcareService(arg2.name)))))
	}
}
