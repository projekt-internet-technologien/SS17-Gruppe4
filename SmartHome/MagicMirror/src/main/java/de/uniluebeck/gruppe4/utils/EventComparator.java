package de.uniluebeck.gruppe4.utils;

import java.util.Comparator;
import java.util.Date;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

public class EventComparator implements Comparator<Event>{

	@Override
	public int compare(Event o1, Event o2) {
		if(o1 == null || o2 == null){
			if(o2 == null && o2 == null){
				return 0;
			}
			if(o1 == null){
				return -1;
			}
			return 1;
		}
		
		DateTime start1 = o1.getStart().getDate();
		DateTime start2 = o2.getStart().getDate();
		
		if(start1 == null || start2 == null){
			if(start1 == null && start2 == null){
				return 0;
			}
			if(start1 == null){
				return -1;
			}
			return 1;
		}
		
		Date d1 = new Date(start1.getValue());
		Date d2 = new Date(start2.getValue());
		
		return d1.compareTo(d2);
	}

}
