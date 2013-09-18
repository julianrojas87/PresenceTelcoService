package org.telcomp.sbb;

import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import org.telcomp.events.EndGetDataTelcoServiceEvent;
import org.telcomp.events.EndPresenceTelcoServiceEvent;
import org.telcomp.events.EndSearchDataTelcoServiceEvent;
import org.telcomp.events.StartGetDataTelcoServiceEvent;
import org.telcomp.events.StartPresenceTelcoServiceEvent;
import org.telcomp.events.StartSearchDataTelcoServiceEvent;

public abstract class PresenceTelcoServiceSbb implements javax.slee.Sbb {
	
	private NullActivityFactory nullActivityFactory;
	private NullActivityContextInterfaceFactory nullACIFactory;
	private String userId;

	public void onStartPresenceTelcoServiceEvent(StartPresenceTelcoServiceEvent event, ActivityContextInterface aci) {
		this.setExecutionACI(aci);
		System.out.println("*******************************************");
		System.out.println("PresenceTelcoService Invoked");
		System.out.println("Input Parameter = "+event.getParameter());
		System.out.println("Input Value = "+event.getValue());
		
		if(event.getParameter().equals("identification")){
			this.userId = event.getParameter();
			HashMap<String, Object> operationInputs = new HashMap<String, Object>();
			operationInputs.put("parameter", (String) "state");
			operationInputs.put("identification", (String) event.getValue());
			StartGetDataTelcoServiceEvent startDataAccess = new StartGetDataTelcoServiceEvent(operationInputs);
			ActivityContextInterface nullAci = this.createNullActivityACI();
			nullAci.attach(this.sbbContext.getSbbLocalObject());
			this.fireStartGetDataTelcoServiceEvent(startDataAccess, nullAci, null);
		} else{
			HashMap<String, Object> operationInputs = new HashMap<String, Object>();
			operationInputs.put("parameter", (String) event.getParameter());
			operationInputs.put("value", (String) event.getValue());
			StartSearchDataTelcoServiceEvent startDataAccess = new StartSearchDataTelcoServiceEvent(operationInputs);
			ActivityContextInterface nullAci = this.createNullActivityACI();
			nullAci.attach(this.sbbContext.getSbbLocalObject());
			this.fireStartSearchDataTelcoServiceEvent(startDataAccess, nullAci, null);
		}
	}
	
	public void onEndGetDataTelcoServiceEvent(EndGetDataTelcoServiceEvent event, ActivityContextInterface aci) {
		HashMap<String, Object> operationInputs = new HashMap<String, Object>();
		operationInputs.put("userID", (String) this.userId);
		operationInputs.put("state", (String) event.getValue());
		EndPresenceTelcoServiceEvent EndPresenceEvent = new EndPresenceTelcoServiceEvent(operationInputs);
		this.fireEndPresenceTelcoServiceEvent(EndPresenceEvent, this.getExecutionACI(), null);
		aci.detach(this.sbbContext.getSbbLocalObject());
		this.getExecutionACI().detach(this.sbbContext.getSbbLocalObject());
		System.out.println("Output UserID = "+this.userId);
		System.out.println("Output State = "+event.getValue());
		System.out.println("*******************************************");
	}
	
	public void onEndSearchDataTelcoServiceEvent(EndSearchDataTelcoServiceEvent event, ActivityContextInterface aci) {
		aci.detach(this.sbbContext.getSbbLocalObject());
		this.userId = event.getIdentification();
		HashMap<String, Object> operationInputs = new HashMap<String, Object>();
		operationInputs.put("parameter", (String) "state");
		operationInputs.put("identification", (String) event.getIdentification());
		StartGetDataTelcoServiceEvent startDataAccess = new StartGetDataTelcoServiceEvent(operationInputs);
		ActivityContextInterface nullAci = this.createNullActivityACI();
		nullAci.attach(this.sbbContext.getSbbLocalObject());
		this.fireStartGetDataTelcoServiceEvent(startDataAccess, nullAci, null);
	}
	
	private ActivityContextInterface createNullActivityACI() {
		NullActivity nullActivity = this.nullActivityFactory.createNullActivity();
		ActivityContextInterface nullActivityACI = null;
		nullActivityACI = this.nullACIFactory.getActivityContextInterface(nullActivity);
		return nullActivityACI;
	}


	public abstract void setExecutionACI(ActivityContextInterface aci);
	public abstract ActivityContextInterface getExecutionACI();
	
	
	// TODO: Perform further operations if required in these methods.
	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;
		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");
			nullActivityFactory = (NullActivityFactory) ctx.lookup("slee/nullactivity/factory");
			nullACIFactory = (NullActivityContextInterfaceFactory) ctx.lookup("slee/nullactivity/activitycontextinterfacefactory");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
    public void unsetSbbContext() { this.sbbContext = null; }
    
    // TODO: Implement the lifecycle methods if required
    public void sbbCreate() throws javax.slee.CreateException {}
    public void sbbPostCreate() throws javax.slee.CreateException {}
    public void sbbActivate() {}
    public void sbbPassivate() {}
    public void sbbRemove() {}
    public void sbbLoad() {}
    public void sbbStore() {}
    public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface activity) {}
    public void sbbRolledBack(RolledBackContext context) {}
	
	public abstract void fireEndPresenceTelcoServiceEvent (EndPresenceTelcoServiceEvent event, ActivityContextInterface aci, Address address);
	public abstract void fireStartGetDataTelcoServiceEvent (StartGetDataTelcoServiceEvent event, ActivityContextInterface aci, Address address);
	public abstract void fireStartSearchDataTelcoServiceEvent (StartSearchDataTelcoServiceEvent event, ActivityContextInterface aci, Address address);

	
	/**
	 * Convenience method to retrieve the SbbContext object stored in setSbbContext.
	 * 
	 * TODO: If your SBB doesn't require the SbbContext object you may remove this 
	 * method, the sbbContext variable and the variable assignment in setSbbContext().
	 *
	 * @return this SBB's SbbContext object
	 */
	
	protected SbbContext getSbbContext() {
		return sbbContext;
	}

	private SbbContext sbbContext; // This SBB's SbbContext

}
