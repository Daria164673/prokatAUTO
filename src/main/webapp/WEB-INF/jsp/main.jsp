<%@ include file="/WEB-INF/jspf/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/page.jspf" %>

<c:set var="current" value="main"/>

<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="sidenav">

         <div class="login-main-text">
            <h2>ProkatAUTO</h2>
            <p><fmt:message key="main.label.LoginRegister"/></p>
         </div>
      </div>

<%@ include file="/WEB-INF/jspf/error_msg.jspf" %>

      <div class="main">
         <div class="col-md-6 col-sm-12">
            <div class="login-form">
               <form  action="controller" method="post">
                  <input type="hidden" name="command" value="login"/>
                  <div class="form-group">
                  	<div class="input-group">
                  	  <div class="input-group-prepend">
                     	 <span class="input-group-text"> <i class="fa fa-user"></i> </span>
                     </div>
                     <input type="text" class="form-control" placeholder="<fmt:message key="all.label.UserName"/>" name="login">
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
                            <button class="btn btn-primary" onclick='submit()'>
                                     <i class="fa fa-sign-in-alt"></i>&nbsp;&nbsp;<fmt:message key="all.label.login"/>
                               </button>

                        <input type="button" class="btn btn-secondary" id="registerbutton" value="<fmt:message key="all.button.register"/>"
                              onclick='location.href="controller?command=register&loginvalue="+login.value+""' />

                        </div>
                    </div>

               </form>
            </div>
         </div>
</div>

<style>


</style>

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>