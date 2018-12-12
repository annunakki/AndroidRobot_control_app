/*
 * T3I PROJECT
 */
   
#include <Servo.h>
#include <pt.h>

Servo servoLR;           //brown
Servo servoUD;           //orange

//String data = 0;            //Variable for storing received data

#define in1 6             //green
#define in2 7             //yellow
#define in3 4             //blue
#define in4 5             //orange

static struct pt pt1, pt2, pt3; // each thread needs one of these

String array[5]; //store all the value coming from the Bluetooth

String prev; //Memorize the previous String from the Stream

int currentLR = 90; //starting position of servo   Left and Right
int currentUD = 90;  //starting position of servo Up and Down

void setup() {

  Serial.begin(9600);   //Sets the baud for serial data transmission  

/*
 * Initialize the threads  
 */
 
  PT_INIT(&pt1);  
  PT_INIT(&pt2); 
  PT_INIT(&pt3);  

  /*
   * Pin for motor driver
   */
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);
  
  /*
   * Servo Left & Right motor pin
   */ 
  servoLR.attach(9);
  
  /*
   * Servo Up & Down motor pin
   */
  servoUD.attach(8);


  servoLR.write(90);
  servoUD.write(90);

}

void moveforward()
{
  //Left wheel pins settings
  digitalWrite(in1, HIGH);
  digitalWrite(in3, LOW);
  //Right wheel pins settings
  digitalWrite(in2, HIGH);
  digitalWrite(in4, LOW);
}

void movebackward() {
  //Left wheel pins settings
  digitalWrite(in1, LOW);
  digitalWrite(in3, HIGH);
  //Right wheel pins settings
  digitalWrite(in2, LOW);
  digitalWrite(in4, HIGH);
}

void turnleft()
{
  //Left wheel pins settings
  digitalWrite(in1, LOW);
  digitalWrite(in3, HIGH);
  //Right wheel pins settings
  digitalWrite(in2, HIGH);
  digitalWrite(in4, LOW);
}

void turnright()
{
  //Left wheel pins settings
  digitalWrite(in1, HIGH);
  digitalWrite(in3, LOW);
  //Right wheel pins settings
  digitalWrite(in2, LOW);
  digitalWrite(in4, HIGH);
}

void stopmove()
{
  //Left wheel pins settings
  digitalWrite(in1, LOW);
  digitalWrite(in3, LOW);
  //Right wheel pins settings
  digitalWrite(in2, LOW);
  digitalWrite(in4, LOW);
}

void loop() {
  String data;  //Recived data will get stored in this variable

  if(Serial.available()) { //Here We're checking whether data is available or not
    data = Serial.readStringUntil(' '); //Data received
    //   Serial.println(data);
    split(data);
    for(int i = 0; i<5; i++)
      Serial.println(array[i]);  

    if(array[0] == "F" || array[0] == "B" || array[0] == "R" || array[0] == "L" || array[0] == "S"){
      prev = array[0];
    }
    movethread(&pt1);
    if( array[0] == "T"){
      moveservoLR();
      moveservoUD();
    }
  }
}


static int movethread(struct pt *pt) {
  static unsigned long timestamp = 0;
  PT_BEGIN(pt);
  while(1) { // never stop 
    PT_WAIT_UNTIL(pt, millis() - timestamp > 100 );
    timestamp = millis(); // take a new timestamp
    if(prev=="F") {
      moveforward();
    }

    else if(prev== "B") {
      movebackward();
    }

    else if(prev=="R") {
      turnright();
    }

    else if(prev== "L") {
      turnleft();
    }

    else {
      stopmove();
    }  
  }
  PT_END(pt);
}
static void split(String text){
  char buf[900];
  text.toCharArray(buf, sizeof(buf));
  char *p = buf;
  char *str;
  int i = 0;

  while ((str = strtok_r(p, ":", &p)) != NULL){ // delimiter is the semicolon
    array[i] = str;
    ++i;
  }
}
static int servothreadLR(struct pt *pt) {
  static unsigned long timestamp = 0;
  PT_BEGIN(pt);
  while(1) { // never stop 
    PT_WAIT_UNTIL(pt, millis() - timestamp > 100 );
    timestamp = millis(); // take a new timestamp;

    int pos = map(array[4].toInt(), -180, 180, 0, 180);
    servoLR.write(pos);

  } 
  PT_END(pt);
}

