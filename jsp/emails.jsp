<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<h2>Email List</h2>
<div class="email-list">
    	<c:forEach var="emailTO" items="${emails}" varStatus="emailStatus">
    		<div><a href="/notifier/application?action=deleteEmail&emailID=${emailTO.id}"><img src="images/delete.png" alt="Delete" title="Delete"/></a>  ${emailTO.email}</div>
    	</c:forEach>
    	<c:if test="${empty emails}">
    		No Emails added Yet.
    	</c:if>
</div>
<br/>
<form class="form-email" action="/notifier/application?action=saveEmail" method="POST">
<input type="email" name="newEmail" class="form-control" placeholder="Add new Email"/>
	<button class="btn btn-md btn-primary btn-block" type="submit">Save</button>
</form>
<input type="hidden" id="allEmails" value="${allEmails}"/>