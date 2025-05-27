#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>                                                        // Include the WebServer library
#include <EEPROM.h>
#include "Index1.h"
#include <ArduinoJson.h>
#include <Ticker.h>

Ticker timer;

String FridgeTemp = "";
String FreezeTemp = "";
String FridgeDoor = "";
String FreezeDoor = "";

const char* ssid_defult = "Refrigerator RnD";
const char* password_defult = "123456789";

WiFiClient espClient;
String macAddress;

String ssidInput, passwordInput;

ESP8266WebServer server(80);                                                         // Create a webserver object that listens HTTP request on port 80

void setupAP(void);
void setupWebServer(void);
void saveConfig(String ssid_new, String password_new);
void recoverConfig(void);
void HandleRoot(void);                                                               // function prototypes for HTTP handlers
void HandleRoot1(void);
void HandleNotFound(void);
void HandleConnect(void);
void HandleConnect1(void);
void HandleControl(void);
void Monitoring(void);
void HandleAtoE(void);
void HandleControl1(void);
void HandleMonitorTemps(void);
void HandleMonitorDoors(void);
void HandleLock(void);
void HandleZone(void); // New handler
void HandleFunction(void); // New handler
void HandleTemp(void); // New handler

int sensor;

const int8_t array_fast_temp [] = {-99
,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99
,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99
,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99,-99
,-99,-99,-99,-99,-99,-40,-40,-40,-40,-40,-40,-39,-39,-39,-39,-39,-39,-38,-38,-38,-38,-38,-38,-38,-37,-37,-37,-37,-37,-37
,-36,-36,-36,-36,-36,-36,-36,-35,-35,-35,-35,-35,-35,-35,-35,-34,-34,-34,-34,-34,-34,-34,-33,-33,-33,-33,-33,-33,-33,-33
,-32,-32,-32,-32,-32,-32,-32,-32,-32,-31,-31,-31,-31,-31,-31,-31,-31,-30,-30,-30,-30,-30,-30,-30,-30,-30,-29,-29,-29,-29
,-29,-29,-29,-29,-29,-29,-28,-28,-28,-28,-28,-28,-28,-28,-28,-27,-27,-27,-27,-27,-27,-27,-27,-27,-27,-26,-26,-26,-26,-26
,-26,-26,-26,-26,-26,-26,-25,-25,-25,-25,-25,-25,-25,-25,-25,-25,-25,-24,-24,-24,-24,-24,-24,-24,-24,-24,-24,-24,-23,-23
,-23,-23,-23,-23,-23,-23,-23,-23,-23,-22,-22,-22,-22,-22,-22,-22,-22,-22,-22,-22,-22,-21,-21,-21,-21,-21,-21,-21,-21,-21
,-21,-21,-21,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-19,-19,-19,-19,-19,-19,-19,-19,-19,-19,-19,-19,-19,-18
,-18,-18,-18,-18,-18,-18,-18,-18,-18,-18,-18,-18,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-16,-16,-16,-16
,-16,-16,-16,-16,-16,-16,-16,-16,-16,-16,-15,-15,-15,-15,-15,-15,-15,-15,-15,-15,-15,-15,-15,-15,-14,-14,-14,-14,-14,-14
,-14,-14,-14,-14,-14,-14,-14,-14,-13,-13,-13,-13,-13,-13,-13,-13,-13,-13,-13,-13,-13,-13,-13,-12,-12,-12,-12,-12,-12,-12
,-12,-12,-12,-12,-12,-12,-12,-12,-11,-11,-11,-11,-11,-11,-11,-11,-11,-11,-11,-11,-11,-11,-11,-10,-10,-10,-10,-10,-10,-10
,-10,-10,-10,-10,-10,-10,-10,-10,-10,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-8,-8,-8,-8,-8,-8,-8,-8,-8,-8,-8,-8
,-8,-8,-8,-7,-7,-7,-7,-7,-7,-7,-7,-7,-7,-7,-7,-7,-7,-7,-7,-7,-6,-6,-6,-6,-6,-6,-6,-6,-6,-6,-6,-6,-6,-6,-6,-6,-5,-5,-5,-5
,-5,-5,-5,-5,-5,-5,-5,-5,-5,-5,-5,-5,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-3,-3,-3,-3,-3,-3,-3,-3,-3,-3,-3
,-3,-3,-3,-3,-3,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0,0
,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,3,3,3
,3,3,3,3,3,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,7,7,7,7,7
,7,7,7,7,7,7,7,7,7,7,7,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,10,10,10,10,10,10,10,10,10,10,10
,10,10,10,10,10,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,13,13,13,13
,13,13,13,13,13,13,13,13,13,13,13,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,15,15,15,15,15,15,15,15,15,15,15,15,15,15
,15,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,17,17,17,17,17,17,17,17,17,17,17,17,17,17,18,18,18,18,18,18,18,18,18,18
,18,18,18,18,19,19,19,19,19,19,19,19,19,19,19,19,19,19,20,20,20,20,20,20,20,20,20,20,20,20,20,21,21,21,21,21,21,21,21,21
,21,21,21,21,21,22,22,22,22,22,22,22,22,22,22,22,22,22,23,23,23,23,23,23,23,23,23,23,23,23,24,24,24,24,24,24,24,24,24,24
,24,24,24,25,25,25,25,25,25,25,25,25,25,25,25,26,26,26,26,26,26,26,26,26,26,26,27,27,27,27,27,27,27,27,27,27,27,27,28,28
,28,28,28,28,28,28,28,28,28,29,29,29,29,29,29,29,29,29,29,29,30,30,30,30,30,30,30,30,30,30,30};

