<%@ include file="/WEB-INF/jspf/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/page.jspf" %>

<c:set var="current" value="repair_invoice"/>

<%@ include file="/WEB-INF/jspf/head.jspf" %>

<%@ include file="/WEB-INF/jspf/error_msg.jspf" %>

<div class="d-flex" style="height: 200px;">
  <div class="vr"></div>
</div>

<div class="container contact">
	<div class="row">
		<div class="col-md-3">
			<div class="contact-info">
				<img src="https://image.ibb.co/kUASdV/contact-image.png" alt="image"/>
				<h2><fmt:message key="repair.label.repairinvoicefor"/> ${car.brand} ${car.model} ${car.car_number}</h2>
				<h4></h4>
			</div>
		</div>

		<form  action="controller" method="post">
		<input type="hidden" name="command" value="saveRepairInvoice"/>
		<input type="hidden" name="car_id" value="${car.id}"/>
		<div class="col-md-15">
			<div class="contact-form">
				<div class="form-group">
				  <label class="control-label col-sm-2" for="contractor"><fmt:message key="all.label.contractor"/></label>
				  <div class="col-sm-10">
					<input type="text" class="form-control" id="contractor" placeholder="" name="contractor">
				  </div>
				</div>
				<div class="form-group">
				  <label class="control-label col-sm-2" for="amount"><fmt:message key="all.label.amount"/></label>
				  <div class="col-sm-10">
					<input type="number" class="form-control" min=0 step="0.01" id="amount" placeholder="" name="amount">
				  </div>
				</div>
				<div class="form-group">
				  <label class="control-label col-sm-2" for="repairInfo"><fmt:message key="all.label.repairinfo"/></label>
				  <div class="col-sm-10">
					<textarea class="form-control" rows="5" id="repairInfo" name="repairInfo"></textarea>
				  </div>
				</div>
				<div class="form-group">
				  <div class="col-sm-offset-2 col-sm-10">
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

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>
