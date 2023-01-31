<%@ include file="/WEB-INF/jspf/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/page.jspf" %>

<c:set var="current" value="users"/>

<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="span7">
<div class="widget stacked widget-table action-table"  id="page_content">

				 <div class="widget-header">
					<i class="icon-th-list"></i>
					<h1><fmt:message key="users.h.users"/></h1>
					<br>
				</div> <!-- /widget-header -->

<%@ include file="/WEB-INF/jspf/error_msg.jspf" %>

                <c:if test="${user.getRole().name()==\"ADMIN\"}">
                    <form class="tdform" action="controller" method="get">
                         <input type="hidden" name="command" value="register">

                        <button class="btn btn-primary" type="submit">
                             <i class="fa fa-plus fa-lg"></i>&nbsp;&nbsp;<fmt:message key="users.button.newmanager"/>
                        </button>

                    </form>
                </c:if>

				<div class="widget-content">
                    <br>
					<table class="table table-striped table-bordered">
						<thead>
							<tr>
								<th><fmt:message key="all.label.id"/></th>
								<th><fmt:message key="all.label.login"/></th>
                                <th><fmt:message key="users.th.lang"/></th>
                                <th><fmt:message key="all.label.role"/></th>
							    <th class="td-actions"><fmt:message key="all.label.action"/></th>
							</tr>
						</thead>

						<tbody>
                                <c:forEach var="user_item" items="${users}">
                                      <tr>
                                        <td>${user_item.id}</td>
                                        <td>${user_item.login}</td>
                                        <td>${user_item.locale.getLanguage().toUpperCase()}</td>
                                        <td>${user_item.role}</td>
                                        <c:if test="${user_item.isBlocked == true && user.getRole().name()==\"ADMIN\"}">
                                             <td>
                                                  <form class="tdform" action="controller" method="post">
                                                       <input type="hidden" name="command" value="change_users_block">
                                                       <input type="hidden" name="user_id" value="${user_item.id}">
                                                       <input type="hidden" name="isBlocked_value" value="false">

                                                    <button class="btn btn-success" type="submit">
                                                         <i class="fa fa-check"></i>&nbsp;&nbsp;<fmt:message key="users.button.unblock"/>
                                                    </button>

                                                  </form>
                                             </td>
                                        </c:if>
                                        <c:if test="${(user_item.isBlocked != true) && user.getRole().name()==\"ADMIN\"}">
                                             <td>
                                                  <form class="tdform" action="controller" method="post">
                                                       <input type="hidden" name="command" value="change_users_block">
                                                       <input type="hidden" name="user_id" value="${user_item.id}">
                                                       <input type="hidden" name="isBlocked_value" value="true">

                                                        <button class="btn btn-danger" type="submit">
                                                         <i class="fa fa-ban fa-lg"></i>&nbsp;&nbsp;<fmt:message key="users.button.block"/>
                                                       </button>

                                                  </form>
                                             </td>
                                        </c:if>
                                    </tr>
                                </c:forEach>
						</tbody>
				    </table>

                    <c:set var="current_page" value="<%=Path.COMMAND__USERS%>"/>
                    <%@ include file="/WEB-INF/jspf/pagination.jspf" %>

				</div> <!-- /widget-content -->

			</div> <!-- /widget -->
</div>

<style>

</style>

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>