package com.workflowfm.healthcarehandover.instances

import akka.actor.{ ActorRef, ActorSystem }
import com.workflowfm.pew.simulator.PiSimulatedProcess
import com.workflowfm.simulator.TaskGenerator
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._
import com.workflowfm.healthcarehandover.processes._
import scala.concurrent.{ ExecutionContext, Future }

class CheckOutcomeInstance(override val simulationName:String,  override val simulationActor: ActorRef, taskGenerator:TaskGenerator) (implicit system: ActorSystem, context: ExecutionContext) extends CheckOutcome with PiSimulatedProcess {
	override def apply( arg0 :OpenContract, arg1 :CompletedHealthcareService, arg2 :HealthcareActor ) :Future[(CheckedHealthcareService,ClosedContract)] = {
      simulate(taskGenerator, (_,_) => (CheckedHealthcareService(arg1.name),ClosedContract(arg0.requester,arg0.provider,arg0.patient)), arg2.name, arg0.patient.name )
	}
}
