<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<h2>Notification Setup</h2>
<div class="email-list notification-list">
	<ul>
		<c:forEach var="emailDataTO" items="${emailData}" varStatus="emailDataTOStatus">
    			<li onClick="loadSection('${emailDataTO.id}')">${emailDataTO.title} (${emailDataTO.date})</li>
    			<div class="none item-list" id="selection_${emailDataTO.id}">
    				<c:set var="emailDataTO" value="${emailDataTO}" scope="request"/>
    				<c:import url="singleNotification.jsp"></c:import>
    			</div>
	    	</c:forEach>
	    	<li onClick="loadSection('')">Add New Notification</li>
	    	<div class="none item-list" id="selection_">
	    		<c:set var="emailDataTO" value="${null}" scope="request"/>
	    		<c:import url="singleNotification.jsp"></c:import>
	    	</div>
    	</ul>
</div>