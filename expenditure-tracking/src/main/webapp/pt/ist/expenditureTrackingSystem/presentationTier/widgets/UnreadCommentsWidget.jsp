<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<bean:define id="dashBoardId" name="widget" property="dashBoardPanel.externalId"/>
<bean:define id="widgetId" name="widget" property="externalId"/>

<bean:define id="optionsEdit" value="<%= "edit-widgetOptions-" + widgetId %>" type="java.lang.String" toScope="page"/>
<bean:define id="optionsView" value="<%= "widgetOptions-" + widgetId %>" type="java.lang.String" toScope="page"/>

<logic:present name="<%= optionsEdit %>" scope="request">
	<fr:form action="<%= "/dashBoardManagement.do?method=widgetSubmition&dashBoardId=" + dashBoardId + "&dashBoardWidgetId=" + widgetId%>">
		<fr:edit id="<%= optionsView %>" name="<%= optionsEdit %>" scope="request">
			<fr:layout name="tabular">
				<fr:property name="classes" value="width100pc"/>
				<fr:property name="hideValidators" value="true"/>
			</fr:layout>
			<fr:schema bundle="EXPENDITURE_RESOURCES" type="pt.ist.expenditureTrackingSystem.domain.ExpenditureWidgetOptions">
				<fr:slot name="maxListSize" key="widget.numberOfProcesses">
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
		 			<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.NumberRangeValidator">
            			<fr:property name="upperBound" value="999"/>
            			<fr:property name="lowerBound" value="1"/>
        			</fr:validator>
       				<fr:property name="size" value="3"/>
					<fr:property name="maxLength" value="3"/>
				</fr:slot>
			</fr:schema>
		</fr:edit>
		<p>
			<html:submit styleClass="inputbutton">Ok</html:submit>
			<html:cancel styleClass="inputbutton">Cancel</html:cancel>
		</p>
	</fr:form>
</logic:present>

<logic:notPresent name="<%= optionsEdit %>" scope="request">
	<logic:notEmpty name="processesWithUnreadComments">
		<table class="width100pc">
			<logic:iterate id="process" name="processesWithUnreadComments">
				<bean:define id="oid" name="process" property="externalId" type="java.lang.String"/>
				<tr>
					<td>
						<html:link page="<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + oid %>">
							<fr:view name="process" property="acquisitionProcessId" />
						</html:link>
					</td>
					<td><fr:view name="process" property="processStateName" /></td>
				</tr>
			</logic:iterate>
			<tr>
				<td colspan="2" class="aright" style="padding-bottom: 8px !important;">
				<html:link page="/search.do?method=searchJump&showOnlyWithUnreadComments=true">
					<bean:message key="label.viewAll" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
				</td>			
			</tr>
		</table>
	</logic:notEmpty>
	<logic:empty name="processesWithUnreadComments">
		<p><em><bean:message key="label.no.processesWithUnreadComments" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
	</logic:empty>
</logic:notPresent>
