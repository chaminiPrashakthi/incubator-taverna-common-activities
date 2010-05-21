/*******************************************************************************
 * Copyright (C) 2007 The University of Manchester   
 * 
 *  Modifications to the initial code base are copyright of their
 *  respective authors, or their employers as appropriate.
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *    
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *    
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 ******************************************************************************/
package net.sf.taverna.t2.activities.beanshell;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import net.sf.taverna.t2.workflowmodel.Processor;
import net.sf.taverna.t2.workflowmodel.health.HealthCheck;
import net.sf.taverna.t2.workflowmodel.health.HealthChecker;
import net.sf.taverna.t2.visit.VisitReport;
import net.sf.taverna.t2.visit.VisitReport.Status;
import bsh.ParseException;
import bsh.Parser;

public class BeanshellActivityHealthChecker implements HealthChecker<BeanshellActivity> {

	public boolean canVisit(Object subject) {
		return (subject!=null && subject instanceof BeanshellActivity);
	}

	public VisitReport visit(BeanshellActivity activity, List<Object> ancestors) {
		Processor p = (Processor) VisitReport.findAncestor(ancestors, Processor.class);
		if (p == null) {
			return null;
		}
		Parser parser = new Parser(new StringReader(activity.getConfiguration().getScript()));
		try {
			while (!parser.Line());
		} catch (ParseException e) {
			return new VisitReport(HealthCheck.getInstance(), p ,e.getMessage(), HealthCheck.INVALID_SCRIPT, Status.SEVERE);
		}
		return new VisitReport(HealthCheck.getInstance(), p, "Beanshell service script parsed OK",HealthCheck.NO_PROBLEM, Status.OK);
	}

	public boolean isTimeConsuming() {
		return false;
	}


}
