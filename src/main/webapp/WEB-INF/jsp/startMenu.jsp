<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="resources/fontawesome-free-5.12.1-web/css/all.css">
    <link rel="stylesheet" href="resources/css/style.css"/>

</head>
<body>

<div class="container-fluid bor cont">
    <nav class="navbar navbar-expand-lg navbar-light bg-light" style="background-color: #e3f2fd;">
        <div class="row wight-100">
            <div class="col-2">
                <a class="navbar-brand" href="#"><h3>АСТ Тест</h3></a>
                <button class="navbar-toggler" type="button" data-toggle="collapse"
                        data-target="#navbarSupportedContent"
                        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
            </div>
            <div class="col-7">
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav mr-auto">

                        <li class="nav-item">
                            <a class="nav-link" href="#">Тесты</a>
                        </li>

                        <c:if test="${sessionScope.user_role=='ADMIN'}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    Администрирование
                                </a>
                                <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                                    <a class="dropdown-item" href="#">Редактирование пользователей</a>
                                    <div class="dropdown-divider"></div>
                                    <a class="dropdown-item" href="#">Редактирование тестов</a>
                                </div>
                            </li>
                        </c:if>

                        <li class="nav-item">
                            <a class="nav-link" href="#">Статистика</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">О нас</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="col-3">
                <div class="row">
                    <div class="col-2 p-0 icon-user">
                        <i class="far fa-address-card fa-2x color-dodgerblue"></i>
                    </div>
                    <div class="col-auto m-t-15 ">${sessionScope.user_login}
                    </div>
                    <div class="col-4">
                        <form action="test" method="POST" class="m-0">
                            <input type="hidden" name="command" value="sign_out"/>
                            <button type="submit" class="btn btn-outline-primary btn-md m-t-7">Sign out</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

    </nav>
    <div class="wrapper bor">
        <nav id="sidebar">
            <div class="sidebar-header">
                <strong></strong>
            </div>


            <div class="row p-l-15">
                <div class="col-sm-2 p-0 bor">
                    <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">

                        <c:forEach var="item" items="${tests_type}">

                        <a class="nav-link" id="v-pills-home-tab" data-toggle="pill" href="#v-pills-home"
                               role="tab"
                               aria-controls="v-pills-home" aria-selected="true">${item.title}</a>
                        </c:forEach>


                    </div>
                </div>
                <div class="col-sm-10 p-0 bor">
                    <div class="tab-content" id="v-pills-tabContent">
                        <div class="tab-pane fade show active" id="v-pills-home" role="tabpanel"
                             aria-labelledby="v-pills-home-tab">...
                        </div>
                        <div class="tab-pane fade" id="v-pills-profile" role="tabpanel"
                             aria-labelledby="v-pills-profile-tab">...
                        </div>
                        <div class="tab-pane fade" id="v-pills-messages" role="tabpanel"
                             aria-labelledby="v-pills-messages-tab">...
                        </div>
                        <div class="tab-pane fade" id="v-pills-settings" role="tabpanel"
                             aria-labelledby="v-pills-settings-tab">...
                        </div>
                    </div>
                </div>
            </div>
        </nav>
        <div id="content bor"></div>
    </div>


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