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

<fmt:message bundle="${loc}" key="editTest.tab.lable.test" var="lable_test"/>
<fmt:message bundle="${loc}" key="editTest.tab.lable.question" var="lable_question"/>
<fmt:message bundle="${loc}" key="editTest.message.error" var="message_error_save"/>
<fmt:message bundle="${loc}" key="editTest.lable.testTitle" var="lable_testTitle"/>
<fmt:message bundle="${loc}" key="editTest.lable.testKey" var="lable_testKey"/>
<fmt:message bundle="${loc}" key="editTest.lable.duration" var="lable_duration"/>
<fmt:message bundle="${loc}" key="editTest.lable.testType" var="lable_testType"/>
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
<fmt:message bundle="${loc}" key="editTest.button.save.question" var="button_save_question"/>
<fmt:message bundle="${loc}" key="editTest.checkbox.conditions" var="message_checkbox"/>
<fmt:message bundle="${loc}" key="editTest.button.preview" var="button_preview"/>


<div class="container-fluid p-0">

    <jsp:include page="../parts/nav-menu.jsp"/>

    <div class="row">
        <div class="col-5" style="margin: 0 auto;">
            <div id="addedQuestions" class="alert alert-info" role="alert" style="display: none;">
            </div>
            <div id="testId">
            </div>
            <ul class="nav nav-tabs" id="myTab" role="tablist">
                <li class="nav-item" role="presentation">
                    <a class="nav-link active" id="testEditForm-tab" data-toggle="tab" href="#testEditForm" role="tab"
                       aria-controls="home"
                       aria-selected="true">${lable_test}</a>
                </li>
                <li class="nav-item" role="presentation">
                    <a class="nav-link disabled" id="questionEditForm-tab" data-toggle="tab" href="#questionEditForm"
                       role="tab"
                       aria-controls="profile" aria-selected="false" aria-disabled="true">${lable_question}</a>
                </li>
            </ul>
            <div class="tab-content" id="myTabContent">
                <div class="tab-pane fade show active" id="testEditForm" role="tabpanel" aria-labelledby="home-tab">

                    <div id="successCreatedTest" class="alert alert-success" role="alert" style="display: none;">
                        ${message_success_save} </div>

                    <div id="dangerCreatedTest" class="alert alert-danger" role="alert" style="display: none;">
                        ${message_error_save} </div>

                    <form id="addTestForm" onsubmit="saveTestInfo(this);return false;" enctype="multipart/form-data"
                          accept-charset="UTF-8" class="key-form" role="form">
                        <div class="form-group">
                            <label for="testType">${lable_testType}</label>
                            <select required class="form-control" name="typeId" id="testType">
                                <c:forEach var="type" items="${requestScope.testTypes}">
                                    <option value="${type.id}">${type.title}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="testTitle">${lable_testTitle}</label>
                            <input required type="text" class="form-control" name="testTitle" id="testTitle">
                            <small id="testTitleHelpInline" class="text-muted">
                                ${message_testTitle_conditions} </small>
                            <div class="invalid-feedback">
                                ${message_invalid_testTitle} </div>
                        </div>
                        <div class="form-group">
                            <label for="keyValue">${lable_testKey}</label>
                            <input type="text" class="form-control" name="testKey" id="keyValue">
                            <small id="keyHelpInline" class="text-muted">
                                ${message_key_conditions} </small>
                            <div class="invalid-feedback">
                                ${message_invalid_key} </div>
                        </div>
                        <div class="form-group">
                            <label for="duration">${lable_duration}</label>
                            <input required type="time" class="form-control" name="testDuration" id="duration">
                            <div class="invalid-feedback">
                                ${message_invalid_time} </div>
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn btn-info float-right">${button_save}</button>
                        </div>
                        <div id="buttonBack" class="form-group float-left">
                            <button onclick="window.history.back()" type="submit" class="btn btn-info">${button_to_tests}
                            </button>
                        </div>
                    </form>
                </div>
                <div class="tab-pane fade" id="questionEditForm" role="tabpanel" aria-labelledby="profile-tab">

                    <form onsubmit="saveQuestion(this);return false;" enctype="multipart/form-data"
                          accept-charset="UTF-8" class="key-form" role="form">
                        <div class="form-group">
                            <label for="quest">${button_addQuestion}</label>
                            <textarea class="form-control" id="quest" name="question" rows="3"></textarea>
                        </div>
                        <div>${message_checkbox}</div>
                        <div class="input-group mb-3">
                            <div class="input-group-prepend">
                                <div class="input-group-text">
                                    <input type="checkbox" name="check-1"
                                           aria-label="Checkbox for following text input">
                                </div>
                            </div>
                            <textarea type="text" class="form-control" name="answer-1"
                                      aria-label="Text input with checkbox"></textarea>
                        </div>
                        <div class="form-group">
                            <button onclick="addAnswerTextArea(this)" type="button" class="btn btn-link">${button_add_answer}
                            </button>
                        </div>
                        <div class="form-group  float-right">
                            <button type="submit" class="btn btn-info">${button_save_question}</button>
                        </div>
                        <div class="form-group m-t-15">
                            <button type="button" onclick="showPreviewPage()"
                                    class="btn btn-outline-info btn-lg btn-block">${button_preview}
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