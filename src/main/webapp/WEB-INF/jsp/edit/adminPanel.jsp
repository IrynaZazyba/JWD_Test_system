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
<fmt:message bundle="${loc}" key="nav-link.admin-panel.add_test" var="nav_link_add_test"/>
<fmt:message bundle="${loc}" key="nav-link.admin-panel.add_test_type" var="nav_link_add_test_type"/>

<fmt:message bundle="${loc}" key="admin_panel.tests.table.test_title" var="table_test_title"/>
<fmt:message bundle="${loc}" key="admin_panel.tests.table.duration" var="table_duration"/>
<fmt:message bundle="${loc}" key="admin_panel.tests.table.key" var="table_key"/>
<fmt:message bundle="${loc}" key="admin_panel.tests.table.in_process" var="table_in_process"/>
<fmt:message bundle="${loc}" key="admin_panel.tests.button.add" var="button_add_test"/>
<fmt:message bundle="${loc}" key="admin_panel.types.button.add" var="button_add_type"/>
<fmt:message bundle="${loc}" key="admin_panel.types.button.delete" var="button_delete_type"/>

<fmt:message bundle="${loc}" key="admin_panel.tests.button.delete" var="button_delete"/>
<fmt:message bundle="${loc}" key="admin_panel.tests.button.edit" var="button_edit"/>
<fmt:message bundle="${loc}" key="admin_panel.tests.message.delete" var="message_impossible_delete"/>
<fmt:message bundle="${loc}" key="admin_panel.tests.message.empty_data" var="message_empty_data"/>


<fmt:message bundle="${loc}" key="delete.confirm.message" var="delete_confirm_message"/>
<fmt:message bundle="${loc}" key="delete.button.cancel" var="delete_button_cancel"/>
<fmt:message bundle="${loc}" key="delete.button.delete" var="delete_button_delete"/>
<fmt:message bundle="${loc}" key="admin_panel.tests.button.close" var="button_close"/>
<fmt:message bundle="${loc}" key="admin_panel.tests.button.save" var="button_save"/>
<fmt:message bundle="${loc}" key="admin_panel.tests.message.enter.type" var="message_enter_type"/>
<fmt:message bundle="${loc}" key="admin_panel.tests.message.exists.value" var="message_exists_value"/>
<fmt:message bundle="${loc}" key="assignment.message.empty_data" var="message_empty_data"/>


