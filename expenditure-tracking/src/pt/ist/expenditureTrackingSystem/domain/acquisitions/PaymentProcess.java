package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Project;
import pt.ist.expenditureTrackingSystem.domain.organization.SubProject;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericLog;
import myorg.domain.util.Money;
import pt.ist.fenixWebFramework.security.UserView;

public abstract class PaymentProcess extends PaymentProcess_Base {

    public PaymentProcess() {
	super();
	final PaymentProcessYear acquisitionProcessYear = getPaymentProcessYearForConstruction();
	setPaymentProcessYear(acquisitionProcessYear);
	setAcquisitionProcessNumber(acquisitionProcessYear.nextAcquisitionProcessYearNumber());
    }

    private PaymentProcessYear getPaymentProcessYearForConstruction() {
	return PaymentProcessYear.getPaymentProcessYearByYear(getYearForConstruction());
    }

    protected int getYearForConstruction() {
	return new LocalDate().getYear();
    }

    public abstract <T extends RequestWithPayment> T getRequest();

    public List<Unit> getPayingUnits() {
	List<Unit> res = new ArrayList<Unit>();
	for (Financer financer : getRequest().getFinancers()) {
	    res.add(financer.getUnit());
	}
	return res;
    }

    public boolean isRealValueEqualOrLessThanFundAllocation() {
	Money allocatedMoney = this.getRequest().getTotalValue();
	Money realMoney = this.getRequest().getRealTotalValue();
	return realMoney.isLessThanOrEqual(allocatedMoney);
    }

    public boolean isResponsibleForAtLeastOnePayingUnit(Person person) {
	for (Unit unit : getPayingUnits()) {
	    if (unit.isResponsible(person)) {
		return true;
	    }
	}
	return false;
    }

    public Person getRequestor() {
	return getRequest().getRequester();
    }

    public boolean isResponsibleForUnit(Person person) {
	Set<Authorization> validAuthorizations = person.getValidAuthorizations();
	for (Unit unit : getPayingUnits()) {
	    for (Authorization authorization : validAuthorizations) {
		if (unit.isSubUnit(authorization.getUnit())) {
		    return true;
		}
	    }
	}

	return false;
    }

    public boolean isResponsibleForUnit() {
	User user = UserView.getUser();
	if (user == null) {
	    return false;
	}
	return isResponsibleForUnit(user.getPerson());
    }

    public boolean isProjectAccountingEmployee(final Person person) {
	return getRequest().isProjectAccountingEmployee(person);
    }

    public boolean isProjectAccountingEmployee() {
	final User user = UserView.getUser();
	return user != null && isProjectAccountingEmployee(user.getPerson());
    }

    public boolean hasAllocatedFundsForAllProjectFinancers(final Person person) {
	return getRequest().hasAllocatedFundsForAllProjectFinancers(person);
    }

    public boolean hasAnyFundAllocationId() {
	return getRequest().hasAnyFundAllocationId();
    }

    public boolean hasAnyNonProjectFundAllocationId() {
	return getRequest().hasAnyNonProjectFundAllocationId();
    }

    public boolean isAccountingEmployee(final Person person) {
	return getRequest().isAccountingEmployee(person);
    }

    public boolean isAccountingEmployee() {
	final User user = UserView.getUser();
	return user != null && isAccountingEmployee(user.getPerson());
    }

    public boolean isAccountingEmployeeForOnePossibleUnit() {
	final User user = UserView.getUser();
	return user != null && isAccountingEmployeeForOnePossibleUnit(user.getPerson());
    }

    public boolean isAccountingEmployeeForOnePossibleUnit(final Person person) {
	return getRequest().isAccountingEmployeeForOnePossibleUnit(person);
    }

    public boolean hasAllocatedFundsForAllProjectFinancers() {
	return getRequest().hasAllocatedFundsForAllProjectFinancers();
    }

    public boolean hasAnyAllocatedFunds() {
	return getRequest().hasAnyAllocatedFunds();
    }

    public boolean hasAllFundAllocationId(Person person) {
	return getRequest().hasAllFundAllocationId(person);
    }

    public Set<Financer> getFinancersWithFundsAllocated() {
	return getRequest().getFinancersWithFundsAllocated();
    }

    public Set<Financer> getFinancersWithFundsAllocated(Person person) {
	return getRequest().getFinancersWithFundsAllocated(person);
    }

    public Set<ProjectFinancer> getProjectFinancersWithFundsAllocated() {
	return getRequest().getProjectFinancersWithFundsAllocated();
    }

    public Set<ProjectFinancer> getProjectFinancersWithFundsAllocated(final Person person) {
	return getRequest().getProjectFinancersWithFundsAllocated(person);
    }

    public abstract boolean isInGenesis();

    public abstract boolean isPendingApproval();

    public abstract boolean isInApprovedState();

    public abstract boolean isPendingFundAllocation();

    public abstract void allocateFundsToUnit();

    public abstract void submitForApproval();

    public abstract boolean isInAllocatedToUnitState();

    public abstract boolean isInAuthorizedState();

    protected abstract void authorize();

    public boolean isResponsibleForUnit(final Person person, final Money amount) {
	Set<Authorization> validAuthorizations = person.getValidAuthorizations();
	for (Unit unit : getPayingUnits()) {
	    for (Authorization authorization : validAuthorizations) {
		if (authorization.getMaxAmount().isGreaterThanOrEqual(amount) && unit.isSubUnit(authorization.getUnit())) {
		    return true;
		}
	    }
	}
	return false;
    }

    public void authorizeBy(final Person person) {
	getRequest().authorizeBy(person);
	if (getRequest().isAuthorizedByAllResponsibles()) {
	    authorize();
	}
    }

    public boolean hasProjectsAsPayingUnits() {
	for (Unit unit : getPayingUnits()) {
	    if (unit instanceof Project || unit instanceof SubProject) {
		return true;
	    }
	}
	return false;
    }

    public boolean isRealValueFullyAttributedToUnits() {
	return getRequest().isRealValueFullyAttributedToUnits();
    }

    public boolean isSimplifiedProcedureProcess() {
	return false;
    }

    public boolean isInvoiceConfirmed() {
	return false;
    }

    public boolean hasAllocatedFundsPermanentlyForAllProjectFinancers() {
	final RequestWithPayment requestWithPayment = getRequest();
	return requestWithPayment.hasAllocatedFundsPermanentlyForAllProjectFinancers();
    }

    public void allocateFundsPermanently() {
    }

    public boolean isAllocatedPermanently() {
	return false;
    }

    public void resetEffectiveFundAllocationId() {
    }

    public <T extends GenericLog> List<T> getExecutionLogsForState(String stateName) {
	return (List<T>) getExecutionLogs();
    }

    public boolean isPayed() {
	return false;
    }

    public boolean isAuthorized() {
	return isInAuthorizedState();
    }

    public boolean isRefundProcess() {
	return false;
    }

    public abstract String getAcquisitionProcessId();

    public abstract boolean isAvailableForCurrentUser();

}
