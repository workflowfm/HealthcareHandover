package com.workflowfm.healthcarehandover

import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._
import com.workflowfm.healthcarehandover.processes._
import com.workflowfm.healthcarehandover.instances._
import com.workflowfm.healthcarehandover._

import com.workflowfm.proter._
import com.workflowfm.proter.metrics._
import com.workflowfm.pew.simulator.PiSimulation

import java.io.File
import java.util.UUID
import java.lang.Runtime

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.util.Try

object SimulationsMain {
  
  def main(args: Array[String]): Unit = {
   	implicit val context: ExecutionContext = ExecutionContext.global

	val obstacleProbability = 0.3
	
	val diagnosis = new HealthcareService("Diagnosis")
	val haemodialysis = new HealthcareService("Haemodialysis")
	
	val petros = HealthcareActor("Petros",List(diagnosis))
	val orphen = HealthcareActor("Orphen",List(diagnosis,haemodialysis))
	val blah = HealthcareActor("Blah",List(diagnosis))
	val pat1 = Patient("Patient1")
	val pat2 = Patient("Patient2")
	val pat3 = Patient("Patient3")

    val patients = List(pat1, pat2, pat3)
	val actors = List (petros, orphen, blah)
	val resources  = List (petros, orphen, blah, pat1, pat2, pat3)
	//

    val coordinator = new Coordinator(new DefaultScheduler())

	val handler = SimMetricsOutputs(
	  new SimMetricsPrinter(),
	  new SimCSVFileOutput("output" + File.separator,"HealthcareHandoverStatefulSimulation"),
	  new SimD3Timeline("output" + File.separator,"HealthcareHandoverStatefulSimulation")
	)

    // Subscribe the metrics actor
    coordinator.subscribe(new SimMetricsHandler(handler))

    //coordinator.subscribe(new com.workflowfm.proter.events.PrintEventHandler)

	coordinator.addResources(resources)

	val copy_OpenContract_2 = new Copy_OpenContract_2Instance
   	val copy_ServiceProvider_2 = new Copy_ServiceProvider_2Instance
   	val requestAssignment = new RequestAssignmentInstance
   	val requestDelegation = new RequestDelegationInstance
   	val setAssignmentResponsible = new SetAssignmentResponsibleInstance
    val setDelegationResponsible = new SetDelegationResponsibleInstance
	
	class AssignmentSimulation(simulationName:String,p:Patient,a:HealthcareActor,s:HealthcareService)(implicit context: ExecutionContext) extends PiSimulation(simulationName, coordinator)(context) {

	  val checkOutcome = new CheckOutcomeInstance(this,Task("CheckOutcome", 2L) withCost(1))(context)
	  val awardContract = new AwardContractInstance(this,Task("AwardContract",1L).withCost(1))(context)
	  val decideCollaboration = new DecideCollaborationInstance(simulationName,actors,3)(context)
	  val provideService = new ProvideServiceInstance(this,Task("ProvideService",5L).withCost(1))((1-obstacleProbability)*100 toInt)(context)
 	  val assignHealthcareService = new AssignHealthcareService(awardContract , checkOutcome , copy_OpenContract_2 , copy_ServiceProvider_2 , decideCollaboration , provideService , requestAssignment , setAssignmentResponsible)
   	  
   	  override val rootProcess = assignHealthcareService
      override val args = Seq( a, s, p )
    }
  

	class DelegationSimulation(simulationName:String,p:Patient,a:HealthcareActor,s:HealthcareService)(implicit context: ExecutionContext) extends PiSimulation(simulationName, coordinator)(context) {
	  
	  val checkOutcome = new CheckOutcomeInstance(this,Task("CheckOutcome",2L).withCost(1))(context)
	  val awardContract = new AwardContractInstance(this,Task("AwardContract",1L).withCost(1))(context)
	  val decideCollaboration = new DecideCollaborationInstance(simulationName,actors,3)(context)
	  val provideService = new ProvideServiceInstance(this,Task("ProvideService",5L).withCost(1))((1-obstacleProbability)*100 toInt)(context)
   	  val delegateHealthcareService = new DelegateHealthcareService(awardContract , checkOutcome , copy_OpenContract_2 , decideCollaboration , provideService , requestDelegation , setDelegationResponsible)
	  
   	  override val rootProcess = delegateHealthcareService
      override val args = Seq( a, s, p )
	}
	
	//val superevent = new TaskSimulation("TaskSim", coordinator, Seq("Petros","Orphen"), new ConstantGenerator(5), new ConstantGenerator(5), -1, Task.Highest)
	/*
	val s1 = system.actorOf(Props(new AssignmentSimulation("A1",pat1,petros,diagnosis)))
	val s2 = system.actorOf(Props(new DelegationSimulation("D2",pat1,petros,haemodialysis)))
	val s3 = system.actorOf(Props(new AssignmentSimulation("A3",pat1,petros,haemodialysis)))
	val s4 = system.actorOf(Props(new DelegationSimulation("D4",pat2,orphen,diagnosis)))
	val s5 = system.actorOf(Props(new AssignmentSimulation("A5",pat3,blah,diagnosis)))
	val s6 = system.actorOf(Props(new DelegationSimulation("D6",pat3,blah,haemodialysis)))
	val s7 = system.actorOf(Props(new AssignmentSimulation("A7",pat1,petros,haemodialysis)))
	val s8 = system.actorOf(Props(new DelegationSimulation("D8",pat1,petros,diagnosis)))

	coordinator ! Coordinator.AddSim(1,s2)
	coordinator ! Coordinator.AddSim(10,s1)
//	coordinator ! Coordinator.AddSim(7,superevent,executor)
	coordinator ! Coordinator.AddSim(12,s3)
	coordinator ! Coordinator.AddSim(13,s4)
	coordinator ! Coordinator.AddSim(10,s5)
	coordinator ! Coordinator.AddSim(10,s6)
	coordinator ! Coordinator.AddSim(23,s7)
	coordinator ! Coordinator.AddSim(23,s8)
	 */
    val r = scala.util.Random

    def rand[T](s: Seq[T]): T = s(r.nextInt(s.size))

    def tryToInt( s: String ) = Try(s.toInt).toOption
    val n = if (args.size > 0) {
     tryToInt(args(0)).getOrElse(5)
    } else 5
    val sims =
      (for (i <- 1 to n) yield new AssignmentSimulation(s"A$i",rand(patients),rand(actors),diagnosis)) ++
      (for (i <- 1 to n) yield new DelegationSimulation(s"D$i",rand(patients),rand(actors),diagnosis))

//    sims.map(_.subscribe(new com.workflowfm.pew.stream.PrintEventHandler))

    coordinator.addSimulationsNow(sims)
	Await.result(coordinator.start(), 1.hour)
  }
}
