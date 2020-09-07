<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


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
<fmt:message bundle="${loc}" key="nav-item.article" var="nav_item_articles"/>
<fmt:message bundle="${loc}" key="nav-item.assign_test" var="nav_item_assign_test"/>
<fmt:message bundle="${loc}" key="nav-item.results" var="nav_item_results"/>
<fmt:message bundle="${loc}" key="nav-item.assigned_test" var="nav_item_assigned_tests"/>
<fmt:message bundle="${loc}" key="nav-item.user.account" var="nav_item_user_account"/>


<nav class="navbar navbar-expand-lg navbar-light menu-color p-t-b-0 border-menu">
    <div class="collapse navbar-collapse start-page-nav-itm max-w-nav" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto start-page-nav-itm" id="myTab">
            <li class="nav-item">

                <a class="navbar-brand logo-color active " href="${pageContext.request.contextPath}/test?command=main_page">
                    <img alt="logo" class="logo-size" src="resources/img/logo.png">
                </a>
            </li>


            <c:if test="${not empty sessionScope.user_id}">

                <li class="nav-item">
                    <a class="nav-link nav-vrl item-start"
                       href="${pageContext.request.contextPath}/test?command=show_tests_page">${nav_item_tests}</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link nav-vrl item-start"
                       href="${pageContext.request.contextPath}/test?command=show_assigned_tests_page">${nav_item_assigned_tests}</a>
                </li>
            </c:if>


            <c:if test="${sessionScope.user_role=='ADMIN'}">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle nav-vrl item-start" href="#" id="navbarDropdown"
                       role="button" role="tab"
                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            ${nav_item_admin}
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item"
                           href="${pageContext.request.contextPath}/test?command=show_admin_panel">${nav_item_admin_tests}</a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item"
                           href="${pageContext.request.contextPath}/test?command=assign_test">${nav_item_assign_test}</a>
                    </div>
                </li>
                <li class="nav-item">
                    <a class="nav-link nav-vrl item-start"
                       href="${pageContext.request.contextPath}/test?command=tests_results">${nav_item_results}</a>
                </li>
            </c:if>

            <c:if test="${not empty sessionScope.user_id}">
                <li class="nav-item ">
                    <a class="nav-link nav-vrl item-start"
                       href="${pageContext.request.contextPath}/test?command=display_statistic">${nav_item_statistic}</a>
                </li>
            </c:if>

            <li class="nav-item">
                <a class="nav-link nav-vrl item-start"
                   href="${pageContext.request.contextPath}/test?command=about_us">${nav_item_about}</a>
            </li>
        </ul>
    </div>

    <form action="test" method="POST" class="m-0">
        <input type="hidden" name="command" value="change_language"/>
        <input type="hidden" name="local" value="ru"/>
        <button type="submit" class="btn ru"></button>
    </form>

    <form action="test" method="POST" class="m-0">
        <input type="hidden" name="command" value="change_language"/>
        <input type="hidden" name="local" value="en"/>
        <button type="submit" class="btn en"></button>
    </form>

    <c:if test="${not empty sessionScope.user_id}">
        <ul class="navbar-nav mr-auto start-page-nav-itm p-l-21">

            <li class="nav-item dropdown ">
                <a class="nav-link dropdown-toggle user-detail " href="#" id="navbarDropdownUser" role="button"
                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <i class="far fa-address-card fa-2x m-t-10"></i>
                    <span class="user-name"><c:out value="${sessionScope.user_login}"/></span>
                </a>


                <div class="dropdown-menu p-t-b-0" aria-labelledby="navbarDropdown-User">
                    <a class="dropdown-item user-drop-down p-t-b-5"
                       href="${pageContext.request.contextPath}/test?command=show_user_account">
                            ${nav_item_user_account} </a>
                    <div class="dropdown-divider"></div>

                    <form action="test" method="POST" class="m-0 txt-algn-center">
                        <input type="hidden" name="command" value="sign_out"/>
                        <button type="submit"
                                class="btn btn-outline-primary btn-sign-out">${button_sign_out}</button>
                    </form>
                </div>
            </li>
        </ul>
    </c:if>

</nav>