package com.demo.bean;

import java.util.List;

public class EventLogWithFilenames extends EventLog {
	
	public List<String> fileNames;
	
	public static EventLogWithFilenames construct(EventLog e){
		EventLogWithFilenames evtWithFN = new EventLogWithFilenames();
		evtWithFN.actionType = e.actionType;
		evtWithFN.dateOfEvent = e.dateOfEvent;
		evtWithFN.dateOfLogging = e.dateOfLogging;
		evtWithFN.documentUrl = e.documentUrl;
		evtWithFN.id = e.id;
		evtWithFN.levelOfEmergency = e.levelOfEmergency;
		evtWithFN.log = e.log;
		evtWithFN.logger = e.logger;
		evtWithFN.referralId = e.referralId;
		evtWithFN.type = e.type;
		evtWithFN.valid = e.valid;
		return evtWithFN;
	}

}
