<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ taglib uri="customtag" prefix="res" %>

    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="resources/fontawesome-free-5.12.1-web/css/all.css">
    <link rel="stylesheet" href="resources/css/style.css"/>
    <link rel="stylesheet" href="resources/css/test-card.css"/>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@2.8.0"></script>


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
<fmt:message bundle="${loc}" key="result.page.card-title" var="title_result"/>
<fmt:message bundle="${loc}" key="result.page.chart.right-count" var="chart_right"/>
<fmt:message bundle="${loc}" key="result.page.chart.wrong-count" var="chart_wrong"/>
<fmt:message bundle="${loc}" key="result.page.title.all-count" var="title_all_count"/>
<fmt:message bundle="${loc}" key="result.page.title.right_count" var="title_right_count"/>
<fmt:message bundle="${loc}" key="result.page.title.date_start" var="title_start_time"/>
<fmt:message bundle="${loc}" key="result.page.title.date_end" var="title_finish_time"/>


<div class="container-fluid p-0">

    <jsp:include page="parts/nav-menu.jsp"/>

    <div class="row p-t-25">
        <div class="col-3"></div>
        <div class="col-6">
            <div class="row">

                <div class="card border-info mb-3" style="min-width: 100%;">
                    <div class="card-header text-info"><h5> ${requestScope.test_name}</h5>
                    </div>
                    <div class="card-body">
                        <div class="row m-0">
                            <div class="col-4">
                                <h5 style="background-color:#fa7d93">${title_result}
                                    <res:result-tag rightCountQuestion="${requestScope.testResult.rightCountQuestion}"
                                                    countTestQuestion="${requestScope.testResult.countTestQuestion}"/>%</h5>
                                <p>
                                <h6>${title_all_count} ${requestScope.testResult.countTestQuestion}</h6>
                                <h6>${title_right_count} ${requestScope.testResult.rightCountQuestion}</h6>
                                <h6>${title_start_time} <res:local-date
                                        date="${requestScope.testResult.dateStart}"/></h6>
                                <h6>${title_finish_time} <res:local-date
                                        date="${requestScope.testResult.dateEnd}"/></h6>
                                </p>
                            </div>
                            <div class="col-5">
                                <div style="width: 500px;height: 300px">
                                    <canvas id="resultChart"></canvas>
                                </div>
                                <input type="hidden" id="countRight" name="${chart_right}"
                                       value="${requestScope.testResult.rightCountQuestion}">
                                <input type="hidden" id="countAll" name="${chart_wrong}"
                                       value="${requestScope.testResult.countTestQuestion}">
                            </div>
                        </div>


                    </div>
                </div>
            </div>
            <div class="col-3"></div>
        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="" crossorigin="anonymous"></script>
    <script src="resources/js/bootstrap.min.js.map"
            integrity="" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="" crossorigin="anonymous"></script>
    <script src="resources/js/pie.js"></script>

</body>

</html>