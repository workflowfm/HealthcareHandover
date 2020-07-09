package com.workflowfm.healthcarehandover.instances

import scala.concurrent._
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._
import com.workflowfm.healthcarehandover.processes._

class Copy_OpenContract_2Instance extends Copy_OpenContract_2 {
	override def apply( arg0 :OpenContract ) :Future[(OpenContract,OpenContract)] = {
		Future.successful((arg0,arg0))
	}
}
