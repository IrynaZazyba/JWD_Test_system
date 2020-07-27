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

<fmt:message bundle="${loc}" key="editTest.tab.label.test" var="label_test"/>
<fmt:message bundle="${loc}" key="editTest.tab.label.question" var="label_question"/>
<fmt:message bundle="${loc}" key="editTest.message.error" var="message_error_save"/>
<fmt:message bundle="${loc}" key="editTest.message.success" var="message_success_save"/>

<fmt:message bundle="${loc}" key="editTest.label.testTitle" var="label_testTitle"/>
<fmt:message bundle="${loc}" key="editTest.label.testKey" var="label_testKey"/>
<fmt:message bundle="${loc}" key="editTest.label.duration" var="label_duration"/>
<fmt:message bundle="${loc}" key="editTest.label.testType" var="label_testType"/>
<fmt:message bundle="${loc}" key="editTest.message.invalid.key" var="message_invalid_key"/>
<fmt:message bundle="${loc}" key="editTest.message.invalid.testTitle" var="message_invalid_testTitle"/>
<fmt:message bundle="${loc}" key="editTest.message.key.conditions" var="message_key_conditions"/>
<fmt:message bundle="${loc}" key="editTest.message.testTitle.conditions" var="message_testTitle_conditions"/>
<fmt:message bundle="${loc}" key="editTest.message.invalid.time" var="message_invalid_time"/>
<fmt:message bundle="${loc}" key="editTest.button.save" var="button_save"/>
<fmt:message bundle="${loc}" key="editTest.button.toTestList" var="button_to_tests"/>
<fmt:message bundle="${loc}" key="editTest.button.add.question" var="button_addQuestion"/>
<fmt:message bundle="${loc}" key="editTest.button.add.answer" var="button_add_answer"/>
<fmt:message bundle="${loc}" key="editTest.button.save.question" var="button_save_question"/>
<fmt:message bundle="${loc}" key="editTest.checkbox.conditions" var="message_checkbox"/>
<fmt:message bundle="${loc}" key="editTest.button.continue" var="button_continue"/>
<fmt:message bundle="${loc}" key="editTest.mode.add.test" var="mode_add_test"/>


<div class="container-fluid ">


    <nav class="navbar navbar-expand-lg navbar-light menu-color p-t-b-0 border-menu">
        <div class="collapse navbar-collapse start-page-nav-itm max-w-nav" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto start-page-nav-itm" id="myTab">
                <li class="nav-item">
                    <img alt="logo" class="logo-size" src="resources/img/logo.png">
                    </a>
                </li>
            </ul>
            <ul class="navbar-nav mr-auto start-page-nav-itm" id="editPageMessage">
                <li class="nav-item p-t-15">
                   <h5> ${mode_add_test}</h5> </li>
            </ul>
        </div>

        <form action="test" method="POST" class="m-0">
            <input type="hidden" name="command" value="change_language"/>
            <input type="hidden" name="local" value="ru"/>
            <button type="submit" class="btn ru"></button>
        </form>

        <form action="test" method="POST" class="m-0">
            <input type="hidden" name="command" value="change_language"/>
            <input type="hidden" name="local" value="en"/>
            <button type="submit" class="btn en"></button>
        </form>
    </nav>

    <div class="row">
        <div class="col-5" style="margin: 0 auto;">
            <div id="addedQuestions" class="alert alert-info" role="alert" style="display: none;">
            </div>
            <div id="testId">
            </div>
            <div class="tab-content p-t-27" id="myTabContent">
                <div class="tab-pane fade show active" id="testEditForm" role="tabpanel" aria-labelledby="home-tab">

                    <div id="successCreatedTest" class="alert alert-success" role="alert" style="display: none;">
                        ${message_success_save} </div>

                    <div id="dangerCreatedTest" class="alert alert-danger" role="alert" style="display: none;">
                        ${message_error_save} </div>

                    <form id="addTestForm" onsubmit="saveTestInfo(this);return false;" enctype="multipart/form-data"
                          accept-charset="UTF-8" class="key-form p-t-27" role="form">
                        <div class="form-group">
                            <label for="testType">${label_testType}</label>
                            <select required class="form-control" name="typeId" id="testType">
                                <c:forEach var="type" items="${requestScope.testTypes}">
                                    <c:if test="${not empty sessionScope.typeId}">
                                        <c:if test="${sessionScope.typeId==type.id}">
                                            <option selected value="${type.id}">${type.title}</option>
                                        </c:if>
                                        <c:if test="${sessionScope.typeId!=type.id}">
                                            <option value="${type.id}">${type.title}</option>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${empty sessionScope.typeId}">
                                        <option value="${type.id}">${type.title}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="testTitle">${label_testTitle}</label>
                            <c:if test="${not empty sessionScope.testTitle}">
                                <input required type="text" class="form-control" name="testTitle"
                                       value="${sessionScope.testTitle}" id="testTitle">
                            </c:if>
                            <c:if test="${empty sessionScope.testTitle}">
                                <input required type="text" class="form-control" name="testTitle" id="testTitle">
                            </c:if>
                            <small id="testTitleHelpInline" class="text-muted">
                                ${message_testTitle_conditions} </small>
                            <div class="invalid-feedback">
                                ${message_invalid_testTitle} </div>
                        </div>
                        <div class="form-group">
                            <label for="keyValue">${label_testKey}</label>
                            <c:if test="${not empty sessionScope.testTitle}">
                                <input type="text" class="form-control" name="testKey" value="${sessionScope.testKey}"
                                       id="keyValue">
                            </c:if>
                            <c:if test="${empty sessionScope.testTitle}">
                                <input type="text" class="form-control" name="testKey" id="keyValue">
                            </c:if>
                            <small id="keyHelpInline" class="text-muted">
                                ${message_key_conditions} </small>
                            <div class="invalid-feedback">
                                ${message_invalid_key} </div>
                        </div>
                        <div class="form-group">
                            <label for="duration">${label_duration}</label>
                            <c:if test="${not empty sessionScope.testTitle}">
                                <input required type="time" class="form-control" name="testDuration"
                                       value="${sessionScope.testDuration}" id="duration">
                            </c:if>
                            <c:if test="${empty sessionScope.testTitle}">
                                <input required type="time" class="form-control" name="testDuration" id="duration">
                            </c:if>
                            <div class="invalid-feedback">
                                ${message_invalid_time} </div>
                        </div>
                        <div class="form-group m-t-15">
                            <a href="${pageContext.request.contextPath}/test?command=show_admin_panel">
                                <button type="button" id="continueLater" class="btn btn-outline-info">
                                    ${button_to_tests}</button>
                            </a>
                            <button type="submit"
                                    class="btn btn-outline-info">${button_continue}
                            </button>
                        </div>

                    </form>
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
<script src="resources/js/addEditTest.js"></script>
<script src="resources/js/script.js"></script>

</body>

</html>