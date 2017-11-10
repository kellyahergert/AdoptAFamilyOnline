<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

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
    <span class="flowFuture">Upload PDFs</span>
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
    <p><label><span class=label_1>Mail Server IP Address: </span>
      <input type=text list=mailServerList name="smtpServerIP" maxlength="100" value="${smtpServerIP}" required>
      <datalist id=mailServerList>
        <option label="smtp.comcast.net" value="smtp.comcast.net">
        <!-- <option label="smtp.office365.com" value="smtp.office365.com"> -->
        <option label="Internal Relay" value="10.10.16.236">
      </datalist>
      </label>
    </p>
    <p><label><span class=label_1>Mail Server Port: </span>
      <input type=text list=portList name="smtpServerPort" maxlength="5" value="${smtpServerPort}" required></label>
      <datalist id=portList>
        <option label="587" value="587">
        <option label="25" value="25">
      </datalist>
    </p>
    <p><label><span class=label_1>Email Login: </span>
      <input type=text list=loginList name="smtpServerLogin" maxlength="100" value="${smtpServerLogin}"></label>
      <datalist id=loginList>
        <option label="jordhergert@comcast.net" value="jordhergert@comcast.net">
      </datalist>
    </p>
    <p><label><span class=label_1>Email Password: </span>
      <input type=password name="smtpServerPassword" value="${smtpServerPassword}"></label>
    </p>

    <h3>Send Test email</h3>
    <p><label><span class=label_3>"Sender" Email Address: </span>
      <input type=email list=fromEmailList name="testEmailFromAddr" maxlength="100" value=""></label>
      <datalist id=fromEmailList>
        <option label="test@monkey.com" value="test@monkey.com">
        <option label="jlittlejohn@denrescue.org" value="jlittlejohn@denrescue.org">
      </datalist>
    </p>
    <p>
      <label>
        <span class=label_3>Send test email to this email address: </span>
        <span class=label_1><input type=email list=toEmailList name="testEmailToAddr" maxlength="100" value=""></span>
        <datalist id=toEmailList>
          <option label="jordhergert@gmail.com" value="jordhergert@gmail.com">
          <option label="kellyahergert@gmail.com" value="kellyahergert@gmail.com">
        </datalist>
      </label>
      <input type=submit name="sendTestSponsorEmail" value="Send Test Email">
    </p>
    <p style="color:darkgrey;"><span class=label_3>&nbsp;</span>${emailResponseMsg}</p>
    <p><input type="submit" name="goToAaf2" value="Continue to Step 2"></p>
    
    <br>
    <hr>
    
    <h3 title="Only select this if you already ran through normally and saved data to the database">
      Run with Last Uploaded Data
    </h3>
    <p title="Only select this if you already ran through normally and saved data to the database">
      <input type="submit" name="goToAafDbFiles" value="Skip to Upload Files">
    </p>
    
  </form>
</section>

</body>
</html>
