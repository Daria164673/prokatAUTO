<%@ include file="/WEB-INF/jspf/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/page.jspf" %>

<c:set var="current" value="user_account"/>

<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="span7">
<div class="widget stacked widget-table action-table"  id="page_content">

				<div class="widget-header">
					<i class="icon-th-list"></i>
					<h1><fmt:message key="orders.h.orders"/></h1>
				</div> <!-- /widget-header -->
<br>

<%@ include file="/WEB-INF/jspf/error_msg.jspf" %>

                <div class="widget-content">

					<table class="table table-striped table-bordered">
						<thead>
							<tr>
								<th><fmt:message key="all.label.id"/></th>
								<th><fmt:message key="all.label.date"/></th>
                                <th><fmt:message key="all.label.car"/></th>
								<th><fmt:message key="all.label.driver"/></th>
                                <th><fmt:message key="all.label.term"/></th>
                                <th><fmt:message key="all.label.state"/></th>
                                <th><fmt:message key="all.label.amount"/></th>
                                <th><fmt:message key="orders.label.returnreject"/></th>
                                <th class="td-actions"><fmt:message key="all.label.action"/></th>
							</tr>
						</thead>

						<tbody>
                                <c:forEach var="order" items="${orders}">
                                      <tr>
                                        <td>${order.id}</td>
                                        <td>${order.date}</td>
                                        <td>${order.car.id} ${order.car.brand} ${order.car.model} ${order.car.car_number}</td>
                                        <td>
                                            <div class="col-md-3">
                                                        <input type="checkbox" name="withDriver" value=""
                                                                 ${(order.withDriver == true)? " checked": ""} disabled>
                                            </div>
                                        </td>
                                        <td>${order.term}</td>
                                        <td>${order.state}</td>
                                        <td><fmt:formatNumber type="number" pattern="0.00" value="${order.amount}"/></td>
                                        <td>${order.returnDate} ${order.reject_reason}</td>
                                        <c:if test="${order.state != state_rejected}">
                                             <td>
                                                <form class="tdform" action="controller" method="get">
                                                    <input type="hidden" name="command" value="pay_invoice">
                                                    <input type="hidden" name="order_id" value="${order.id}">
                                                    <c:if test="${order.state == state_new}">
                                                        <button class="btn btn-primary" onclick='submit()'>
                                                             <i class="fa fa-credit-card"></i>&nbsp;&nbsp;<fmt:message key="orders.button.payinvoice"/>
                                                        </button>
                                                    </c:if>
                                                    <c:if test="${order.state != state_new}">
                                                         <button class="btn btn-secondary" onclick='submit()'>
                                                              <i class="fa fa-credit-card"></i>&nbsp;&nbsp;<fmt:message key="orders.button.payinvoice"/>
                                                         </button>
                                                     </c:if>
                                                 </form>
                                             </td>
                                        </c:if>


                                    </tr>
                                </c:forEach>
							</tbody>
						</table>

                    <c:set var="current_page" value="<%=Path.COMMAND__USER_ACCOUNT%>"/>
                    <%@ include file="/WEB-INF/jspf/pagination.jspf" %>

				</div> <!-- /widget-content -->

			</div> <!-- /widget -->
</div>

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>