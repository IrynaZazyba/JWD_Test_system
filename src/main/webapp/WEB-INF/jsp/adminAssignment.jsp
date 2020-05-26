<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="resources/fontawesome-free-5.12.1-web/css/all.css">
    <link rel="stylesheet" href="resources/css/style.css"/>
    <link rel="stylesheet" href="resources/css/test-card.css"/>
    <!-- Include Date Range Picker -->
    <script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
    <link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.css"/>


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
<fmt:message bundle="${loc}" key="statistic.table.test_title" var="table_test_title"/>
<fmt:message bundle="${loc}" key="statistic.table.date_start" var="table_date_start"/>
<fmt:message bundle="${loc}" key="statistic.table.test_execution_time" var="table_test_execution_time"/>
<fmt:message bundle="${loc}" key="statistic.table.time_spend_on_test" var="table_time_spend_on_test"/>
<fmt:message bundle="${loc}" key="statistic.table.count_right_answer" var="table_count_right_answer"/>
<fmt:message bundle="${loc}" key="statistic.table.result" var="table_result"/>
<fmt:message bundle="${loc}" key="assignment.exists" var="exists_assignment"/>
<fmt:message bundle="${loc}" key="assignment.success_message" var="success_assignment"/>


<div class="container-fluid p-0">
    <input type="hidden" name="url"
           value="${pageContext.request.contextPath}+/test?+${pageContext.request.queryString}"/>

    <jsp:include page="parts/nav-menu.jsp"/>

    <div class="row">
        <div class="col-4">
                <form name="assign" id="assign" onsubmit="assignUser(); return false;" enctype="multipart/form-data"
                      accept-charset="UTF-8" class="key-form" role="form">
                    <input type="hidden" name="command" value="assign_test"/>
                    <div class="form-group">
                        <label for="testType">Example select</label>
                        <select class="form-control" name="testTypeId" id="testType">
                            <option selected>Choose...</option>
                            <c:forEach var="item" items="${requestScope.type_tests}">
                                <option value="${item.id}">${item.title}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="testTitle">Example select</label>
                        <select class="form-control" name="testId" id="testTitle">
                            <option selected>Choose...</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="students">Example multiple select</label>
                        <select multiple class="form-control" name="users" id="students">
                            <c:forEach var="user" items="${requestScope.users}">
                                <option value="${user.id}">${user.firstName} ${user.lastName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <input id="date" type="date" class="form-control" class="mydate" name="date" placeholder="Дата"
                           min="${requestScope.dateNow}">
                    <button type="submit" class="btn btn-outline-primary card-btn">Submit</button>
                </form>
        </div>
        <div class="col-3">
            <div id="attention" style="width: 500px; margin: 0 auto;">
                <div class="alert alert-danger" role="alert" id="alert" style="visibility: hidden">
                    ${exists_assignment}
                    <hr>
                    <div id="existsAssignment"></div>
                </div>
                <div class="alert alert-success" role="alert" id="success" style="visibility: hidden">
                    ${success_assignment}
                    <hr>
                    <div id="successMessage"></div>
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
<script src="resources/js/adminScript.js"></script>

</body>

</html>