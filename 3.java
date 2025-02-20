<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Training</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        .form-container {
            width: 600px;
            margin: 0 auto;
        }

        .form-group {
            margin-bottom: 15px;
        }

        label {
            display: inline-block;
            width: 100px;
            text-align: left;
        }

        input[type="text"],
        input[type="email"],
        select {
            background-color: #f0f0f0;
            border: 1px solid #ccc;
            padding: 5px;
            margin-left: 20px;
        }

        /* Specific widths for each input */
        #userId {
            width: 250px;
        }

        #username {
            width: 250px;
        }

        #sex {
            width: 150px;
        }

        #birthday {
            width: 200px;
        }

        #email {
            width: 250px;
        }

        textarea {
            width: 400px;
            height: 100px;
            background-color: #f0f0f0;
            border: 1px solid #ccc;
            padding: 5px;
            resize: none;
            margin-left: 20px;
        }

        .error-message {
            color: red;
            text-align: center;
            margin-bottom: 20px;
        }

        .button-container {
            margin-top: 20px;
            margin-left: 120px; /* Aligned with input fields (100px label + 20px margin) */
        }

        button {
            width: 100px;
            padding: 5px;
            background-color: #f0f0f0;
            border: 1px solid #000;
            cursor: pointer;
            margin-right: 20px;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <div class="error-message">
            &lt;ERRORMESSAGE>
        </div>

        <form>
            <div class="form-group">
                <label for="userId">UserID</label>
                <input type="text" id="userId" name="userId">
            </div>

            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username">
            </div>

            <div class="form-group">
                <label for="sex">Sex</label>
                <select id="sex" name="sex">
                    <option value="">Select sex</option>
                    <option value="male">Male</option>
                    <option value="female">Female</option>
                </select>
            </div>

            <div class="form-group">
                <label for="birthday">Birthday</label>
                <input type="text" id="birthday" name="birthday">
            </div>

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email">
            </div>

            <div class="form-group">
                <label for="address">Address</label>
                <textarea id="address" name="address" maxlength="256"></textarea>
            </div>

            <div class="button-container">
                <button type="submit">Login</button>
                <button type="reset">Clear</button>
            </div>
        </form>
    </div>
</body>
</html>
