package com.workflowfm.healthcarehandover.instances

import com.workflowfm.pew.simulator.{ PiSimulation, PiSimulatedProcess }
import com.workflowfm.proter.Task
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._
import com.workflowfm.healthcarehandover.processes._
import scala.concurrent.{ ExecutionContext, Future }

class AwardContractInstance (override val simulation: PiSimulation, task: Task)(implicit context: ExecutionContext) extends AwardContract with PiSimulatedProcess {
	override def apply( arg0 :AcceptedContract, arg1 :ServiceProvider ) :Future[OpenContract] = {
      simulate(task withResources Seq(arg0.requester.name), (_,_) => OpenContract(arg0.requester,arg1.actor,arg0.patient))
	}
}
