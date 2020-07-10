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

    <jsp:include page="../parts/nav-menu.jsp"/>

    <div class="row">
        <div class="col-5" style="margin: 0 auto;">
            <div id="addedQuestions" class="alert alert-info" role="alert" style="display: none;">
            </div>
            <div id="testId">
            </div>
            <ul class="nav nav-tabs" id="myTab" role="tablist">
                <li class="nav-item" role="presentation">
                    <a class="nav-link active" id="testEditForm-tab" data-toggle="tab" href="#testEditForm" role="tab" aria-controls="home"
                       aria-selected="true">Тест</a>
                </li>
                <li  class="nav-item" role="presentation">
                    <a class="nav-link disabled" id="questionEditForm-tab" data-toggle="tab" href="#questionEditForm" role="tab"
                       aria-controls="profile" aria-selected="false" aria-disabled="true" >Вопрос</a>
                </li>
            </ul>
            <div class="tab-content" id="myTabContent">
                <div class="tab-pane fade show active" id="testEditForm" role="tabpanel" aria-labelledby="home-tab">

                    <div id="successCreatedTest" class="alert alert-success" role="alert" style="display: none;">
                        Тест был успешно сохранен
                    </div>

                    <div id="dangerCreatedTest" class="alert alert-danger" role="alert" style="display: none;">
                        Произошла ошибка при сохранении данных.
                    </div>

                    <form id="addTestForm" onsubmit="saveTestInfo(this);return false;" enctype="multipart/form-data"
                          accept-charset="UTF-8" class="key-form" role="form">
                        <div class="form-group">
                            <label for="testType">Раздел</label>
                            <select required class="form-control" name="typeId" id="testType">
                                <c:forEach var="type" items="${requestScope.testTypes}">
                                <option value="${type.id}">${type.title}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="testTitle">Название</label>
                            <input required type="text" class="form-control" name="testTitle" id="testTitle">
                            <small id="testTitleHelpInline" class="text-muted">
                                Must be no longer than 20 characters.
                            </small>
                            <div class="invalid-feedback">
                                Please provide a valid test title.
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="keyValue">Ключ</label>
                            <input type="text" class="form-control" name="testKey" id="keyValue">
                            <small id="keyHelpInline" class="text-muted">
                                Must be 4-7 characters long (letters and numbers).
                            </small>
                            <div class="invalid-feedback">
                                Please provide a valid key.
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="duration">Продолжительность, чч:mm</label>
                            <input required type="time" class="form-control" name="testDuration" id="duration">
                            <div class="invalid-feedback">
                                Please enter time in required format.
                            </div>
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn btn-info float-right">Сохранить</button>
                        </div>
                        <div id="buttonBack" class="form-group float-left">
                            <button onclick="window.history.back()" type="submit" class="btn btn-info">К списку тестов</button>
                        </div>
                    </form>
                </div>
                <div class="tab-pane fade" id="questionEditForm" role="tabpanel" aria-labelledby="profile-tab">

                    <form onsubmit="saveQuestion(this);return false;" enctype="multipart/form-data"
                          accept-charset="UTF-8" class="key-form" role="form">
                        <div class="form-group">
                            <label for="quest">Добавить вопрос</label>
                            <textarea class="form-control" id="quest" name="question" rows="3"></textarea>
                        </div>
                        <div>Отметьте правильный вариант ответа(ов) чекбоксом.</div>
                        <div class="input-group mb-3">
                            <div class="input-group-prepend">
                                <div class="input-group-text">
                                    <input type="checkbox" name="check-1" aria-label="Checkbox for following text input">
                                </div>
                            </div>
                            <textarea type="text" class="form-control" name="answer-1"
                                      aria-label="Text input with checkbox"></textarea>
                        </div>
                        <div class="form-group">
                            <button onclick="addAnswerTextArea(this)" type="button" class="btn btn-link">+Добавить ответ
                            </button>
                        </div>
                        <div class="form-group  float-right">
                            <button type="submit" class="btn btn-info">Сохранить вопрос</button>
                        </div>
                        <div class="form-group m-t-15">
                            <button type="button" onclick="showPreviewPage()" class="btn btn-outline-info btn-lg btn-block">Предпросмотр</button>
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