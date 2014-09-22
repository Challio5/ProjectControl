// Project Control
// Door: Martijn de Vries, Tom Voshaar, Laurens Braam en Rob Bonhof

/* 
Hardware
ALPS STEC12E08 rotary encoder
NXP 74HC595 shift register
NXP 74HC164 shift register
KINGBRIGHT SC56-11GWA 7-segment display
Button
*/

/*
Implementatie rottary ecoder
Een draai aan de rotary encoder wordt geregistreerd door interrupts
Elke verandering (omhoog/omlaag) in zowel de a of b golf heeft een eigen interrupt methode
Dus per draai worden er vier verandering gedetecteerd en geregistreerd in een counter
Per interrupt kan worden bepaald welke kant er op gedraaid wordt door de golven te vergelijken
Een vierde van deze counter (1 draai) wordt gebruikt voor het draaien van de code
Ook wordt alleen het laatste cijfer in het getal gebruikt zodat alleen de cijfers 1 t/m 9 gebruikt worden
*/

/*
Implementatie shift register
*/

// Servo bibliotheek voor het gebruik van de servo
#include <Servo.h> 

// Pin setup voor rotary encoder (Interrupts)
const int pinA = 2;   // Blauw
const int pinB = 3;   // Geel

// Pin setup voor de knop
const int button = 5;

// Pinsetup voor servo
int servoPin = 4;

// Pin setup voor de shifters
const int clockPin164 = 6;    // Deze pin is verbonden met pin 8 van de 164 shifter (De clockpin)
const int dataPin164  = 7;    // Deze pin is verbonden met pin 2 van de 164 shifter (De datapin)
const int clockPin    = 8;    // Deze pin is verbonden met Pin 11 van de 595 shifter (De clockpin)
const int latchPin    = 9;    // Deze pin is verbonden met Pin 12 van de 595 shifter (De latchpin)
const int dataPin     = 10;   // Deze pin is verbonden met Pin 14 van de 595 shifter (De datapin)
const int clockPin2   = 11;   // Deze pin is verbonden met Pin 11 van de 595 shifter (De clockpin)
const int latchPin2   = 12;   // Deze pin is verbonden met Pin 12 van de 595 shifter (De latchpin)
const int dataPin2    = 13;   // Deze pin is verbonden met Pin 14 van de 595 shifter (De datapin)

// Counters voor het aantal transities van de encoder en voor het aantal draaien
int counter = 1000;
int output;

// Waarden van de knop met het aantal keren deze is ingedrukt
int buttonPressed;
int buttonCounter;

// Waarden van de knop voor het debouncen
int reading;
int lastReading;
int debounceTimer;
int debounceTime = 50;

// Servo variabele met variabele voor of de kluis open of dicht is
Servo servo;  
int lock = 0; 

// Led variabelen voor de groene en rode led om de staat van de kluis aan te geven
int redLed = A0;
int greenLed = A1;

// String en array voor het opslaan van inkomende data met flag of deze compleet is
String inputString = "";
boolean stringComplete = false;
String input[10];
int ingevuldeCode[] = {0,0,0};
int code[] = {1,2,3};
boolean opened = false;
boolean nieuweCode = false;

// Array met bytes voor de shifters om de cijfers te representeren
// Index komt overeen met het getal wat wordt weergegeven op de displays
const byte numbers[10] = {
  // Pinnummer .......
  // Q- Poort  0  1  2  3  4  5  6  7
  // Segment   DP A  B  C  D  E  F  G
  
  // Binair    1, 0, 0, 0, 0, 0, 0, 1
  0x81,
  // Binair    1, 1, 0, 0, 1, 1, 1, 1
  0xCF,
  // Binair    1, 0, 0, 1, 0, 0, 1, 0  
  0x92,
  // Binair    1, 0, 0, 0, 0, 1, 1, 0
  0x86,
  // Binair    1, 1, 0, 0, 1, 1, 0, 0
  0xCC,
  // Binair    1, 0, 1, 0, 0, 1, 0, 0
  0xA4,
  // Binair    1, 0, 1, 0, 0, 0, 0, 0
  0xA0,
  // Binair    1, 0, 0, 0, 1, 1, 1, 1
  0x8F,
  // Binair    1, 0, 0, 0, 0, 0, 0, 0
  0x80,
  // Binair    0, 0, 0, 0, 0, 1, 0, 0
  0x04,
};


