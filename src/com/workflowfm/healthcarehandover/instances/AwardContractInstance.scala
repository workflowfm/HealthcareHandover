package com.workflowfm.healthcarehandover.instances

import akka.actor.{ ActorRef, ActorSystem }
import com.workflowfm.pew.simulator.PiSimulatedProcess
import com.workflowfm.simulator.{ Task, TaskGenerator }
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._
import com.workflowfm.healthcarehandover.processes._
import scala.concurrent.{ ExecutionContext, Future }

class AwardContractInstance(override val simulationName:String, override val simulationActor: ActorRef, taskGenerator:TaskGenerator) (implicit system: ActorSystem, context: ExecutionContext) extends AwardContract with PiSimulatedProcess {
	override def apply( arg0 :AcceptedContract, arg1 :ServiceProvider ) :Future[OpenContract] = {
      simulate(taskGenerator, (_,_) => OpenContract(arg0.requester,arg1.actor,arg0.patient), arg0.requester.name)
	}
}
