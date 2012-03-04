/*
 * @(#)SyncProjectsIST.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain;

import myorg.domain.util.Money;



/**
 * 
 * @author Luis Cruz
 * 
 */
public class SyncProjectsIST extends SyncProjectsAux {

    private final static Money AUTHORIZED_VALUE = new Money("75000");

    @Override
    public String getVirtualHost() {
	return "dot.ist.utl.pt";
    }

    @Override
    protected String getDbPropertyPrefix() {
	return "db.mgp.ist";
    }

    @Override
    protected boolean isResponsabilityAllowed(final String responsibleString) {
	return projectResponsibles.contains(Integer.valueOf(responsibleString));
    }

    @Override
    protected Money getAuthorizationValue() {
	return AUTHORIZED_VALUE;
    }

    @Override
    protected Boolean isDefaultRegeimIsCCP(final String type) {
	return !type.equalsIgnoreCase("i");
    }

}
