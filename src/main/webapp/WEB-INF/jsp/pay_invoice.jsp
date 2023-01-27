<%@ include file="/WEB-INF/jspf/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/page.jspf" %>

<c:set var="current" value="pay_invoice"/>

<%@ include file="/WEB-INF/jspf/head.jspf" %>

<%@ include file="/WEB-INF/jspf/error_msg.jspf" %>

<div id="inventory-invoice">

    <div class="toolbar hidden-print">
        <div class="text-right">
            <button id="printInvoice" class="btn btn-info"><i class="fa fa-print"></i><fmt:message key="all.button.print"/></button>
            /*<button class="btn btn-info"><i class="fa fa-file-pdf-o"></i> Export as PDF</button>*/
        </div>
        <hr>
    </div>
    <div class="invoice overflow-auto">
        <div style="min-width: 600px">
            <header>
                <div class="row">
                    <div class="col">
                        <a target="_blank" href="#">
                            <h1>prokatAUTO</h1>
                        </a>
                    </div>
                    <div class="col company-details">
                        <h2 class="name">
                            <a target="_blank" href="#">
                                prokatAUTO
                            </a>
                        </h2>
                        <div>26 Tower Name, City 123456, INDIA</div>
                        <div>(123) 456-789</div>
                        <div>prokatAUTO202@gmail.com</div>
                    </div>
                </div>
            </header>
            <main>
                <div class="row contacts">
                    <div class="col invoice-to">
                        <div class="text-gray-light"><fmt:message key="invoice.label.invoiceTo"/></div>
                        <h2 class="to">client</h2>
                        <div class="address">client adress</div>
                        <div class="email"><a href="mailto:test@example.com">test@example.com</a></div>
                    </div>
                    <div class="col invoice-details">
                        <h1 class="invoice-id"><fmt:message key="invoice.label.invoice"/> ${order.id}</h1>
                        <div class="date"><fmt:message key="invoice.label.dateofinvoice"/>: ${order.date}</div>
                        <div class="date"><fmt:message key="invoice.label.duedate"/>: ${order.date}</div>
                    </div>
                </div>
                <table border="0" cellspacing="0" cellpadding="0">
                    <thead>
                        <tr>
                            <th><fmt:message key="invoice.th.NO"/></th>
                            <th class="text-left"><fmt:message key="invoice.th.description"/></th>
                            <th class="text-right"><fmt:message key="invoice.th.price"/></th>
                            <th class="text-right"><fmt:message key="invoice.th.tax"/> (13%)</th>
                            <th class="text-right"><fmt:message key="invoice.th.total"/></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="no">01</td>
                            <td class="text-left"><h3><fmt:message key="invoice.td.rentcar"/> ${order.car.brand}</h3>${order.car.model} ${order.car.car_number}</td>
                            <td class="unit"><fmt:formatNumber type="number" pattern="0.00" value="${order.amount*0.9}"/></td>
                            <td class="tax">10%</td>
                            <td class="total">${order.amount}</td>
                        </tr>
                        <c:if test="${order.withDriver == true}">
                            <tr>
                                <td class="no">02</td>
                                <td class="text-left"><h3><fmt:message key="invoice.td.driverservice"/></h3>for free</td>
                                <td class="unit">00.00</td>
                                <td class="tax">10%</td>
                                <td class="total">0.00</td>
                            </tr>
                        </c:if>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="2"></td>
                            <td colspan="2"><fmt:message key="invoice.td.subtotal"/></td>
                            <td><fmt:formatNumber type="number" pattern="0.00" value="${order.amount*0.9}"/></td>
                        </tr>
                        <tr>
                            <td colspan="2"></td>
                            <td colspan="2"><fmt:message key="invoice.td.tax"/></td>
                            <td><fmt:formatNumber type="number" pattern="0.00" value="${order.amount*0.1}"/></td>
                        </tr>
                        <tr>
                            <td colspan="2"></td>
                            <td colspan="2"><fmt:message key="invoice.td.grandtotal"/></td>
                            <td>${order.amount}</td>
                        </tr>
                    </tfoot>
                </table>
                <div class="thanks"><fmt:message key="invoice.label.thanks"/></div>
                <div class="notices">
                    <div><fmt:message key="invoice.label.notice"/></div>
                    <div class="notice">System Generated Invoice.</div>
                </div>
            </main>
            <footer>
                Invoice was generated on a computer and is valid without the signature and seal.
            </footer>
        </div>
        <div></div>
    </div>
                <c:if test="${order.state == state_new}">
                    <button class="btn btn-primary" data-toggle="modal"
                             data-target="#modalPayment">
                                <i class="fa fa-credit-card"></i>&nbsp;&nbsp;<fmt:message key="invoice.button.pay"/>
                        </button>

                </c:if>
