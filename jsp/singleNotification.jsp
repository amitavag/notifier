<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div class="single-notification">
	<form action="/notifier/application?action=saveNotification" method="POST" id="myform_${emailDataTO.id}">
		<input type="hidden" name="emailDataID" value="${emailDataTO.id}"/>
		<div class="row">
			<div class="col-lg-5">
				<input type="text" name="title" class="form-control" placeholder="Enter Title" value="${emailDataTO.title}">
		        <input type="text" name="date" class="form-control date-field" placeholder="Expiry Date" value="${emailDataTO.date}">
		        <input type="text" value="${emailDataTO.emailTo}" name="emailTo" class="form-control" placeholder="Enter Email IDs (comma separated)" id="emailTo_${emailDataTO.id}">
		        <input type="checkbox" class="form-check-input" id="allEmails_${emailDataTO.id}" name="allEmails_${emailDataTO.id}" onClick="selectAllEmails('${emailDataTO.id}')">
    				<label class="form-check-label" for="allEmails_${emailDataTO.id}">All Emails</label>
	        </div>
	        <div class="col-lg-6">
	        		<input type="text" name="subject" class="form-control" placeholder="Email Subject" value="${emailDataTO.subject}">
	        		<textarea rows="5" cols="5" name="body" class="form-control" placeholder="Email Body">${emailDataTO.body}</textarea>
	        </div>
		</div>
		<c:choose>
			<c:when test="${empty emailDataTO.id}">
				<div class="multi-btn">
					<button class="btn btn-md btn-primary" type="submit">Save</button>
				</div>
			</c:when>
			<c:otherwise>
				<div class="multi-btn">
					<button class="btn btn-md btn-primary" type="submit">Update</button>
					<button class="btn btn-md btn-primary" type="submit" onClick="deleteItem('${emailDataTO.id}')">Delete</button>
				</div>
				<div>
					<label>API URL: ${serverUrl}?action=invokeNotification&timeSpan=[90,30,20]&id=${emailDataTO.id}</span></label>
				</div>
			</c:otherwise>
		</c:choose>	
	</form>
</div>