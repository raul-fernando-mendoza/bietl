<html>
<head>
<meta charset="ISO-8859-1">
<title>LoginCognito</title>
</head>
<body>
	<b>Please login ${name}</b>
<div class="container">
    <div class="row">
        <div class="col">
            Authenticated successfully as [[${#authentication.name}]]<br/>
            Principal: [[${#authentication.principal}]]
            <div>
                <a th:href="@{/logout}" class="btn btn-primary">Logout</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>