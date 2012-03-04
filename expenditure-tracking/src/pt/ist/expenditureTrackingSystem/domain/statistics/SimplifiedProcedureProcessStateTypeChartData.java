/*
 * @(#)SimplifiedProcedureProcessStateTypeChartData.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.domain.statistics;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.util.Calculation.Operation;

/**
 * 
 * @author Luis Cruz
 * 
 */
public abstract class SimplifiedProcedureProcessStateTypeChartData extends PaymentProcessChartData<AcquisitionProcessStateType> {

    public SimplifiedProcedureProcessStateTypeChartData(final PaymentProcessYear paymentProcessYear, final Operation operation) {
	super(paymentProcessYear, operation);
    }

    @Override
    protected AcquisitionProcessStateType[] getCategories() {
	return AcquisitionProcessStateType.values();
    };

    @Override
    protected String getLabel(final AcquisitionProcessStateType acquisitionProcessStateType) {
	return acquisitionProcessStateType.getLocalizedName();
    }

}
