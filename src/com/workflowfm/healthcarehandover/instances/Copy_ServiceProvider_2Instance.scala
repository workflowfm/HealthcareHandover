package com.workflowfm.healthcarehandover.instances

import scala.concurrent._
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._
import com.workflowfm.healthcarehandover.processes._

class Copy_ServiceProvider_2Instance extends Copy_ServiceProvider_2 {
	override def apply( arg0 :ServiceProvider ) :Future[(ServiceProvider,ServiceProvider)] = {
		Future.successful((arg0,arg0))
	}
}