</div>

                                        <div class="modal fade" id=modalPayment tabindex="-1">
                                          <div class="modal-dialog">
                                            <div class="modal-content">
                                              <div class="modal-header">
                                                <h5 class="modal-title"><fmt:message key="pay_invoice.modal.title"/> #${order.id}</h5>
                                                <button type="button" class="close" data-dismiss="modal">
                                                  <span>&times;</span>
                                                </button>
                                              </div>
                                              <div class="modal-body">
                                                    <!-- CREDIT CARD FORM STARTS HERE -->
                                                                <div class="panel panel-default credit-card-box">
                                                                    <div class="panel-heading display-table" >
                                                                        <div class="row display-tr" >
                                                                            <h3 class="panel-title display-td" >Payment Details</h3>
                                                                            <div class="display-td" >
                                                                                <img class="img-responsive pull-right" src="http://i76.imgup.net/accepted_c22e0.png">
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="panel-body">
                                                                        <form role="form" id="payment-form" method="POST" action="controller">
                                                                            <div class="row">
                                                                                <div class="col-xs-12">
                                                                                    <div class="form-group">
                                                                                        <label for="cardNumber">CARD NUMBER</label>
                                                                                        <div class="input-group">
                                                                                            <input
                                                                                                type="tel"
                                                                                                class="form-control"
                                                                                                name="cardNumber"
                                                                                                placeholder="Valid Card Number"
                                                                                                autocomplete="cc-number"
                                                                                                required autofocus
                                                                                            />
                                                                                            <span class="input-group-addon"><i class="fa fa-credit-card"></i></span>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                            <div class="row">
                                                                                <div class="col-xs-7 col-md-7">
                                                                                    <div class="form-group">
                                                                                        <label for="cardExpiry"><span class="hidden-xs">EXPIRATION</span><span class="visible-xs-inline">EXP</span> DATE</label>
                                                                                        <input
                                                                                            type="tel"
                                                                                            class="form-control"
                                                                                            name="cardExpiry"
                                                                                            placeholder="MM / YY"
                                                                                            autocomplete="cc-exp"
                                                                                            required
                                                                                        />
                                                                                    </div>
                                                                                </div>
                                                                                <div class="col-xs-5 col-md-5 pull-right">
                                                                                    <div class="form-group">
                                                                                        <label for="cardCVC">CV CODE</label>
                                                                                        <input
                                                                                            type="tel"
                                                                                            class="form-control"
                                                                                            name="cardCVC"
                                                                                            placeholder="CVC"
                                                                                            autocomplete="cc-csc"
                                                                                            required
                                                                                        />
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                            <div class="row">
                                                                                <div class="col-xs-12">
                                                                                    <div class="form-group">
                                                                                        <label for="couponCode">COUPON CODE</label>
                                                                                        <input type="text" class="form-control" name="couponCode" />
                                                                                    </div>
                                                                                </div>
                                                                            </div>

                                                                            <div class="row">
                                                                                <div class="col-xs-12">
                                                                                            <input type="hidden" name="command" value="pay">
                                                                                             <input type="hidden" name="order_id" value="${order.id}">
                                                                                             <button class="btn btn-primary" onclick='submit()'>
                                                                                                  <i class="fa fa-credit-card"></i>&nbsp;&nbsp;<fmt:message key="invoice.button.pay"/>
                                                                                            </button>

                                                                                </div>
                                                                            </div>

                                                                            <div class="row" style="display:none;">
                                                                                <div class="col-xs-12">
                                                                                    <p class="payment-errors"></p>
                                                                                </div>
                                                                            </div>
                                                                        </form>
                                                                    </div>
                                                                </div>
                                                                <!-- CREDIT CARD FORM ENDS HERE -->
                                              </div>
                                              <div class="modal-footer">

                                              </div>
                                            </div>
                                          </div>
                                        </div>

