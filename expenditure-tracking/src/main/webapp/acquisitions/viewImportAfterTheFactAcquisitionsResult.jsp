<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="link.sideBar.importAfterTheFactAcquisitions" bundle="EXPENDITURE_RESOURCES"/></h2>

<bean:define id="afterTheFactAcquisitionsImportBean" name="afterTheFactAcquisitionsImportBean" type="pt.ist.expenditureTrackingSystem.domain.dto.AfterTheFactAcquisitionsImportBean"/>

<logic:equal name="afterTheFactAcquisitionsImportBean" property="createData" value="true">
	<logic:equal name="afterTheFactAcquisitionsImportBean" property="errorCount" value="0">
		<div class="success1">
			<p class="mvert05"><strong>Ficheiro importado com sucesso.</strong></p>
			<p class="mvert05"><bean:message key="label.afterTheFactAcquisition.import.lines" bundle="ACQUISITION_RESOURCES"/>: <bean:write name="afterTheFactAcquisitionsImportBean" property="importedLines"/></p>
		</div>
	</logic:equal>
</logic:equal>

<logic:greaterThan name="afterTheFactAcquisitionsImportBean" property="errorCount" value="0">
	<div class="error1">
		<bean:message key="label.afterTheFactAcquisition.import.not.imported" bundle="ACQUISITION_RESOURCES"/>
		<bean:message key="label.afterTheFactAcquisition.import.errors" bundle="ACQUISITION_RESOURCES" arg0="<%= Integer.toString(afterTheFactAcquisitionsImportBean.getErrorCount()) %>"/>
		<table>
			<logic:iterate id="issue" name="afterTheFactAcquisitionsImportBean" property="issues" type="pt.ist.expenditureTrackingSystem.domain.dto.Issue">
				<% if (issue.getIssueType().getIssueTypeLevel().name().equals("ERROR")) { %>
					<tr>
						<td>
							<bean:define id="key">label.afterTheFactAcquisition.import.issueType.<%= issue.getIssueType() %></bean:define>
							<%
								final String[] args = new String[5];
								args[0] = Integer.toString(issue.getLineNumber());
								for (int i = 0; i < issue.getMessageArgs().length; i++) {
								    args[i + 1] = issue.getMessageArgs()[i];
								}
								for (int i = issue.getMessageArgs().length + 1; i < 5; i++) {
								    args[i] = "";
								}
							%>
							<bean:message key="<%= key %>" bundle="ACQUISITION_RESOURCES" arg0="<%= args[0] %>" arg1="<%= args[1] %>" arg2="<%= args[2] %>" arg3="<%= args[3] %>" arg4="<%= args[4] %>"/>
						</td>
					</tr>
				<% } %>
			</logic:iterate>
		</table>
	</div>
</logic:greaterThan>
<logic:greaterThan name="afterTheFactAcquisitionsImportBean" property="warningCount" value="0">
	<div class="warning2">
		<bean:message key="label.afterTheFactAcquisition.import.warnings" bundle="ACQUISITION_RESOURCES" arg0="<%= Integer.toString(afterTheFactAcquisitionsImportBean.getWarningCount()) %>"/>
		<table>
			<logic:iterate id="issue" name="afterTheFactAcquisitionsImportBean" property="issues" type="pt.ist.expenditureTrackingSystem.domain.dto.Issue">
				<% if (issue.getIssueType().getIssueTypeLevel().name().equals("WARNING")) { %>
					<tr>
						<td>
							<bean:define id="key">label.afterTheFactAcquisition.import.issueType.<%= issue.getIssueType() %></bean:define>
							<%
								final String[] args = new String[5];
								args[0] = Integer.toString(issue.getLineNumber());
								for (int i = 0; i < issue.getMessageArgs().length; i++) {
								    args[i+1] = issue.getMessageArgs()[i];
								}
								for (int i = issue.getMessageArgs().length + 1; i < 5; i++) {
								    args[i] = "";
								}
							%>
							<bean:message key="<%= key %>" bundle="ACQUISITION_RESOURCES" arg0="<%= args[0] %>" arg1="<%= args[1] %>" arg2="<%= args[2] %>" arg3="<%= args[3] %>" arg4="<%= args[4] %>"/>
						</td>
					</tr>
				<% } %>
			</logic:iterate>
		</table>
	</div>
</logic:greaterThan>

<div class="forminline mbottom2">

<logic:equal name="afterTheFactAcquisitionsImportBean" property="createData" value="false">
	<logic:equal name="afterTheFactAcquisitionsImportBean" property="errorCount" value="0">
		<p><bean:message key="label.afterTheFactAcquisition.processDoneWishtoImport" bundle="ACQUISITION_RESOURCES"/></p>
		<fr:form action="/acquisitionAfterTheFactAcquisitionProcess.do?method=importAcquisitions">
			<fr:edit id="bean" name="afterTheFactAcquisitionsImportBean" visible="false"/>
			<html:submit styleClass="inputbutton"><bean:message key="label.import" bundle="ACQUISITION_RESOURCES"/></html:submit>
		</fr:form>
	</logic:equal>
</logic:equal>

<fr:form action="/acquisitionAfterTheFactAcquisitionProcess.do?method=prepareImport">
	<html:submit styleClass="inputbutton"><bean:message key="label.back" bundle="EXPENDITURE_RESOURCES"/></html:submit>
</fr:form>

</div>
