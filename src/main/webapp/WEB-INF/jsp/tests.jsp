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
<fmt:message bundle="${loc}" key="button.test.get_started" var="button_get_started"/>

<div class="container-fluid p-0">

    <jsp:include page="parts/nav-menu.jsp"/>

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

                        <div class="row m-t-35">
                            <c:forEach var="itm" items="${item.tests}">
                                <div class="col-2 card-main">
                                    <div class="card-section card-section-third border rounded">
                                        <div class="card-header card-header-third rounded">
                                            <div class="ribbon ribbon-top-type-left"><span></span></div>
                                            <div class="ribbon-type ribbon-top-asgmt-left"><span>${item.title}</span>
                                            </div>

                                        </div>
                                        <div class="card-body text-center mb-2 card-test">
                                            <h5 class="name-test">${itm.title}</h5>
                                            <hr>

                                            <p class="card-text time-quest">${itm.duration} min</p>
                                            <p class="card-text time-quest">${itm.countQuestion} questions</p>


                                        </div>
                                        <form method="GET" action="test">
                                            <input type="hidden" name="command" value="show_exe_test_page"/>
                                            <input type="hidden" name="test_id" value="${itm.id}"/>
                                            <button type="submit"
                                                    class="card-btn btn btn-outline-primary d-block mx-auto">
                                                ${button_get_started}
                                            </button>
                                        </form>
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
        integrity="" crossorigin="anonymous"></script>
<script src="resources/js/bootstrap.min.js.map"
        integrity="" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="" crossorigin="anonymous"></script>
<script src="resources/js/script.js"></script>

</body>

</html>