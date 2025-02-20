<!DOCTYPE html>
<html>
<head>
<style>
  body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 20px;
    min-height: 100vh;
    display: flex;
    flex-direction: column;
  }

  .header {
    margin-bottom: 40px;
  }

  .header h1 {
    font-size: 24px;
    font-weight: normal;
    margin: 0 0 10px 0;
  }

  .header hr {
    border: none;
    border-top: 1px solid #000;
    margin: 0;
  }

  .welcome {
    margin-bottom: 80px;
  }

  .login-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 20px;
  }

  .title {
    font-size: 24px;
    margin-bottom: 5px;
  }

  .error-message {
    color: red;
    font-size: 14px;
    margin-bottom: 20px;
  }

  .form-group {
    display: grid;
    grid-template-columns: 80px 200px;
    gap: 10px;
    align-items: center;
    margin-bottom: 10px;
  }

  .form-group label {
    text-align: left;
  }

  .form-group input {
    background-color: #e0e0e0;
    border: 1px solid #999;
    padding: 5px;
  }

  .buttons {
    display: flex;
    gap: 20px;
    margin-top: 10px;
    width: 290px;
    position: relative;
  }

  .buttons button {
    width: 80px;
    padding: 3px;
    background-color: #e0e0e0;
    border: 1px solid #999;
    cursor: pointer;
  }

  .buttons button:first-child {
    position: absolute;
    left: 35px; /* Adjusted to extend into the middle space */
  }

  .buttons button:last-child {
    margin-left: auto;
    transform: translateX(20px);
  }

  .footer {
    margin-top: auto;
    padding-top: 20px;
    font-size: 12px;
    color: #666;
  }

  .footer hr {
    border: none;
    border-top: 1px solid #000;
    margin: 0 0 10px 0;
  }
</style>
</head>
<body>
  <div class="header">
    <h1>Training</h1>
    <hr>
  </div>

  <div class="welcome">
    Welcome <>
  </div>

  <div class="login-container">
    <div class="title">UserID</div>
    <div class="error-message">&lt;ERRORMESSSAGE&gt;</div>

    <div class="form-group">
      <label>UserID</label>
      <input type="text">
    </div>

    <div class="form-group">
      <label>Password</label>
      <input type="password">
    </div>

    <div class="buttons">
      <button>Login</button>
      <button>Clear</button>
    </div>
  </div>

  <div class="footer">
    <hr>
    <div>design/itMKqdp8jnrUVnrYuuf29O/Untitled?node-id=1-2&t=7AqMvY4XzGDd5tEt-0</div>
  </div>
</body>
</html>
