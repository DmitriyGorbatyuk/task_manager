<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--===========================================================================
JSTL core tag library.
===========================================================================--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--===========================================================================
JSTL functions tag library.
===========================================================================--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%--===========================================================================
JSTL i18n tag library.
===========================================================================--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <title>Tasks</title>

    <style>
        <%@include file="/static/css/bootstrap.min.css" %>
        <%@include file="/static/css/style.css"%>
        <%@include file="/static/js/jquery.min.js" %>
        <%@include file="/static/js/bootstrap.min.js" %>
    </style>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<body>
<div class="container-fluid">
    <br>

    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="/tasks">Task manager</a>
            </div>
            <ul class="nav navbar-nav">
                <li><a href="/tasks">All Task</a></li>
                <li><a href="/todaysTasks">Today</a></li>
                <li><a href="/datedTasks">Schedule</a></li>
                <li class="active"><a href="/categories">Categories</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <form method="post" action="/logout">
                        <input class="btn navbar-btn " type="submit" value="Log out"/>
                        <br>
                    </form>
                </li>
            </ul>
        </div>
    </nav>

    <div class="row">
        <div class="col-md-2">
        </div>

        <div class="col-md-8">
            <div class="form-group">
                <c:if test="${currentCategory.id ne 1}">
                    <a href="/categories?currentCategoryId=${currentCategory.rootId}"> <- Go back one level</a>
                    <br><br>
                </c:if>

                        <form action="/addCategory" method="post">
                            <div class="col-md-10">
                                <input type="text" name="newCategoryName" placeholder="Name" class="form-control"
                                       maxlength="44" required/>
                            </div>
                            <div class="col-md-2">
                                <input type="submit" value="Add" class="btn btn-default"/>
                            </div>
                        </form>


                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <td></td>
                                <td><a href="/categories?sort=name"><h6>Name</h6></a></td>
                                <td><a href="/categories?sort=complexity"><h6>Color</h6></a></td>
                                <td><a href="/categories?sort=time"><h6>Spent time</h6></a></td>
                                <td></td>
                                <td></td>
                            </tr>
                            </thead>


                            <c:choose>
                            <c:when test="${not empty categoriesToTime}">
                            <tbody>
                            <c:choose>
                                <c:when test="${not empty categoriesToTime}">
                                    <c:forEach items="${categoriesToTime}" var="entry">
                                        <tr>
                                            <form method="post" action="editCategory">
                                                <input type="text" name="currentCategoryId" value="${entry.category.id}" hidden>
                                            <td>
                                                <a href="/categories?currentCategoryId=${entry.category.id}">Inner
                                                    categories</a>
                                            </td>
                                            <td>
                                                <input class="form-control" type="text" name="newCategoryName"
                                                       value="${entry.category.name}"/>
                                            </td>

                                            <td>
                                                <input id="${entry.category.id}" style="background:${entry.category.color}"
                                                       onchange="changeColor(${entry.category.id})" class="button-circle"
                                                       type="color" name="newCategoryColor">
                                            </td>
                                            <td>${entry.formattedTime}</td>
                                            <td>
                                                    <input class="form-control btn btn-info" type="submit"
                                                           value="Edit"/>
                                            </td>
                                            </form>
                                            <td>
                                                <form method="post" action="/deleteCategory">
                                                    <input type="text" name="currentCategoryId" value="${entry.category.id}" hidden>
                                                    <input class="form-control btn btn-danger" type="submit"
                                                           value="Delete"/>
                                                </form>
                                            </td>

                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="6" class="text-center">There is not subcategories. You can add new
                                            one
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                            </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="6" class="text-center">There is no subcategories. You can add new one</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </table>
            </div>
        </div>

        <div class="col-md-2">
        </div>
    </div>
</div>
<script>
    <%@include file="/static/js/jquery.min.js" %>
    <%@include file="/static/js/scripts.js" %>
</script>
</body>
</html>