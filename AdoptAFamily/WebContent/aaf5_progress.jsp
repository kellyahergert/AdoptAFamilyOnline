<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Progress - Adopt a Family</title>
  <link rel="stylesheet" type="text/css" href="styles.css" media="all">
</head>

<body>
<section>
  <form method="POST" action="servletStop">
    <!-- <h2>Running...</h2> -->
    <p><input type=submit name=stop value="Stop"></p>
  </form>
</section>

<p style="margin-left:30px;margin-bottom:1px">Progress:</p>
<p style="color:darkgray;margin-left:50px;margin-top:1px">

<!-- Maybe even a progress bar :) -->

<table>
  <tbody>
    <tr>"<%= request.getAttribute("message") %>"</tr>
  </tbody>
</table>

</p>

</body>
</html>