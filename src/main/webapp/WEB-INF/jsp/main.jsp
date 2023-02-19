<%@ include file="/WEB-INF/jspf/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/page.jspf" %>

<c:set var="current" value="main"/>

<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="sidenav">

     <div class="col-xs-15 col-md-12">
         <div class="login-main-text">
            <h2>ProkatAUTO</h2>
            <p><fmt:message key="main.label.LoginRegister"/></p>
         </div>
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
                        <button class="btn btn-primary" onclick='submit()'>
                                     <i class="fa fa-sign-in-alt"></i>&nbsp;&nbsp;<fmt:message key="all.label.login"/>
                               </button>

                        <input type="button" class="btn btn-secondary" id="registerbutton" value="<fmt:message key="all.button.register"/>"
                               onclick='location.href="controller?command=register&loginvalue="+login.value+""' />


                  </div>

               </form>
            </div>
         </div>
    </div>

    <div class="container cta-150" style="width: 1600px">
            <div class="container" style="width: 1600px">
              <div class="row blog">
                <div class="col-md-20 col-sm-20">
                  <div id="blogCarousel" class="carousel slide container-blog" data-ride="carousel">
                    <ol class="carousel-indicators">
                      <li data-target="#blogCarousel" data-slide-to="0" class="active"></li>
                      <li data-target="#blogCarousel" data-slide-to="1"></li>
                    </ol>

                    <!-- Carousel items -->
                    <div class="carousel-inner">
                      <div class="carousel-item active">

                      <div class="row">

                          <c:forEach var="i" begin="0" end="4">
                          <c:set var="car" value="${cars[i]}"/>

                          <div class="col-md-4" >
                            <div class="item-box-blog">
                              <div class="item-box-blog-image">
                                <!--Price-->
                                <div class="item-box-blog-date bg-blue-ui white"> <span class="mon">${car.price} $</span> </div>
                                <!--Image-->
                                <figure> <img alt="" src="data:image/jpg;base64,${car.base64Image}"> </figure>
                              </div>
                              <div class="item-box-blog-body">
                                <!--Heading-->
                                <div class="item-box-blog-heading">
                                  <a href="#" tabindex="0">
                                    <h5>${car.brand}</h5>
                                  </a>
                                </div>

                                <!--Data-->
                                <div class="item-box-blog-data" style="padding: px 15px;">
                                </div>

                                <!--Text-->
                                <div class="item-box-blog-text">
                                  <p>${car.model} ${car.car_number}</p>
                                </div>

                                <c:if test="${user==null}">
                                     <label><fmt:message key="main.label.LoginRegisterToOrder"/></label>
                                </c:if>

                                <c:if test="${user!=null}">
                                      <input type="button" class="btn btn-secondary" id="lookmorebutton" value="<fmt:message key="main.button.look_more_and_order"/>"
                                                                                   onclick='location.href="controller?command=car&car_id="+${car.id}+""' />
                                </c:if>
                                <!--See More Button-->

                              </div>
                            </div>
                          </div>

                          </c:forEach>

                        </div>
                        <!--.row-->


                      </div>
                      <!--.item-->

                      <div class="carousel-item ">
                        <div class="row">

                          <c:forEach var="i" begin="5" end="9">
                          <c:set var="car" value="${cars[i]}"/>

                                <div class="col-md-4" >
                                      <div class="item-box-blog">
                                        <div class="item-box-blog-image">
                                          <!--Price-->
                                          <div class="item-box-blog-date bg-blue-ui white"> <span class="mon">${car.price} $</span> </div>
                                          <!--Image-->
                                          <figure> <img alt="" src="data:image/jpg;base64,${car.base64Image}"> </figure>
                                        </div>
                                        <div class="item-box-blog-body">
                                          <!--Heading-->
                                          <div class="item-box-blog-heading">
                                            <a href="#" tabindex="0">
                                              <h5>${car.brand}</h5>
                                            </a>
                                          </div>

                                          <!--Data-->
                                          <div class="item-box-blog-data" style="padding: px 15px;">
                                          </div>

                                          <!--Text-->
                                          <div class="item-box-blog-text">
                                            <p>${car.model} ${car.car_number}</p>
                                          </div>

                                          <c:if test="${user==null}">
                                            <label><fmt:message key="main.label.LoginRegisterToOrder"/></label>
                                          </c:if>

                                          <c:if test="${user!=null}">
                                            <input type="button" class="btn btn-secondary" id="lookmorebutton" value="<fmt:message key="main.button.look_more_and_order"/>"
                                                                         onclick='location.href="controller?command=car&car_id="+${car.id}+""' />
                                          </c:if>

                                          <!--See More Button-->

                                        </div>
                                      </div>
                                    </div>

                          </c:forEach>

                        </div>
                        <!--.row-->
                      </div>
                      <!--.item-->
                    </div>
                    <!--.carousel-inner-->
                  </div>
                  <!--.Carousel-->
                </div>
              </div>
            </div>
          </div>



<style>

.cta-150 {
  margin-top: 30px;
  padding-left: 8%;
  padding-top: 7%;
}
.col-md-4{
    padding-bottom:20px;
    height: 400px;
}

.white {
  color: #fff !important;
}
.mt{float: left;margin-top: -20px;padding-top: 20px;}
.bg-blue-ui {
  background-color: #708198 !important;
}

figure img{width:300px; height:240px}

#blogCarousel {
  padding-bottom: 100px;
}

.blog .carousel-indicators {
  left: 0;
  top: -50px;
  height: 50%;
}


/* The colour of the indicators */

.blog .carousel-indicators li {
  background: #708198;
  border-radius: 50%;
  width: 8px;
  height: 8px;
}

.blog .carousel-indicators .active {
  background: #0fc9af;
}




.item-carousel-blog-block {
  outline: medium none;
  padding: 15px;
}

.item-box-blog {
  border: 1px solid #dadada;
  text-align: center;
  z-index: 4;
  padding: 20px;
}

.item-box-blog-image {
  position: relative;
}

.item-box-blog-image figure img {
  width: 100%;
  height: auto;
}

.item-box-blog-date {
  position: absolute;
  z-index: 5;
  padding: 4px 20px;
  top: -20px;
  right: 8px;
  background-color: #41cb52;
}

.item-box-blog-date span {
  color: #fff;
  display: block;
  text-align: center;
  line-height: 1.2;
}

.item-box-blog-date span.mon {
  font-size: 18px;
}

.item-box-blog-date span.day {
  font-size: 16px;
}

.item-box-blog-body {
  padding: 10px;
}

.item-heading-blog a h5 {
  margin: 0;
  line-height: 1;
  text-decoration:none;
  transition: color 0.3s;
}

.item-box-blog-heading a {
    text-decoration: none;
}

.item-box-blog-data p {
  font-size: 13px;
}

.item-box-blog-data p i {
  font-size: 12px;
}

.item-box-blog-text {
  max-height: 100px;
  overflow: hidden;
}

.mt-10 {
  float: left;
  margin-top: -10px;
  padding-top: 10px;
}

.btn.bg-blue-ui.white.read {
  cursor: pointer;
  padding: 4px 20px;
  float: left;
  margin-top: 10px;
}

.btn.bg-blue-ui.white.read:hover {
  box-shadow: 0px 5px 15px inset #4d5f77;
}

</style>

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>