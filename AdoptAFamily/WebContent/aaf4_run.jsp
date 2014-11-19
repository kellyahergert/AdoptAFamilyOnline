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
  <h2>Previews</h2>
  <!-- <p><a href=servletSponsorEmailPreview target=_>Sponsor Email Preview</a></p> -->
  <form method="POST" action="TestEmailServlet">
    <p>
      <label>
        <span class=label_4>Send test Sponsor email to this email address: </span>
        <span class=label_1><input type=email name=testEmail required></span>
      </label>
      <input type=submit name=sendTestSponsorEmail value="Send Test Email">
    </p>
  </form>
  
  <!-- <p><a href=nominatorEmailPreview  target=_>Nominator Email Preview</a></p> -->
  <form method="POST" action="TestEmailServlet">
    <p>
      <label>
        <span class=label_4 style="font-size:85%">Send test Nominator email to this email address: </span>
        <span class=label_1><input type=email name=testEmail required></span>
      </label>
      <input type=submit name=sendTestNominatorEmail value="Send Test Email">
    </p>
  </form>
</section>

<section>
  <form method="POST" action="RunServlet">
    <h2>Send All Emails</h2>
    <p><input type=submit name=run value="Run!"></p>
  </form>
</section>
<br>
<pre style="color:white;">${statusMsg}</pre>

<footer>
</footer>
</body>
</html>