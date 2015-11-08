<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Preview and Run - Adopt a Family</title>
  <link rel="stylesheet" type="text/css" href="styles.css" media="all">
</head>

<body>

<header>
  <nav>
    <a class="flowPrevious" href="aaf1_email_info.jsp">Email Info</a>
    <span class="arrowPrevious">&#8674;</span>
    <a class="flowPrevious" href="aaf2_files.html">Upload PDFs</a>
    <span class="arrowPrevious">&#8674;</span>
    <a class="flowPrevious" href="aaf2.1_files2.html">Upload Files</a>
    <span class="arrowPrevious">&#8674;</span>
    <a class="flowPrevious" href="aaf3_family_matching.html">Family Matching</a>
    <span class="arrowPrevious">&#8674;</span>
    <span class="flowCurrent">Preview and Run!</span>
  </nav>
</header>

<section>
  <h2>Email Previews</h2>
  <datalist id=emailAddressList>
    <option label="jordhergert@gmail.com" value="jordhergert@gmail.com">
    <option label="kellyahergert@gmail.com" value="kellyahergert@gmail.com">
    <option label="jlittlejohn@denrescue.org" value="jlittlejohn@denrescue.org">
  </datalist>
  
  <form method="POST" action="TestEmailServlet">
    <p>
      <label>
        <span class=label_5>Send test Sponsor email to this email address: </span>
        <span class=label_1><input type=email list=emailAddressList name=testEmail required></span>
      </label>
      <input type=submit name=sendTestSponsorEmail value="Send Test Email">
    </p>
  </form>
  
  <form method="POST" action="TestEmailServlet">
    <p>
      <label>
        <span class=label_5>Send test Nominator email to this email address: </span>
        <span class=label_1><input type=email list=emailAddressList name=testEmail required></span>
      </label>
      <input type=submit name=sendTestNominatorEmail value="Send Test Email">
    </p>
  </form>
  
  <form method="POST" action="TestEmailServlet">
    <p>
      <label>
        <span class=label_5>Send test Wait List email to this email address: </span>
        <span class=label_1><input type=email list=emailAddressList name=testEmail required></span>
      </label>
      <input type=submit name=sendTestWaitListEmail value="Send Test Email">
    </p>
  </form>
</section>

<section>
  <form method="POST" action="RunServlet">
    <h2>Send All Emails</h2>
    <p><input name="sendEmailsCheckbox" type="checkbox">&nbsp;&nbsp;Send Emails</p>
    <p><input type=submit name=run value="Run!"></p>
  </form>
</section>
<br>

<pre style="color:white;padding-left:33px;">${statusMsg}</pre>

<footer>
</footer>
</body>
</html>