static int servothreadUD(struct pt *pt) {
  static unsigned long timestamp = 0;
  PT_BEGIN(pt);
  while(1) { // never stop 
    PT_WAIT_UNTIL(pt, millis() - timestamp > 100 );
    timestamp = millis(); // take a new timestamp

    int pos = map(array[3].toInt(), -90, 90, 0, 180);
    servoUD.write(pos);

  }
  PT_END(pt);
}

static void moveservoLR(){
  int pos = map(array[4].toInt(), -180, 180, 0, 180);
  if (pos > currentLR)
    for (int i = currentLR; i <= pos; i++){
      servoLR.write(i);
      delay(1 - array[1].toInt());
      }
  else
    for (int i = currentLR; i > pos; i--){
      servoLR.write(i);
    delay(1 - array[1].toInt());
    }
    currentLR = pos;
}

static void moveservoUD(){
  int pos = map(array[3].toInt(), -90, 90, 0, 180);
  if (pos > currentUD)
    for (int i = currentUD; i <= pos; i++){
      servoLR.write(i);
    delay(1 - array[2].toInt());
    }
  else
    for (int i = currentUD; i > pos; i--){
      servoLR.write(i);
    delay(1 - array[2].toInt());
    }
    currentUD = pos;
}

/*Code for camera
 NOT WORKING PROPERTELY WITH BLUWTOOTH
 */

//void start_capture(){
//  myCAM.clear_fifo_flag();
//  myCAM.start_capture();
//}
//
//void camCapture(ArduCAM myCAM){
//  WiFiClient client = server.client();
//
//  size_t len = myCAM.read_fifo_length();
//  if (len >= 0x07ffff){
//    Serial.println("Over size.");
//    return;
//  }
//  else if (len == 0 ){
//    Serial.println("Size is 0.");
//    return;
//  }
//
//  myCAM.CS_LOW();
//  myCAM.set_fifo_burst();
//#if !(defined (ARDUCAM_SHIELD_V2) && defined (OV2640_CAM))
//  SPI.transfer(0xFF);
//#endif
//
//  static const size_t bufferSize = 4096;
//  static uint8_t buffer[bufferSize] = {
//    0xFF  };
//
//  while (len) {
//    size_t will_copy = (len < bufferSize) ? len : bufferSize;
//    SPI.transferBytes(&buffer[0], &buffer[0], will_copy);
//    Serial.write(&buffer[0], will_copy); //Send Picture Through Bluetooth
//    len -= will_copy;
//  }
//
//  myCAM.CS_HIGH();
//}
//
//void serverCapture(){
//  start_capture();
//  Serial.println("CAM Capturing");
//
//  int total_time = 0;
//
//  total_time = millis();
//  while (!myCAM.get_bit(ARDUCHIP_TRIG, CAP_DONE_MASK));
//  total_time = millis() - total_time;
//  Serial.print("capture total_time used (in miliseconds):");
//  Serial.println(total_time, DEC);
//
//  total_time = 0;
//
//  Serial.println("CAM Capture Done!");
//  total_time = millis();
//  camCapture(myCAM);
//  total_time = millis() - total_time;
//  Serial.print("send total_time used (in miliseconds):");
//  Serial.println(total_time, DEC);
//  Serial.println("CAM send Done!");
//}
//
//void serverStream(){
//  while (1){
//    start_capture();
//
//    while (!myCAM.get_bit(ARDUCHIP_TRIG, CAP_DONE_MASK));
//
//    size_t len = myCAM.read_fifo_length();
//    if (len >= 0x07ffff){
//      Serial.println("Over size.");
//      continue;
//    }
//    else if (len == 0 ){
//      Serial.println("Size is 0.");
//      continue;
//    }
//
//    myCAM.CS_LOW();
//    myCAM.set_fifo_burst();
//    
//#if !(defined (ARDUCAM_SHIELD_V2) && defined (OV2640_CAM))
//    SPI.transfer(0xFF);
//#endif
//
//    static const size_t bufferSize = 4096;
//    static uint8_t buffer[bufferSize] = {
//      0xFF    };
//
//    while (len) {
//      size_t will_copy = (len < bufferSize) ? len : bufferSize;
//      SPI.transferBytes(&buffer[0], &buffer[0], will_copy);
//      if (!client.connected()) break;
//      Serial.write(&buffer[0], will_copy); //Send The Stream Through BLUETOOTH
//      len -= will_copy;
//    }
//    myCAM.CS_HIGH();
//
//}
