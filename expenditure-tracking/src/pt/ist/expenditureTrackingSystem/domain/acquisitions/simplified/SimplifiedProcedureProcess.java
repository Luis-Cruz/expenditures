package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.AddAcquisitionProposalDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.AddPayingUnit;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.AllocateFundsPermanently;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.AllocateProjectFundsPermanently;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ApproveAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.AssignPayingUnitToItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CancelAcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ChangeAcquisitionProposalDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ChangeFinancersAccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ChangeRefundee;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ConfirmInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CreateAcquisitionPurchaseOrderDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CreateAcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.DeleteAcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.DistributeRealValuesForPayingUnits;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.EditAcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.EditAcquisitionRequestItemRealValues;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.FixInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.FundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.FundAllocationExpirationDate;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.PayAcquisition;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ProjectFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ReceiveInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RejectAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RemoveFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RemoveFundAllocationExpirationDate;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RemoveFundsPermanentlyAllocated;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RemovePayingUnit;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RemoveProjectFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RevertInvoiceSubmission;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SendPurchaseOrderToSupplier;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SetRefundee;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SetSkipSupplierFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SkipPurchaseOrderDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SubmitForApproval;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SubmitForConfirmInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SubmitForFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.UnApproveAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.UnSubmitForApproval;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.UnsetRefundee;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.UnsetSkipSupplierFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.services.Service;

public class SimplifiedProcedureProcess extends SimplifiedProcedureProcess_Base {

    private static Money PROCESS_VALUE_LIMIT = new Money("5000");

    private static Map<ActivityScope, List<GenericAcquisitionProcessActivity>> activities = new HashMap<ActivityScope, List<GenericAcquisitionProcessActivity>>();

    static {
	List<GenericAcquisitionProcessActivity> requestInformationActivities = new ArrayList<GenericAcquisitionProcessActivity>();
	List<GenericAcquisitionProcessActivity> requestItemActivities = new ArrayList<GenericAcquisitionProcessActivity>();

	requestInformationActivities.add(new CreateAcquisitionPurchaseOrderDocument());
	requestInformationActivities.add(new SendPurchaseOrderToSupplier());
	requestInformationActivities.add(new SkipPurchaseOrderDocument());

	requestInformationActivities.add(new AddAcquisitionProposalDocument());
	requestInformationActivities.add(new ChangeAcquisitionProposalDocument());
	requestInformationActivities.add(new CreateAcquisitionRequestItem());
	requestInformationActivities.add(new AddPayingUnit());
	requestInformationActivities.add(new RemovePayingUnit());
	// requestInformationActivities.add(new DeleteAcquisitionProcess());
	requestInformationActivities.add(new SubmitForApproval());

	requestInformationActivities.add(new SubmitForFundAllocation());
	requestInformationActivities.add(new FundAllocationExpirationDate());

	requestInformationActivities.add(new ApproveAcquisitionProcess());
	requestInformationActivities.add(new RejectAcquisitionProcess());

	requestInformationActivities.add(new AllocateProjectFundsPermanently());
	requestInformationActivities.add(new AllocateFundsPermanently());
	requestInformationActivities.add(new RemoveFundsPermanentlyAllocated());
	requestInformationActivities.add(new UnApproveAcquisitionProcess());

	requestInformationActivities.add(new ProjectFundAllocation());
	requestInformationActivities.add(new FundAllocation());
	requestInformationActivities.add(new RemoveFundAllocation());
	requestInformationActivities.add(new RemoveProjectFundAllocation());
	requestInformationActivities.add(new RemoveFundAllocationExpirationDate());
	requestInformationActivities.add(new CancelAcquisitionRequest());

	requestInformationActivities.add(new PayAcquisition());
	requestInformationActivities.add(new ReceiveInvoice());
	requestInformationActivities.add(new FixInvoice());
	requestInformationActivities.add(new SubmitForConfirmInvoice());
	requestInformationActivities.add(new ConfirmInvoice());
	requestInformationActivities.add(new UnSubmitForApproval());
	requestInformationActivities.add(new ChangeFinancersAccountingUnit());

	requestInformationActivities.add(new SetRefundee());
	requestInformationActivities.add(new ChangeRefundee());
	requestInformationActivities.add(new UnsetRefundee());

	requestInformationActivities.add(new SetSkipSupplierFundAllocation());
	requestInformationActivities.add(new UnsetSkipSupplierFundAllocation());

	requestInformationActivities.add(new RevertInvoiceSubmission());

	requestItemActivities.add(new DeleteAcquisitionRequestItem());
	requestItemActivities.add(new EditAcquisitionRequestItem());
	requestItemActivities.add(new AssignPayingUnitToItem());
	requestItemActivities.add(new EditAcquisitionRequestItemRealValues());
	requestItemActivities.add(new DistributeRealValuesForPayingUnits());

	activities.put(ActivityScope.REQUEST_INFORMATION, requestInformationActivities);
	activities.put(ActivityScope.REQUEST_ITEM, requestItemActivities);

    }