void setup() {
  Serial.begin(115200);                                 // Start the serial communication to send messages to the computer

  timer.attach(15, Monitoring);

  pinMode(D0, OUTPUT);
  pinMode(D1, OUTPUT);
  pinMode(D2, OUTPUT);
  //pinMode(D3, INPUT);
  //pinMode(D4, INPUT);
  pinMode(D5, INPUT);
  pinMode(D6, INPUT);
  pinMode(A0, INPUT);
  digitalWrite (D0 , LOW);
  digitalWrite (D1 , LOW);
  digitalWrite (D2 , LOW);

  setupAP();
  setupWebServer();
  recoverConfig();
}

void setupAP(void)
{
  WiFi.mode(WIFI_AP_STA);
  Serial.printf("Create Access Point with SSID = %s & Password = %s",ssid_defult,password_defult);
  Serial.println("\nSetting soft-AP ...");
  Serial.println(WiFi.softAP(ssid_defult, password_defult)? "Ready" : "Failed!");
  Serial.print("Soft-AP IP Address: ");
  Serial.println(WiFi.softAPIP());
}

void setupWebServer(void)
{
  server.on("/", HandleRoot);                        // Call the "HandleRoot" function when a client requests URL "/"
  server.on("/mv", HandleRoot1);
  server.onNotFound(HandleNotFound);                 // when a client requests an unknown URL (i.e. something other than "/"), call function "HandleNotFound"
  server.on("/connect", HandleConnect);
  server.on("/mv/connect1", HandleConnect1);
  server.on("/mv/control1", HandleControl1);
  server.on("/getMethod", HandleControl);
  server.on("/atoe", HandleAtoE);

  server.on("/api/lock", HTTP_POST, HandleLock);
  server.on("/api/zone", HTTP_POST, HandleZone);
  server.on("/api/function", HTTP_POST, HandleFunction);
  server.on("/api/temp", HTTP_POST, HandleTemp);

  server.on("/api/MonitorTemps", HTTP_GET, HandleMonitorTemps); // MUST ADDED
  server.on("/api/MonitorDoors", HTTP_GET, HandleMonitorDoors); // MUST ADDED

  Serial.println("Starting WebServer in ESP8266...");
  server.begin();
  Serial.println("HTTP Server started.");
}

