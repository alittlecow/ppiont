<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>
<html>
<head>
    <title>点点的人大才女</title>
</head>
<body background="img/slide_1.jpg">
<font color="#4169e1">welcome to the test page!</font><br/>
<c:set var="id" scope="request" value="${test.id}"/>
<c:if test="${not empty test.id}">
    success
</c:if>
<c:if test="${empty test.id}">
    fail
</c:if>
</body>
</html>
