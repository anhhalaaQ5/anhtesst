<!DOCTYPE html>
<html>
<head>
<style>
  html, body {
    height: 100%;
    margin: 0;
  }
  
  body {
    font-family: Arial, sans-serif;
    padding: 2% 5%;
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    box-sizing: border-box;
  }
  
  .content {
    flex: 1;
  }
  
  .header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
  }
  
  .yellow-section {
    background-color: #f4d03f;
    padding: 15px;
    margin: 20px 0;
    box-sizing: border-box;
  }
  
  .input-group {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    gap: 10px;
  }
  
  .input-section {
    display: flex;
    align-items: center;
    gap: 5px;
    min-width: max-content;
  }
  
  .name-section {
    flex: 0 1 25%;
    margin-left: 20px;
  }
  
  .sex-section {
    flex: 0 1 20%;
  }
  
  .dash-section {
    flex: 0 1 25%;
  }
  
  input[type="text"] {
    padding: 5px;
    border: 1px solid #ccc;
    min-width: 80px;
    max-width: 150px;
    width: 100%;
    box-sizing: border-box;
  }
  
  select {
    padding: 5px;
    border: 1px solid #ccc;
    width: 100px;
    box-sizing: border-box;
  }
  
  /* Checkbox styles */
  input[type="checkbox"] {
    appearance: none;
    -webkit-appearance: none;
    width: 16px;
    height: 16px;
    border: 2px solid black;
    background-color: white;
    position: relative;
    cursor: pointer;
  }
  
  input[type="checkbox"]:checked::after {
    content: '';
    position: absolute;
    top: 2px;
    left: 2px;
    width: 8px;
    height: 8px;
    background-color: black;
  }
  
  .navigation {
    display: flex;
    justify-content: space-between;
    margin: 20px 0;
    align-items: center;
  }
  
  .nav-group {
    display: flex;
    align-items: center;
    gap: 10px;
  }
  
  .nav-buttons {
    display: flex;
  }
  
  .nav-buttons button {
    margin: 0;
    border-right: none;
  }
  
  .nav-buttons button:last-child {
    border-right: 1px solid #999;
  }
  
  button {
    padding: 5px 10px;
    background-color: #e0e0e0;
    border: 1px solid #999;
    cursor: pointer;
  }
  
  .input-group button {
    width: 120px;
  }
  
  table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
  }
  
  th, td {
    border: 1px solid #999;
    padding: 8px;
    text-align: left;
  }
  
  th {
    background-color: #e0e0e0;
  }
  
  /* ID link styling with diagonal triangle */
  .id-link-container {
    position: relative;
    display: inline-block;
    overflow: hidden;
    padding-top: 10px;
    padding-left: 10px;
  }
  
  /* Diagonal triangle */
  .id-link-container:before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 0;
    height: 0;
    border-style: solid;
    border-width: 10px 10px 0 0;
    border-color: #000 transparent transparent transparent;
  }
  
  .id-link {
    color: blue;
    text-decoration: underline;
    cursor: pointer;
  }
  
  .table-container {
    background-color: #f4d03f;
    padding: 15px;
  }
  
  .action-buttons {
    margin-top: 20px;
    display: flex;
    gap: 10px;
  }
  
  .action-buttons button:first-child {
    width: 120px;
  }
  
  .action-buttons button:last-child {
    width: 60px;
  }
  
  .footer {
    margin-top: auto;
    padding-top: 20px;
    color: #666;
    font-size: 0.9em;
  }
  
  .footer hr {
    margin: 0 0 10px 0;
    border: none;
    border-top: 1px solid #000;
  }
  
  .blue-bar {
    background-color: blue;
    height: 20px;
    margin: 20px 0;
    width: 100%;
  }

  @media screen and (max-width: 768px) {
    .input-group {
      flex-wrap: wrap;
    }
    
    .input-section {
      flex: 1 1 auto;
    }
    
    .name-section {
      margin-left: 10px;
    }
    
    .input-group button {
      width: 100px;
    }
  }
</style>
</head>
<body>
  <div class="content">
    <div class="header">
      <div>Welcome <></div>
      <div>Log out</div>
    </div>
    
    <div class="blue-bar"></div>
    
    <div class="yellow-section">
      <div class="input-group">
        <div class="input-section name-section">
          <label>Name</label>
          <input type="text">
        </div>
        <div class="input-section sex-section">
          <label>Sex</label>
          <select>
            <option value="">Select...</option>
          </select>
        </div>
        <div class="input-section dash-section">
          <input type="text">
          <span>-</span>
          <input type="text">
        </div>
        <button>Search</button>
      </div>
    </div>
    
    <div class="navigation">
      <div class="nav-group">
        <div class="nav-buttons">
          <button><<</button>
          <button><</button>
        </div>
        <span>Previous</span>
      </div>
      <div class="nav-group">
        <span>Next</span>
        <div class="nav-buttons">
          <button>></button>
          <button>>></button>
        </div>
      </div>
    </div>
    
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th><input type="checkbox"></th>
            <th>ID</th>
            <th>Name</th>
            <th>Sex</th>
            <th>Birthday</th>
            <th>Address</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td><input type="checkbox"></td>
            <td>
              <div class="id-link-container">
                <span class="id-link">001</span>
              </div>
            </td>
            <td>John Doe</td>
            <td>Male</td>
            <td>01/01/1990</td>
            <td>123 Main St</td>
          </tr>
          <tr>
            <td><input type="checkbox"></td>
            <td>
              <div class="id-link-container">
                <span class="id-link">002</span>
              </div>
            </td>
            <td>Jane Smith</td>
            <td>Female</td>
            <td>05/15/1988</td>
            <td>456 Oak Ave</td>
          </tr>
        </tbody>
      </table>
    </div>
    
    <div class="action-buttons">
      <button>Add new</button>
      <button>Delete</button>
    </div>
  </div>
  
  <div class="footer">
    <hr>
    <div>design/itMKqdp8jnrUVnrYuuf29O/Untitled?node-id=1-2&t=7AqMvY4XzGDd5tEt-0</div>
  </div>
</body>
</html>
