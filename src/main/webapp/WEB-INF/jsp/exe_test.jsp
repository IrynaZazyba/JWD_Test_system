<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>

<html>
<head>

    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

    <title>Welcome</title>

    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="resources/css/style.css"/>
    <link rel="stylesheet" href="resources/css/timer.css"/>
    <link rel="stylesheet" href="resources/fontawesome-free-5.12.1-web/css/all.css">
    <link rel="stylesheet" type="text/css" href="resources/css/test-card.css">
    <link rel="stylesheet" type="text/css" href="resources/css/exe-card.css">
    <script src="resources/js/script.js"></script>


</head>

<body>
<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="local/local" var="loc"/>
<fmt:message bundle="${loc}" key="message.json.time_is_over" var="message_time_is_over"/>
<fmt:message bundle="${loc}" key="test.message.invalid_key" var="message_invalid_key"/>
<fmt:message bundle="${loc}" key="test.run_page.button.next" var="button_next"/>
<fmt:message bundle="${loc}" key="test.run_page.button.start" var="button_start"/>
<fmt:message bundle="${loc}" key="test.run_page.input.enter_key" var="input_enter_key"/>
<fmt:message bundle="${loc}" key="test.run_page.message.duration_test" var="message_test_duration"/>
<fmt:message bundle="${loc}" key="test.run_page.message.number_question" var="message_number_question"/>

<div class="row">
       <button onclick="window.history.back()" type="submit"
            class="card-exe-btn btn btn-right btn-outline-primary">Назад</button>
</div>
<div class="row m-0">
    <div id="timer" class="row m-0" style="visibility: hidden">
        <div id="countdown" class="countdown">
            <div class="countdown-number">
                <span class="minutes countdown-time"></span>
                <span class="countdown-text">min</span>
                <span class="seconds countdown-time"></span>
                <span class="countdown-text">sec</span>
            </div>
        </div>
    </div>
</div>

<div class="row exe-main">
    <div class="col-2"></div>
    <div class="col-7 card-main-exe">
        <div class="card-section card-section-exe border rounded">
            <div class="card-header card-header-exe rounded">
                <div class="ribbon-type ribbon-top-asgmt-left"><span>${requestScope.testInfo.title}</span></div>
                <div class="ribbon ribbon-top-type-left"><span>ribbon</span></div>

            </div>
            <div id="card-body" class="card-body mb-2 card-test">


                <c:if test="${!requestScope.testInfo.started}">
                    <div id="conditions" class="text-ctr">
                        <h3> ${requestScope.testInfo.duration} ${message_test_duration}
                                ${message_number_question} ${requestScope.testInfo.countQuestion}</h3></div>

                    <form onsubmit="getQuestion(); return false" id="exeTest" enctype="multipart/form-data"
                          accept-charset="UTF-8" class="key-form" role="form">
                        <input type="hidden" name="command" value="save_answer"/>
                        <input type="hidden" id="testId" name="test_id" value="${requestScope.testInfo.id}"/>


                        <c:if test="${not empty requestScope.testInfo.key}">

                            <div id="invalid_key" class="alert alert-danger" role="alert" style="visibility: hidden">
                                    ${message_invalid_key}</div>

                            <div id="key" class="text-ctr">

                                <div class="form-group">
                                    <div class="row justify-content-center">

                                        <label class="col-sm-3 text-ctr control-label p-0">${input_enter_key}</label>
                                        <div class="col-sm-3">
                                            <input id="key_value" type="text" class="form-control" required
                                                   name="key">
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </c:if>
                        <div id="exeButton">
                            <div class="form-group">
                                <div class="row justify-content-center">
                                    <div class="col-sm-offset-2 col-sm-10 p-top-95">
                                        <button type="submit"
                                                class="card-exe-btn btn btn-outline-primary">${button_start}
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="quest" style="visibility: hidden">
                            <button type="submit"
                                    class="card-exe-btn btn btn-right btn-outline-primary">${button_next}</button>
                        </div>

                    </form>
                    <div id="complete" style="visibility: hidden">
                        <div id="timeIsEnded" class="alert alert-danger" role="alert" style="visibility: hidden">
                                ${message_time_is_over}</div>
                    </div>

                </c:if>

                <c:if test="${requestScope.testInfo.started}">
                    <form onsubmit="getQuestion(); return false" id="exeTest" enctype="multipart/form-data"
                          accept-charset="UTF-8" class="key-form" role="form">
                        <input type="hidden" name="command" value="save_answer"/>
                        <input type="hidden" id="testId" name="test_id" value="${requestScope.testInfo.id}"/>
                        <div id="quest" style="visibility: hidden">
                            <button type="submit"
                                    class="card-exe-btn btn btn-right btn-outline-primary">${button_next}</button>
                        </div>

                    </form>

                    <div id="complete" style="visibility: hidden">
                        <div id="timeIsEnded" class="alert alert-danger" role="alert" style="visibility: hidden">
                                ${message_time_is_over}</div>
                    </div>
                    <script>let a = getQuestion();</script>
                </c:if>

            </div>
        </div>


    </div>
</div>
<div class="col-3"></div>

</div>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
        integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
        integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
        crossorigin="anonymous"></script>


</body>
</html>