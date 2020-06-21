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

<fmt:message bundle="${loc}" key="assignment.label.test_type" var="lable_test_type"/>
<fmt:message bundle="${loc}" key="assignment.label.test_title" var="lable_test"/>
<fmt:message bundle="${loc}" key="assignment.label.deadline_date" var="lable_deadline_date"/>
<fmt:message bundle="${loc}" key="assignment.label.date" var="lable_date"/>
<fmt:message bundle="${loc}" key="assignment.label.users" var="lable_users"/>
<fmt:message bundle="${loc}" key="assignment.select.choose" var="view_all"/>
<fmt:message bundle="${loc}" key="assignment.message.invalid_date" var="message_invalid_date"/>

<fmt:message bundle="${loc}" key="admin.users_result_page.table.first_name" var="table_first_name"/>
<fmt:message bundle="${loc}" key="admin.users_result_page.table.last_name" var="table_last_name"/>
<fmt:message bundle="${loc}" key="admin.users_result_page.table.test_type" var="table_test_type"/>
<fmt:message bundle="${loc}" key="admin.users_result_page.table.test_title" var="table_test_title"/>
<fmt:message bundle="${loc}" key="admin.users_result_page.table.completed" var="table_completed"/>
<fmt:message bundle="${loc}" key="admin.users_result_page.table.count_questions" var="table_count_questions"/>
<fmt:message bundle="${loc}" key="admin.users_result_page.table.right_answers" var="table_right_answers"/>
<fmt:message bundle="${loc}" key="admin.users_result_page.button_show" var="button_show"/>










<div class="container-fluid p-0">

    <jsp:include page="parts/nav-menu.jsp"/>

    <div class="row m-t-15">
        <div class="col-1"></div>
        <div class="col-10">
            <div class="row">
                <form class="wight-100" name="assign" id="assign" onsubmit="showResult(); return false;"
                      enctype="multipart/form-data"
                      accept-charset="UTF-8" class="key-form" role="form">
                    <input type="hidden" name="command" value="assign_test"/>
                    <div class="row">
                        <div class="col-3">
                            <div class="form-group">
                                <label for="testType"><c:out value="${lable_test_type}"/></label>
                                <select class="form-control" name="testTypeId" id="testType">
                                    <option  value=""><c:out value="${view_all}"/></option>
                                    <c:forEach var="item" items="${requestScope.type_tests}">
                                        <option value="${item.id}">${item.title}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="col-3">
                            <div class="form-group">
                                <label for="testTitle"><c:out value="${lable_test}"/></label>
                                <select class="form-control" name="testId" id="testTitle">
                                    <option  value=""><c:out value="${view_all}"/></option>
                                </select>
                            </div>
                        </div>
                        <div class="col-3">
                            <div class="form-group">
                                <label for="students"><c:out value="${lable_users}"/></label>
                                <select class="form-control" name="assigned_users" id="students">
                                    <option  value=""><c:out value="${view_all}"/></option>
                                    <c:forEach var="user" items="${requestScope.users}">
                                        <option value="${user.id}">${user.firstName} ${user.lastName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="col-2">
                            <label for="date"><c:out value="${lable_date}"/></label>
                            <input id="date" type="date" class="form-control mydate" name="date"
                                   min="${requestScope.dateNow}">
                            <div class="invalid-feedback">
                                <c:out value="${message_invalid_date}"/>
                            </div>
                        </div>
                        <div class="col-1 m-t-30">
                            <button type="submit" class="btn btn-outline-primary card-btn">${button_show}</button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="row">
                <div id="resultContent" class="col-10">
                    <table class="table table-sm">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th scope="col">${table_first_name}</th>
                            <th scope="col">${table_last_name}</th>
                            <th scope="col">${table_test_type}</th>
                            <th scope="col">${table_test_title}</th>
                            <th scope="col">${table_completed}</th>
                            <th scope="col">${table_count_questions}</th>
                            <th scope="col">${table_right_answers}</th>
                        </tr>
                        </thead>
                        <tbody id="jsData">

                        </tbody>
                    </table>
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
<script src="resources/js/script.js"></script>


</body>

</html>