<div class="container-fluid p-0">

    <jsp:include page="../parts/nav-menu.jsp"/>

    <div class="row height-90">
        <div class="col-2 background-gradient height-100 p-l-15 p-r-0">
            <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">

                <button type="submit" data-toggle="modal" data-target="#addTestType" data-onclick="addTestType()"
                        class="card-btn btn btn-outline-primary d-block mx-auto vertical-menu-button-add">
                    ${button_add_type}
                </button>

                <c:forEach var="type" items="${requestScope.testTypes}">
                    <c:if test="${type.id==requestScope.activeTypeId}">
                        <a class="nav-link vertical-menu active " id="v-pills-admin-${type.id}"
                           href="${pageContext.request.contextPath}/test?command=show_admin_panel&typeId=${type.id}"
                           role="tab"
                           aria-controls="v-pills-test" aria-selected="true"><c:out value="${type.title}"/></a>
                    </c:if>
                    <c:if test="${type.id!=requestScope.activeTypeId}">

                        <a class="nav-link vertical-menu " id="v-pills-admin-${type.id}"
                           href="${pageContext.request.contextPath}/test?command=show_admin_panel&typeId=${type.id}"
                           role="tab"
                           aria-controls="v-pills-test" aria-selected="true"><c:out value="${type.title}"/></a>
                    </c:if>
                </c:forEach>

                <a href="${pageContext.request.contextPath}/test?command=delete_test_type_page">
                    <button type="submit"
                            class="card-btn btn btn-outline-primary d-block mx-auto vertical-menu-button-add">
                        ${button_delete_type} </button>
                </a>
            </div>


        </div>
        <div class="col-9">
            <div class="row m-15">
                <a href="${pageContext.request.contextPath}/test?command=add_test&typeId=${requestScope.activeTypeId}">
                    <button type="submit"
                            class="card-btn btn btn-outline-primary d-block mx-auto">
                        ${button_add_test} </button>
                </a>
            </div>
            <div id="invalidDeleteMessage" style="display: none">
                <div class="alert alert-danger" role="alert">
                    ${message_impossible_delete} </div>
            </div>
            <div class="tab-content" id="v-pills-tabContent">
                <table class="table table-sm">
                    <thead>
                    <tr>
                        <th scope="col">${table_test_title}</th>
                        <th scope="col">${table_duration}</th>
                        <th scope="col">${table_key}</th>
                        <th scope="col">${table_in_process}</th>
                        <th scope="col"></th>
                        <th scope="col"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${not empty requestScope.testsInfoData}">
                        <c:forEach var="test" items="${requestScope.testsInfoData}">
                            <tr>
                                <td>${test.title}</td>
                                <td>${test.duration}</td>
                                <td>${test.key}</td>
                                <td>${test.edited}</td>

                                <td>
                                    <c:if test="${!test.started}">
                                        <a href="${pageContext.request.contextPath}/test?command=preview_test&testId=${test.id}">
                                            <button class="btn btn-link">${button_edit}</button>
                                        </a>
                                    </c:if>
                                    <c:if test="${test.started}">
                                        <button disabled class="btn btn-link">
                                            <a href="">
                                                    ${button_edit}
                                            </a>
                                        </button>

                                    </c:if>
                                </td>
                                <td>
                                    <button class="btn btn-link" id="btn-${test.id}" data-toggle="modal"
                                            data-target="#confirm-delete"
                                            data-onclick="deleteTest(${test.id},this)">${button_delete}</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    <c:if test="${empty requestScope.testsInfoData}">
                        <tr>
                            <td>${message_empty_data}</td>
                        </tr>
                    </c:if>

                    </tbody>
                </table>

                <div style="position: absolute; bottom: 0px; width: 100%; align-content: center">
                    <div>
                        <nav aria-label="...">
                            <ul class="pagination pagination-sm pagination_center">
                                <c:forEach var="i" begin="1" end="${requestScope.countPages}">
                                    <c:if test="${i==requestScope.currentPage}">

                                        <li class="page-item page-item-change active" aria-current="page">
                                        <span class="page-link">${i}
                                <span class="sr-only">(current)</span>
                          </span>
                                    </c:if>

                                    <c:if test="${i!=requestScope.currentPage}">
                                        <li class="page-item">
                                            <a class="page-link"
                                               href="${pageContext.request.contextPath}/test?command=show_admin_panel&typeId=${requestScope.activeTypeId}&currentPage=${i}">
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

</div>
<div id="content bor"></div>


</div>
<div id="confirm-delete" class="modal fade" data-backdrop="static" tabindex="-1" role="dialog"
     aria-labelledby="staticBackdropLabel" aria-hidden="true" data-target="#staticBackdrop">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="staticBackdropLabel">
                    ${delete_confirm_message}
                </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">${delete_button_cancel}</button>
                <button type="button" class="btn btn-danger btn-ok">${delete_button_delete}</button>
            </div>
        </div>
    </div>
</div>

<div id="addTestType" class="modal fade" data-backdrop="static" tabindex="-1" role="dialog"
     aria-labelledby="staticBackdropLabel" aria-hidden="true" data-target="#staticBackdrop">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="staticLabel">
                    ${message_enter_type}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <input type="text" class="form-control" id="testTypeTitle">
                    <div class="invalid-feedback">
                        ${message_exists_value}
                    </div>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">${button_close}</button>
                <button type="button" onclick="addTestType()" class="btn btn-danger btn-ok">${button_save}</button>
            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
        integrity="" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
        integrity="" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="" crossorigin="anonymous"></script>
<script src="resources/js/addEditTest.js"></script>
<script src="resources/js/script.js"></script>


</body>
</html>