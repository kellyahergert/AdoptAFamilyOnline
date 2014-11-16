<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<!--   -----Added automatically by Eclipse, not needed?
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
-->

<head>
  <meta charset="UTF-8">
  <title>Email Setup - Adopt a Family</title>
  <link rel="stylesheet" type="text/css" href="styles.css" media="all">
</head>

<body>

<header>
  <nav>
    <span class="flowCurrent">Email Info</span>
    <span class="arrowFuture">&#8674;</span>
    <span class="flowFuture">Upload Files</span>
    <span class="arrowFuture">&#8674;</span>
    <span class="flowFuture">Family Matching</span>
    <span class="arrowFuture">&#8674;</span>
    <span class="flowFuture">Preview and Run!</span>
  </nav>
</header>

<section>
  <form method="POST" action="EmailSenderServlet">
    <h2>Email Info</h2>
    <p><label><span class=label_1>"Sender" Email Address: </span><input type=email name=emailInput maxlength="100" required></label></p>
    <p><label><span class=label_1>Mail Server IP Address: </span><input type=text name="smtpServerIP" placeholder="smtp.comcast.net" maxlength="100" required></label></p>
    <p><label><span class=label_1>Email Login: </span><input type=text name=emailLogin maxlength="100"></label></p>
    <p><label><span class=label_1>Email Password: </span><input type=password name=emailPassword></label></p>

    <h3>Send Test email</h3>
    <p>
      <label>
        <span class=label_3>Send test email to this email address: </span>
        <span class=label_1><input type=email name=testEmail maxlength="100" ></span>
      </label>
      <input type=submit name=sendTestSponsorEmail value="Send Test Email">
    </p>
    <!-- <p><span style="float:left;margin-left:275px;color:darkgrey;">Email results...</span></p> -->
    <p style="color:darkgrey;"><span class=label_3>&nbsp</span>${emailResponseMsg}</p>
    <br>
    <p><input type="submit" name=goToAaf2 value="Continue to Step 2"></p>

  </form>
</section>

</body>
</html>