void saveConfig(String ssid_new, String password_new)
{
  EEPROM.begin(512);
  int r = 2, t = 2;
  int k = ssid_new.length();
  int l = password_new.length();
  EEPROM.write(0, k);
  EEPROM.write(1, l);
  Serial.printf("\nssid_new_length: %d.\n",k);
  Serial.printf("\npassword_new_length: %d.\n",l);
  for( r ; r < ssid_new.length() + 2; r++)
  {
    if(ssid_new[r-2] == '\0')
    {
      EEPROM.write(r, ssid_new[r-2]);
      break;
    }else
    {
      EEPROM.write(r, ssid_new[r-2]);
    } 
  }
  Serial.print("For save ssid: ");
  Serial.printf("%d -\n",r);
  Serial.printf("ssid_new for save : %s \n",ssid_new.c_str());
  for (t = 2 ; t < password_new.length() + 2 ; t++)
  {
    if(password_new[t-2] == '\0')
    {
      EEPROM.write(t + 32, password_new[t-2]);
      break;
    }else
    {
      EEPROM.write(t + 32, password_new[t-2]);
    } 
  }
  EEPROM.commit();
  Serial.print("For save password: ");
  Serial.printf("%d -\n",t);
  Serial.printf("password_new for save : %s \n",password_new.c_str());
  Serial.println("********    Saved!  *********");
}

void recoverConfig(void)
{
  EEPROM.begin(512);
  String ssidFromMemory = "";
  String passwordFromMemory = "";
  int i = 2, j = 34;
  int p = EEPROM.read(0);
  int q = EEPROM.read(1);
  Serial.printf("\np = %d.\n",p);
  Serial.printf("\nq = %d.\n",q);
  for (i ; i < p + 2 ; i++)
  {
    char c = EEPROM.read(i);
    if(c != 0)
    {
      ssidFromMemory += c;
    }
  }
  for (j ; j < q + 34 ; j++)
  {
    char d = EEPROM.read(j);
    if(d != 0)
    {
      passwordFromMemory += d;
    }
  }

  Serial.printf("*****//////  ssidFromMemory : %s     ****\n",ssidFromMemory.c_str());
  Serial.printf("*****//////  passwordFromMemory : %s     ****\n",passwordFromMemory.c_str());
  
  if(ssidFromMemory != "" && passwordFromMemory != "")
  {
    WiFi.begin(ssidFromMemory.c_str(), passwordFromMemory.c_str());
    unsigned char b = 0;
    while(WiFi.status() != WL_CONNECTED)
    {
      delay(500);
      Serial.print("->");
      b++;

      if(b > 30)
      {
        break;
      }
      if(WiFi.status() == WL_CONNECTED)
      {
        Serial.println("\n********    IS Set!  *********");
        macAddress = WiFi.macAddress();
        Serial.println("");
        Serial.println("WiFi connected");
        Serial.println("IP address: ");
        Serial.println(WiFi.localIP());
        Serial.println("");
        Serial.print("MAC Address : ");
        Serial.println(macAddress);
        break;
      }
    }
    if(WiFi.status() != WL_CONNECTED)
    {
      Serial.println("\n********    Connection Failed!  *********");
      Serial.print("WiFi Status: ");
      Serial.println(WiFi.status());
    }
  }
}

void HandleRoot(void) {
  if (WiFi.status() == WL_CONNECTED) {
    ssidInput = WiFi.SSID();
    String json = "{\"status\":\"connected\",\"ssid\":\"" + ssidInput + "\",\"ip\":\"" + WiFi.localIP().toString() + "\"}";
    server.send(200, "application/json", json);
  } else {
    String json = "{\"status\":\"disconnected\"}";
    server.send(200, "application/json", json);
  }
}

