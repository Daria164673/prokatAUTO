<%@ include file="/WEB-INF/jspf/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/page.jspf" %>

<c:set var="current" value="repair_invoices"/>

<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="span7">
<div class="widget stacked widget-table action-table">

                <div class="widget-header">
					<i class="icon-th-list"></i>
					<h1><fmt:message key="repair.label.repairinvoices"/></h1>
				</div> <!-- /widget-header -->

<%@ include file="/WEB-INF/jspf/error_msg.jspf" %>

                <div class="widget-content">

					<table class="table table-striped table-bordered">
						<thead>
							<tr>
								<th><fmt:message key="all.label.id"/></th>
								<th><fmt:message key="all.label.date"/></th>
                                <th><fmt:message key="all.label.car"/></th>
                                <th><fmt:message key="all.label.contractor"/></th>
								<th><fmt:message key="all.label.amount"/></th>
                                <th><fmt:message key="all.label.repairinfo"/></th>
                                <th><fmt:message key="all.label.returndate"/></th>
                                <th class="td-actions"><fmt:message key="all.label.action"/></th>
							</tr>
						</thead>

						<tbody>
                                <c:forEach var="repair" items="${repairs}">
                                      <tr>
                                        <td>${repair.id}</td>
                                        <td>${repair.date}</td>
                                        <td>${order.car.id} ${repair.car.brand} ${repair.car.model} ${repair.car.car_number}</td>
                                        <td>${repair.contractor}</td>
                                        <td><fmt:formatNumber type="number" pattern="0.00" value="${repair.amount}"/></td>
                                        <td>${repair.repairInfo}</td>
                                        <td>${repair.returnDate}</td>

                                        <c:if test="${user.getRole().name()==\"MANAGER\" && repair.returnDate==null}">
                                             <td>
                                                <form class="tdform" action="controller" method="post">
                                                    <input type="hidden" name="command" value="return_from_repair">
                                                    <input type="hidden" name="repair_id" value="${repair.id}">
                                                    <input type="hidden" name="car_id" value="${repair.car.id}">
                                                    <button class="btn btn-primary" onclick='submit()'>
                                                         <i class="fas fa-undo"></i>&nbsp;&nbsp;<fmt:message key="all.button.return"/>
                                                    </button>
                                                 </form>
                                             </td>
                                        </c:if>
                                    </tr>
                                </c:forEach>
							</tbody>
						</table>

                    <c:set var="current_page" value="<%=Path.COMMAND__REPAIR_INVOICES%>"/>
                    <%@ include file="/WEB-INF/jspf/pagination.jspf" %>

				</div> <!-- /widget-content -->

			</div> <!-- /widget -->
</div>

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>