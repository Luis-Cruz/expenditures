<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="authorizations.title.delegate" bundle="EXPENDITURE_RESOURCES"/></h2>

<bean:define id="urlExpand" type="java.lang.String">/expenditureTrackingOrganization.do?method=chooseDelegationUnit&amp;authorizationOid=<bean:write name="authorization" property="externalId"/></bean:define>

<bean:define id="urlSelect" type="java.lang.String">/expenditureTrackingOrganization.do?method=delegateAuthorization&amp;authorizationOid=<bean:write name="authorization" property="externalId"/></bean:define>
<p>
	<html:link action="<%= urlExpand %>">
		<bean:message key="link.top" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
</p>

<logic:present name="unit">
	<bean:write name="unit" property="name"/>
	<html:link action="<%= urlExpand %>" paramId="unitOid" paramName="unit" paramProperty="externalId">
		<bean:message key="link.expand" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
	<html:link action="<%= urlSelect %>" paramId="unitOid" paramName="unit" paramProperty="externalId">
		<bean:message key="link.select" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
	<br/>	
</logic:present>

<logic:present name="units">
	<logic:iterate id="u" name="units">
		<logic:present name="unit">
			<logic:present name="unit" property="parentUnit">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</logic:present>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</logic:present>
		<bean:write name="u" property="name"/>
		<html:link action="<%= urlExpand %>" paramId="unitOid" paramName="u" paramProperty="externalId">
			<bean:message key="link.expand" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
		<html:link action="<%= urlSelect %>" paramId="unitOid" paramName="u" paramProperty="externalId">
			<bean:message key="link.select" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
		<br/>
	</logic:iterate>
</logic:present>
