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



        <nav class="navbar navbar-expand-lg navbar-light  menu-color p-t-b-0 border-menu">
                <a class="navbar-brand logo-color"  href="${pageContext.request.contextPath}">
                    <img alt="logo" class="logo-size" src="resources/img/logo.png">
                </a>
                <button class="navbar-toggler" type="button" data-toggle="collapse"
                        data-target="#navbarSupportedContent"
                        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse start-page-nav-itm max-w-nav" id="navbarSupportedContent">
                    <ul class="navbar-nav mr-auto start-page-nav-itm" id="myTab">

                        <li class="nav-item" >
                            <a class="nav-link nav-vrl item-start active" data-toggle="tab" role="tab"
                               href="${pageContext.request.contextPath}/test?command=show_main_page">${nav_item_tests}</a>
                        </li>

                        <c:if test="${sessionScope.user_role=='ADMIN'}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle nav-vrl item-start" href="#" id="navbarDropdown"
                                   role="button" role="tab"
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

                        <li class="nav-item ">
                            <a class="nav-link nav-vrl item-start" data-toggle="tab" role="tab" href="#">${nav_item_statistic}</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link  nav-vrl item-start" data-toggle="tab" role="tab" href="#">${nav_item_about}</a>
                        </li>
                    </ul>
                </div>

                        <form action="test" method="POST" class="m-0">
                            <input type="hidden" name="command" value="change_language"/>
                            <input type="hidden" name="local" value="ru"/>
                            <button type="submit"
                                    class="btn ru"></button>
                        </form>

                        <form action="test" method="POST" class="m-0">
                            <input type="hidden" name="command" value="change_language"/>
                            <input type="hidden" name="local" value="en"/>
                            <button type="submit"
                                    class="btn en">

                            </button>
                        </form>




                <ul class="navbar-nav mr-auto start-page-nav-itm p-l-21">

                    <li class="nav-item dropdown ">
                        <a class="nav-link dropdown-toggle user-detail " href="#" id="navbarDropdownUser" role="button"
                           data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i class="far fa-address-card fa-2x m-t-10"></i>
                            <span class="user-name"><c:out value="${sessionScope.user_login}"/></span>
                        </a>


                        <div class="dropdown-menu p-t-b-0" aria-labelledby="navbarDropdown-User">
                            <a class="dropdown-item user-drop-down p-t-b-5"
                               href="${pageContext.request.contextPath}/test?command=show_user_account">
                                Личный кабинет
                            </a>
                            <div class="dropdown-divider"></div>

                            <form action="test" method="POST" class="m-0 txt-algn-center">
                                <input type="hidden" name="command" value="sign_out"/>
                                <button type="submit"
                                        class="btn btn-outline-primary btn-sign-out">${button_sign_out}</button>
                            </form>

                        </div>
                    </li>
                </ul>

        </nav>



    <div class="row height-90">
        <div class="col-2 background-gradient height-100 p-l-15 p-r-0">
            <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
                <c:forEach var="item" items="${requestScope.tests_type}">
                    <a class="nav-link vertical-menu" id="v-pills-${item.id}-tab" data-toggle="pill"
                       href="#v-pills-${item.id}"
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
                                            <a href="#" class="btn btn-primary test-start-btn">Переход куда-нибудь</a>
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
        integrity=""        crossorigin="anonymous"></script>
<script src="resources/js/bootstrap.min.js.map"
        integrity=""        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity=""        crossorigin="anonymous"></script>
<script src="resources/js/script.js"></script>

</body>

</html>