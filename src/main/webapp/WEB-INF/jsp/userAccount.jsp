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
<fmt:message bundle="${loc}" key="nav-link.assigned_tests" var="nav_link_assigned_tests"/>
<fmt:message bundle="${loc}" key="nav-link.account_settings" var="nav_link_account_settings"/>
<fmt:message bundle="${loc}" key="label.field.login" var="lable_login"/>
<fmt:message bundle="${loc}" key="label.field.password" var="lable_password"/>
<fmt:message bundle="${loc}" key="label.field.email" var="lable_email"/>
<fmt:message bundle="${loc}" key="label.field.first_name" var="lable_first_name"/>
<fmt:message bundle="${loc}" key="label.field.last_name" var="lable_last_name"/>
<fmt:message bundle="${loc}" key="label.field.old_password" var="label_old_password"/>
<fmt:message bundle="${loc}" key="label.field.new_password" var="label_new_password"/>
<fmt:message bundle="${loc}" key="account.title.change_password" var="title_change_password"/>
<fmt:message bundle="${loc}" key="account.title.edit_data" var="title_edit_data"/>
<fmt:message bundle="${loc}" key="button.change_password" var="button_change_password"/>

<fmt:message bundle="${loc}" key="button.save_changes" var="button_save_changes"/>
<fmt:message bundle="${loc}" key="button.language_en" var="button_language_en"/>
<fmt:message bundle="${loc}" key="button.language_ru" var="button_language_ru"/>


<div class="container-fluid p-0">

    <jsp:include page="parts/nav-menu.jsp"/>

    <div class="row height-90 m-27">
        <div class="col-2">
            <div class="row m-t-15">
                <div class="col p-l-27 p-t-27">
                    <div class="media">
                        <img src="resources/img/minava.png" class="mr-3 img-size round bor" alt="ava">
                    </div>
                </div>
            </div>

        </div>
        <div class="col-4">
            <h6>${title_edit_data}</h6>
            <div class="form userAccountCard">
                <form id="formElem" onsubmit="changeUserInfo();return false;" enctype="multipart/form-data"
                      accept-charset="UTF-8"
                      class="form-horizontal m-0" role="form">
                    <input type="hidden" name="command" value="edit_user"/>
                    <div class="form-group">
                        <div>

                            <div id="message">

                            </div>

                        </div>
                        <div class="form-group">
                            <div class="row">

                                <label class="col-sm-3 control-label">${lable_first_name}</label>
                                <div class="col-sm-9">
                                    <input type="text" class="form-control" required
                                           name="first_name"
                                           value="${requestScope.user_info.firstName}">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <label class="col-sm-3 control-label">${lable_last_name}</label>
                                <div class="col-sm-9">
                                    <input type="text" class="form-control" required
                                           name="last_name" value="${requestScope.user_info.lastName}">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <label class="col-sm-3 control-label" for="email">${lable_email}</label>
                                <div class="col-sm-9">
                                    <input disabled type="email" class="form-control" id="email"
                                           name="email" value="${requestScope.user_info.email}"
                                           aria-describedby="emailHelp">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <label class="col-sm-3  control-label ">${lable_login}</label>
                                <div class="col-sm-9">
                                    <input type="text" class="form-control" required name="login"
                                           value="${requestScope.user_info.login}">
                                </div>
                            </div>

                        </div>
                        <div class="form-group">
                            <div class="row">
                                <div class="col-sm-offset-2 col-sm-10 p-top-7">
                                    <button type="submit"
                                            class="card-btn btn btn-outline-primary">${button_save_changes}
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="col-4">
            <h6>${title_change_password}</h6>
            <div class="form userAccountCard">
                <form id="changePassword" onsubmit="changePassword(this);return false;" enctype="multipart/form-data"
                      accept-charset="UTF-8"
                      class="form-horizontal m-0" role="form">
                    <input type="hidden" name="command" value="change_password"/>
                    <div class="form-group">
                        <div>

                            <div id="passMessage">

                            </div>

                        </div>
                        <div class="form-group">
                            <div class="row">
                                <label class="col-sm-3 control-label">${label_old_password}</label>
                                <div class="col-sm-9 ">
                                    <input type="password" class="form-control" required
                                           name="oldPassword">
                                </div>
                            </div>

                        </div>
                        <div class="form-group">
                            <div class="row">
                                <label class="col-sm-3 control-label">${label_new_password}</label>
                                <div class="col-sm-9 ">
                                    <input type="password" class="form-control" required
                                           name="password">
                                </div>
                            </div>

                        </div>

                        <div class="form-group">
                            <div class="row">
                                <div class="col-sm-offset-2 col-sm-10 p-top-7">
                                    <button type="submit"
                                            class="card-btn btn btn-outline-primary">${button_change_password}
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>


        </div>
        <div class="col-1"></div>
    </div>
    <div id="content bor"></div>


</div>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
        integrity="" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
        integrity="" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="" crossorigin="anonymous"></script>
<script src="resources/js/script.js"></script>


</body>
</html>