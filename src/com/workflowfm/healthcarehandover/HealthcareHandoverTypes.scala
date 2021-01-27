package com.workflowfm.healthcarehandover

import com.workflowfm.proter.TaskResource

package object HealthcareHandoverTypes
{  

  class HealthcareService(val name:String) {
    override def toString = "Service:" + name
  }
	case class PendingHealthcareService(override val name:String) extends HealthcareService(name)
	case class CompletedHealthcareService(override val name:String) extends HealthcareService(name)
	case class CheckedHealthcareService(override val name:String) extends HealthcareService(name)
  
	case class HealthcareActor(override val name:String,val capabilities:Seq[HealthcareService],override val costPerTick:Int=1) extends TaskResource(name,costPerTick) {
    override def toString = "Actor:" + name
    def can(service :HealthcareService) :Boolean = capabilities contains service
  }
	case class ServiceProvider(actor :HealthcareActor)
	case class ServiceRequester(actor :HealthcareActor)
  
  sealed trait Contract
	case class RequestedContract(requester:HealthcareActor,service:HealthcareService,patient:Patient) extends Contract
	case class OpenContract(requester:HealthcareActor,provider:HealthcareActor,patient:Patient) extends Contract
	case class ClosedContract(requester:HealthcareActor,provider:HealthcareActor,patient:Patient) extends Contract
	
	case class AcceptedContract(requester:HealthcareActor,provider:HealthcareActor,patient:Patient) extends Contract
	case class RejectedContract(requester:HealthcareActor,patient:Patient) extends Contract


	case class Patient(override val name:String) extends TaskResource(name,0)
	
	case class Obstacle(name:String) 
	
	case class Assignment()
	case class Delegation()
}
