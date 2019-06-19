<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>

<table border ="1">
    <thead>
        <tr>
            <th>Naziv</th>
            <th>Adresa</th>
            <th>Pregled proizvoda</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="p" items="${proizvodjaci}">
            <tr>
                <th>
                    ${p.ime}
                </th>
                <th>
                    ${p.adresa}
                </th>
                <th>
                    <a href="">TODO</a>
                </th>
            </tr>
        </c:forEach>
    </tbody>
</table>