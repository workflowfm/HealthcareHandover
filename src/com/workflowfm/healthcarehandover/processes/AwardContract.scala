package com.workflowfm.healthcarehandover.processes

import scala.concurrent._
import com.workflowfm.pew._
import com.workflowfm.pew.execution._
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._

trait AwardContract extends ((AcceptedContract, ServiceProvider) => Future[OpenContract]) with AtomicProcess {
	override val name = "AwardContract"
	override val output = (Chan("AwardContract_0__a_OpenContract"),"oAwardContract_OpenContract_")
	override val inputs = Seq((Chan("AwardContract_0__a_AcceptedContract"),"cAwardContract_AcceptedContract_1"),(Chan("AwardContract_1__a_ServiceProvider"),"cAwardContract_ServiceProvider_2"))
	override val channels = Seq("cAwardContract_AcceptedContract_1","cAwardContract_ServiceProvider_2","oAwardContract_OpenContract_")

	def run(args:Seq[PiObject])(implicit ec:ExecutionContext):Future[PiObject] = args match {
		case Seq(o1,o2) => this(PiObject.getAs[AcceptedContract](o1),PiObject.getAs[ServiceProvider](o2)) map PiObject.apply
	}
}
