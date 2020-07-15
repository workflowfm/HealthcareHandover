package com.workflowfm.healthcarehandover

import akka.actor.{ActorSystem, ActorRef }
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
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.util.Try
import scala.util.{Failure, Success}
import uk.ac.ed.inf.ppapapan.subakka.Subscriber

object TestNoPEW {
  
  def main(args: Array[String]): Unit = {
	implicit val system = ActorSystem("HealthcareHandoverStatefulSimulation")
   	implicit val context: ExecutionContext = ExecutionContext.global

	//println(system.settings)
    println(s"Cores: ${Runtime.getRuntime().availableProcessors()}")
    val config = system.settings.config.getConfig("akka.actor.default-dispatcher")
    println(s"Parallelism: ${config.getInt("fork-join-executor.parallelism-min")}-${config.getInt("fork-join-executor.parallelism-max")} x ${config.getDouble("fork-join-executor.parallelism-factor")}")

	val obstacleProbability = 0.3
	
	//val diagnosis = new HealthcareService("Diagnosis")
	//val haemodialysis = new HealthcareService("Haemodialysis")
	
	
    val r1 = new TaskResource("r1",0)
    val r2 = new TaskResource("r2",0)
    val resources = List (r1,r2)


    val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))
    val shutdownActor = Subscriber.actor(new ShutdownHandler())

    implicit val timeout = Timeout(2.seconds)

	val handler = SimMetricsOutputs(
	  new SimMetricsPrinter(),
	  new SimCSVFileOutput("output" + File.separator,"TestNoPEW"),
	  new SimD3Timeline("output" + File.separator,"TestNoPEW")
	)
    // Subscribe the metrics actor
    Await.result(new SimOutputHandler(handler).subAndForgetTo(coordinator,Some("MetricsHandler")), 3.seconds)
    // Subscribe the shutdown actor
    Await.result(shutdownActor ? Subscriber.SubAndForgetTo(coordinator), 3.seconds)

	coordinator ! Coordinator.AddResources(resources)

  val generator = new ConstantGenerator[Long](3L)

  class TestSimulationActor(
    name: String,
    coordinator: ActorRef
  ) (implicit context: ExecutionContext)
  extends SimulationActor(name,coordinator) {
    override def run() = {
      val g1 = TaskGenerator("tX1","simX",generator,ConstantGenerator(0L))
      val g2 = TaskGenerator("tX2","simX",generator,ConstantGenerator(0L))
      val g3 = TaskGenerator("tX3","simX",generator,ConstantGenerator(0L))
      val r = Seq("r1","r2")
      val task1 = task(g1, r:_*)
      val task2 = task(g2, "r1")
      val future1 = task1
      val future2 = task2
      ready()

      //val future3 = Future.sequence(Seq(future1,future2)).flatMap { x =>
      //  val task3 = task(g3, "r1")
      //  ready()
      //  task3
      //}
      Future.sequence(Seq(future1,future2))
    }
  }
      
  //coordinator ! Coordinator.AddSim(0L,system.actorOf((TaskSimulatorActor.props("sim1",coordinator,Seq("r1","r2"),generator)),"sim1"))
  //coordinator ! Coordinator.AddSim(0L,system.actorOf(Props(new TaskSimulatorActor("sim2",coordinator,Seq("r1"),generator)),"sim2"))
  //coordinator ! Coordinator.AddSim(0L,system.actorOf(Props(new TaskSimulatorActor("sim3",coordinator,Seq("r2"),generator)),"sim3"))
  coordinator ! Coordinator.AddSim(0L,system.actorOf(Props(new TestSimulationActor("simX",coordinator)),"simX"))

  //sims map { coordinator ! Coordinator.AddSim(0L,_) }
	coordinator ! Coordinator.Start
  }
}