    protected SimplifiedProcedureProcess(final Person requester) {
	super();
	inGenesis();
	new AcquisitionRequest(this, requester);
    }

    protected SimplifiedProcedureProcess(Supplier supplier, Person person) {
	super();
	inGenesis();
	new AcquisitionRequest(this, supplier, person);
    }

    @Service
    public static SimplifiedProcedureProcess createNewAcquisitionProcess(
	    final CreateAcquisitionProcessBean createAcquisitionProcessBean) {
	if (!isCreateNewProcessAvailable()) {
	    throw new DomainException("acquisitionProcess.message.exception.invalidStateToRun.create");
	}
	SimplifiedProcedureProcess process = new SimplifiedProcedureProcess(createAcquisitionProcessBean.getSupplier(),
		createAcquisitionProcessBean.getRequester());
	process.getAcquisitionRequest().setRequestingUnit(createAcquisitionProcessBean.getRequestingUnit());
	if (createAcquisitionProcessBean.isRequestUnitPayingUnit()) {
	    final Unit unit = createAcquisitionProcessBean.getRequestingUnit();
	    process.getAcquisitionRequest().addFinancers(unit.finance(process.getAcquisitionRequest()));
	}

	return process;
    }

    public boolean isEditRequestItemAvailable() {
	User user = UserView.getUser();
	return user != null && user.getPerson().equals(getRequestor()) && getLastAcquisitionProcessState().isInGenesis();
    }

    public List<GenericAcquisitionProcessActivity> getActiveActivitiesForItem() {
	return getActiveActivities(ActivityScope.REQUEST_ITEM);
    }

    public List<GenericAcquisitionProcessActivity> getActiveActivitiesForRequest() {
	return getActiveActivities(ActivityScope.REQUEST_INFORMATION);
    }

    public List<GenericAcquisitionProcessActivity> getActiveActivities(ActivityScope scope) {
	List<GenericAcquisitionProcessActivity> activitiesResult = new ArrayList<GenericAcquisitionProcessActivity>();
	for (GenericAcquisitionProcessActivity activity : activities.get(scope)) {
	    if (activity.isActive(this)) {
		activitiesResult.add(activity);
	    }
	}
	return activitiesResult;
    }

    public List<GenericAcquisitionProcessActivity> getActiveActivities() {
	List<GenericAcquisitionProcessActivity> activitiesResult = new ArrayList<GenericAcquisitionProcessActivity>();
	for (ActivityScope scope : activities.keySet()) {
	    activitiesResult.addAll(getActiveActivities(scope));
	}
	return activitiesResult;
    }

    @Override
    public GenericAcquisitionProcessActivity getActivityByName(String activityName) {

	for (ActivityScope scope : activities.keySet()) {
	    for (GenericAcquisitionProcessActivity activity : activities.get(scope)) {
		if (activity.getName().equals(activityName)) {
		    return activity;
		}
	    }
	}
	return null;
    }

    @Override
    public Money getAcquisitionRequestValueLimit() {
	return PROCESS_VALUE_LIMIT;
    }

    @Override
    public boolean isSimplifiedAcquisitionProcess() {
	return true;
    }

    @Override
    public Map<ActivityScope, List<GenericAcquisitionProcessActivity>> getProcessActivityMap() {
	return activities;
    }

    @Override
    public List<AcquisitionProcessStateType> getAvailableStates() {
	List<AcquisitionProcessStateType> availableStates = new ArrayList<AcquisitionProcessStateType>();
	availableStates.add(AcquisitionProcessStateType.IN_GENESIS);
	availableStates.add(AcquisitionProcessStateType.SUBMITTED_FOR_APPROVAL);
	availableStates.add(AcquisitionProcessStateType.SUBMITTED_FOR_FUNDS_ALLOCATION);
	availableStates.add(AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER);
	availableStates.add(AcquisitionProcessStateType.FUNDS_ALLOCATED);
	availableStates.add(AcquisitionProcessStateType.APPROVED);
	availableStates.add(AcquisitionProcessStateType.ACQUISITION_PROCESSED);
	availableStates.add(AcquisitionProcessStateType.INVOICE_RECEIVED);
	availableStates.add(AcquisitionProcessStateType.SUBMITTED_FOR_CONFIRM_INVOICE);
	availableStates.add(AcquisitionProcessStateType.INVOICE_CONFIRMED);
	availableStates.add(AcquisitionProcessStateType.FUNDS_ALLOCATED_PERMANENTLY);
	availableStates.add(AcquisitionProcessStateType.ACQUISITION_PAYED);
	availableStates.add(AcquisitionProcessStateType.REJECTED);
	availableStates.add(AcquisitionProcessStateType.CANCELED);
	return availableStates;
    }
}
