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
<fmt:message bundle="${loc}" key="admin_panel.type.message.delete" var="message_impossible_delete"/>
<fmt:message bundle="${loc}" key="admin.form.label.test_type" var="test_type_lable"/>
<fmt:message bundle="${loc}" key="editTest.button.toTestList" var="button_to_tests"/>
<fmt:message bundle="${loc}" key="delete.confirm.message" var="delete_comfirm_message"/>
<fmt:message bundle="${loc}" key="editTest.confirm.button.confirm" var="mode_button_confirm"/>
<fmt:message bundle="${loc}" key="delete.button.cancel" var="delete_button_cancel"/>
<fmt:message bundle="${loc}" key="delete.button.delete" var="delete_button_delete"/>


<div class="container-fluid p-0">

    <jsp:include page="../parts/nav-menu.jsp"/>

    <div class="row">
        <div class="col-4"></div>
        <div class="col-3 p-t-27">
            <div id="invalidDeleteMessage" style="display: none">
                <div class="alert alert-danger" role="alert">
                    ${message_impossible_delete} </div>
            </div>
            <div class="tab-content" id="v-pills-tabContent">
                <table class="table table-sm p-t-27">
                    <thead>
                    <tr>
                        <th scope="col">${test_type_lable}</th>
                        <th scope="col"></th>
                    </tr>
                    </thead>
                    <tbody>


                    <c:forEach var="type" items="${requestScope.testTypes}">
                        <tr id="${type.id}">
                            <td>${type.title}</td>
                            <td>
                                <button id="btn-${type.id}" type="button"
                                        onclick="showConfirmDeleteType(${type.id});return false;"
                                        class="btn btn-outline-info btn-block">
                                    <i class="far fa-trash-alt"></i>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="form-group m-t-15">
                    <a href="${pageContext.request.contextPath}/test?command=show_admin_panel">
                        <button type="button" id="continueLater" class="btn btn-outline-info">
                            ${button_to_tests}</button>
                    </a>
                </div>
            </div>
            <div class="col-4"></div>

        </div>


    </div>

    <div id="confirmDeleteType" class="modal fade" data-backdrop="static" tabindex="-1" role="dialog"
         aria-labelledby="staticBackdropLabel" aria-hidden="true" data-target="#staticBackdrop">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="staticBackdropLabel">
                        ${delete_comfirm_message}
                    </h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary"
                            data-dismiss="modal">${delete_button_cancel}</button>
                    <button type="button" onclick="deleteTestType(); return false"
                            class="btn btn-danger btn-ok">${delete_button_delete}</button>
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
    <script src="resources/js/adminScript.js"></script>

</body>

</html>