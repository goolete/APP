#include <IAP15.h>
#include <intrins.h>
#include <sys.h>
#include <lcd.h>
#include <font.h>
#include <ESP8266.h>

void IFInit();
void InttoString(unsigned int a,unsigned char *p);
void UChartoString(unsigned char a,unsigned char *c);

unsigned char j[5] = {48,49,56,53,'\0'};
void main()
{
	unsigned int gp = 0;
	unsigned int hx = 40,hy = 0;
	unsigned char s[6] = {0};
	Lcd_Init();
	UartInit();
	IFInit();
	POINT_COLOR = BLUE;
	BACK_COLOR = WHITE;
	delayms(2000);
	
	ESP_8266_Init();
	while(1)
	{
		InttoString(getDate(0),s);
		LCD_ShowString(80,16,s);
		InttoString(getDate(1),s);
		LCD_ShowString(130,16,s);
		InttoString(getDate(2),s);
		LCD_ShowString(180,16,s);
		InttoString(getDate(3),s);
		LCD_ShowString(230,16,s);
		
		UChartoString(getR(),s);
		LCD_ShowString(80,32,s);
		InttoString(getRad(),s);
		LCD_ShowString(80,48,s);
		UChartoString(getProgress(0),s);
		LCD_ShowString(80,64,s);
		UChartoString(getProgress(1),s);
		LCD_ShowString(130,64,s);
		UChartoString(getProgress(2),s);
		LCD_ShowString(180,64,s);
		UChartoString(getProgress(3),s);
		LCD_ShowString(230,64,s);
		
		UChartoString(getK(0),s);
		LCD_ShowString(0,112,s);
		UChartoString(getK(1),s);
		LCD_ShowString(40,112,s);
		UChartoString(getK(2),s);
		LCD_ShowString(80,112,s);
		UChartoString(getK(3),s);
		LCD_ShowString(120,112,s);
		UChartoString(getK(4),s);
		LCD_ShowString(160,112,s);
		UChartoString(getK(5),s);
		LCD_ShowString(200,112,s);
		UChartoString(getK(6),s);
		LCD_ShowString(240,112,s);
		UChartoString(getK(7),s);
		LCD_ShowString(280,112,s);
	}
	
	
}

void IFInit()
{
	LCD_Clear(WHITE);
	POINT_COLOR=GREEN;
	BACK_COLOR = WHITE;
	LCD_ShowString(0,0,"Hello");
	LCD_ShowString(0,16,"data     =");
	
	LCD_ShowString(0,32,"R        =");
	
	LCD_ShowString(0,48,"Rad      =");
	
	LCD_ShowString(0,64,"Progress =");
	
	POINT_COLOR=RED;
	LCD_ShowString(0,96,"K =");
}
void InttoString(unsigned int a,unsigned char *c)
{
	char i ;
	for(i = 0;i<5;i++){c[i]=0;}
	i = 4;
	for(;a!=0;i--)
	{
		c[i] = a%10;
		a /= 10;
	}
	for(i = 0;i<5;i++){c[i]+=48;}
	c[5] = '\0';
}

void UChartoString(unsigned char a,unsigned char *c)
{
	char i ;
	for(i = 0;i<5;i++){c[i]=0;}
	i = 2;
	for(;a!=0;i--)
	{
		c[i] = a%10;
		a /= 10;
	}
	for(i = 0;i<5;i++){c[i]+=48;}
	c[3] = '\0';
}