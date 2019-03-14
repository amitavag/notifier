<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
   <head lang="en">
      <meta charset="UTF-8" />
      <title>Notifier</title>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
      <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
      <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
      <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
      <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
      <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
      <script src="js/application.js"></script>
      <link rel="stylesheet" href="css/mystyle.css">
      <script>
      </script>
   </head>
   <body>
      <div class="container">
      	<div class="header clearfix">
      		<h1><img src="images/email.png"/>Notifier</h1>
	      </div>
         <div class="lead">
         	<p>This is used for setting up various types of notifications.</p>
         	<c:if test="${loginStatus eq 'Y'}">
         		<a href="/notifier/application?action=logOut" class="btn btn-primary btn-sm">Log Out</a>
         	</c:if>
         </div>
        
         
         <c:choose>
         	<c:when test="${loginStatus eq 'N'}">
         		 <div class="jumbotron">
		         	<form class="form-signin" action="/notifier/application?action=login" method="POST">
				        <h2 class="form-signin-heading">Please sign in</h2>
				        <label for="inputEmail" class="sr-only">User Name</label>
				        <input type="text" id="inputEmail" name="inputEmail" class="form-control" placeholder="Email address" required="" autofocus="">
				        <label for="inputPassword" class="sr-only">Password</label>
				        <input type="password" id="inputPassword" name="inputPassword" class="form-control" placeholder="Password" required="">
				        <br/>
				        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
				    </form>
		         </div>
         	</c:when>
         	<c:otherwise>
         		<div class="jumbotron">
         			<div class="row">
         				<div class="col-lg-4">
         					<c:import url="emails.jsp"></c:import>
				        </div>
				        <div class="col-lg-8">
         					<c:import url="notification.jsp"></c:import>
				        </div>
         			</div>
         		</div>
         	</c:otherwise>
         </c:choose>
        
         
         
      </div>
   </body>
</html>