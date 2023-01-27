<%@ include file="/WEB-INF/jspf/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/page.jspf" %>

<c:set var="current" value="orders"/>

<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="span7">
<div class="widget stacked widget-table action-table">

<div class="widget-header">
					<i class="icon-th-list"></i>
					<h1>Orders</h1>
				</div> <!-- /widget-header -->

<%@ include file="/WEB-INF/jspf/error_msg.jspf" %>

                <div class="widget-content">

					<table class="table table-striped table-bordered">
						<thead>
							<tr>
								<th><fmt:message key="all.label.id"/></th>
								<th><fmt:message key="all.label.date"/></th>
                                <th><fmt:message key="all.label.user"/></th>
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
						        <c:set var="sign" value="#"/>
                                <c:set var="modalReject" value="modalReject"/>
                                <c:forEach var="order" items="${orders}">
                                      <tr>
                                        <td>${order.id}</td>
                                        <td>${order.date}</td>
                                        <td>${order.user.login}</td>
                                        <td>${order.car.id} ${order.car.brand} ${order.car.model} ${order.car.car_number}</td>
                                        <td>
                                            <div class="col-md-3">
                                                        <input type="checkbox" name="withDriver1" value=""
                                                                 ${(order.withDriver == true)? " checked": ""} disabled>
                                            </div>


                                        </td>
                                        <td>${order.term}</td>
                                        <td>${order.state}</td>
                                        <td><fmt:formatNumber type="number" pattern="0.00" value="${order.amount}"/></td>
                                        <td>${order.returnDate} ${order.reject_reason}</td>

                                          <c:if test="${order.state != state_rejected && order.state != state_finished && user.getRole().name()==\"MANAGER\"}">
                                                <td>
                                                    <button class="btn btn-danger" data-toggle="modal"
                                                             data-target="${sign}${modalReject}${order.id}">
                                                            <i class="fa fa-times fa-lg"></i>&nbsp;&nbsp;<fmt:message key="orders.button.reject"/>
                                                    </button>
                                                </td>
                                                </c:if>
                                          <c:if test="${order.state == state_paid &&
                                                user.getRole().name()==\"MANAGER\"}">
                                             <td>
                                                <form class="tdform" action="controller" method="post">
                                                    <input type="hidden" name="command" value="return_order">
                                                    <input type="hidden" name="order_id" value="${order.id}">
                                                    <input type="hidden" name="car_id" value="${order.car.id}">
                                                    <button class="btn btn-primary" onclick='submit()'>
                                                        <i class="fas fa-undo"></i>&nbsp;&nbsp;<fmt:message key="all.button.return"/>
                                                    </button>
                                                 </form>
                                             </td>
                                        </c:if>


                                        <div class="modal fade" id=${modalReject}${order.id} tabindex="-1">
                                          <div class="modal-dialog">
                                            <div class="modal-content">
                                              <div class="modal-header">
                                                <h5 class="modal-title"><fmt:message key="orders.modal.rejectreason.title"/></h5>
                                                <button type="button" class="close" data-dismiss="modal">
                                                  <span>&times;</span>
                                                </button>
                                              </div>
                                              <div class="modal-body">
                                                    <form class="tdform" action="controller" method="post">
                                                                       <input type="hidden" name="command" value="reject_order">
                                                                       <input type="hidden" name="order_id" value="${order.id}">
                                                                       <input type="hidden" name="car_id" value="${order.car.id}">
                                                                       <div class="col-sm-10">
                                                                            <textarea class="form-control" rows="2" id="reject_reason"
                                                                                name="reject_reason" placeholder="<fmt:message key="all.label.rejectreason"/>" ></textarea>
                                                                       </div>
                                                                       <br>
                                                                       <button class="btn btn-primary" onclick='submit()'>
                                                                              <i class="fa fa-times fa-lg"></i>&nbsp;&nbsp;<fmt:message key="orders.button.reject"/>
                                                                       </button>
                                                    </form>
                                              </div>
                                              <div class="modal-footer">

                                              </div>
                                            </div>
                                          </div>
                                        </div>

                                    </tr>
                                </c:forEach>
						</tbody>
					</table>

                    <c:set var="current_page" value="<%=Path.COMMAND__ORDERS%>"/>
                    <%@ include file="/WEB-INF/jspf/pagination.jspf" %>

				</div> <!-- /widget-content -->

			</div> <!-- /widget -->
</div>

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>