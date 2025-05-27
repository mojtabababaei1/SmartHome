const char First_WebPage[] PROGMEM = R"=(
<!DOCTYPE HTML>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Refrigerator Web Server</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <style>
    body {
    background: url('modem-connection.jpg') no-repeat center center fixed; /* لینک عکس پس‌زمینه */
    background-size: cover;
    font-family: 'Arial', sans-serif;
    color: #333;
}

.container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background: rgba(255, 255, 255, 0.8); /* ایجاد پس‌زمینه شفاف */
    padding: 20px;
    border-radius: 10px;
}

.card {
    background: rgba(255, 255, 255, 0.9); /* کمی شفاف‌تر برای وضوح بیشتر */
    border-radius: 10px;
}

h1 {
    font-size: 1.5rem;
    margin-bottom: 20px;
}

@media (max-width: 768px) {
    h1 {
        font-size: 1.25rem;
    }
    .container {
        padding: 10px;
    }
}

@media (max-width: 576px) {
    h1 {
        font-size: 1rem;
    }
    .container {
        padding: 5px;
    }
}
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center align-items-center vh-100">
            <div class="col-md-6">
                <div class="card p-4 shadow-lg">
                    <h1 class="text-center text-danger">Please enter the SSID and Password of your home local network</h1>
                    <form action="/mv/connect1" method="post" class="mt-4">
                        <div class="mb-3">
                            <label for="ssid" class="form-label">SSID:</label>
                            <input type="text" id="ssid" name="ssid" class="form-control">
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Password:</label>
                            <input type="text" id="password" name="password" class="form-control">
                        </div>
                        <div class="text-center">
                            <input type="submit" value="Connect" class="btn btn-primary w-100">
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
)=";