void HandleRoot1(void)
{
   if (WiFi.status() == WL_CONNECTED)
   {
       ssidInput = WiFi.SSID();
       Serial.printf("SSID_old: %s\n", WiFi.SSID().c_str());
       Serial.println("******************");
       Serial.printf("IP Address : %s\n",WiFi.localIP().toString().c_str());
       Serial.println("******************");
       String msg = "<!DOCTYPE HTML><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\"><title>Device is Connected</title><link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css\"><style>body {background-size: cover;font-family: 'Arial', sans-serif;color: #333;margin: 0;padding: 0;height: 100%;display: flex;justify-content: center;align-items: center;}.container {display: flex;justify-content: center;align-items: center;height: 100vh;background: rgba(255, 255, 255, 0.8);padding: 20px;border-radius: 10px;}.card {background: rgba(255, 255, 255, 0.9);border-radius: 10px;}h1{font-size: 2rem;margin-bottom: 20px;}h2{font-size: 1.5rem;margin-bottom: 20px;}a.btn {font-size: 1.25rem;padding: 10px 20px;}@media (max-width: 768px) {h1 {font-size: 1.5rem;}h2 {font-size: 1.25rem;}a.btn {font-size: 1rem;padding: 8px 16px;}}@media (max-width: 576px) {h1 {font-size: 1.25rem;}h2 {font-size: 1rem;}a.btn {font-size: 0.875rem;padding: 6px 12px;}}</style></head><body><div class=\"container\"><div class=\"row justify-content-center align-items-center vh-100\"><div class=\"col-md-8 col-lg-6\"><div class=\"card p-4 shadow-lg text-center\"><h1 class=\"text-success\">Refrigerator is Connected to Home Local Network</h1><h2 class=\"text-info\">IP Address : " + WiFi.localIP().toString()+ "</h2><h2 class=\"text-info\">MAC Address : " + WiFi.macAddress()+ "</h2><a target=\"self_\" href=\"/mv/control1\" id=\"home-link\" class=\"btn btn-primary mt-4\">Go to Home</a></div></div></div></div></body></html>";
       server.send(200, "text/html", msg);
   }else 
   {
      Serial.println("###################");
      String str2 = "";
      server.send(200, "text/html", First_WebPage);
   }
}

void HandleNotFound(void) {
  String json = "{\"status\":\"not found\",\"message\":\"ERROR 404: Not Found\"}";
  server.send(404, "application/json", json);
}

void HandleConnect(void) {
  if (server.hasArg("ssid") && server.hasArg("password")) {
    ssidInput = server.arg("ssid");
    passwordInput = server.arg("password");
    Serial.println("Received new SSID and password from client.");
    WiFi.mode(WIFI_AP_STA);
    WiFi.begin(ssidInput.c_str(), passwordInput.c_str());
    delay(1000); // Wait for 5 seconds to allow WiFi connection to establish
    unsigned char attempts = 0;
    while (WiFi.status() != WL_CONNECTED && attempts < 20) {
      delay(500);
      Serial.print(".");
      attempts++;
    }
    if (WiFi.status() == WL_CONNECTED) {
      saveConfig(ssidInput.c_str(), passwordInput.c_str());
      Serial.println("Successfully connected to new WiFi network.");
      String json = "{\"status\":\"connected\",\"ssid\":\"" + ssidInput + "\",\"ip\":\"" + WiFi.localIP().toString() + "\"}";
      server.send(200, "application/json", json);
    } else {
      Serial.println("Failed to connect to new WiFi network.");
      String json = "{\"status\":\"failed to connect\"}";
      server.send(200, "application/json", json);
    }
  } else {
    Serial.println("Received bad request from client. Missing SSID or Password.");
    String json = "{\"status\":\"bad request\",\"message\":\"Missing SSID or Password\"}";
    server.send(400, "application/json", json);
  }
}

