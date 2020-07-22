<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Welcome</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="resources/css/style.css"/>
    <link rel="stylesheet" href="resources/fontawesome-free-5.12.1-web/css/all.css">
</head>
<body>


<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="local/local" var="loc"/>
<fmt:message bundle="${loc}" key="label.field.login" var="lable_login"/>
<fmt:message bundle="${loc}" key="label.field.password" var="lable_password"/>
<fmt:message bundle="${loc}" key="label.field.first_name" var="lable_first_name"/>
<fmt:message bundle="${loc}" key="label.field.last_name" var="lable_last_name"/>
<fmt:message bundle="${loc}" key="label.field.email" var="lable_email"/>
<fmt:message bundle="${loc}" key="button.sign_in" var="button_sign_in"/>
<fmt:message bundle="${loc}" key="button.sign_up" var="button_sign_up"/>
<fmt:message bundle="${loc}" key="message.invalid_sign_in" var="message_invalid_sign_in"/>
<fmt:message bundle="${loc}" key="message.invalid_login" var="message_invalid_login"/>
<fmt:message bundle="${loc}" key="message.invalid_first_name" var="message_invalid_first_name"/>
<fmt:message bundle="${loc}" key="message.invalid_last_name" var="message_invalid_last_name"/>
<fmt:message bundle="${loc}" key="message.invalid_email" var="message_invalid_email"/>
<fmt:message bundle="${loc}" key="message.exists_email" var="message_exists_email"/>
<fmt:message bundle="${loc}" key="message.invalid_password" var="message_invalid_password"/>
<fmt:message bundle="${loc}" key="message.success_sign_up" var="message_success_sign_up"/>
<fmt:message bundle="${loc}" key="nav-tabs.log_in" var="nav_tabs_log_in"/>
<fmt:message bundle="${loc}" key="nav-tabs.sign_up" var="nav_tabs_sign_up"/>
<fmt:message bundle="${loc}" key="nav-item.article" var="nav_item_articles"/>
<fmt:message bundle="${loc}" key="message.start_page" var="message_start_page"/>
<fmt:message bundle="${loc}" key="button.language_en" var="button_language_en"/>
<fmt:message bundle="${loc}" key="button.language_ru" var="button_language_ru"/>
<fmt:message bundle="${loc}" key="message.exists_login" var="message_exists_login"/>
<fmt:message bundle="${loc}" key="button.sign_out" var="button_sign_out"/>
<fmt:message bundle="${loc}" key="nav-item.about" var="nav_item_about"/>