void setup() { 
  // Pin A en B als input gebruiken voor het uitlezen van de rotary
  pinMode(pinA, INPUT);
  pinMode(pinB, INPUT);
  
  // Button
  pinMode(button, INPUT);
  
  // Overige pinnen als output gebruiken voor het aansturen van de shifters
  pinMode(clockPin164, OUTPUT);
  pinMode(dataPin164,  OUTPUT);
  pinMode(latchPin,    OUTPUT);
  pinMode(clockPin,    OUTPUT);
  pinMode(dataPin,     OUTPUT);
  pinMode(latchPin2,   OUTPUT);
  pinMode(clockPin2,   OUTPUT);
  pinMode(dataPin2,    OUTPUT);
  
  // Rode en groende Led voor de status van de kluis (Standaard rood)
  pinMode(redLed, OUTPUT);
  pinMode(greenLed, OUTPUT);
  digitalWrite(redLed, HIGH);
  
  // Intterrupts voor transitie in rotary encoder
  attachInterrupt(0, pinAOmhoog, RISING);
  attachInterrupt(1, pinBOmhoog, RISING);
  
  // Koppelt de servo variabele aan de juiste pin
  servo.attach(servoPin);
  closeLock();
  
  // Zet de displays op nul
  digitalWrite(latchPin,LOW);
  shiftOut(dataPin, clockPin, LSBFIRST, numbers[0]);
  digitalWrite(latchPin, HIGH);
  
  digitalWrite(latchPin2,LOW);
  shiftOut(dataPin2, clockPin2, LSBFIRST, numbers[0]);
  digitalWrite(latchPin2, HIGH);
  
  shiftOut(dataPin164, clockPin164, LSBFIRST, numbers[0]);
  
  // Setup seriele communicatie
  Serial.begin(9600);
  Serial.flush();
} 

void loop() {
  // Neemt een vierde (1 draai) en vervolgens het laatste cijfer van de counter
  output = (counter / 4) % 10;
    // Print de cijfers op het juiste 7segment display
  printer();
   getalOpslaan();
    
  
  // Leest de waarde van de button in
  reading = digitalRead(button);
  
  // Checkt of er een verandering is door debouncing
  if (buttonPressed != lastReading) {
    // Reset de timer voor debouncing
    debounceTimer = millis();
  } 
  
  
  // Checkt of het verschil de huidige tijd en de timer groter is de dan de debouncetijd
  if((millis() - debounceTimer) > debounceTime) {
    
    // Checkt of er verandering is
    if(reading != buttonPressed) {
      buttonPressed = reading;
      
      // Checkt of de knop is ingedrukt en slaat het aantal keren op
      if(buttonPressed) {
        // Verhoogt het aantal knopdrukken met 1
        buttonCounter++;
        
        
        // Reset de counter, zodat het display weer op nul staat
        counter = 1000;

        // Als de teller hoger of gelijk aan 3 is, wordt deze gereset
        if(buttonCounter == 3 && !nieuweCode) {
          testCode();
          
          if(opened){
            openLock();
          }
        }
        //als de counter 3 is en er moet een nieuweCode worden ingevoerd
        if(buttonCounter == 3 && nieuweCode){
          setCode();
          
        }
        if(buttonCounter == 4 && opened){
          int buttonDelay = 0;
          //Zolang de button wordt ingeklikt. als hij langer dan 1 sec is ingeklikt gaat breakt hij automatisch
          while(digitalRead(button) == HIGH){
            buttonDelay++;
            delay(100);
            if(buttonDelay > 10){
              buttonDelay = 0;
              askCode();
              nieuweCode = true;
              break;
            }
          }
        }
        
        //Als de buttoncounter 4 is zal hij de kluis weer sluiten en resetten
        if(buttonCounter == 4 && !nieuweCode){
          closeLock();
          resetGetallen();
          buttonCounter = 0;
        }   
      }
    }
  }
  // Slaat de reading op zodat een nieuwe reading hiermee vergeleken kan worden
  lastReading = reading;
    
  // Checkt op binnenkomende commando's van de Java GUI.
  if (stringComplete) {
    handleCommand();
    inputString = "";
    stringComplete = false;
  }
}
//Methode om de binnenkomende commands te behandelen
void handleCommand(){
  if(input[0] == "Open"){
      buttonCounter = 0;
      counter = input[1].toInt() + 1000;
      ingevuldeCode[0] = input[1].toInt();
      printDisplay1(input[1].toInt());
      ingevuldeCode[1] = input[2].toInt();
      printDisplay2(input[2].toInt());
      ingevuldeCode[2] = input[3].toInt();
      printDisplay3(input[3].toInt());
      testCode();
      if(opened){
        openLock();
        buttonCounter = 3;
        Serial.println("Open true");
      } else {
        Serial.println("Open false");
      }
    } 
    if(input[0] == "Ingevulde"){
      buttonCounter = 0;
      counter = input[1].toInt() + 1000;
      ingevuldeCode[0] = input[1].toInt();
      printDisplay1(input[1].toInt());
      ingevuldeCode[1] = input[2].toInt();
      printDisplay2(input[2].toInt());
      ingevuldeCode[2] = input[3].toInt();
      printDisplay3(input[3].toInt());
    }
    if(input[0] == "SetCode"){
      if(opened){
       nieuweCode = true;
       askCode();
       Serial.println("SetCode true");
      } else {
      Serial.println("SetCode false");
      }     
    }
    if(input[0] == "GetCode"){
      String teprinten = "Code " + String(code[0]) + " " +String(code[1])+ " " +String(code[2]);
      Serial.println(teprinten);
    }
}
//set een nieuwe code
void setCode(){
  for(int i = 0; i<3;i++){
    code[i] = ingevuldeCode[i];
  }
  nieuweCode = false;
}

