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
<fmt:message bundle="${loc}" key="button.test.continue" var="button_continue"/>
<fmt:message bundle="${loc}" key="nav-link.assigned_tests" var="assigned_test"/>
<fmt:message bundle="${loc}" key="test.card.time.min" var="card_time_min"/>
<fmt:message bundle="${loc}" key="test.card.question" var="card_question"/>


<div class="container-fluid p-0">

    <jsp:include page="parts/nav-menu.jsp"/>

    <div class="row height-90">
        <div class="col-2 background-gradient height-100 p-l-15 p-r-0">
            <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">

                <c:forEach var="type" items="${requestScope.testTypes}">
                    <c:if test="${type.id==requestScope.activeTypeId}">
                        <a class="nav-link vertical-menu active " id="v-pills-admin-${type.id}"
                           href="${pageContext.request.contextPath}/test?command=show_tests_page&typeId=${type.id}"
                           role="tab"
                           aria-controls="v-pills-test" aria-selected="true"><c:out value="${type.title}"/></a>
                    </c:if>
                    <c:if test="${type.id!=requestScope.activeTypeId}">

                        <a class="nav-link vertical-menu " id="v-pills-admin-${type.id}"
                           href="${pageContext.request.contextPath}/test?command=show_tests_page&typeId=${type.id}"
                           role="tab"
                           aria-controls="v-pills-test" aria-selected="true"><c:out value="${type.title}"/></a>
                    </c:if>
                </c:forEach>

            </div>
        </div>
        <div class="col-10">


            <div class="row m-t-35">
                <c:forEach var="itm" items="${requestScope.testsInfoData}">
                    <div class="t-card">
                        <div class="col-2 card-main">
                            <div class="card-section card-section-third border rounded">
                                <div class="card-header card-header-third rounded">
                                    <div class="ribbon ribbon-top-type-left"><span></span></div>
                                    <div class="ribbon-type ribbon-top-asgmt-left">
                                        <span>${requestScope.testTypeTitle}</span>
                                    </div>

                                </div>
                                <div class="card-body text-center mb-2 card-test">
                                    <h6 class="name-test">${itm.title}</h6>
                                    <hr>

                                    <p class="card-text time-quest">${itm.duration} ${card_time_min}</p>
                                    <p class="card-text time-quest">${itm.countQuestion} ${card_question}</p>


                                </div>


                                <c:if test="${!itm.started}">

                                    <form method="GET" action="test">
                                        <input type="hidden" name="command" value="show_exe_test_page"/>
                                        <input type="hidden" name="testId" value="${itm.id}"/>
                                        <button type="submit"
                                                class="card-btn btn btn-outline-primary d-block mx-auto">
                                                ${button_get_started}
                                        </button>
                                    </form>

                                </c:if>
                                <c:if test="${itm.started}">

                                    <form method="GET" action="test">
                                        <input type="hidden" name="command" value="show_exe_test_page"/>
                                        <input type="hidden" name="testId" value="${itm.id}"/>

                                        <button type="submit"
                                                class="card-btn btn btn-outline-primary d-block mx-auto">
                                                ${button_continue}
                                        </button>
                                    </form>

                                </c:if>

                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div style="position: absolute; bottom: 0px; width: 100%; align-content: center">
                <div>
                    <nav aria-label="..." class="m-t-27">
                        <ul class="pagination pagination-sm pagination_center">
                            <c:forEach var="i" begin="1" end="${requestScope.countPages}">
                                <c:if test="${i==requestScope.currentPage}">

                                    <li class="page-item page-item-change active" aria-current="page">
                                                <span class="page-link">${i}
                                                            <span class="sr-only">(current)</span>
                                                            </span>
                                    </li>
                                </c:if>


                                <c:if test="${i!=requestScope.currentPage}">
                                    <li class="page-item">
                                        <a class="page-link"
                                           href="${pageContext.request.contextPath}/test?command=show_tests_page&currentPage=${i}">
                                                ${i}
                                        </a>
                                    </li>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </nav>
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
<script src="resources/js/script.js"></script>

</body>

</html>