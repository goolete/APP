#ifndef _ESP8266_H
#define _ESP8266_H

extern unsigned char Buf[100];
extern unsigned int hp;
extern char cou;

void UartInit(void);
void Send_String(unsigned char *a);

void ESP_8266_Init();
unsigned int getDate(unsigned char c);
unsigned char getR();
unsigned int getRad();
unsigned char getProgress(unsigned char c);
unsigned char getK(unsigned char c);

#endif
