<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register</title>
<link href='/cerioscoop-web/css/masterdetail.css' type='text/css' rel='stylesheet' />
<link href='/cerioscoop-web/css/shared.css' type='text/css' rel='stylesheet' />
</head>
<body>
 		<div><jsp:include page="/jsp/shared/navbar.jsp"></jsp:include></div>

<h1>Register</h1>

	<div>
	<form method="POST" action="/cerioscoop-web/RegisterServlet"> 
	  First name:<br>
	  <input id="firstname" type="text" name="firstname" placeholder="Enter your first name" required>
	  <br>Last name:<br>
	  <input id="lastname" type="text" name="lastname" placeholder="Enter your last name" required>
	  <br>Username:<br>
	  <input id="username" type="text" name="username" placeholder="Enter your username" required>
	  <br>Password:<br>
	  <input id="password" type="text" name="password" placeholder="Enter your password" required>
	  <br>E-mail:<br>
	  <input id="email" type="email" name="email" placeholder="Enter your email address" required>
	  <br>
	  <br><input id="submit" type="submit" name="submitit" value="Submit">
	</form> 
	</div>
	
	<jsp:include page="/jsp/shared/footer.jsp" />
</body>
</html>