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
<fmt:message bundle="${loc}" key="assignment.exists" var="exists_assignment"/>
<fmt:message bundle="${loc}" key="assignment.success_message" var="success_assignment"/>
<fmt:message bundle="${loc}" key="assignment.nav-link.assign_test" var="assign_test"/>
<fmt:message bundle="${loc}" key="assignment.nav-link.users_assignments" var="users_assignments"/>
<fmt:message bundle="${loc}" key="assignment.nav-link.users_assignments" var="users_assignments"/>

<fmt:message bundle="${loc}" key="assignment.label.test_type" var="lable_test_type"/>
<fmt:message bundle="${loc}" key="assignment.label.test_title" var="lable_test"/>
<fmt:message bundle="${loc}" key="assignment.label.deadline_date" var="lable_deadline_date"/>
<fmt:message bundle="${loc}" key="assignment.label.users" var="lable_users"/>
<fmt:message bundle="${loc}" key="assignment.select.choose" var="select_choose"/>
<fmt:message bundle="${loc}" key="assignment.message.invalid_date" var="message_invalid_date"/>


<div class="container-fluid p-0">

    <jsp:include page="parts/nav-menu.jsp"/>

    <div class="row height-90">
        <div class="col-2 background-gradient height-100 p-l-15 p-r-0">
            <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">

                <a class="nav-link vertical-menu active" id="v-pills-assigned_test-tab" data-toggle="pill"
                   href="#v-pills-assigned_test"
                   role="tab"
                   aria-controls="v-pills-assigned_test" aria-selected="true"><c:out value="${assign_test}"/></a>
                <a class="nav-link vertical-menu" id="v-pills-assigned_users-tab" data-toggle="pill"
                   href="#v-pills-assigned_users"
                   role="tab"
                   aria-controls="v-pills-assigned_users" aria-selected="true">
                    <c:out value="${users_assignments}"/></a>

            </div>
        </div>
        <div class="col-9">
            <div class="tab-content" id="v-pills-tabContent">

                <div class="tab-pane fade active show " id="v-pills-assigned_test" role="tabpanel"
                     aria-labelledby="v-pills-assigned_test-tab">
                    <div class="row m-t-15">
                        <div class="col-1"></div>
                        <div class="col-4">
                            <form name="assign" id="assign" onsubmit="assignUser(); return false;"
                                  enctype="multipart/form-data"
                                  accept-charset="UTF-8" class="key-form" role="form">
                                <input type="hidden" name="command" value="assign_test"/>
                                <div class="form-group">
                                    <label for="testType"><c:out value="${lable_test_type}"/></label>
                                    <select class="form-control" name="testTypeId" id="testType">
                                        <option selected><c:out value="${select_choose}"/></option>
                                        <c:forEach var="item" items="${requestScope.type_tests}">
                                            <option value="${item.id}">${item.title}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="testTitle"><c:out value="${lable_test}"/></label>

                                    <select class="form-control" name="testId" id="testTitle">
                                        <option selected><c:out value="${select_choose}"/></option>
                                    </select>


                                </div>
                                <div class="form-group">
                                    <label for="students"><c:out value="${lable_users}"/></label>
                                    <select multiple class="form-control" name="assigned_users" id="students">
                                        <c:forEach var="user" items="${requestScope.users}">
                                            <option value="${user.id}">${user.firstName} ${user.lastName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <label for="date"><c:out value="${lable_deadline_date}"/></label>
                                <input id="date" type="date" class="form-control mydate" name="date" placeholder="Дата"
                                       min="${requestScope.dateNow}">
                                <div class="invalid-feedback">
                                    <c:out value="${message_invalid_date}"/>
                                </div>
                                <button type="submit" class="btn btn-outline-primary card-btn">Submit</button>
                            </form>
                        </div>
                        <div class="col-2">
                            <div id="attention" style="width: 500px; margin: 0 auto;">
                                <div class="alert alert-danger" role="alert" id="alert" style="display: none">
                                    ${exists_assignment}
                                    <hr>
                                    <div id="existsAssignment"></div>
                                </div>
                                <div class="alert alert-success" role="alert" id="success" style="display: none">
                                    ${success_assignment}
                                    <hr>
                                    <div id="successMessage"></div>
                                </div>
                                <%--                min="${requestScope.dateNow}--%>

                            </div>
                        </div>
                    </div>
                </div>

                <div class="tab-pane fade " id="v-pills-assigned_users" role="tabpanel"
                     aria-labelledby="v-pills-assigned_users-tab">
xfcgvhjbkml

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