void HandleConnect1(void)
{
  if (server.hasArg("ssid") && server.hasArg("password"))
  {
    ssidInput = server.arg("ssid");
    passwordInput = server.arg("password");

    WiFi.mode(WIFI_AP_STA);
    WiFi.begin(ssidInput.c_str(), passwordInput.c_str());

    unsigned char z = 0;
    while(WiFi.status() != WL_CONNECTED)
    {
      delay(500);
      Serial.print(".");
      if (z >= 40)
      {
        server.handleClient();
        if(WiFi.status() == WL_CONNECTED) 
        {
          break;
        }
      }
      z++;
    }
    if(WiFi.status() == WL_CONNECTED) 
    {
       saveConfig(ssidInput.c_str(),passwordInput.c_str());
    }
    Serial.println("\nWiFi Connected.");
    Serial.print("Refrigerator IP Address: ");
    Serial.print(WiFi.localIP());
    Serial.println(" in home local network.");

    macAddress = WiFi.macAddress();
    Serial.println("");
    Serial.print("MAC Address : ");
    Serial.println(macAddress);
    String message = "<!DOCTYPE HTML><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\"><title>Refrigerator Web Server</title><link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css\"><style>body {background-size: cover;font-family: 'Arial', sans-serif;color: #333;margin: 0;padding: 0;height: 100%;display: flex;justify-content: center;align-items: center;}.container {display: flex;justify-content: center;align-items: center;height: 100vh;background: rgba(255, 255, 255, 0.8);padding: 20px;border-radius: 10px;}.card {background: rgba(255, 255, 255, 0.9);border-radius: 10px;}h1 {font-size: 2rem;margin-bottom: 20px;}p {font-size: 1.25rem;margin-bottom: 10px;}a.btn {font-size: 1.25rem;padding: 10px 20px;}@media (max-width: 768px) {h1 {font-size: 1.5rem;}p {font-size: 1rem;}a.btn {font-size: 1rem;padding: 8px 16px;}}@media (max-width: 576px) {h1 {font-size: 1.25rem;}p {font-size: 0.875rem;}a.btn {font-size: 0.875rem;padding: 6px 12px;}}</style></head><body><div class=\"container\"><div class=\"row justify-content-center align-items-center vh-100\"><div class=\"col-md-8 col-lg-6\"><div class=\"card p-4 shadow-lg text-center\"><h1 class=\"text-center text-danger\"><strong>Network Information</strong></h1><p>Connected to network:</p><p>SSID: <span id=\"ssid\">"+ ssidInput + "</span></p><p>IP Address: <span id=\"ip-address\">" + WiFi.localIP().toString() + "</p></span><br><p>MAC Address: <span id=\"ip-address\">" + WiFi.macAddress() + "</p></span><a target=\"self_\" href=\"/mv/control1\" class=\"btn btn-primary\">Go to Home</a></div></div></div></div></body></html>";
    server.send(200, "text/html", message);
  } 
  else
  {
    server.send(400, "Bad Request", "Missing SSID or Password");
  }
}

void HandleControl(void) {
  String json = "{\"status\":\"success\",\"message\":\"Enter Monitor mode\"}";
  server.send(200, "application/json", json);
}

void HandleControl1(void) {
    String message = "<!DOCTYPE html><html lang=\"fa\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>مانیتور یخچال و فریزر</title><style>body { font-family: 'Arial', sans-serif;background-color: #f0f8ff;margin: 0; padding: 0;display: flex;justify-content: center;align-items: center;height: 100vh;}.container {text-align: center;background-color: #fff;border-radius: 15px;box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);padding: 30px;width: 350px; }h1 {font-size: 24px;color: #333; }.button {display: inline-block;margin: 10px;  padding: 15px 30px;font-size: 16px;color: #fff;background-color: #007bff;border: none;border-radius: 8px;cursor: pointer;transition: background-color 0.3s ease; }.button:hover { background-color: #0056b3;}</style></head><body><div class=\"container\"><h1>مانیتورینگ یخچال و فریزر</h1><button class=\"button\" onclick=\"monitor('MonitorTemps')\">مانیتور دما</button><button class=\"button\" onclick=\"monitor('MonitorDoors')\">مانیتور وضعیت در</button></div><script> function monitor(action) {// ایجاد یک فرم مخفی برای ارسال دادهlet form = document.createElement(\"form\"); form.method = \"POST\"; form.action = \"/atoe\";let input = document.createElement(\"input\"); input.type = \"hidden\";input.name = action;input.value = true; form.appendChild(input);document.body.appendChild(form);// ارسال فرم form.submit();}</script></body></html>";
    server.send(200, "text/html", message);
}

