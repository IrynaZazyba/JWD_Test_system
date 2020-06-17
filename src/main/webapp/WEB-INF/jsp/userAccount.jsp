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
<fmt:message bundle="${loc}" key="label.field.first_name" var="lable_first_name"/>
<fmt:message bundle="${loc}" key="label.field.last_name" var="lable_last_name"/>
<fmt:message bundle="${loc}" key="button.save_changes" var="button_save_changes"/>
<fmt:message bundle="${loc}" key="button.language_en" var="button_language_en"/>
<fmt:message bundle="${loc}" key="button.language_ru" var="button_language_ru"/>


<div class="container-fluid p-0">

    <jsp:include page="parts/nav-menu.jsp"/>

    <div class="row height-90">
        <div class="col-2 background-gradient height-100 p-l-15 p-r-0">
            <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">

                <a class="nav-link vertical-menu active" id="v-pills-home-tab" data-toggle="pill" href="#v-pills-home"
                   role="tab"
                   aria-controls="v-pills-test" aria-selected="true">${nav_link_account_settings}</a>
            </div>


        </div>
        <div class="col-9">
            <div class="tab-content " id="v-pills-tabContent">
                <div class="tab-pane fade  active show " id="v-pills-home" role="tabpanel" aria-labelledby="v-pills-home-tab">


                    <div class="row m-t-15">
                        <div class="col-sm-3 p-l-27 p-t-27">
                            <div class="media">
                                <img src="resources/img/minava.png" class="mr-3 img-size round bor" alt="ava">
                            </div>
                        </div>
                        <div class="col-sm-9 p-l-27">


                            <div class="form">
                                <form id="formElem" onsubmit="" enctype="multipart/form-data" accept-charset="UTF-8"
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
                                                <label class="col-sm-3  control-label ">${lable_login}</label>
                                                <div class="col-sm-9">
                                                    <input type="text" class="form-control" required name="login"
                                                           value="${requestScope.user_info.login}">
                                                </div>
                                            </div>

                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <label class="col-sm-3 control-label">${lable_password}</label>
                                                <div class="col-sm-9 ">
                                                    <input type="password" class="form-control" required
                                                           name="password" value="${requestScope.user_info.password}">
                                                </div>
                                            </div>

                                        </div>

                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-offset-2 col-sm-10 p-top-7">
                                                    <button type="submit"
                                                            class="btn btn-outline-primary">${button_save_changes}
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div><!-- form  -->
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>

</div>
</div>
</div>
<div id="content bor"></div>


</div>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
        integrity=""        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
        integrity=""        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity=""        crossorigin="anonymous"></script>
<script src="resources/js/script.js"></script>


</body>
</html>