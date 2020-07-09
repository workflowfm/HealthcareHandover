package com.workflowfm.healthcarehandover.instances

import akka.actor.{ ActorRef, ActorSystem }
import com.workflowfm.pew.simulator.PiSimulatedProcess
import com.workflowfm.simulator.TaskGenerator
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._
import com.workflowfm.healthcarehandover.processes._
import scala.concurrent.{ ExecutionContext, Future }

class ProvideServiceInstance (override val simulationName:String, override val simulationActor: ActorRef, taskGenerator:TaskGenerator)(chance :Int, max :Int = 100) (implicit system: ActorSystem, context: ExecutionContext) extends ProvideService with PiSimulatedProcess {
	override def apply( arg0 :OpenContract, arg1 :PendingHealthcareService ) :Future[Either[CompletedHealthcareService,(Obstacle,PendingHealthcareService)]] = { 
	  val random = new util.Random()
		val result = if (random.nextInt(max) < chance) {
		  //println(name + " (COMPLETED)")
		  Left(CompletedHealthcareService(arg1.name))
		} else {
		  //println(name + " (OBSTACLE)") 
		  Right(Obstacle("obstacle"),PendingHealthcareService(arg1.name))
		}
      simulate(taskGenerator, (_,_) => result, arg0.provider.name, arg0.patient.name)
	}
}
