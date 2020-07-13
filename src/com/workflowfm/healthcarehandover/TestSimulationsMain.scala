package com.workflowfm.healthcarehandover

import akka.actor.ActorSystem
import akka.util.Timeout
import akka.pattern.ask
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._
import com.workflowfm.healthcarehandover.processes._
import com.workflowfm.healthcarehandover.instances._
import com.workflowfm.healthcarehandover._
import akka.actor.Props
import com.workflowfm.pew.simulator.PiSimulationActor
import com.workflowfm.simulator._
import com.workflowfm.simulator.events.{ ShutdownHandler }
import com.workflowfm.simulator.metrics._
import java.io.File
import java.util.UUID
import java.lang.Runtime
import com.workflowfm.pew.execution.AkkaExecutor
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.util.Try
import uk.ac.ed.inf.ppapapan.subakka.Subscriber

object TestSimulationsMain {
  
  def main(args: Array[String]): Unit = {
	implicit val system = ActorSystem("HealthcareHandoverStatefulSimulation")
   	implicit val context: ExecutionContext = ExecutionContext.global

	//println(system.settings)
    println(s"Cores: ${Runtime.getRuntime().availableProcessors()}")
    val config = system.settings.config.getConfig("akka.actor.default-dispatcher")
    println(s"Parallelism: ${config.getInt("fork-join-executor.parallelism-min")}-${config.getInt("fork-join-executor.parallelism-max")} x ${config.getDouble("fork-join-executor.parallelism-factor")}")

	val obstacleProbability = 0.3
	
	val diagnosis = new HealthcareService("Diagnosis")
	val haemodialysis = new HealthcareService("Haemodialysis")
	
	val petros = HealthcareActor("Petros",List(diagnosis))
	val orphen = HealthcareActor("Orphen",List(diagnosis,haemodialysis))
	val blah = HealthcareActor("Blah",List(diagnosis))
	val pat1 = Patient("Patient1")
	val pat2 = Patient("Patient2")
	val pat3 = Patient("Patient3")
    val pat4 = Patient("Patient4")

    val patients = List(pat1, pat2, pat3, pat4)
	val actors = List (petros, orphen, blah)
	val resources  = List (petros, orphen, blah, pat1, pat2, pat3, pat4)
	//

    val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))
    val shutdownActor = Subscriber.actor(new ShutdownHandler())

    implicit val timeout = Timeout(2.seconds)

	val handler = SimMetricsOutputs(
	  new SimMetricsPrinter(),
	  new SimCSVFileOutput("output" + File.separator,"TestHealthcareHandoverStatefulSimulation"),
	  new SimD3Timeline("output" + File.separator,"TestHealthcareHandoverStatefulSimulation")
	)

    // Subscribe the metrics actor
    Await.result(new SimOutputHandler(handler).subAndForgetTo(coordinator,Some("MetricsHandler")), 3.seconds)

    // Subscribe the shutdown actor
    Await.result(shutdownActor ? Subscriber.SubAndForgetTo(coordinator), 3.seconds)

	coordinator ! Coordinator.AddResources(resources)

	val copy_OpenContract_2 = new Copy_OpenContract_2Instance
   	val copy_ServiceProvider_2 = new Copy_ServiceProvider_2Instance
   	val requestAssignment = new RequestAssignmentInstance
   	val requestDelegation = new RequestDelegationInstance
   	val setAssignmentResponsible = new SetAssignmentResponsibleInstance
    val setDelegationResponsible = new SetDelegationResponsibleInstance
	
	class AssignmentSimulation(simulationName:String,p:Patient,a:HealthcareActor,s:HealthcareService)(implicit val executionContext: ExecutionContext) extends PiSimulationActor[UUID](simulationName, coordinator)(executionContext) {

	  val checkOutcome = new CheckOutcomeInstance(simulationName,self,TaskGenerator("CheckOutcome",name,new ConstantGenerator(2L),new ConstantGenerator(1)))
	  val awardContract = new AwardContractInstance(simulationName,self,TaskGenerator("AwardContract",name,new ConstantGenerator(1L),new ConstantGenerator(1)))
	  val decideCollaboration = new DecideCollaborationInstance(simulationName,actors,3)
	  val provideService = new ProvideServiceInstance(simulationName,self,TaskGenerator("ProvideService",name,new ConstantGenerator(5L),new ConstantGenerator(1)))((1-obstacleProbability)*100 toInt)
 	  val assignHealthcareService = new AssignHealthcareService(awardContract , checkOutcome , copy_OpenContract_2 , copy_ServiceProvider_2 , decideCollaboration , provideService , requestAssignment , setAssignmentResponsible)
   	  
   	  override val rootProcess = assignHealthcareService
      override val args = Seq( a, s, p )
      override val executor  = new AkkaExecutor()(context.system)
    }
  

	class DelegationSimulation(simulationName:String,p:Patient,a:HealthcareActor,s:HealthcareService)(implicit val executionContext: ExecutionContext) extends PiSimulationActor[UUID](simulationName, coordinator)(executionContext) {
	  
	  val checkOutcome = new CheckOutcomeInstance(simulationName,self,TaskGenerator("CheckOutcome",name,new ConstantGenerator(2L),new ConstantGenerator(1)))
	  val awardContract = new AwardContractInstance(simulationName,self,TaskGenerator("AwardContract",name,new ConstantGenerator(1L),new ConstantGenerator(1)))
	  val decideCollaboration = new DecideCollaborationInstance(simulationName,actors,3)
	  val provideService = new ProvideServiceInstance(simulationName,self,TaskGenerator("ProvideService",name,new ConstantGenerator(5L),new ConstantGenerator(1)))((1-obstacleProbability)*100 toInt)
   	  val delegateHealthcareService = new DelegateHealthcareService(awardContract , checkOutcome , copy_OpenContract_2 , decideCollaboration , provideService , requestDelegation , setDelegationResponsible)
	  
   	  override val rootProcess = delegateHealthcareService
      override val args = Seq( a, s, p )
      override val executor  = new AkkaExecutor()(context.system)
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
      (for (i <- 1 to n) yield system.actorOf(Props(new AssignmentSimulation(s"A$i",rand(patients),rand(actors),diagnosis)),s"A$i")) ++
      (for (i <- 1 to n) yield system.actorOf(Props(new DelegationSimulation(s"D$i",rand(patients),rand(actors),diagnosis)),s"D$i"))
      
    coordinator ! Coordinator.AddSim(5L,(system.actorOf(Props(new AssignmentSimulation("T1",rand(patients),rand(actors),diagnosis)),"T1")))

    sims map { coordinator ! Coordinator.AddSim(0L,_) }
	coordinator ! Coordinator.Start
  }
}