<div class="container-fluid colorForPage height-100 p-0">

    <jsp:include page="WEB-INF/jsp/parts/nav-menu.jsp"/>

    <div class="row align-items-center m-0 ">

        <c:if test="${empty sessionScope.user_id}">
            <div class="col-7">
                <div class="row p-t-25">
                    <div class="col text-center p-t-25">
                        <h4 class="letter_style">${message_start_page}</h4>
                    </div>
                </div>
                <div class="row p-t-25">
                    <div class="col"></div>
                    <div class="col">

                        <img src="resources/img/main-page-image.png"></div>

                    <div class="col"></div>
                </div>

            </div>
        </c:if>

        <c:if test="${not empty sessionScope.user_id}">
            <div class="col-12">
                <div class="row p-t-25">
                    <div class="col text-center p-t-25">
                        <h4 class="letter_style">${message_start_page}</h4>
                    </div>
                </div>
                <div class="row p-t-25">
                    <div class="col"></div>
                    <div class="col">
                        <img src="resources/img/main-page-image.png"></div>
                    <div class="col"></div>
                </div>
            </div>
        </c:if>

        <c:if test="${empty sessionScope.user_id}">
            <div class="col-4">
                <nav>
                    <div class="nav nav-tabs" id="nav-tab" role="tablist">
                        <c:choose>
                            <c:when test="${not empty requestScope.signUpError}">
                                <a class="nav-item nav-link text-tab-start-form" id="nav-home-tab" data-toggle="tab"
                                   href="#nav-home" role="tab"
                                   aria-controls="nav-home" aria-selected="false">${nav_tabs_log_in}</a>
                            </c:when>
                            <c:when test="${requestScope.signUpError==null}">
                                <a class="nav-item nav-link text-tab-start-form active" id="nav-home-tab"
                                   data-toggle="tab" href="#nav-home"
                                   role="tab"
                                   aria-controls="nav-home" aria-selected="true">${nav_tabs_log_in}</a>
                            </c:when>
                        </c:choose>
                        <c:choose>
                            <c:when test="${not empty requestScope.signUpError}">
                                <a class="nav-item nav-link text-tab-start-form active" id="nav-profile-tab"
                                   data-toggle="tab"
                                   href="#nav-profile" role="tab"
                                   aria-controls="nav-profile" aria-selected="true">${nav_tabs_sign_up}</a>
                            </c:when>
                            <c:when test="${requestScope.signUpError==null}">
                                <a class="nav-item nav-link text-tab-start-form" id="nav-profile-tab" data-toggle="tab"
                                   href="#nav-profile"
                                   role="tab"
                                   aria-controls="nav-profile" aria-selected="false">${nav_tabs_sign_up}</a>
                            </c:when>
                        </c:choose>
                    </div>
                </nav>
                <div class="tab-content bor-gray-solid bor" id="nav-tabContent">
                    <c:choose>
                    <c:when test="${not empty requestScope.signUpError}">
                    <div class="tab-pane fade" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">

                        </c:when>
                        <c:when test="${requestScope.signUpError==null}">
                        <div class="tab-pane fade show active" id="nav-home" role="tabpanel"
                             aria-labelledby="nav-home-tab">
                            </c:when>
                            </c:choose>


                            <div class="form">
                                <form action="test" class="form-horizontal" role="form" method="POST">
                                    <input type="hidden" name="command" value="sign_in"/>
                                    <div class="form-group">
                                        <div>
                                            <c:if test="${not empty requestScope.signInError}">
                                                <div class="alert alert-danger" role="alert">
                                                    <c:out value="${message_invalid_sign_in}"/></div>
                                            </c:if>

                                            <c:if test="${not empty requestScope.successMessage}">
                                                <div class="alert alert-success" role="alert">
                                                    <c:out value="${message_success_sign_up}"/></div>
                                            </c:if>


                                        </div>


                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-1"></div>

                                                <div class="col-sm-2"></div>
                                                <div class="col-sm-7"><label
                                                        class="control-label">${lable_login}</label>
                                                </div>
                                                <div class="col-sm-2"></div>
                                            </div>
                                            <div class="row">
                                                <div class="col-sm-1"></div>
                                                <div class="col-sm-2 p-l-27">
                                                    <i class="fas fa-user fa-2x color-icon"></i>
                                                </div>
                                                <div class="col-sm-7">
                                                    <input type="text" class="form-control" required name="login"></div>
                                                <div class="col-sm-2"></div>
                                            </div>

                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-1"></div>
                                                <div class="col-sm-2"></div>
                                                <div class="col-sm-7"><label
                                                        class="control-label">${lable_password}</label>
                                                </div>
                                                <div class="col-sm-2"></div>
                                            </div>
                                            <div class="row">
                                                <div class="col-sm-1"></div>

                                                <div class="col-sm-2 p-l-27">
                                                    <i class="fas fa-unlock-alt fa-2x color-icon"></i>
                                                </div>
                                                <div class="col-sm-7"><input type="password" class="form-control"
                                                                             required
                                                                             name="password"></div>
                                                <div class="col-sm-2"></div>
                                            </div>
                                        </div>


                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-3"></div>
                                                <div class="col-sm-7 p-top-7">
                                                    <button type="submit"
                                                            class="btn btn-outline-primary text-button-start-form">${button_sign_in}</button>
                                                </div>
                                                <div class="col-sm-2"></div>
                                            </div>
                                        </div>
                                    </div>

                                </form>
                            </div><!-- form  -->
                        </div>


                        <c:choose>
                        <c:when test="${not empty requestScope.signUpError}">
                        <div class="tab-pane fade show active" id="nav-profile" role="tabpanel"
                             aria-labelledby="nav-profile-tab">
                            </c:when>
                            <c:when test="${requestScope.signUpError==null}">
                            <div class="tab-pane fade" id="nav-profile" role="tabpanel"
                                 aria-labelledby="nav-profile-tab">
                                </c:when>
                                </c:choose>
                                <div class="form">
                                    <form action="test" class="form-horizontal" role="form" method="POST">
                                        <input type="hidden" name="command" value="sign_up"/>
                                        <div class="form-group">
                                            <div>

                                                <c:if test="${not empty requestScope.invalid_login}">
                                                    <div class="alert alert-danger" role="alert">
                                                        <c:out value="${message_invalid_login}"/></div>
                                                </c:if>

                                                <c:if test="${not empty requestScope.invalid_password}">
                                                    <div class="alert alert-danger" role="alert">
                                                        <c:out value="${message_invalid_password}"/></div>
                                                </c:if>

                                                <c:if test="${not empty requestScope.invalid_first_name}">
                                                    <div class="alert alert-danger" role="alert">
                                                        <c:out value="${message_invalid_first_name}"/></div>
                                                </c:if>

                                                <c:if test="${not empty requestScope.invalid_last_name}">
                                                    <div class="alert alert-danger" role="alert">
                                                        <c:out value="${message_invalid_last_name}"/></div>
                                                </c:if>

                                                <c:if test="${not empty requestScope.signUpExistsError}">
                                                    <div class="alert alert-danger" role="alert">
                                                        <c:out value="${message_exists_login}"/></div>
                                                </c:if>


                                                <c:if test="${not empty requestScope.invalid_email}">
                                                    <div class="alert alert-danger" role="alert">
                                                        <c:out value="${message_invalid_email}"/></div>
                                                </c:if>
                                                <c:if test="${not empty requestScope.existsEmail}">
                                                    <div class="alert alert-danger" role="alert">
                                                        <c:out value="${message_exists_email}"/></div>
                                                </c:if>


                                            </div>
                                            <div class="form-group">
                                                <div class="row">
                                                    <div class="col-sm-6 p-r-0">
                                                        <div class="form-group">
                                                            <label class="col-sm-7 control-label">${lable_first_name}</label>
                                                            <div class="col-sm-12">
                                                                <input type="text" class="form-control" required
                                                                       name="first_name">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-6 p-l-0">
                                                        <div class="form-group">
                                                            <label class="col-sm-7 control-label">${lable_last_name}</label>
                                                            <div class="col-sm-12">
                                                                <input type="text" class="form-control" required
                                                                       name="last_name">
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                            </div>
                                            <div class="form-group">
                                                <div class="row">

                                                    <label class="col-sm-3  control-label p-l-27" for="email">${lable_email}</label>
                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-12 p-right-27 p-l-27">
                                                        <input type="email" class="form-control" name="email" required
                                                               id="email" aria-describedby="emailHelp">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="row">
                                                    <label class="col-sm-3  control-label p-l-27">${lable_login}</label>
                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-12 p-right-27 p-l-27">
                                                        <input type="text" class="form-control" required name="login">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="row">
                                                    <label class="col-sm-2 p-l-27 control-label">${lable_password}</label>
                                                </div>
                                                <div class="row">

                                                    <div class="col-sm-12 p-right-27 p-l-27">
                                                        <input type="password" class="form-control" required
                                                               name="password">
                                                    </div>
                                                </div>

                                            </div>

                                            <div class="form-group">
                                                <div class="row">
                                                    <div class="col-sm-offset-2 col-sm-10 p-l-27 p-top-7">
                                                        <button type="submit"
                                                                class="btn btn-outline-primary text-button-start-form">${button_sign_up}
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
        </c:if>

    </div>
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