package module.workingCapital.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowProcess;
import module.workingCapital.domain.activity.ApproveActivity;
import module.workingCapital.domain.activity.AuthorizeActivity;
import module.workingCapital.domain.activity.UnApproveActivity;
import module.workingCapital.domain.activity.UnAuthorizeActivity;
import module.workingCapital.domain.activity.UnVerifyActivity;
import module.workingCapital.domain.activity.VerifyActivity;
import myorg.domain.User;
import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class WorkingCapitalProcess extends WorkingCapitalProcess_Base {

    public static final Comparator<WorkingCapitalProcess> COMPARATOR_BY_UNIT_NAME = new Comparator<WorkingCapitalProcess>() {
	@Override
	public int compare(WorkingCapitalProcess o1, WorkingCapitalProcess o2) {
	    final int c = o1.getWorkingCapital().getUnit().getName().compareTo(o2.getWorkingCapital().getUnit().getName());
	    return c == 0 ? o2.hashCode() - o1.hashCode() : c;
	}
    };

    private static final List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> activities;

    static {
	final List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> activitiesAux = new ArrayList<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>>();
	activitiesAux.add(new ApproveActivity());
	activitiesAux.add(new UnApproveActivity());
	activitiesAux.add(new VerifyActivity());
	activitiesAux.add(new UnVerifyActivity());
	activitiesAux.add(new AuthorizeActivity());
	activitiesAux.add(new UnAuthorizeActivity());
	activities = Collections.unmodifiableList(activitiesAux);
    }

    public WorkingCapitalProcess() {
        super();
    }

    public WorkingCapitalProcess(final Integer year, final Unit unit, final Person person,
	    final Money requestedAnualValue, final String fiscalId, final String bankAccountId) {
	this();
	final WorkingCapitalInitialization workingCapitalInitialization = new WorkingCapitalInitialization(year, unit, person, requestedAnualValue, fiscalId, bankAccountId);
	final WorkingCapital workingCapital = workingCapitalInitialization.getWorkingCapital();
	setWorkingCapital(workingCapital);
    }

    @Override
    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> List<T> getActivities() {
	return (List) activities;
    }

    @Override
    public boolean isActive() {
	return true;
    }

    @Override
    public List<Class<? extends ProcessFile>> getAvailableFileTypes() {
        return Collections.emptyList();
    }

    public boolean isPendingAproval(final User user) {
	return getWorkingCapital().isPendingAproval(user);
    }

    public boolean isPendingVerification(User user) {
	return getWorkingCapital().isPendingVerification(user);
    }

    public boolean isPendingAuthorization(User user) {
	return getWorkingCapital().isPendingAuthorization(user);
    }

    @Override
    public User getProcessCreator() {
        return getWorkingCapital().getRequester();
    }

}
