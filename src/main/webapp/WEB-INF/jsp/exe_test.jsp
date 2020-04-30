<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>

<html>
<head>

    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <title>Welcome</title>

    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="resources/css/style.css"/>
    <link rel="stylesheet" href="resources/fontawesome-free-5.12.1-web/css/all.css">
    <link rel="stylesheet" type="text/css" href="resources/css/test-card.css">
    <link rel="stylesheet" type="text/css" href="resources/css/exe-card.css">

</head>

<body>
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
                <div class="ribbon-type ribbon-top-asgmt-left"><span>${requestScope.title}</span></div>
                <div class="ribbon ribbon-top-type-left"><span>ribbon</span></div>

            </div>
            <div class="card-body mb-2 card-test">

                <div id="conditions" class="text-ctr">
                    <h3>На выполнение теста отведено ${requestScope.duration} минут. Количество
                        вопросов ${requestScope.count_question}</h3></div>

                <c:if test="${not empty requestScope.key}">

                    <form onsubmit="getQuestion(); return false" id="exeTest" enctype="multipart/form-data"
                          accept-charset="UTF-8" class="key-form" role="form">
                        <input type="hidden" name="command" value="save_answer"/>
                        <input type="hidden" id="test_id" name="test_id" value="${requestScope.test_id}"/>

                        <div id="key" class="text-ctr">

                            <div class="form-group">
                                <div class="row justify-content-center">

                                    <label class="col-sm-3 control-label p-0">Введите ключ</label>
                                    <div class="col-sm-3">
                                        <input id="key_value" type="text" class="form-control" required
                                               name="key">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="row justify-content-center">
                                    <div class="col-sm-offset-2 col-sm-10 p-top-95">
                                        <button type="submit" class="card-exe-btn btn btn-outline-primary">Запустить
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="quest" style="visibility: hidden">
                            <button type="submit" class="card-exe-btn btn btn-right btn-outline-primary">Next</button>
                        </div>

                    </form>

                    <div id="complete" style="visibility: hidden">

                    </div>
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
<script src="resources/js/script.js"></script>


</body>
</html>