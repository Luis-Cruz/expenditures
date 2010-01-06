package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalSystem;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class VerifyActivity extends WorkflowActivity<WorkingCapitalProcess, VerifyActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstance(); 
	if (workingCapitalSystem.isAccountingMember(user)) {
	    final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	    for (final WorkingCapitalInitialization workingCapitalInitialization : workingCapital.getWorkingCapitalInitializationsSet()) {
		if (workingCapitalInitialization.hasResponsibleForUnitApproval() && !workingCapitalInitialization.hasResponsibleForAccountingVerification()) {
		    return true;
		}
	    }
	}
	return false;
    }

    @Override
    protected void process(final VerifyActivityInformation activityInformation) {
	final WorkingCapitalInitialization workingCapitalInitialization = activityInformation.getWorkingCapitalInitialization();
	final User user = getLoggedPerson();
	workingCapitalInitialization.verify(user);
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new VerifyActivityInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
	return false;
    }

    @Override
    public boolean isVisible() {
	return false;
    }

}
