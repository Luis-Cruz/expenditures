package pt.ist.expenditureTrackingSystem.presentationTier.renderers;

import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class MoneyRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout()  {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		Money money = (Money) object;
		return new HtmlText(money.getValue().toPlainString());
	    }
	    
	};
	
    }

}
