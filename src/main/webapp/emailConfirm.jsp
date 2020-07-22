<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="resources/fontawesome-free-5.12.1-web/css/all.css">
    <link rel="stylesheet" href="resources/css/style.css"/>


</head>
<body>
<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="local/local" var="loc"/>
<fmt:message bundle="${loc}" key="account.confirm.message.success" var="confirm_message_succes"/>
<fmt:message bundle="${loc}" key="account.confirm.message.invalid" var="confirm_message_invalid"/>
<fmt:message bundle="${loc}" key="account.confirm.button.to_site" var="confirm_button_to_site"/>


<div class="container-fluid contentCenter" style="padding-top: 70px">

    <div class="confirmEmail" style="margin: 0 auto; padding-top: 20px">

        <h6>
            <c:if test="${not empty requestScope.invalidData}">
                ${confirm_message_invalid}
            </c:if>
            <c:if test="${not empty requestScope.success}">
                ${confirm_message_succes} </c:if>
        </h6>
        <div class="form-group">
            <div class="row m-0">
                <div class="p-top-7 text-center" style="width: 100%; text-align: center">
                    <a href="${pageContext.request.contextPath}/test?command=main_page">
                        <button type="submit"
                                class="btn btn-outline-primary text-button-start-form">${confirm_button_to_site}
                        </button>
                    </a>
                </div>
            </div>
        </div>

    </div>


</div>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
        integrity="" crossorigin="anonymous"></script>
<script src="resources/js/bootstrap.min.js.map"
        integrity="" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="" crossorigin="anonymous"></script>

</body>

</html>