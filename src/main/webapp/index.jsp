<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="resources/css/style.css"/>
    <link rel="stylesheet" href="resources/fontawesome-free-5.12.1-web/css/all.css">
</head>
<body>
<div class="container size">
    <div class="row align-items-center">
        <div class="col-3"></div>
        <div class="col-5">
            <nav>
                <div class="nav nav-tabs" id="nav-tab" role="tablist">
                    <c:choose>
                        <c:when test="${sign_up_error!=null}">
                            <a class="nav-item nav-link" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab"
                               aria-controls="nav-home" aria-selected="false">Log in</a>
                        </c:when>
                        <c:when test="${sign_up_error==null}">
                            <a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home"
                               role="tab"
                               aria-controls="nav-home" aria-selected="true">Log in</a>
                        </c:when>
                    </c:choose>
                    <c:choose>
                        <c:when test="${sign_up_error!=null}">
                            <a class="nav-item nav-link active" id="nav-profile-tab" data-toggle="tab"
                               href="#nav-profile" role="tab"
                               aria-controls="nav-profile" aria-selected="true">Sign up</a>
                        </c:when>
                        <c:when test="${sign_up_error==null}">
                            <a class="nav-item nav-link" id="nav-profile-tab" data-toggle="tab" href="#nav-profile"
                               role="tab"
                               aria-controls="nav-profile" aria-selected="false">Sign up</a>
                        </c:when>
                    </c:choose>
                </div>
            </nav>
            <div class="tab-content bor-gray-solid bor" id="nav-tabContent">
                <c:choose>
                <c:when test="${sign_up_error!=null}">
                <div class="tab-pane fade" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">

                    </c:when>
                    <c:when test="${sign_up_error==null}">
                    <div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
                        </c:when>
                        </c:choose>


                        <div class="form">
                            <form action="test" class="form-horizontal" role="form" method="POST">
                                <input type="hidden" name="command" value="sign_in"/>
                                <div class="form-group">
                                    <div>
                                        <c:if test="${sign_in_error!=null}">
                                            <div class="alert alert-danger" role="alert">${sign_in_error}</div>
                                        </c:if>

                                        <c:if test="${success_message!=null}">
                                            <div class="alert alert-success" role="alert">${success_message}</div>
                                        </c:if>


                                    </div>

                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-1"></div>

                                            <div class="col-sm-2"></div>
                                            <div class="col-sm-7"><label class="control-label">Login</label></div>
                                            <div class="col-sm-2"></div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-1"></div>
                                            <div class="col-sm-2 p-l-27">
                                                <i class="fas fa-user fa-2x color-dodgerblue"></i>
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
                                            <div class="col-sm-7"><label class="control-label">Password</label>
                                            </div>
                                            <div class="col-sm-2"></div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-1"></div>

                                            <div class="col-sm-2 p-l-27">
                                                <i class="fas fa-unlock-alt fa-2x color-dodgerblue"></i>
                                            </div>
                                            <div class="col-sm-7"><input type="password" class="form-control" required
                                                                         name="password"></div>
                                            <div class="col-sm-2"></div>
                                        </div>
                                    </div>


                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-3"></div>
                                            <div class="col-sm-7 p-top-7">
                                                <button type="submit" class="btn btn-outline-primary">Log in</button>
                                            </div>
                                            <div class="col-sm-2"></div>
                                        </div>
                                    </div>
                                </div>

                            </form>
                        </div><!-- form  -->
                    </div>


                    <c:choose>
                    <c:when test="${sign_up_error!=null}">
                    <div class="tab-pane fade show active" id="nav-profile" role="tabpanel"
                         aria-labelledby="nav-profile-tab">
                        </c:when>
                        <c:when test="${sign_up_error==null}">
                        <div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab">
                            </c:when>
                            </c:choose>
                            <div class="form">
                                <form action="test" class="form-horizontal" role="form" method="POST">
                                    <input type="hidden" name="command" value="sign_up"/>
                                    <div class="form-group">
                                        <div>

                                            <c:if test="${login_invalid!=null}">
                                                <div class="alert alert-danger" role="alert">
                                                        ${login_invalid}</div>
                                            </c:if>

                                            <c:if test="${password_invalid!=null}">
                                                <div class="alert alert-danger" role="alert">
                                                        ${password_invalid}</div>
                                            </c:if>

                                            <c:if test="${first_name_invalid!=null}">
                                                <div class="alert alert-danger" role="alert">
                                                        ${first_name_invalid}</div>
                                            </c:if>

                                            <c:if test="${last_name_invalid!=null}">
                                                <div class="alert alert-danger" role="alert">
                                                        ${last_name_invalid}</div>
                                            </c:if>

                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-6 p-r-0">
                                                    <div class="form-group">
                                                        <label class="col-sm-7 control-label">First name</label>
                                                        <div class="col-sm-12">
                                                            <input type="text" class="form-control" required
                                                                   name="first_name">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-6 p-l-0">
                                                    <div class="form-group">
                                                        <label class="col-sm-7 control-label">Last name</label>
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
                                                <label class="col-sm-3  control-label p-l-27">Login</label>
                                            </div>
                                            <div class="row">
                                                <div class="col-sm-12 p-right-27 p-l-27">
                                                    <input type="text" class="form-control" required name="login">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <label class="col-sm-2 p-l-27 control-label">Password</label></div>
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
                                                    <button type="submit" class="btn btn-outline-primary">Sign up
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
                <div class="col-3"></div>

            </div>

        </div>
    </div>
</div>


<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
        integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
        integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
        crossorigin="anonymous"></script>

</body>
</html>