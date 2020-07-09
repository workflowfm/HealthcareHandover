package com.workflowfm.healthcarehandover.instances

import scala.concurrent._
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._
import com.workflowfm.healthcarehandover.processes._

class SetAssignmentResponsibleInstance extends SetAssignmentResponsible {
	override def apply( arg0 :Assignment, arg1 :ServiceProvider ) :Future[HealthcareActor] = {
		Future.successful(arg1.actor)
	}
	
	// TODO naming way
}
