package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import org.fenixedu.bennu.WorkflowConfiguration;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.domain.ProcessFileSignatureHandler;
import module.workflow.domain.ProcessFileSignatureHandler.Provider;
import module.workflow.domain.SigningState;
import module.workflow.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem._development.ExpenditureConfiguration;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.FundAllocationExpirationDate;

@ClassNameBundle(bundle = "AcquisitionResources")
public class AdvancePaymentDocument extends AdvancePaymentDocument_Base {

    private static class AdvancePaymentDocumentSignHandler extends ProcessFileSignatureHandler<AdvancePaymentDocument> {

        private AdvancePaymentDocumentSignHandler(final AdvancePaymentDocument processFile) {
            super(processFile);
        }

        private SimplifiedProcedureProcess getProcess() {
            return (SimplifiedProcedureProcess) processFile.getProcess();
        }

        @Override
        public String filename() {
            return getProcess().getProcessNumber() + ".pdf";
        }

        @Override
        public String title() {
            return getProcess().getProcessNumber();
        }

        @Override
        public String queue() {
            return ExpenditureConfiguration.get().queueSimplifiedAdvancePayments();
        }

        @Override
        public boolean canSignFile() {
            final SigningState signingState = processFile.getSigningState();
            final User user = Authenticate.getUser();
            return signingState == SigningState.CREATED && ExpenditureTrackingSystem.isExpenseAuthority(user);
        }
    }

    static {
        final Provider<AdvancePaymentDocument> provider = AdvancePaymentDocumentSignHandler::new;
        ProcessFileSignatureHandler.register(AdvancePaymentDocument.class, provider);
    }

    public AdvancePaymentDocument(String displayName, String filename, byte[] content) {
        super();
        init(displayName, filename, content);
    }

    @Override
    public boolean isPossibleToArchieve() {
        SimplifiedProcedureProcess process = (SimplifiedProcedureProcess) getProcess();
        return process.getAcquisitionProcessState().getAcquisitionProcessStateType() == AcquisitionProcessStateType.IN_GENESIS
                || (process.getActivity(FundAllocationExpirationDate.class.getSimpleName()).isActive(process,
                        Authenticate.getUser()))
                || (getSigningState() == SigningState.PENDING
                        && ExpenditureTrackingSystem.isExpenseAuthority(Authenticate.getUser()));
    }

    public Boolean isSigned() {
        return !WorkflowConfiguration.getConfiguration().smartsignerIntegration()
                || (getSigningState() != null && getSigningState().compareTo(SigningState.SIGNED) == 0);
    }

}
