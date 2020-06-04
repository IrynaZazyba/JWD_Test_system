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
    <link rel="stylesheet" href="resources/css/test-card.css"/>


</head>
<body>
<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="local/local" var="loc"/>
<fmt:message bundle="${loc}" key="button.sign_out" var="button_sign_out"/>
<fmt:message bundle="${loc}" key="nav-item.tests" var="nav_item_tests"/>
<fmt:message bundle="${loc}" key="nav-item.admin" var="nav_item_admin"/>
<fmt:message bundle="${loc}" key="nav-item.statistic" var="nav_item_statistic"/>
<fmt:message bundle="${loc}" key="nav-item.about" var="nav_item_about"/>
<fmt:message bundle="${loc}" key="nav-item.admin.tests" var="nav_item_admin_tests"/>
<fmt:message bundle="${loc}" key="nav-item.admin.users" var="nav_item_admin_users"/>
<fmt:message bundle="${loc}" key="button.language_en" var="button_language_en"/>
<fmt:message bundle="${loc}" key="button.language_ru" var="button_language_ru"/>

<div class="container-fluid p-0">

    <jsp:include page="parts/nav-menu.jsp"/>
    <div class="row">
        <div class="col-1"></div>
        <div class="col-5">
            <form>
                <div class="form-group">
                    <label for="testTitle">Название теста</label>
                    <input type="email" class="form-control" value="${requestScope.testData.title}" id="testTitle">
                </div>
                <div class="form-group">
                    <label for="testType">Example select</label>
                    <select class="form-control" id="testType">
                        <c:forEach var="type" items="${requestScope.testsTypes}">
                            <c:if test="${requestScope.testData.type.id==type.id}">
                                <option selected value="${type.id}">${type.title}</option>
                            </c:if>
                            <c:if test="${requestScope.testData.type.id!=type.id}">
                                <option value="${type.id}">${type.title}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="testKey">Ключ</label>
                    <input type="email" class="form-control" value="${requestScope.testData.key}" id="testKey">
                </div>
                <div class="form-group">
                    <label for="testDuration">Продолжительность, мин</label>
                    <input type="email" class="form-control" value="${requestScope.testData.duration}"
                           id="testDuration">
                </div>
                <c:forEach var="quest" items="${requestScope.testData.questions}">
                    <div class="form-group">
                        <label for="${requestScope.testData.id}">Вопрос</label>
                        <textarea class="form-control" id="${requestScope.testData.id}"
                                  rows="3">${quest.question}</textarea>
                    </div>
                    <c:forEach var="answer" items="${quest.answers}">
                        <c:if test="${answer.result eq true}">

                        <div class="input-group mb-3">
                            <div class="input-group-prepend">
                                <div class="input-group-text">
                                    <input type="checkbox" checked aria-label="Checkbox for following text input">
                                </div>
                            </div>
                            <input type="text" style="background-color: palegreen" class="form-control"
                                   id="${answer.id}"
                                   value="${answer.answer}"
                                   aria-label="Text input with checkbox">
                        </div>
                        </c:if>

                        <c:if test="${answer.result eq false}">
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <div class="input-group-text">
                                        <input type="checkbox" aria-label="Checkbox for following text input">
                                    </div>
                                </div>
                                <input type="text" class="form-control" id="${answer.id}" value="${answer.answer}"
                                       aria-label="Text input with checkbox">
                            </div>
                        </c:if>
                    </c:forEach>
                </c:forEach>
            </form>
        </div>
        <div class="col-3"></div>

    </div>

    <button onclick="window.history.back()">Кнопка назад</button>

</div>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
        integrity="" crossorigin="anonymous"></script>
<script src="resources/js/bootstrap.min.js.map"
        integrity="" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="" crossorigin="anonymous"></script>
<script src="resources/js/script.js"></script>

</body>

</html>