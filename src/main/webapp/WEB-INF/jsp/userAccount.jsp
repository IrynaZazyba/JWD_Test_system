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
<div class="container-fluid cont">

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
                            <a class="nav-link active"
                               href="${pageContext.request.contextPath}/test?command=show_main_page">Тесты</a>
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


            <div class="col-auto p-0 icon-user">
                <i class="far fa-address-card fa-2x color-dodgerblue"></i>
            </div>
            <div class="col-auto m-t-15">${sessionScope.user_login}
            </div>

            <div class="col-1">
                <form action="test" method="POST" class="m-0">
                    <input type="hidden" name="command" value="sign_out"/>
                    <button type="submit" class="btn btn-outline-primary btn-md m-t-7">Sign out</button>
                </form>
            </div>
        </div>

    </nav>

    <div class="row height-90">
        <div class="col-2 background-gradient height-100">
            <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">

                <a class="nav-link active" id="v-pills-test-tab" data-toggle="pill" href="#v-pills-test"
                   role="tab"
                   aria-controls="v-pills-test" aria-selected="true">Назначенные тесты</a>

                <a class="nav-link" id="v-pills-home-tab" data-toggle="pill" href="#v-pills-home"
                   role="tab"
                   aria-controls="v-pills-test" aria-selected="true">Настройки аккаунта</a>

            </div>


        </div>
        <div class="col-9">
            <div class="tab-content" id="v-pills-tabContent">
                <div class="tab-pane fade " id="v-pills-home" role="tabpanel" aria-labelledby="v-pills-home-tab">


                    <div class="row m-t-15">
                        <div class="col-sm-3 p-l-27 p-t-27">
                            <div class="media">
                                <img src="resources/img/minava.png" class="mr-3 img-size round bor" alt="ava">
                            </div>
                        </div>
                        <div class="col-sm-9 p-l-27">


                            <div class="form">
                                <form action="test" class="form-horizontal m-0" role="form" method="POST">
                                    <input type="hidden" name="command" value="edit_user"/>
                                    <div class="form-group">
                                        <div>

                                            <c:if test="${sign_up_error!=null}">
                                                <div class="alert alert-danger" role="alert">
                                                        ${sign_up_error}</div>
                                            </c:if>


                                        </div>
                                        <div class="form-group">
                                            <div class="row">

                                                <label class="col-sm-3 control-label">First name</label>
                                                <div class="col-sm-9">
                                                    <input type="text" class="form-control" required
                                                           name="first_name" value="${user_info.firstName}">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <label class="col-sm-3 control-label">Last name</label>
                                                <div class="col-sm-9">
                                                    <input type="text" class="form-control" required
                                                           name="last_name" value="${user_info.lastName}">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <label class="col-sm-3  control-label ">Login</label>
                                                <div class="col-sm-9">
                                                    <input type="text" class="form-control" required name="login"
                                                           value="${user_info.login}">
                                                </div>
                                            </div>

                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <label class="col-sm-3 control-label">Password</label>
                                                <div class="col-sm-9 ">
                                                    <input type="password" class="form-control" required
                                                           name="password" value="${user_info.password}">
                                                </div>
                                            </div>

                                        </div>

                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-offset-2 col-sm-10 p-top-7">
                                                    <button type="submit" class="btn btn-outline-primary">Save changes
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div><!-- form  -->
                        </div>
                    </div>
                    <div class="tab-pane fade " id="v-pills-test" role="tabpanel" aria-labelledby="v-pills-test-tab">

                    </div>

                </div>
            </div>

        </div>

    </div>
</div>
</div>
<div id="content bor"></div>


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