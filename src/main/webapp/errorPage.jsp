<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="resources/css/style.css"/>
    <link rel="stylesheet" href="resources/fontawesome-free-5.12.1-web/css/all.css">
</head>
<body>
<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="local/local" var="loc"/>
<fmt:message bundle="${loc}" key="error.page.message_fix" var="message_fix"/>
<fmt:message bundle="${loc}" key="error.page.message_sorry" var="message_sorry"/>
<fmt:message bundle="${loc}" key="error.page.message_try_later" var="message_try_later"/>




<div class="container-fluid colorForPage height-100 p-0">

    <jsp:include page="WEB-INF/jsp/parts/nav-menu.jsp"/>

    <div class="row errorPagePicture m-t-27" >
        <img src="resources/img/error.png" style="width: 300px; height: 150px">

    </div>
    <div class="row errorPageText">
        <h5>${message_sorry}</h5>
        <h6>${message_fix}</h6>
        <h6>${message_try_later}</h6>
    </div>
</div>


</body>
</html>
