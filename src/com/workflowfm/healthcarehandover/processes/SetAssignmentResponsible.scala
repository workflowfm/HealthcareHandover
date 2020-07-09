package com.workflowfm.healthcarehandover.processes

import scala.concurrent._
import com.workflowfm.pew._
import com.workflowfm.pew.execution._
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._

trait SetAssignmentResponsible extends ((Assignment, ServiceProvider) => Future[HealthcareActor]) with AtomicProcess {
	override val name = "SetAssignmentResponsible"
	override val output = (Chan("SetAssignmentResponsible_0__a_HealthcareActor"),"oSetAssignmentResponsible_HealthcareActor_")
	override val inputs = Seq((Chan("SetAssignmentResponsible_0__a_Assignment"),"cSetAssignmentResponsible_Assignment_1"),(Chan("SetAssignmentResponsible_1__a_ServiceProvider"),"cSetAssignmentResponsible_ServiceProvider_2"))
	override val channels = Seq("cSetAssignmentResponsible_Assignment_1","cSetAssignmentResponsible_ServiceProvider_2","oSetAssignmentResponsible_HealthcareActor_")

	def run(args:Seq[PiObject])(implicit ec:ExecutionContext):Future[PiObject] = args match {
		case Seq(o1,o2) => this(PiObject.getAs[Assignment](o1),PiObject.getAs[ServiceProvider](o2)) map PiObject.apply
	}
}