<style>
#inventory-invoice{
    padding: 30px;
}
#inventory-invoice a{text-decoration:none ! important;}
.invoice {
    position: relative;
    background-color: #FFF;
    min-height: 680px;
    padding: 15px
}

.invoice header {
    padding: 10px 0;
    margin-bottom: 20px;
    border-bottom: 1px solid #3989c6
}

.invoice .company-details {
    text-align: right
}

.invoice .company-details .name {
    margin-top: 0;
    margin-bottom: 0
}

.invoice .contacts {
    margin-bottom: 20px
}

.invoice .invoice-to {
    text-align: left
}

.invoice .invoice-to .to {
    margin-top: 0;
    margin-bottom: 0
}

.invoice .invoice-details {
    text-align: right
}

.invoice .invoice-details .invoice-id {
    margin-top: 0;
    color: #3989c6
}

.invoice main {
    padding-bottom: 50px
}

.invoice main .thanks {
    margin-top: -100px;
    font-size: 2em;
    margin-bottom: 50px
}

.invoice main .notices {
    padding-left: 6px;
    border-left: 6px solid #3989c6
}

.invoice main .notices .notice {
    font-size: 1.2em
}

.invoice table {
    width: 100%;
    border-collapse: collapse;
    border-spacing: 0;
    margin-bottom: 20px
}

.invoice table td,.invoice table th {
    padding: 15px;
    background: #eee;
    border-bottom: 1px solid #fff
}

.invoice table th{
    white-space: nowrap;
    font-weight: 400;
    font-size: 16px;
    border:1px solid #fff;
}
.invoice table td{
    border:1px solid #fff;
}
.invoice table td h3 {
    margin: 0;
    font-weight: 400;
    color: #3989c6;
    font-size: 1.2em
}

.invoice table .tax,.invoice table .total,.invoice table .unit {
    text-align: right;
    font-size: 1.2em
}

.invoice table .no {
    color: #fff;
    font-size: 1.6em;
    background: #17a2b8
}

.invoice table .unit {
    background: #ddd
}

.invoice table .total {
    background: #17a2b8;
    color: #fff
}

.invoice table tfoot td {
    background: 0 0;
    border-bottom: none;
    white-space: nowrap;
    text-align: right;
    padding: 10px 20px;
    font-size: 1.2em;
    border-top: 1px solid #aaa
}

.invoice table tfoot tr:first-child td {
    border-top: none
}

.invoice table tfoot tr:last-child td {
    color: #3989c6;
    font-size: 1.4em;
    border-top: 1px solid #3989c6
}

.invoice table tfoot tr td:first-child {
    border: none
}

.invoice footer {
    width: 100%;
    text-align: center;
    color: #777;
    border-top: 1px solid #aaa;
    padding: 8px 0
}

@media print {
    .invoice {
        font-size: 11px!important;
        overflow: hidden!important
    }

    .invoice footer {
        position: absolute;
        bottom: 10px;
        page-break-after: always
    }

    .invoice>div:last-child {
        page-break-before: always
    }
}

/* Padding - just for asthetics on Bootsnipp.com */
body { margin-top:20px; }

/* CSS for Credit Card Payment form */
.credit-card-box .panel-title {
    display: inline;
    font-weight: bold;
}
.credit-card-box .form-control.error {
    border-color: red;
    outline: 0;
    box-shadow: inset 0 1px 1px rgba(0,0,0,0.075),0 0 8px rgba(255,0,0,0.6);
}
.credit-card-box label.error {
  font-weight: bold;
  color: red;
  padding: 2px 8px;
  margin-top: 2px;
}
.credit-card-box .payment-errors {
  font-weight: bold;
  color: red;
  padding: 2px 8px;
  margin-top: 2px;
}
.credit-card-box label {
    display: block;
}
/* The old "center div vertically" hack */
.credit-card-box .display-table {
    display: table;
}
.credit-card-box .display-tr {
    display: table-row;
}
.credit-card-box .display-td {
    display: table-cell;
    vertical-align: middle;
    width: 50%;
}
/* Just looks nicer */
.credit-card-box .panel-heading img {
    min-width: 180px;
}

</style>

<script>
 $('#printInvoice').click(function(){
            Popup($('.invoice')[0].outerHTML);
            function Popup(data)
            {
                window.print();
                return true;
            }
        });
 </script>

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>
