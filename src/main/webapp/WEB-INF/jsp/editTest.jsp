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
        <input type="hidden" name="testId" id="testId" value="${requestScope.testData.id}"/>
        <div class="col-3"></div>
        <div class="col-5">
            <form id="editTest">
                <div id="testInfo" class="edit-quest-form">
                    <div class="row edit-button">
                    <button onclick="showModalWindowEditTestInfo(this); return false;" type="button"
                            value="${requestScope.testData.id}"
                            class="btn btn-link"><i class="far fa-edit"></i></button></div>
                    <div class="form-group">
                        <label for="testTitle">Название теста</label>
                        <input disabled type="email" class="form-control" name="testTitle" value="${requestScope.testData.title}" id="testTitle">
                    </div>
                    <div class="form-group">
                        <label for="testType">Тип теста</label>
                        <select disabled class="form-control" name="typeId" id="testType">
                            <c:forEach var="type" items="${requestScope.testTypes}">
                                <c:if test="${requestScope.testData.type.id==type.id}">
                                    <option  selected value="${type.id}">${type.title}</option>
                                </c:if>
                                <c:if test="${requestScope.testData.type.id!=type.id}">
                                    <option  value="${type.id}">${type.title}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="testKey">Ключ</label>
                        <input disabled type="email" class="form-control" name="testKey" value="${requestScope.testData.key}" id="testKey">
                    </div>
                    <div class="form-group">
                        <label for="testDuration">Продолжительность, мин</label>
                        <input disabled type="email" class="form-control" name="testDuration" value="${requestScope.testData.duration}"
                               id="testDuration">
                    </div>
                </div>
                <div class="form-group">
                    <button type="button" onclick="showModalWindowAddQuestion();return false;"
                            class="btn btn-outline-info btn-block m-t-27">
                        <i class="fas fa-plus"></i></button>

                </div>
                <c:forEach var="quest" items="${requestScope.testData.questions}">
                    <div id="modal-${quest.id}" class="form-group edit-quest-form">
                        <div class="row edit-button">
                            <button onclick="showModalWindowEditQuestion(this); return false;" type="button"
                                    value="${quest.id}"
                                    class="btn btn-link"><i class="far fa-edit"></i></button>
                            <button onclick="deleteQuestion(this); return false;" type="button" value="${quest.id}"
                                    class="btn btn-link"><i
                                    class="far fa-trash-alt "></i>
                            </button>
                        </div>
                        <div class="row">
                            <div class="col-10 p-0">
                                <textarea disabled class="form-control" name="question-${quest.id}"
                                          id="question" rows="3">${quest.question}</textarea>
                            </div>

                        </div>
                        <c:forEach var="answer" items="${quest.answers}">
                            <c:if test="${answer.result eq true}">
                                <div class="row m-t-7 answer">
                                    <div class="col-10 p-0">
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text">
                                                    <input checked disabled type="checkbox" name="check-${answer.id}"
                                                           aria-label="Checkbox for following text input">
                                                </div>
                                            </div>
                                            <input id="${answer.id}" name="answer-${answer.id}" disabled type="text"
                                                   value="${answer.answer}" class="form-control"
                                                   aria-label="Text input with checkbox">
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${answer.result eq false}">
                                <div class="row m-t-7 answer">
                                    <div class="col-10 p-0">
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text">
                                                    <input disabled type="checkbox" name="check-${answer.id}"
                                                           aria-label="Checkbox for following text input">
                                                </div>
                                            </div>
                                            <input id="${answer.id}" name="answer-${answer.id}" disabled type="text"
                                                   value="${answer.answer}" class="form-control"
                                                   aria-label="Text input with checkbox">
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </c:forEach>
                <div class="form-group m-t-15">
                    <button type="button" onclick="completeTestCreating()" id="preview" class="btn btn-outline-info">
                        Завершить создание теста
                    </button>
                </div>
            </form>
        </div>
        <div class="col-3"></div>
    </div>
</div>


<div id="modal" class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Edit</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span class="closeButton" aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form class="questionFormEdit">

                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-secondary closeButton" data-dismiss="modal">Close</button>
                <button onclick="updateQuestion(this)" type="button" class="btn btn-info">Сохранить</button>
            </div>
        </div>
    </div>
</div>

<div id="modalTestInfo" class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Edit</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span class="closeButton" aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="testInfoFormEdit">

                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-secondary closeButton" data-dismiss="modal">Close</button>
                <button onclick="updateTestInfo(this)" type="button" class="btn btn-info">Сохранить</button>
            </div>
        </div>
    </div>
</div>

<div id="modalAddQuestion" class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Edit</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span class="closeButton" aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form enctype="multipart/form-data" id="addQuestionModalWindowForm"
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
                </form>

            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-secondary closeButton" data-dismiss="modal">Close</button>
                <div class="form-group  float-right">
                    <button type="submit" onclick="addQuestion(this)" class="btn btn-info">Сохранить вопрос</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
        integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        crossorigin="anonymous"></script>
<script src="resources/js/bootstrap.min.js.map"
        integrity="" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="" crossorigin="anonymous"></script>
<script src="resources/js/addEditTest.js"></script>

</body>

</html>