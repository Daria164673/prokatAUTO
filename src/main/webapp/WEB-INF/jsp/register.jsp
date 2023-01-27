<%@ include file="/WEB-INF/jspf/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/page.jspf" %>

<c:set var="current" value="register"/>

<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="sidenav">
         <div class="login-main-text">
            <h2>ProkatAUTO</h2>
            <p><fmt:message key="register.label.register"/></p>
         </div>
      </div>

<%@ include file="/WEB-INF/jspf/error_msg.jspf" %>

      <div class="main">
         <div class="col-md-6 col-sm-12">
            <div class="login-form">
               <form  action="controller" method="post">
                  <input type="hidden" name="command" value="register"/>

                  <c:if test="${user.getRole().name()==\"ADMIN\"}">
                    <input type="hidden" name="role" value="MANAGER"/>
                  </c:if>

                   <div class="form-group">
                   	<div class="input-group">
                   	  <div class="input-group-prepend">
                      	 <span class="input-group-text"> <i class="fa fa-user"></i> </span>
                      </div>
                      <input type="text" class="form-control" placeholder="<fmt:message key="all.label.UserName"/>" name="login" value="${login}">
                   </div>
                   </div>

                  <div class="form-group">
                  	<div class="input-group">
                  		<div class="input-group-prepend">
                  		    <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
                  		 </div>
                  	    <input class="form-control" placeholder="<fmt:message key="all.label.Password"/>" type="password" name="password">
                  </div>
<br>
                  <div class="form-group">
                  	<div class="input-group">
                  		<div class="input-group-prepend">
                  		    <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
                  		 </div>
                  	    <input class="form-control" placeholder="<fmt:message key="register.label.confirm"/>" type="password" name="confirm">
                  </div>
<br>
                  <button class="btn btn-primary" onclick='submit()'>
                         <i class="fas fa-user-check"></i>&nbsp;&nbsp;<fmt:message key="all.button.register"/>
                    </button>
               </form>
            </div>
         </div>
      </div>

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>