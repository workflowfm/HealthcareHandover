package com.workflowfm.healthcarehandover.instances

import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._
import com.workflowfm.healthcarehandover.processes._
import scala.concurrent.{ ExecutionContext, Future }

// TODO this should use a future with an actor to determine the queues of all the actors so we can prioritize by queue size and check if the queue is full

class DecideCollaborationInstance(simulationName:String, actors:Seq[HealthcareActor], maxQueue :Int)(implicit context: ExecutionContext) extends DecideCollaboration {
	override def apply( arg0 :RequestedContract ) :Future[Either[(AcceptedContract,ServiceProvider),RejectedContract]] = Future {
	  val acts = actors.toList filter (!_.name.equals(arg0.requester.name)) filter (_.can(arg0.service)) // sortWith (_.queue.size < _.queue.size) 
		if (acts.isEmpty) {
		  println("*** Simulation " + simulationName + ": DecideCollaboration found no capable actors.")
		  Right(RejectedContract(arg0.requester,arg0.patient))
		} else {
		  val random = new util.Random()
		  val actr = acts(random.nextInt(acts.size))
		  println("*** Simulation " + simulationName + ": DecideCollaboration randomly picked \"" + actr.name + "\" among: " + (acts map (_.name)))
//		  if (actr.queue.size >= maxQueue)
//		    Right(RejectedContract(arg0.requester))
//		  else 
		    Left((AcceptedContract(arg0.requester,actr,arg0.patient),ServiceProvider(actr)))
		}
	}
}