//Gebruikt zodat de gebruiker weet dat hij een nieuwe code kan invoeren
void askCode(){
  printDisplay1(1);
  delay(100);
  printDisplay1(2);
  delay(100);
  resetGetallen();
  buttonCounter = 0;
}
//reset de getallen op de displays en ingevuldeCode
void resetGetallen(){
  printDisplay1(0);
  printDisplay2(0);
  printDisplay3(0);
  int ingevuldeCode[] = {0,0,0};
  }

// Vergelijkt de ingevoerde code met de actuele code
void testCode(){
  int teller = 0;
  int i = 0;
  while(i < 3 && !opened){
    if(code[i] == ingevuldeCode[i]){
      teller++;
      if(teller == 3){
        opened = true;
        teller = 0;
      }
    }
    i++;
  }
}
    
//Zet het getal op de juiste plek in ingevulde code
//Dit weet hij door het aantal keren dat de knop is ingedrukt
void getalOpslaan(){
   if(buttonCounter == 0) {
    ingevuldeCode[0] = output;
  }
  if(buttonCounter == 1) {
    ingevuldeCode[1] = output;
  }
  if(buttonCounter == 2) {
    ingevuldeCode[2] = output;
  }
    // Stuur het cijfer door naar de Java GUI
    String teprinten = "Rotary "+String(buttonCounter)+" "+String(output);
    Serial.println(teprinten);
}

// Methode om de kluis te openen
void openLock() {
  servo.write(90);
  digitalWrite(greenLed, HIGH);
  digitalWrite(redLed, LOW);
}

// Methode om de kluis te sluiten
void closeLock() {
  servo.write(0);
  opened = false;
  digitalWrite(greenLed, LOW);
  digitalWrite(redLed, HIGH);
}

// Roept de juist methode om de output op te printen
// Gelijk aan het aantal keren de knop is ingedrukt
void printer() {
  if(buttonCounter == 0) {
    printDisplay1(output);
  }
  if(buttonCounter == 1) {
    printDisplay2(output);
  }
  if(buttonCounter == 2) {
    printDisplay3(output);
  }
}

// Geeft het eerst gedraaide getal weer op het 7segment-display
void printDisplay1(int input) {
  digitalWrite(latchPin,LOW);
  shiftOut(dataPin, clockPin, LSBFIRST, numbers[input]);
  digitalWrite(latchPin, HIGH);
}

// Geeft het tweede gedraaide getal weer op het 7segment-display
void printDisplay2(int input) {
  digitalWrite(latchPin2,LOW);
  shiftOut(dataPin2, clockPin2, LSBFIRST, numbers[input]);
  digitalWrite(latchPin2, HIGH);
}

// Geeft het eerst gedraaide getal weer op het 7segment-display
void printDisplay3(int input) {
  shiftOut(dataPin164, clockPin164, LSBFIRST, numbers[input]);
}

