<%@ include file="/common/taglibs.jsp"%>
 
<head>
    <title><fmt:message key="passportList.title"/></title>
    <meta name="menu" content="PassportMenu"/>
</head>
 
<h2><fmt:message key="passportList.heading"/></h2>
 
<div id="actions" class="btn-group">
    <a href='<c:url value="/passportform"/>' class="btn btn-primary">
        <i class="icon-plus icon-white"></i> <fmt:message key="button.add"/></a>
    <a href='<c:url value="/home"/>' class="btn btn-default"><i class="icon-ok"></i> <fmt:message key="button.done"/></a>
</div>
 
<display:table name="passportList" class="table table-condensed table-striped table-hover" requestURI=""
               id="passportList" export="true" pagesize="25">
    <display:column property="id" sortable="true" href="passportform" media="html"
        paramId="id" paramProperty="id" titleKey="passport.id"/>
    <display:column property="id" media="csv excel xml pdf" titleKey="passport.id"/>
    <display:column property="firstName" sortable="true" titleKey="passport.firstName"/>
    <display:column property="lastName" sortable="true" titleKey="passport.lastName"/>
 
    <display:setProperty name="paging.banner.item_name"><fmt:message key="passportList.passport"/></display:setProperty>
    <display:setProperty name="paging.banner.items_name"><fmt:message key="passportList.passports"/></display:setProperty>
 
    <display:setProperty name="export.excel.filename"><fmt:message key="passportList.title"/>.xls</display:setProperty>
    <display:setProperty name="export.csv.filename"><fmt:message key="passportList.title"/>.csv</display:setProperty>
    <display:setProperty name="export.pdf.filename"><fmt:message key="passportList.title"/>.pdf</display:setProperty>
</display:table>