<%@ include file="/WEB-INF/jspf/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/page.jspf" %>

<c:set var="current" value="car"/>

<%@ include file="/WEB-INF/jspf/head.jspf" %>

<%@ include file="/WEB-INF/jspf/error_msg.jspf" %>

<div class="d-flex" style="height: 200px;">
  <div class="vr"></div>
</div>



<div class="container contact">
	<div class="row">
		<div class="col-md-5">
			<div class="contact-info">
			    <img src="data:image/jpg;base64,${car.base64Image}" width="360" height="240"/>
				<h2><fmt:message key="all.label.car"/> #${car.id}</h2>
				<h4>${car.brand} ${car.model} ${car.car_number}</h4>
			</div>
		</div>


            <div class="col-md-15">

                <form  action="controller" method="post" enctype="multipart/form-data">
                <input type="hidden" name="command" value="save_car"/>
                <input type="hidden" name="car_id" value="${car.id}"/>
                <input type="hidden" name="car_curr_state" value="${car.curr_state}"/>

			    <div class="contact-form">

                      <div class="form-group">
                        <label class="control-label col-sm-2" for="brand"><fmt:message key="all.label.brand"/></label>
                        <div class="col-sm-15">
                          <select class="form-control" id="brand" name="brand" ${user.getRole().name()=="CUSTOMER"? "disabled":""}>
                            <c:if test="${car==null}">
                                <option value="0" selected><fmt:message key="car.option.brand"/></option>
                            </c:if>
                            <c:forEach var="brand" items="${applicationScope.brands}">
                                <option ${(car.brand.id == brand.id)? " selected": ""} value="${brand.id}">${brand.name}</option>
                            </c:forEach>
                          </select>
                        </div>
                      </div>


                    <div class="form-group">
                      <label class="control-label col-sm-2" for="model"><fmt:message key="all.label.model"/></label>
                      <div class="col-sm-15">
                        <input type="text" class="form-control" id="model" name="model" value=${car.model}
                                ${user.getRole().name()=="CUSTOMER"? "disabled":""}>
                      </div>
                    </div>

                    <div class="form-group">
                      <label class="control-label col-sm-2" for="car_number"><fmt:message key="all.label.carnumber"/></label>
                      <div class="col-sm-15">
                        <input type="text" class="form-control" id="car_number" name="car_number" value=${car.car_number}
                                ${user.getRole().name()=="CUSTOMER"? "disabled":""}>
                      </div>
                    </div>

                    <div class="form-group">
                       <label class="control-label col-sm-2" for="qualityClass"><fmt:message key="all.label.qclass"/></label>
                       <div class="col-sm-15">
                           <select class="form-control" id="qualityClass" name="qualityClass"
                                ${user.getRole().name()=="CUSTOMER"? "disabled":""}>
                            <c:if test="${car==null}">
                                <option value="0" selected><fmt:message key="car.option.qclass"/></option>
                            </c:if>

                             <c:forEach var="qualityClass" items="${applicationScope.qClasses}">
                                 <option ${(car.qualityClass.id == qualityClass.id)? " selected": ""} value="${qualityClass.id}">${qualityClass.name}</option>
                             </c:forEach>
                           </select>
                       </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-sm-2" for="price"><fmt:message key="all.label.price"/></label>
                        <div class="col-sm-15">
                            <input type="number" class="form-control" id="price" name="price" min=0 step="0.01" pattern="0.00"
                                    onkeypress="return checkDigit(event)" value="${car.price}" ${user.getRole().name()=="CUSTOMER"? "disabled":""}>
                        </div>
                    </div>

                    <div class="form-group">
                         <input type="hidden" name="imgPath" value="${car.imgPath}"/>
                         <label class="control-label col-sm-2" for="file"><fmt:message key="all.label.img"/></label>
                         <div class="col-sm-15">
                             <input type = "file" name = "file" accept=".jpg, .jpeg, .png" ${user.getRole().name()=="CUSTOMER"? "disabled":""} />
                         </div>
                    </div>

			    </div>

			    <c:if test="${user.getRole().name()==\"ADMIN\"}">
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-20">
                            <button class="btn btn-success" onclick='submit()'>
                                  <i class="fas fa-2x fa-save"></i>&nbsp;&nbsp;<fmt:message key="all.button.save"/>
                            </button>
                        </div>
                    </div>
                </c:if>

			    </form>

                <div class="d-flex" style="height: 50px;">
                  <div class="vr"></div>
                </div>


                <c:if test="${user.getRole().name()==\"CUSTOMER\"}">
                     <form class="tdform" action="controller" method="get">
                          <input type="hidden" name="command" value="order">
                          <input type="hidden" name="car_id" value="${car.id}">
                          <div class="form-group">
                               <div class="col-sm-offset-2 col-sm-20">
                                  <button class="btn btn-primary" onclick='submit()'>
                                       <i class="fa fa-2x fa-car"></i>&nbsp;&nbsp;<fmt:message key="cars.button.order"/>
                                  </button>
                               </div>
                          </div>
                     </form>
                </c:if>

		    </div>

	</div>

</div>

<script>
 function checkDigit(event) {
     var code = (event.which) ? event.which : event.keyCode;

     if ((code < 48 || code > 57) && (code <> 2022)) {
        return false;
     }

     return true;
 }
 </script>

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>