// Serialevent voor het wegschrijven van inkomende data
void serialEvent(){
  while (Serial.available()) {
    // pakt een nieuwe byte
    char inChar = (char)Serial.read(); 
    // als de volgende character is een newline, zegt hij dat de string klaar is.
    if (inChar == '\n') {
      toArray(inputString, ' ');
      stringComplete = true;
    } 
    else {
      // plaats de char bij de inputstring
      inputString += inChar;
    }
  }
}

// Split de string in aparte woorden en schrijft deze weg in een array
void toArray(String data, char separator){
  int found = 0;
  int strIndex[] = { 0, -1  };
  int maxIndex = data.length()-1;
  int number = count(data,separator);
  if(!(number>10)){ //controleren of het past in de van tevoren aangemaakte array
    for(int i=0; i<=maxIndex; i++){
      if(data.charAt(i)==separator || i==maxIndex){
        strIndex[0] = strIndex[1]+1;
        if(i == maxIndex){
          strIndex[1] = i+1;
        } 
        else {
          strIndex[1] = i;
        }
        input[found] = data.substring(strIndex[0], strIndex[1]);
        found++;
      }
    }
  }else{
    Serial.println(" teveel woorden");
  }
}

// Checkt op het aantal woorden in een string
int count(String data, char separator){
  int aantal = 0;
  int maxIndex = data.length() -1;
  for(int i = 0; i<=maxIndex ; i++){
    if(data.charAt(i) == separator || i == maxIndex){
      aantal ++;
    }
  }
  return aantal;
}

// Voor als de pinA van laag naar hoog gaat
void pinAOmhoog(){
  // Ontkoppelt interrupt zodat de afhandeling van de methode niet wordt gestoord
  detachInterrupt(0);
  
  // Het uitlezen van de pin
  int lezingPinB = digitalRead(pinB);
  
  // Checkt de waarde van de pin
  // Als pinB hoog is wordt er met de klok mee gedraaid
  if(lezingPinB){
    counter--;
  }
  // Als pinB laag is wordt er tegen de klok in gedraaid
  if(!lezingPinB){
    counter++;
  }
  
  // Interrupt voor als de golf waar omlaag gaat
  attachInterrupt(0, pinAOmlaag, FALLING);
}

// Voor als de pinA van hoog naar laag gaat
void pinAOmlaag(){  
  // Ontkoppelt interrupt zodat de afhandeling van de methode niet wordt gestoord
  detachInterrupt(0);
  
  // Het uitlezen van de pin
  int lezingPinB = digitalRead(pinB);
  
  // Checkt de waarde van de pin
  // Als pinB hoog is wordt er met de klok mee gedraaid
  if(lezingPinB){
    counter++;
  }
  // Als pinB laag is wordt er tegen de klok in gedraaid
  if(!lezingPinB){
    counter--;
  }
  
  // Interrupt voor als de golf waar omhoog gaat
  attachInterrupt(0, pinAOmhoog, RISING);
}

// Voor als de pinB van laag naar hoog gaat
void pinBOmhoog(){
  // Ontkoppelt interrupt zodat de afhandeling van de methode niet wordt gestoord
  detachInterrupt(1);
  
  // Het uitlezen van de pin
  int lezingPinA = digitalRead(pinA);
  
  // Checkt de waarde van de pin
  // Als pinA hoog is wordt er tegen de klok in gedraaid
  if(lezingPinA){
    counter++;
  }
  // Als pinA laag is wordt er met de klok mee gedraaid
  if(!lezingPinA){
    counter--;
  }
  
  // Interrupt voor als de golf waar omlaag gaat
  attachInterrupt(1, pinBOmlaag, FALLING);
}

// Voor als de pinB van hoog naar laag gaat
void pinBOmlaag(){
  // Ontkoppelt interrupt zodat de afhandeling van de methode niet wordt gestoord
  detachInterrupt(1);
  
  // Het uitlezen van de pin
  int lezingPinA = digitalRead(pinA);
  
  // Checkt de waarde van de pin
  // Als pinA hoog is wordt er tegen de klok in gedraaid
  if(lezingPinA){
    counter--;
  }
  // Als pinA laag is wordt er met de klok mee gedraaid
  if(!lezingPinA){
    counter++;
  }
  
  // Interrupt voor als de golf waar omhoog gaat
  attachInterrupt(1, pinBOmhoog, RISING);
}


