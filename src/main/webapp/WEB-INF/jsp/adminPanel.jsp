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


<div class="container-fluid p-0">

    <jsp:include page="parts/nav-menu.jsp"/>

    <div class="row height-90">
        <div class="col-2 background-gradient height-100 p-l-15 p-r-0">
            <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">


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
            </div>


        </div>
        <div class="col-9">
            <div class="row m-15">
                <a href="${pageContext.request.contextPath}/test?command=add_test&typeId=${requestScope.activeTypeId}">
                    <button type="submit"
                            class="card-btn btn btn-outline-primary d-block mx-auto">
                        Добавить
                    </button>
                </a>
            </div>
            <div id="invalidDeleteMessage" style="display: none">
                <div class="alert alert-danger" role="alert">
                    Данный тест не может быть удален.
                </div>
            </div>
            <div class="tab-content" id="v-pills-tabContent">
                <table class="table table-sm">
                    <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Название</th>
                        <th scope="col">Продолжительность</th>
                        <th scope="col">Ключ</th>
                        <th scope="col">В процессе создания</th>
                        <th scope="col"></th>
                        <th scope="col"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${not empty requestScope.testsInfoData}">
                        <c:set var="count" value="0"/>
                        <c:forEach var="test" items="${requestScope.testsInfoData}">
                            <tr>
                                <th scope="row">${count=count+1}</th>
                                <td>${test.title}</td>
                                <td>${test.duration}</td>
                                <td>${test.key}</td>
                                <td>${test.edited}</td>

                                <td>
                                    <a href="${pageContext.request.contextPath}/test?command=edit_test&testId=${test.id}">
                                        <button class="btn btn-link">Редактировать</button>
                                    </a>
                                </td>
                                <td>
                                    <button  class="btn btn-link" onclick="deleteTest(${test.id},this);return false;">Удалить</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    <c:if test="${empty requestScope.testsInfoData}">
                        <tr>empty data</tr>
                    </c:if>

                    </tbody>
                </table>
            </div>
        </div>

    </div>

</div>
<div id="content bor"></div>


</div>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
        integrity="" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
        integrity="" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="" crossorigin="anonymous"></script>
<script src="resources/js/addEditTest.js"></script>


</body>
</html>