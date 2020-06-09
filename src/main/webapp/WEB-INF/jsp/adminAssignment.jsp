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
<fmt:message bundle="${loc}" key="assignment.nav-link.assign_test" var="nav_item_assign_test"/>
<fmt:message bundle="${loc}" key="assignment.nav-link.users_assignments" var="users_assignments"/>
<fmt:message bundle="${loc}" key="assignment.nav-link.users_assignments" var="users_assignments"/>

<fmt:message bundle="${loc}" key="assignment.label.test_type" var="lable_test_type"/>
<fmt:message bundle="${loc}" key="assignment.label.test_title" var="lable_test"/>
<fmt:message bundle="${loc}" key="assignment.label.deadline_date" var="lable_deadline_date"/>
<fmt:message bundle="${loc}" key="assignment.label.users" var="lable_users"/>
<fmt:message bundle="${loc}" key="assignment.select.choose" var="view_all"/>
<fmt:message bundle="${loc}" key="assignment.message.invalid_date" var="message_invalid_date"/>

<fmt:message bundle="${loc}" key="assignment.table.test_title" var="table_test_title"/>
<fmt:message bundle="${loc}" key="assignment.table.user_first_name" var="table_first_name"/>
<fmt:message bundle="${loc}" key="assignment.table.user_last_name" var="table_last_name"/>
<fmt:message bundle="${loc}" key="assignment.table.assignment_date" var="table_assignment_date"/>
<fmt:message bundle="${loc}" key="assignment.table.test_deadline" var="table_deadline"/>
<fmt:message bundle="${loc}" key="assignment.table.is_completed" var="table_is_completed"/>
<fmt:message bundle="${loc}" key="assignment.checkbox.completed" var="checkbox_completed"/>
<fmt:message bundle="${loc}" key="admin.form.label.test_title" var="lable_test_title"/>
<fmt:message bundle="${loc}" key="admin.form.label.test_type" var="lable_test_type"/>
<fmt:message bundle="${loc}" key="admin.form.label.date" var="lable_date"/>
<fmt:message bundle="${loc}" key="admin.form.label.users" var="lable_users"/>
<fmt:message bundle="${loc}" key="assignment.button.assign" var="button_assign"/>
<fmt:message bundle="${loc}" key="assignment.button.show" var="button_show"/>
<fmt:message bundle="${loc}" key="assignment.message.empty_data" var="message_empty_data"/>


<div class="container-fluid p-0">

    <jsp:include page="parts/nav-menu.jsp"/>
    <div class="row height-90">
        <div class="col-2 background-gradient height-100 p-l-15 p-r-0">
            <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">

                <a class="nav-link vertical-menu active" id="v-pills-assigned_test-tab" data-toggle="pill"
                   href="#v-pills-assigned_test"
                   role="tab"
                   aria-controls="v-pills-assigned_test" aria-selected="true"><c:out
                        value="${nav_item_assign_test}"/></a>
                <a class="nav-link vertical-menu" id="v-pills-assigned_users-tab" data-toggle="pill"
                   href="#v-pills-assigned_users"
                   role="tab"
                   aria-controls="v-pills-assigned_users" aria-selected="true">
                    <c:out value="${users_assignments}"/></a>

            </div>
        </div>
        <div class="col-10">
            <div class="tab-content" id="v-pills-tabContent">

                <div class="tab-pane fade active show " id="v-pills-assigned_test" role="tabpanel"
                     aria-labelledby="v-pills-assigned_test-tab">
                    <div class="row m-t-15">
                        <div class="col-1"></div>
                        <div class="col-4">
                            <form name="assign" id="assignAction" onsubmit="assignUser(); return false;"
                                  enctype="multipart/form-data"
                                  accept-charset="UTF-8" class="key-form" role="form">
                                <input type="hidden" name="command" value="assign_test"/>
                                <div class="form-group">
                                    <label for="testType"><c:out value="${lable_test_type}"/></label>
                                    <select required class="form-control" name="testTypeId" id="testType">
                                        <option value="0" selected><c:out value="${view_all}"/></option>
                                        <c:forEach var="item" items="${requestScope.type_tests}">
                                            <option value="${item.id}">${item.title}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="testTitle"><c:out value="${lable_test}"/></label>

                                    <select required class="form-control" name="testId" id="testTitle">
                                        <option value="0" selected><c:out value="${view_all}"/></option>
                                    </select>


                                </div>
                                <div class="form-group">
                                    <label for="students"><c:out value="${lable_users}"/></label>
                                    <select required multiple class="form-control" name="assigned_users" id="students">
                                        <c:forEach var="user" items="${requestScope.users}">
                                            <option value="${user.id}">${user.firstName} ${user.lastName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <label for="date"><c:out value="${lable_deadline_date}"/></label>
                                <input id="date" type="date" class="form-control mydate" name="date"
                                       min="${requestScope.dateNow}">
                                <div class="invalid-feedback">
                                    <c:out value="${message_invalid_date}"/>
                                </div>
                                <button type="submit" class="btn btn-outline-primary card-btn m-t-7">${button_assign}</button>
                            </form>
                        </div>
                        <div class="col-2">
                            <div id="assignResult" style="width: 500px; margin: 0 auto;">
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
                                <div id="assignmentError" class="alert alert-danger" role="alert"
                                     style="display: none;">
                                    ${message_empty_data}
                                </div>

                            </div>
                        </div>
                    </div>
                </div>

                <div class="tab-pane fade m-27" id="v-pills-assigned_users" role="tabpanel"
                     aria-labelledby="v-pills-assigned_users-tab">
                    <div class="row">

                        <form id="displayUsers" enctype="multipart/form-data" class="form-width100"
                              onsubmit="showUsersAssignedToTest();return false;"
                              accept-charset="UTF-8" class="key-form" role="form">
                            <div class="row">
                                <div class="col-3">
                                    <div class="form-group">
                                        <label for="type">${lable_test_type}</label>
                                        <select class="form-control" name="typeId" id="type">
                                            <option value="0" selected>${view_all}</option>
                                            <c:forEach var="item" items="${requestScope.type_tests}">
                                                <option value="${item.id}">${item.title}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-3">
                                    <div class="form-group">
                                        <label for="test">${lable_test}</label>
                                        <select class="form-control" name="testId" id="test">
                                            <option value="0" selected>${view_all}</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-auto">
                                    <div class="form-group form-check m-t-27 p-top-7">
                                        <input type="checkbox" name="completed" class="form-check-input"
                                               id="completed">
                                        <label class="form-check-label" for="completed">${checkbox_completed}</label>
                                    </div>
                                </div>
                                <div class="col-1 p-top-7">
                                    <button type="submit" class="btn btn-outline-primary card-btn m-t-27">${button_show}
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="row m-t-15">
                        <div class="col-10" id="usersAssignment" style="display: none;">
                            <table class="table table-sm">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th scope="col">${table_test_title}</th>
                                    <th scope="col">${table_first_name}</th>
                                    <th scope="col">${table_last_name}</th>
                                    <th scope="col">${table_assignment_date}</th>
                                    <th scope="col">${table_deadline}</th>
                                    <th scope="col">${table_is_completed}</th>
                                    <th scope="col"></th>
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