void Monitoring(void)
{
  digitalWrite (D0 , LOW);  //C
  digitalWrite (D1 , LOW);  //B
  digitalWrite (D2 , LOW);  //A
  delay (10);
  sensor=analogRead(A0);
  Serial.println("freeze=");
  Serial.println(array_fast_temp[sensor]);
  FreezeTemp = String(array_fast_temp[sensor]);

  digitalWrite (D0 , LOW); //C
  digitalWrite (D1 , HIGH); //B
  digitalWrite (D2 , HIGH);  //A
  delay (10);
  sensor=analogRead(A0);
  Serial.println("fridge=");
  Serial.println(array_fast_temp[sensor]);
  FridgeTemp = String(array_fast_temp[sensor]);

  if(digitalRead(D5) == true )
  {
      Serial.println("freeze door is close");
      FreezeDoor = "close";
  }else if(digitalRead(D5) == false )
      {
        Serial.println("freeze door is open");
        FreezeDoor = "open";
      }
    
  if(digitalRead(D6) == true )
  {
    Serial.println("fridge door is close");
    FridgeDoor = "close";
  }else if(digitalRead(D6) == false )
    {
      Serial.println("fridge door is open");
      FridgeDoor = "open";
    }
}

void HandleMonitorTemps(void)
{
  String json1 = "{\"status\":\"success\", \"FridgeTemp\" : " + FridgeTemp + ", \"FreezeTemp\" : " + FreezeTemp + "}";
  server.send(200, "application/json", json1); // MUST HAVE
}

void HandleMonitorDoors(void)
{
  String json2 = "{\"status\":\"success\", \"FridgeDoor\" : \"" + FridgeDoor + "\", \"FreezeDoor\" : \"" + FreezeDoor + "\"}";
  server.send(200, "application/json", json2); // MUST HAVE
}

void HandleAtoE(void) {
  if (server.hasArg("MonitorTemps")) {
    HandleMonitorTemps();
  } else if (server.hasArg("MonitorDoors")) {
    HandleMonitorDoors();
  } 
}



void HandleLock(void)
{
  // Implement the logic for the lock command
  // For example, control a GPIO pin or perform some action
  
  // Example: Toggle a pin to lock the appliance
  digitalWrite(D0, HIGH);  // Assuming LOCK_PIN is defined and connected appropriately
  delay(1000);
  digitalWrite(D0, LOW);

  // Send a response back to the client
  String json = "{\"status\":\"success\",\"message\":\"Appliance locked\"}";
  server.send(200, "application/json", json);
}

void HandleZone(void)
{
    // Implement the logic for the zone command
    // For example, toggle a pin or perform an action
    // Here, we'll just print to Serial and send a success response

    Serial.println("Zone command received");
    // Add your specific logic here

    String json = "{\"status\":\"success\",\"message\":\"Zone command executed\"}";
    server.send(200, "application/json", json);
}

void HandleFunction(void)
{
    // Implement the logic for the function command
    Serial.println("Function command received");
    // Add your specific logic here

    String json = "{\"status\":\"success\",\"message\":\"Function command executed\"}";
    server.send(200, "application/json", json);
}

void HandleTemp(void)
{
    // Implement the logic for the temp command
    Serial.println("Temp command received");
    // Add your specific logic here

    String json = "{\"status\":\"success\",\"message\":\"Temp command executed\"}";
    server.send(200, "application/json", json);
}





void loop() {
  server.handleClient();                         // Listen for HTTP Requests from Clients
}
