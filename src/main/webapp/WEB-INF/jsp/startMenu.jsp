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
<fmt:setBundle basename="local" var="loc"/>
<fmt:message bundle="${loc}" key="button.sign_out" var="button_sign_out"/>
<fmt:message bundle="${loc}" key="nav-item.tests" var="nav_item_tests"/>
<fmt:message bundle="${loc}" key="nav-item.admin" var="nav_item_admin"/>
<fmt:message bundle="${loc}" key="nav-item.statistic" var="nav_item_statistic"/>
<fmt:message bundle="${loc}" key="nav-item.about" var="nav_item_about"/>
<fmt:message bundle="${loc}" key="nav-item.admin.tests" var="nav_item_admin_tests"/>
<fmt:message bundle="${loc}" key="nav-item.admin.users" var="nav_item_admin_users"/>

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
                               href="${pageContext.request.contextPath}/test?command=show_main_page">${nav_item_tests}</a>
                        </li>

                        <c:if test="${sessionScope.user_role=='ADMIN'}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    ${nav_item_admin}
                                </a>
                                <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                                    <a class="dropdown-item" href="#">${nav_item_admin_tests}</a>
                                    <div class="dropdown-divider"></div>
                                    <a class="dropdown-item" href="#">${nav_item_admin_users}</a>
                                </div>
                            </li>
                        </c:if>

                        <li class="nav-item">
                            <a class="nav-link" href="#">${nav_item_statistic}</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">${nav_item_about}</a>
                        </li>
                    </ul>
                </div>
            </div>


            <div class="col-auto p-0 icon-user">
                <a href="${pageContext.request.contextPath}/test?command=show_user_account"> <i
                        class="far fa-address-card fa-2x color-dodgerblue"></i></a>
            </div>
            <div class="col-auto m-t-15"><c:out value="${sessionScope.user_login}"/>
            </div>
            <div class="col-1">
                <form action="test" method="POST" class="m-0">
                    <input type="hidden" name="command" value="sign_out"/>
                    <button type="submit" class="btn btn-outline-primary btn-md m-t-7">${button_sign_out}</button>
                </form>
            </div>
            <div class="col-2">
                <form action="test" method="POST" class="m-0">
                    <input type="hidden" name="command" value="change_language"/>
                    <input type="hidden" name="local" value="ru"/>
                    <button type="submit" class="btn btn-outline-primary btn-md m-t-7">ru</button>
                </form>
                <form action="test" method="POST" class="m-0">
                    <input type="hidden" name="command" value="change_language"/>
                    <input type="hidden" name="local" value="en"/>
                    <button type="submit" class="btn btn-outline-primary btn-md m-t-7">en</button>
                </form>
            </div>
        </div>
    </nav>

    <div class="row height-90">
        <div class="col-2 background-gradient height-100">
            <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
                <c:forEach var="item" items="${requestScope.tests_type}">
                    <a class="nav-link" id="v-pills-${item.id}-tab" data-toggle="pill" href="#v-pills-${item.id}"
                       role="tab"
                       aria-controls="v-pills-${item.id}" aria-selected="true">
                        <c:out value="${item.title}"/></a>
                </c:forEach>

            </div>


        </div>
        <div class="col-9">
            <div class="tab-content" id="v-pills-tabContent">
                <c:forEach var="item" items="${requestScope.tests_type}">

                    <div class="tab-pane fade " id="v-pills-${item.id}" role="tabpanel"
                         aria-labelledby="v-pills-${item.id}-tab">

                        <div class="row m-t-15">
                            <c:forEach var="itm" items="${item.tests}">
                                <div class="col-auto">
                                    <div class="card" style="width: 18rem;">
                                            <%--                          <img src="..." class="card-img-top" alt="...">--%>
                                        <div class="card-body">
                                            <h5 class="card-title"><c:out value="${itm.title}"/>
                                            </h5>
                                            <p class="card-text">Some quick example text to build on the card title and
                                                make
                                                up the bulk of the card's content.</p>
                                            <a href="#" class="btn btn-primary">Переход куда-нибудь</a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>

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