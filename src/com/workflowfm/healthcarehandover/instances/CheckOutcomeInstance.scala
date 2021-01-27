package com.workflowfm.healthcarehandover.instances

import com.workflowfm.pew.simulator.{ PiSimulation, PiSimulatedProcess }
import com.workflowfm.proter.Task
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._
import com.workflowfm.healthcarehandover.processes._
import scala.concurrent.{ ExecutionContext, Future }

class CheckOutcomeInstance (override val simulation: PiSimulation, task: Task)(implicit context: ExecutionContext) extends CheckOutcome with PiSimulatedProcess {
	override def apply( arg0 :OpenContract, arg1 :CompletedHealthcareService, arg2 :HealthcareActor ) :Future[(CheckedHealthcareService,ClosedContract)] = {
      simulate(task withResources Seq(arg2.name, arg0.patient.name), (_,_) => (CheckedHealthcareService(arg1.name),ClosedContract(arg0.requester,arg0.provider,arg0.patient)))
      }
}
