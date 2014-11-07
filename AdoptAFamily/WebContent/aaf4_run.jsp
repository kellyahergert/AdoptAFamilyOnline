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
    <a class="flowPrevious" href="aaf1_email_info.html">Email Info</a>
    <span class="arrowPrevious">&#8674;</span>
    <a class="flowPrevious" href="aaf2_files.html">Upload Files</a>
    <span class="arrowPrevious">&#8674;</span>
    <a class="flowPrevious" href="aaf3_family_matching.html">Family Matching</a>
    <span class="arrowPrevious">&#8674;</span>
    <span class="flowCurrent">Preview and Run!</span>
  </nav>
</header>

<section>
  <h2>Previews</h2>
  <p><a href=servletSponsorEmailPreview target=_>Sponsor Email Preview</a></p>
  <form method="POST" action="servletSponsorEmailTest">
    <p>
      <label>
        <span class=label_4>Send test Sponsor email to this email address: </span>
        <span class=label_1><input type=email name=testEmail required></span>
      </label>
      <input type=submit name=sendTestSponsorEmail value="Send Test Email">
    </p>
  </form>

  <p><a href=servletFamilyEmailPreview target=_>Family Email Preview</a></p>
  <form method="POST" action="servletSponsorEmailTest">
    <p>
      <label>
        <span class=label_4>Send test Family email to this email address: </span>
        <span class=label_1><input type=email name=testEmail required></span>
      </label>
      <input type=submit name=sendTestFamilyEmail value="Send Test Email">
    </p>
  </form>
</section>

<section>
  <form method="POST" action="servletRun">
    <h2>Send All Emails</h2>
    <p><input type=submit name=run value="Run!"></p>
  </form>
</section>

<footer>
</footer>
</body>
</html>