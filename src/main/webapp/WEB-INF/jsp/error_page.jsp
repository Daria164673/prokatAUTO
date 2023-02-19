<%@ include file="/WEB-INF/jspf/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/page.jspf" %>

<c:set var="current" value="error_page"/>

<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="form-group"  id="page_content">

<c:set var="title" value="Error page" />
<c:set var="msg" value="${errorMessage}" />

        <h1><fmt:message key="error.label.error_page"/> </h1>
        <h3><fmt:message key="error.label.something_went_wrong"/> </h3>

<br>

<%@ include file="/WEB-INF/jspf/error_msg.jspf" %>

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>