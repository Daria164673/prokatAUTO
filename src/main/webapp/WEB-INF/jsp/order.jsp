<%@ include file="/WEB-INF/jspf/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/page.jspf" %>

<c:set var="current" value="order"/>

<%@ include file="/WEB-INF/jspf/head.jspf" %>

<%@ include file="/WEB-INF/jspf/error_msg.jspf" %>

<div class="d-flex" style="height: 200px;">
  <div class="vr"></div>
</div>

<div class="container contact" >
	<div class="row" >
		<div class="col-md-4">
			<div class="contact-info">
				<img src="https://image.ibb.co/kUASdV/contact-image.png" alt="image"/>
				<h2>New order for ${car.brand} ${car.model} ${car.car_number}</h2>
				<h4><fmt:message key="order.label.OurManager"/></h4>
			</div>
		</div>

		<form  action="controller" method="post">
		<input type="hidden" name="command" value="saveOrder"/>
		<input type="hidden" name="car_id" value="${car.id}"/>

		<div class="col-md-30">
			<div class="contact-form">

                <div class="d-flex" style="height: 20px;">
                      <div class="vr"></div>
                </div>

				<div class="form-group">
				  <label class="control-label col-sm-30" for="term"><fmt:message key="order.label.term"/></label>
				  <div class="col-sm-20">
					<input type="number" class="form-control" id="term" placeholder="Term in days" name="term"
					    onkeypress="return checkDigit(event)" min ="0" >
				  </div>
				</div>

                <div class="d-flex" style="height: 20px;">
                      <div class="vr"></div>
                </div>

				<div class="col-sm-20">
                				<div class="form-check">
                				  <input class="form-check-input" type="checkbox" id="withDriver" name="withDriver" >
                                  <label class="form-check-label" for="withDriver">
                                    <fmt:message key="order.label.withDriver"/>
                                  </label>
                                </div>
                                </div>
                    <div class="d-flex" style="height: 20px;">
                        <div class="vr"></div>
                  </div>

				<div class="form-group">
				  <label class="control-label col-sm-20" for="passportData"><fmt:message key="order.label.passportdata"/></label>
				  <div class="col-sm-20">
					<textarea class="form-control" rows="5" id="passportData" name="passportData"></textarea>
				  </div>
				</div>

				<div class="d-flex" style="height: 50px;">
                      <div class="vr"></div>
                </div>

				<div class="form-group">
				  <div class="col-sm-offset-2 col-sm-20">
					<button class="btn btn-success" onclick='submit()'>
                        <i class="fas fa-2x fa-save"></i>&nbsp;&nbsp;<fmt:message key="all.button.save"/>
                    </button>
				  </div>
				</div>
			</div>
		</div>
		</form>
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
