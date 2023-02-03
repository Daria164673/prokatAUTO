<%@ include file="/WEB-INF/jspf/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/page.jspf" %>

<c:set var="current" value="cars"/>

<%@ include file="/WEB-INF/jspf/head.jspf" %>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
<script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>

<div class="span7">
<div class="widget stacked widget-table action-table"  id="page_content">

				 <div class="widget-header">
					<i class="icon-th-list"></i>
					<h1><fmt:message key="all.label.cars"/></h1>
				</div> <!-- /widget-header -->

<%@ include file="/WEB-INF/jspf/error_msg.jspf" %>

                <c:if test="${user.getRole().name()==\"ADMIN\"}">
                    <form class="tdform" action="controller" method="get">
                         <input type="hidden" name="command" value="car">
                         <input type="hidden" name="car_id" value="0">
                         <button class="btn btn-primary" type="submit">
                              <i class="fa fa-plus fa-lg"></i>&nbsp;&nbsp;<fmt:message key="cars.button.newcar"/>
                         </button>
                    </form>
                </c:if>

                <div class="col-md-6 col-sm-12">
                      <form name="changeCurrent" action="controller" method="get">
                        <div class="form-group">
                        <div class="input-group">

                          <input type="hidden" name="command" value="cars">

                           <div class="col-md-3">
                                 <label><fmt:message key="cars.label.filter_by_brand"/></label>
                            </div>
                            <div class="col-md-6">
                                <select class="selectpicker" multiple name="filter_brands" onchange="submit()">
                                    <c:forEach var="brand" items="${applicationScope.brands}">
                                      <c:set var="str_id">${brand.id}</c:set>
                                      <option ${(filter_brands!=null && filter_brands.contains(str_id))?" selected" : ""}
                                            value="${brand.id}">${brand.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        </div>

                        <div class="form-group">
                        <div class="input-group">
                            <div class="col-md-3">
                                 <label><fmt:message key="cars.label.filter_by_qclass"/></label>
                            </div>
                            <div class="col-md-6">
                                <select class="selectpicker" multiple name="filter_qClass"  onchange="submit()">
                                  <c:forEach var="qClass" items="${applicationScope.qClasses}">
                                      <c:set var="str_id">${qClass.id}</c:set>
                                      <option ${(filter_qClass!=null && filter_qClass.contains(str_id))?" selected" : ""}
                                                                                  value="${qClass.id}">${qClass.name}</option>
                                  </c:forEach>
                                </select>
                            </div>
                         </div>
                         </div>
                        </div>

                        <div class="form-group">
                        <div class="input-group">
                            <div class="col-md-3">
                                <label><fmt:message key="cars.label.sort_by"/></label>
                            </div>
                            <div class="col-md-2">
                                <select class="custom-select" name="sorting"  onchange="submit()" value=${curr_sorting}>
                                    <option value="null">---<fmt:message key="cars.option.sort_default"/>---</option>
                                    <option value="name" ${curr_sorting=="name"?" selected" : ""} ><fmt:message key="cars.option.sort_name"/></option>
                                    <option value="name_desc" ${curr_sorting=="name_desc"?" selected" : ""} ><fmt:message key="cars.option.sort_name_desc"/></option>
                                    <option value="price" ${curr_sorting=="price"?" selected" : ""} ><fmt:message key="cars.option.sort_price"/></option>
                                    <option value="price_desc" ${curr_sorting=="price_desc"?" selected" : ""} ><fmt:message key="cars.option.sort_price_desc"/></option>
                                </select>
                            </div>
                        </div>
                        </div>

                        <!--<button class="btn btn-primary" type="submit"> -->
                        <!--       <i class="fa fa-filter fa-lg"></i>&nbsp;&nbsp;<fmt:message key="cars.button.filter"/> -->
                        <!--</button> -->
                      </form>

                </div> <!-- /widget-filter -->

				<div class="widget-content" id="table_content">

					<table class="table table-striped table-bordered">
						<thead>
							<tr>
								<th><fmt:message key="all.label.id"/></th>
								<th><fmt:message key="all.label.img"/></th>
                                <th><fmt:message key="all.label.brand"/></th>
                                <th><fmt:message key="all.label.model"/></th>
                                <th><fmt:message key="all.label.qclass"/></th>
								<th><fmt:message key="all.label.carnumber"/></th>
                                <th><fmt:message key="all.label.price"/></th>
                                <th><fmt:message key="cars.label.carstate"/></th>
                                <th class="td-actions"><fmt:message key="all.label.action"/></th>
							</tr>
						</thead>

						<tbody>
                                <c:forEach var="car" items="${cars}">
                                      <tr>
                                        <td>${car.id}</td>
                                        <td><img src="data:image/jpg;base64,${car.base64Image}" width="180" height="120"/></td>
                                        <td>${car.brand}</td>
                                        <td>${car.model}</td>
                                        <td>${car.qualityClass}</td>
                                        <td>${car.car_number}</td>
                                        <td><fmt:formatNumber type="number" pattern="0.00" value="${car.price}"/></td>
                                        <td>${car.curr_state}</td>
                                        <td>

                                        <c:if test="${car.curr_state == state_free  && user.getRole().name()==\"CUSTOMER\"
                                                        ||car.curr_state == state_free && user.getRole().name()==\"MANAGER\"}">
                                            <form class="tdform" action="controller" method="get">
                                                        <input type="hidden" name="command" value="car">
                                                        <input type="hidden" name="car_id" value="${car.id}">
                                                        <button class="btn btn-primary" onclick='submit()'>
                                                              <i class="fas fa-2x fa-search"></i>&nbsp;&nbsp;<fmt:message key="cars.button.view"/>
                                                        </button>
                                              </form>
                                        </c:if>

                                        <c:if test="${car.curr_state == state_free  && user.getRole().name()==\"CUSTOMER\"}">
                                             <form class="tdform" action="controller" method="get">
                                                       <input type="hidden" name="command" value="order">
                                                       <input type="hidden" name="car_id" value="${car.id}">
                                                       <button class="btn btn-primary" onclick='submit()'>
                                                             <i class="fa fa-2x fa-car"></i>&nbsp;&nbsp;<fmt:message key="cars.button.order"/>
                                                        </button>
                                             </form>
                                        </c:if>

                                        <c:if test="${car.curr_state == state_free && user.getRole().name()==\"MANAGER\"}">
                                               <form class="tdform" action="controller" method="get">
                                                      <input type="hidden" name="command" value="repair_invoice">
                                                      <input type="hidden" name="car_id" value="${car.id}">
                                                      <button class="btn btn-primary" onclick='submit()'>
                                                            <i class="fa fa-2x fa-wrench"></i>&nbsp;&nbsp;<fmt:message key="cars.button.repairinvoice"/>
                                                       </button>
                                               </form>
                                       </c:if>

                                        <c:if test="${user.getRole().name()==\"ADMIN\"}">
                                            <form class="tdform" action="controller">
                                                    <input type="hidden" name="command" value="car">
                                                    <input type="hidden" name="car_id" value="${car.id}">
                                                    <button class="btn btn-primary" onclick='submit()'>
                                                            <i class="fas fa-2x fa-edit"></i>&nbsp;&nbsp;<fmt:message key="cars.button.edit"/>
                                                    </button>
                                            </form>
                                            <form class="tdform" action="controller" method="delete">
                                                    <input type="hidden" name="command" value="delete_car">
                                                    <input type="hidden" name="car_id" value="${car.id}">
                                                    <button class="btn btn-danger" onclick='submit()'>
                                                            <i class="fas fa-2x fa-trash-alt"></i>&nbsp;&nbsp;<fmt:message key="cars.button.delete"/>
                                                    </button>
                                            </form>
                                            <form class="tdform" action="controller" method="get">
                                                     <input type="hidden" name="command" value="car">
                                                     <input type="hidden" name="copy" value="true">
                                                     <input type="hidden" name="car_id" value="${car.id}">
                                                     <button class="btn btn-success" onclick='submit()'>
                                                          <i class="fas fa-2x fa-copy" aria-hidden="true"></i>&nbsp;&nbsp;<fmt:message key="cars.button.copy"/>
                                                    </button>
                                            </form>
                                        </c:if>
                                        </td>

                                    </tr>
                                </c:forEach>
							</tbody>
					</table>

                    <%@ include file="/WEB-INF/jspf/pagination.jspf" %>

				</div> <!-- /widget-content -->

			</div> <!-- /widget -->
</div>

<style>
#table_content{
    padding: 30px;
}
</style>

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>