package com.workflowfm.healthcarehandover.processes

import scala.concurrent._
import com.workflowfm.pew._
import com.workflowfm.pew.execution._
import com.workflowfm.healthcarehandover.HealthcareHandoverTypes._

trait Copy_ServiceProvider_2 extends ((ServiceProvider) => Future[(ServiceProvider,ServiceProvider)]) with AtomicProcess {
	override val name = "Copy_ServiceProvider_2"
	override val output = (PiPair(Chan("Copy_ServiceProvider_2_0_l_a_ServiceProvider"),Chan("Copy_ServiceProvider_2_0_r_a_ServiceProvider")),"oCopy_ServiceProvider_2_lB_ServiceProvider_x_ServiceProvider_rB_")
	override val inputs = Seq((Chan("Copy_ServiceProvider_2_0__a_ServiceProvider"),"cCopy_ServiceProvider_2_ServiceProvider_1"))
	override val channels = Seq("cCopy_ServiceProvider_2_ServiceProvider_1","oCopy_ServiceProvider_2_lB_ServiceProvider_x_ServiceProvider_rB_")

	def run(args:Seq[PiObject])(implicit ec:ExecutionContext):Future[PiObject] = args match {
		case Seq(o1) => this(PiObject.getAs[ServiceProvider](o1)) map PiObject.apply
	}
}
